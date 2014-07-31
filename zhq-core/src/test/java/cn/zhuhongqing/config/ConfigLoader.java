package cn.zhuhongqing.config;

/**
 * Load config-resource.
 * 
 * @author HongQing.Zhu
 */

public interface ConfigLoader {

	/**
	 * Load resource as {@link Config}.
	 * 
	 * @param path
	 */

	public Config load(String path);

}
