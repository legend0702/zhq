package cn.zhuhongqing.utils.matcher;

import cn.zhuhongqing.anno.NotThreadSafe;
import cn.zhuhongqing.utils.StringPool;

/**
 * 匹配URL用的
 * 
 * @author HongQing.Zhu
 * 
 */

@NotThreadSafe
public class URLMatch extends AbstractMatcher {

	@Override
	String getPathSeparator() {
		return StringPool.SLASH;
	}

}
