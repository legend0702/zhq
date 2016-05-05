package cn.zhuhongqing.utils.scan;

/**
 * True filter
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */
public class TrueResourceFilter<R> implements ResourceFilter<R> {

	public static final ResourceFilter<?> INSTANCE = new TrueResourceFilter<>();

	@Override
	public boolean accept(R resource) {
		return true;
	}

}