package cn.zhuhongqing.utils.bean;

import cn.zhuhongqing.utils.spi.SPIUtil;

public interface BeanAutowired {

	default void inject(Object bean) {
	}

	default void autowired() {
		BeanAutowired factory = SPIUtil.load(BeanAutowired.class);
		factory.inject(this);
	}

}