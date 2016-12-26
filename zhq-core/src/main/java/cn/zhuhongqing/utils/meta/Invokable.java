package cn.zhuhongqing.utils.meta;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import cn.zhuhongqing.bean.DefaultBeanConstructor;
import cn.zhuhongqing.bean.DefaultBeanMethod;

/**
 * 可执行对象 整合{@link MetaData}
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public interface Invokable extends MetaData, GenericDeclaration {

	static Invokable of(Method method) {
		return new DefaultBeanMethod(method);
	}

	static Invokable of(Constructor<?> constructor) {
		return new DefaultBeanConstructor(constructor);
	}

	Executable getExecutor();

	@Override
	default Member getMember() {
		return getExecutor();
	}

	Object invoke(Object obj, Object... args);

	/**
	 * @see Executable#toGenericString()
	 */
	default String toGenericString() {
		return getExecutor().toGenericString();
	}

	// invoke

	/**
	 * @see Executable#isAccessible()
	 */

	default boolean isAccessible() {
		return getExecutor().isAccessible();
	}

	// Parameter

	/**
	 * @see Executable#getParameterTypes()
	 */
	default Class<?>[] getParameterTypes() {
		return getExecutor().getParameterTypes();
	}

	/**
	 * @see Executable#getParameterCount()
	 */
	default int getParameterCount() {
		return getExecutor().getParameterCount();
	}

	/**
	 * @see Executable#getParameterCount()
	 */
	default boolean hasParameter() {
		return getParameterCount() != 0;
	}

	/**
	 * @see Executable#getGenericParameterTypes()
	 */
	default Type[] getGenericParameterTypes() {
		return getExecutor().getGenericParameterTypes();
	}

	/**
	 * @see Executable#isVarArgs()
	 */
	default boolean isVarArgs() {
		return getExecutor().isVarArgs();
	}

	/**
	 * @see Executable#getParameters()
	 */
	default Parameter[] getParameters() {
		return getExecutor().getParameters();
	}

	// Exception

	/**
	 * @see Executable#getExceptionTypes()
	 */

	default Class<?>[] getExceptionTypes() {
		return getExecutor().getExceptionTypes();
	}

	/**
	 * @see Executable#getGenericExceptionTypes()
	 */
	default Type[] getGenericExceptionTypes() {
		return getExecutor().getGenericExceptionTypes();
	}

	// Annotation

	/**
	 * @see Executable#getAnnotatedReturnType()
	 */
	default AnnotatedType getAnnotatedReturnType() {
		return getExecutor().getAnnotatedReturnType();
	}

	/**
	 * @see Executable#getAnnotatedReceiverType()
	 */

	default AnnotatedType getAnnotatedReceiverType() {
		return getExecutor().getAnnotatedReceiverType();
	}

	/**
	 * @see Executable#getAnnotatedParameterTypes()
	 */

	default AnnotatedType[] getAnnotatedParameterTypes() {
		return getExecutor().getAnnotatedParameterTypes();
	}

	/**
	 * @see Executable#getAnnotatedExceptionTypes()
	 */

	default AnnotatedType[] getAnnotatedExceptionTypes() {
		return getExecutor().getAnnotatedExceptionTypes();
	}

	// GenericDeclaration

	/**
	 * @see Executable#getTypeParameters()
	 * @see GenericDeclaration#getTypeParameters()
	 */
	default TypeVariable<?>[] getTypeParameters() {
		return getExecutor().getTypeParameters();
	}
}
