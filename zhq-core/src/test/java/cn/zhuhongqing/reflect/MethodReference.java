package cn.zhuhongqing.reflect;

import java.lang.reflect.Method;

/**
 * A Class'method reference.
 * 
 * Save all declaredMethods, from ownClass to Object.class.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 * @param <C>
 * 
 */

public class MethodReference extends ClassReference<Method> {

	public MethodReference(Class<?> clazz) {
		super(clazz);
	}

	/**
	 * add all declaredMethods, from ownClass to Object.class.
	 * 
	 * @return
	 */

	void init(Class<?> clazz) {
		Class<?> target = clazz;
		do {
			addAll(target.getDeclaredMethods());
			target = target.getSuperclass();
		} while (!target.equals(Object.class));
	}

	@Override
	int createMemberSoftReferenceHashCode(Method referent) {
		StringBuffer sb = new StringBuffer();
		sb.append(referent.getName());
		Class<?>[] arguments = referent.getParameterTypes();
		for (Class<?> arg : arguments)
			sb.append("@").append(arg);
		return sb.toString().intern().hashCode();
	}

	@Override
	boolean matchMember(Method member, Class<?>[] relations) {
		if (member.getParameterTypes().length != relations.length)
			return false;
		Class<?>[] parameterTypes = member.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			if (!parameterTypes[i].isAssignableFrom(relations[i]))
				return false;
		}
		return true;
	}

}
