package cn.zhuhongqing.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Some utilities for JavaBean.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class BeanUtil {

	@SuppressWarnings("unchecked")
	public static <T> T copy(T bean) {
		T returnT = (T) ReflectUtil.newInstance(bean.getClass());
		copy(bean, returnT);
		return returnT;
	}

	@SuppressWarnings("unchecked")
	public static void copy(Object origin, Object target) {
		if (Map.class.isAssignableFrom(origin.getClass())) {
			MapToBean((Map<String, Object>) origin, target);
			return;
		}
		PropertyDescriptor[] props = BeanInfoUtil.getPropertyDescriptors(origin
				.getClass());
		for (int i = 0; i < props.length; i++) {
			PropertyDescriptor propertyDescriptor = props[i];
			setProperty(origin, target, propertyDescriptor);
		}
	}

	public static void copy(Object origin, Object target, String name) {
		Object value = getProperty(origin, name);
		setProperty(target, name, value);
	}

	@SuppressWarnings("unchecked")
	public static void extend(Object origin, Object target) {
		if (Map.class.isAssignableFrom(target.getClass())) {
			MapToBean((Map<String, Object>) target, origin);
			return;
		}
		PropertyDescriptor[] props = BeanInfoUtil.getPropertyDescriptors(target
				.getClass());
		for (int i = 0; i < props.length; i++) {
			PropertyDescriptor propertyDescriptor = props[i];
			Object value = getProperty(target, propertyDescriptor);
			if (GeneralUtil.isNull(value))
				continue;
			setProperty(origin, propertyDescriptor, value);
		}
	}

	static void MapToBean(Map<String, Object> beanMap, Object target) {
		Iterator<Entry<String, Object>> originItr = beanMap.entrySet()
				.iterator();
		while (originItr.hasNext()) {
			Entry<String, Object> entry = originItr.next();
			setProperty(target, entry.getKey(), entry.getValue());
		}
	}

	static void setProperty(Object origin, Object target,
			PropertyDescriptor descriptor) {
		Object value = getProperty(origin, descriptor);
		setProperty(target, descriptor, value);
	}

	static void setProperty(Object target, PropertyDescriptor descriptor,
			Object value) {
		if (BeanInfoUtil.OBJECT_PROPERTY.contains(descriptor))
			return;
		Method setMethod = descriptor.getWriteMethod();
		if (GeneralUtil.isNull(setMethod))
			return;
		MethodUtil.invoke(setMethod, target, value);
	}

	static Object getProperty(Object target, PropertyDescriptor descriptor) {
		Method getMethod = descriptor.getReadMethod();
		if (GeneralUtil.isNull(getMethod))
			return null;
		return MethodUtil.invoke(descriptor.getReadMethod(), target);
	}

	public static Object getProperty(Object target, String name) {
		if (Map.class.isAssignableFrom(target.getClass()))
			return ((Map<?, ?>) target).get(name);
		return getProperty(target,
				BeanInfoUtil.findPropertyDescriptor(target, name));
	}

	@SuppressWarnings("unchecked")
	public static void setProperty(Object target, String name, Object value) {
		if (Map.class.isAssignableFrom(target.getClass())) {
			((Map<String, Object>) target).put(name, value);
			return;
		}
		setProperty(target, BeanInfoUtil.findPropertyDescriptor(target, name),
				value);
	}

}
