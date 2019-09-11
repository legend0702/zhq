package cn.zhuhongqing.util.matcher;

import cn.zhuhongqing.util.StringPool;

public class ClassPathMatcher extends AbstractMatcher {

	@Override
	String getPathSeparator() {
		return StringPool.DOT;
	}

}
