package cn.zhuhongqing.exception;

/**
 * ZHQ global exception.
 * 
 * All ZHQ-module'exception will extends this.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class ZHQException extends Exception {

	private static final long serialVersionUID = 1L;

	public ZHQException() {
		super();
	}

	public ZHQException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZHQException(String message) {
		super(message);
	}

	public ZHQException(Throwable cause) {
		super(cause);
	}

}
