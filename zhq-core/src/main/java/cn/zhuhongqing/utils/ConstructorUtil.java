package cn.zhuhongqing.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cn.zhuhongqing.exception.UtilsException;

public class ConstructorUtil {

	public static Object invoke(Constructor<?> con, Object... params) {
		ReflectUtil.makeAccessible(con);
		try {
			return con.newInstance(params);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new UtilsException(e);
		} finally {
			ReflectUtil.closeAccessible(con);
		}
	}

}
