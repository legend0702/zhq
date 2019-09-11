package cn.zhuhongqing.bean;

import cn.zhuhongqing.bean.spi.SPI;

@SPI("A")
public interface BeanInterface {

	BeanTest create(String str);

	default BeanInterface get() {
		return this;
	}
}