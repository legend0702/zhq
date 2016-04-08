package cn.zhuhongqing.utils.matcher;

import cn.zhuhongqing.anno.NotThreadSafe;
import cn.zhuhongqing.utils.StringPool;

@NotThreadSafe
public class FilePathMatcher extends AbstractMatcher {

	@Override
	String getPathSeparator() {
		return StringPool.BACK_SLASH;
	}

}
