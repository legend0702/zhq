package cn.zhuhongqing.template.exception;

import cn.zhuhongqing.exception.ZHQRuntimeException;

public class TemplateException extends ZHQRuntimeException {
	private static final long serialVersionUID = 1L;

	public TemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(Throwable cause) {
		super(cause);
	}

}
