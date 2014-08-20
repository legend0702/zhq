package cn.zhuhongqing.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Utilities for String.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class StringUtil {

	// /
	private static final String FOLDER_SEPARATOR = StringPool.SLASH;

	// \\
	private static final String WINDOWS_FOLDER_SEPARATOR = StringPool.BACK_SLASH;

	// .
	private static final String CURRENT_PATH = StringPool.DOT;

	// ..
	private static final String TOP_PATH = StringPool.DOTDOT;

	// ---------------------------------------------------------------- empty

	/**
	 * Is empty string.
	 */

	public static boolean isEmpty(CharSequence str) {
		return (str == null || str.toString().trim().isEmpty());
	}

	/**
	 * Is not empty string.
	 * 
	 * @see StringUtil#isEmpty(String)
	 */

	public static boolean isNotEmpty(CharSequence str) {
		return (!isEmpty(str));
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
	 * Thus "fooBah" becomes "FooBah" and "X" becomes "X".
	 * 
	 * @param name
	 *            The string to be capitalized.
	 * @return The capitalized version of the string.
	 */

	public static String capitalize(String name) {
		if (name.length() == 0) {
			return name;
		}
		if (Character.isUpperCase(name.charAt(0))) {
			return name;
		}
		char chars[] = name.toCharArray();
		char c = chars[0];
		char modifiedChar = Character.toUpperCase(c);
		if (modifiedChar == c) {
			return name;
		}
		chars[0] = modifiedChar;
		return new String(chars);
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
		if (name.length() > 1 && Character.isUpperCase(name.charAt(1))
				&& Character.isUpperCase(name.charAt(0))) {
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
	public static String replace(String inString, String oldPattern,
			String newPattern) {
		if (!isEmpty(inString) || !isEmpty(oldPattern) || newPattern == null) {
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
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR,
				FOLDER_SEPARATOR);

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

		String[] pathArray = delimitedListToStringArray(pathToUse,
				FOLDER_SEPARATOR);
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

		return prefix
				+ collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
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
	public static String collectionToDelimitedString(Collection<?> coll,
			String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
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
	public static String collectionToDelimitedString(Collection<?> coll,
			String delim, String prefix, String suffix) {
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
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of
	 * potential delimiter characters - in contrast to
	 * {@code tokenizeToStringArray}.
	 * 
	 * @param str
	 *            the input String
	 * @param delimiter
	 *            the delimiter between elements (this is a single delimiter,
	 *            rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str,
			String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of
	 * potential delimiter characters - in contrast to
	 * {@code tokenizeToStringArray}.
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
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str,
			String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
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
		return null;
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
	 * @see #cutSurrounding(String, String, String)
	 */
	public static String cutSurrounding(String string, String fix) {
		return cutSurrounding(string, fix, fix);
	}

	/**
	 * Removes surrounding prefix and suffixes.
	 */
	public static String cutSurrounding(String string, String prefix,
			String suffix) {
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

}
