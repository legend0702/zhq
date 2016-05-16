package cn.zhuhongqing.utils.matcher;

import cn.zhuhongqing.utils.StringPool;

/**
 * Match path like URI.
 * 
 * @author HongQing.Zhu
 * 
 */

public class URIMatch extends AbstractMatcher {

	public static URIMatch INSTANCE = new URIMatch();

	@Override
	String getPathSeparator() {
		return StringPool.SLASH;
	}

}
