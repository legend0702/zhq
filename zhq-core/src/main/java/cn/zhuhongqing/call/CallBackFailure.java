package cn.zhuhongqing.call;

public interface CallBackFailure<P> extends CallBackThr<P> {

	/**
	 * Don't implement this method!!!
	 */

	@Override
	default void invoke(P param) throws RuntimeException {
		try {
			invokeThr(param);
		} catch (Exception e) {
			failure(e);
		}
	}

	void failure(Exception e);

}