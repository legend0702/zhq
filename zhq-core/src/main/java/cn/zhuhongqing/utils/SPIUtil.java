package cn.zhuhongqing.utils;

import java.util.Iterator;
import java.util.ServiceLoader;

import cn.zhuhongqing.exception.UtilsException;

/**
 * Utils for SPI.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class SPIUtil {

	public static <S> S load(Class<S> ifs) {
		return load(ifs, ClassUtil.getDefaultClassLoader());
	}

	public static <S> S load(Class<S> ifs, ClassLoader classLoader) {
		Iterator<S> itr = ServiceLoader.load(ifs, classLoader).iterator();
		int count = 0;
		while (itr.hasNext()) {
			return itr.next();
		}
		if (count == 0) {
			throw new UtilsException("Can't find type of " + ifs + "'s impls.");
		}
		// never do it
		return null;
	}

}
