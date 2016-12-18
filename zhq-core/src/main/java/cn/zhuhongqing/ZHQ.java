package cn.zhuhongqing;

import java.nio.charset.Charset;

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
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public class ZHQ {

	private ZHQ() {
	};

	/**
	 * Default encoding (UTF8).
	 */
	public static String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Default Charset (UTF8).
	 */
	public static Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_ENCODING);

	/**
	 * ZHQ's package-name.
	 */

	public static final String DEFAULT_PACKAGE_NAME = ClassUtil.getPackageName(ZHQ.class);

	/**
	 * Checks module is loaded.
	 */
	public static boolean checkModule(String moduleName) {
		ClassLoader classLoader = ZHQ.class.getClassLoader();
		moduleName = StringUtil.capitalize(moduleName);
		try {
			classLoader.loadClass(DEFAULT_PACKAGE_NAME.concat(StringPool.DOT).concat(moduleName));
			return true;
		} catch (ClassNotFoundException cnfex) {
			return false;
		}
	}

}
