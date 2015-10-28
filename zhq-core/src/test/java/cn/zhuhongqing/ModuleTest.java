package cn.zhuhongqing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import cn.zhuhongqing.utils.DateUtil;
import cn.zhuhongqing.utils.date.GenericDateFormat;
import cn.zhuhongqing.utils.date.Iso8601DateFormat;
import cn.zhuhongqing.utils.date.UTCDateFormat;

public class ModuleTest {

	public static final String GENERIC_FORMAT = "G yyyy/yy-M/MM/MMM/MML-w/ww/W-D/DDD-d/dd-E/EEE/EEL-F-a-h/hh/k/kk-H/HH/K/KK-m/mm-s/ss-S/SSS-z/zzzz-Z";

	public static final String SIMPLE_FORMAT = "G yyyy/yy-M/MM/MMM-w/W-D/d/dd-F-E/EEE/EEEE-a-H-m-s-SSS-z/zzzz-Z X/XX/XXX";

	// "EEE, dd MMM yyyy HH:mm:ss a z zzzz"
	public static final SimpleDateFormat HTTP_DATE_FORMAT = new SimpleDateFormat(
			SIMPLE_FORMAT, Locale.UK);

	@Test
	public void showTimeZoneAndLocale() {
		System.out.println("TimeZone-------------------------------------");
		for (String zone : TimeZone.getAvailableIDs()) {
			TimeZone timeZone = TimeZone.getTimeZone(zone);
			System.out.println(timeZone.getID());
			System.out.println(timeZone.getDisplayName());
			System.out.println(timeZone.getDisplayName(false, TimeZone.SHORT));
		}
		// System.out.println("Locale---------------------------------------");
		// for (Locale locale : Locale.getAvailableLocales()) {
		// System.out.println(locale.toLanguageTag());
		// System.out.println(locale.getCountry());
		// System.out.println(locale.getDisplayCountry());
		// System.out.println("Loacle+++++++++++++++++++++++++++++++++++");
		// }
	}

	@Test
	public void showTimeISO8601() {
		String _format = "YYYY-MM-MML-MMS-DD-DL-DS-D-DDD/WW/WWW/W E hh:mm:ss.mss TZL/TZS";
		// "YYYY-MM-DD hh:mm:ss.mss TZS";
		Iso8601DateFormat dateFormat = new Iso8601DateFormat();
		Calendar date = DateUtil.getCurrentCalendar();
		String format = dateFormat.format(date, _format);
		System.out.println(format);
		// date = dateFormat.parse(format, _format);
		// System.out.println(dateFormat.format(date, _format));
	}

	@Test
	public void showUTC() {
		String _format = "YYYY/MM-DD T HH:mm:SS";
		UTCDateFormat dateFormat = new UTCDateFormat();
		Calendar calendar = DateUtil.getCurrentCalendar();
		String calendarFormat = dateFormat.format(calendar, _format);
		System.out.println(calendarFormat);
		// Core.TIME_ZONE = "America/New_York";
		String dateStr = calendarFormat + " -07";
		calendar = dateFormat.parse(dateStr, _format += " Z");
		String format = dateFormat.format(calendar, _format);
		System.out.println(format);
	}

	@Test
	public void showFirstWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
		System.out.println(calendar.get(Calendar.DAY_OF_YEAR));
		System.out.println(calendar.get(Calendar.ZONE_OFFSET));
		System.out.println(calendar.getTimeZone().getDisplayName(false,
				TimeZone.SHORT));
		System.out.println(calendar.getTime());
	}

	@Test
	public void showGenericDateFormat() {
		// America/New_York
		Core.TIME_ZONE = TimeZone.getTimeZone("GMT+1130").getID();
		Core.LOCALE_LANGUAGE_TAG = Locale.UK.toLanguageTag();
		GenericDateFormat dateFormat = new GenericDateFormat();
		Calendar calendar = DateUtil.getCurrentCalendar();
		System.out.println(dateFormat.format(calendar, SIMPLE_FORMAT));
		HTTP_DATE_FORMAT.setTimeZone(calendar.getTimeZone());
		System.out.println(HTTP_DATE_FORMAT.format(calendar.getTime()));
	}

	@Test
	public void showRawoff() {
		Calendar calendar = DateUtil.getCurrentCalendar();
		int raw = calendar.get(Calendar.ZONE_OFFSET)
				+ calendar.get(Calendar.DST_OFFSET);
		System.out.println(calendar.get(Calendar.DST_OFFSET));
		System.out.println(raw);
		System.out.println(calendar.getTimeZone().getDSTSavings());
		System.out.println(calendar.getTimeZone().getRawOffset());
	}

	public static void main(String[] args) {
		String _format = "YYYY-MM-MML-MMS-DD-DL-DS-D-DDD/WW/WWW/W E hh:mm:ss.mss TZL/TZS";
		Iso8601DateFormat dateFormat = new Iso8601DateFormat();
		GenericDateFormat GDateFormat = new GenericDateFormat();
		// America/Los_Angeles GMT-07:30
		// "America/Los_Angeles"
		TimeZone timeZone = TimeZone.getTimeZone("GMT+4");// GMT+4:30
		Calendar calendar = Calendar.getInstance(timeZone);
		HTTP_DATE_FORMAT.setTimeZone(calendar.getTimeZone());
		// calendar.set(Calendar.MONTH, 2);
		// System.out.println(TimeZoneUtil.getRFC822TimeZone(calendar));
		System.out.println(dateFormat.format(calendar, _format));
		System.out.println(GDateFormat.format(calendar, SIMPLE_FORMAT));
		System.out.println(HTTP_DATE_FORMAT.format(calendar.getTime()));
	}

	// @Test
	// public void staticMethod() {
	// Object obj = ReflectUtil.invoke(ReflectUtil.class, "autoNewInstance",
	// Person.class);
	// System.out.println(obj);
	// }
}
