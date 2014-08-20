package cn.zhuhongqing.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.zhuhongqing.exception.UtilsException;

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

	static List<PropertyDescriptor> OBJECT_PROPERTY = new ArrayList<PropertyDescriptor>(
			0);

	static {
		OBJECT_PROPERTY = Arrays.asList(getPropertyDescriptors(Object.class));
	}

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
		PropertyDescriptor[] props = getPropertyDescriptors(origin.getClass());
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
		PropertyDescriptor[] props = getPropertyDescriptors(target.getClass());
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
		if (OBJECT_PROPERTY.contains(descriptor))
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
		return getProperty(target, findPropertyDescriptor(target, name));
	}

	@SuppressWarnings("unchecked")
	public static void setProperty(Object target, String name, Object value) {
		if (Map.class.isAssignableFrom(target.getClass())) {
			((Map<String, Object>) target).put(name, value);
			return;
		}
		setProperty(target, findPropertyDescriptor(target, name), value);
	}

	static BeanInfo getBeanInfo(Class<?> beanClass) {
		try {
			return Introspector.getBeanInfo(beanClass);
		} catch (IntrospectionException e) {
			throw new UtilsException(e);
		}
	}

	static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
		BeanInfo beanInfo = getBeanInfo(beanClass);
		return beanInfo.getPropertyDescriptors();
	}

	static PropertyDescriptor findPropertyDescriptor(Object target, String name) {
		PropertyDescriptor[] props = getPropertyDescriptors(target.getClass());
		for (PropertyDescriptor prop : props) {
			if (OBJECT_PROPERTY.contains(prop))
				continue;
			if (prop.getName().equals(name))
				return prop;
		}
		throw new UtilsException("Can not find [" + name + "] property from"
				+ target.getClass());
	}
}
