package cn.zhuhongqing.utils.scan;

import java.util.Set;

/**
 * Scan resource and load it.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public interface ResourceScan<E> {

	/**
	 * Load resource by path.
	 */

	E getResource(String path);

	/**
	 * Use pattern to find and load resource.
	 * 
	 */

	Set<E> getResources(String path);

}
