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

	public static UtilsException of(String msg, Object... args) {
		return new UtilsException(String.format(msg, args));
	}

	public static UtilsException wrap(Throwable cause) {
		return new UtilsException(cause);
	}

	public static UtilsException wrap(Throwable cause, String message) {
		return new UtilsException(message, cause);
	}

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
