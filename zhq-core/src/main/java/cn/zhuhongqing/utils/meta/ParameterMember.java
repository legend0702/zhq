package cn.zhuhongqing.utils.meta;

import java.lang.reflect.Member;
import java.lang.reflect.Parameter;

class ParameterMember implements Member {

	protected Parameter param;

	ParameterMember(Parameter param) {
		this.param = param;
	}

	@Override
	public Class<?> getDeclaringClass() {
		return param.getDeclaringExecutable().getDeclaringClass();
	}

	@Override
	public String getName() {
		return param.getName();
	}

	@Override
	public int getModifiers() {
		return param.getModifiers();
	}

	@Override
	public boolean isSynthetic() {
		return param.isSynthetic();
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
		ParameterMember other = (ParameterMember) obj;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ParameterMember [param=" + param + "]";
	}

}
