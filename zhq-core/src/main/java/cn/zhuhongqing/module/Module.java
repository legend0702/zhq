package cn.zhuhongqing.module;

import cn.zhuhongqing.exception.ModuleException;

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
	 * Get subclass's top superClass of {@link #Module}.
	 */

	@SuppressWarnings("unchecked")
	public static final Class<? extends Module> getModuleType(
			Class<? extends Module> claz) {
		Class<? extends Module> moduleClass = claz;
		while (!Module.class.equals(moduleClass.getSuperclass())) {
			moduleClass = (Class<? extends Module>) moduleClass.getSuperclass();
		}
		return moduleClass;
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
