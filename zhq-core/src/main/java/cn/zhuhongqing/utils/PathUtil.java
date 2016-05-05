package cn.zhuhongqing.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Some utilities for path.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class PathUtil {

	/**
	 * @see Path#isAbsolute()
	 */

	public static boolean isAbsolute(String path) {
		return toPath(path).isAbsolute();
	}

	/**
	 * @see Path#normalize()
	 */

	public static Path normalize(String path) {
		return toPath(path).normalize();
	}

	/**
	 * @see Paths#get(String, String...)
	 */

	public static Path toPath(String path) {
		return Paths.get(path);
	}

}
