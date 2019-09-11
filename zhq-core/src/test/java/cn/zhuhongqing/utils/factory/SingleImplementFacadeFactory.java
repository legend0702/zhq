package cn.zhuhongqing.utils.factory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.ReflectUtils;
import cn.zhuhongqing.util.StringPool;
import cn.zhuhongqing.util.StringUtils;
import cn.zhuhongqing.util.scan.ResourceFilter;
import cn.zhuhongqing.util.scan.ResourceScanManager;

/**
 * 一个简单的门面工具类 <br>
 * 主要用来以一种默认的方式 获取接口的实现(虽然没有限定一定是接口..)<br>
 * 默认追加"impl"包 并在接口类后面追加"Impl"字母 <br>
 * <br>
 * 以java.lang.Object这个类为例 <br>
 * 则会去查询java.lang.impl包下的java.lang.impl.ObjectImpl类 并进行实例化 <br>
 * 
 * @deprecated Use SPI to instead
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>github:github.com/legend0702</li>
 *         </nl>
 */

@Deprecated()
public final class SingleImplementFacadeFactory {

	private SingleImplementFacadeFactory() {
	};

	private static final Logger log = LoggerFactory.getLogger(SingleImplementFacadeFactory.class);

	private static final ConcurrentHashMap<Class<?>, Class<?>> SINGLE_IMPLEMENT_PAIR = new ConcurrentHashMap<Class<?>, Class<?>>();

	private static final String DEFAULT_PACKAGE_SUFFIX = "impl";

	/**
	 * 
	 * @param ifs
	 * 
	 * @param constructorArgs
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public static <I> I getSingleImplement(Class<I> ifs, Object... constructorArgs) {
		Class<?> implClass = SINGLE_IMPLEMENT_PAIR.get(ifs);
		if (implClass == null) {
			implClass = tryToCheckMutliImplAndGetOne(ifs);
			SINGLE_IMPLEMENT_PAIR.put(ifs, implClass);
		}
		try {
			return (I) ReflectUtils.newInstance(implClass, constructorArgs);
		} catch (Exception e) {
			throw new UtilsException("Try newInstance  [" + implClass + "] base on [" + ifs + "] with args: ["
					+ constructorArgs + "] fail!", e);
		}
	}

	/**
	 * Example:<br>
	 * in:java.lang.Object<br>
	 * out:java.lang.impl.* out:java.lang.impl.ObjectImpl
	 */

	static String createImplPackagePattern(Class<?> ifs) {
		StringBuilder sb = new StringBuilder(ifs.getPackage().getName());
		sb.append(StringPool.DOT);
		sb.append(DEFAULT_PACKAGE_SUFFIX);
		sb.append(StringPool.DOT);
		sb.append(StringPool.ASTERISK);
		// sb.append(ifs.getSimpleName());
		// sb.append(DEFAULT_CLASS_SUFFIX);
		return sb.toString();
	}

	/**
	 * 尝试按规则去寻找相应的实现类<br>
	 * 如果出现多个实现类共存的情况 则会以warn的方式进行提醒<br>
	 * 按 {@link Class#forName(String)}的方式返回一个实现类
	 */

	@SuppressWarnings({ "rawtypes" })
	static Class<?> tryToCheckMutliImplAndGetOne(final Class<?> ifs) {
		String packName = createImplPackagePattern(ifs);
		Set<Class> clazs = ResourceScanManager.autoGetResources(StringUtils.replaceDotToSlash(packName), Class.class,
				new ResourceFilter<Class>() {
					@Override
					public boolean accept(Class resource) {
						if (ClassUtils.isOrdinaryAndDiectNewAndAssignable(ifs, resource)) {
							return true;
						}
						return false;
					}
				});
		if (clazs.isEmpty()) {
			throw new UtilsException("Can not find implment class for : [" + ifs.getClass() + "]!");
		}
		Class<?> reClaz = clazs.iterator().next();
		if (clazs.size() > 1) {
			log.warn("Class path contains multiple [" + ifs + "] implments:" + clazs + " So use most nearly one -> "
					+ reClaz);
		}
		return reClaz;
	}

}
