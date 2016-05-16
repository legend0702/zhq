package cn.zhuhongqing.utils.scan;

import java.io.File;

import cn.zhuhongqing.io.FileUtil;
import cn.zhuhongqing.utils.ArraysUtil;

/**
 * 文件路径与名称过滤器<br/>
 * 
 * @see FileUtil#isInOrExcludeFile(File, String[], String[])
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class FileInAndExcludeResourceFilter implements ResourceFilter<File> {

	private String[] includePattern;
	private String[] excludePattern;

	public FileInAndExcludeResourceFilter(String includePattern,
			String excludePattern) {
		this(new String[] { includePattern }, new String[] { excludePattern });
	}

	public FileInAndExcludeResourceFilter(String[] includePattern,
			String[] excludePattern) {
		this.includePattern = ArraysUtil.isEmpty(includePattern) ? ArraysUtil
				.createEmptyArray(String.class) : includePattern;
		this.excludePattern = ArraysUtil.isEmpty(excludePattern) ? ArraysUtil
				.createEmptyArray(String.class) : excludePattern;
	}

	@Override
	public boolean accept(File res) {
		return FileUtil.isInOrExcludeFile(res, includePattern, excludePattern);
	}

}
