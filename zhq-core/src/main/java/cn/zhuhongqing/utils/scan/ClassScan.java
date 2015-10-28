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

public class ClassScan extends AbstractScan<Class<?>> {

	private FileAbstractScan<Class<?>> fileScan = new ClassFileScan();

	private ThreadLocal<String> rootPackage = new ThreadLocal<String>();

	/**
	 * {@link ClassUtil#forName(String)}
	 */

	@Override
	public Class<?> getResource(String className) {
		return ClassUtil.forName(className);
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
		String rePath = ClassUtil.classPathToFilePath(packagePattern);
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
	 * "ClassName.class" --> "ClassName"
	 */

	Class<?> classFileToClass(File file) {
		return getResource(ClassUtil.filePathToClassPath(file.getPath()
				.substring(file.getPath().indexOf(rootPackage.get()))));
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
				return classFileToClass(file);
			return null;
		}

	}

}
