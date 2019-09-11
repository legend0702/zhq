package cn.zhuhongqing.util.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import cn.zhuhongqing.bean.BeanProperty;

/**
 * 将Java内部的Member跟AnnotatedElement整合一下 并再扩展一些方法<br/>
 * 实现类必须实现(覆盖)以下方法：<br/>
 * <br/>
 * 
 * {@link #getMetaType()}<br/>
 * {@link #getMember()}<br/>
 * {@link #getDeclaringClass()}<br/>
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public interface MetaData extends AnnotatedElement, Member {

	// static of

	static MetaData of(Class<?> clazz) {
		return ClassMeta.of(clazz);
	}

	static MetaData of(Constructor<?> con) {
		return Invokable.of(con);
	}

	static MetaData of(Field field) {
		return BeanProperty.of(field);
	}

	static MetaData of(Method method) {
		return Invokable.of(method);
	}

	static MetaData of(Parameter param) {
		return ParameterMeta.of(param);
	}

	// Meta

	Class<?> getMetaType();

	/**
	 * 默认是{@link Class}元数据
	 */

	default Member getMember() {
		return ClassMeta.of(getDeclaringClass());
	}

	// Member

	default Class<?> getDeclaringClass() {
		return getMember().getDeclaringClass();
	}

	default String getName() {
		return getMember().getName();
	}

	default int getModifiers() {
		return getMember().getModifiers();
	}

	default boolean isSynthetic() {
		return getMember().isSynthetic();
	}

	// AnnotatedElement

	default AnnotatedElement getAnnotatedElement() {
		return getMetaType();
	}

	default <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return getAnnotatedElement().getAnnotation(annotationClass);
	}

	default Annotation[] getAnnotations() {
		return getAnnotatedElement().getAnnotations();
	}

	default Annotation[] getDeclaredAnnotations() {
		return getAnnotatedElement().getDeclaredAnnotations();
	}

	// Type is

	default boolean isClass() {
		return ClassMeta.class.isAssignableFrom(getMember().getClass());
	}

	default boolean isEnum() {
		return Enum.class.isAssignableFrom(getMetaType());
	}

	default boolean isAnnotation() {
		return Annotation.class.isAssignableFrom(getMetaType());
	}

	default boolean isConstructor() {
		return Constructor.class.isAssignableFrom(getMember().getClass());
	}

	default boolean isField() {
		return Field.class.isAssignableFrom(getMember().getClass());
	}

	default boolean isMethod() {
		return Method.class.isAssignableFrom(getMember().getClass());
	}

	default boolean isParameter() {
		return ParameterMember.class.isAssignableFrom(getMember().getClass());
	}

	// Modifier

	/**
	 * @see Modifier#isPublic(int)
	 */
	default boolean isPublic() {
		return Modifier.isPublic(getModifiers());
	}

	/**
	 * @see Modifier#isProtected(int)
	 */
	default boolean isProtected() {
		return Modifier.isProtected(getModifiers());
	}

	/**
	 * @see Modifier#isPrivate(int)
	 */
	default boolean isPrivate() {
		return Modifier.isPrivate(getModifiers());
	}

	/**
	 * @see Modifier#isStatic(int)
	 */
	default boolean isStatic() {
		return Modifier.isStatic(getModifiers());
	}

	/**
	 * @see Modifier#isFinal(int)
	 */
	default boolean isFinal() {
		return Modifier.isFinal(getModifiers());
	}

	/**
	 * @see Modifier#isSynchronized(int)
	 */
	default boolean isSynchronized() {
		return Modifier.isSynchronized(getModifiers());
	}

	/**
	 * @see Modifier#isVolatile(int)
	 */
	default boolean isVolatile() {
		return Modifier.isVolatile(getModifiers());
	}

	/**
	 * @see Modifier#isTransient(int)
	 */
	default boolean isTransient() {
		return Modifier.isTransient(getModifiers());
	}

	/**
	 * @see Modifier#isNative(int)
	 */
	default boolean isNative() {
		return Modifier.isNative(getModifiers());
	}

	/**
	 * @see Modifier#isInterface(int)
	 */
	default boolean isInterface() {
		return Modifier.isInterface(getModifiers());
	}

	/**
	 * @see Modifier#isAbstract(int)
	 */
	default boolean isAbstract() {
		return Modifier.isAbstract(getModifiers());
	}

	/**
	 * @see Modifier#isStrict(int)
	 */

	default boolean isStrict() {
		return Modifier.isStrict(getModifiers());
	}

	default boolean isPublicStaticFinal() {
		return (isPublic() && isStatic() && isFinal());
	}

	default boolean isOverWriteAble() {
		return !(isFinal() || isNative());
	}

	default boolean isInvokable() {
		return (isMethod() || isConstructor());
	}

}