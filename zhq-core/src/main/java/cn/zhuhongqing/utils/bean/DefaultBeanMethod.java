package cn.zhuhongqing.utils.bean;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import cn.zhuhongqing.utils.MethodUtil;

public class DefaultBeanMethod extends ObjectStateWithAttrbuteHole<Integer, BeanProperty> implements BeanInvokable {

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
		return MethodUtil.invoke(method, obj, args);
	}

	@Override
	Object getTarget() {
		return method;
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
