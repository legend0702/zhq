package cn.zhuhongqing.exception;

/**
 * ZHQ global runtimeException.
 * 
 * All ZHQ-module'runtimeException will extends this.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */
public class ZHQRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ZHQRuntimeException() {
		super();
	}

	public ZHQRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZHQRuntimeException(String message) {
		super(message);
	}

	public ZHQRuntimeException(Throwable cause) {
		super(cause);
	}

}
