package cn.zhuhongqing.utils.spi;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;

import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.bean.BeanDefinition;
import cn.zhuhongqing.utils.bean.ObjectState;

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

public class SPIUtil {

	public static <S> S load(Class<S> ifs) {
		return load(ifs, ClassUtil.getDefaultClassLoader());
	}

	public static <S> S load(Class<S> ifs, ClassLoader classLoader) {
		Iterator<S> itr = ServiceLoader.load(ifs, classLoader).iterator();
		int count = 0;
		while (itr.hasNext()) {
			return itr.next();
		}
		if (count == 0) {
			throw new UtilsException("Can't find type of " + ifs + "'s impls.");
		}
		// never do it
		return null;
	}

	public static ObjectState setObjectState(ObjectState state, AnnotatedElement element) {
		SPI spi = getSPI(element);
		if (!GeneralUtil.hasNull(state, spi)) {
			state.setGroup(spi.value());
			state.setScope(spi.scope());
		}
		return state;
	}

	public static ObjectState setObjectState(ObjectState state, SPI spi) {
		if (!GeneralUtil.hasNull(state, spi)) {
			state.setGroup(spi.value());
			state.setScope(spi.scope());
		}
		return state;
	}

	public static String getClassNameForGroup(Class<?> clazz) {
		return clazz.getSimpleName();
	}

	public static String getClassNameForGroup(BeanDefinition define) {
		return getClassNameForGroup(define.getMetaClass());
	}

	public static String getGroup(String group) {
		return GeneralUtil.isNull(group) ? StringPool.DEFAULT : group;
	}

	public static String getGroup(Class<?> clazz) {
		SPI spi = getSPI(clazz);
		if (GeneralUtil.isNull(spi)) {
			return null;
		}
		return spi.value();
	}

	public static boolean isDefGroup(String group) {
		return StringPool.DEFAULT.equals(group);
	}

	public static SPI getSPI(AnnotatedElement element) {
		return GeneralUtil.isNull(element) ? null : element.getAnnotation(SPI.class);
	}

	public static boolean hasSPI(AnnotatedElement element) {
		return GeneralUtil.isNotNull(getSPI(element));
	}

	public static void throwNoBeanFind(Class<?> clazz) {
		throw new IllegalArgumentException(
				"Can't find BeanDefinition for class:" + clazz + ",maybe no interface or implement registered.");
	}

	public static void throwNoBeanFind(Class<?> clazz, String group) {
		throw new IllegalArgumentException("Can't find BeanDefinition for class:" + clazz + " by groupName:" + group
				+ ",maybe no implement registered.");
	}

	public static void throwNoGroupFind(Class<?> clazz, String group) {
		throw new IllegalArgumentException("Can't find group information [" + group + "] or on the class:" + clazz
				+ ",maybe no SPI Annotation on the class.");
	}

	public static void throwDupGroup(Class<?> ifsClass, String group, Class<?>... dupClass) {
		throw new IllegalStateException("The class [ " + ifsClass + " ] has more than one implement class "
				+ Arrays.toString(dupClass) + " on same group [" + group + "].");
	}

	public static UserParamState buildParamState(Class<?> clazz, String group) {
		return new UserParamState(clazz, group);
	}

	public static class UserParamState {
		private boolean userGroup = true;;
		private String group;

		public UserParamState(Class<?> clazz, String group1) {
			group = group1;
			if (GeneralUtil.isNull(group)) {
				userGroup = false;
				group = SPIUtil.getGroup(clazz);
				if (GeneralUtil.isNull(group)) {
					// 既没有传入参数 也没有注解
					// SPIUtil.throwNoGroupFind(clazz, group1);
					group = StringPool.DEFAULT;
				}
			}
		}

		public boolean isUserGroup() {
			return userGroup;
		}

		public String getGroup() {
			return group;
		}

	}

}
