package cn.zhuhongqing.bean;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Parameter;

import cn.zhuhongqing.util.meta.MetaData;

public interface BeanProperty extends MetaData, ObjectState {

	public static <M extends AnnotatedElement & Member> BeanProperty of(M element) {
		return new DefaultBeanProperty(element);
	}

	public static BeanProperty of(Field field) {
		return new DefaultBeanProperty(field);
	}

	public static BeanProperty of(Parameter param) {
		return new DefaultBeanProperty(MetaData.of(param));
	}

}