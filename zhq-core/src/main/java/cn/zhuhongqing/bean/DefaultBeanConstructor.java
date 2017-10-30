package cn.zhuhongqing.bean;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.util.LinkedHashMap;

import cn.zhuhongqing.utils.ConstructorUtil;

/**
 * 构造函数
 * 
 * 同一个类里 多个构造函数用Group进行区分
 * 
 * 同一个类中 Group不能出现重复
 * 
 */

public class DefaultBeanConstructor extends ObjectStateWithAttrbuteHole<Integer, BeanProperty>
		implements BeanInvokable {

	private Constructor<?> con;

	public DefaultBeanConstructor(Constructor<?> con) {
		super(new LinkedHashMap<>());
		this.con = con;
	}

	@Override
	public String getName() {
		return getGroup();
	}

	@Override
	public Executable getExecutor() {
		return con;
	}

	@Override
	public Member getMember() {
		return con;
	}

	@Override
	public Object invoke(Object obj, Object... args) {
		return ConstructorUtil.invoke(con, args);
	}

	@Override
	Object getTarget() {
		return con;
	}

	@Override
	public Class<?> getMetaType() {
		return con.getDeclaringClass();
	}

	@Override
	public AnnotatedElement getAnnotatedElement() {
		return con;
	}

}
