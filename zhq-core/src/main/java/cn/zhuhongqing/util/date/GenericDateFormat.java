package cn.zhuhongqing.util.date;

import java.util.Calendar;
import java.util.Locale;

import cn.zhuhongqing.util.LocaleUtils;
import cn.zhuhongqing.util.NumberUtils;
import cn.zhuhongqing.util.StringUtils;
import cn.zhuhongqing.util.TimeZoneUtils;

/**
 * <b>GMT</b> specification, enhanced by some custom patterns. For more
 * information see: <a href="http://en.wikipedia.org/wiki/GMT"></a>
 * 
 * <p>
 * Patterns list:
 * 
 * <ul>
 * <li>G - era</li>
 * <li>yyyy + year</li>
 * <li>yy + yy</li>
 * <li>M + month (1 ~ 12)</li>
 * <li>MM + month (01 ~ 12)</li>
 * <li>MMM - month short name</li>
 * <li>MMMM - month long name</li>
 * <li>w - week of year (1 ~ 58)</li>
 * <li>ww - week of year (01 ~ 58)</li>
 * <li>W - week of month (1 ~ 5)</li>
 * <li>F - week of month (1 ~ 5)</li>
 * <li>D - day of year (1 ~ 366)</li>
 * <li>DDD - day of year (001 ~ 366)</li>
 * <li>d + day of month (1 ~ 31)</li>
 * <li>dd + day of month (01 ~ 31)</li>
 * <li>E - day of week (short name)</li>
 * <li>EE - day of week (1 ~ 7)</li>
 * <li>EEE - day of week short name</li>
 * <li>EEEE - day of week long name</li>
 * <li>a - AM/PM</li>
 * <li>H + hour of day (0 ~ 23)</li>
 * <li>HH + hour of day (00 ~ 23)</li>
 * <li>K + hour of day (1 ~ 24)</li>
 * <li>KK + hour of day (01 ~ 24)</li>
 * <li>h - hour of AM/PM (0 ~ 11)</li>
 * <li>H + hour of day (0 ~ 23)</li>
 * <li>hh - hour of AM/PM (00 ~ 11)</li>
 * <li>k - hour of AM/PM (1 ~ 12)</li>
 * <li>kk - hour of AM/PM (01 ~ 12)</li>
 * <li>m + minute (0 ~ 59)</li>
 * <li>mm + minute (00 ~ 59)</li>
 * <li>s + seconds (0 ~ 59)</li>
 * <li>ss + seconds (00 ~ 59)</li>
 * <li>S + milliseconds (0 ~ 999)</li>
 * <li>SSS + milliseconds (000 ~ 999)</li>
 * <li>z - timeZone short name</li>
 * <li>zzzz - timeZone long name</li>
 * <li>X + ("-/+hh" form) timeZone</li>
 * <li>XX + ("-/+hhmm" form) timeZone</li>
 * <li>XXX + ("-/+hh:mm" form) timeZone</li>
 * <li>Z + ("-/+hhmm" form) timeZone</li>
 * </ul>
 * 
 * <p>
 * Patterns noted with + sign are used both for conversion and parsing. All
 * patterns are used for conversion.
 */

public class GenericDateFormat extends DateFormat {

	private static final String[] patterns = new String[] { "G", // 0 - era
			"yyyy", // 1 + year
			"yy", // 2 - short year
			"M", // 3 + month (1 ~ 12)
			"MM", // 4 + month (01 ~ 12)
			"MMM", // 5 - month short name
			"MMMM", // 6 - month long name
			"w", // 7 - week of year (1 ~ 58)
			"ww", // 8 - week of year (01 ~ 58)
			"W", // 9 - week of month (1 ~ 5)
			"F", // 10 - week of month (1 ~ 5)
			"D", // 11 - day of year (1 ~ 366)
			"DDD", // 12 - day of year (001 ~ 366)
			"d", // 13 + day of month (1 ~ 31)
			"dd", // 14 + day of month (01 ~ 31)
			"E", // 15 - day of week (short name)
			"EE", // 16 - day of week (1 ~ 7)
			"EEE", // 17 - day of week short name
			"EEEE", // 18 - day of week long name
			"a", // 19 - AM/PM
			"H", // 20 + hour of day (0 ~ 23)
			"HH", // 21 + hour of day (00 ~ 23)
			"K", // 22 + hour of day (1 ~ 24)
			"KK", // 23 + hour of day (01 ~ 24)
			"h", // 24 - hour of AM/PM (0 ~ 11)
			"hh", // 25 - hour of AM/PM (00 ~ 11)
			"k", // 26 - hour of AM/PM (1 ~ 12)
			"kk", // 27 - hour of AM/PM (01 ~ 12)
			"m", // 28 + minute (0 ~ 59)
			"mm", // 29 + minute (00 ~ 59)
			"s", // 30 + seconds (0 ~ 59)
			"ss", // 31 + seconds (00 ~ 59)
			"S", // 32 + milliseconds (0 ~ 999)
			"SSS", // 33 + milliseconds (000 ~ 999)
			"z", // 34 - timeZone short name
			"zzzz", // 35 - timeZone long name
			"X", // 36 + ("-/+hh" form) timeZone
			"XX", // 37 + ("-/+hhmm" form) timeZone
			"XXX", // 38 + ("-/+hh:mm" form) timeZone
			"Z", // 39 + ("-/+hhmm" form) timeZone
	};

	public GenericDateFormat() {
		super(LocaleUtils.getLocale());
	}

	public GenericDateFormat(Locale locale) {
		super(locale);
	}

	@Override
	protected String convertPattern(int patternIndex, Calendar calendar) {
		switch (patternIndex) {
		case 0:
			return LocaleUtils.getEra(calendar.get(Calendar.ERA), locale);
		case 1:
			return Integer.toString((calendar.get(Calendar.YEAR)));
		case 2:
			return StringUtils.forwardFilling4((calendar.get(Calendar.YEAR)))
					.substring(2, 4);
		case 3:
			return Integer.toString(calendar.get(Calendar.MONTH) + 1);
		case 4:
			return StringUtils.forwardFilling2(calendar.get(Calendar.MONTH) + 1);
		case 5:
			return LocaleUtils.getShortMonthNames(calendar.get(Calendar.MONTH),
					locale);
		case 6:
			return LocaleUtils.getMonthNames(calendar.get(Calendar.MONTH),
					locale);
		case 7:
			return Integer.toString(calendar.get(Calendar.WEEK_OF_YEAR));
		case 8:
			return StringUtils.forwardFilling2(calendar
					.get(Calendar.WEEK_OF_YEAR));
		case 9:
			return Integer.toString(calendar.get(Calendar.WEEK_OF_MONTH));
		case 10:
			return Integer.toString(calendar.get(Calendar.WEEK_OF_MONTH));
		case 11:
			return Integer.toString(calendar.get(Calendar.DAY_OF_YEAR));
		case 12:
			return StringUtils.forwardFilling3(calendar
					.get(Calendar.DAY_OF_YEAR));
		case 13:
			return Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		case 14:
			return StringUtils.forwardFilling2(calendar
					.get(Calendar.DAY_OF_MONTH));
		case 15:
			return LocaleUtils.getShortWeekday(
					calendar.get(Calendar.DAY_OF_WEEK), locale);
		case 16:
			return Integer.toString(calendar.get(Calendar.DAY_OF_WEEK));
		case 17:
			return LocaleUtils.getShortWeekday(
					calendar.get(Calendar.DAY_OF_WEEK), locale);
		case 18:
			return LocaleUtils.getWeekday(calendar.get(Calendar.DAY_OF_WEEK),
					locale);
		case 19:
			return LocaleUtils.getAmPm(calendar.get(Calendar.AM_PM), locale);
		case 20:
			return Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		case 21:
			return StringUtils.forwardFilling2(calendar
					.get(Calendar.HOUR_OF_DAY));
		case 22:
			return Integer.toString(calendar.get(Calendar.HOUR_OF_DAY) + 1);
		case 23:
			return StringUtils.forwardFilling2(calendar
					.get(Calendar.HOUR_OF_DAY) + 1);
		case 24:
			return Integer.toString(calendar.get(Calendar.HOUR));
		case 25:
			return StringUtils.forwardFilling2(calendar.get(Calendar.HOUR));
		case 26:
			return Integer.toString(calendar.get(Calendar.HOUR) + 1);
		case 27:
			return StringUtils.forwardFilling2(calendar.get(Calendar.HOUR) + 1);
		case 28:
			return Integer.toString(calendar.get(Calendar.MINUTE));
		case 29:
			return StringUtils.forwardFilling2(calendar.get(Calendar.MINUTE));
		case 30:
			return Integer.toString(calendar.get(Calendar.SECOND));
		case 31:
			return StringUtils.forwardFilling2(calendar.get(Calendar.SECOND));
		case 32:
			return Integer.toString(calendar.get(Calendar.MILLISECOND));
		case 33:
			return StringUtils.forwardFilling3(calendar
					.get(Calendar.MILLISECOND));
		case 34:
			return LocaleUtils.getShortTimeZone(calendar.getTime(),
					calendar.getTimeZone(), locale);
		case 35:
			return LocaleUtils.getLongTimeZone(calendar.getTime(),
					calendar.getTimeZone(), locale);
		case 36:
			return TimeZoneUtils.getRFC822TimeZoneShort(calendar);
		case 37:
			return TimeZoneUtils.getRFC822TimeZone(calendar);
		case 38:
			return TimeZoneUtils.getRFC822TimeZoneColon(calendar);
		case 39:
			return TimeZoneUtils.getRFC822TimeZone(calendar);
		default:
			return new String(patterns[patternIndex]);
		}
	}

	@Override
	protected void parseValue(int patternIndex, String value,
			DateTimeZoneStamp destination) {
		Integer v = null;
		if (NumberUtils.isNumber(value)) {
			v = Integer.parseInt(value);
		}
		switch (patternIndex) {
		case 1:
			destination.year = v;
			break;
		case 2:
			destination.year = v;
			break;
		case 3:
			destination.month = v - 1;
			break;
		case 4:
			destination.month = v - 1;
			break;
		case 13:
			destination.day = v;
			break;
		case 14:
			destination.day = v;
			break;
		case 20:
			destination.hour = v;
			break;
		case 21:
			destination.hour = v;
			break;
		case 22:
			destination.hour = v - 1;
			break;
		case 23:
			destination.hour = v - 1;
			break;
		case 28:
			destination.minute = v;
			break;
		case 29:
			destination.minute = v;
			break;
		case 30:
			destination.second = v;
			break;
		case 31:
			destination.second = v;
			break;
		case 32:
			destination.millisecond = v;
			break;
		case 33:
			destination.millisecond = v;
			break;
		case 36:
			destination.shortTimeZone = TimeZoneUtils.getGMTTimeZoneID(value);
			break;
		case 37:
			destination.shortTimeZone = TimeZoneUtils.getGMTTimeZoneID(value);
			break;
		case 38:
			destination.shortTimeZone = TimeZoneUtils.getGMTTimeZoneID(value);
			break;
		case 39:
			destination.shortTimeZone = TimeZoneUtils.getGMTTimeZoneID(value);
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
