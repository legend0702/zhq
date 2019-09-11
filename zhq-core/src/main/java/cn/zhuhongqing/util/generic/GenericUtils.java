package cn.zhuhongqing.util.generic;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Map;

import cn.zhuhongqing.bean.BeanInfoUtils;
import cn.zhuhongqing.util.ArraysUtils;
import cn.zhuhongqing.util.BeanWrap;
import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.ReflectUtils;

/**
 * Some utilities for generic.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class GenericUtils {

	/**
	 * Determine the target type for the generic return type of the given
	 * <em>generic method</em>, where formal type variables are declared on the
	 * given method itself.
	 * <p>
	 * For example, given a factory method with the following signature, if
	 * {@code resolveReturnTypeForGenericMethod()} is invoked with the reflected
	 * method for {@code creatProxy()} and an {@code Object[]} array containing
	 * {@code MyService.class}, {@code resolveReturnTypeForGenericMethod()} will
	 * infer that the target return type is {@code MyService}.
	 *
	 * <pre class="code">
	 * {@code public static <T> T createProxy(Class<T> clazz)}
	 * </pre>
	 *
	 * <h4>Possible Return Values</h4>
	 * <ul>
	 * <li>the target return type, if it can be inferred</li>
	 * <li>the {@linkplain Method#getReturnType() standard return type}, if the
	 * given {@code method} does not declare any
	 * {@linkplain Method#getTypeParameters() formal type variables}</li>
	 * <li>the {@linkplain Method#getReturnType() standard return type}, if the
	 * target return type cannot be inferred (e.g., due to type erasure)</li>
	 * <li>{@code null}, if the length of the given arguments array is shorter
	 * than the length of the {@linkplain Method#getGenericParameterTypes()
	 * formal argument list} for the given method</li>
	 * </ul>
	 *
	 * @param method
	 *            the method to introspect, never {@code null}
	 * @param args
	 *            the arguments that will be supplied to the method when it is
	 *            invoked (never {@code null})
	 * @param classLoader
	 *            the ClassLoader to resolve class names against, if necessary
	 *            (may be {@code null})
	 * @return the resolved target return type, the standard return type, or
	 *         {@code null}
	 * @since 3.2.5
	 * @see #resolveReturnType
	 */
	public static Class<?> resolveReturnTypeForGenericMethod(Method method, Object[] args, ClassLoader classLoader) {

		TypeVariable<Method>[] declaredTypeVariables = method.getTypeParameters();
		// No declared type variables to inspect, so just return the standard
		// return type.
		if (declaredTypeVariables.length == 0) {
			return method.getReturnType();
		}

		Type genericReturnType = method.getGenericReturnType();
		Type[] methodArgumentTypes = method.getGenericParameterTypes();

		// The supplied argument list is too short for the method's signature,
		// so
		// return null, since such a method invocation would fail.
		if (args.length < methodArgumentTypes.length) {
			return null;
		}

		// Ensure that the type variable (e.g., T) is declared directly on the
		// method
		// itself (e.g., via <T>), not on the enclosing class or interface.
		boolean locallyDeclaredTypeVariableMatchesReturnType = false;
		for (TypeVariable<Method> currentTypeVariable : declaredTypeVariables) {
			if (currentTypeVariable.equals(genericReturnType)) {
				locallyDeclaredTypeVariableMatchesReturnType = true;
				break;
			}
		}

		if (locallyDeclaredTypeVariableMatchesReturnType) {
			for (int i = 0; i < methodArgumentTypes.length; i++) {
				Type currentMethodArgumentType = methodArgumentTypes[i];
				if (currentMethodArgumentType.equals(genericReturnType)) {
					return args[i].getClass();
				}
				if (currentMethodArgumentType instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) currentMethodArgumentType;
					Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
					for (Type typeArg : actualTypeArguments) {
						if (typeArg.equals(genericReturnType)) {
							Object arg = args[i];
							if (arg instanceof Class) {
								return (Class<?>) arg;
							} else if (arg instanceof String && classLoader != null) {
								try {
									return classLoader.loadClass((String) arg);
								} catch (ClassNotFoundException ex) {
									throw new IllegalStateException(
											"Could not resolve specific class name argument [" + arg + "]", ex);
								}
							} else {
								// Consider adding logic to determine the class
								// of the typeArg, if possible.
								// For now, just fall back...
								return method.getReturnType();
							}
						}
					}
				}
			}
		}

		// Fall back...
		return method.getReturnType();
	}

	/**
	 * Get first generic type.
	 * 
	 * @see #resolveTypeArgument(Class, Class, int)
	 */

	public static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> generic) {
		return resolveTypeArgument(clazz, generic, 0);
	}

	/**
	 * Resolve the single type argument of the given generic interface against
	 * the given target class which is assumed to implement the generic
	 * interface and possibly declare a concrete type for its type variable.
	 *
	 * @param clazz
	 *            the target class to check against
	 * @param genericIfc
	 *            the generic interface or superclass to resolve the type
	 *            argument from
	 * @return the resolved type of the argument, or {@code null} if not
	 *         resolvable
	 */
	public static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> generic, int index) {
		ResolvableType resolvableType = forClassAs(clazz, generic);
		if (!resolvableType.hasGenerics()) {
			return null;
		}
		return getSingleGeneric(resolvableType, index);
	}

	private static ResolvableType forClassAs(Class<?> clazz, Class<?> generic) {
		return ResolvableType.forClass(clazz).as(generic);
	}

	private static Class<?> getSingleGeneric(ResolvableType resolvableType, int index) {
		return resolvableType.getGeneric(index).resolve();
	}

	/**
	 * Resolve the type arguments of the given generic interface against the
	 * given target class which is assumed to implement the generic interface
	 * and possibly declare concrete types for its type variables.
	 *
	 * @param clazz
	 *            the target class to check against
	 * @param genericIfc
	 *            the generic interface or superclass to resolve the type
	 *            argument from
	 * @return the resolved type of each argument, with the array size matching
	 *         the number of actual type arguments, or {@code null} if not
	 *         resolvable
	 */
	public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
		ResolvableType type = ResolvableType.forClass(clazz).as(genericIfc);
		if (!type.hasGenerics() || type.isEntirelyUnresolvable()) {
			return null;
		}
		return type.resolveGenerics(Object.class);
	}

	public static Class<?>[] resolvePropertyArguments(PropertyDescriptor property) {
		if (BeanInfoUtils.isReadable(property)) {
			return null;
		}

		if (BeanInfoUtils.isWriteable(property)) {
			return null;
		}
		return null;
	}

	public static Object resolveMethodArguments(Method method) {
		Type[] declaredTypeVariables = method.getGenericParameterTypes();
		// No declared type variables to inspect, so just return the standard
		// return type.
		if (declaredTypeVariables.length == 0) {
			return method.getParameterTypes();
		}
		return declaredTypeVariables;
	}

	public static void main(String[] args) {
		// add elementData(int)
		Method method = ReflectUtils.getSupportedMethods(TestClass.class, "iterator");
		// System.out.println(resolveMethodArguments(method));
		System.out.println(resolveReturnTypeForGenericMethod(method, ArraysUtils.emptyArray(Object.class),
				ClassUtils.getDefaultClassLoader()));
	}

	static class TestClass extends ArrayList<BeanWrap> {
		private static final long serialVersionUID = 1L;

		public void test(Map<String, BeanWrap> wrap) {
		}
	}
}
