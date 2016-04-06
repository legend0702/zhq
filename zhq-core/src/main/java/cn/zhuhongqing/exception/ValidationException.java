package cn.zhuhongqing.exception;

public class ValidationException extends UncheckedException {

	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(message);
	}

}
