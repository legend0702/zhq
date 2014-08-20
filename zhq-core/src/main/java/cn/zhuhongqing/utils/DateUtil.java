package cn.zhuhongqing.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Some utilities for date.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class DateUtil {

	public static Calendar getCurrentCalendar() {
		return Calendar.getInstance(TimeZoneUtil.getTimeZone(),
				LocaleUtil.getLocale());
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
}
