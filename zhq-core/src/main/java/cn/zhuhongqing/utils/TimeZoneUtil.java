package cn.zhuhongqing.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cn.zhuhongqing.Core;

/**
 * Utilities for {@link TimeZone}.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 * @see DateUtil
 * @see LocaleUtil
 */

public class TimeZoneUtil {

	public static final String GMT = "GMT";

	static final int ONE_HOUR_MILLISECONDS = 1000 * 60 * 60;

	/**
	 * Get TimeZone by {@link Core#TIME_ZONE}.
	 */

	public static TimeZone getTimeZone() {
		return TimeZone.getTimeZone(Core.TIME_ZONE);
	}

	public static String getGMTTimeZoneID(String gmt) {
		return TimeZone.getTimeZone(StringUtil.startPad(gmt, GMT)).getID();
	}

	/**
	 * @see TimeZoneUtil#getTimeZone(TimeZone)
	 */

	public static double getTimeZone(Calendar calendar) {
		return getTimeZone(calendar.getTimeZone());
	}

	/**
	 * Get timeZone(RawOffset).
	 * 
	 * Range:[ -12 ~ +12 ]
	 * 
	 * @see TimeZone#getRawOffset()
	 * @see TimeZone#getDSTSavings()
	 */

	public static double getTimeZone(TimeZone timeZone) {
		double value = timeZone.getRawOffset() + timeZone.getDSTSavings();
		value /= ONE_HOUR_MILLISECONDS;
		int valueInt = (int) value;
		if (value != valueInt) {
			// decimal convert to date decimal
			value = valueInt + ((value - valueInt) * 0.6);
		}
		return value;
	}

	/**
	 * General time zone Pacific Standard Time like "GMT-0800".
	 */

	public static String getGMTTimeZone(Calendar calendar) {
		return new StringBuffer().append(GMT)
				.append(getRFC822TimeZone(calendar)).toString();
	}

	/**
	 * General time zone Pacific Standard Time like "GMT-08:00".
	 */

	public static String getGMTTimeZoneColon(Calendar calendar) {
		return new StringBuffer().append(getGMTTimeZone(calendar))
				.insert(6, StringPool.COLON).toString();
	}

	/**
	 * RFC 822 short timeZone like "-08".
	 */
	public static String getRFC822TimeZoneShort(Calendar calendar) {
		return getRFC822TimeZone(calendar).substring(0, 3);
	}

	/**
	 * RFC 822 long timeZone add colon like "-08:00".
	 */

	public static String getRFC822TimeZoneColon(Calendar calendar) {
		return new StringBuffer().append(getRFC822TimeZone(calendar))
				.insert(3, StringPool.COLON).toString();
	}

	/**
	 * RFC 822 long timeZone like "-0800".
	 */
	public static String getRFC822TimeZone(Calendar calendar) {
		StringBuffer buffer = new StringBuffer();
		double value = getTimeZone(calendar);
		if (value >= 0) {
			buffer.append(StringPool.PLUS);
		} else {
			buffer.append(StringPool.MINUS);
			value = -value;
		}
		buffer.append(StringUtil.forwardFilling4((int) (value * 100)));
		return buffer.toString();
	}

	public static boolean isInDaylightTime(Date date) {
		return isInDaylightTime(date, getTimeZone());
	}

	public static boolean isInDaylightTime(Date date, TimeZone timeZone) {
		return timeZone.inDaylightTime(date);
		// long now = date.getTime();
		// int offset = timeZone.getOffset(now);
		// int rawOffset = timeZone.getRawOffset();
		// return (offset != rawOffset);
	}

}
