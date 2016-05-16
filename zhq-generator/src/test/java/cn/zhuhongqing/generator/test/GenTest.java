package cn.zhuhongqing.generator.test;

import java.util.HashMap;
import java.util.Map;

import cn.zhuhongqing.Generator;

public class GenTest {

	public static void main(String[] args) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", "GenName");
		data.put("data", "GenData");
		Generator.gen("temp", "e:/gen", data);
	}

}