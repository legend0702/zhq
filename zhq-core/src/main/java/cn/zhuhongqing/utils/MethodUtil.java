package cn.zhuhongqing.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.zhuhongqing.exception.UtilsException;

/**
 * Method utilities.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class MethodUtil {

	public static Object invoke(Method method, Object target, Object... params) {
		ReflectUtil.makeAccessible(method);
		try {
			return method.invoke(target, params);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new UtilsException(e);
		}
	}

}
