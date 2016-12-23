package cn.zhuhongqing.utils.bean;

import java.util.HashMap;
import java.util.Map;

import cn.zhuhongqing.utils.struct.MapAttrbuteHole;

public abstract class ObjectStateWithAttrbuteHole<K, V> extends MapAttrbuteHole<K, V> implements ObjectState {

	private String group;
	private ObjectScope scope;

	public ObjectStateWithAttrbuteHole() {
		this(new HashMap<>());
	}

	public ObjectStateWithAttrbuteHole(Map<K, V> instance) {
		super(instance);
	}

	abstract Object getTarget();

	@Override
	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public ObjectScope getScope() {
		return scope;
	}

	@Override
	public void setScope(ObjectScope scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [Object =" + getTarget() + " ,group=" + group + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
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
		ObjectStateWithAttrbuteHole<?, ?> other = (ObjectStateWithAttrbuteHole<?, ?>) obj;
		if (group == null) {
			if (other.getGroup() != null)
				return false;
		} else if (!group.equals(other.getGroup()))
			return false;
		if (getTarget() == null) {
			if (other.getTarget() != null)
				return false;
		} else if (!getTarget().equals(other.getTarget()))
			return false;
		return true;
	}

}
