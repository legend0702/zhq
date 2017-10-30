package cn.zhuhongqing.bean;

import cn.zhuhongqing.utils.StringPool;

/**
 * 标记组别以及生命范围
 * 
 * 规则如下：
 * 
 * <pre>
 * 一.父类默认/非默认 子类默认
 * 子类默认则跟随父类设定
 * 如果父类也是默认 那么结果就是 
 * {@link #value()} = {@link StringPool#DEFAULT}
 * {@link #scope()} = {@link ObjectScope#DEFAULT}
 * 
 * 二.父类默认/非默认 子类非默认
 * 子类设定优先与父类设定
 * 因此根据子类设定执行后续的逻辑
 * </pre>
 *
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
