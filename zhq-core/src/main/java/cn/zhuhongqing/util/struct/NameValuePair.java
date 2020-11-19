package cn.zhuhongqing.util.struct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.zhuhongqing.anno.NotNull;
import cn.zhuhongqing.util.ObjectUtils;
import cn.zhuhongqing.util.StringUtils;

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
	 * @param name  The name.
	 * @param value The value.
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
		return StringUtils.toJson(name, value);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof NameValuePair) {
			final NameValuePair that = (NameValuePair) object;
			return this.name.equals(that.name) && ObjectUtils.nullSafeEquals(this.value, that.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public Object clone() {
		return new NameValuePair(name, value);
	}
	
	public static Builder builder(String name, String value) {
		return new Builder().add(name, value);
	}

	public static class Builder {
		private List<NameValuePair> pairs = new ArrayList<>();

		public Builder add(String name, String value) {
			pairs.add(new NameValuePair(name, value));
			return this;
		}

		public NameValuePair build() {
			if (pairs.isEmpty())
				return null;
			return pairs.get(0);
		}

		// Just Call once
		public List<NameValuePair> buildList() {
			try {
				return pairs;
			} finally {
				pairs = new ArrayList<>();
			}
		}

		// Same name will return first value
		public Set<NameValuePair> buildSet() {
			Set<NameValuePair> set = new LinkedHashSet<>(pairs.size() * 2);
			Set<String> names = new HashSet<>(pairs.size() * 2);
			for (NameValuePair pair : pairs) {
				if (names.contains(pair.name))
					continue;
				set.add(pair);
			}
			return set;
		}
	}
}