package cn.zhuhongqing.util.matcher;

import cn.zhuhongqing.util.StringPool;

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
