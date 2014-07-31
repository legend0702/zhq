package cn.zhuhongqing.exception;

import cn.zhuhongqing.exception.i18n.ExceptionType;

public class ExceptionMessage {

	ExceptionType exceptionType = null;

	String exceptionCode = null;

	String exceptionMessage = null;

	String message = null;

	String defaultMessage = "is wrong";

	public ExceptionMessage(String message) {
		this.message = message;
	}

	public ExceptionMessage(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
		this.exceptionCode = exceptionType.getMessageKey();
		this.exceptionMessage = exceptionType.getExceptionResource()
				.getMessage(this.exceptionCode);
	}

	public ExceptionMessage(ExceptionType exceptionType, String message) {
		this.exceptionType = exceptionType;
		this.exceptionCode = exceptionType.getMessageKey();
		this.exceptionMessage = exceptionType.getExceptionResource()
				.getMessage(this.exceptionCode);
		this.message = message;
	}

	public String toString() {

		StringBuffer stringBuffer = new StringBuffer();

		if (exceptionCode != null) {
			exceptionCode = "{" + exceptionCode;
		} else {
			exceptionCode = "{";
		}

		if (exceptionMessage != null) {
			exceptionMessage = exceptionMessage + "}";
		} else {
			exceptionMessage = "}";
		}

		String exception = exceptionCode + ":" + exceptionMessage;

		if (exception.length() > 2) {
			stringBuffer.append(exception);
		}

		stringBuffer.append("[" + (message == null ? defaultMessage : message)
				+ "]");

		return stringBuffer.toString();
	}

}
