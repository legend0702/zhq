package cn.zhuhongqing.bean;

import cn.zhuhongqing.util.StringPool;

/**
 * 标记组别以及生命范围
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public interface ObjectState {

	static ObjectState of(String group, ObjectScope scope) {
		return new DefaultObjectState(group, scope);
	}

	/**
	 * Set the object's group
	 */

	void setGroup(String group);

	/**
	 * Return the object's Group.
	 * 
	 * Default is {@link StringPool#DEFAULT}
	 */
	String getGroup();

	/**
	 * Return the name of the current target scope for this object.
	 */
	ObjectScope getScope();

	/**
	 * Override the target scope of this object, specifying a new scope name.
	 * 
	 * @see ObjectScope
	 */
	void setScope(ObjectScope scope);

	// ObjectScope

	default boolean isSingleton() {
		return ObjectScope.SINGLETON == getScope();
	}

	default boolean isPrototype() {
		return ObjectScope.PROTOTYPE == getScope();
	}

	default boolean isDefaultScope() {
		return ObjectScope.DEFAULT == getScope();
	}

	default boolean isDefaultGroup() {
		return StringPool.DEFAULT.equals(getGroup());
	}

}
