package cn.zhuhongqing.util.matcher;

import cn.zhuhongqing.util.StringPool;

public class ClassPathMatcher extends AbstractMatcher {

	public static ClassPathMatcher INSTANCE = new ClassPathMatcher();
	
	@Override
	String getPathSeparator() {
		return StringPool.DOT;
	}

}
