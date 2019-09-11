package cn.zhuhongqing.bean;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.zhuhongqing.bean.spi.SPI;
import cn.zhuhongqing.util.FieldUtils;
import cn.zhuhongqing.util.ReflectUtils;

public class BeanTest implements BeanInterface {

	private String name;

	public BeanTest() {
	}

	public BeanTest(String name) {
		this.name = name;
	}

	@SPI
	private BeanInterface i;

	@SPI("bean")
	BeanInterface i2;

	public void show() {
		System.out.println(name);
	}

	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		BeanTest bean = new BeanTest();
		String name = "zhangsan";
		// 100w
		int count = 1000000;
		long s = System.currentTimeMillis();
		for (Integer i = 0; i < count; i++) {
			bean.setName(name);
		}
		long e = System.currentTimeMillis();
		System.out.println("预热:" + (e - s));

		s = System.currentTimeMillis();
		for (Integer i = 0; i < count; i++) {
			bean.setName(name);
		}
		e = System.currentTimeMillis();
		System.out.println("Use time:" + (e - s));

		Field field = ReflectUtils.getSupportedField(BeanTest.class, "name");
		s = System.currentTimeMillis();
		for (Integer i = 0; i < count; i++) {
			FieldUtils.set(field, bean, name);
		}
		e = System.currentTimeMillis();
		System.out.println("Reflect Use time:" + (e - s));

		// System.out.println(ArraysUtil.toString());
		// bean.autowired();
		// bean.i.create("A").show();
		// bean.i2.create("B").show();

		// System.out.println(Foo.class.getName());
		//
		// show(ClassUtil.getAllInterfacesForClass(Foo.class));
		// show(ReflectUtil.getSuperClasses(Foo.class).toArray(new Class<?>[]
		// {}));

		// PropertyDescriptor[] descs =
		// BeanInfoUtil.getPropertyDescriptors(Foo.class);
		// for (PropertyDescriptor desc : descs) {
		// System.out.println(desc.getName());
		// System.out.println(desc.getShortDescription());
		// System.out.println(desc.getDisplayName());
		// System.out.println(desc.getPropertyType().getAnnotation(SPI.class));
		// System.out.println(desc.getPropertyEditorClass());
		// System.out.println(ReflectUtil.getSupportedField(Foo.class,
		// desc.getName()));
		// System.out.println("========================================");
		// }

	}

	public static void show(Class<?>[] interfaces) {
		for (Class<?> t : interfaces) {
			System.out.println(t);
		}
		System.out.println("EOF========================================");
	}

	public void testmap() {
		// 1000w
		int count = 10000 * 1000;
		// 先热身
		put(new HashMap<>(1000), 10000);

		// 无锁单线程
		System.out.println("无锁单线程耗时:" + put(new HashMap<>(10000), count));
		// 有锁多线程
		System.out.println("有锁单线程耗时:" + put(new ConcurrentHashMap<>(10000), count));
	}

	public static long put(Map<String, Object> map, int num) {
		long s = System.currentTimeMillis();
		for (Integer i = 0; i < num; i++) {
			map.put(i.toString(), i);
		}
		long e = System.currentTimeMillis();
		return e - s;
	}

	public static void show(AnnotatedType[] types) {
		for (AnnotatedType type : types) {
			System.out.println(type.getType());
		}

		System.out.println("EOF=================================");
	}

	@Override
	public BeanTest create(String str) {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BeanInterface getI() {
		return i;
	}

	public void setI(BeanInterface i) {
		this.i = i;
	}

}
