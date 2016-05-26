package cn.zhuhongqing.generator;

import java.util.Map;
import java.util.Map.Entry;

import cn.zhuhongqing.utils.bean.BeanUtil;

public class AddKeysToUpper implements GeneratorFilter {

	@Override
	public Object beforeAll(Object model) {
		Map<String, Object> data = BeanUtil.beanToMap(model);
		for (Entry<String, Object> e : data.entrySet()) {
			data.put(e.getKey().toUpperCase(), e.getValue());
		}
		return data;
	}

}
