package cn.zhuhongqing.bean;

public abstract class BeanAutowired {

	public BeanAutowired() {
		BeanFactoryHole.register(this);
		BeanFactoryHole.BEAN_FACTORY.inject(this);
	}

}