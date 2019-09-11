package cn.zhuhongqing.bean.spi;

import cn.zhuhongqing.util.scan.ResourceFilter;

public class SPIClassResourceFilter implements ResourceFilter<Class<?>> {

	public static final SPIClassResourceFilter INSTANCE = new SPIClassResourceFilter();

	private SPIClassResourceFilter() {
	}

	@Override
	public boolean accept(Class<?> resource) {
		return SPIUtil.hasSPI(resource);
	}

}
