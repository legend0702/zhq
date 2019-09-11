package cn.zhuhongqing.util.scan;

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

	@SuppressWarnings("unchecked")
	public static <T> ResourceFilter<T> instance() {
		return (ResourceFilter<T>) INSTANCE;
	}

	@Override
	public boolean accept(R resource) {
		return true;
	}

}