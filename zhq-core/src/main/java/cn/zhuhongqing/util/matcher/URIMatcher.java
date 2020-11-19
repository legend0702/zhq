package cn.zhuhongqing.util.matcher;

import cn.zhuhongqing.util.StringPool;

/**
 * Match path like URI.
 * 
 * @author HongQing.Zhu
 * 
 */

public class URIMatcher extends AbstractMatcher {

	public static URIMatcher INSTANCE = new URIMatcher();

	@Override
	String getPathSeparator() {
		return StringPool.SLASH;
	}

}
