package cn.zhuhongqing.util.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

import cn.zhuhongqing.util.TimeZoneUtils;

/**
 * Generic date time stamp just stores and holds date and time information. This
 * class does not contain any date/time logic, neither guarantees that date is
 * valid.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */
public class DateTimeZoneStamp implements Comparable<DateTimeZoneStamp>,
		Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	/**
	 * Default empty constructor.
	 */
	public DateTimeZoneStamp() {
	}

	/**
	 * Constructor that sets date and time.
	 */
	public DateTimeZoneStamp(int year, int month, int day, int hour,
			int minute, int second, int millisecond) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.millisecond = millisecond;
	}

	/**
	 * Constructor that sets just date. Time is set to zeros.
	 */
	public DateTimeZoneStamp(int year, int month, int day) {
		this(year, month, day, 0, 0, 0, 0);
	}

	/**
	 * Year.
	 */
	int year;

	/**
	 * Month, range: [0 - 11]
	 */
	int month = 0;

	/**
	 * Day, range: [1 - 31]
	 */
	int day = 1;

	/**
	 * Hour, range: [0 - 23]
	 */
	int hour;

	/**
	 * Minute, range [0 - 59]
	 */
	int minute;

	/**
	 * Second, range: [0 - 59]
	 */
	int second;

	/**
	 * Millisecond, range: [0 - 1000]
	 */
	int millisecond;

	/**
	 * @see TimeZone#getTimeZone(String)
	 */

	String shortTimeZone;

	// ---------------------------------------------------------------- get/set

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getMillisecond() {
		return millisecond;
	}

	public void setMillisecond(int millisecond) {
		this.millisecond = millisecond;
	}

	public String getShortTimeZone() {
		return shortTimeZone;
	}

	public void setShortTimeZone(String shortTimeZone) {
		this.shortTimeZone = shortTimeZone;
	}

	// ---------------------------------------------------------------- compare

	/**
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 * 
	 * @param o
	 *            the Object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException
	 *             if the specified object's type prevents it from being
	 *             compared to this Object.
	 */
	@Override
	public int compareTo(DateTimeZoneStamp o) {
		DateTimeZoneStamp dts = o;

		int date1 = year * 10000 + month * 100 + day;
		int date2 = dts.year * 10000 + dts.month * 100 + dts.day;

		if (date1 < date2) {
			return -1;
		}
		if (date1 > date2) {
			return 1;
		}

		date1 = (hour * 10000000) + (minute * 100000) + (second * 1000)
				+ millisecond;
		date2 = (dts.hour * 10000000) + (dts.minute * 100000)
				+ (dts.second * 1000) + dts.millisecond;

		if (date1 < date2) {
			return -1;
		}
		if (date1 > date2) {
			return 1;
		}
		return 0;
	}

	/**
	 * Compares just date component of two date time stamps.
	 */
	public int compareDateTo(Object o) {
		DateTimeZoneStamp dts = (DateTimeZoneStamp) o;

		int date1 = year * 10000 + month * 100 + day;
		int date2 = dts.year * 10000 + dts.month * 100 + dts.day;

		if (date1 < date2) {
			return -1;
		}
		if (date1 > date2) {
			return 1;
		}
		return 0;
	}

	// ---------------------------------------------------------------- toString

	/**
	 * Simple to string conversion.
	 * 
	 * @return date/time string in 'y-m-d h:m:m.s' format
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(25);
		sb.append(year).append('-').append(month).append('-').append(day)
				.append(' ');
		sb.append(hour).append(':').append(minute).append(':').append(second)
				.append('.').append(millisecond).append(" ")
				.append(shortTimeZone);
		return sb.toString();
	}

	// ---------------------------------------------------------------- equals &
	// hashCode

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + hour;
		result = prime * result + millisecond;
		result = prime * result + minute;
		result = prime * result + month;
		result = prime * result + second;
		result = prime * result
				+ ((shortTimeZone == null) ? 0 : shortTimeZone.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateTimeZoneStamp other = (DateTimeZoneStamp) obj;
		if (day != other.day)
			return false;
		if (hour != other.hour)
			return false;
		if (millisecond != other.millisecond)
			return false;
		if (minute != other.minute)
			return false;
		if (month != other.month)
			return false;
		if (second != other.second)
			return false;
		if (shortTimeZone == null) {
			if (other.shortTimeZone != null)
				return false;
		} else if (!shortTimeZone.equals(other.shortTimeZone))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	// ---------------------------------------------------------------- clone

	@Override
	protected DateTimeZoneStamp clone() {
		DateTimeZoneStamp dts = new DateTimeZoneStamp();
		dts.year = this.year;
		dts.month = this.month;
		dts.day = this.day;
		dts.hour = this.hour;
		dts.minute = this.minute;
		dts.second = this.second;
		dts.millisecond = this.millisecond;
		dts.shortTimeZone = this.shortTimeZone;
		return dts;
	}

	// ---------------------------------------------------------------- equals

	public boolean isEqualDate(DateTimeZoneStamp date) {
		return date.day == this.day && date.month == this.month
				&& date.year == this.year;
	}

	public boolean isEqualTime(DateTimeZoneStamp time) {
		return time.hour == this.hour && time.minute == this.minute
				&& time.second == this.second
				&& time.millisecond == this.millisecond;
	}

	public Calendar toCalendar() {
		TimeZone timeZone = shortTimeZone == null ? TimeZoneUtils.getTimeZone()
				: TimeZone.getTimeZone(shortTimeZone);
		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.setTimeZone(timeZone);
		calendar.set(year, month, day, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, millisecond);
		return calendar;
	}
}
