package cn.zhuhongqing.utils.matcher;

import cn.zhuhongqing.utils.StringPool;

/**
 * 匹配URL用的
 * 
 * @author HongQing.Zhu
 * 
 */

public class URLMatch extends AbstractMatcher {

	@Override
	String getPathSeparator() {
		return StringPool.SLASH;
	}

}
