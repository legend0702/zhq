package cn.zhuhongqing.generator.db;

import cn.zhuhongqing.generator.GeneratorException;

public class GeneratorDBException extends GeneratorException {

	private static final long serialVersionUID = 1L;

	public GeneratorDBException(String message) {
		super(message);
	}

	public GeneratorDBException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneratorDBException(Throwable cause) {
		super(cause);
	}

}
