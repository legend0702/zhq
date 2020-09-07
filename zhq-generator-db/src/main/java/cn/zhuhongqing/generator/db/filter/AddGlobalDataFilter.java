package cn.zhuhongqing.generator.db.filter;

import cn.zhuhongqing.generator.db.GeneratorDBConfig;
import cn.zhuhongqing.generator.filter.GeneratorFilter;
import cn.zhuhongqing.util.BeanWrap;

/**
 * 添加一个全局参数的{@link GeneratorFilter}<br/>
 * 
 * 主要用来将{@link GeneratorDBConfig#getGlobalData()}添加到扩展参数中<br/>
 * 
 * 放在第一个位置 以便后续的Filter中可以用到<br/>
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
	public boolean beforeAll(BeanWrap beanWrap) {
		beanWrap.getExParams().putAll(config.getGlobalData());
		return true;
	}

}
