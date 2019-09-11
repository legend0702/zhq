package cn.zhuhongqing.util.matcher;

/**
 * Match path by pattern.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public interface PathMatcher {

	/**
	 * 单字符匹配符
	 */
	public static final String SINGLE_WORD_PATTERN = "?";

	/**
	 * 单行全匹配符
	 */
	public static final String ALL_WORD_PATTERN = "*";

	/**
	 * 全匹配符,匹配之后所有没有匹配规则的数据
	 */
	public static final String ALL_PATTERN = "**";

	public boolean match(String pattern, String path);

	/**
	 * 只匹配传入的path部分
	 */
	public boolean matchStart(String pattern, String path);

	public boolean isPattern(String path);

	public boolean hasPattern(String path);
}
