package cn.zhuhongqing.bean.spi;

import java.util.Collection;

import cn.zhuhongqing.bean.SPIUtil;
import cn.zhuhongqing.utils.scan.ClassScan;
import cn.zhuhongqing.utils.scan.ResourceFilter;

public class SPIClassScanBeanFactory extends SPIAbstractBeanFactory {

	private static final ClassScan CLASS_SCAN = new ClassScan();

	public SPIClassScanBeanFactory(String root) {
		super(root);
	}

	private static final ResourceFilter<Class<?>> CLASS_FILTER = new ClassResourceFilter();

	@Override
	protected Collection<Class<?>> loadClass(String root) {
		return CLASS_SCAN.getResources(root, CLASS_FILTER);
	}

	static class ClassResourceFilter implements ResourceFilter<Class<?>> {

		@Override
		public boolean accept(Class<?> resource) {
			return SPIUtil.hasSPI(resource);
		}

	}

}
