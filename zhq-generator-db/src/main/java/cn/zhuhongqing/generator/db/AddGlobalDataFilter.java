package cn.zhuhongqing.generator.db;

import java.util.Map;

import cn.zhuhongqing.generator.GeneratorFilter;
import cn.zhuhongqing.utils.bean.BeanUtil;

/**
 * 添加一个全局参数的{@link GeneratorFilter}
 * 
 * 放在最后一个位置 避免被其他过滤器污染
 *
 */

public class AddGlobalDataFilter implements GeneratorFilter {

	private GeneratorDBConfig config;

	public AddGlobalDataFilter(GeneratorDBConfig config) {
		this.config = config;
	}

	public GeneratorDBConfig getConfig() {
		return config;
	}

	public void setConfig(GeneratorDBConfig config) {
		this.config = config;
	}

	@Override
	public Object beforeAll(Object model) {
		Map<String, Object> data = BeanUtil.beanToMap(model);
		data.putAll(config.getGlobalData());
		return data;
	}

}
