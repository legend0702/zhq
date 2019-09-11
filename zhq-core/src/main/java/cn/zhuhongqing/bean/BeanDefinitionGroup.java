package cn.zhuhongqing.bean;

import cn.zhuhongqing.util.meta.MetaData;
import cn.zhuhongqing.util.struct.AttrHole;
import cn.zhuhongqing.util.struct.MapAttrHole;

public interface BeanDefinitionGroup extends AttrHole<String, BeanDefinition>, MetaData {

	public static BeanDefinitionGroup build(Class<?> clazz) {
		return new DefaultBeanDefinitionGroup(clazz);
	}

	public static class DefaultBeanDefinitionGroup extends MapAttrHole<String, BeanDefinition>
			implements BeanDefinitionGroup {

		private Class<?> clazz;

		DefaultBeanDefinitionGroup(Class<?> clazz) {
			this.clazz = clazz;
		}

		@Override
		public Class<?> getDeclaringClass() {
			return clazz;
		}

		@Override
		public Class<?> getMetaType() {
			return clazz;
		}

		@Override
		public String toString() {
			return "DefaultBeanDefinitionGroup [clazz=" + clazz + ", hole=" + super.toString() + "]";
		}

	}

}