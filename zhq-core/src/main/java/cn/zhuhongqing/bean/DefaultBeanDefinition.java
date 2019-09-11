package cn.zhuhongqing.bean;

public class DefaultBeanDefinition extends ObjectStateWithAttrHole<String, BeanProperty> implements BeanDefinition {

	private Class<?> clazz;

	public DefaultBeanDefinition(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public Class<?> getDeclaringClass() {
		return (Class<?>) clazz;
	}

	@Override
	Object getTarget() {
		return clazz;
	}

	@Override
	public Class<?> getMetaType() {
		return clazz;
	}

}
