package cn.zhuhongqing.utils.meta;

import java.util.concurrent.ConcurrentHashMap;

import cn.zhuhongqing.utils.GeneralUtil;

public class ClassMeta implements MetaData {

	private static final ConcurrentHashMap<Class<?>, ClassMeta> CLASS_META_HOLE = new ConcurrentHashMap<>(20);
	private Class<?> clazz;

	protected ClassMeta(Class<?> clazz) {
		this.clazz = clazz;
	}

	static MetaData of(Class<?> clazz) {
		ClassMeta meta = CLASS_META_HOLE.get(clazz);
		if (GeneralUtil.isNull(meta)) {
			meta = buildAndPut(clazz);
		}
		return meta;
	}

	private static ClassMeta buildAndPut(Class<?> clazz) {
		ClassMeta m = new ClassMeta(clazz);
		ClassMeta om = CLASS_META_HOLE.putIfAbsent(clazz, m);
		return GeneralUtil.defValue(om, m);
	}

	@Override
	public Class<?> getMetaType() {
		return clazz;
	}

	@Override
	public Class<?> getDeclaringClass() {
		return clazz;
	}

	@Override
	public String getName() {
		return clazz.getSimpleName();
	}

	@Override
	public int getModifiers() {
		return clazz.getModifiers();
	}

	@Override
	public boolean isSynthetic() {
		return clazz.isSynthetic();
	}

	@Override
	public String toString() {
		return "ClassMeta [clazz=" + clazz + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
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
		ClassMeta other = (ClassMeta) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		return true;
	}

}
