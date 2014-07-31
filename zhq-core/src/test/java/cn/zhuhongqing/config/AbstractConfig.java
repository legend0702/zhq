package cn.zhuhongqing.config;

import cn.zhuhongqing.exception.UncheckedException;

/**
 * AbstractConfig.
 * 
 * @author HongQing.Zhu
 * 
 */

public abstract class AbstractConfig implements Config {

	/**
	 * Add to origin config.
	 * 
	 * @param config
	 * @return
	 */

	public void add(Config config) {
		if (!this.getClass().equals(config.getClass()))
			throw new UncheckedException("Can not add config from "
					+ config.getClass() + " as " + this.getClass());
		addConfig(config);
	}

	/**
	 * {@link #add(Config)}
	 * 
	 * @param config
	 */

	abstract void addConfig(Config config);

}
