package cn.zhuhongqing.utils.bean;

import cn.zhuhongqing.utils.meta.MetaData;
import cn.zhuhongqing.utils.struct.AttributeHole;

public interface BeanDefinition extends AttributeHole<String, BeanProperty>, ObjectState, MetaData {

	public static BeanDefinition of(Class<?> clazz) {
		return new DefaultBeanDefinition(clazz);
	}
}
