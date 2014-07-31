package cn.zhuhongqing.exception;

/**
 * 
 * Wrap up {@link RuntimeException}
 * 
 * @author HongQing.Zhu
 * 
 */

public class UncheckedException extends ZHQRuntimeException {

	private static final long serialVersionUID = 1L;

	public UncheckedException() {
		super();
	}

	public UncheckedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UncheckedException(String message) {
		super(message);
	}

	public UncheckedException(Throwable cause) {
		super(cause);
	}

}
