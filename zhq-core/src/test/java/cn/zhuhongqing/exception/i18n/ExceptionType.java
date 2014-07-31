package cn.zhuhongqing.exception.i18n;

import cn.zhuhongqing.i18n.I18nResource;

/**
 * 异常类型
 * 
 * 主要提供异常的类型获取
 * 
 * 以及异常提示信息资源
 * 
 * @author zhq mail:qwepoidjdj(a)gmail.com
 * @version $Id: ExceptionType.java 20 2014-01-07 11:17:36Z legend $
 * @since 1.5
 * 
 */

public interface ExceptionType {

	/**
	 * 异常编码获取
	 * 
	 * 用该编码从异常信息资源中获得想要的message
	 * 
	 */

	String getMessageKey();

	/**
	 * 异常信息资源获取
	 * 
	 * 主要提供编码到信息的功能
	 * 
	 */

	I18nResource getExceptionResource();

}
