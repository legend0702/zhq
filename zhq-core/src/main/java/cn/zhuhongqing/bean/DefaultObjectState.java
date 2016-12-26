package cn.zhuhongqing.bean;

public class DefaultObjectState implements ObjectState {

	private String group;
	private ObjectScope scope;

	public DefaultObjectState() {
		this(null, null);
	}

	public DefaultObjectState(String group, ObjectScope scope) {
		this.group = group;
		this.scope = scope;
	}

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

}
