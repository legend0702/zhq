package cn.zhuhongqing.script;

/**
 * Script Exception.
 * 
 * @author HongQing.Zhu
 * 
 */

public class ScriptException extends Exception {

	private static final long serialVersionUID = 1L;

	public ScriptException() {
		super();
	}

	public ScriptException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ScriptException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScriptException(String message) {
		super(message);
	}

	public ScriptException(Throwable cause) {
		super(cause);
	}

}
