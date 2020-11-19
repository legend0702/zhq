package cn.zhuhongqing.util.condition;

import cn.zhuhongqing.util.StringUtils;
import cn.zhuhongqing.util.matcher.PathMatcher;

abstract class AbstractPathMatchCondition<V> extends AbstractMatchCondition<String, V> implements PathMatchCondition<V> {

	abstract PathMatcher mather();

	@Override
	boolean match(String pattern, String path) {
		return mather().match(pattern, path);
	}

	abstract String getPathSeparator();

	@Override
	Integer level(String key) {
		if (!key.contains(PathMatcher.ALL_PATTERN))
			return StringUtils.split(key, getPathSeparator()).size();
		return StringUtils.countMatches(key, getPathSeparator());
	}

}
