package cn.zhuhongqing.bean.spi;

import java.util.Collection;

import cn.zhuhongqing.util.scan.ClassScan;

public class SPIClassScanBeanFactory extends SPIAbstractBeanFactory {

	private static final ClassScan CLASS_SCAN = ClassScan.INSTANCE;

	public SPIClassScanBeanFactory(String root) {
		super(root);
	}

	@Override
	protected Collection<Class<?>> loadClass(String root) {
		return CLASS_SCAN.getResources(root, SPIClassResourceFilter.INSTANCE);
	}

}
