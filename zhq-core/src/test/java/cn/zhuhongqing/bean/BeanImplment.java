package cn.zhuhongqing.bean;

import cn.zhuhongqing.bean.spi.SPI;

@SPI("A")
public class BeanImplment implements BeanInterface {

	@Override
	public BeanTest create(String str) {
		return new BeanTest(str);
	}

}
