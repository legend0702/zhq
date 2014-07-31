package cn.zhuhongqing;

import java.io.Serializable;

import cn.zhuhongqing.reflect.MethodUtil;

/**
 * Test MethodUtil :)
 * 
 * @author HongQing.Zhu
 * 
 */

public class MethodUtilTest {

	Person person = new Person();

	public void getDeclaredMethods() {
		Utils.show(MethodUtil.getDeclaredMethods(Person.class));
	}

	public void getMethods() {
		Utils.show(MethodUtil.getMethods(Person.class));
	}

	public void getSuperDeclaredMethods() {
		Utils.show(MethodUtil.getSuperDeclaredMethods(Person.class,
				Serializable.class));
	}

	public void getSuperDeclaredMethodsAll() {
		Utils.show(MethodUtil.getSuperDeclaredMethods(Person.class,
				Object.class));
	}

}
