package cn.zhuhongqing.utils.meta;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Parameter;

public class ParameterMeta implements MetaData {

	private Parameter param;
	private Member paramMember;

	protected ParameterMeta(Parameter param) {
		this.param = param;
		this.paramMember = new ParameterMember(param);
	}

	static MetaData of(Parameter param) {
		return new ParameterMeta(param);
	}

	@Override
	public Class<?> getMetaType() {
		return param.getType();
	}

	@Override
	public AnnotatedElement getAnnotatedElement() {
		return param;
	}

	@Override
	public Member getMember() {
		return paramMember;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((param == null) ? 0 : param.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterMeta other = (ParameterMeta) obj;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ParameterMeta [param=" + param + "]";
	}

}
