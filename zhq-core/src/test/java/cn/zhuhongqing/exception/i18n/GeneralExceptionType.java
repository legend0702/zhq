package cn.zhuhongqing.exception.i18n;

import cn.zhuhongqing.i18n.I18nResource;

/**
 * 常规异常的异常类型
 * 
 * @author zhq mail:qwepoidjdj(a)hotmail.com
 * @serial 1.7
 * @version $Id$
 * 
 */

public enum GeneralExceptionType implements ExceptionType {

	/**
	 * Unsupported type 不支持的类型
	 */
	DEBUG1000("DEBUG1000"),
	/**
	 * Parameter can not be null 参数不能为空
	 */
	DEBUG1001("DEBUG1001"),
	/**
	 * not found 找不到
	 */
	DEBUG1002("DEBUG1002");

	private String messageKey;

	GeneralExceptionType(String messageKey) {
		this.messageKey = messageKey;
	}

	@Override
	public I18nResource getExceptionResource() {
		return ExceptionResource.getI18nResource();
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

}
