package cn.zhuhongqing.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import cn.zhuhongqing.util.date.GenericDateFormat;

/**
 * Some utilities for date.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class DateUtils {

	private static final GenericDateFormat DATE_FORMAT = new GenericDateFormat();

	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static Calendar getCurrentCalendar() {
		return Calendar.getInstance(TimeZoneUtils.getTimeZone(), LocaleUtils.getLocale());
	}

	public static Calendar dateToCalendar(Date date) {
		Calendar cal = getCurrentCalendar();
		cal.setTime(date);
		return cal;
	}

	public static Date getCurrentDate() {
		return getCurrentCalendar().getTime();
	}

	public static Date getCurrentDay() {
		Calendar calendar = getCurrentCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static String getCurrentDayStr() {
		return defaultFormat(getCurrentDate());
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(getCurrentCalendar().getTime().getTime());
	}

	public static Date getMonthFirstDay(Date date) {
		Calendar cal = dateToCalendar(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static Date getMonthLastDay(Date date) {
		Calendar cal = dateToCalendar(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return cal.getTime();
	}

	public static Date getQuarterFirstDay(Date date) {
		Calendar c = dateToCalendar(date);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		if (currentMonth > 0 && currentMonth < 4)
			c.set(Calendar.MONTH, 0);
		else if (currentMonth > 3 && currentMonth < 7)
			c.set(Calendar.MONTH, 3);
		else if (currentMonth > 6 && currentMonth < 10)
			c.set(Calendar.MONTH, 4);
		else if (currentMonth > 9 && currentMonth < 13)
			c.set(Calendar.MONTH, 9);
		c.set(Calendar.DATE, 1);
		return c.getTime();
	}

	public static Date getQuarterLastDay(Date date) {
		Calendar c = dateToCalendar(date);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		if (currentMonth > 0 && currentMonth < 4) {
			c.set(Calendar.MONTH, 2);
			c.set(Calendar.DATE, 31);
		} else if (currentMonth > 3 && currentMonth < 7) {
			c.set(Calendar.MONTH, 5);
			c.set(Calendar.DATE, 30);
		} else if (currentMonth > 6 && currentMonth < 10) {
			c.set(Calendar.MONTH, 8);
			c.set(Calendar.DATE, 30);
		} else if (currentMonth > 9 && currentMonth < 13) {
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);
		}
		return c.getTime();
	}

	public static String defaultFormat(Date date) {
		return format(date, YYYY_MM_DD);
	}

	public static String timestampFormat(Date date) {
		return format(date, YYYY_MM_DD_HH_MM_SS);
	}

	public static String format(Date date, String format) {
		return DATE_FORMAT.format(date, format);
	}

	public static Date defaultParse(String date) {
		return parse(date, YYYY_MM_DD);
	}

	public static Date parse(String date, String format) {
		return DATE_FORMAT.parseToDate(date, format);
	}
	
	public static LocalDate parseToLocalDate(String date) {
		return parseToLocalDate(date, YYYY_MM_DD);
	}
	
	public static LocalDate parseToLocalDate(String date, String format) {
		return DATE_FORMAT.parserToLocalDate(date, format);
	}
	
	public static LocalDateTime parserToLocalDateTime(String date, String format) {
		return DATE_FORMAT.parserToLocalDateTime(date, format);
	}

	// JDK8 DATE INSTANT LOCALDATE LOCALDATE TIME

	public static LocalDateTime toLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public static LocalDate toLocalDate(Date date) {
		return toLocalDateTime(date).toLocalDate();
	}

	public static Date toDate(LocalDateTime dateTime) {
		return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date toDate(LocalDate date) {
		return toDate(date.atStartOfDay());
	}

	public static String toString(LocalDate date) {
		return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

}
