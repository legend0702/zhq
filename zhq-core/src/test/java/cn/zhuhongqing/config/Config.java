package cn.zhuhongqing.config;

import cn.zhuhongqing.module.Module;

/**
 * Config defined.
 * 
 * @author HongQing.Zhu
 * 
 */

public interface Config {

	/**
	 * Use "key" to get Config.
	 */

	public String get(String name);

	/**
	 * Convert config to module-config.
	 */

	public <T extends Module> T toModule(Class<T> clazz);

}
