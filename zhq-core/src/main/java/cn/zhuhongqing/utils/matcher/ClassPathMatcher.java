package cn.zhuhongqing.utils.matcher;

import cn.zhuhongqing.anno.NotThreadSafe;
import cn.zhuhongqing.utils.StringPool;

@NotThreadSafe
public class ClassPathMatcher extends AbstractMatcher {

	@Override
	String getPathSeparator() {
		return StringPool.DOT;
	}

}
