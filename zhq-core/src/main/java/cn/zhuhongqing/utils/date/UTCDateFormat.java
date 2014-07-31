package cn.zhuhongqing.utils.date;

import java.util.Calendar;

import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.LocaleUtil;
import cn.zhuhongqing.utils.NumberUtil;
import cn.zhuhongqing.utils.TimeZoneUtil;

/**
 * <b>UTC</b> specification, enhanced by some custom patterns. For more
 * information see: <a href="http://en.wikipedia.org/wiki/UTC"></a>
 * 
 * <p>
 * Patterns list:
 * 
 * <ul>
 * <li>YYYY + year</li>
 * <li>MM + month</li>
 * <li>DD + day of month</li>
 * <li>HH + hour of day</li>
 * <li>mm + minute</li>
 * <li>SS + seconds (no milliseconds)</li>
 * <li>Z + ("-/+ZZ" form) timeZone</li>
 * <li>ZZ + ("-/+ZZ:zz" form) timeZone</li>
 * <li>T - default pattern</li>
 * </ul>
 * 
 * <p>
 * Patterns noted with + sign are used both for conversion and parsing. All
 * patterns are used for conversion.
 */

public class UTCDateFormat extends DateFormat {

	private static final String[] patterns = new String[] { 
			"YYYY",// 0 + year
			"MM",// 1 + month
			"DD",// 2 + day of month
			"HH",// 3 + hour
			"mm",// 4 + minute
			"SS",// 5 + seconds
			"Z", // 6 + ("-/+ZZ" form) timeZone
			"ZZ",// 7 + ("-/+ZZ:zz" form) timeZone
			"T" // 8 - default pattern
	};

	public UTCDateFormat() {
		super(LocaleUtil.getLocale());
	}

	@Override
	protected String convertPattern(int patternIndex, Calendar calendar) {
		switch (patternIndex) {
		case 0:
			return String.valueOf((calendar.get(Calendar.YEAR)));
		case 1:
			return GeneralUtil
					.forwardFilling2(calendar.get(Calendar.MONTH) + 1);
		case 2:
			return GeneralUtil.forwardFilling2(calendar.get(Calendar.DATE));
		case 3:
			return GeneralUtil.forwardFilling2(calendar
					.get(Calendar.HOUR_OF_DAY));
		case 4:
			return GeneralUtil.forwardFilling2(calendar.get(Calendar.MINUTE));
		case 5:
			return GeneralUtil.forwardFilling2(calendar.get(Calendar.SECOND));
		case 6:
			return TimeZoneUtil.getRFC822TimeZoneShort(calendar);
		case 7:
			return TimeZoneUtil.getRFC822TimeZone(calendar);
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
		case 3:
			destination.hour = v;
			break;
		case 4:
			destination.minute = v;
			break;
		case 5:
			destination.second = v;
			break;
		case 6:
			destination.shortTimeZone = TimeZoneUtil.getGMTTimeZoneID(value);
			break;
		case 7:
			destination.shortTimeZone = TimeZoneUtil.getGMTTimeZoneID(value);
			break;
		case 8:
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
