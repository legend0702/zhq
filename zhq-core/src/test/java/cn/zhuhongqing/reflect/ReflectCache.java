package cn.zhuhongqing.reflect;

import java.lang.reflect.Member;
import java.util.HashMap;

public abstract class ReflectCache<T extends Member> extends
		HashMap<Class<?>, T> {

	private static final long serialVersionUID = 1L;

}
