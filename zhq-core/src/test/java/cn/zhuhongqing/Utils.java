package cn.zhuhongqing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * utilities for test :)
 * 
 * @author HongQing.Zhu
 * 
 */

public class Utils {

	public static void show(Object[] objs) {
		show(Arrays.asList(objs));
	}

	public static void show(Collection<?> col) {
		Iterator<?> itr = col.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			System.out.println(obj);
		}
	}

}
