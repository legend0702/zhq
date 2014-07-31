package cn.zhuhongqing.module;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.zhuhongqing.exception.ModuleException;
import cn.zhuhongqing.exception.MultiOnceModuleException;

/**
 * Store all modules here.
 * 
 * @see ModuleStore#MODULE_STORE
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class ModuleStore {

	/**
	 * Get {@link ModuleStore} instance.
	 * 
	 * It's single-instance.
	 */

	final static ModuleStore getModuleStoreInstance() {
		return SingleInstance.instance;
	}

	private ModuleStore() {
	};

	/**
	 * Module-Mapping-Store.
	 * 
	 * Key is {@link Module#getModuleType()}.
	 * 
	 * Value is instance of {@link Module}.
	 * 
	 * It's synchronizedMap.
	 */

	private Map<Class<? extends Module>, Module> MODULE_STORE = Collections
			.synchronizedMap(new HashMap<Class<? extends Module>, Module>());

	/**
	 * Get {@link #MODULE_STORE};
	 */

	public static Map<Class<? extends Module>, Module> getModuleStore() {
		return getModuleStoreInstance().MODULE_STORE;
	}

	/**
	 * Get module by moduleClass.
	 * 
	 * @param moduleClass
	 * @throws ModuleException
	 */

	@SuppressWarnings("unchecked")
	public static <T extends Module> T getModule(Class<T> moduleClass) {
		if (Module.equals(moduleClass)) {
			throw new ModuleException(moduleClass.getName()
					+ " is not a real-module.");
		}
		// if (!Module.equals(moduleClass.getSuperclass()))
		// throw new ConfigException(moduleClass.getName()
		// + " must module's directly under");
		Module module = getModuleStore().get(Module.getModuleType(moduleClass));
		if (module == null)
			return null;
		if (moduleClass.isAssignableFrom(module.getClass())) {
			return (T) module;
		}
		throw new ModuleException("Module class ["
				+ module.getClass().getName() + "] can not cast to "
				+ moduleClass.getName());
	}

	/**
	 * Add module.
	 * 
	 * @param module
	 * @throws ModuleException
	 */

	public static synchronized void addModule(Module module) {
		Class<? extends Module> moduleConfigClz = module.getClass();
		Class<? extends Module> moduleClass = module.getModuleType();
		Module moduleC = getModule(moduleClass);
		if (moduleC == null) {
			getModuleStore().put(moduleClass, module);
			return;
		}
		Class<? extends Module> moduleClz = moduleC.getClass();
		// SubClass cover parent class
		if (!moduleClz.isAssignableFrom(moduleConfigClz)) {
			return;
		}
		if (moduleClz.equals(moduleConfigClz)) {
			getModuleStore().put(moduleClass, module);
			return;
		}
		if (moduleClz.getSuperclass().equals(moduleConfigClz.getSuperclass())) {
			throw new MultiOnceModuleException(module);
		}
		getModuleStore().put(moduleClass, module);
	}

	/**
	 * Add config to module.
	 * 
	 * @param moduleClass
	 * @param config
	 */

	// public static synchronized void addModuleConfig(
	// Class<? extends Module> moduleClass, Config config) {
	// getModule(moduleClass).addConfig(config.toModule(moduleClass));
	// TODO
	// }

	private static class SingleInstance {
		private static ModuleStore instance = new ModuleStore();
	}

}
