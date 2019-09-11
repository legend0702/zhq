package cn.zhuhongqing.call;

import cn.zhuhongqing.exception.UtilsException;

public interface CallBackThr<P> extends CallBack<P> {

	/**
	 * Don't implement this method!!!
	 */

	@Override
	default void invoke(P param) throws RuntimeException {
		try {
			invokeThr(param);
		} catch (Exception e) {
			throw UtilsException.wrap(e);
		}
	}

	/**
	 * Implement this method instead :)
	 */

	void invokeThr(P param) throws Exception;

}
