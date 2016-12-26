package cn.zhuhongqing.bean;

@SPI("A")
public class BeanImplment implements BeanInterface {

	@Override
	public BeanTest create(String str) {
		return new BeanTest(str);
	}

}
