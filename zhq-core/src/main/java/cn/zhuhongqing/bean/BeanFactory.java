package cn.zhuhongqing.bean;

import java.util.List;

import cn.zhuhongqing.bean.spi.SPI;

/**
 * 类工厂 主要管理类的生命周期以及依赖关系
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

@SPI
public interface BeanFactory {

	default <T> T getBean(Class<T> clazz) {
		return getBean(clazz, null);
	}

	<T> T getBean(Class<T> clazz, String group);

	<T> List<T> getBeans(Class<T> clazz);

	void inject(Object bean);

	default BeanDefinitionGroup register(Class<?> clazz) {
		return register(clazz, null);
	}

	BeanDefinitionGroup register(Class<?> clazz, String group);

	Integer getRegisterCount();

	Class<?>[] getRegisterClasses();

}