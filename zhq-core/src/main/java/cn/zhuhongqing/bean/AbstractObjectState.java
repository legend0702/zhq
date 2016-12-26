package cn.zhuhongqing.bean;

public abstract class AbstractObjectState extends DefaultObjectState implements ObjectState {

	abstract Object getTarget();

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [Object =" + getTarget() + " ,group=" + getGroup() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getGroup() == null) ? 0 : getGroup().hashCode());
		result = prime * result + ((getTarget() == null) ? 0 : getTarget().hashCode());
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
		AbstractObjectState other = (AbstractObjectState) obj;
		if (getGroup() == null) {
			if (other.getGroup() != null)
				return false;
		} else if (!getGroup().equals(other.getGroup()))
			return false;
		if (getTarget() == null) {
			if (other.getTarget() != null)
				return false;
		} else if (!getTarget().equals(other.getTarget()))
			return false;
		return true;
	}

}
