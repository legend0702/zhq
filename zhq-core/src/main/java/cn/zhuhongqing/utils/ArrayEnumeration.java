package cn.zhuhongqing.utils;

import java.util.Enumeration;

/**
 * A simply array enumeration.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public class ArrayEnumeration<T> implements Enumeration<T> {

	private T[] elements;
	private int index = 0;

	public ArrayEnumeration(T[] array) {
		elements = array;
	}

	@Override
	public boolean hasMoreElements() {
		return elements.length > index;
	}

	@Override
	public T nextElement() {
		return elements[index++];
	}
}
