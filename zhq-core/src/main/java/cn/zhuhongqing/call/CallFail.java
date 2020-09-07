package cn.zhuhongqing.call;

import java.util.function.Function;

public interface CallFail<V, R> extends Function<V, R>{

	/**
	 * Don't implement this method!!!
	 */
	default R applyCatch(V v) {
		try {
			return apply(v);
		}catch (Exception e) {
			return fail(e, v);
		}
	}

	/**
	 * Implement this method instead :)
	 */

	R fail(Exception e, V v);

}