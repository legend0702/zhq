package cn.zhuhongqing.utils.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import cn.zhuhongqing.bean.BeanProperty;

/**
 * 将Java内部的Member跟AnnotatedElement整合一下<br/>
 * 实现类必须实现以及覆盖以下方法：<br/>
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

	// static

	static MetaData of(Class<?> clazz) {
		return ClassMeta.of(clazz);
	}

	static MetaData of(Constructor<?> con) {
		return BeanProperty.of(con);
	}

	static MetaData of(Field field) {
		return BeanProperty.of(field);
	}

	static MetaData of(Method method) {
		return BeanProperty.of(method);
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

	// Is

	default boolean isClass() {
		return ClassMeta.class.isAssignableFrom(getMember().getClass());
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

}