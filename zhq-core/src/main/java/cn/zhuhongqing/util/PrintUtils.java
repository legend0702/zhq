package cn.zhuhongqing.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import cn.zhuhongqing.util.struct.AttrHole;

public class PrintUtils {

	public static void print(Object[] objs) {
		print(Arrays.asList(objs));
	}

	public static void print(Collection<?> col) {
		Iterator<?> itr = col.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			System.out.println(obj);
		}
	}

	public static <K, V> void print(AttrHole<K, V> hole) {
		Collection<K> keys = hole.attrKeys();
		if (CollectionUtils.isEmpty(keys))
			return;
		for (K k : keys) {
			System.out.println("Key[" + k + "] --->Val[" + hole.getAttr(k) + "]");
		}

	}
}
