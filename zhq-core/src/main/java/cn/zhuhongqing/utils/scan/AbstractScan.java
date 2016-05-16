package cn.zhuhongqing.utils.scan;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.io.FileUtil;
import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.URIUtil;
import cn.zhuhongqing.utils.matcher.PathMatcher;
import cn.zhuhongqing.utils.matcher.URIMatch;

/**
 * 1.Clean the pathPattern to {@link URI} and init {@link PathCouple}.<br/>
 * 2.Judge the {@link URI} is ClassPath or other platform.<br/>
 * 3.Choose find way base on {@link URI}'s scheme.<br/>
 * 4.Find and match the resource and convert it to Generic.<br/>
 * 5.Return value(s).
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

abstract class AbstractScan<R> implements ResourceScan<R> {

	/**
	 * Use URIMatch.
	 */
	private static final PathMatcher PATH_MATCHER = new URIMatch();

	/**
	 * To comparison path with pattern.
	 */

	PathMatcher getPathMatcher() {
		return PATH_MATCHER;
	}

	/**
	 * @see StringPool#SLASH
	 */

	String getSeparator() {
		return StringPool.SLASH;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R getResource(String path) {
		return getResource(path, (ResourceFilter<R>) TrueResourceFilter.INSTANCE);
	}

	/**
	 * Get one resource with filter.
	 */

	@Override
	public R getResource(String path, ResourceFilter<R> filter) {
		PathCouple pathCouple = initParams(path);
		if (StringUtil.isNotEmpty(pathCouple.pattern)) {
			throw new IllegalArgumentException("The path must without pattern.");
		}
		pathCouple.filter = filter;
		Set<R> resource = choosePlatformAndFind(pathCouple, pathCouple.rootURI);
		if (resource.isEmpty()) {
			return null;
		}
		return resource.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<R> getResources(String pathPattern) {
		return getResources(pathPattern, (ResourceFilter<R>) TrueResourceFilter.INSTANCE);
	}

	/**
	 * @see ResourceScan#getResources(String, ResourceFilter)
	 */

	@Override
	public Set<R> getResources(String pathPattern, ResourceFilter<R> filter) {
		if (!getPathMatcher().hasPattern(pathPattern)) {
			throw new IllegalArgumentException("The pathPattern must has pattern words.");
		}
		PathCouple pathCouple = initParams(pathPattern);
		pathCouple.filter = filter;
		return choosePlatformAndFind(pathCouple, pathCouple.rootURI);
	}

	protected Set<R> choosePlatformAndFind(PathCouple pathCouple, URI uri) {
		try {
			if (URIUtil.isClassPath(uri)) {
				return findInClassPath(pathCouple, uri);
			} else {
				return findInOther(pathCouple, uri);
			}
		} catch (Exception e) {
			throw new UtilsException("There are some wrong things.", e);
		}
	}

	/**
	 * 
	 * Sub-class maybe invoke {@link PathCouple#setFullPattern(URI)},to init
	 * currentURI and currentPathPattern.
	 */

	protected Set<R> findInOther(PathCouple pathCouple, URI uri) throws Exception {
		return chooseSchemeAndFind(pathCouple, uri);
	}

	/**
	 * Find resource in class-Path.
	 */

	private Set<R> findInClassPath(PathCouple pathCouple, URI uri) throws Exception {
		LinkedHashSet<R> resourceSet = new LinkedHashSet<R>();
		Enumeration<URL> urls = ClassUtil.getDefaultClassLoader().getResources(uri.getSchemeSpecificPart());
		while (urls.hasMoreElements()) {
			URI uuri = urls.nextElement().toURI();
			resourceSet.addAll(chooseSchemeAndFind(pathCouple, uuri));
		}
		return resourceSet;
	}

	protected Set<R> chooseSchemeAndFind(PathCouple pathCouple, URI uri) throws Exception {
		if (URIUtil.isFile(uri)) {
			LinkedHashSet<R> files = new LinkedHashSet<R>();
			if (StringUtil.isEmpty(pathCouple.pattern)) {
				// getResource
				files.add(chooseAndConvert(uri, pathCouple));
			} else {
				// getResources
				pathCouple.setFullPattern(uri);
				deepMatchAndFindInFile(files, pathCouple, uri);
			}
			return files;
		} else if (URIUtil.isJar(uri)) {
			return deepMatchAndFindInJar(pathCouple, uri);
		}
		// Could not choose a scheme of URI: [" + uri + "]")
		return Collections.emptySet();
	}

	protected void deepMatchAndFindInFile(Set<R> rSet, PathCouple pathCouple, URI... uris) {
		for (URI uri : uris) {
			File file = new File(uri);
			if (!FileUtil.isExisAndReadable(file)) {
				continue;
			}
			String subPath = StringUtil.cutPrefix(uri.getPath(), pathCouple.currentURI.getPath());
			if (file.isDirectory()) {
				// Current directory
				if (StringUtil.isEmpty(subPath) || matchStartPath(pathCouple.pattern, subPath)) {
					deepMatchAndFindInFile(rSet, pathCouple, FileUtil.toURIs(file.listFiles()));
				}
			} else if (file.isFile() && matchPath(pathCouple.pattern, subPath)) {
				R r = chooseAndConvert(uri, pathCouple);
				if (GeneralUtil.isNotNull(r)) {
					rSet.add(r);
				}
			}
		}
	}

	protected Set<R> deepMatchAndFindInJar(PathCouple pathCouple, URI uri) throws Exception {
		JarFile jarFile = FileUtil.toJarFile(uri);
		String rootEntryPath = URIUtil.getJarRootEntryPath(uri);
		pathCouple.setFullPattern(uri);
		Set<R> result = new LinkedHashSet<R>();
		for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			String entryPath = entry.getName();
			if (entryPath.startsWith(rootEntryPath)) {
				String relativePath = entryPath.substring(rootEntryPath.length());
				if (getPathMatcher().match(pathCouple.pattern, relativePath)) {
					result.add(chooseAndConvert(
							URIUtil.toURI(uri.getScheme(), uri.getSchemeSpecificPart() + relativePath), pathCouple));
				}
			}
		}
		jarFile.close();
		return result;

	}

	private R chooseAndConvert(URI uri, PathCouple couple) {
		if (GeneralUtil.isNotNull(uri)) {
			R r;
			try {
				r = convert(uri, couple);
			} catch (Exception e) {
				return null;
			}
			if (GeneralUtil.isNotNull(r) && couple.filter.accept(r)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Convert file to other type which you want.
	 */

	abstract R convert(URI uri, PathCouple couple) throws Exception;

	/**
	 * Match path by pattern.
	 */

	protected boolean matchPath(String pattern, String path) {
		return getPathMatcher().match(pattern, path);
	}

	/**
	 * Match start-path by pattern.
	 */

	protected boolean matchStartPath(String pattern, String path) {
		return getPathMatcher().matchStart(pattern, path);
	}

	/**
	 * Determine the root directory for the given location.
	 * 
	 * Will return "a/b/c" for the pattern "a/b/c/**", for example.
	 * 
	 * @param location
	 *            the location to check
	 * @return the part of the location that denotes the root directory
	 */

	protected String determineRootDir(String location) {
		int prefixEnd = 0;
		int rootDirEnd = location.length();
		while (rootDirEnd > prefixEnd && getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd))) {
			rootDirEnd = location.lastIndexOf(getSeparator(), rootDirEnd - 2) + 1;
		}
		if (rootDirEnd == 0) {
			rootDirEnd = prefixEnd;
		}
		return location.substring(0, rootDirEnd);
	}

	/**
	 * Clean the pathPattern and create {@link PathCouple}.<br/>
	 * Return {@link PathCouple} which has root-path by
	 * {@link #determineRootDir(String)} and pattern.
	 * 
	 */

	protected PathCouple initParams(String pathPattern) {
		String cleanPatn = StringUtil.cleanPath(pathPattern);
		String rootPath = determineRootDir(cleanPatn);
		return new PathCouple(URIUtil.toURI(rootPath), cleanPatn.substring(rootPath.length()));
	}

	/**
	 * Use to store parameters in find-process.
	 */

	class PathCouple {
		/** Root uri */
		URI rootURI;
		/** Pattern */
		String pattern;
		/** Current directory URI */
		URI currentURI;
		/** Current directory {@link URI#getSchemeSpecificPart()} + Pattern */
		// String currentPathPattern;
		/** User's filter */
		private ResourceFilter<R> filter;

		public PathCouple(URI uri, String pattern) {
			this.rootURI = uri;
			this.pattern = pattern;
		}

		/**
		 * Set root-path and root-path-pattern
		 */

		public void setFullPattern(URI currentRootURI) {
			this.currentURI = currentRootURI;
		}
	}

}
