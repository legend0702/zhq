package cn.zhuhongqing.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.util.GeneralUtils;

/**
 * Some utilities for JavaBean's meta-data.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 *
 */

public class BeanInfoUtils {

	static Set<PropertyDescriptor> OBJECT_PROPERTY = Collections.emptySet();

	static {
		OBJECT_PROPERTY = new HashSet<>(getPropertyDescriptorsList(Object.class));
	}

	static BeanInfo getBeanInfo(Class<?> beanClass) {
		try {
			return Introspector.getBeanInfo(beanClass);
		} catch (IntrospectionException e) {
			throw new UtilsException(e);
		}
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
		return getBeanInfo(beanClass).getPropertyDescriptors();
	}

	public static List<PropertyDescriptor> getPropertyDescriptorsList(Class<?> beanClass) {
		return Arrays.asList(getPropertyDescriptors(beanClass));
	}

	public static PropertyDescriptor findPropertyDescriptor(Class<?> target, String name) {
		PropertyDescriptor[] props = getPropertyDescriptors(target);
		for (int i = 0; i < props.length; i++) {
			if (OBJECT_PROPERTY.contains(props[i]))
				continue;
			if (props[i].getName().equals(name))
				return props[i];
		}
		throw new UtilsException("Can not find [" + name + "] property from" + target);
	}

	public static boolean isWriteable(PropertyDescriptor descriptor) {
		return GeneralUtils.isNotNull(descriptor.getWriteMethod());
	}

	public static boolean isReadable(PropertyDescriptor descriptor) {
		return GeneralUtils.isNotNull(descriptor.getReadMethod());
	}

}