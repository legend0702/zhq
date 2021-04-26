package cn.zhuhongqing.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Some utils for {@link URL}.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class URLUtils {

	public static URL toURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public static InputStream getInputSteam(URL url) {
		try {
			return url.openStream();
		} catch (IOException e) {
			return null;
		}
	}

	public static String getLastPath(URL url) {
		return StringUtils.cutFromLastIndexOf(url.getPath(), StringPool.SLASH, 1);
	}

}