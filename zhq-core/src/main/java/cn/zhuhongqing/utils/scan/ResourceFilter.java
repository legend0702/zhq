package cn.zhuhongqing.utils.scan;

import java.io.FileFilter;

/**
 * Like {@link FileFilter}<br/>
 * Return true to accept resource or false to discard it.<br/>
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public interface ResourceFilter<R> {

	/**
	 * Return true to accept resource or false to discard it.
	 */

	public boolean accept(R resource);

}
