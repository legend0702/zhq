package cn.zhuhongqing.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import cn.zhuhongqing.exception.ZHQRuntimeException;

/**
 * 
 * Some utilities for Collection.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

@SuppressWarnings("rawtypes")
public class CollectionUtils {

	public static final Class<Map> MAP_CLASS = Map.class;
	public static final Class<Collection> COLLECTION_CLASS = Collection.class;
	public static final Class<List> List_CLASS = List.class;
	public static final Class<ConcurrentMap> CONCURRENT_MAP_CLASS = ConcurrentMap.class;

	@SuppressWarnings("unchecked")
	public static <C extends Collection<G>, G> C create(final Class<C> colClass, final Class<G> gClass) {
		C returnCol = null;
		try {
			returnCol = colClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			if (Set.class.equals(colClass)) {
				returnCol = (C) new LinkedHashSet<G>();
			} else if (List.class.equals(colClass)) {
				returnCol = (C) new ArrayList<G>();
			} else if (Queue.class.equals(colClass)) {
				returnCol = (C) new ArrayDeque<G>();
			} else if (Deque.class.equals(colClass)) {
				returnCol = (C) new LinkedList<G>();
			}
			throw new ZHQRuntimeException(e);
		}
		return returnCol;
	}

	public static boolean isEmpty(Collection<?> models) {
		return (models == null || models.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> models) {
		return (models == null || models.isEmpty());
	}

	public static <T> boolean isStrContains(Collection<String> strCol, String str) {
		for (String strC : strCol) {
			if (str.toUpperCase().contains(strC))
				return true;
		}
		return false;
	}
	
	/**
	 * Delegates to {@link Collection#contains}. 
	 * Returns {@code false} if the {@code contains} method throws a {@code ClassCastException} or {@code NullPointerException}.
	 */
	public static boolean safeContains(Collection<?> collection, Object object) {
		try {
			return collection.contains(object);
		} catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}
	
	public static boolean safeContains(Collection<?> collection, Collection<?> objs) {
		return safeContains(collection, objs.iterator());
	}
	
	public static boolean safeContains(Collection<?> collection, Iterator<?> iterator) {
		boolean changed = false;
		while (iterator.hasNext()) {
			changed |= safeContains(collection, iterator.next());
		}
		return changed;
	}

	/**
	 * Delegates to {@link Collection#remove}. 
	 * Returns {@code false} if the {@code remove} method throws a {@code ClassCastException} or {@code NullPointerException}.
	 */
	public static boolean safeRemove(Collection<?> collection, Object object) {
		try {
			return collection.remove(object);
		} catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}
	
	/** Remove each element in a removeElements from a collection. */
	public static boolean safeRemove(Collection<?> collection, Collection<?> removeElements) {
		return safeContains(collection, removeElements.iterator());
	}
	
	/** Remove each element in an iterable from a collection. */
	public static boolean safeRemove(Collection<?> collection, Iterator<?> iterator) {
		boolean changed = false;
		while (iterator.hasNext()) {
			changed |= safeRemove(collection, iterator.next());
		}
		return changed;
	}
	
	/** Used to avoid http://bugs.sun.com/view_bug.do?bug_id=6558557 */
	public static <T> Collection<T> cast(Iterable<T> iterable) {
		return (Collection<T>) iterable;
	}
	
	public static <T> T randomGet(Collection<T> collect) {
		if (isEmpty(collect))
			return null;
		return IteratorUtils.get(collect.iterator(), RandomUtils.nextInt(collect.size()));
	}
	
	public static <T> T randomGet(List<T> list) {
		if (isEmpty(list))
			return null;
		return list.get(RandomUtils.nextInt(list.size()));
	}
	
	public static <V> void iteratorAndRemove(Collection<V> colData, Function<V, Boolean> doIter) {
		while (!colData.isEmpty()) {
			Iterator<V> iter = colData.iterator();
			while (iter.hasNext()) {
				V v = iter.next();
				if (doIter.apply(v))
					iter.remove();
			}
		}
	}

	/**
	  * Returns a collection that applies {@code function} to each element of {@code fromCollection}.
	  * The returned collection is an ArrayList.
	  */
	public static <E, R> Collection<R> trans(Collection<E> fromCollection, Function<? super E, R> function) {
		Collection<R> collection = new ArrayList<>(fromCollection.size() * 2);
		for (E e : fromCollection) {
			collection.add(function.apply(e));
		}
		return collection;
	}

	public static String toString(Collection<?> collect) {
		StringBuffer sb = new StringBuffer().append("[");
		boolean first = true;
		for (Object o : collect) {
			if (!first) 
				sb.append(", ");
			first = false;
			if (o instanceof Collection) {
				sb.append(toString((Collection<?>) o));
			} else {
				sb.append(o.toString());
			}
		}
		return sb.append("]").toString();
	}

	@SuppressWarnings("unchecked")
	public static <E> E[] toArray(Collection<E> collection) {
		return (E[]) collection.toArray();
	}
	
	@SafeVarargs
	public static <T> Collection<T> arrayList(T... args) {
		return ArraysUtils.toList(args);
	}

}
