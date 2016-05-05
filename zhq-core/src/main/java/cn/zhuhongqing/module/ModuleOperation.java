package cn.zhuhongqing.module;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.zhuhongqing.ZHQ;
import cn.zhuhongqing.exception.ModuleException;
import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.ReflectUtil;
import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.scan.ClassScan;

/**
 * Load and init {@link Module}.
 * 
 * @see {@link ModuleStore}
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class ModuleOperation {

	private ModuleOperation() {
	};

	/**
	 * @throws ModuleException
	 * @see ModuleStore#addModule(Module)
	 */

	public static void addModule(Module... modules) {
		for (Module module : modules)
			ModuleStore.addModule(module);
	}

	/**
	 * Load module.
	 * 
	 * @param moduleClasses
	 * @see #addModule(Module...)
	 */

	@SafeVarargs
	public static void loadModule(Class<? extends Module>... moduleClasses) {
		for (Class<? extends Module> moduleClass : moduleClasses) {
			addModule(ReflectUtil.newInstance(moduleClass));
		}
	}

	/**
	 * Init module-config.
	 * 
	 * @throws ModuleException
	 */

	public static void loaderDefaultPackgeModule() {
		loadModule(ZHQ.DEFAULT_PACKAGE_NAME);
	}

	/**
	 * Scan package and get class which is {@link Module}'s sub-Class. Then
	 * create and load.
	 * 
	 * @throws ModuleException
	 */

	@SuppressWarnings("unchecked")
	public static void loadModule(String... packageNames) {
		Set<Class<?>> moduleClasses = new LinkedHashSet<Class<?>>();
		for (String packageName : packageNames) {
			packageName = ClassUtil.classToSlash(packageName);
			moduleClasses.addAll(new ClassScan().getResources(StringUtil
					.endPadAsterisk(packageName)));
		}
		Iterator<Class<?>> classItr = moduleClasses.iterator();
		while (classItr.hasNext()) {
			Class<?> clazz = classItr.next();
			if (Module.class.isAssignableFrom(clazz)
					&& ReflectUtil.isOrdinaryClass(clazz)) {
				continue;
			}
			classItr.remove();
		}
		classItr = moduleClasses.iterator();
		while (classItr.hasNext()) {
			loadModule((Class<Module>) classItr.next());
		}
	}

	/**
	 * Init specify module.
	 * 
	 * @param moduleClass
	 * @throws ModuleException
	 */

	public static void initModule(Class<Module> moduleClass) {
		ModuleStore.getModule(moduleClass).initModule();
	}

	/**
	 * Init all loaded module.
	 * 
	 * @throws ModuleException
	 */

	public static void initAllModules() {
		Iterator<Module> moduleItr = ModuleStore.getModuleStore().values()
				.iterator();
		while (moduleItr.hasNext()) {
			moduleItr.next().initModule();
		}
	}
}
