package cn.zhuhongqing.bean;

import cn.zhuhongqing.util.meta.MetaData;
import cn.zhuhongqing.util.struct.AttrHole;

public interface BeanDefinition extends AttrHole<String, BeanProperty>, ObjectState, MetaData {

	public static BeanDefinition of(Class<?> clazz) {
		return new DefaultBeanDefinition(clazz);
	}
}
