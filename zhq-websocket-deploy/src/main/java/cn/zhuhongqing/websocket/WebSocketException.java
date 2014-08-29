package cn.zhuhongqing.websocket;

import cn.zhuhongqing.exception.ZHQRuntimeException;

public class WebSocketException extends ZHQRuntimeException {

	private static final long serialVersionUID = 1L;

	public WebSocketException(Throwable cause) {
		super(cause);
	}

}
