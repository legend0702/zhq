package cn.zhuhongqing.utils.spi;

import java.util.List;

import cn.zhuhongqing.utils.bean.BeanFactory;

/**
 * 同一个JVM可以有多个BeanFactory 他们共享注册信息
 *
 */

public class SPIBeanFactory {

	public static class Builder {
		private static SPIDefaultBeanFactory DEFAULT = new SPIDefaultBeanFactory();

		public static BeanFactory Build() {
			return DEFAULT;
		}

		public static BeanFactory Build(String root) {
			return new SPIDefaultBeanFactory(root);
		}

		public static BeanFactory Build(String[] roots) {
			return new SPIDefaultBeanFactory(roots);
		}

		public static BeanFactory Build(List<String> roots) {
			return new SPIDefaultBeanFactory(roots);
		}

		public static BeanFactory BuildByBasePackage(String root) {
			return new SPIClassScanBeanFactory(root);
		}
	}

}
