package cn.zhuhongqing.utils.bean;

public interface BeanFactory {

	default <T> T getBean(Class<T> clazz) {
		return getBean(clazz, null);
	}

	<T> T getBean(Class<T> clazz, String group);

	default BeanDefinitionGroup register(Class<?> clazz) {
		return register(clazz, null);
	}

	BeanDefinitionGroup register(Class<?> clazz, String group);

	Integer getRegisterCount();

	Class<?>[] getRegisterClasses();

}