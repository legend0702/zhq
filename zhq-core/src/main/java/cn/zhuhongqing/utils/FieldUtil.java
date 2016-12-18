package cn.zhuhongqing.utils;

import java.lang.reflect.Field;

import cn.zhuhongqing.exception.UtilsException;

public class FieldUtil {

	public static boolean set(Field field, Object obj, Object param) {
		if (!ReflectUtil.makeAccessible(field)) {
			return false;
		}
		try {
			field.set(obj, param);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new UtilsException(e);
		} finally {
			ReflectUtil.closeAccessible(field);
		}
	}

	public static Object get(Field field, Object obj) {
		if (!ReflectUtil.makeAccessible(field)) {
			return false;
		}
		try {
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new UtilsException(e);
		} finally {
			ReflectUtil.closeAccessible(field);
		}
	}

}
