package cn.zhuhongqing.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import cn.zhuhongqing.util.meta.Invokable;
import cn.zhuhongqing.util.struct.AttrHole;

/**
 * Bean中可执行对象
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public interface BeanInvokable extends BeanProperty, Invokable, AttrHole<Integer, BeanProperty> {

	public static BeanInvokable of(Constructor<?> invokable) {
		return new DefaultBeanConstructor(invokable);
	}

	public static BeanInvokable of(Method invokable) {
		return new DefaultBeanMethod(invokable);
	}

}