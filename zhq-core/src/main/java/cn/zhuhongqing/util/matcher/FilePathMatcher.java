package cn.zhuhongqing.util.matcher;

import cn.zhuhongqing.util.StringPool;

public class FilePathMatcher extends AbstractMatcher {

	public static FilePathMatcher INSTANCE = new FilePathMatcher();
	
	@Override
	String getPathSeparator() {
		return StringPool.FILE_SEPARATOR;
	}

}
