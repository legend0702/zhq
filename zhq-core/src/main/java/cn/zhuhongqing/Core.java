package cn.zhuhongqing;

import java.util.Locale;
import java.util.TimeZone;

import cn.zhuhongqing.module.Module;
import cn.zhuhongqing.module.ModuleStore;

/**
 * Project Core.
 * 
 * Global define here.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 * 
 */

public class Core extends Module {

	/**
	 * module-name.
	 */

	public static String CORE = Module.getModuleName(Core.class);

	/**
	 * Get {@link CORE} which is in {@link ModuleStore}.
	 */

	public static Core getModule() {
		return ModuleStore.getModule(Core.class);
	}

	/**
	 * Default byte size (4 KB).
	 */

	public static int BYTE_SIZE = 4096;

	/**
	 * Default TimeZone,use id like "Asia/Shanghai".
	 */

	public static String TIME_ZONE = TimeZone.getDefault().getID();

	/**
	 * Used to create Locale,like "zh-CN".
	 */

	public static String LOCALE_LANGUAGE_TAG = Locale.getDefault()
			.toLanguageTag();

	public static int getByteSize() {
		return BYTE_SIZE;
	}

	public static void setByteSize(int byteSize) {
		BYTE_SIZE = byteSize;
	}

	public static String getTimeZone() {
		return TIME_ZONE;
	}

	public static void setTimeZone(String timeZone) {
		TIME_ZONE = timeZone;
	}

	public static String getLocaleLanguageTage() {
		return LOCALE_LANGUAGE_TAG;
	}

	public static void setLocaleLanguageTage(String LanguageTage) {
		LOCALE_LANGUAGE_TAG = LanguageTage;
	}

	@Override
	protected void init() throws Exception {
		LOCALE_LANGUAGE_TAG = "en-US";
	}

}
