package cn.zhuhongqing.utils.struct;

import java.io.Serializable;

import cn.zhuhongqing.anno.NotNull;
import cn.zhuhongqing.utils.ObjectUtil;
import cn.zhuhongqing.utils.StringUtil;

/**
 * Name / Value Pair~
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class NameValuePair implements Cloneable, Serializable {

	private static final long serialVersionUID = -9008766593584677923L;

	protected final String name;
	protected final String value;

	/**
	 * Default Constructor taking a name and a value. The value may be null.
	 *
	 * @param name
	 *            The name.
	 * @param value
	 *            The value.
	 */
	public NameValuePair(@NotNull final String name, final String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return StringUtil.toJson(name, value);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof NameValuePair) {
			final NameValuePair that = (NameValuePair) object;
			return this.name.equals(that.name) && ObjectUtil.nullSafeEquals(this.value, that.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new NameValuePair(name, value);
	}

}