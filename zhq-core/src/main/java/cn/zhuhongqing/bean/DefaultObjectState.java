package cn.zhuhongqing.bean;

import cn.zhuhongqing.utils.StringPool;

public class DefaultObjectState implements ObjectState {

	private String group;
	private ObjectScope scope;

	public DefaultObjectState() {
		this(StringPool.DEFAULT, ObjectScope.DEFAULT);
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
	public void setScope(ObjectScope scope) {
		this.scope = scope;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public ObjectScope getScope() {
		return scope;
	}

}
