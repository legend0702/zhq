package cn.zhuhongqing.anno;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import cn.zhuhongqing.bean.BeanUtils;
import cn.zhuhongqing.util.ArraysUtils;
import cn.zhuhongqing.util.GeneralUtils;
import cn.zhuhongqing.util.MethodUtils;
import cn.zhuhongqing.util.ReflectUtils;
import cn.zhuhongqing.util.meta.Invokable;
import cn.zhuhongqing.util.meta.MetaData;

public class AnnotationUtils {

	public static String DEFALUT_METHOD_NAME = "value";

	/**
	 * Use {@link #extend(Object, AnnotationWrap)} instead of.
	 */
	@Deprecated
	public static <T> T extend(T bean, Annotation anno) {
		Method[] methods = ReflectUtils.getAccessibleMethods(anno.annotationType());
		for (Method method : methods) {
			Invokable invoker = Invokable.of(method);
			String name = getFieldName(invoker, bean.getClass());
			Object value = invoker.invoke(anno);
			BeanUtils.setProperty(bean, name, value);
		}
		return bean;
	}

	public static <T> T extend(T bean, AnnotationWrap wrap) {
		Method[] methods = ReflectUtils.getAccessibleMethods(wrap.getAnnotationType());
		for (Method method : methods) {
			Invokable invoker = Invokable.of(method);
			String name = getFieldName(invoker, bean.getClass());
			Object value = getMethodValue(wrap.getMetaData(), invoker, wrap.getAnnotation());
			BeanUtils.setProperty(bean, name, value);
		}
		return bean;
	}

	public static void parserSingleAnnoAndExtend(Object bean, Class<?> fromClass,
			Class<? extends Annotation> annoClass) {
		AnnotationHole annoHole = getAnnotations(fromClass, annoClass);
		if (annoHole.isEmpty() || !annoHole.isSingleType())
			return;
		for (AnnotationWrap wrap : annoHole.getAnnotationWraps()) {
			extend(bean, wrap);
			break;
		}
	}

	public static <T> Collection<T> parser(Class<T> beanClass, Class<?> fromClass, Class<? extends Annotation> annoClass) {
		ArrayList<T> beanList = new ArrayList<>();
		AnnotationHole annoHole = getAnnotations(fromClass, annoClass);
		for (AnnotationWrap wrap : annoHole.getAnnotationWraps()) {
			beanList.add(AnnotationUtils.extend(ReflectUtils.newInstanceWithoutArgs(beanClass), wrap));
		}
		return beanList;
	}

	public static Object getMethodValue(MetaData metaData, Invokable invoker, Annotation anno) {
		Object value = invoker.invoke(anno);
		Object def = invoker.getDefaultValue();
		if (GeneralUtils.isNull(def))
			return value;
		if (def.equals(value)) {
			def = getDefaltValue(metaData, invoker);
		} else {
			return value;
		}
		if (GeneralUtils.isNotNull(def))
			return def;
		return value;

	}

	static Object getDefaltValue(MetaData meta, Invokable invoker) {
		DefBy def = invoker.getAnnotation(DefBy.class);
		if (GeneralUtils.isNull(def))
			return null;
		return def.value().getDefValue(meta);
	}

	public static String getFieldName(Invokable invoker, Class<?> forClass) {
		ValueFor valueFor = getValueFor(invoker, forClass);
		return GeneralUtils.isNull(valueFor) ? invoker.getName() : valueFor.value();
	}

	public static ValueFor getValueFor(Invokable invoker) {
		return getValueFor(invoker, null);
	}

	public static ValueFor getValueFor(Invokable invoker, Class<?> forClass) {
		ValueFor[] valueFors = invoker.getAnnotationsByType(ValueFor.class);
		if (ArraysUtils.isEmpty(valueFors))
			return null;
		if (GeneralUtils.isNull(forClass)) {
			forClass = Object.class;
		}
		for (ValueFor valueFor : valueFors) {
			if (valueFor.forClass().isAssignableFrom(forClass))
				return valueFor;
		}
		return null;
	}

	public static AnnotationHole getAnnotations(Class<?> clazz) {
		MetaData classMeta = MetaData.of(clazz);
		AnnotationHole hole = AnnotationHole.of(classMeta, classMeta.getAnnotations());

		for (Field field : ReflectUtils.getSupportedFields(clazz)) {
			getAnnotations(hole, MetaData.of(field));
		}
		for (Method method : ReflectUtils.getSupportedMethods(clazz)) {
			getAnnotations(hole, MetaData.of(method));
		}

		return hole;
	}

	public static AnnotationHole getAnnotations(Class<?> clazz, Class<? extends Annotation> annoClass) {
		MetaData classMeta = MetaData.of(clazz);
		AnnotationHole hole = AnnotationHole.of(classMeta, classMeta.getAnnotationsByType(annoClass));

		for (Field field : ReflectUtils.getSupportedFields(clazz)) {
			getAnnotations(hole, MetaData.of(field), annoClass);
		}
		for (Method method : ReflectUtils.getSupportedMethods(clazz)) {
			getAnnotations(hole, MetaData.of(method), annoClass);
		}

		return hole;
	}

	static AnnotationHole getAnnotations(AnnotationHole hole, MetaData meta, Class<? extends Annotation> annoClass) {
		return hole.add(meta, meta.getAnnotationsByType(annoClass));
	}

	static AnnotationHole getAnnotations(AnnotationHole hole, MetaData meta) {
		return hole.add(meta, meta.getAnnotations());
	}

	public static boolean isRepeatable(Class<? extends Annotation> annoClass) {
		Method method = ReflectUtils.findMethod(annoClass, DEFALUT_METHOD_NAME);
		if (GeneralUtils.isNull(method))
			return false;
		Class<?> rType = method.getReturnType();
		if (GeneralUtils.isNull(rType) || !rType.isArray())
			return false;
		Class<?> subClass = rType.getComponentType();
		Repeatable able = subClass.getAnnotation(Repeatable.class);
		if (GeneralUtils.isNull(able))
			return false;
		if (annoClass.equals(able.value()))
			return true;
		return false;
	}

	public static Annotation[] getRepeatableValues(Annotation anno) {
		Method method = ReflectUtils.findMethod(anno.annotationType(), DEFALUT_METHOD_NAME);
		return (Annotation[]) MethodUtils.invoke(method, anno);
	}

	public static ElementType getElementType(MetaData meta) {
		if (meta.isConstructor())
			return ElementType.CONSTRUCTOR;
		if (meta.isMethod())
			return ElementType.METHOD;
		if (meta.isField())
			return ElementType.FIELD;
		if (meta.isAnnotation())
			return ElementType.ANNOTATION_TYPE;
		if (meta.isParameter())
			return ElementType.PARAMETER;
		return ElementType.TYPE;
	}

}