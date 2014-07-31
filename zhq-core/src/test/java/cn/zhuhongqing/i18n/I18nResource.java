package cn.zhuhongqing.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import cn.zhuhongqing.utils.GeneralUtil;

public abstract class I18nResource implements Message {

	public final static Locale zh_CN_Locale = new Locale("zh", "CN");

	public final static Locale en_US_Locale = new Locale("en", "US");

	public final static String MESSAGE_FILE_NAME = "message";

	private static Map<Class<? extends I18nResource>, ResourceBundle> resourceBundleMap = new HashMap<Class<? extends I18nResource>, ResourceBundle>();

	protected I18nResource() {
		registResourceBundle(this.getClass(), initResourceBundle());
	}

	public static ResourceBundle getResourceBundle(
			Class<? extends I18nResource> i18nClass) {
		return resourceBundleMap.get(i18nClass);
	}

	public static void registResourceBundle(
			Class<? extends I18nResource> i18nClass,
			ResourceBundle resourceBundle) {
		resourceBundleMap.put(i18nClass, resourceBundle);
	}

	public static String getMessage(Class<? extends I18nResource> i18nClass,
			String messageKey) {
		return getResourceBundle(i18nClass).getString(messageKey);
	}

	protected abstract ResourceBundle initResourceBundle();

	protected static ResourceBundle createResourceBundle(String messageFileName) {

		return createResourceBundle(null, messageFileName, null);
	}

	protected static ResourceBundle createResourceBundle(
			String messageFileName, Locale locale) {

		return createResourceBundle(null, messageFileName, locale);
	}

	protected static ResourceBundle createResourceBundle(Class<?> rootClass,
			String messageFileName) {

		return createResourceBundle(rootClass, messageFileName, null);

	}

	protected static ResourceBundle createResourceBundle(Class<?> rootClass,
			String messageFileName, Locale locale) {

		if (messageFileName == null) {
			throw new IllegalArgumentException(messageFileName + ":not found");
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}

		if (rootClass == null) {
			return ResourceBundle.getBundle(messageFileName, locale);
		}

		return ResourceBundle.getBundle(GeneralUtil.getClassRootPath(rootClass)
				+ messageFileName, locale);
	}

}
