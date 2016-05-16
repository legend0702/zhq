package cn.zhuhongqing.generator;

import cn.zhuhongqing.exception.ZHQRuntimeException;

public class GeneratorException extends ZHQRuntimeException {

	private static final long serialVersionUID = 1L;

	public GeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneratorException(String message) {
		super(message);
	}

	public GeneratorException(Throwable cause) {
		super(cause);
	}

}
