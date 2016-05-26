package cn.zhuhongqing.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.io.FileIOParams;
import cn.zhuhongqing.io.OrderedProperties;

/**
 * 
 * Some general utilities.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class GeneralUtil {

	/**
	 * 判断obj是否为null
	 * 
	 * 不支持简单类型
	 * 
	 * @param obj
	 * @return
	 */

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	/**
	 * 循环一个数组 只要存在一个是null的 就返回true
	 * 
	 * @param objs
	 * @return
	 */

	public static boolean hasNull(Object... objs) {
		for (Object obj : objs) {
			if (isNull(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断obj是否不是null
	 * 
	 * 不支持简单类型
	 * 
	 * @param obj
	 * @return
	 */

	public static boolean isNotNull(Object obj) {
		return (!isNull(obj));
	}

	/**
	 * To determine whether the package name.
	 * 
	 * @param packageName
	 * @return
	 */

	public static boolean isPackageName(String packageName) {
		return Package.getPackage(packageName) != null;
	}

	/**
	 * If val is null,return def;
	 * 
	 * @param val
	 * @param def
	 * @return
	 */

	public static <T> T defValue(T val, T def) {
		return isNull(val) ? def : val;
	}

	/**
	 * Load a properties.
	 * 
	 * @return {@link OrderedProperties}
	 */

	public static Properties loadProp(String propPath) {
		FileIOParams io = new FileIOParams(propPath);
		OrderedProperties prop = new OrderedProperties();
		try (Reader r = io.toInStreamReader()) {
			prop.load(r);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
		return prop;
	}

	// ----------------------------------------------------------------
	// System.print

	public static void report(String msg) {
		System.err.println("ZHQ: " + msg);
	}

}
