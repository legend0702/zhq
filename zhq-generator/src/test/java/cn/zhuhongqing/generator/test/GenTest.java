package cn.zhuhongqing.generator.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import cn.zhuhongqing.Generator;

public class GenTest {

	@Test
	@Ignore
	public void test1() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", "GenName");
		data.put("data", "GenData");
		Generator.gen("temp", "e:/gen2", data);
		System.out.println("Gen success!");
	}

}