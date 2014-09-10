package cn.zhuhongqing.call;

/**
 * Call Back.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public interface CallBack<P> {

	void invoke(P param);

}