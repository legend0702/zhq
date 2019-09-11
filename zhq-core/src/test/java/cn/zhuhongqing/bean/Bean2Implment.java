package cn.zhuhongqing.bean;

import cn.zhuhongqing.bean.spi.SPI;

@SPI("Bean")
public class Bean2Implment implements BeanInterface {

	private BeanInterface ifs;

	@SPI
	private BeanInterface beanInterface;

	public Bean2Implment(@SPI BeanInterface ifs) {
		this.ifs = ifs;
	}

	@Override
	public BeanTest create(String str) {
		return new BeanTest(str + "2");
	}

	/**
	 * @return the beanInterface
	 */
	public BeanInterface getBeanInterface() {
		return beanInterface;
	}

	/**
	 * @param beanInterface
	 *            the beanInterface to set
	 */
	public void setBeanInterface(BeanInterface beanInterface) {
		this.beanInterface = beanInterface;
	}

	@Override
	public BeanInterface get() {
		System.out.println(beanInterface + ":" + beanInterface.create("Hello SPI Inside!").getName());
		return ifs;
	}

}
