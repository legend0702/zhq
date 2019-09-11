package cn.zhuhongqing.util.matcher;

import cn.zhuhongqing.util.StringPool;

public class FilePathMatcher extends AbstractMatcher {

	@Override
	String getPathSeparator() {
		return StringPool.FILE_SEPARATOR;
	}

}
