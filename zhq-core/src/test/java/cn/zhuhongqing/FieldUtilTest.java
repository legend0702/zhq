package cn.zhuhongqing;

import org.junit.Test;

import cn.zhuhongqing.reflect.FieldReference;

public class FieldUtilTest {

	FieldReference reference = new FieldReference(Person.class);

	@Test
	public void test() {
		Utils.show(reference.getAll().values());
	}

	@Test
	public void test2() {
		Utils.show(Person.class.getDeclaredFields());
	}

	public static void main(String[] args) {
		Person person = new Person();
		person.name = "abc";
		// person.setName("abc");
		System.err.println(((People) person).name);
	}

}
