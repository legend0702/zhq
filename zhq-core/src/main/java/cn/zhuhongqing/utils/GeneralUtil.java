package cn.zhuhongqing.utils;

/**
 * 
 * Some general utils.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class GeneralUtil {

	/**
	 * 判断objs中是否有null
	 * 
	 * 不支持简单类型
	 * 
	 * @param objs
	 * @return
	 */

	public static boolean isNull(Object... objs) {
		for (Object obj : objs) {
			if (obj == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断objs中是否不存在null
	 * 
	 * 不支持简单类型
	 * 
	 * @param objs
	 * @return
	 */

	public static boolean isNotNull(Object... objs) {
		return (!isNull(objs));
	}

	/**
	 * 得到一个类的根路径
	 * 
	 * @param clazz
	 * @return
	 */

	public static String getClassRootPath(Class<?> clazz) {
		int index = clazz.getCanonicalName().lastIndexOf(StringPool.DOT);
		if (index != -1) {
			index++;
		} else {
			index = 0;
		}
		return clazz.getCanonicalName().substring(0, index);
	}

	/**
	 * To determine whether the package name.
	 * 
	 * @param packageName
	 * @return
	 */

	public static boolean isPackageName(String packageName) {
		Package pkg = Package.getPackage(packageName);
		return (pkg != null);
	}

	// ---------------------------------------------------------------- pad

	public static String startPad(String str, String start) {
		if (!str.startsWith(start))
			str = start + str;
		return str;
	}

	public static String endPad(String str, String end) {
		if (!str.endsWith(end))
			str = str + end;
		return str;
	}

	/**
	 * forwardFilling value 00 - 99.
	 */
	public static String forwardFilling2(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value must be positive: "
					+ value);
		}
		if (value < 10) {
			return '0' + Integer.toString(value);
		}
		if (value < 100) {
			return Integer.toString(value);
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	/**
	 * forwardFilling value 00 - 999.
	 */
	public static String forwardFilling3(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value must be positive: "
					+ value);
		}
		if (value < 10) {
			return "00" + Integer.toString(value);
		}
		if (value < 100) {
			return '0' + Integer.toString(value);
		}
		if (value < 1000) {
			return Integer.toString(value);
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	/**
	 * Prints 4 digits and optional minus sign.
	 */
	public static String forwardFilling4(int value) {
		char[] result = new char[4];
		int count = 0;

		if (value < 0) {
			result[count++] = '-';
			value = -value;
		}

		String str = Integer.toString(value);

		if (value < 10) {
			result[count++] = '0';
			result[count++] = '0';
			result[count++] = '0';
			result[count++] = str.charAt(0);
		} else if (value < 100) {
			result[count++] = '0';
			result[count++] = '0';
			result[count++] = str.charAt(0);
			result[count++] = str.charAt(1);
		} else if (value < 1000) {
			result[count++] = '0';
			result[count++] = str.charAt(0);
			result[count++] = str.charAt(1);
			result[count++] = str.charAt(2);
		} else {
			if (count > 0) {
				return '-' + str;
			}
			return str;
		}
		return new String(result, 0, count);
	}

	/**
	 * backFilling value 0 - 99
	 */

	public static int backFilling2(int value) {
		if (value < 10) {
			return value * 10;
		}
		if (value < 100) {
			return value;
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	/**
	 * backFilling value 0 - 999
	 */

	public static int backFilling3(int value) {
		if (value < 10) {
			return value * 100;
		}
		if (value < 100) {
			return value * 10;
		}
		if (value < 1000) {
			return value;
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	/**
	 * backFilling value 0 - 9999
	 */

	public static int backFilling4(int value) {
		if (value < 10) {
			return value * 1000;
		}
		if (value < 100) {
			return value * 100;
		}
		if (value < 1000) {
			return value * 10;
		}
		if (value < 10000) {
			return value;
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

}
