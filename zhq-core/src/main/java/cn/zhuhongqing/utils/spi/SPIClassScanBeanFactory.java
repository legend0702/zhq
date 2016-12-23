package cn.zhuhongqing.utils.spi;

import java.util.Collection;

import cn.zhuhongqing.utils.scan.ResourceFilter;
import cn.zhuhongqing.utils.scan.ResourceScanManager;

@SuppressWarnings("rawtypes")
public class SPIClassScanBeanFactory extends SPIAbstractBeanFactory {

	SPIClassScanBeanFactory(String root) {
		super(root);
	}

	private static final ResourceFilter<Class> CLASS_FILTER = new ClassResourceFilter();

	@Override
	protected Collection<Class> loadClass(String root) {
		return ResourceScanManager.getResourceScan(Class.class).getResources(root, CLASS_FILTER);
	}

	static class ClassResourceFilter implements ResourceFilter<Class> {

		@Override
		public boolean accept(Class resource) {
			return SPIUtil.hasSPI(resource);
		}

	}

}
