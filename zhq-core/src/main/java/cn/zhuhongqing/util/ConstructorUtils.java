package cn.zhuhongqing.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cn.zhuhongqing.exception.UtilsException;

public class ConstructorUtils {

	public static Object invoke(Constructor<?> con, Object... params) {
		try {
			ReflectUtils.makeAccessible(con);
			return con.newInstance(params);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new UtilsException(e);
		} finally {
			ReflectUtils.closeAccessible(con);
		}
	}

}
