package cn.zhuhongqing.bean.spi;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ServiceLoader;

import cn.zhuhongqing.anno.NotNull;
import cn.zhuhongqing.bean.BeanDefinition;
import cn.zhuhongqing.bean.BeanInfoUtils;
import cn.zhuhongqing.bean.BeanInvokable;
import cn.zhuhongqing.bean.BeanProperty;
import cn.zhuhongqing.bean.BeanUtils;
import cn.zhuhongqing.bean.ObjectScope;
import cn.zhuhongqing.bean.ObjectState;
import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.GeneralUtils;
import cn.zhuhongqing.util.ReflectUtils;
import cn.zhuhongqing.util.meta.MetaData;

/**
 * Utils for SPI.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

@SPI
public class SPIUtil {

	private static SPI DEF_SPI = SPIUtil.class.getAnnotation(SPI.class);

	public static <S> S load(Class<S> ifs) {
		return load(ifs, ClassUtils.getDefaultClassLoader());
	}

	public static <S> S load(Class<S> ifs, ClassLoader classLoader) {
		Iterator<S> itr = ServiceLoader.load(ifs, classLoader).iterator();
		while (itr.hasNext()) {
			return itr.next();
		}
		throw new UtilsException("Can't find type of " + ifs + "'s impls.");
	}

	/**
	 * 将{@link Class}解析为{@link BeanDefinition} 并设置{@link ObjectState}相关属性
	 */

	public static BeanDefinition classToBeanDefinition(Class<?> pureClass) {
		BeanDefinition define = BeanDefinition.of(pureClass);
		initObjectState(define);
		return define;
	}

	/**
	 * 解析{@link BeanDefinition}内部的成员变量 并变量名称作为Key 存放到该Bean的hole中<br/>
	 */

	public static BeanDefinition initBeanDefinition(BeanDefinition define) {
		Class<?> pureClass = define.getMetaType();
		// 解析其中需要注入的成员变量
		PropertyDescriptor[] beanPropArr = BeanInfoUtils.getPropertyDescriptors(pureClass);
		for (PropertyDescriptor prop : beanPropArr) {
			// 不能写的属性就过滤啦
			// if(GeneralUtils.isNull(prop.getWriteMethod()))
			// continue;
			// 只管理真实属性
			Field propField = ReflectUtils.getSupportedField(pureClass, prop.getName());
			if (GeneralUtils.isNull(propField))
				continue;
			SPI propSpi = getSPI(propField);
			// 无注解不管理
			if (GeneralUtils.isNull(propSpi))
				continue;
			define.setAttr(prop.getName(), initObjectState(BeanProperty.of(propField)));
		}
		return define;
	}

	/**
	 * 获取构造对象
	 * 
	 * <pre>
	 * 1.判断是否已经存在 存在则直接返回
	 * 2.如果只有一个构造函数 则直接使用该函数 
	 * 3.尝试获取符合参数中的group的构造函数 如果出现重复的group 则抛出异常 需要用户自己调整group
	 * 3.1 如不存在 则尝试获取公共无参构造函数  这里要注意一点 因为之前已经用group去取过了 但是肯定没有取道 所以要判断一下获取到的无参构造是否不带注解
	 * 4.解析参数类型
	 * 5.将构造对象保存起来以便后续再度使用
	 * 6.返回该对象
	 * </pre>
	 */

	public static BeanInvokable getConstructor(BeanDefinition define) {
		BeanProperty beanProp = define.getAttr(define.getGroup());
		if (GeneralUtils.isNotNull(beanProp)) {
			if (BeanInvokable.class.isAssignableFrom(beanProp.getClass())) {
				return (BeanInvokable) beanProp;
			}
			throw new IllegalStateException(
					"The class [" + define.getMetaType() + "] has same words for group and field name ["
							+ define.getGroup() + "],change one for distinguish.");
		}
		Class<?> clazz = define.getMetaType();
		Constructor<?> useCon = null;
		Constructor<?>[] cons = clazz.getConstructors();
		if (cons.length == 1) {
			useCon = cons[0];
		} else {
			HashMap<String, Constructor<?>> conHole = new HashMap<>();
			for (Constructor<?> con : cons) {
				SPI spi = getSPI(con);
				if (GeneralUtils.isNull(spi))
					continue;
				String cGroup = spi.value();
				if (conHole.containsKey(cGroup)) {
					BeanUtils.throwDupConstructor(clazz, cGroup);
				}
				conHole.put(cGroup, con);
			}
			useCon = conHole.get(define.getGroup());
		}

		if (GeneralUtils.isNull(useCon)) {
			useCon = ReflectUtils.getNoParamAndUsableConstructor(clazz);
			if (GeneralUtils.isNull(useCon) || hasSPI(useCon)) {
				BeanUtils.throwNoConstructor(clazz, define.getGroup());
			}
		}

		BeanInvokable beanCon = BeanInvokable.of(useCon);
		initObjectState(beanCon);
		Parameter[] params = beanCon.getParameters();
		for (int i = 0; i < beanCon.getParameterCount(); i++) {
			BeanProperty param = initObjectState(BeanProperty.of(params[i]));
			checkAndThrowRefSelf(param, define);
			beanCon.setAttr(i, param);
		}
		define.setAttr(define.getGroup(), beanCon);
		return beanCon;
	}

	/**
	 * 获取class上的注解 如果没有 则用默认值{@link #DEF_SPI}<br/>
	 * 
	 * 如果{@link ObjectState#getScope()}是{@link ObjectScope#DEFAULT}
	 * 则设置为{@link ObjectScope#SINGLETON}
	 */

	public static BeanDefinition initObjectState(BeanDefinition define) {
		setObjectState(define, define);
		if (define.isDefaultScope()) {
			define.setScope(ObjectScope.SINGLETON);
		}
		return define;
	}

	/**
	 * 获取属性上的注解 如果没有 则用默认值{@link #DEF_SPI}<br/>
	 * 
	 * 如果{@link ObjectState#getScope()}是{@link ObjectScope#DEFAULT}
	 * 则设置为{@link BeanDefinition#getScope()}上的一致
	 */

	public static BeanProperty initObjectState(BeanProperty prop) {
		BeanDefinition define = classToBeanDefinition(prop.getMetaType());
		setObjectState(prop, prop);
		if (prop.isDefaultScope()) {
			prop.setScope(define.getScope());
		}
		if (prop.isDefaultGroup()) {
			prop.setGroup(define.getGroup());
		}
		return prop;
	}

	/**
	 * 获取element上的{@link SPI}属性 赋值到state上
	 */

	static <T extends ObjectState> T setObjectState(@NotNull T state, @NotNull AnnotatedElement element) {
		SPI spi = getSPI(element);
		if (GeneralUtils.isNull(spi)) {
			spi = DEF_SPI;
		}
		setObjectState0(state, spi);
		return state;
	}

	/**
	 * 将spi的值赋到state里
	 */

	static void setObjectState0(ObjectState state, SPI spi) {
		if (!GeneralUtils.hasNull(state, spi)) {
			state.setGroup(spi.value());
			state.setScope(spi.scope());
		}
	}

	public static String getGroup(Class<?> clazz) {
		SPI spi = getSPI(clazz);
		if (GeneralUtils.isNotNull(spi)) {
			return spi.value();
		}
		return null;
	}

	public static SPI getSPI(AnnotatedElement element) {
		return GeneralUtils.isNull(element) ? null : element.getAnnotation(SPI.class);
	}

	public static boolean hasSPI(AnnotatedElement element) {
		return GeneralUtils.isNotNull(getSPI(element));
	}

	// throw

	public static <O extends ObjectState & MetaData, T extends ObjectState & MetaData> void checkAndThrowRefSelf(O o,
			T t) {
		if (o.getMetaType().isAssignableFrom((t.getMetaType())))
			if (o.getGroup().equals(t.getGroup()))
				throw new IllegalStateException(
						"The Class " + t.getMetaType() + " has ref-self on same group [" + t.getGroup() + "].");

	}

}
