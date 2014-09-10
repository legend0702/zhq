package cn.zhuhongqing.module;

import cn.zhuhongqing.exception.ModuleException;
import cn.zhuhongqing.utils.ReflectUtil;

/**
 * ZHQ module's design and config-store.
 * 
 * Use it in self-thread or not set prop in multi threads.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 * 
 */

public abstract class Module {

	/**
	 * @see ModuleStore#getModuleStoreInstance()
	 */

	public static final ModuleStore getModuleStore() {
		return ModuleStore.getModuleStoreInstance();
	}

	/**
	 * Get {@link Module}.
	 * 
	 * @throws ModuleException
	 */

	public static final <T extends Module> T getModule(Class<T> clz)
			throws ModuleException {
		return ModuleStore.getModule(clz);
	}

	/**
	 * Get Module's name.
	 * 
	 * @see Module#getModuleType(Class)
	 * @see Class#getSimpleName()
	 */

	public static final String getModuleName(Class<? extends Module> module) {
		return getModuleType(module).getSimpleName();
	}

	/**
	 * Get class's top subClass of {@link #Module}.
	 */

	public static final Class<? extends Module> getModuleType(
			Class<? extends Module> claz) {
		return ReflectUtil.getTopClass(claz, Module.class);
	}

	/**
	 * Judge class is Module.
	 */

	public static final boolean equals(Class<?> moduleClass) {
		return Module.class.equals(moduleClass);
	}

	/**
	 * @see #getModuleName(Class)
	 */

	public final String getModuleName() {
		return getModuleName(this.getModuleType());
	}

	/**
	 * @see #getModuleType(Class)
	 */

	public final Class<? extends Module> getModuleType() {
		return getModuleType(getClass());
	}

	/**
	 * Init module.
	 */

	public final void initModule() {
		try {
			init();
		} catch (Exception e) {
			throw new ModuleException(e);
		}
	}

	protected void init() throws Exception {

	}

}
