package cn.zhuhongqing.util.scan;

import java.io.File;

import cn.zhuhongqing.util.ArraysUtils;
import cn.zhuhongqing.util.file.FileUtils;

/**
 * 文件路径与名称过滤器<br/>
 * 
 * @see FileUtils#isInOrExcludeFile(File, String[], String[])
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

	private String[] includePattern = ArraysUtils.emptyArray(String.class);
	private String[] excludePattern = ArraysUtils.emptyArray(String.class);

	public FileInAndExcludeResourceFilter(String includePattern, String excludePattern) {
		this(new String[] { includePattern }, new String[] { excludePattern });
	}

	public FileInAndExcludeResourceFilter(String[] includePattern, String[] excludePattern) {
		this.includePattern = ArraysUtils.isEmpty(includePattern) ? this.includePattern : includePattern;
		this.excludePattern = ArraysUtils.isEmpty(excludePattern) ? this.excludePattern : excludePattern;
	}

	@Override
	public boolean accept(File res) {
		return FileUtils.isInOrExcludeFile(res, includePattern, excludePattern);
	}

}
