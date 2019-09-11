package cn.zhuhongqing.call;

public interface CallValue<P, V> extends CallBack<P>{

	/**
	 * Don't implement this method!!!
	 */
	@Override
	default void invoke(P param) {
		invokeValue(param);
	}

	/**
	 * Implement this method instead :)
	 */

	V invokeValue(P param);

}