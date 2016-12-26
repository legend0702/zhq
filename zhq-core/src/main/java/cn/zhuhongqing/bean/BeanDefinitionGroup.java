package cn.zhuhongqing.bean;

import cn.zhuhongqing.utils.meta.MetaData;
import cn.zhuhongqing.utils.struct.AttributeHole;
import cn.zhuhongqing.utils.struct.MapAttrbuteHole;

public interface BeanDefinitionGroup extends AttributeHole<String, BeanDefinition>, MetaData {

	public static BeanDefinitionGroup build(Class<?> clazz) {
		return new DefaultBeanDefinitionGroup(clazz);
	}

	public static class DefaultBeanDefinitionGroup extends MapAttrbuteHole<String, BeanDefinition>
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