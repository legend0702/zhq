package cn.zhuhongqing.ref;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import cn.zhuhongqing.exception.ExceptionMessage;
import cn.zhuhongqing.exception.UncheckedException;
import cn.zhuhongqing.exception.i18n.GeneralExceptionType;
import cn.zhuhongqing.utils.GeneralUtil;

/**
 * <pre>
 * 
 * Method utilities.
 * 
 * Depend on java-reflect.
 * 
 * </pre>
 * 
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 * 
 */

public class MethodUtil {

	private static final MethodUtil METHOD_UTILS = new MethodUtil();

	/**
	 * methodCache
	 */

	private static HashMap<String, Method> methodCache = new HashMap<String, Method>();

	/**
	 * a singleton
	 */

	protected MethodUtil() {
	}

	/**
	 * @see {@link java.lang.Class#getDeclaredMethods()};
	 * 
	 * @param clazz
	 * @return
	 */

	public static Collection<Method> getDeclaredMethods(Class<?> clazz) {
		return Arrays.asList(clazz.getDeclaredMethods());
	}

	/**
	 * 得到class的公共、保护、默认（包）访问和私有方法,但不包括继承的方法。
	 * 
	 * @param clazz
	 *            需要得到方法的class
	 * @return
	 */

	public static Collection<Method> getAllDeclaredMethods(Class<?> clazz) {
		return getSuperDeclaredMethods(clazz, clazz.getSuperclass());
	}

	/**
	 * 得到class的公共、保护、默认（包）访问和私有方法,包括继承的方法。
	 * 
	 * @param clazz
	 *            需要得到方法的class
	 * @return
	 */

	public static Collection<Method> getSuperDeclaredMethods(Class<?> clazz) {
		return getSuperDeclaredMethods(clazz, Object.class);
	}

	/**
	 * 得到class的公共、保护、默认（包）访问和私有方法,包括继承的方法。
	 * 
	 * 可以指定到哪层继承的级别停止
	 * 
	 * 
	 * @param clazz
	 *            需要得到方法的class
	 * @param stopClass
	 *            到哪层继承停止
	 * @return
	 */

	public static Collection<Method> getSuperDeclaredMethods(Class<?> clazz,
			Class<?> stopClass) {
		Collection<Method> methods = new HashSet<Method>();
		getSuperDeclaredMethods(clazz, stopClass, methods);
		return methods;
	}

	/**
	 * 得到class的公共、保护、默认（包）访问和私有方法,包括继承的方法。
	 * 
	 * 可以指定到哪层继承的级别停止,由于Object为最基类,故到Object也会停止
	 * 
	 * @param clazz
	 *            需要得到方法的class
	 * @param stopClass
	 *            到哪层继承停止
	 * @param methodContainer
	 *            装载方法的容器
	 * 
	 * @return
	 */

	private static void getSuperDeclaredMethods(Class<?> clazz,
			Class<?> stopClass, Collection<Method> methodContainer) {
		methodContainer.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		Class<?> superClass = clazz.getSuperclass();
		if (!(superClass.equals(Object.class) || stopClass.equals(superClass))) {
			getSuperDeclaredMethods(superClass, stopClass, methodContainer);
		}
	}

	/**
	 * 从公共方法中寻找方法名为methodName,参数类型为parameterTypes的方法
	 * 
	 * @param methodName
	 *            想要找到的方法的名字
	 * @param clazz
	 *            寻找的目标类
	 * @param parameterTypes
	 *            寻找的方法的参数
	 * @return
	 * @throws UtilsException
	 */

	public static Method findPublicMethod(String methodName, Class<?> clazz,
			Class<?>... parameterTypes) {
		return findMethod(methodName, clazz, MethodType.PUBLIC_METHODS,
				parameterTypes);
	}

	/**
	 * 从公共、保护、私有方法中寻找方法名为methodName,参数类型为parameterTypes的方法
	 * 
	 * @param methodName
	 *            想要找到的方法的名字
	 * @param clazz
	 *            寻找的目标类
	 * @param parameterTypes
	 *            寻找的方法的参数
	 * @return
	 * @throws UtilsException
	 */

	public static Method findDeclaredMethod(String methodName, Class<?> clazz,
			Class<?>... parameterTypes) {
		return findMethod(methodName, clazz, MethodType.DECLARED_METHODS,
				parameterTypes);
	}

	/**
	 * 从公共、保护、私有方法中寻找方法名为methodName,参数类型为parameterTypes的方法
	 * 
	 * 包括父类的私有方法
	 * 
	 * @param methodName
	 *            想要找到的方法的名字
	 * @param clazz
	 *            寻找的目标类
	 * @param parameterTypes
	 *            寻找的方法的参数
	 * @return
	 * @throws UtilsException
	 */

	public static Method findSuperDeclaredMethod(String methodName,
			Class<?> clazz, Class<?>... parameterTypes) {
		return findMethod(methodName, clazz, MethodType.SUPER_DECLARED_METHODS,
				parameterTypes);
	}

	/**
	 * 从公共、保护、私有方法中寻找方法名为methodName,参数类型为parameterTypes的方法
	 * 
	 * 
	 * @param methodName
	 * @param clazz
	 * @param methodType
	 * @param parameterTypes
	 * @return
	 * @throws UtilsException
	 */

	public static Method findMethod(String methodName, Class<?> clazz,
			MethodType methodType, Class<?>... parameterTypes) {

		// 参数为空就抛出异常
		if (GeneralUtil.hasNull(methodName, clazz, methodType)) {
			throw new IllegalArgumentException(new ExceptionMessage(
					GeneralExceptionType.DEBUG1001).toString());
		}

		Method returnMethod = null;

		returnMethod = methodCache.get(createCacheKey(clazz, methodName,
				parameterTypes));
		if (returnMethod != null) {
			return returnMethod;
		}

		/*
		 * 先用原生态方法获取一下
		 */

		try {
			returnMethod = clazz.getMethod(methodName, parameterTypes);
			methodCache.put(createCacheKey(clazz, methodName, parameterTypes),
					returnMethod);
			return returnMethod;
		} catch (NoSuchMethodException | SecurityException e) {
			// This is ok
		}

		/*
		 * 获得该class中的方法 进行遍历 返回匹配的
		 */

		Collection<Method> methodList = methodType.invoke(clazz);

		Iterator<Method> itr = methodList.iterator();
		while (itr.hasNext()) {
			Method methodTemp = itr.next();
			if (methodTemp.getName().equals(methodName)) {
				// 如果有参数类型限制,则需要匹配
				if (parameterTypes.length > 0) {
					if (Arrays.equals(methodTemp.getParameterTypes(),
							parameterTypes)) {
						returnMethod = methodTemp;
						break;
					}
				} else {
					returnMethod = methodTemp;
					break;
				}
			}
		}

		if (returnMethod == null) {
			// 没匹配到则报异常
			throw new UncheckedException(new ExceptionMessage(
					GeneralExceptionType.DEBUG1002, clazz.getCanonicalName()
							+ "[" + methodName + "]").toString());
		}
		methodCache.put(createCacheKey(clazz, methodName, parameterTypes),
				returnMethod);
		return returnMethod;
	}

	/**
	 * 生成缓存中的KEY值。
	 */
	private static String createCacheKey(Class<?> clazz, String methodName,
			Class<?>... arguments) {
		StringBuffer sb = new StringBuffer();
		sb.append(clazz.getName()).append(".").append(methodName);
		if ((arguments != null) && (arguments.length != 0)) {
			for (Class<?> arg : arguments) {
				sb.append(".").append(arg);
			}
		}
		return sb.toString();
	}

	public HashMap<String, Method> getMethodCache() {
		return methodCache;
	}

	/**
	 * 获得方法的方法
	 * 
	 * @author zhq mail:qwepoidjdj(a)gmail.com
	 * @version $Id: MethodUtils.java 20 2014-01-07 11:17:36Z legend $
	 * @since 1.5
	 * 
	 * 
	 */

	enum MethodType {
		PUBLIC_METHODS("getPublicMethods"), DECLARED_METHODS(
				"getDeclaredMethods"), SUPER_DECLARED_METHODS(
				"getSuperDeclaredMethods");

		private Method method;

		MethodType(String methodName) {
			try {
				this.method = MethodUtil.class.getMethod(methodName,
						Class.class);
			} catch (NoSuchMethodException | SecurityException e) {
				// why can not find ?
				throw new UncheckedException(e);
			}
		}

		@SuppressWarnings("unchecked")
		public Collection<Method> invoke(Class<?> clazz) {
			try {
				return (Collection<Method>) method.invoke(METHOD_UTILS, clazz);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// why can not find ?
				throw new UncheckedException(e);
			}
		}

	}

}
