package cn.zhuhongqing.bean;

import cn.zhuhongqing.utils.spi.SPI;

@SPI("A")
public interface BeanInterface {

	BeanTest create(String str);

	default BeanInterface get() {
		return this;
	}
}