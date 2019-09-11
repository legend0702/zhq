package cn.zhuhongqing.util.scan;

import java.util.Set;

/**
 * Scan and get resources.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public interface ResourceScan<R> {

	/**
	 * Get resource by path.
	 * 
	 * @param path
	 *            A path without pattern.
	 */

	R getResource(String path);

	/**
	 * Get resource by path with filter.
	 * 
	 * @param path
	 *            A path without pattern.
	 */

	R getResource(String path, ResourceFilter<R> filter);

	/**
	 * Use pattern to find and get resource.
	 * 
	 * @param path
	 *            A path-pattern.
	 */

	Set<R> getResources(String pathPattern);

	/**
	 * Use pattern to find and get resource with filter
	 * 
	 * @param path
	 *            A path-pattern.
	 */

	Set<R> getResources(String pathPattern, ResourceFilter<R> filter);

}
