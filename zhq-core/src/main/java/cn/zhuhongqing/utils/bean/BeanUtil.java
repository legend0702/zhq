package cn.zhuhongqing.utils.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.MethodUtil;
import cn.zhuhongqing.utils.ReflectUtil;

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
		if (ClassUtil.isMap(origin.getClass())) {
			mapToBean((Map<String, Object>) origin, target);
			return;
		}
		PropertyDescriptor[] props = BeanInfoUtil.getPropertyDescriptors(origin
				.getClass());
		for (int i = 0; i < props.length; i++) {
			setProperty(origin, target, props[i]);
		}
	}

	public static void copy(Object origin, Object target, String name) {
		Object value = getProperty(origin, name);
		setProperty(target, name, value);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> beanToMap(Object bean) {
		if (ClassUtil.isMap(bean.getClass()))
			return (Map<String, Object>) bean;
		PropertyDescriptor[] propds = BeanInfoUtil.getPropertyDescriptors(bean
				.getClass());
		LinkedHashMap<String, Object> beanMap = new LinkedHashMap<String, Object>(
				propds.length);
		for (PropertyDescriptor pd : propds) {
			Object val = getProperty(bean, pd);
			if (GeneralUtil.isNull(val))
				continue;
			beanMap.put(pd.getName(), val);
		}
		return beanMap;
	}

	public static void mapToBean(Map<String, Object> beanMap, Object target) {
		Iterator<Entry<String, Object>> originItr = beanMap.entrySet()
				.iterator();
		while (originItr.hasNext()) {
			Entry<String, Object> entry = originItr.next();
			setProperty(target, entry.getKey(), entry.getValue());
		}
	}

	public static <T extends Collection<String>> Map<String, Object> populateRequestMap(
			Map<String, T> requestMap) {
		Map<String, Object> parmsMap = new HashMap<String, Object>(
				requestMap.size());
		Iterator<Entry<String, T>> mapItr = requestMap.entrySet().iterator();
		while (mapItr.hasNext()) {
			Entry<String, T> entry = mapItr.next();
			String name = entry.getKey();
			T value = entry.getValue();
			if (value.isEmpty()) {
				continue;
			}
			if (value.size() == 1) {
				parmsMap.put(name, value.iterator().next());
				continue;
			}
			parmsMap.put(name, value);
		}
		return parmsMap;
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
		if (BeanInfoUtil.OBJECT_PROPERTY.contains(descriptor))
			return null;
		Method getMethod = descriptor.getReadMethod();
		if (GeneralUtil.isNull(getMethod))
			return null;
		return MethodUtil.invoke(getMethod, target);
	}

	public static Object getProperty(Object target, String name) {
		if (ClassUtil.isMap(target.getClass()))
			return ((Map<?, ?>) target).get(name);
		return getProperty(target,
				BeanInfoUtil.findPropertyDescriptor(target.getClass(), name));
	}

	@SuppressWarnings("unchecked")
	public static void setProperty(Object target, String name, Object value) {
		if (ClassUtil.isMap(target.getClass())) {
			((Map<String, Object>) target).put(name, value);
			return;
		}
		setProperty(target,
				BeanInfoUtil.findPropertyDescriptor(target.getClass(), name),
				value);
	}

}
