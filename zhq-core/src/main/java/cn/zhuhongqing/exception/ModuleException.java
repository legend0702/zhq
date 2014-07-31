package cn.zhuhongqing.exception;

public class ModuleException extends ZHQRuntimeException {

	private static final long serialVersionUID = 1L;

	public ModuleException() {
	}

	public ModuleException(String message) {
		super(message);
	}

	public ModuleException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModuleException(Throwable cause) {
		super(cause);
	}

}
