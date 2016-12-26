package cn.zhuhongqing.bean;

@SPI("A")
public interface BeanInterface {

	BeanTest create(String str);

	default BeanInterface get() {
		return this;
	}
}