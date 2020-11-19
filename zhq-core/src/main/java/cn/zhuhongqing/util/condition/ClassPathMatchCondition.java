package cn.zhuhongqing.util.condition;

import cn.zhuhongqing.util.StringPool;
import cn.zhuhongqing.util.matcher.ClassPathMatcher;
import cn.zhuhongqing.util.matcher.PathMatcher;

public class ClassPathMatchCondition<V> extends AbstractPathMatchCondition<V> {

	@Override
	PathMatcher mather() {
		return ClassPathMatcher.INSTANCE;
	}

	@Override
	String getPathSeparator() {
		return StringPool.DOT;
	}

}
