package cn.zhuhongqing.utils.factory;

import java.io.File;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.ReflectUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.scan.FileScan;

/**
 * 一个简单的门面工具类 <br>
 * 主要用来以一种默认的方式 获取接口的实现(虽然没有限定一定是接口..)<br>
 * 默认追加"impl"包 并在接口类后面追加"Impl"字母 <br>
 * <br>
 * 以java.lang.Object这个类为例 <br>
 * 则会去查询java.lang.impl包下的java.lang.impl.ObjectImpl类 并进行实例化 <br>
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>github:github.com/legend0702</li>
 *         </nl>
 */

public final class SingleImplementFacadeFactory {

	private SingleImplementFacadeFactory() {
	};

	private static final Logger log = LoggerFactory
			.getLogger(SingleImplementFacadeFactory.class);

	private static final ConcurrentHashMap<Class<?>, Class<?>> SINGLE_IMPLEMENT_PAIR = new ConcurrentHashMap<Class<?>, Class<?>>();

	private static final FileScan FILE_SCAN = new FileScan();

	private static final String DEFAULT_PACKAGE_SUFFIX = "impl";
	private static final String DEFAULT_CLASS_SUFFIX = "Impl";

	/**
	 * 
	 * @param ifs
	 * 
	 * @param constructorArgs
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public static <I> I getSingleImplement(Class<I> ifs,
			Object... constructorArgs) {
		Class<?> implClass = SINGLE_IMPLEMENT_PAIR.get(ifs);
		if (implClass == null) {
			implClass = tryToCheckMutliImplAndGetOne(ifs);
			SINGLE_IMPLEMENT_PAIR.put(ifs, implClass);
		}
		try {
			return (I) ReflectUtil.newInstance(implClass, constructorArgs);
		} catch (Exception e) {
			throw new UtilsException("Try newInstance  [" + implClass
					+ "] base on [" + ifs + "] with args: [" + constructorArgs
					+ "] fail!", e);
		}
	}

	/**
	 * Example:<br>
	 * in:java.lang.Object<br>
	 * out:java.lang.impl.ObjectImpl
	 */

	static String createImplementClassName(Class<?> ifs) {
		StringBuilder sb = new StringBuilder(ifs.getPackage().getName());
		sb.append(StringPool.DOT);
		sb.append(DEFAULT_PACKAGE_SUFFIX);
		sb.append(StringPool.DOT);
		sb.append(ifs.getSimpleName());
		sb.append(DEFAULT_CLASS_SUFFIX);
		return sb.toString();
	}

	/**
	 * 尝试按规则去寻找相应的实现类<br>
	 * 如果出现多个实现类共存的情况 则会以warn的方式进行提醒<br>
	 * 按 {@link Class#forName(String)}的方式返回一个实现类
	 */

	static Class<?> tryToCheckMutliImplAndGetOne(Class<?> ifs) {
		String implClassName = createImplementClassName(ifs);
		Set<File> files = FILE_SCAN.getResources(ClassUtil
				.classPathToFilePath(implClassName));
		if (files.isEmpty()) {
			throw new UtilsException("Can not find class: [" + implClassName
					+ "]!");
		}
		Class<?> implClass = ClassUtil.forName(implClassName);
		if (files.size() > 1) {
			log.warn("Class path contains multiple [" + ifs + "] implments:"
					+ files
					+ " So use most nearly one,but I don't know which one :(");
		}
		return implClass;
	}

	public static void main(String[] args) {
		File file = new File("E:/music/Human-christina perri.mp3");
		System.out.println(file.getPath());
	}
}
