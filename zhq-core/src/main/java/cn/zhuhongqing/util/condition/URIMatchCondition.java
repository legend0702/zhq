package cn.zhuhongqing.util.condition;

import cn.zhuhongqing.util.StringPool;
import cn.zhuhongqing.util.matcher.PathMatcher;
import cn.zhuhongqing.util.matcher.URIMatcher;

public class URIMatchCondition<V> extends AbstractPathMatchCondition<V> {

	@Override
	PathMatcher mather() {
		return URIMatcher.INSTANCE;
	}

	@Override
	String getPathSeparator() {
		return StringPool.SLASH;
	}

}
