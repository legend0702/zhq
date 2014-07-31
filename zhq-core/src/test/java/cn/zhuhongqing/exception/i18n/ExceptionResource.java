package cn.zhuhongqing.exception.i18n;

import java.util.ResourceBundle;

import cn.zhuhongqing.i18n.I18nResource;

/**
 * 专门处理异常信息的国际化信息管理
 * 
 * 对异常类型进行分类
 * 
 * @author zhq mail:qwepoidjdj(a)gmail.com
 * @version $Id: ExceptionResource.java 20 2014-01-07 11:17:36Z legend $
 * @since 1.5
 */

public class ExceptionResource extends I18nResource {

	/**
	 * 加载的国际化文件名
	 */

	public final static String EXCEPTION_MESSAGE = "exceptionMessage";

	/**
	 * 实现单例模式
	 */

	private static ExceptionResource exceptionResource = null;

	private ExceptionResource() {
		super();
	}

	/**
	 * 加载配置文件
	 */

	@Override
	protected ResourceBundle initResourceBundle() {
		ResourceBundle resourceBundle = createResourceBundle(
				ExceptionResource.class, EXCEPTION_MESSAGE);
		if (resourceBundle == null) {
			resourceBundle = createResourceBundle(ExceptionResource.class,
					EXCEPTION_MESSAGE, en_US_Locale);
		}
		return resourceBundle;
	}

	/**
	 * 实现message接口
	 * 
	 * 提供获取message的方法
	 */

	public String getMessage(String messageKey) {
		return getMessage(ExceptionResource.class, messageKey);
	}

	/**
	 * 单例
	 * 
	 * 懒汉式
	 */

	public static I18nResource getI18nResource() {
		if (exceptionResource == null) {
			synchronized (MESSAGE_FILE_NAME) {
				exceptionResource = new ExceptionResource();
			}
		}
		return exceptionResource;
	}
}
