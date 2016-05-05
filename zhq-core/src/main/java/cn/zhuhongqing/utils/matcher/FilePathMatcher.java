package cn.zhuhongqing.utils.matcher;

import cn.zhuhongqing.utils.StringPool;

public class FilePathMatcher extends AbstractMatcher {

	@Override
	String getPathSeparator() {
		return StringPool.FILE_SEPARATOR;
	}

}
