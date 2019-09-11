package cn.zhuhongqing.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.GeneralUtils;
import cn.zhuhongqing.util.MethodUtils;
import cn.zhuhongqing.util.ReflectUtils;
import cn.zhuhongqing.util.StringPool;

/**
 * Some utilities for JavaBean.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class BeanUtils {

	@SuppressWarnings("unchecked")
	public static <T> T copy(T bean) {
		T returnT = (T) ReflectUtils.newInstance(bean.getClass());
		copy(bean, returnT);
		return returnT;
	}

	@SuppressWarnings("unchecked")
	public static void copy(Object origin, Object target) {
		if (ClassUtils.isMap(origin.getClass())) {
			mapToBean((Map<String, Object>) origin, target);
			return;
		}
		PropertyDescriptor[] props = BeanInfoUtils.getPropertyDescriptors(origin.getClass());
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
		if (ClassUtils.isMap(bean.getClass()))
			return (Map<String, Object>) bean;
		PropertyDescriptor[] propds = BeanInfoUtils.getPropertyDescriptors(bean.getClass());
		LinkedHashMap<String, Object> beanMap = new LinkedHashMap<String, Object>(propds.length);
		for (PropertyDescriptor pd : propds) {
			Object val = getProperty(bean, pd);
			if (GeneralUtils.isNull(val))
				continue;
			beanMap.put(pd.getName(), val);
		}
		return beanMap;
	}

	/**
	 * class Foo { String name; Integer age; get/set... }
	 * 
	 * ==>
	 * 
	 * Map { foo.name : name, foo.age : age }
	 */

	public static Map<String, Object> beanToMapAddClass(Object bean) {
		Map<String, Object> map = beanToMap(bean);
		Map<String, Object> reMap = new LinkedHashMap<String, Object>(map.size());
		String clzName = bean.getClass().getName();
		for (Entry<String, Object> entry : map.entrySet()) {
			reMap.put(clzName + StringPool.DOT + entry.getKey(), entry.getValue());
		}
		return reMap;
	}

	public static <T> T mapToBean(Map<String, Object> beanMap, Class<T> tClass) {
		T bean = (T) ReflectUtils.newInstanceWithoutArgs(tClass);
		mapToBean(beanMap, bean);
		return bean;
	}

	public static void mapToBean(Map<String, Object> beanMap, Object target) {
		Iterator<Entry<String, Object>> originItr = beanMap.entrySet().iterator();
		while (originItr.hasNext()) {
			Entry<String, Object> entry = originItr.next();
			setProperty(target, entry.getKey(), entry.getValue());
		}
	}

	public static <T> List<T> listMapToListBean(List<Map<String, Object>> listMap, Class<T> tClass) {
		List<T> beanList = new ArrayList<>(listMap.size());
		for (Map<String, Object> data : listMap) {
			beanList.add(mapToBean(data, tClass));
		}
		return beanList;
	}

	public static <T extends Collection<String>> Map<String, Object> populateRequestMap(Map<String, T> requestMap) {
		Map<String, Object> parmsMap = new HashMap<String, Object>(requestMap.size());
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

	public static void setProperty(Object origin, Object target, PropertyDescriptor descriptor) {
		Object value = getProperty(origin, descriptor);
		setProperty(target, descriptor, value);
	}

	public static void setProperty(Object target, PropertyDescriptor descriptor, Object value) {
		if (BeanInfoUtils.OBJECT_PROPERTY.contains(descriptor))
			return;
		Method setMethod = descriptor.getWriteMethod();
		if (GeneralUtils.isNull(setMethod))
			return;
		MethodUtils.invoke(setMethod, target, value);
	}

	public static Object getProperty(Object target, PropertyDescriptor descriptor) {
		if (BeanInfoUtils.OBJECT_PROPERTY.contains(descriptor))
			return null;
		Method getMethod = descriptor.getReadMethod();
		if (GeneralUtils.isNull(getMethod))
			return null;
		return MethodUtils.invoke(getMethod, target);
	}

	public static Object getProperty(Object target, String name) {
		if (ClassUtils.isMap(target.getClass()))
			return ((Map<?, ?>) target).get(name);
		return getProperty(target, BeanInfoUtils.findPropertyDescriptor(target.getClass(), name));
	}

	@SuppressWarnings("unchecked")
	public static void setProperty(Object target, String name, Object value) {
		if (ClassUtils.isMap(target.getClass())) {
			((Map<String, Object>) target).put(name, value);
			return;
		}
		setProperty(target, BeanInfoUtils.findPropertyDescriptor(target.getClass(), name), value);
	}

	// BeanFactory

	public static String getClassNameForGroup(Class<?> clazz) {
		return clazz.getSimpleName();
	}

	public static String getClassNameForGroup(BeanDefinition define) {
		return getClassNameForGroup(define.getMetaType());
	}

	// throw

	public static void throwNoBeanFind(Class<?> clazz) {
		throw new IllegalArgumentException(
				"Can't find BeanDefinition for class:" + clazz + ",maybe no interface or implement registered.");
	}

	public static void throwNoBeanFind(Class<?> clazz, String group) {
		throw new IllegalArgumentException("Can't find BeanDefinition for class:" + clazz + " by groupName:" + group
				+ ",maybe no implement registered.");
	}

	public static void throwNoGroupFind(Class<?> clazz, String group) {
		throw new IllegalArgumentException("Can't find group information [" + group + "] or on the class:" + clazz
				+ ",maybe no SPI Annotation on the class.");
	}

	public static void throwDupGroup(Class<?> ifsClass, String group, Class<?>... dupClass) {
		throw new IllegalStateException("The class [ " + ifsClass + " ] has more than one implement class "
				+ Arrays.toString(dupClass) + " on same group [" + group + "].");
	}

	public static void throwNoConstructor(Class<?> clazz, String group) {
		throw new IllegalStateException(
				"Can't find useable Constructor in the class [ " + clazz + " ] on group [" + group + "].");
	}

	public static void throwDupConstructor(Class<?> clazz, String group) {
		throw new IllegalStateException(
				"The class [ " + clazz + " ] has more than one Constructor on same group [" + group + "].");
	}

}
