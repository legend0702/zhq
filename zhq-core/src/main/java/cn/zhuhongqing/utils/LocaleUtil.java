package cn.zhuhongqing.utils;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cn.zhuhongqing.Core;

/**
 * Some utilities for {@link Locale} and {@link TimeZone}
 * 
 * If method's parameters has {@link Locale},put one most,please.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class LocaleUtil {

	public static Locale getLocale() {
		return Locale.forLanguageTag(Core.LOCALE_LANGUAGE_TAG);
	}

	/**
	 * Get local-Date-Format.
	 */

	private static DateFormatSymbols createDateFormat(Locale... locales) {
		return DateFormatSymbols
				.getInstance((locales != null && locales.length == 1) ? locales[0]
						: getLocale());
	}

	/**
	 * Get monthNames.
	 * 
	 * Range: [0 - 11]
	 */

	public static String getMonthNames(int index, Locale... locale) {
		return createDateFormat(locale).getMonths()[index];
	}

	/**
	 * Get shortMonthNames.
	 * 
	 * Range: [0 - 11]
	 */

	public static String getShortMonthNames(int index, Locale... locale) {
		return createDateFormat(locale).getShortMonths()[index];
	}

	/**
	 * Get weekday.
	 * 
	 * Range: [1 - 7]
	 */
	public static String getWeekday(int index, Locale... locale) {
		return createDateFormat(locale).getWeekdays()[index];
	}

	/**
	 * Get short weekday.
	 * 
	 * Range: [1 - 7]
	 */
	public static String getShortWeekday(int index, Locale... locale) {
		return createDateFormat(locale).getShortWeekdays()[index];
	}

	/**
	 * Get era.
	 * 
	 * Range: [0 - 1]
	 */

	public static String getEra(int index, Locale... locale) {
		return createDateFormat(locale).getEras()[index];
	}

	/**
	 * Get BC era.
	 */
	public static String getBcEra(Locale... locale) {
		return getEra(0, locale);
	}

	/**
	 * Get AD era.
	 */
	public static String getAdEra(Locale... locale) {
		return getEra(1, locale);
	}

	/**
	 * Get AM or PM.
	 * 
	 * 0 is AM,1 is PM.
	 */

	public static String getAmPm(int index, Locale... locale) {
		return createDateFormat(locale).getAmPmStrings()[index];
	}

	/**
	 * Get AM.
	 */
	public static String getAM(Locale... locale) {
		return getAmPm(0, locale);
	}

	/**
	 * Get PM.
	 */
	public static String getPM(Locale... locale) {
		return getAmPm(1, locale);
	}

	/**
	 * @see DateUtil#getCurrentDate()
	 * @see LocaleUtil#getShortTimeZone(Date)
	 */

	public static String getShortTimeZone() {
		return getShortTimeZone(DateUtil.getCurrentDate());
	}

	/**
	 * @param date
	 *            judge isInDaylightTime
	 * @see LocaleUtil#getTimeZone()
	 * @see LocaleUtil#getLocale()
	 * @see LocaleUtil#getShortTimeZone(Date, TimeZone, Locale)
	 */

	public static String getShortTimeZone(Date date) {
		return getShortTimeZone(date, TimeZoneUtil.getTimeZone(), getLocale());
	}

	/**
	 * Judge isInDaylightTime by date,timeZone.
	 * 
	 * Get short-Time-Zone's format by locale.
	 * 
	 * @param date
	 * @param timeZone
	 * @param locale
	 */

	public static String getShortTimeZone(Date date, TimeZone timeZone,
			Locale locale) {
		return timeZone.getDisplayName(timeZone.inDaylightTime(date),
				TimeZone.SHORT, locale);
	}

	/**
	 * @see DateUtil#getCurrentDate()
	 * @see LocaleUtil#getLongTimeZone(Date)
	 */

	public static String getLongTimeZone() {
		return getLongTimeZone(DateUtil.getCurrentDate());
	}

	/**
	 * @see LocaleUtil#getTimeZone()
	 * @see LocaleUtil#getLocale()
	 * @see LocaleUtil#getLongTimeZone(Date, TimeZone, Locale)
	 */

	public static String getLongTimeZone(Date date) {
		return getLongTimeZone(date, TimeZoneUtil.getTimeZone(), getLocale());
	}

	/**
	 * Judge isInDaylightTime by date,timeZone.
	 * 
	 * Get long-Time-Zone's format by locale.
	 * 
	 * @param date
	 * @param timeZone
	 * @param locale
	 */

	public static String getLongTimeZone(Date date, TimeZone timeZone,
			Locale locale) {
		return timeZone.getDisplayName(timeZone.inDaylightTime(date),
				TimeZone.LONG, locale);
	}
}
