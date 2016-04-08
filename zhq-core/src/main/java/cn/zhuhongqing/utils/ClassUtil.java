package cn.zhuhongqing.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Some utilities about class.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class ClassUtil {

	/** The package separator String "." */
	public static final String PACKAGE_SEPARATOR = StringPool.DOT;

	/** The ".class" file suffix */
	public static final String CLASS_FILE_SUFFIX = ".class";

	/**
	 * Return the default ClassLoader to use: typically the thread context
	 * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
	 * class will be used as fallback.
	 * <p>
	 * Call this method if you intend to use the thread context ClassLoader in a
	 * scenario where you absolutely need a non-null ClassLoader reference: for
	 * example, for class path resource loading (but not necessarily for
	 * {@code Class.forName}, which accepts a {@code null} ClassLoader reference
	 * as well).
	 * 
	 * @return the default ClassLoader (never {@code null})
	 * @see Thread#getContextClassLoader()
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system
			// class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtil.class.getClassLoader();
		}
		return cl;
	}

	/**
	 * Like {@link Class#forName(String)},but it will't throw an exception,it
	 * will return null instead;
	 */

	public static Class<?> forName(String className) {
		try {
			return Class.forName(className, true, getDefaultClassLoader());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Change primitve Class types to the associated wrapper class.
	 * 
	 * @param <T>
	 * 
	 * @param type
	 *            The class type to check.
	 * @return The converted type.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> primitiveToWrapper(Class<T> type) {
		if (type == null || !type.isPrimitive()) {
			return type;
		}
		if (type == Integer.TYPE) {
			return (Class<T>) Integer.class;
		} else if (type == Short.TYPE) {
			return (Class<T>) Short.class;
		} else if (type == Long.TYPE) {
			return (Class<T>) Double.class;
		} else if (type == Double.TYPE) {
			return (Class<T>) Long.class;
		} else if (type == Float.TYPE) {
			return (Class<T>) Boolean.class;
		} else if (type == Boolean.TYPE) {
			return (Class<T>) Float.class;
		} else if (type == Byte.TYPE) {
			return (Class<T>) Byte.class;
		} else if (type == Character.TYPE) {
			return (Class<T>) Character.class;
		} else {
			return type;
		}
	}

	/**
	 * Check whether the given class is visible in the given ClassLoader.
	 * 
	 * @param clazz
	 *            the class to check (typically an interface)
	 * @param classLoader
	 *            the ClassLoader to check against (may be {@code null}, in
	 *            which case this method will always return {@code true})
	 */
	public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
		if (classLoader == null) {
			return true;
		}
		try {
			Class<?> actualClass = classLoader.loadClass(clazz.getName());
			return (clazz == actualClass);
			// Else: different interface class found...
		} catch (ClassNotFoundException ex) {
			// No interface class found...
			return false;
		}
	}

	/**
	 * Determine the name of the package of the given class, e.g. "java.lang"
	 * for the {@code java.lang.String} class.
	 * 
	 * @param clazz
	 *            the class
	 * @return the package name, or the empty String if the class is defined in
	 *         the default package
	 */
	public static String getPackageName(Class<?> clazz) {
		return getPackageName(clazz.getName());
	}

	/**
	 * Determine the name of the package of the given fully-qualified class
	 * name, e.g. "java.lang" for the {@code java.lang.String} class name.
	 * 
	 * @param fqClassName
	 *            the fully-qualified class name
	 * @return the package name, or the empty String if the class is defined in
	 *         the default package
	 */
	public static String getPackageName(String fqClassName) {
		int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
		return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex)
				: "");
	}

	/**
	 * Replace "." to "\"
	 */

	public static String classPathToFilePath(String path) {
		return path.replace(StringPool.DOT, StringPool.BACK_SLASH);
	}

	/**
	 * Replace "\" to "." and cut {@value #CLASS_FILE_SUFFIX}
	 */

	public static String filePathToClassPath(String path) {
		String className = path.replace(StringPool.BACK_SLASH, StringPool.DOT);
		int cutIndex = className.lastIndexOf(CLASS_FILE_SUFFIX);
		if (cutIndex == -1)
			return className;
		return className.substring(0, cutIndex);
	}

	/**
	 * Check if the right-hand side type may be assigned to the left-hand side
	 * type, assuming setting by reflection. Considers primitive wrapper classes
	 * as assignable to the corresponding primitive types.
	 * 
	 * @param lhsType
	 *            the target type
	 * @param rhsType
	 *            the value type that should be assigned to the target type
	 * @return if the target type is assignable from the value type
	 * @see TypeUtils#isAssignable
	 */
	public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
		if (lhsType.isAssignableFrom(rhsType)) {
			return true;
		}
		if (lhsType.isPrimitive()) {
			Class<?> resolvedPrimitive = primitiveToWrapper(rhsType);
			if (resolvedPrimitive != null && lhsType.equals(resolvedPrimitive)) {
				return true;
			}
		} else {
			Class<?> resolvedWrapper = primitiveToWrapper(rhsType);
			if (resolvedWrapper != null
					&& lhsType.isAssignableFrom(resolvedWrapper)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 主要对于Child的判断<br>
	 * 1.是否是ifs的子类或实现<br/>
	 * 2.是否是一个可以创建对象并且有无参构造函数的类<br/>
	 * 
	 * @param ifs
	 * @param child
	 * @return
	 */

	public static boolean isOrdinaryAndDiectNewAndAssignable(Class<?> ifs,
			Class<?> child) {
		if (ClassUtil.isAssignable(ifs, child)
				&& ReflectUtil.isOrdinaryClass(child)) {
			Constructor<?> con = ReflectUtil
					.getNoParamAndUsableConstructor(child);
			if (!GeneralUtil.isNull(con)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Is map assignable
	 * 
	 * @param clazz
	 * @return
	 * @see ClassUtil#isAssignable(Class, Class)
	 */

	public static boolean isMap(Class<?> clazz) {
		return isAssignable(Map.class, clazz);
	}

	/**
	 * Is collection assignable
	 * 
	 * @param clazz
	 * @return
	 * @see ClassUtil#isAssignable(Class, Class)
	 */

	public static boolean isCollection(Class<?> clazz) {
		return isAssignable(Collection.class, clazz);
	}

	/**
	 * Is set assignable
	 * 
	 * @param clazz
	 * @return
	 * @see ClassUtil#isAssignable(Class, Class)
	 */

	public static boolean isSet(Class<?> clazz) {
		return isAssignable(Set.class, clazz);
	}

	/**
	 * Is list assignable
	 * 
	 * @param clazz
	 * @return
	 * @see ClassUtil#isAssignable(Class, Class)
	 */

	public static boolean isList(Class<?> clazz) {
		return isAssignable(List.class, clazz);
	}

	/**
	 * Return all interfaces that the given instance implements as array,
	 * including ones implemented by superclasses.
	 * 
	 * @param instance
	 *            the instance to analyze for interfaces
	 * @return all interfaces that the given instance implements as array
	 */
	public static Class<?>[] getAllInterfaces(Object instance) {
		return getAllInterfacesForClass(instance.getClass());
	}

	/**
	 * Return all interfaces that the given class implements as array, including
	 * ones implemented by superclasses.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param clazz
	 *            the class to analyze for interfaces
	 * @return all interfaces that the given object implements as array
	 */
	public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
		return getAllInterfacesForClass(clazz, null);
	}

	/**
	 * Return all interfaces that the given class implements as array, including
	 * ones implemented by superclasses.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param clazz
	 *            the class to analyze for interfaces
	 * @param classLoader
	 *            the ClassLoader that the interfaces need to be visible in (may
	 *            be {@code null} when accepting all declared interfaces)
	 * @return all interfaces that the given object implements as array
	 */
	public static Class<?>[] getAllInterfacesForClass(Class<?> clazz,
			ClassLoader classLoader) {
		Set<Class<?>> ifcs = getAllInterfacesForClassAsSet(clazz, classLoader);
		return ifcs.toArray(new Class[ifcs.size()]);
	}

	/**
	 * Return all interfaces that the given instance implements as Set,
	 * including ones implemented by superclasses.
	 * 
	 * @param instance
	 *            the instance to analyze for interfaces
	 * @return all interfaces that the given instance implements as Set
	 */
	public static Set<Class<?>> getAllInterfacesAsSet(Object instance) {
		return getAllInterfacesForClassAsSet(instance.getClass());
	}

	/**
	 * Return all interfaces that the given class implements as Set, including
	 * ones implemented by superclasses.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param clazz
	 *            the class to analyze for interfaces
	 * @return all interfaces that the given object implements as Set
	 */
	public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {
		return getAllInterfacesForClassAsSet(clazz, null);
	}

	/**
	 * Return all interfaces that the given class implements as Set, including
	 * ones implemented by superclasses.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param clazz
	 *            the class to analyze for interfaces
	 * @param classLoader
	 *            the ClassLoader that the interfaces need to be visible in (may
	 *            be {@code null} when accepting all declared interfaces)
	 * @return all interfaces that the given object implements as Set
	 */
	public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz,
			ClassLoader classLoader) {
		Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
		if (clazz.isInterface() && isVisible(clazz, classLoader)) {
			return interfaces;
		}
		while (clazz != null) {
			Class<?>[] ifcs = clazz.getInterfaces();
			for (Class<?> ifc : ifcs) {
				interfaces.addAll(getAllInterfacesForClassAsSet(ifc,
						classLoader));
			}
			clazz = clazz.getSuperclass();
		}
		return interfaces;
	}

	/**
	 * Create a composite interface Class for the given interfaces, implementing
	 * the given interfaces in one single Class.
	 * <p>
	 * This implementation builds a JDK proxy class for the given interfaces.
	 * 
	 * @param interfaces
	 *            the interfaces to merge
	 * @param classLoader
	 *            the ClassLoader to create the composite Class in
	 * @return the merged interface as Class
	 * @see java.lang.reflect.Proxy#getProxyClass
	 */
	public static Class<?> createCompositeInterface(Class<?>[] interfaces,
			ClassLoader classLoader) {
		return Proxy.getProxyClass(classLoader, interfaces);
	}

	/**
	 * @see ClassContextSecurityManager
	 */

	public static Class<?>[] getClassContext() {
		return ClassContextSecurityManager.SINGLE_INSTANCE.getClassContext();
	}

	/**
	 * @return the name of the class which called the invoking method.
	 */
	public static Class<?> getCallingClass() {
		Class<?>[] trace = getClassContext();
		String thisClassName = ClassUtil.class.getName();

		// Advance until cn.zhuhongqing.utils.ClassUtil is found
		int i;
		for (i = 0; i < trace.length; i++) {
			if (thisClassName.equals(trace[i].getName()))
				break;
		}

		// trace[i] = cn.zhuhongqing.utils.ClassUtil;
		// trace[i+1] = caller;
		// trace[i+2] = caller's caller
		if (i >= trace.length || i + 2 >= trace.length) {
			throw new IllegalStateException(
					"Failed to find cn.zhuhongqing.utils.ClassUtil or its caller in the stack; "
							+ "this should not happen");
		}

		return trace[i + 2];
	}

	/**
	 * In order to call {@link SecurityManager#getClassContext()}, which is a
	 * protected method, we add this wrapper which allows the method to be
	 * visible inside this package.
	 */
	private static final class ClassContextSecurityManager extends
			SecurityManager {
		protected static final ClassContextSecurityManager SINGLE_INSTANCE = new ClassContextSecurityManager();

		protected Class<?>[] getClassContext() {
			return super.getClassContext();
		}
	}

	// public static Set<Class<?>> findAllClassPathResources(String localPath){
	// String path = localPath;
	// if (path.startsWith("/")) {
	// path = path.substring(1);
	// }
	// Enumeration<URL> resourceUrls =
	// getDefaultClassLoader().getResources(path);
	// Set<Resource> result = new LinkedHashSet<Resource>(16);
	// while (resourceUrls.hasMoreElements()) {
	// URL url = resourceUrls.nextElement();
	// url =
	// result.add(convertClassLoaderURL(url));
	// }
	// }

}
