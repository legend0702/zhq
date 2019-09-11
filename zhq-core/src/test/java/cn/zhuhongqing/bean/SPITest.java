package cn.zhuhongqing.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;

import cn.zhuhongqing.bean.spi.SPIBeanFactory;
import cn.zhuhongqing.util.ArraysUtils;
import cn.zhuhongqing.util.ReflectUtils;
import cn.zhuhongqing.util.meta.MetaData;

public class SPITest {

	@Test
	public void fileLoad() {
		BeanFactory factory = SPIBeanFactory.Builder.Build();
		System.out.println(factory.getRegisterCount());
		System.out.println(ArraysUtils.toString(factory.getRegisterClasses()));
	}

	@Test
	public void classLoad() {
		BeanFactory factory = SPIBeanFactory.Builder.Build("cn/zhuhongqing/bean/*");
		System.out.println(factory.getRegisterCount());
		System.out.println(ArraysUtils.toString(factory.getRegisterClasses()));
	}

	@Test
	public void showTime() {
		time(new Runnable() {
			@Override
			public void run() {
				fileLoad();
			}
		});
		time(new Runnable() {
			@Override
			public void run() {
				classLoad();
			}
		});
	}

	@Test
	public void getBean() {
		BeanFactory factory = SPIBeanFactory.Builder.Build("cn/zhuhongqing/bean/*");
		BeanInterface fis = factory.getBean(BeanInterface.class, "Bean");
		BeanInterface fis2 = factory.getBean(BeanInterface.class);
		System.out.println(fis + ":" + fis.create("Hello SPI!").getName());
		System.out.println(fis.get() + ":" + fis.get().create("Hello SPI!").getName());
		System.out.println("----------------------");
		System.out.println(fis2 + ":" + fis2.create("Hello SPI!").getName());
		System.out.println(fis2.get() + ":" + fis2.get().create("Hello SPI!").getName());

		// manual
		factory.register(Foo.class, "Bean");
		factory.register(Woo.class);
		Woo f = factory.getBean(Foo.class, "Bean");
		Woo w = factory.getBean(Woo.class);
		System.out.println(w);
		System.out.println(f);

		System.out.println(factory.getRegisterCount());
		System.out.println(ArraysUtils.toString(factory.getRegisterClasses()));
		// System.out.println(AbstractBeanFactory.BEAN_DEFINITION_GROUP.values());
	}

	@Test
	public void metaDataTest() {
		// Class
		MetaData clazz = MetaData.of(ABC.class);
		System.out.println(clazz);
		System.out.println(clazz.getMember().getClass());
		System.out.println(clazz.getMetaType());
		System.out.println(clazz.getDeclaringClass());
		System.out.println(clazz.getName());

		// Field
		Field field = ReflectUtils.getSupportedField(Bean2Implment.class, "beanInterface");
		MetaData m = MetaData.of(field);
		System.out.println(m);
		System.out.println(m.getMember().getClass());
		System.out.println(m.getMetaType());
		System.out.println(m.getDeclaringClass());
		System.out.println(m.getName());

		// Method
		Method m1 = ReflectUtils.findMethod(ABC.class, "hello");
		m = MetaData.of(m1);
		System.out.println(m);
		System.out.println(m.getMember().getClass());
		System.out.println(m.getMetaType());
		System.out.println(m.getDeclaringClass());
		System.out.println(m.getName());

		// Constructor
		Constructor<ABC> abc = ReflectUtils.getParamsAndUsableConstructor(ABC.class, SPITest.class);
		// Invokable invoker = Invokable.of(abc);
		m = MetaData.of(abc);
		System.out.println(m);
		System.out.println(m.getMember().getClass());
		System.out.println(m.getMetaType());
		System.out.println(m.getDeclaringClass());
		System.out.println(m.getName());

		// Paramter
		m = MetaData.of(abc.getParameters()[0]);
		System.out.println(m);
		System.out.println(m.getMember().getClass());
		System.out.println(m.getMetaType());
		System.out.println(m.getMember().getDeclaringClass());
		System.out.println(m.getName());
	}

	public void time(Runnable run) {
		long start = System.currentTimeMillis();
		run.run();
		long end = System.currentTimeMillis();
		System.out.println("Use time:" + (end - start));
	}

	public static class ABC {
		public ABC() {

		}

		public ABC(SPITest test) {

		}

		public String hello(String str) {
			return "ABC!" + str;
		}
	}

}
