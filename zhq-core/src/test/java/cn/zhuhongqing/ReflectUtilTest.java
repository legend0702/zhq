package cn.zhuhongqing;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * 
 * java.lang.Object has 12 methods.
 * 
 * @author HongQing.Zhu
 * 
 */

public class ReflectUtilTest {

	static Map<String, String[]> requestMap = new HashMap<String, String[]>();

	String[] strArr = new String[0];

	Person person = new Person();

	static {
		requestMap.put("idCard", new String[] { "330204198505043698" });
		requestMap.put("name", new String[] { "sam" });
		// requestMap.put("age", new String[] { "20" });
		// requestMap.put("birthday", new String[] { "1985-05-04" });
	}

	@Test
	public void test1() throws IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {
		// System.out.println(ReflectUtil.invoke(person, "secret"));
		// MethodUtils.findMethod(methodName, clazz, methodType, parameterTypes)
		show(Arrays.asList(Person.class.getDeclaredMethods()));
		System.out.println("------------------------------");
		show(Arrays.asList(Person.class.getMethods()));
		// show(Arrays.asList(Person.class.getDeclaredMethods()));
	}

	@Test
	public void getMethod() throws NoSuchMethodException, SecurityException {

		System.out.println(Person.class.getDeclaredMethod("say"));

		// show(Arrays.asList(People.class.getDeclaredMethods()));
	}

	@Test
	public void getAnnotations() throws NoSuchMethodException,
			SecurityException {
		Method say = Person.class.getMethod("say");
		show(say.getDeclaredAnnotations());
		System.out.println("-------------------------");
		show(say.getAnnotations());
		System.out.println("------------------------");
		System.out.println(say.isBridge());
	}

	@Test
	public void methodProp() throws NoSuchMethodException, SecurityException {
		Method add = ArrayList.class.getMethod("add", Object.class);
		show(add.getGenericParameterTypes());
		System.out.println("---------------------------");
		show(add.getParameterTypes());
		System.out.println("---------------------------");
		// show(add.getTypeParameters());
		System.out.println(add.toGenericString());
		System.out.println("------------------------");
		System.out.println(add.toString());
		System.out.println("----------------------------");
	}

	@Test
	public void test2() {
		show(Arrays.asList(Arr.class.getDeclaredClasses()));
	}

	@Test
	public void superClass() {
		// System.out.println(Arr.class.getSuperclass());
		show(Method.class.getInterfaces());
	}

	@Test
	public void getSuperDeclaredMethods() {
		Method[] son = Person.class.getDeclaredMethods();
		Method[] parent = Person.class.getSuperclass().getDeclaredMethods();
		show(son);
		System.out.println("--------------------------------");
		show(parent);
		System.out.println("--------------------------------");
		Set<Method> context = new HashSet<Method>();
		context.addAll(Arrays.asList(son));
		context.addAll(Arrays.asList(parent));
		show(context);

	}

	@Test
	public void getSuperDeclaredMethods2() {
		Method[] son = Person.class.getDeclaredMethods();
		Method[] parent = Person.class.getSuperclass().getDeclaredMethods();
		show(son);
		System.out.println("--------------------------------");
		show(parent);
		System.out.println("--------------------------------");
		OneClassMethodSet context = new OneClassMethodSet();
		context.addAll(son);
		context.addAll(parent);
		show(context.toCollection());

	}

	public void isModifiableClass0() throws NoSuchMethodException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		Constructor<?> con = Class
				.forName("sun.instrument.InstrumentationImpl")
				.getDeclaredConstructor(long.class, boolean.class,
						boolean.class);
		con.setAccessible(true);
		Instrumentation instrumentation = (Instrumentation) con.newInstance(0,
				false, false);
		System.out.println(instrumentation
				.isModifiableClass(Instrumentation.class));
	}

	public void isModifiableClass() throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		Class<?> clazz = Class.class;
		Field classRedefinedCount = clazz
				.getDeclaredField("classRedefinedCount");
		Field lastRedefinedCount = clazz.getDeclaredField("lastRedefinedCount");
		classRedefinedCount.setAccessible(true);
		lastRedefinedCount.setAccessible(true);
		System.out.println(classRedefinedCount.getInt(person));
		System.out.println(lastRedefinedCount.getInt(person));

	}

	@Test
	public void getClassMethod() throws NoSuchMethodException,
			SecurityException {
		// MethodReference methods = new MethodReference(Person.class);
		long start = 0;
		long end = 0;
		start = System.currentTimeMillis();
		System.out.println(Person.class.getMethod("setName", String.class));
		end = System.currentTimeMillis();
		System.out.println("start:" + start + "  end:" + end);
		start = System.currentTimeMillis();
		// System.out.println(methods.getMember("say", String.class));
		// show(methods.getMember("say");
		// show(methods.getAll().entrySet());
		end = System.currentTimeMillis();
		System.out.println("start:" + start + "  end:" + end);
		start = System.currentTimeMillis();
		// System.out.println(methods.getMember(Son.class, String.class));
		// show(methods.getMember(Son.class));
		// show(methods.getAll().entrySet());
		end = System.currentTimeMillis();
		System.out.println("start:" + start + "  end:" + end);
	}

	public void a(List<Object> obj) {

	}

	public void a(Collection<String> str) {
	}

	@Test
	public void objectClass() {
		System.out.println(Object.class.getClass().getClass());
	}

	/**
	 * first come first served.
	 * 
	 * use {@link #createMethodKey(Method)} to compare
	 * 
	 * @author HongQing.Zhu
	 * 
	 */

	class OneClassMethodSet {

		HashMap<String, Method> methodSet = new HashMap<String, Method>();

		public boolean contains(Method o) {
			return methodSet.containsKey(createMethodKey(o));
		}

		public void addAll(Method[] methods) {
			for (Method m : methods) {
				add(m);
			}
		}

		public boolean add(Method e) {
			String key = createMethodKey(e);
			if (methodSet.get(key) != null)
				return false;
			methodSet.put(key, e);
			return true;
		}

		/***
		 * return a new Collection and clear this
		 * 
		 * wait for gc release :)
		 * 
		 * @return
		 */

		public Collection<Method> toCollection() {
			try {
				return new ArrayList<Method>(methodSet.values());
			} finally {
				methodSet.clear();
			}
		}

		/**
		 * Create a key base on Method's name and parameterTypes.
		 * 
		 * @param method
		 * @return
		 */

		private String createMethodKey(Method method) {
			StringBuffer sb = new StringBuffer();
			sb.append(method.getName());
			Class<?>[] arguments = method.getParameterTypes();
			for (Class<?> arg : arguments) {
				sb.append(".").append(arg);
			}
			return sb.toString();
		}
	}

	@Test
	public void test3() {
		System.out.println(Person.class.getCanonicalName());
	}

	@Test
	public void hashSetTest() {
		HashSet<Person> set = new HashSet<Person>();
		Person one = new Person();
		Person two = new Person();
		one.setName("sam");
		one.setAge(19);
		two.setName("sam");
		two.setAge(91);
		System.out.println(one.equals(two));
		System.out.println(set.add(one));
		System.out.println(set.add(two));
		System.out.println(set.size());
		System.out.println(set.iterator().next().getAge());
	}

	void show(Object[] objs) {
		show(Arrays.asList(objs));
	}

	void show(Collection<?> col) {
		Iterator<?> itr = col.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			System.out.println(obj);
		}
	}

	class Arr extends ArrayList<String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}

}
