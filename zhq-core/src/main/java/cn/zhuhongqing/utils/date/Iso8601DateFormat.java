package cn.zhuhongqing.utils.date;

import java.util.Calendar;
import java.util.Locale;

import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.LocaleUtil;
import cn.zhuhongqing.utils.NumberUtil;

/**
 * <b>ISO 8601</b> specification, enhanced by some custom patterns. For more
 * information see: <a href="http://en.wikipedia.org/wiki/ISO_8601">ISO 8601 on
 * Wikipedia</a>
 * 
 * <p>
 * Patterns list:
 * 
 * <ul>
 * <li>YYYY + year</li>
 * <li>MM + month</li>
 * <li>DD + day of month</li>
 * <li>D - day of week</li>
 * <li>MML - month name (add-on)</li>
 * <li>MMS - month abbreviation (add-on)</li>
 * <li>DL - day of week name (add-on)</li>
 * <li>DS - day of week abbreviation (add-on)</li>
 * <li>hh + hour</li>
 * <li>mm + minute</li>
 * <li>ss + seconds (no milliseconds)</li>
 * <li>mss + milliseconds (add-on)</li>
 * <li>DDD - day of year</li>
 * <li>WW - week of year</li>
 * <li>WWW - week of year with 'W' prefix</li>
 * <li>W - week of month (add-on)</li>
 * <li>E - era</li>
 * <li>TLZ - time zone long</li>
 * <li>TLS - time zone short</li>
 * </ul>
 * 
 * <p>
 * Patterns noted with + sign are used both for conversion and parsing. All
 * patterns are used for conversion.
 */

public class Iso8601DateFormat extends DateFormat {

	private static final String[] patterns = new String[] { "YYYY", // 0 + year
			"MM", // 1 + month
			"DD", // 2 + day of month
			"D", // 3 - day of week
			"MML", // 4 - month long name
			"MMS", // 5 - month short name
			"DL", // 6 - day of week long name
			"DS", // 7 - day of week short name
			"hh", // 8 + hour of day
			"mm", // 9 + minute
			"ss", // 10 + seconds
			"mss", // 11 + milliseconds
			"DDD", // 12 - day of year
			"WW", // 13 - week of year
			"WWW", // 14 - week of year with 'W' prefix
			"W", // 15 - week of month
			"E", // 16 - era
			"TZL", // 17 - timeZone long name
			"TZS", // 18 - timeZone short name
	};

	public Iso8601DateFormat() {
		super(LocaleUtil.getLocale());
	}

	public Iso8601DateFormat(Locale locale) {
		super(locale);
	}

	@Override
	protected String convertPattern(int patternIndex, Calendar calendar) {
		switch (patternIndex) {
		case 0:
			return String.valueOf((calendar.get(Calendar.YEAR)));
		case 1:
			return StringUtil.forwardFilling2(calendar.get(Calendar.MONTH) + 1);
		case 2:
			return StringUtil.forwardFilling2(calendar.get(Calendar.DATE));
		case 3:
			return Integer.toString(calendar.get(Calendar.DAY_OF_WEEK));
		case 4:
			return LocaleUtil.getMonthNames(calendar.get(Calendar.MONTH),
					locale);
		case 5:
			return LocaleUtil.getShortMonthNames(calendar.get(Calendar.MONTH),
					locale);
		case 6:
			return LocaleUtil.getWeekday(calendar.get(Calendar.DAY_OF_WEEK),
					locale);
		case 7:
			return LocaleUtil.getShortWeekday(
					calendar.get(Calendar.DAY_OF_WEEK), locale);
		case 8:
			return StringUtil.forwardFilling2(calendar
					.get(Calendar.HOUR_OF_DAY));
		case 9:
			return StringUtil.forwardFilling2(calendar.get(Calendar.MINUTE));
		case 10:
			return StringUtil.forwardFilling2(calendar.get(Calendar.SECOND));
		case 11:
			return StringUtil.forwardFilling3(calendar
					.get(Calendar.MILLISECOND));
		case 12:
			return StringUtil.forwardFilling3(calendar
					.get(Calendar.DAY_OF_YEAR));
		case 13:
			return StringUtil.forwardFilling2(calendar
					.get(Calendar.WEEK_OF_YEAR));
		case 14:
			return 'W' + StringUtil.forwardFilling2(calendar
					.get(Calendar.WEEK_OF_YEAR));
		case 15:
			return Integer.toString(calendar.get(Calendar.WEEK_OF_MONTH));
		case 16:
			return LocaleUtil.getEra(calendar.get(Calendar.ERA), locale);
		case 17:
			return LocaleUtil.getLongTimeZone(calendar.getTime(),
					calendar.getTimeZone(), locale);
		case 18:
			return LocaleUtil.getShortTimeZone(calendar.getTime(),
					calendar.getTimeZone(), locale);
		default:
			return new String(patterns[patternIndex]);
		}
	}

	@Override
	protected void parseValue(int patternIndex, String value,
			DateTimeZoneStamp destination) {
		Integer v = null;
		if (NumberUtil.isNumber(value)) {
			v = Integer.parseInt(value);
		}
		switch (patternIndex) {
		case 0:
			destination.year = v;
			break;
		case 1:
			destination.month = v - 1;
			break;
		case 2:
			destination.day = v;
			break;
		case 8:
			destination.hour = v;
			break;
		case 9:
			destination.minute = v;
			break;
		case 10:
			destination.second = v;
			break;
		case 11:
			destination.millisecond = v;
			break;
		case 18:
			destination.shortTimeZone = value;
			break;
		default:
			throw new IllegalArgumentException("Invalid template: "
					+ new String(patterns[patternIndex]));
		}
	}

	@Override
	protected String[] getPatterns() {
		return patterns;
	}

}
