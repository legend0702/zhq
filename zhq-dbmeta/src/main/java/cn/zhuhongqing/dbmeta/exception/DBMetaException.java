package cn.zhuhongqing.dbmeta.exception;

import cn.zhuhongqing.exception.ZHQRuntimeException;

public class DBMetaException extends ZHQRuntimeException {

	private static final long serialVersionUID = 1L;

	public DBMetaException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBMetaException(String message) {
		super(message);
	}

	public DBMetaException(Throwable cause) {
		super(cause);
	}

}
