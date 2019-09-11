package cn.zhuhongqing.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some utilities about class.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class ClassUtils {

	private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

	/** Object's class. */
	public static final Class<?> OBJECT_CLASS = Object.class;
	/** String's class. */
	public static final Class<?> STRING_CLASS = String.class;

	/** The package separator String "." */
	public static final String PACKAGE_SEPARATOR = StringPool.DOT;

	/** The ".class" file suffix */
	public static final String CLASS_FILE_SUFFIX = ".class";

	/**
	 * Map with primitive type as key and corresponding wrapper type as value,
	 * for example: int.class -> Integer.class.
	 */
	private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_WRAPPER_MAP = new IdentityHashMap<Class<?>, Class<?>>(
			8);

	/**
	 * Map with common "java.lang" class name as key and corresponding Class as
	 * value. Primarily for efficient deserialization of remote invocations.
	 */
	private static final Map<String, Class<?>> COMMON_CLASS = new HashMap<String, Class<?>>(32);

	static {
		PRIMITIVE_TYPE_WRAPPER_MAP.put(Boolean.TYPE, Boolean.class);
		PRIMITIVE_TYPE_WRAPPER_MAP.put(Byte.TYPE, Byte.class);
		PRIMITIVE_TYPE_WRAPPER_MAP.put(Character.TYPE, Character.class);
		PRIMITIVE_TYPE_WRAPPER_MAP.put(Double.TYPE, Double.class);
		PRIMITIVE_TYPE_WRAPPER_MAP.put(Float.TYPE, Float.class);
		PRIMITIVE_TYPE_WRAPPER_MAP.put(Integer.TYPE, Integer.class);
		PRIMITIVE_TYPE_WRAPPER_MAP.put(Long.TYPE, Integer.class);
		PRIMITIVE_TYPE_WRAPPER_MAP.put(Short.TYPE, Short.class);

		for (Map.Entry<Class<?>, Class<?>> entry : PRIMITIVE_TYPE_WRAPPER_MAP.entrySet()) {
			registerCommonClasses(entry.getValue());
		}

		registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Double[].class, Float[].class,
				Integer[].class, Long[].class, Short[].class);
		registerCommonClasses(Number.class, Number[].class, String.class, String[].class, Object.class, Object[].class,
				Class.class, Class[].class);
		registerCommonClasses(Throwable.class, Exception.class, RuntimeException.class, Error.class,
				StackTraceElement.class, StackTraceElement[].class);
	}

	/**
	 * Register the given common classes with the ClassUtils cache.
	 */
	private static void registerCommonClasses(Class<?>... commonClasses) {
		for (Class<?> clazz : commonClasses) {
			COMMON_CLASS.put(clazz.getName(), clazz);
		}
	}

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
			cl = ClassUtils.class.getClassLoader();
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
			log.warn("Can't find class for className:" + className, e);
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
		return (Class<T>) PRIMITIVE_TYPE_WRAPPER_MAP.get(type);
	}

	public static boolean isCommonClass(Class<?> type) {
		return COMMON_CLASS.containsKey(type.getName());
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

	public static boolean isClassFile(String path) {
		return path.endsWith(CLASS_FILE_SUFFIX);
	}

	public static Enumeration<URL> getResources(String name) {
		Enumeration<URL> urls = null;
		try {
			urls = getDefaultClassLoader().getResources(name);
			if (GeneralUtils.isNull(urls)) {
				urls = ClassLoader.getSystemResources(name);
			}
		} catch (IOException e) {
			return null;
		}
		return urls;
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
		return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
	}

	/**
	 * Replace {@link StringPool#FILE_SEPARATOR} to "." and cut
	 * {@value #CLASS_FILE_SUFFIX}
	 */

	public static String cleanSuffixAndToClass(String path) {
		String className = StringUtils.cleanPath(path).replace(StringPool.FILE_SEPARATOR, StringPool.DOT);
		int cutIndex = className.lastIndexOf(CLASS_FILE_SUFFIX);
		if (cutIndex == -1)
			return className;
		return className.substring(0, cutIndex);
	}

	public static String classToSlash(String clazType) {
		String str = StringUtils.replaceDotToSlash(clazType);
		return StringUtils.endPadSlash(str);
	}

	public static boolean isObjectClass(Class<?> clazz) {
		return OBJECT_CLASS.equals(clazz);
	}

	/**
	 * Is abstract class?
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * A pure class like {@link Object}.
	 */

	public static boolean isPureClass(Class<?> clazz) {
		if (isAbstract(clazz) || clazz.isInterface() || clazz.isPrimitive() || clazz.isArray() || clazz.isEnum()
				|| clazz.isAnnotation() || clazz.isAnonymousClass() || clazz.isSynthetic() || clazz.isLocalClass()
				|| clazz.isMemberClass()) {
			return false;
		}
		return true;
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
	 * @see Class#isAssignableFrom(Class)
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
			if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {
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

	public static boolean isOrdinaryAndDiectNewAndAssignable(Class<?> ifs, Class<?> child) {
		if (ClassUtils.isAssignable(ifs, child) && ReflectUtils.isOrdinaryClass(child)) {
			Constructor<?> con = ReflectUtils.getNoParamAndUsableConstructor(child);
			if (!GeneralUtils.isNull(con)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param name
	 *            {@link Class#getName()}
	 */

	public static boolean isArray(String name) {
		// "java.lang.String[]" style arrays
		if (name.endsWith(StringPool.ARRAY_SUFFIX)) {
			return true;
		}

		// "[Ljava.lang.String;" style arrays
		if (name.startsWith(StringPool.NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
			return true;
		}

		// "[[I" or "[[Ljava.lang.String;" style arrays
		if (name.startsWith(StringPool.INTERNAL_ARRAY_PREFIX)) {
			return true;
		}
		return false;
	}

	/**
	 * Is map assignable
	 * 
	 * @param clazz
	 * @return
	 * @see ClassUtils#isAssignable(Class, Class)
	 */

	public static boolean isMap(Class<?> clazz) {
		return isAssignable(Map.class, clazz);
	}

	/**
	 * Is collection assignable
	 * 
	 * @param clazz
	 * @return
	 * @see ClassUtils#isAssignable(Class, Class)
	 */

	public static boolean isCollection(Class<?> clazz) {
		return isAssignable(Collection.class, clazz);
	}

	/**
	 * Is set assignable
	 * 
	 * @param clazz
	 * @return
	 * @see ClassUtils#isAssignable(Class, Class)
	 */

	public static boolean isSet(Class<?> clazz) {
		return isAssignable(Set.class, clazz);
	}

	/**
	 * Is list assignable
	 * 
	 * @param clazz
	 * @return
	 * @see ClassUtils#isAssignable(Class, Class)
	 */

	public static boolean isList(Class<?> clazz) {
		return isAssignable(List.class, clazz);
	}

	public static boolean hasInterface(Class<?> clazz) {
		return !ArraysUtils.isEmpty(clazz.getInterfaces());
	}

	public static boolean hasSuperClass(Class<?> clazz) {
		Class<?> superClass = clazz.getSuperclass();
		return !(OBJECT_CLASS.equals(superClass) || superClass == null);
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
	public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader) {
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
	public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, ClassLoader classLoader) {
		if (clazz.isInterface() && isVisible(clazz, classLoader)) {
			return Collections.singleton(clazz);
		}
		Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
		while (clazz != null) {
			Class<?>[] ifcs = clazz.getInterfaces();
			for (Class<?> ifc : ifcs) {
				interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
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
	public static Class<?> createCompositeInterface(Class<?>[] interfaces, ClassLoader classLoader) {
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
		String thisClassName = ClassUtils.class.getName();

		// Advance until cn.zhuhongqing.util.ClassUtils is found
		int i;
		for (i = 0; i < trace.length; i++) {
			if (thisClassName.equals(trace[i].getName()))
				break;
		}

		// trace[i] = cn.zhuhongqing.util.ClassUtils;
		// trace[i+1] = caller;
		// trace[i+2] = caller's caller
		if (i >= trace.length || i + 2 >= trace.length) {
			throw new IllegalStateException("Failed to find cn.zhuhongqing.util.ClassUtils or its caller in the stack; "
					+ "this should not happen");
		}

		return trace[i + 2];
	}

	/**
	 * @return the name of the class which called the invoking method.
	 */
	public static Class<?> getCallingClassOut() {
		Class<?>[] trace = getClassContext();
		String thisClassName = ClassUtils.class.getName();
		Class<?> outCallClass = null;

		// Advance until cn.zhuhongqing.util.ClassUtils is found
		int i, j = 0;
		for (i = 0; i < trace.length; i++) {
			if (thisClassName.equals(trace[i].getName())) {
				if (j == 0)
					j = 1;
			} else {
				if (j == 0)
					continue;
				if (outCallClass == null) {
					outCallClass = trace[i];
				} else {
					if (!trace[i].equals(outCallClass))
						return trace[i];
				}
			}
		}
		throw new IllegalStateException("Can't find the caller class in the call stack");
	}

	/**
	 * level = 0 = cn.zhuhongqing.util.ClassUtils<br/>
	 * level = 1 = caller class
	 * 
	 * @return the name of the class which called the invoking method.
	 */
	public static Class<?> getCallingClass(int level) {
		Class<?>[] trace = getClassContext();
		// skip cn.zhuhongqing.util.ClassUtils$ClassContextSecurityManager
		int deep = 2;
		if (level >= (trace.length - deep)) {
			throw new IllegalStateException("The level is over the call stack");
		}
		// level = 0 = cn.zhuhongqing.util.ClassUtils
		// level = 1 = caller class
		return trace[deep + level];
	}

	/**
	 * In order to call {@link SecurityManager#getClassContext()}, which is a
	 * protected method, we add this wrapper which allows the method to be
	 * visible inside this package.
	 */
	private static final class ClassContextSecurityManager extends SecurityManager {
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
