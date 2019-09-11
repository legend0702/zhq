package cn.zhuhongqing.call;

/**
 * Call Back.
 * 
 * More CallBack interface see sub-interface.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public interface CallBack<P> {

	void invoke(P param);

}