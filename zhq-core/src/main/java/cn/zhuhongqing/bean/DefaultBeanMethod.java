package cn.zhuhongqing.bean;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

import cn.zhuhongqing.util.MethodUtils;

public class DefaultBeanMethod extends ObjectStateWithAttrHole<Integer, BeanProperty> implements BeanInvokable {

	private Method method;

	public DefaultBeanMethod(Method method) {
		super(new LinkedHashMap<>());
		this.method = method;
	}

	@Override
	public Executable getExecutor() {
		return method;
	}

	@Override
	public Member getMember() {
		return method;
	}

	@Override
	public Object invoke(Object obj, Object... args) {
		return MethodUtils.invoke(method, obj, args);
	}

	@Override
	Object getTarget() {
		return method;
	}

	@Override
	public Type getReturnType() {
		return method.getReturnType();
	}

	@Override
	public Object getDefaultValue() {
		return method.getDefaultValue();
	}

	@Override
	public AnnotatedElement getAnnotatedElement() {
		return method;
	}

	@Override
	public Class<?> getMetaType() {
		return method.getDeclaringClass();
	}

}
