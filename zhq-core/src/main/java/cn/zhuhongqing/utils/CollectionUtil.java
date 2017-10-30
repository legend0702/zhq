package cn.zhuhongqing.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import cn.zhuhongqing.exception.ZHQRuntimeException;
import cn.zhuhongqing.utils.struct.CaseInsensitiveMap;

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
public class CollectionUtil {

	public static final Class<Map> MAP_CLASS = Map.class;
	public static final Class<Collection> COLLECTION_CLASS = Collection.class;
	public static final Class<List> List_CLASS = List.class;
	public static final Class<ConcurrentMap> CONCURRENT_MAP_CLASS = ConcurrentMap.class;

	@SuppressWarnings("unchecked")
	public static <C extends Collection<G>, G> C createCollection(final Class<C> colClass, final Class<G> gClass) {
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

	public static <V> Map<String, V> caseInsensitiveMap(Map<String, V> map) {
		return CaseInsensitiveMap.of(map);
	}

}
