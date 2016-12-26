package cn.zhuhongqing.bean;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class BeanAutowiredTest extends BeanAutowired {

	@SPI
	private BeanInterface beanInterface;

	public BeanInterface getBeanInterface() {
		return beanInterface;
	}

	public void setBeanInterface(BeanInterface beanInterface) {
		this.beanInterface = beanInterface;
	}

	@Test
	public void autowired() throws IOException, URISyntaxException {
		BeanAutowiredTest bean = new BeanAutowiredTest();
		System.out.println(bean.getBeanInterface());
	}
}
