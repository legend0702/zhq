package cn.zhuhongqing.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.zhuhongqing.exception.UtilsException;

/**
 * Some reflect utilities.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */
public class ReflectUtil {

	/** Empty class array. */
	public static final Class<?>[] NO_PARAMETERS = new Class[0];

	/** Empty object array. */
	public static final Object[] NO_ARGUMENTS = new Object[0];

	/** Empty type array. */
	public static final Type[] NO_TYPES = new Type[0];

	public static final int MEMBER_ABSTRACT = 1025;

	/** Object's class. */
	public static final Class<?> OBJECT_CLASS = Object.class;

	/** Bean's operate prefix. */
	public static final String METHOD_GET_PREFIX = "get";
	public static final String METHOD_IS_PREFIX = "is";
	public static final String METHOD_SET_PREFIX = "set";

	// ---------------------------------------------------------------- method0
	private static Method _getMethod0;

	static {
		try {
			_getMethod0 = Class.class.getDeclaredMethod("getMethod0",
					String.class, Class[].class);
			_getMethod0.setAccessible(true);
		} catch (Exception ignore) {
			try {
				_getMethod0 = Class.class.getMethod("getMethod", String.class,
						Class[].class);
			} catch (Exception ignored) {
				_getMethod0 = null;
			}
		}
	}

	/**
	 * Invokes private <code>Class.getMethod0()</code> without throwing
	 * <code>NoSuchMethodException</code>. Returns only public methods or
	 * <code>null</code> if method not found. Since no exception is throwing, it
	 * works faster.
	 * 
	 * @param c
	 *            class to inspect
	 * @param name
	 *            name of method to find
	 * @param parameterTypes
	 *            parameter types
	 * @return founded method, or null
	 */
	public static Method getMethod0(Class<?> c, String name,
			Class<?>... parameterTypes) {
		try {
			return (Method) _getMethod0.invoke(c, name, parameterTypes);
		} catch (Exception ignore) {
			return null;
		}
	}

	// ---------------------------------------------------------------- find
	// method

	/**
	 * Returns method from an object, matched by name. This may be considered as
	 * a slow operation, since methods are matched one by one. Returns only
	 * accessible methods. Only first method is matched.
	 * 
	 * @param c
	 *            class to examine
	 * @param methodName
	 *            Full name of the method.
	 * @return null if method not found
	 */
	public static Method findMethod(Class<?> c, String methodName) {
		return findDeclaredMethod(c, methodName, true);
	}

	/**
	 * @see #findMethod(Class, String)
	 */
	public static Method findDeclaredMethod(Class<?> c, String methodName) {
		return findDeclaredMethod(c, methodName, false);
	}

	private static Method findDeclaredMethod(Class<?> c, String methodName,
			boolean publicOnly) {
		if ((methodName == null) || (c == null)) {
			return null;
		}
		Method[] ms = publicOnly ? c.getMethods() : c.getDeclaredMethods();
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		return null;
	}

	// ---------------------------------------------------------------- classes

	/**
	 * Returns classes from array of specified objects.
	 */
	public static Class<?>[] getClasses(Object... objects) {
		if (objects == null) {
			return null;
		}
		Class<?>[] result = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				result[i] = objects[i].getClass();
			}
		}
		return result;
	}

	// ---------------------------------------------------------------- invoke

	/**
	 * Invokes accessible method of an object.
	 * 
	 * @param c
	 *            class that contains method
	 * @param obj
	 *            object to execute
	 * @param method
	 *            method to invoke
	 * @param paramClasses
	 *            classes of parameters
	 * @param params
	 *            parameters
	 */
	public static Object invoke(Class<?> c, Object obj, String method,
			Class<?>[] paramClasses, Object[] params) {
		Method m;
		try {
			m = c.getMethod(method, paramClasses);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new UtilsException(e);
		}
		try {
			return m.invoke(obj, params);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new UtilsException(e);
		}
	}

	/**
	 * Invokes static method.
	 */
	public static Object invoke(Class<?> c, String method,
			Class<?>[] paramClasses, Object[] params) {
		return invoke(c, null, method, paramClasses, params);
	}

	/**
	 * Invokes accessible method of an object.
	 * 
	 * @param obj
	 *            object
	 * @param method
	 *            name of the objects method
	 * @param params
	 *            method parameters
	 * @param paramClasses
	 *            method parameter types
	 */
	public static Object invoke(Object obj, String method,
			Class<?>[] paramClasses, Object[] params) {
		return invoke(obj.getClass(), method, paramClasses, params);
	}

	/**
	 * Invokes accessible method of an object without specifying parameter
	 * types.
	 * 
	 * @param obj
	 *            object
	 * @param method
	 *            method of an object
	 * @param params
	 *            method parameters
	 */
	public static Object invoke(Object obj, String method, Object... params) {
		Class<?>[] paramClass = getClasses(params);
		return invoke(obj, method, paramClass, params);
	}

	public static Object invoke(Class<?> c, Object obj, String method,
			Object... params) {
		Class<?>[] paramClass = getClasses(params);
		return invoke(c, obj, method, paramClass, params);
	}

	/**
	 * Invokes static method.
	 */
	public static Object invoke(Class<?> c, String method, Object... params) {
		Class<?>[] paramClass = getClasses(params);
		return invoke(c, null, method, paramClass, params);
	}

	// ----------------------------------------------------------------
	// invokeDeclared

	/**
	 * Invokes any method of a class, even private ones.
	 * 
	 * @param c
	 *            class to examine
	 * @param obj
	 *            object to inspect
	 * @param method
	 *            method to invoke
	 * @param paramClasses
	 *            parameter types
	 * @param params
	 *            parameters
	 */
	public static Object invokeDeclared(Class<?> c, Object obj, String method,
			Class<?>[] paramClasses, Object[] params) {
		Method m;
		try {
			m = c.getDeclaredMethod(method, paramClasses);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new UtilsException("Is it a declared method?", e);
		}
		m.setAccessible(true);
		try {
			return m.invoke(obj, params);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new UtilsException("Is it a callable method?", e);
		}
	}

	public static Object invokeDeclared(Class<?> c, String method,
			Class<?>[] paramClasses, Object[] params) {
		return invokeDeclared(c, null, method, paramClasses, params);
	}

	/**
	 * Invokes any method of a class suppressing java access checking.
	 * 
	 * @param obj
	 *            object to inspect
	 * @param method
	 *            method to invoke
	 * @param paramClasses
	 *            parameter types
	 * @param params
	 *            parameters
	 */
	public static Object invokeDeclared(Object obj, String method,
			Class<?>[] paramClasses, Object[] params) {
		return invokeDeclared(obj.getClass(), obj, method, paramClasses, params);
	}

	public static Object invokeDeclared(Object obj, String method,
			Object... params) {
		Class<?>[] paramClasses = getClasses(params);
		return invokeDeclared(obj.getClass(), obj, method, paramClasses, params);
	}

	public static Object invokeDeclared(Class<?> c, Object obj, String method,
			Object... params) {
		Class<?>[] paramClass = getClasses(params);
		return invokeDeclared(c, obj, method, paramClass, params);
	}

	// public static Object invokeDeclared(Class<?> c, String method,
	// Object... params) {
	// Class<?>[] paramClass = getClasses(params);
	// return invokeDeclared(c, null, method, paramClass, params);
	// }

	// ---------------------------------------------------------------- match
	// classes

	/**
	 * Determines if first class match the destination and simulates kind of
	 * <code>instanceof</code>. All subclasses and interface of first class are
	 * examined against second class. Method is not symmetric.
	 */
	public static boolean isSubclass(Class<?> thisClass, Class<?> target) {
		if (target.isInterface()) {
			return isInterfaceImpl(thisClass, target);
		}
		for (Class<?> x = thisClass; x != null; x = x.getSuperclass()) {
			if (x == target) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if provided class is interface implementation.
	 */
	public static boolean isInterfaceImpl(Class<?> thisClass,
			Class<?> targetInterface) {
		for (Class<?> x = thisClass; x != null; x = x.getSuperclass()) {
			Class<?>[] interfaces = x.getInterfaces();
			for (Class<?> i : interfaces) {
				if (i == targetInterface) {
					return true;
				}
				if (isInterfaceImpl(i, targetInterface)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Dynamic version of <code>instanceof</code>.
	 * 
	 * @param o
	 *            object to match
	 * @param target
	 *            target class
	 * @return <code>true</code> if object is an instance of target class
	 */
	public static boolean isInstanceOf(Object o, Class<?> target) {
		return isSubclass(o.getClass(), target);
	}

	/**
	 * Get topClass's direct subClass.
	 * 
	 * Don't put same class.
	 */

	@SuppressWarnings("unchecked")
	public static <T, S extends T> Class<T> getTopClass(Class<S> subClass,
			Class<T> topClass) {
		if (topClass.isInterface()) {
			return getTopClassOfInterface(subClass, topClass);
		}
		Class<T> tClass = (Class<T>) subClass;
		while (!topClass.equals(tClass.getSuperclass())) {
			tClass = (Class<T>) tClass.getSuperclass();
		}
		return tClass;
	}

	/**
	 * @see ReflectUtil#getTopClass(Class, Class)
	 */

	@SuppressWarnings("unchecked")
	public static <T, S extends T> Class<T> getTopClassOfInterface(
			Class<S> subClass, Class<T> topInterface) {
		for (Class<T> x = (Class<T>) subClass; x != OBJECT_CLASS; x = (Class<T>) x
				.getSuperclass()) {
			Class<?>[] interfaces = x.getInterfaces();
			for (Class<?> i : interfaces) {
				if (i == topInterface) {
					return x;
				}
			}
		}
		return null;
	}

	/**
	 * Is abstract class?
	 */

	public static boolean isAbstract(Class<?> claz) {
		return MEMBER_ABSTRACT == claz.getModifiers();
	}

	/**
	 * A class is not Interface and abstract.
	 * 
	 */

	public static boolean isOrdinaryClass(Class<?> claz) {
		return (!claz.isInterface() && !isAbstract(claz));
	}

	// ----------------------------------------------------------------
	// accessible methods

	/**
	 * Returns array of all methods that are accessible from given class.
	 * 
	 * @see #getAccessibleMethods(Class, Class)
	 */
	public static Method[] getAccessibleMethods(Class<?> clazz) {
		return getAccessibleMethods(clazz, OBJECT_CLASS);
	}

	/**
	 * Returns array of all methods that are accessible from given class, upto
	 * limit (usually <code>Object.class</code>). Abstract methods are ignored.
	 */
	public static Method[] getAccessibleMethods(Class<?> clazz, Class<?> limit) {
		Package topPackage = clazz.getPackage();
		List<Method> methodList = new ArrayList<Method>();
		int topPackageHash = topPackage == null ? 0 : topPackage.hashCode();
		boolean top = true;
		do {
			if (clazz == null) {
				break;
			}
			Method[] declaredMethods = clazz.getDeclaredMethods();
			for (Method method : declaredMethods) {
				if (Modifier.isVolatile(method.getModifiers())) {
					continue;
				}
				// if (Modifier.isAbstract(method.getModifiers())) {
				// continue;
				// }
				if (top == true) { // add all top declared methods
					methodList.add(method);
					continue;
				}
				int modifier = method.getModifiers();
				if (Modifier.isPrivate(modifier) == true) {
					continue; // ignore super private methods
				}
				if (Modifier.isAbstract(modifier) == true) { // ignore super
																// abstract
																// methods
					continue;
				}
				if (Modifier.isPublic(modifier) == true) {
					addMethodIfNotExist(methodList, method); // add super public
																// methods
					continue;
				}
				if (Modifier.isProtected(modifier) == true) {
					addMethodIfNotExist(methodList, method); // add super
																// protected
																// methods
					continue;
				}
				// add super default methods from the same package
				Package pckg = method.getDeclaringClass().getPackage();
				int pckgHash = pckg == null ? 0 : pckg.hashCode();
				if (pckgHash == topPackageHash) {
					addMethodIfNotExist(methodList, method);
				}
			}
			top = false;
		} while ((clazz = clazz.getSuperclass()) != limit);

		Method[] methods = new Method[methodList.size()];
		for (int i = 0; i < methods.length; i++) {
			methods[i] = methodList.get(i);
		}
		return methods;
	}

	private static void addMethodIfNotExist(List<Method> allMethods,
			Method newMethod) {
		for (Method m : allMethods) {
			if (compareSignatures(m, newMethod) == true) {
				return;
			}
		}
		allMethods.add(newMethod);
	}

	// ----------------------------------------------------------------
	// accessible fields

	public static Field[] getAccessibleFields(Class<?> clazz) {
		return getAccessibleFields(clazz, OBJECT_CLASS);
	}

	public static Field[] getAccessibleFields(Class<?> clazz, Class<?> limit) {
		Package topPackage = clazz.getPackage();
		List<Field> fieldList = new ArrayList<Field>();
		int topPackageHash = topPackage == null ? 0 : topPackage.hashCode();
		boolean top = true;
		do {
			if (clazz == null) {
				break;
			}
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				if (top == true) { // add all top declared fields
					fieldList.add(field);
					continue;
				}
				int modifier = field.getModifiers();
				if (Modifier.isPrivate(modifier) == true) {
					continue; // ignore super private fields
				}
				if (Modifier.isPublic(modifier) == true) {
					addFieldIfNotExist(fieldList, field); // add super public
															// methods
					continue;
				}
				if (Modifier.isProtected(modifier) == true) {
					addFieldIfNotExist(fieldList, field); // add super protected
															// methods
					continue;
				}
				// add super default methods from the same package
				Package pckg = field.getDeclaringClass().getPackage();
				int pckgHash = pckg == null ? 0 : pckg.hashCode();
				if (pckgHash == topPackageHash) {
					addFieldIfNotExist(fieldList, field);
				}
			}
			top = false;
		} while ((clazz = clazz.getSuperclass()) != limit);

		Field[] fields = new Field[fieldList.size()];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fieldList.get(i);
		}
		return fields;
	}

	private static void addFieldIfNotExist(List<Field> allFields, Field newField) {
		for (Field f : allFields) {
			if (compareSignatures(f, newField) == true) {
				return;
			}
		}
		allFields.add(newField);
	}

	// ----------------------------------------------------------------
	// supported methods

	public static Method[] getSupportedMethods(Class<?> clazz) {
		return getSupportedMethods(clazz, OBJECT_CLASS);
	}

	/**
	 * Returns a <code>Method</code> array of the methods to which instances of
	 * the specified respond except for those methods defined in the class
	 * specified by limit or any of its superclasses. Note that limit is usually
	 * used to eliminate them methods defined by <code>java.lang.Object</code>.
	 * If limit is <code>null</code> then all methods are returned.
	 */
	public static Method[] getSupportedMethods(Class<?> clazz, Class<?> limit) {
		ArrayList<Method> supportedMethods = new ArrayList<Method>();
		for (Class<?> c = clazz; c != limit; c = c.getSuperclass()) {
			Method[] methods = c.getDeclaredMethods();
			for (Method method : methods) {
				boolean found = false;
				for (Method supportedMethod : supportedMethods) {
					if (compareSignatures(method, supportedMethod)) {
						found = true;
						break;
					}
				}
				if (found == false) {
					supportedMethods.add(method);
				}
			}
		}
		return supportedMethods.toArray(new Method[supportedMethods.size()]);
	}

	public static Field[] getSupportedFields(Class<?> clazz) {
		return getSupportedFields(clazz, OBJECT_CLASS);
	}

	public static Field[] getSupportedFields(Class<?> clazz, Class<?> limit) {
		ArrayList<Field> supportedFields = new ArrayList<Field>();
		for (Class<?> c = clazz; c != limit; c = c.getSuperclass()) {
			Field[] fields = c.getDeclaredFields();
			for (Field field : fields) {
				boolean found = false;
				for (Field supportedField : supportedFields) {
					if (compareSignatures(field, supportedField)) {
						found = true;
						break;
					}
				}
				if (found == false) {
					supportedFields.add(field);
				}
			}
		}
		return supportedFields.toArray(new Field[supportedFields.size()]);
	}

	// ---------------------------------------------------------------- compare

	/**
	 * Compares method declarations: signature and return types.
	 */
	public static boolean compareDeclarations(Method first, Method second) {
		if (first.getReturnType() != second.getReturnType()) {
			return false;
		}
		return compareSignatures(first, second);
	}

	/**
	 * Compares method signatures: names and parameters.
	 */
	public static boolean compareSignatures(Method first, Method second) {
		if (first.getName().equals(second.getName()) == false) {
			return false;
		}
		return compareParameters(first.getParameterTypes(),
				second.getParameterTypes());
	}

	/**
	 * Compares constructor signatures: names and parameters.
	 */
	public static boolean compareSignatures(Constructor<?> first,
			Constructor<?> second) {
		if (first.getName().equals(second.getName()) == false) {
			return false;
		}
		return compareParameters(first.getParameterTypes(),
				second.getParameterTypes());
	}

	public static boolean compareSignatures(Field first, Field second) {
		return first.getName().equals(second.getName());
	}

	/**
	 * Compares method or ctor parameters.
	 */
	public static boolean compareParameters(Class<?>[] first, Class<?>[] second) {
		if (first.length != second.length) {
			return false;
		}
		for (int i = 0; i < first.length; i++) {
			if (first[i] != second[i]) {
				return false;
			}
		}
		return true;
	}

	// ---------------------------------------------------------------- force

	/**
	 * Suppress access check against a reflection object. SecurityException is
	 * silently ignored. Checks first if the object is already accessible.
	 */
	public static void forceAccess(AccessibleObject accObject) {
		if (accObject.isAccessible() == true) {
			return;
		}
		try {
			accObject.setAccessible(true);
		} catch (SecurityException sex) {
			// ignore
		}
	}

	// ---------------------------------------------------------------- is
	// public

	/**
	 * Returns <code>true</code> if class member is public.
	 */
	public static boolean isPublic(Member member) {
		return Modifier.isPublic(member.getModifiers());
	}

	/**
	 * Returns <code>true</code> if class member is public and if its declaring
	 * class is also public.
	 */
	public static boolean isPublicPublic(Member member) {
		if (Modifier.isPublic(member.getModifiers()) == true) {
			if (Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if class is public.
	 */
	public static boolean isPublic(Class<?> c) {
		return Modifier.isPublic(c.getModifiers());
	}

	// ---------------------------------------------------------------- create

	/**
	 * Creates new instances including for common mutable classes that do not
	 * have a default constructor. more user-friendly. It examines if class is a
	 * map, list, String, Character, Boolean or a Number. Immutable instances
	 * are cached and not created again. Arrays are also created with no
	 * elements. Note that this bunch of <code>if</code> blocks is faster then
	 * using a <code>HashMap</code>.
	 * 
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> type, Object... args) {
		Object returnObj = null;
		if (type.isPrimitive()) {
			if (type == int.class) {
				returnObj = Integer.valueOf(0);
			}
			if (type == long.class) {
				returnObj = Long.valueOf(0);
			}
			if (type == boolean.class) {
				returnObj = Boolean.FALSE;
			}
			if (type == float.class) {
				returnObj = Float.valueOf(0);
			}
			if (type == double.class) {
				returnObj = Double.valueOf(0);
			}
			if (type == byte.class) {
				returnObj = Byte.valueOf((byte) 0);
			}
			if (type == short.class) {
				returnObj = Short.valueOf((short) 0);
			}
			if (type == char.class) {
				returnObj = Character.valueOf((char) 0);
			}
			if (returnObj == null)
				throw new IllegalArgumentException("Invalid primitive type: "
						+ type);
			return (T) returnObj;
		}
		if (type == Integer.class) {
			returnObj = Integer.valueOf(0);
		}
		if (type == String.class) {
			returnObj = StringPool.EMPTY;
		}
		if (type == Long.class) {
			returnObj = Long.valueOf(0);
		}
		if (type == Boolean.class) {
			returnObj = Boolean.FALSE;
		}
		if (type == Float.class) {
			returnObj = Float.valueOf(0);
		}
		if (type == Double.class) {
			returnObj = Double.valueOf(0);
		}

		if (type == Map.class) {
			returnObj = new HashMap<Object, Object>();
		}
		if (type == List.class) {
			returnObj = new ArrayList<Object>();
		}
		if (type == Set.class) {
			returnObj = new LinkedHashSet<Object>();
		}
		if (type == Collection.class) {
			returnObj = new ArrayList<Object>();
		}

		if (type == Byte.class) {
			returnObj = Byte.valueOf((byte) 0);
		}
		if (type == Short.class) {
			returnObj = Short.valueOf((short) 0);
		}
		if (type == Character.class) {
			returnObj = Character.valueOf((char) 0);
		}

		if (type.isEnum() == true) {
			returnObj = type.getEnumConstants()[0];
		}

		if (type.isArray() == true) {
			returnObj = Array.newInstance(type.getComponentType(), 0);
		}

		if (returnObj != null)
			return (T) returnObj;
		returnObj = autoNewInstance(type, args);
		return (T) returnObj;
	}

	public static Object newInstance(String classFullName, Object... args) {
		try {
			return newInstance(Class.forName(classFullName), args);
		} catch (ClassNotFoundException e) {
			throw new UtilsException(e);
		}
	}

	/**
	 * 以参数匹配构造函数创建对象并返回(支持无参)
	 * 
	 * 如果有构造参数类型一样 则返回第一个匹配到的
	 * 
	 * @param tClass
	 *            创建的对象类型
	 * @param args
	 *            构造器参数
	 * @throws UtilsException
	 */

	@SuppressWarnings("unchecked")
	public static <T> T autoNewInstance(Class<T> tClass, Object... args) {

		if (null == args || args.length == 0) {
			return newInstanceWithoutArgs(tClass);
		}

		Constructor<?> finalConstructor = null;
		Constructor<?>[] constructors = tClass.getConstructors();
		for (Constructor<?> constructor : constructors) {
			Class<?>[] paramTypes = constructor.getParameterTypes();
			// 参数长度不一样直接忽略
			if (paramTypes.length != args.length)
				continue;
			for (int i = 0; i < paramTypes.length; i++) {
				// 参数类型错误直接忽略
				if (!paramTypes[i].isInstance(args[i]))
					break;
				if (i == paramTypes.length) {
					finalConstructor = constructor;
				}
			}
		}

		if (null == finalConstructor) {
			throw new UtilsException(new NoSuchMethodException(
					"Can not find a constructor for:" + tClass + ":" + args));
		}

		makeAccessible(finalConstructor);
		try {
			return (T) finalConstructor.newInstance(args);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new UtilsException(e);
		} finally {
			finalConstructor.setAccessible(false);
		}

	}

	public static <T> T newInstanceWithoutArgs(Class<T> tClass) {
		try {
			return tClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new UtilsException(e);
		}
	}

	// ---------------------------------------------------------------- misc

	/**
	 * Returns <code>true</code> if the first member is accessible from second
	 * one.
	 */
	public static boolean isAssignableFrom(Member member1, Member member2) {
		return member1.getDeclaringClass().isAssignableFrom(
				member2.getDeclaringClass());
	}

	/**
	 * Returns all superclasses.
	 */
	public static Class<?>[] getSuperclasses(Class<?> type) {
		int i = 0;
		for (Class<?> x = type.getSuperclass(); x != null; x = x
				.getSuperclass()) {
			i++;
		}
		Class<?>[] result = new Class[i];
		i = 0;
		for (Class<?> x = type.getSuperclass(); x != null; x = x
				.getSuperclass()) {
			result[i] = x;
			i++;
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if method is user defined and not defined in
	 * <code>Object</code> class.
	 */
	public static boolean isUserDefinedMethod(final Method method) {
		return method.getDeclaringClass() != OBJECT_CLASS;
	}

	/**
	 * Returns <code>true</code> if method defined in <code>Object</code> class.
	 */
	public static boolean isObjectMethod(final Method method) {
		return method.getDeclaringClass() == OBJECT_CLASS;
	}

	/**
	 * Returns <code>true</code> if method is a bean property.
	 */
	public static boolean isBeanProperty(Method method) {
		if (isObjectMethod(method)) {
			return false;
		}
		String methodName = method.getName();
		Class<?> returnType = method.getReturnType();
		Class<?>[] paramTypes = method.getParameterTypes();
		if (methodName.startsWith(METHOD_GET_PREFIX)) { // getter method must
														// starts with 'get' and
														// it is not getClass()
			if ((returnType != null) && (paramTypes.length == 0)) { // getter
																	// must have
																	// a return
																	// type and
																	// no
																	// arguments
				return true;
			}
		} else if (methodName.startsWith(METHOD_IS_PREFIX)) { // ister must
																// starts with
																// 'is'
			if ((returnType != null) && (paramTypes.length == 0)) { // ister
																	// must have
																	// return
																	// type and
																	// no
																	// arguments
				return true;
			}
		} else if (methodName.startsWith(METHOD_SET_PREFIX)) { // setter must
																// start with a
																// 'set'
			if (paramTypes.length == 1) { // setter must have just one argument
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if method is bean getter.
	 */
	public static boolean isBeanPropertyGetter(Method method) {
		return getBeanPropertyGetterPrefixLength(method) != 0;
	}

	private static int getBeanPropertyGetterPrefixLength(Method method) {
		if (isObjectMethod(method)) {
			return 0;
		}
		String methodName = method.getName();
		Class<?> returnType = method.getReturnType();
		Class<?>[] paramTypes = method.getParameterTypes();
		if (methodName.startsWith(METHOD_GET_PREFIX)) { // getter method must
														// starts with 'get' and
														// it is not getClass()
			if ((returnType != null) && (paramTypes.length == 0)) { // getter
																	// must have
																	// a return
																	// type and
																	// no
																	// arguments
				return 3;
			}
		} else if (methodName.startsWith(METHOD_IS_PREFIX)) { // ister must
																// starts with
																// 'is'
			if ((returnType != null) && (paramTypes.length == 0)) { // ister
																	// must have
																	// return
																	// type and
																	// no
																	// arguments
				return 2;
			}
		}
		return 0;
	}

	/**
	 * Returns beans property getter name or <code>null</code> if method is not
	 * a real getter.
	 */
	public static String getBeanPropertyGetterName(Method method) {
		int prefixLength = getBeanPropertyGetterPrefixLength(method);
		if (prefixLength == 0) {
			return null;
		}
		String methodName = method.getName().substring(prefixLength);
		return StringUtil.decapitalize(methodName);
	}

	/**
	 * Returns <code>true</code> if method is bean setter.
	 */
	public static boolean isBeanPropertySetter(Method method) {
		return getBeanPropertySetterPrefixLength(method) != 0;
	}

	private static int getBeanPropertySetterPrefixLength(Method method) {
		if (isObjectMethod(method)) {
			return 0;
		}
		String methodName = method.getName();
		Class<?>[] paramTypes = method.getParameterTypes();
		if (methodName.startsWith(METHOD_SET_PREFIX)) { // setter must start
														// with a 'set'
			if (paramTypes.length == 1) { // setter must have just one argument
				return 3;
			}
		}
		return 0;
	}

	/**
	 * Returns beans property setter name or <code>null</code> if method is not
	 * a real setter.
	 */
	public static String getBeanPropertySetterName(Method method) {
		int prefixLength = getBeanPropertySetterPrefixLength(method);
		if (prefixLength == 0) {
			return null;
		}
		String methodName = method.getName().substring(prefixLength);
		return StringUtil.decapitalize(methodName);
	}

	// ---------------------------------------------------------------- generics

	public static Class<?> getComponentType(Type type) {
		return getComponentType(type, null, 0);
	}

	public static Class<?> getComponentType(Type type, Class<?> implClass) {
		return getComponentType(type, implClass, 0);
	}

	public static Class<?> getComponentType(Type type, int index) {
		return getComponentType(type, null, index);
	}

	/**
	 * Returns the component type of the given type. Returns <code>null</code>
	 * if given type does not have a single component type. For example the
	 * following types all have the component-type MyClass:
	 * <ul>
	 * <li>MyClass<?>[]</li>
	 * <li>List&lt;MyClass&gt;</li>
	 * <li>Foo&lt;? extends MyClass&gt;</li>
	 * <li>Bar&lt;? super MyClass&gt;</li>
	 * <li>&lt;T extends MyClass&gt; T[]</li>
	 * </ul>
	 * 
	 * Index represents the index of component type, when class supports more
	 * then one. For example, <code>Map&lt;A, B&gt;</code> has 2 component
	 * types. If index is 0 or positive, than it represents order of component
	 * type. If the value is negative, then it represents component type counted
	 * from the begin! Therefore, the default value of <code>0</code> always
	 * returns the <b>first</b> component type.If you give less than 0,it will
	 * pick component type from the end.
	 */
	public static Class<?> getComponentType(Type type, Class<?> implClass,
			int index) {
		if (type instanceof Class) {
			Class<?> clazz = (Class<?>) type;
			if (clazz.isArray()) {
				return clazz.getComponentType();
			}
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Type[] generics = pt.getActualTypeArguments();
			if (index < 0) {
				index = generics.length + index;
			}
			if (index < generics.length) {
				return getRawType(generics[index], implClass);
			}
		} else if (type instanceof GenericArrayType) {
			GenericArrayType gat = (GenericArrayType) type;
			return getRawType(gat.getGenericComponentType(), implClass);
		}
		return null;
	}

	/**
	 * Get implement interface index in type's interfaces.
	 */

	public static int getInterfaceIndex(Class<?> type, Class<?> interfaceClass) {
		if (interfaceClass.isAssignableFrom(type)) {
			Class<?>[] interfaces = type.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				if (interfaces[i].equals(interfaceClass)) {
					return i;
				}
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns generic supertype for given class and 0-based index.
	 * 
	 * @see #getComponentType(java.lang.reflect.Type, int)
	 */
	public static Class<?> getGenericSupertype(Class<?> type, int index) {
		return getComponentType(type.getGenericSuperclass(), null, index);
	}

	/**
	 * @see #getComponentType(java.lang.reflect.Type)
	 */
	public static Class<?> getGenericSupertype(Class<?> type) {
		return getComponentType(type.getGenericSuperclass());
	}

	/**
	 * Returns raw class for given <code>type</code>. Use this method with both
	 * regular and generic types.
	 * 
	 * @param type
	 *            the type to convert
	 * @return the closest class representing the given <code>type</code>
	 * @see #getRawType(java.lang.reflect.Type, Class)
	 */
	public static Class<?> getRawType(Type type) {
		return getRawType(type, null);
	}

	/**
	 * Returns raw class for given <code>type</code> when implementation class
	 * is known and it makes difference.
	 * 
	 * @see #resolveVariable(java.lang.reflect.TypeVariable, Class)
	 */
	public static Class<?> getRawType(Type type, Class<?> implClass) {
		if (type instanceof Class) {
			return (Class<?>) type;
		}
		if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			return getRawType(pType.getRawType(), implClass);
		}
		if (type instanceof WildcardType) {
			WildcardType wType = (WildcardType) type;

			Type[] lowerTypes = wType.getLowerBounds();
			if (lowerTypes.length > 0) {
				return getRawType(lowerTypes[0], implClass);
			}

			Type[] upperTypes = wType.getUpperBounds();
			if (upperTypes.length != 0) {
				return getRawType(upperTypes[0], implClass);
			}

			return OBJECT_CLASS;
		}
		if (type instanceof GenericArrayType) {
			Type genericComponentType = ((GenericArrayType) type)
					.getGenericComponentType();
			Class<?> rawType = getRawType(genericComponentType, implClass);
			// this is sort of stupid, but there seems no other way (consider
			// don't creating new instances each time)...
			return Array.newInstance(rawType, 0).getClass();
		}
		if (type instanceof TypeVariable) {
			TypeVariable<?> varType = (TypeVariable<?>) type;
			if (implClass != null) {
				Type resolvedType = resolveVariable(varType, implClass);
				if (resolvedType != null) {
					return getRawType(resolvedType, null);
				}
			}
			Type[] boundsTypes = varType.getBounds();
			if (boundsTypes.length == 0) {
				return OBJECT_CLASS;
			}
			return getRawType(boundsTypes[0], implClass);
		}
		return null;
	}

	/**
	 * Resolves <code>TypeVariable</code> with given implementation class.
	 */
	public static Type resolveVariable(TypeVariable<?> variable,
			final Class<?> implClass) {
		final Class<?> rawType = getRawType(implClass, null);

		int index = ArraysUtil.indexOf(rawType.getTypeParameters(), variable);
		if (index >= 0) {
			return variable;
		}

		final Class<?>[] interfaces = rawType.getInterfaces();
		final Type[] genericInterfaces = rawType.getGenericInterfaces();

		for (int i = 0; i <= interfaces.length; i++) {
			Class<?> rawInterface;

			if (i < interfaces.length) {
				rawInterface = interfaces[i];
			} else {
				rawInterface = rawType.getSuperclass();
				if (rawInterface == null) {
					continue;
				}
			}

			final Type resolved = resolveVariable(variable, rawInterface);
			if (resolved instanceof Class
					|| resolved instanceof ParameterizedType) {
				return resolved;
			}

			if (resolved instanceof TypeVariable) {
				final TypeVariable<?> typeVariable = (TypeVariable<?>) resolved;
				index = ArraysUtil.indexOf(rawInterface.getTypeParameters(),
						typeVariable);

				if (index < 0) {
					throw new IllegalArgumentException(
							"Can't resolve type variable:" + typeVariable);
				}

				final Type type = i < genericInterfaces.length ? genericInterfaces[i]
						: rawType.getGenericSuperclass();

				if (type instanceof Class) {
					return OBJECT_CLASS;
				}

				if (type instanceof ParameterizedType) {
					return ((ParameterizedType) type).getActualTypeArguments()[index];
				}

				throw new IllegalArgumentException("Unsupported type: " + type);
			}
		}
		return null;
	}

	/**
	 * Converts <code>Type</code> to a <code>String</code>. Supports successor
	 * interfaces:
	 * <ul>
	 * <li><code>java.lang.Class</code> - represents usual class</li>
	 * <li><code>java.lang.reflect.ParameterizedType</code> - class with generic
	 * parameter (e.g. <code>List</code>)</li>
	 * <li><code>java.lang.reflect.TypeVariable</code> - generic type literal
	 * (e.g. <code>List</code>, <code>T</code> - type variable)</li>
	 * <li><code>java.lang.reflect.WildcardType</code> - wildcard type (
	 * <code>List&lt;? extends Number&gt;</code>, <code>"? extends Number</code>
	 * - wildcard type)</li>
	 * <li><code>java.lang.reflect.GenericArrayType</code> - type for generic
	 * array (e.g. <code>T[]</code>, <code>T</code> - array type)</li>
	 * </ul>
	 */
	public static String typeToString(Type type) {
		StringBuilder sb = new StringBuilder();
		typeToString(sb, type, new HashSet<Type>());
		return sb.toString();
	}

	private static void typeToString(StringBuilder sb, Type type,
			Set<Type> visited) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			sb.append(rawType.getName());
			boolean first = true;
			for (Type typeArg : parameterizedType.getActualTypeArguments()) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append('<');
				typeToString(sb, typeArg, visited);
				sb.append('>');
			}
		} else if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) type;
			sb.append('?');

			// According to
			// JLS(http://java.sun.com/docs/books/jls/third_edition/html/typesValues.html#4.5.1):
			// - Lower and upper can't coexist: (for instance, this is not
			// allowed: <? extends List<String> & super MyInterface>)
			// - Multiple bounds are not supported (for instance, this is not
			// allowed: <? extends List<String> & MyInterface>)

			final Type bound;
			if (wildcardType.getLowerBounds().length != 0) {
				sb.append(" super ");
				bound = wildcardType.getLowerBounds()[0];
			} else {
				sb.append(" extends ");
				bound = wildcardType.getUpperBounds()[0];
			}
			typeToString(sb, bound, visited);
		} else if (type instanceof TypeVariable<?>) {
			TypeVariable<?> typeVariable = (TypeVariable<?>) type;
			sb.append(typeVariable.getName());

			// prevent cycles in case: <T extends List<T>>

			if (!visited.contains(type)) {
				visited.add(type);
				sb.append(" extends ");
				boolean first = true;
				for (Type bound : typeVariable.getBounds()) {
					if (first) {
						first = false;
					} else {
						sb.append(" & ");
					}
					typeToString(sb, bound, visited);
				}
				visited.remove(type);
			}
		} else if (type instanceof GenericArrayType) {
			GenericArrayType genericArrayType = (GenericArrayType) type;
			typeToString(genericArrayType.getGenericComponentType());
			sb.append(genericArrayType.getGenericComponentType());
			sb.append("[]");
		} else if (type instanceof Class) {
			Class<?> typeClass = (Class<?>) type;
			sb.append(typeClass.getName());
		} else {
			throw new IllegalArgumentException("Unsupported type: " + type);
		}
	}

	// ----------------------------------------------------------------
	// annotations

	/**
	 * Reads annotation value. Returns <code>null</code> on error (e.g. when
	 * value name not found).
	 */
	public static Object readAnnotationValue(Annotation annotation, String name) {
		try {
			Method method = annotation.annotationType().getDeclaredMethod(name);
			return method.invoke(annotation);
		} catch (Exception ignore) {
			return null;
		}
	}

	// ---------------------------------------------------------------- caller

	private static class ReflectUtilSecurityManager extends SecurityManager {
		public Class<?> getCallerClass(int callStackDepth) {
			return getClassContext()[callStackDepth + 1];
		}
	}

	private static ReflectUtilSecurityManager SECURITY_MANAGER;

	static {
		try {
			SECURITY_MANAGER = new ReflectUtilSecurityManager();
		} catch (Exception ex) {
			SECURITY_MANAGER = null;
		}
	}

	/**
	 * Emulates <code>Reflection.getCallerClass</code> using standard API. This
	 * implementation uses custom <code>SecurityManager</code> and it is the
	 * fastest. Other implementations are:
	 * <ul>
	 * <li><code>new Throwable().getStackTrace()[callStackDepth]</code></li>
	 * <li><code>Thread.currentThread().getStackTrace()[callStackDepth]</code>
	 * (the slowest)</li>
	 * </ul>
	 * <p>
	 * In case when usage of <code>SecurityManager</code> is not allowed, this
	 * method fails back to the second implementation.
	 * <p>
	 * Note that original <code>Reflection.getCallerClass</code> is way faster
	 * then any emulation.
	 */
	public static Class<?> getCallerClass(int framesToSkip) {
		if (SECURITY_MANAGER != null) {
			return SECURITY_MANAGER.getCallerClass(framesToSkip);
		}

		StackTraceElement[] stackTraceElements = new Throwable()
				.getStackTrace();

		if (framesToSkip >= 2) {
			framesToSkip += 4;
		}

		String className = stackTraceElements[framesToSkip].getClassName();

		try {
			return Thread.currentThread().getContextClassLoader()
					.loadClass(className);
		} catch (ClassNotFoundException cnfex) {
			throw new UnsupportedOperationException(className + " not found.");
		}
	}

	/**
	 * Determine whether the given member is a "public static final" constant.
	 * 
	 * @param field
	 *            the field to check
	 */
	public static boolean isPublicStaticFinal(Member member) {
		int modifiers = member.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier
				.isFinal(modifiers));
	}

	/**
	 * Make the given field accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called when
	 * actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * 
	 * @param field
	 *            the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
					.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * Make the given method accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called when
	 * actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * 
	 * @param method
	 *            the method to make accessible
	 * @see java.lang.reflect.Method#setAccessible
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier
				.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * Make the given constructor accessible, explicitly setting it accessible
	 * if necessary. The {@code setAccessible(true)} method is only called when
	 * actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * 
	 * @param ctor
	 *            the constructor to make accessible
	 * @see java.lang.reflect.Constructor#setAccessible
	 */
	public static void makeAccessible(Constructor<?> ctor) {
		if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor
				.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
			ctor.setAccessible(true);
		}
	}

}