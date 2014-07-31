package cn.zhuhongqing.reflect;

import java.lang.reflect.Field;

public class FieldReference extends ClassReference<Field> {

	public FieldReference(Class<?> clazz) {
		super(clazz);
	}

	@Override
	void init(Class<?> clazz) {
		Class<?> target = clazz;
		do {
			addAll(target.getDeclaredFields());
			target = target.getSuperclass();
		} while (!target.equals(Object.class));
	}

	@Override
	boolean matchMember(Field member, Class<?>[] relations) {
		return true;
	}

	@Override
	int createMemberSoftReferenceHashCode(Field referent) {
		return referent.getName().intern().hashCode();
	}

}
