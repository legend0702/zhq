package cn.zhuhongqing.utils.scan;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

import cn.zhuhongqing.anno.NotThreadSafe;
import cn.zhuhongqing.exception.UncheckedException;
import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.matcher.FilePathMatcher;
import cn.zhuhongqing.utils.matcher.PathMatcher;

/**
 * Scan file.<br/>
 * 
 * @author HongQing.Zhu
 */

@NotThreadSafe
public abstract class FileAbstractScan<T> extends AbstractScan<T> {

	/**
	 * Resource loader.
	 */

	private static ClassLoader loader = ClassUtil.getDefaultClassLoader();

	/**
	 * Path match.
	 */

	private final PathMatcher PATH_MATCHER = new FilePathMatcher();

	/**
	 * @see FileFilter
	 */

	private final FilenameFilter FILE_FILTER = new FileFilter();

	/**
	 * Resource store.
	 * 
	 * @see #getResources(String)
	 */
	private Set<T> RETURN_SET = Collections.emptySet();;

	/**
	 * Use to choose subFile.
	 */

	private String PATH_PATTERN = null;

	/**
	 * Scan file.
	 */

	@Override
	public Set<T> getResources(String pattern) {
		String rootPath = initLocalParams(replacePath(pattern));
		Enumeration<URL> urls = null;
		try {
			urls = loader.getResources(rootPath);
		} catch (IOException e) {
			throw new UncheckedException(e);
		}
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			File rootFile;
			try {
				rootFile = new File(url.toURI());
			} catch (URISyntaxException e) {
				throw new UncheckedException(e);
			}
			if (rootFile.isFile()) {
				setFile(rootFile);
				continue;
			}
			PATH_PATTERN = rootFile.getPath() + PATH_PATTERN;
			findFile(rootFile);
		}
		return RETURN_SET;
	}

	/**
	 * @see ClassLoader#getResource(String)
	 * @see FileAbstractScan#chooseAndConvert(File)
	 */

	@Override
	public T getResource(String path) {
		try {
			File file = new File(loader.getResource(path).toURI());
			return chooseAndConvert(file);
		} catch (URISyntaxException e) {
			return null;
		}
	}

	void findFile(File... files) {
		for (File subFile : files) {
			if (subFile.isDirectory()) {
				findFile(subFile.listFiles(FILE_FILTER));
				continue;
			}
			setFile(subFile);
		}
	}

	private void setFile(File subFile) {
		T addE = chooseAndConvert(subFile);
		if (addE != null)
			RETURN_SET.add(addE);
	}

	/**
	 * Init param for {@link #getResources(String)}.
	 * 
	 * Return root-path by {@link #determineRootDir(String)}.
	 * 
	 */

	private String initLocalParams(String pattern) {
		String rootPath = determineRootDir(pattern);
		PATH_PATTERN = pattern.substring(rootPath.length());
		RETURN_SET = getSet();
		return rootPath;
	}

	/**
	 * Replace "/" to {@link #getSeparator()}.
	 */

	String replacePath(String path) {
		return path.replace(StringPool.SLASH, getSeparator());
	}

	T chooseAndConvert(File file) {
		if (choose(file))
			return convert(file);
		return null;
	}

	boolean choose(File file) {
		return file.isFile();
	}

	abstract T convert(File file);

	@Override
	PathMatcher getPathMatcher() {
		return PATH_MATCHER;
	}

	@Override
	String getSeparator() {
		return StringPool.BACK_SLASH;
	}

	/**
	 * Match the file by {@link FileAbstractScan#getPathMatcher()} and
	 * {@link FileAbstractScan#PATH_PATTERN}.
	 * 
	 */

	class FileFilter implements FilenameFilter {
		@Override
		public boolean accept(File file, String name) {
			if (file.isDirectory())
				return true;
			return getPathMatcher().match(PATH_PATTERN, file.getPath());
		}
	}

}