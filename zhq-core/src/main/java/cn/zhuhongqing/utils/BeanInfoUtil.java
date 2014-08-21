package cn.zhuhongqing.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.zhuhongqing.exception.UtilsException;

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

public class BeanInfoUtil {

	static List<PropertyDescriptor> OBJECT_PROPERTY = new ArrayList<PropertyDescriptor>(
			0);

	static {
		OBJECT_PROPERTY = Arrays.asList(getPropertyDescriptors(Object.class));
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
		BeanInfo beanInfo = getBeanInfo(beanClass);
		return beanInfo.getPropertyDescriptors();
	}

	static BeanInfo getBeanInfo(Class<?> beanClass) {
		try {
			return Introspector.getBeanInfo(beanClass);
		} catch (IntrospectionException e) {
			throw new UtilsException(e);
		}
	}

	public static PropertyDescriptor findPropertyDescriptor(Object target,
			String name) {
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
