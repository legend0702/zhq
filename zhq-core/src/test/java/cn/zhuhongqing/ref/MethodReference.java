package cn.zhuhongqing.ref;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MethodReference extends ClassReference<Method> {

	public MethodReference(Class<?> clazz) {
		super(clazz);
	}

	@Override
	void init(Class<?> clazz) {
		Class<?> target = clazz;
		do {
			addAll(target.getDeclaredMethods());
			target = target.getSuperclass();
		} while (!target.equals(Object.class));
	}

	@Override
	Set<Integer> createMemberHashCode(Method member) {
		Set<Integer> hashSet = new HashSet<Integer>();
		String name = member.getName();
		// name
		hashSet.add(name.hashCode());
		// name+paramTypes
		hashSet.add(createMemberSoftReferenceHashCode(member));
		// name+annotation[0] ... name+annotation[n]
		for (Annotation annotation : member.getAnnotations())
			hashSet.add(createHashCode(name, annotation.annotationType()));
		// annotation[0]...annotation[n]
		for (Annotation annotation : member.getAnnotations())
			hashSet.add(createHashCode(annotation.annotationType()));
		return hashSet;
	}

	@Override
	int createMemberSoftReferenceHashCode(Method referent) {
		StringBuffer sb = new StringBuffer();
		sb.append(referent.getName());
		Class<?>[] params = referent.getParameterTypes();
		for (Class<?> param : params)
			sb.append(param);
		return sb.toString().hashCode();
	}
}
