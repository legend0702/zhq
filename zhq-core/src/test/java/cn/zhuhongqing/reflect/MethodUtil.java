package cn.zhuhongqing.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Method utilities.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 * 
 */

public class MethodUtil {

	/**
	 * A cache for Class-Methods :)
	 */

	static final MethodCache METHOD_CACHE = new MethodCache();

	MethodUtil() {
	}

	public static Method getDeclaredMethods(Class<?> clazz, String methodName,
			Class<?>... paramTypes) {
		// OneClassMethodSet methodContainer = METHOD_CACHE.getAndPut(clazz);
		return null;
	}

	/**
	 * @see {@link java.lang.Class#getDeclaredMethods()}
	 * 
	 * @param clazz
	 * @return
	 */

	public static Method[] getDeclaredMethods(Class<?> clazz) {
		return clazz.getDeclaredMethods();
	}

	/**
	 * @see {@link java.lang.Class#getMethods()}
	 * 
	 * @param clazz
	 * @return
	 */

	public static Method[] getMethods(Class<?> clazz) {
		return clazz.getMethods();
	}

	/**
	 * get Super-DeclaredMethods.
	 * 
	 * stopClass is means where to stop.
	 * 
	 * @param clazz
	 * @param stopClass
	 * @return
	 */

	public static <T> Collection<Method> getSuperDeclaredMethods(
			Class<?> clazz, Class<? super T> stopClass) {

		getSuperDeclaredMethods(clazz, stopClass);
		// return METHOD_CACHE.get(key).getMethods();
		return null;
	}

	/**
	 * get SuperClass-DeclaredMethods.
	 * 
	 * stopClass is means where to stop.
	 * 
	 * default stop is Object
	 * 
	 * @param clazz
	 * @param stopClass
	 * @param methodContainer
	 *            {@link OneClassMethodSet}
	 */

	static void getSuperDeclaredMethods0(Class<?> clazz, Class<?> stopClass) {
		OneClassMethodSet methodContainer = METHOD_CACHE.getAndPut(clazz);
		methodContainer.addAll(clazz.getDeclaredMethods());
		List<Class<?>> superClassAndInterface = new ArrayList<Class<?>>(
				Arrays.asList(clazz.getInterfaces()));
		superClassAndInterface.add(clazz);
		if (!(clazz.equals(Object.class) || superClassAndInterface
				.contains(stopClass))) {
			Class<?> superClass = clazz.getSuperclass();
			getSuperDeclaredMethods0(superClass, stopClass);
		}
	}

	static class MethodCache extends HashMap<Class<?>, OneClassMethodSet> {

		private static final long serialVersionUID = 1L;

		public OneClassMethodSet getAndPut(Class<?> key) {
			OneClassMethodSet oneClassMethodSet = super.get(key);
			if (oneClassMethodSet == null) {
				oneClassMethodSet = new OneClassMethodSet();
				put(key, oneClassMethodSet);
			}
			return oneClassMethodSet;
		}
	}

	/**
	 * first come first served.
	 * 
	 * use {@link #createMethodKey(Method)} to compare
	 * 
	 */

	static class OneClassMethodSet {

		// HashMap<String, Method> methodSet = new HashMap<String,
		// Method>();

		/**
		 * a methodMap,key create by {@link #createMethodKey(Method)}
		 */

		// Test
		HashMap<String, Method> methodSet = new LinkedHashMap<String, Method>();

		void addAll(Method[] methods) {
			for (Method m : methods)
				add(m);
		}

		/**
		 * check key to add.
		 * 
		 * @param e
		 * @return
		 */

		boolean add(Method e) {
			String key = createMethodKey(e);
			if (methodSet.get(key) != null)
				return false;
			methodSet.put(key, e);
			return true;
		}

		/***
		 * return a new ArrayList
		 * 
		 * @return
		 */

		Collection<Method> getMethods() {
			return new ArrayList<Method>(methodSet.values());
		}

		/**
		 * Create a key base on method's name and parameterTypes.
		 * 
		 * @param method
		 * @return
		 */

		private String createMethodKey(Method method) {
			StringBuffer sb = new StringBuffer();
			sb.append(method.getName());
			Class<?>[] arguments = method.getParameterTypes();
			for (Class<?> arg : arguments)
				sb.append(".").append(arg);
			return sb.toString();
		}
	}
}
