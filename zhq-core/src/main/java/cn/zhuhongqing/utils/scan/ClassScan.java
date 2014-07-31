package cn.zhuhongqing.utils.scan;

import java.io.File;
import java.util.Set;

import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.matcher.FilePathMatcher;
import cn.zhuhongqing.utils.matcher.PathMatcher;

/**
 * ClassScan.
 * 
 * Scan ClassPath-Resource.
 * 
 * It's thread-safe.
 * 
 * @author HongQing.Zhu
 * 
 */

public class ClassScan extends AbstractScan<Class<?>> implements
		ResourceScan<Class<?>> {

	private FileAbstractScan<Class<?>> fileScan = new ClassFileScan();

	private ThreadLocal<String> rootPackage = new ThreadLocal<String>();

	/**
	 * {@link Class#forName(String)}
	 */

	@Override
	public Class<?> getResource(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Use pattern to find and load class.
	 * 
	 * @see FilePathMatcher
	 */

	@Override
	public Set<Class<?>> getResources(String packagePattern) {
		Set<Class<?>> returnClass = getSet();
		Class<?> isClass = getResource(packagePattern);
		if (isClass != null) {
			returnClass.add(isClass);
			return returnClass;
		}
		String rePath = replacePath(packagePattern);
		/**
		 * If the pattern is root-pattern.
		 * 
		 * Add {PathMatcher#ALL_WORD_PATTERN} to the end.
		 */
		if (!fileScan.getPathMatcher().hasPattern(rePath)) {
			if (!rePath.endsWith(getSeparator()))
				rePath = rePath.concat(getSeparator());
			rePath = rePath.concat(PathMatcher.ALL_WORD_PATTERN);
		}
		rePath = rePath.concat(ClassUtil.CLASS_FILE_SUFFIX);
		rootPackage.set(fileScan.determineRootDir(rePath));
		return fileScan.getResources(rePath);
	}

	/**
	 * Replace "." to "\".
	 * 
	 * @param path
	 * @return
	 */

	String replacePath(String path) {
		return path.replace(StringPool.DOT, StringPool.BACK_SLASH);
	}

	/**
	 * Replace "\" to ".".
	 * 
	 * @param path
	 * @return
	 */

	String reReplacePath(String path) {
		return path.replace(StringPool.BACK_SLASH, StringPool.DOT);
	}

	/**
	 * "ClassName.class" --> "ClassName"
	 * 
	 * @param file
	 * @return
	 */

	Class<?> classFileToClassName(File file) {
		String className = reReplacePath(file.getPath().substring(
				file.getPath().indexOf(rootPackage.get())));
		className = className.substring(0, className.length()
				- ClassUtil.CLASS_FILE_SUFFIX.length());
		return getResource(className);
	}

	/**
	 * Not use.
	 */

	@Override
	PathMatcher getPathMatcher() {
		return null;
	}

	@Override
	String getSeparator() {
		return StringPool.BACK_SLASH;
	}

	class ClassFileScan extends FileAbstractScan<Class<?>> {
		@Override
		Class<?> convert(File file) {
			if (file.getName().endsWith(ClassUtil.CLASS_FILE_SUFFIX))
				return classFileToClassName(file);
			return null;
		}

	}

}
