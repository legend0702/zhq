package cn.zhuhongqing.utils.scan;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.zhuhongqing.utils.matcher.PathMatcher;

public abstract class AbstractScan<E> implements ResourceScan<E> {

	/**
	 * To comparison path with pattern.
	 * 
	 */

	abstract PathMatcher getPathMatcher();

	/**
	 * To split path.
	 * 
	 */

	abstract String getSeparator();

	/**
	 * Determine the root directory for the given location.
	 * 
	 * Will return "a/b/c" for the pattern "a/b/c/**", for example.
	 * 
	 * @param location
	 *            the location to check
	 * @return the part of the location that denotes the root directory
	 */

	String determineRootDir(String location) {
		int prefixEnd = 0;
		int rootDirEnd = location.length();
		while (rootDirEnd > prefixEnd
				&& getPathMatcher().isPattern(
						location.substring(prefixEnd, rootDirEnd))) {
			rootDirEnd = location.lastIndexOf(getSeparator(), rootDirEnd - 2);
		}
		if (rootDirEnd == 0) {
			rootDirEnd = prefixEnd;
		}
		return location.substring(0, rootDirEnd);
	}

	/**
	 * Return a set<E> all sub-Class to use.
	 * 
	 */

	Set<E> getSet() {
		return new LinkedHashSet<E>();
	}

}
