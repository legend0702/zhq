package cn.zhuhongqing.utils.matcher;

import cn.zhuhongqing.utils.StringPool;

public class ClassPathMatcher extends AbstractMatcher {

	@Override
	String getPathSeparator() {
		return StringPool.DOT;
	}

}
