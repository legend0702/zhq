package cn.zhuhongqing.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import cn.zhuhongqing.exception.RuntimeExceptionWrapper;
import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.util.file.FileIOParams;
import cn.zhuhongqing.util.file.OrderedProperties;

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

public class GeneralUtils {

	/**
	 * The obj is null?
	 */

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	/**
	 * Is null in obj'Arr?
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
	 * @see #isNull(Object)
	 */

	public static boolean isNotNull(Object obj) {
		return (!isNull(obj));
	}

	/**
	 * To determine whether the package name.
	 * 
	 * @param packageName
	 */

	public static boolean isPackageName(String packageName) {
		return Package.getPackage(packageName) != null;
	}

	/**
	 * If val is null,return def.
	 * 
	 * @param val
	 * @param def
	 */

	public static <T> T defValue(T val, T def) {
		return isNull(val) ? def : val;
	}
	
	public static <K> K mapValueGetKey(Map<K, ?> map, Object value) {
		if (!map.containsValue(value)) {
			return null;
		}
		for (Entry<K, ?> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * @see #loadProp(FileIOParams)
	 */

	public static Properties loadProp(String propPath) {
		return loadProp(new FileIOParams(propPath));
	}

	/**
	 * Load a properties.
	 * 
	 * @return {@link OrderedProperties}
	 */

	public static Properties loadProp(FileIOParams fileParams) {
		OrderedProperties prop = new OrderedProperties();
		try (Reader r = fileParams.toInStreamReader()) {
			prop.load(r);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
		return prop;
	}

	public static void throwClassNotFound(String strClassName) {
		runtimeExceptionWrapper(new ClassNotFoundException("Can't load class by className:" + strClassName));
	}

	public static void runtimeExceptionWrapper(Exception e) {
		throw new RuntimeExceptionWrapper(e);
	}
	
	public static String uuid() {
		return java.util.UUID.randomUUID().toString().replaceAll("-", "");
	}

	// ----------------------------------------------------------------
	// System.print

	public static void report(String msg) {
		System.err.println("ZHQ: " + msg);
	}

}
