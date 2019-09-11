package cn.zhuhongqing.bean;

import java.util.Collection;

import cn.zhuhongqing.bean.spi.SPIClassResourceFilter;
import cn.zhuhongqing.bean.spi.SPIUtil;
import cn.zhuhongqing.util.StringUtils;

public final class BeanFactoryHole {

	/* Can not instance */
	private BeanFactoryHole() {
	};

	protected static final BeanFactory BEAN_FACTORY = SPIUtil.load(BeanFactory.class);

	public static void register(Object beanClassScanConfigInstance) {
		if (beanClassScanConfigInstance instanceof BeanClassScanConfig) {
			BeanClassScanConfig scan = (BeanClassScanConfig) beanClassScanConfigInstance;
			String pkg = scan.startPackage();
			pkg = StringUtils.endPadSlashAndAllPattern(StringUtils.replaceDotToSlash(pkg));
			register(BeanClassScanConfig.CLASS_SCAN.getResources(pkg, SPIClassResourceFilter.INSTANCE));
			register(scan.getScanClass());
		}
	}
	
	public static void register(Class<?> clazz){
		BEAN_FACTORY.register(clazz);
	}

	static void register(Collection<Class<?>> classes) {
		for (Class<?> clazz : classes) {
			BEAN_FACTORY.register(clazz);
		}
	}

	public static <T> T getBean(Class<T> clazz, String group) {
		return BEAN_FACTORY.getBean(clazz, group);
	}

	public static void inject(Object bean) {
		BEAN_FACTORY.inject(bean);
	}

	public static Integer getRegisterCount() {
		return BEAN_FACTORY.getRegisterCount();
	}

	public static Class<?>[] getRegisterClasses() {
		return BEAN_FACTORY.getRegisterClasses();
	}

}
