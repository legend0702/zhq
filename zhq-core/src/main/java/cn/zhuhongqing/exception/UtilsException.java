package cn.zhuhongqing.exception;

/**
 * 
 * Wrap up {@link RuntimeException}
 * 
 * @author HongQing.Zhu
 * 
 */

public class UtilsException extends ZHQRuntimeException {

	private static final long serialVersionUID = 1L;

	public UtilsException() {
		super();
	}

	public UtilsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilsException(String message) {
		super(message);
	}

	public UtilsException(Throwable cause) {
		super(cause);
	}

}
