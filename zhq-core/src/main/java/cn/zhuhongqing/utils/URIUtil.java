package cn.zhuhongqing.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import cn.zhuhongqing.exception.UtilsException;

/**
 * Some utils for {@link URI}.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class URIUtil implements SchemeAndProtocol {

	/**
	 * If the path isn't absolute or the uri has no scheme, return a class-path
	 * uri.
	 */

	public static URI toURI(String pathOrURI) {
		Path p = null;
		try {
			p = PathUtil.toPath(pathOrURI);
		} catch (Exception e) {
			// Solve file path like 'file:c:/user'
			int index = pathOrURI.indexOf(FILE_PATH_PREFIX);
			if (index != -1) {
				p = PathUtil.toPath(pathOrURI.substring(FILE_PATH_PREFIX.length(), pathOrURI.length()));
			}
		}
		// Is fileSystem?
		if (GeneralUtil.isNotNull(p) && p.isAbsolute()) {
			return p.toUri();
		}
		URI uri = URI.create(pathOrURI);
		if (StringUtil.isEmpty(uri.getScheme())) {
			return toURI(CLASSPATH, pathOrURI);
		}
		return uri;
	}

	/**
	 * @param scheme
	 *            {@link SchemeAndProtocol}
	 * @param path
	 *            like "cn/zhuhongqing/utils/URIUtil.class"
	 * 
	 * @see #URI(String, String, String)
	 */

	public static URI toURI(String scheme, String path) {
		try {
			return new URI(scheme, path, null);
		} catch (URISyntaxException e) {
			throw new UtilsException(e);
		}
	}

	public static boolean isClassPath(URI uri) {
		return CLASSPATH.equals(getLowerScheme(uri));
	}

	/**
	 * Determine whether the given URI points to a resource in the file system,
	 * that is, has scheme "file".
	 * 
	 * @param uri
	 *            the URI to check
	 * @return whether the URI has been identified as a file system URL
	 */
	public static boolean isFile(URI uri) {
		return (FILE.equals(getLowerScheme(uri)));
	}

	/**
	 * Determine whether the given URI points to a resource in a jar file, that
	 * is, has scheme "jar", "zip".
	 * 
	 * @param uri
	 *            the URI to check
	 * @return whether the URI has been identified as a JAR URI
	 */
	public static boolean isJar(URI uri) {
		String scheme = getLowerScheme(uri);
		return (JAR.equals(scheme) || ZIP.equals(scheme));
	}

	/**
	 * Determine whether the given URI points to a jar file itself, that is, has
	 * scheme "file" and ends with the ".jar" extension.
	 * 
	 * @param uri
	 *            the URI to check
	 * @return whether the URI has been identified as a JAR file URI
	 */
	public static boolean isJarFile(URI uri) {
		return (isFile(uri) && uri.getSchemeSpecificPart().endsWith(JAR_FILE_EXTENSION));
	}

	public static String getJarRootEntryPath(URI uri) {
		String rootEntryPath = uri.getSchemeSpecificPart();
		int separatorIndex = rootEntryPath.indexOf(JAR_PATH_SEPARATOR);
		if (separatorIndex != -1) {
			rootEntryPath = rootEntryPath.substring(separatorIndex + JAR_PATH_SEPARATOR.length());
		} else {
			rootEntryPath = "";
		}
		return rootEntryPath;
	}

	public static String getLowerScheme(URI uri) {
		return uri.getScheme().toLowerCase();
	}

	public static String getSchemeSpecificPart(File file) {
		return file.toURI().getSchemeSpecificPart();
	}

}
