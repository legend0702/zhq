package cn.zhuhongqing.util.matcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.zhuhongqing.util.StringPool;
import cn.zhuhongqing.util.StringUtils;

/**
 * AbstractMatcher.<br/>
 * 
 * @author HongQing.Zhu
 * 
 */

public abstract class AbstractMatcher implements PathMatcher {

	/**
	 * Path separator.
	 * */

	abstract String getPathSeparator();

	/**
	 * PatternMatcher cache.
	 */

	final Map<String, AntPathStringMatcher> stringMatcherCache = new ConcurrentHashMap<String, AntPathStringMatcher>(256);

	@Override
	public boolean match(String pattern, String path) {
		return doMatch(pattern, path, true);
	}

	@Override
	public boolean matchStart(String pattern, String path) {
		return doMatch(pattern, path, false);
	}

	@Override
	public boolean isPattern(String path) {
		return (path.indexOf(ALL_WORD_PATTERN) != -1
				|| path.indexOf(SINGLE_WORD_PATTERN) != -1 
				|| path .indexOf(ALL_PATTERN) != -1);
	}

	@Override
	public boolean hasPattern(String path) {
		return isPattern(path);
	}

	/**
	 * Actually match the given {@code path} against the given {@code pattern}.
	 * 
	 * @param pattern
	 *            the pattern to match against
	 * @param path
	 *            the path String to test
	 * @param fullMatch
	 *            whether a full pattern match is required 
	 *            (else a pattern match as far as the given base path goes is sufficient)
	 * @return {@code true} if the supplied {@code path} matched, {@code false} if it didn't
	 */
	protected boolean doMatch(String pattern, String path, boolean fullMatch) {
		if (path.startsWith(getPathSeparator()) != pattern .startsWith(getPathSeparator())) {
			return false;
		}

		String[] pattDirs = tokenizeToStringArray(pattern, getPathSeparator());
		String[] pathDirs = tokenizeToStringArray(path, getPathSeparator());

		int pattIdxStart = 0;
		int pattIdxEnd = pattDirs.length - 1;
		int pathIdxStart = 0;
		int pathIdxEnd = pathDirs.length - 1;

		// Match all elements up to the first {ALL_PATTERN}
		while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
			String patDir = pattDirs[pattIdxStart];
			if (ALL_PATTERN.equals(patDir)) {
				break;
			}
			if (!matchStrings(patDir, pathDirs[pathIdxStart])) {
				return false;
			}
			pattIdxStart++;
			pathIdxStart++;
		}

		if (pathIdxStart > pathIdxEnd) {
			// Path is exhausted, only match if rest of pattern is * or {ALL_PATTERN}'s
			if (pattIdxStart > pattIdxEnd) {
				return (pattern.endsWith(getPathSeparator()) ? 
						path.endsWith(getPathSeparator()) : 
						!path.endsWith(getPathSeparator()));
			}
			if (!fullMatch) {
				return true;
			}
			if (pattIdxStart == pattIdxEnd
					&& pattDirs[pattIdxStart].equals(ALL_WORD_PATTERN)
					&& path.endsWith(getPathSeparator())) {
				return true;
			}
			for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
				if (!pattDirs[i].equals(ALL_PATTERN)) {
					return false;
				}
			}
			return true;
		} else if (pattIdxStart > pattIdxEnd) {
			// String not exhausted, but pattern is. Failure.
			return false;
		} else if (!fullMatch && ALL_PATTERN.equals(pattDirs[pattIdxStart])) {
			// Path start definitely matches due to {ALL_PATTERN} part in pattern.
			return true;
		}

		// up to last {ALL_PATTERN}
		while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
			String patDir = pattDirs[pattIdxEnd];
			if (patDir.equals(ALL_PATTERN)) {
				break;
			}
			if (!matchStrings(patDir, pathDirs[pathIdxEnd])) {
				return false;
			}
			pattIdxEnd--;
			pathIdxEnd--;
		}
		if (pathIdxStart > pathIdxEnd) {
			// String is exhausted
			for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
				if (!pattDirs[i].equals(ALL_PATTERN)) {
					return false;
				}
			}
			return true;
		}

		while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
			int patIdxTmp = -1;
			for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
				if (pattDirs[i].equals(ALL_PATTERN)) {
					patIdxTmp = i;
					break;
				}
			}
			if (patIdxTmp == pattIdxStart + 1) {
				// '{ALL_PATTERN}/{ALL_PATTERN}' situation, so skip one
				pattIdxStart++;
				continue;
			}
			// Find the pattern between padIdxStart & padIdxTmp in str between strIdxStart & strIdxEnd
			int patLength = (patIdxTmp - pattIdxStart - 1);
			int strLength = (pathIdxEnd - pathIdxStart + 1);
			int foundIdx = -1;

			strLoop: for (int i = 0; i <= strLength - patLength; i++) {
				for (int j = 0; j < patLength; j++) {
					String subPat = pattDirs[pattIdxStart + j + 1];
					String subStr = pathDirs[pathIdxStart + i + j];
					if (!matchStrings(subPat, subStr)) {
						continue strLoop;
					}
				}
				foundIdx = pathIdxStart + i;
				break;
			}

			if (foundIdx == -1) {
				return false;
			}

			pattIdxStart = patIdxTmp;
			pathIdxStart = foundIdx + patLength;
		}

		for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
			if (!pattDirs[i].equals(ALL_PATTERN)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Tests whether or not a string matches against a pattern. The pattern may
	 * contain two special characters: <br>
	 * '*' means zero or more characters <br>
	 * '?' means one and only one character
	 * 
	 * @param pattern
	 *            pattern to match against. Must not be {@code null}.
	 * @param str
	 *            string which must be matched against the pattern. Must not be {@code null}.
	 * @return {@code true} if the string matches against the pattern, or {@code false} otherwise.
	 */
	private boolean matchStrings(String pattern, String str) {
		AntPathStringMatcher matcher = stringMatcherCache.get(pattern);
		if (matcher == null) {
			matcher = new AntPathStringMatcher(pattern);
			stringMatcherCache.put(pattern, matcher);
		}
		return matcher.matchStrings(str);
	}

	/**
	 * Tests whether or not a string matches against a pattern via a
	 * {@link Pattern}.
	 * <p>
	 * The pattern may contain special characters: '*' means zero or more
	 * characters; '?' means one and only one character; '{' and '}' indicate a
	 * URI template pattern. For example <tt>/users/{user}</tt>.
	 */
	private class AntPathStringMatcher {

		/**
		 * 正则 查找字符串内是否包含?,*,{,?:,},{},[,],[]
		 */
		final Pattern GLOB_PATTERN = Pattern.compile("\\" + SINGLE_WORD_PATTERN + "|\\" + ALL_WORD_PATTERN);
		// "\\" + SINGLE_WORD_PATTERN + "|\\" + ALL_WORD_PATTERN
		// "\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}"

		static final String DEFAULT_VARIABLE_PATTERN = "(." + ALL_WORD_PATTERN + ")";

		private final Pattern pattern;

		private final List<String> variableNames = new LinkedList<String>();

		public AntPathStringMatcher(String pattern) {
			StringBuilder patternBuilder = new StringBuilder();
			Matcher m = GLOB_PATTERN.matcher(pattern);
			int end = 0;
			while (m.find()) {
				patternBuilder.append(quote(pattern, end, m.start()));
				String match = m.group();
				if (SINGLE_WORD_PATTERN.equals(match)) {
					patternBuilder.append('.');
				} else if (ALL_WORD_PATTERN.equals(match)) {
					patternBuilder.append("." + ALL_WORD_PATTERN);
				} else if (match.startsWith("{") && match.endsWith("}")) {
					int colonIdx = match.indexOf(':');
					if (colonIdx == -1) {
						patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
						this.variableNames.add(m.group(1));
					} else {
						String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
						patternBuilder.append('(');
						patternBuilder.append(variablePattern);
						patternBuilder.append(')');
						String variableName = match.substring(1, colonIdx);
						this.variableNames.add(variableName);
					}
				}
				end = m.end();
			}
			patternBuilder.append(quote(pattern, end, pattern.length()));
			this.pattern = Pattern.compile(patternBuilder.toString());
		}

		private String quote(String s, int start, int end) {
			if (start == end) {
				return StringPool.EMPTY;
			}
			return Pattern.quote(s.substring(start, end));
		}

		/**
		 * Main entry point.
		 * 
		 * @return {@code true} if the string matches against the pattern, or {@code false} otherwise.
		 */
		public boolean matchStrings(String str) {
			return this.pattern.matcher(str).matches();
		}
	}

	/**
	 * split str by delimiters
	 */

	private static String[] tokenizeToStringArray(String str, String delimiters) {
		if (StringUtils.isEmpty(str)) {
			return new String[0];
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			if (!StringUtils.isEmpty(str))
				tokens.add(token);
		}
		return tokens.toArray(new String[tokens.size()]);
	}
}
