package cn.zhuhongqing.bean.spi;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import cn.zhuhongqing.io.ReadLineIterator;
import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.URLUtil;
import cn.zhuhongqing.utils.scan.ResourceScanManager;

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
		InputStream is = URLUtil.getInputSteam(url);
		if (GeneralUtil.isNull(is)) {
			return Collections.emptyList();
		}
		String className = URLUtil.getLastPath(url);
		Class<?> clazz = ClassUtil.forName(className);
		if (GeneralUtil.isNull(clazz)) {
			GeneralUtil.throwClassNotFound(className);
			// logger.warn("Can't load class by className:"+className);
			// return Collections.emptyList();
		}
		List<Class<?>> clzList = new ArrayList<>();
		clzList.add(clazz);

		ReadLineIterator lineIter = new ReadLineIterator(is);
		while (lineIter.hasNext()) {
			String line = lineIter.nextLine();
			if (StringUtil.isEmpty(line))
				continue;
			Class<?> implClazz = ClassUtil.forName(line);
			if (GeneralUtil.isNull(implClazz)) {
				GeneralUtil.throwClassNotFound(line);
			}
			if (!ClassUtil.isAssignable(clazz, implClazz)) {
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
