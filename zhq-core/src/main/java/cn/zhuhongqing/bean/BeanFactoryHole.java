package cn.zhuhongqing.bean;

import java.util.Collection;

import cn.zhuhongqing.utils.StringUtil;

public final class BeanFactoryHole {

	/* Can not instance */
	private BeanFactoryHole() {
	};

	protected static final BeanFactory BEAN_FACTORY = SPIUtil.load(BeanFactory.class);

	public static void register(Object beanClassScanConfigInstance) {
		if (BeanClassScanConfig.class.isAssignableFrom(beanClassScanConfigInstance.getClass())) {
			BeanClassScanConfig scan = (BeanClassScanConfig) beanClassScanConfigInstance;
			String pkg = scan.startPackage();
			pkg = StringUtil.endPadSlashAndAllPattern(StringUtil.replaceDotToSlash(pkg));
			register(BeanClassScanConfig.CLASS_SCAN.getResources(pkg, SPIClassResourceFilter.INSTANCE));
			register(scan.getScanClass());
		}
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
