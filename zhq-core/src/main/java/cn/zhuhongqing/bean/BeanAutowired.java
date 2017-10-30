package cn.zhuhongqing.bean;

public abstract class BeanAutowired {

	public BeanAutowired() {
		BeanFactoryHole.register(getClass());
		BeanFactoryHole.inject(this);
	}

}