package cn.zhuhongqing.util.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.zhuhongqing.util.DateUtils;

/**
 * Date time formatter performs conversion both from and to string
 * representation of time.
 * 
 * It's Thread-safe.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public abstract class DateFormat {

	public DateFormat(Locale locale) {
		this.locale = locale;
		preparePatterns(getPatterns());
	}

	/**
	 * Locale format.
	 */

	protected Locale locale;

	/**
	 * Available patterns list. Used by {@link #findPattern(char[], int)} when
	 * parsing date time format. Each formatter will have its own set of
	 * patterns, in strictly defined order.
	 */
	protected char[][] patterns;

	/**
	 * Escape character.
	 */
	protected char escapeChar = '\'';

	/**
	 * Converts String array of patterns to char arrays.
	 */
	protected void preparePatterns(String[] spat) {
		patterns = new char[spat.length][];
		for (int i = 0; i < spat.length; i++) {
			patterns[i] = spat[i].toCharArray();
		}
	}

	/**
	 * Get patterns.
	 */

	protected abstract String[] getPatterns();

	/**
	 * Finds the longest pattern in provided format starting from specified
	 * position. All available patterns are stored in {@link #patterns}.
	 * 
	 * @param format
	 *            date time format to examine
	 * @param i
	 *            starting index
	 * 
	 * @return 0-based index of founded pattern, or <code>-1</code> if pattern
	 *         not found
	 */
	protected int findPattern(char[] format, int i) {
		int frmtc_len = format.length;
		boolean match;
		int n, lastn = -1;
		int maxLen = 0;
		for (n = 0; n < patterns.length; n++) {
			char[] curr = patterns[n]; // current pattern from the pattern list
			if (i > frmtc_len - curr.length) {
				continue;
			}
			match = true;
			int delta = 0;
			while (delta < curr.length) { // match given pattern
				if (curr[delta] != format[i + delta]) {
					match = false; // no match, go to next
					break;
				}
				delta++;
			}
			if (match == true) { // match
				if (patterns[n].length > maxLen) { // find longest match
					lastn = n;
					maxLen = patterns[n].length;
				}
			}
		}
		return lastn;
	}

	// ---------------------------------------------------------------- convert

	/**
	 * Converts date time to a string using specified format.
	 * 
	 * @param date
	 *            date time to read from
	 * @param format
	 *            pattern string format defined.
	 * @return formatted string with date time information
	 */
	public String format(Calendar calendar, String format) {
		char[] formatChars = format.toCharArray();
		int fmtc_len = formatChars.length;
		StringBuilder result = new StringBuilder(fmtc_len);

		int i = 0;
		while (i < fmtc_len) {
			if (formatChars[i] == escapeChar) { // quote founded
				int end = i + 1;
				while (end < fmtc_len) {
					if (formatChars[end] == escapeChar) { // second quote
															// founded
						if (end + 1 < fmtc_len) {
							end++;
							if (formatChars[end] == escapeChar) { // skip double
																	// quotes
								result.append(escapeChar); // and continue
							} else {
								break;
							}
						}
					} else {
						result.append(formatChars[end]);
					}
					end++;
				}
				i = end;
				continue; // end of quoted string, continue the main loop
			}

			int n = findPattern(formatChars, i);
			if (n != -1) { // pattern founded
				result.append(convertPattern(n, calendar));
				i += patterns[n].length;
			} else {
				result.append(formatChars[i]);
				i++;
			}
		}
		return result.toString();
	}

	/**
	 * Creates a date-time string for founded pattern. Founded patterns is
	 * identified by its {@link #patterns} index.
	 * 
	 * @param patternIndex
	 *            index of founded pattern
	 * @param calendar
	 *            date time information
	 */

	protected abstract String convertPattern(int patternIndex, Calendar calendar);

	/**
	 * @see DateFormat#format(Calendar,String)
	 */

	public String format(Date date, String format) {
		return format(DateUtils.dateToCalendar(date), format);
	}

	// ---------------------------------------------------------------- parse

	/**
	 * Parses string given in specified format and extracts time information. It
	 * returns a new instance of <code>Date</code> or <code>null</code> if error
	 * occurs.
	 * 
	 * @param value
	 *            string containing date time values
	 * @param format
	 *            pattern string format defined.
	 * @return Date instance with populated data
	 */

	public Calendar parse(String value, String format) {
		char[] valueChars = value.toCharArray();
		char[] formatChars = format.toCharArray();

		int i = 0, j = 0;
		int valueLen = valueChars.length;

		int formatLen = formatChars.length;

		// detect if separators are used
		boolean useSeparators = true;

		if (valueLen == formatLen) {
			useSeparators = false;

			for (char valueChar : valueChars) {
				if (Character.isDigit(valueChar) == false) {
					useSeparators = true;
					break;
				}
			}
		}

		DateTimeZoneStamp time = new DateTimeZoneStamp();
		StringBuilder sb = new StringBuilder();
		while (true) {
			int n = findPattern(formatChars, i);
			if (n != -1) { // pattern founded
				int patternLen = patterns[n].length;
				i += patternLen;
				sb.setLength(0);
				if (useSeparators == false) {
					for (int k = 0; k < patternLen; k++) {
						sb.append(valueChars[j++]);
					}
				} else {
					char next = 0xFFFF;
					if (i < formatLen) {
						next = formatChars[i]; // next = delimiter
					}
					while ((j < valueLen) && (valueChars[j] != next)) {
						char scj = valueChars[j];
						if ((scj != ' ') && (scj != '\t')) { // ignore
																// surrounding
																// whitespaces
							sb.append(valueChars[j]);
						}
						j++;
					}
				}
				parseValue(n, sb.toString(), time);
			} else {
				if (!useSeparators) {
					throw new IllegalArgumentException("Invalid value: " + value);
				}
				if (formatChars[i] == valueChars[j]) {
					j++;
				}
				i++;
			}
			if ((i == formatLen) || (j == valueLen)) {
				break;
			}
		}
		return time.toCalendar();
	}

	/**
	 * Parses value for matched pattern. Founded patterns is identified by its
	 * {@link #patterns} index. Note that value may represent both integer and
	 * decimals.
	 * 
	 * @param patternIndex
	 *            index of founded pattern
	 * @param value
	 *            value to parse, no spaces or tabs
	 * @param destination
	 *            destination to store date-time
	 */

	protected abstract void parseValue(int patternIndex, String value, DateTimeZoneStamp destination);

	/**
	 * @see DateFormat#parse(String,String)
	 */
	public Date parseToDate(String value, String format) {
		return parse(value, format).getTime();
	}
	
	public LocalDate parserToLocalDate(String value, String format) {
		return parse(value, format).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public LocalDateTime parserToLocalDateTime(String value, String format) {
		return parse(value, format).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	} 

}