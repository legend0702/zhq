package cn.zhuhongqing.bean;

@SPI
public interface BeanFactory {

	default <T> T getBean(Class<T> clazz) {
		return getBean(clazz, null);
	}

	<T> T getBean(Class<T> clazz, String group);

	void inject(Object bean);

	default BeanDefinitionGroup register(Class<?> clazz) {
		return register(clazz, null);
	}

	BeanDefinitionGroup register(Class<?> clazz, String group);

	Integer getRegisterCount();

	Class<?>[] getRegisterClasses();

}