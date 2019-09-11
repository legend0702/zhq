package cn.zhuhongqing.bean.spi;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import cn.zhuhongqing.io.ReadLineIterator;
import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.GeneralUtils;
import cn.zhuhongqing.util.StringUtils;
import cn.zhuhongqing.util.URLUtils;
import cn.zhuhongqing.util.scan.ResourceScanManager;

public class SPIDefaultBeanFactory extends SPIAbstractBeanFactory {

	public SPIDefaultBeanFactory() {
		super(SERVICES_DIRECTORY);
	}

	public SPIDefaultBeanFactory(List<String> roots) {
		super(roots);
	}

	public SPIDefaultBeanFactory(String root) {
		super(root);
	}

	public SPIDefaultBeanFactory(String[] roots) {
		super(roots);
	}

	@Override
	protected List<Class<?>> loadClass(String root) {
		List<Class<?>> clzList = new ArrayList<>();
		Set<URL> urls = ResourceScanManager.autoGetResources(root, URL.class);
		if (urls.isEmpty()) {
			return clzList;
		}
		for (URL url : urls) {
			clzList.addAll(loadClass(url));
		}
		return clzList;
	}

	/**
	 * 从文件读取类信息
	 */

	protected List<Class<?>> loadClass(URL url) {
		InputStream is = URLUtils.getInputSteam(url);
		if (GeneralUtils.isNull(is)) {
			return Collections.emptyList();
		}
		String className = URLUtils.getLastPath(url);
		Class<?> clazz = ClassUtils.forName(className);
		if (GeneralUtils.isNull(clazz)) {
			GeneralUtils.throwClassNotFound(className);
			// logger.warn("Can't load class by className:"+className);
			// return Collections.emptyList();
		}
		List<Class<?>> clzList = new ArrayList<>();
		clzList.add(clazz);

		ReadLineIterator lineIter = new ReadLineIterator(is);
		while (lineIter.hasNext()) {
			String line = lineIter.nextLine();
			if (StringUtils.isEmpty(line))
				continue;
			Class<?> implClazz = ClassUtils.forName(line);
			if (GeneralUtils.isNull(implClazz)) {
				GeneralUtils.throwClassNotFound(line);
			}
			if (!ClassUtils.isAssignable(clazz, implClazz)) {
				throw new IllegalStateException("Error when load class(interface: " + clazz + ", class line: "
						+ implClazz + "), class " + implClazz + " is not subtype of interface.");
			}
			clzList.add(implClazz);
		}
		if (clzList.isEmpty()) {
			throw new IllegalStateException(
					"No such implement class for the class [" + clazz.getName() + "] in the config: " + url.getPath());
		}
		return clzList;
	}

}
