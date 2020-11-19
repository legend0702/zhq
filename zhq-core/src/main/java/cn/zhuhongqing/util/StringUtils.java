package cn.zhuhongqing.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import cn.zhuhongqing.util.matcher.PathMatcher;

/**
 * Utilities for String.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class StringUtils {

	// /
	public static final String FOLDER_SEPARATOR = StringPool.SLASH;

	// \\
	public static final String WINDOWS_FOLDER_SEPARATOR = StringPool.BACK_SLASH;

	// .
	public static final String CURRENT_PATH = StringPool.DOT;

	// ..
	public static final String TOP_PATH = StringPool.DOTDOT;

	// ---------------------------------------------------------------- empty

	/**
	 * Is empty string.
	 */

	public static boolean isEmpty(CharSequence str) {
		return (str == null || str.toString().trim().isEmpty());
	}

	/**
	 * Has empty string.
	 */

	public static boolean hasEmpty(CharSequence... strs) {
		for (CharSequence s : strs) {
			if (isEmpty(s))
				return true;
		}
		return false;
	}

	/**
	 * Is not empty string.
	 * 
	 * @see StringUtils#isEmpty(String)
	 */

	public static boolean isNotEmpty(CharSequence str) {
		return (!isEmpty(str));
	}

	/**
	 * Return null Or trim-string.
	 */

	public static String trim(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return str.trim();
	}

	/**
	 * Trim <i>all</i> whitespace from the given String: leading, trailing, and
	 * in between characters.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		int index = 0;
		while (sb.length() > index) {
			if (Character.isWhitespace(sb.charAt(index))) {
				sb.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return sb.toString();
	}

	/**
	 * Capitalize a {@code String}, changing the first letter to upper case as
	 * per {@link Character#toUpperCase(char)}. No other letters are changed.
	 * 
	 * Thus "fooBah" becomes "FooBah" and "X" becomes "X".
	 * 
	 * @param str
	 *            the {@code String} to capitalize
	 * @return the capitalized {@code String}
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * Uncapitalize a {@code String}, changing the first letter to lower case as
	 * per {@link Character#toLowerCase(char)}. No other letters are changed.
	 * 
	 * @param str
	 *            the {@code String} to uncapitalize
	 * @return the uncapitalized {@code String}
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (isEmpty(str)) {
			return str;
		}

		char baseChar = str.charAt(0);
		char updatedChar;
		if (capitalize) {
			updatedChar = Character.toUpperCase(baseChar);
		} else {
			updatedChar = Character.toLowerCase(baseChar);
		}
		if (baseChar == updatedChar) {
			return str;
		}

		char[] chars = str.toCharArray();
		chars[0] = updatedChar;
		return new String(chars, 0, chars.length);
	}

	/**
	 * Utility method to take a string and convert it to normal Java variable
	 * name capitalization. This normally means converting the first character
	 * from upper case to lower case, but in the (unusual) special case when
	 * there is more than one character and both the first and second characters
	 * are upper case, we leave it alone.
	 * <p/>
	 * Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays as
	 * "URL".
	 * 
	 * @param name
	 *            The string to be decapitalized.
	 * @return The decapitalized version of the string.
	 */
	public static String decapitalize(String name) {
		if (name.length() == 0) {
			return name;
		}
		if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
			return name;
		}

		char chars[] = name.toCharArray();
		char c = chars[0];
		char modifiedChar = Character.toLowerCase(c);
		if (modifiedChar == c) {
			return name;
		}
		chars[0] = modifiedChar;
		return new String(chars);
	}

	/**
	 * Null to {@link StringPool#EMPTY}
	 */

	public static String toString(Object obj) {
		return obj == null ? StringPool.EMPTY : obj.toString();
	}

	/**
	 * Is Json-Schema String
	 */

	public static boolean isJson(String json) {
		return (isJsonObj(json) || isJsonArray(json));
	}

	/**
	 * Is Json-Object-Schema String
	 */

	public static boolean isJsonObj(String json) {
		if (json != null && json.startsWith(StringPool.LEFT_BRACE) && json.endsWith(StringPool.RIGHT_BRACE)) {
			return true;
		}
		return false;
	}

	/**
	 * Is Json-Array-Schema String
	 */

	public static boolean isJsonArray(String json) {
		if (json != null && json.startsWith(StringPool.LEFT_SQ_BRACKET) && json.endsWith(StringPool.RIGHT_SQ_BRACKET)) {
			return true;
		}
		return false;
	}

	/**
	 * name,value => {name:value}
	 */

	public static String toJson(String name, Object value) {
		return StringPool.LEFT_BRACE + name + StringPool.COLON + toString(value) + StringPool.RIGHT_BRACE;
	}

	/**
	 * name,value,name2,value2 => {name:value,name2:value2}
	 */

	public static String toJson(String... nameValues) {
		if (ArraysUtils.isEmpty(nameValues))
			return StringPool.JSON_OBJ;
		if (nameValues.length == 1)
			return toJson(nameValues[0], StringPool.EMPTY);
		StringBuffer sb = new StringBuffer(50);
		sb.append(StringPool.LEFT_BRACE);
		int index = 0;
		int compen = nameValues.length % 2;
		do {
			sb.append(nameValues[index]);
			sb.append(StringPool.COLON);
			String val = StringPool.EMPTY;
			if (compen == 0)
				val = toString(nameValues[++index]);
			if (compen == 1) {
				if (index + compen == nameValues.length)
					val = StringPool.EMPTY;
				else
					val = toString(nameValues[++index]);
			}
			sb.append(val);
			index++;
			if (index < nameValues.length) {
				sb.append(StringPool.COMMA);
			}
		} while (index < nameValues.length);
		sb.append(StringPool.RIGHT_BRACE);
		return sb.toString();
	}

	/**
	 * Replace all occurences of a substring within a string with another
	 * string.
	 * 
	 * @param inString
	 *            String to examine
	 * @param oldPattern
	 *            String to replace
	 * @param newPattern
	 *            String to insert
	 * @return a String with the replacements
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (isEmpty(inString) || isEmpty(oldPattern) || isEmpty(newPattern)) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sb.append(inString.substring(pos));
		// remember to append any characters to the right of a match
		return sb.toString();
	}

	public static String replaceSlashToDot(String inString) {
		return replace(inString, StringPool.SLASH, StringPool.DOT);
	}

	public static String replaceDotToSlash(String inString) {
		return replace(inString, StringPool.DOT, StringPool.SLASH);
	}

	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner
	 * simple dots.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that
	 * Windows separators ("\") are replaced by simple slashes.
	 * 
	 * @param path
	 *            the original path
	 * @return the normalized path
	 */
	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with element corresponding to top
					// path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}
	
	public static String cleanRegex(String regexStr) {
        // Only check if there exists the valid left parenthesis, leave regex validation for Pattern.
        if (StringUtils.countMatches(regexStr, "(") - StringUtils.countMatches(regexStr, "\\(") ==
                StringUtils.countMatches(regexStr, "(?:") - StringUtils.countMatches(regexStr, "\\(?:")) {
            regexStr = "(" + regexStr + ")";
        }
        return regexStr;
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for {@code toString()} implementations.
	 * 
	 * @param coll
	 *            the Collection to display
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection<?> coll) {
		return collectionToDelimitedString(coll, StringPool.COMMA, StringPool.EMPTY, StringPool.EMPTY);
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for {@code toString()} implementations.
	 * 
	 * @param coll
	 *            the Collection to display
	 * @param delim
	 *            the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, StringPool.EMPTY, StringPool.EMPTY);
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for {@code toString()} implementations.
	 * 
	 * @param coll
	 *            the Collection to display
	 * @param delim
	 *            the delimiter to use (probably a ",")
	 * @param prefix
	 *            the String to start each element with
	 * @param suffix
	 *            the String to end each element with
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
		if (coll == null || coll.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * @see #delimitedListToStringArray(String, String, String)
	 */
	public static String[] delimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, StringPool.COMMA, null);
	}

	/**
	 * @see #delimitedListToStringArray(String, String, String)
	 */

	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of
	 * potential delimiter characters.
	 * 
	 * @param str
	 *            the input String
	 * @param delimiter
	 *            the delimiter between elements (this is a single delimiter,
	 *            rather than a bunch individual delimiter characters)
	 * @param charsToDelete
	 *            a set of characters to delete. Useful for deleting unwanted
	 *            line breaks: e.g. "\r\n\f" will delete all new lines and line
	 *            feeds in a String.
	 * @return an array of the tokens in the list
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List<String> result = new ArrayList<String>();
		if (isEmpty(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		} else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Delete any character in a given String.
	 * 
	 * @param inString
	 *            the original String
	 * @param charsToDelete
	 *            a set of characters to delete. E.g. "az\n" will delete 'a's,
	 *            'z's and new lines.
	 * @return the resulting String
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (isEmpty(inString) || isEmpty(charsToDelete)) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 将一个String依照reg进行截断存入一个List中,最后返回List
	 * 
	 * @param str
	 * @param reg
	 * @return List<String>
	 */

	public static List<String> split(String str, String reg) {
		List<String> strList = new ArrayList<String>();
		if (isEmpty(str)) {
			return strList;
		}
		StringTokenizer st = new StringTokenizer(str, reg);
		while (st.hasMoreElements()) {
			strList.add(st.nextToken().trim());
		}
		return strList;
	}

	/**
	 * 以{@link StringPool#COMMA}分割字符串
	 */

	public static List<String> split(String str) {
		return split(str, StringPool.COMMA);
	}

	/**
	 * Copy the given Collection into a String array. The Collection must
	 * contain String elements only.
	 * 
	 * @param collection
	 *            the Collection to copy
	 * @return the String array ({@code null} if the passed-in Collection was
	 *         {@code null})
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	// ---------------------------------------------------------------- pad

	public static String startPad(String str, String start) {
		if (!str.startsWith(start))
			str = start + str;
		return str;
	}

	public static String endPad(String str, String end) {
		if (!str.endsWith(end))
			str = str + end;
		return str;
	}

	public static String endPadAsterisk(String str) {
		return endPad(str, StringPool.ASTERISK);
	}

	public static String endPadSlash(String str) {
		return endPad(str, StringPool.SLASH);
	}

	public static String endPadDot(String str) {
		return endPad(str, StringPool.DOT);
	}

	public static String endPadSlashAndAllPattern(String str) {
		if (str.endsWith(PathMatcher.ALL_PATTERN) || str.endsWith(PathMatcher.ALL_WORD_PATTERN)) {
			return str;
		}
		String reStr = endPadSlash(str);
		reStr = endPad(reStr, PathMatcher.ALL_PATTERN);
		return reStr;
	}

	/**
	 * forwardFilling value 00 - 99.
	 */
	public static String forwardFilling2(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value must be positive: " + value);
		}
		if (value < 10) {
			return '0' + Integer.toString(value);
		}
		if (value < 100) {
			return Integer.toString(value);
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	/**
	 * forwardFilling value 00 - 999.
	 */
	public static String forwardFilling3(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value must be positive: " + value);
		}
		if (value < 10) {
			return "00" + Integer.toString(value);
		}
		if (value < 100) {
			return '0' + Integer.toString(value);
		}
		if (value < 1000) {
			return Integer.toString(value);
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	/**
	 * Prints 4 digits and optional minus sign.
	 */
	public static String forwardFilling4(int value) {
		char[] result = new char[4];
		int count = 0;

		if (value < 0) {
			result[count++] = '-';
			value = -value;
		}

		String str = Integer.toString(value);

		if (value < 10) {
			result[count++] = '0';
			result[count++] = '0';
			result[count++] = '0';
			result[count++] = str.charAt(0);
		} else if (value < 100) {
			result[count++] = '0';
			result[count++] = '0';
			result[count++] = str.charAt(0);
			result[count++] = str.charAt(1);
		} else if (value < 1000) {
			result[count++] = '0';
			result[count++] = str.charAt(0);
			result[count++] = str.charAt(1);
			result[count++] = str.charAt(2);
		} else {
			if (count > 0) {
				return '-' + str;
			}
			return str;
		}
		return new String(result, 0, count);
	}

	/**
	 * backFilling value 0 - 99
	 */

	public static int backFilling2(int value) {
		if (value < 10) {
			return value * 10;
		}
		if (value < 100) {
			return value;
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	/**
	 * backFilling value 0 - 999
	 */

	public static int backFilling3(int value) {
		if (value < 10) {
			return value * 100;
		}
		if (value < 100) {
			return value * 10;
		}
		if (value < 1000) {
			return value;
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	/**
	 * backFilling value 0 - 9999
	 */

	public static int backFilling4(int value) {
		if (value < 10) {
			return value * 1000;
		}
		if (value < 100) {
			return value * 100;
		}
		if (value < 1000) {
			return value * 10;
		}
		if (value < 10000) {
			return value;
		}
		throw new IllegalArgumentException("Value too big: " + value);
	}

	// ---------------------------------------------------------------- fix

	/**
	 * Inserts prefix if doesn't exist.
	 */
	public static String prefix(String string, String prefix) {
		if (string.startsWith(prefix) == false) {
			string = prefix + string;
		}
		return string;
	}

	/**
	 * Appends suffix if doesn't exist.
	 */
	public static String suffix(String string, String suffix) {
		if (string.endsWith(suffix) == false) {
			string += suffix;
		}
		return string;
	}

	// ---------------------------------------------------------------- cut

	/**
	 * Cuts the string from beginning to the first index of provided substring.
	 */
	public static String cutToIndexOf(String string, String substring) {
		int i = string.indexOf(substring);
		if (i != -1) {
			string = string.substring(0, i);
		}
		return string;
	}

	/**
	 * Cuts the string from beginning to the first index of provided char.
	 */
	public static String cutToIndexOf(String string, char c) {
		int i = string.indexOf(c);
		if (i != -1) {
			string = string.substring(0, i);
		}
		return string;
	}

	/**
	 * Cuts this String from beginning to the first lastIndex of provided
	 * subString.
	 */

	public static String cutToLastIndexOf(String string, String subString) {
		int i = string.lastIndexOf(subString);
		if (i != -1) {
			return string.substring(i, string.length());
		}
		return string;
	}

	/**
	 * Cuts this String from beginning to the first lastIndex of provided
	 * subString and add addLength
	 */

	public static String cutToLastIndexOf(String string, String subString, int addLength) {
		int i = string.lastIndexOf(subString);
		if (i != -1) {
			return string.substring(i + addLength, string.length());
		}
		return string;
	}

	/**
	 * Cuts the string from the first index of provided substring to the end.
	 */
	public static String cutFromIndexOf(String string, String substring) {
		int i = string.indexOf(substring);
		if (i != -1) {
			string = string.substring(i);
		}
		return string;
	}

	/**
	 * Cuts the string from the first index of provided char to the end.
	 */
	public static String cutFromIndexOf(String string, char c) {
		int i = string.indexOf(c);
		if (i != -1) {
			string = string.substring(i);
		}
		return string;
	}

	/**
	 * Cuts prefix if exists.
	 */
	public static String cutPrefix(String string, String prefix) {
		if (string.startsWith(prefix)) {
			string = string.substring(prefix.length());
		}
		return string;
	}

	/**
	 * Cuts sufix if exists.
	 */
	public static String cutSuffix(String string, String suffix) {
		if (string.endsWith(suffix)) {
			string = string.substring(0, string.length() - suffix.length());
		}
		return string;
	}

	public static String cutStartFromEnd(String string, String start, String end) {
		int startIndex = string.indexOf(start);
		if (startIndex == -1) {
			return null;
		}
		StringBuilder sb = new StringBuilder(string);
		sb = sb.delete(0, startIndex + start.length());
		int endIndex = sb.indexOf(end);
		if (endIndex == -1) {
			return null;
		}
		return sb.substring(0, endIndex);
	}

	/**
	 * a/b/c/d,a/b,/ ==> /c
	 */

	public static String cutStartToLastIndex(String str, String start, String lastStr) {
		int startIndex = str.indexOf(start);
		if (startIndex == -1) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		sb = sb.delete(0, startIndex + start.length());
		int endIndex = sb.lastIndexOf(lastStr);
		if (endIndex == -1) {
			return sb.toString();
		}
		return sb.substring(0, endIndex);

	}

	/**
	 * @see #cutSurrounding(String, String, String)
	 */
	public static String cutSurrounding(String string, String fix) {
		return cutSurrounding(string, fix, fix);
	}

	/**
	 * Removes surrounding prefix and suffixes.
	 */
	public static String cutSurrounding(String string, String prefix, String suffix) {
		int start = 0;
		int end = string.length();
		if (string.startsWith(prefix)) {
			start = prefix.length();
		}
		if (string.endsWith(suffix)) {
			end -= suffix.length();
		}

		return string.substring(start, end);
	}
	
	// Count matches
    //-----------------------------------------------------------------------
    /**
     * <p>Counts how many times the substring appears in the larger string.</p>
     *
     * <p>A {@code null} or empty ("") String input returns {@code 0}.</p>
     *
     * <pre>
     * StringUtils.countMatches(null, *)       = 0
     * StringUtils.countMatches("", *)         = 0
     * StringUtils.countMatches("abba", null)  = 0
     * StringUtils.countMatches("abba", "")    = 0
     * StringUtils.countMatches("abba", "a")   = 2
     * StringUtils.countMatches("abba", "ab")  = 1
     * StringUtils.countMatches("abba", "xxx") = 0
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param sub  the String to count, may be null
     * @return the number of occurrences, 0 if either String is {@code null}
     */
	public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

}
