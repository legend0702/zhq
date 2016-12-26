package cn.zhuhongqing.bean;

public abstract class BeanAutowired {

	private static BeanFactory factory = SPIUtil.load(BeanFactory.class);

	public BeanAutowired() {
		factory.inject(this);
	}

}