package cn.zhuhongqing.util.sync;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

import cn.zhuhongqing.util.struct.collect.Table;

public final class Synchronized {

	public static <C extends Collection<E>, E> Collection<E> collection(C collection, Object mutex) {
		return new SynchronizedCollection<C, E>(collection, mutex);
	}

	public static <E> List<E> list(List<E> list, Object mutex) {
		return (list instanceof RandomAccess) 
				? new SynchronizedRandomAccessList<E>(list, mutex)
				: new SynchronizedList<E>(list, mutex);
	}

	public static <E> Set<E> set(Set<E> set, Object mutex) {
		return new SynchronizedSet<E>(set, mutex);
	}

	public static <K, V> Map<K, V> map(Map<K, V> map, Object mutex) {
		return new SynchronizedMap<>(map, mutex);
	}
	
	public static <R, C, V> Table<R, C, V> table(Table<R, C, V> table, Object mutex) {
	    return new SynchronizedTable<>(table, mutex);
    }

}
