package cn.zhuhongqing.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

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

public class CollectionUtil {

	@SuppressWarnings("unchecked")
	public static <C extends Collection<G>, G> C createCollection(
			final Class<C> colClass, final Class<G> gClass) {
		C returnCol = null;
		try {
			returnCol = colClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			if (Set.class.equals(colClass)) {
				returnCol = (C) new HashSet<G>();
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
}
