package cn.zhuhongqing.io;

import java.io.File;

/**
 * Get all files :)
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public class TrueFileFilter extends AbstractFileFilter {

	public static final TrueFileFilter INSTANCE = new TrueFileFilter();

	@Override
	public boolean accept(File pathname) {
		return true;
	}

	@Override
	public boolean accept(File file, String name) {
		return true;
	}

}
