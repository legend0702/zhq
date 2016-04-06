package cn.zhuhongqing.call;

/**
 * Call Back.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public interface CallBack<P> {

	default void invoke(P param) {
	}

	default void invokeThr(P param) throws Exception {
	}

}