package cn.zhuhongqing.util.loader;

import java.io.File;
import java.io.InputStream;

/**
 * Loader resource.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public interface ResourceLoader {

	public File loadAsFile(String path);

	public InputStream loaderAsStream(String path);

}
