package cn.zhuhongqing.util.scan;

import cn.zhuhongqing.util.meta.MetaData;

public class NormalPublicClassFilter implements ResourceFilter<Class<?>> {

	public static final NormalPublicClassFilter INSTANCE = new NormalPublicClassFilter();

	@Override
	public boolean accept(Class<?> clazz) {
		MetaData meta = MetaData.of(clazz);
		if (clazz.isPrimitive() || clazz.isArray() || clazz.isEnum() || clazz.isAnnotation() || clazz.isAnonymousClass()
				|| clazz.isSynthetic() || meta.isNative() || meta.isPrivate() || meta.isProtected()
				|| clazz.isLocalClass() || clazz.isMemberClass()) {
			return false;
		}
		return true;
	}

}
