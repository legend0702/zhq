package cn.zhuhongqing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import cn.zhuhongqing.io.StreamUtil;
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

	// public static void main(String[] args) {
	// Person person = new Person();
	// person.name = "abc";
	// person.setName("abc");
	// System.err.println(((People) person).name);
	//
	// }

	public static void test3() throws IOException {
		long ts = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
//			byte[] temp = FileUtil
//					.readBytes("D:\\MyProgram\\directx-aug2009-redist.rar");
			// System.out.println(temp.length);
			// System.out.println("error:" + temp.length);
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("time: " + ts);
	}

	public static void test1() throws IOException {
		byte[] buffer = new byte[108281899];
		long ts = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			int hr = -1;
			InputStream in = new FileInputStream(new File(
					"D:\\MyProgram\\directx-aug2009-redist.rar"));
			hr = in.read(buffer, 0, 108281899);
			if (hr != 108281899) {
				System.out.println("error:" + hr);
			}
			StreamUtil.close(in);
		}
		ts = System.currentTimeMillis() - ts;
		System.out.println("time: " + ts);
	}

	public static void main(String[] args) throws IOException {
		System.out.println("read file test");
		test1();
	}

}
