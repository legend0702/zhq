package cn.zhuhongqing;

import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.StringUtil;

/**
 * 
 * ZHQ!!! :)
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class ZHQ {

	private ZHQ() {
	};

	/**
	 * Default file encoding (UTF8).
	 */
	public static String DEFAULT_ENCODING = "UTF-8";

	/**
	 * ZHQ's package-name.
	 */

	public static final String DEFAULT_PACKAGE_NAME = ClassUtil
			.getPackageName(ZHQ.class);

	/**
	 * Checks module is loaded.
	 */
	public static boolean checkModule(String moduleName) {
		ClassLoader classLoader = ZHQ.class.getClassLoader();
		moduleName = StringUtil.capitalize(moduleName);
		try {
			classLoader.loadClass(DEFAULT_PACKAGE_NAME.concat(StringPool.DOT)
					.concat(moduleName));
			return true;
		} catch (ClassNotFoundException cnfex) {
			return false;
		}
	}

}
