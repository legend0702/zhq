package cn.zhuhongqing;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import javax.lang.model.type.WildcardType;

import org.junit.Test;

import cn.zhuhongqing.reflect.MethodReference;

public class MethodReferenceTest {

	Person person = new Person();
	MethodReference reference = new MethodReference(Person.class);
	Method method = reference.getOneMember("setName", String.class);
	Method interfaceMethod = reference.getOneMember("setSer",
			Serializable.class);
	Method arrMethod = reference.getOneMember("setFirends", Person[].class);

	@Test
	public void method() {
		Utils.show(method.getGenericParameterTypes());
		System.out.println("-----------------------------");
		Utils.show(method.getParameterTypes());
		System.out.println("-----------------------------");
		Utils.show(method.getParameterAnnotations()[0]);
	}

	@Test
	public void parameterType() {
		Type type = method.getGenericParameterTypes()[0];
		if (type instanceof ParameterizedType)
			System.out.println("ParameterizedType");
		if (type instanceof TypeVariable)
			System.out.println("TypeVariable");
		if (type instanceof GenericArrayType)
			System.out.println("GenericArrayType");
		if (type instanceof java.lang.reflect.WildcardType)
			System.out.println("java.lang.reflect.WildcardType");
		if (type instanceof WildcardType)
			System.out.println("WildcardType");
		System.out.println(type);
	}

	@Test
	public void method2() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		interfaceMethod.invoke(person, "Hello!");
		System.out.println(person.getSer());
	}

	@Test
	public void TypeVariable() {
		TypeVariable<Method>[] typeV = method.getTypeParameters();
		System.out.println(typeV.length);
	}
}
