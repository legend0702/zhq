package cn.zhuhongqing.test;

import java.util.HashMap;
import java.util.Map;

import cn.zhuhongqing.Core;
import cn.zhuhongqing.Script;
import cn.zhuhongqing.module.Module;
import cn.zhuhongqing.module.ModuleOperation;
import cn.zhuhongqing.module.ModuleStore;
import cn.zhuhongqing.script.ScriptException;
import cn.zhuhongqing.script.ScriptStore;
import cn.zhuhongqing.utils.ClassUtil;

public class ModuleTest {

	static final ClassLoader cl = ClassUtil.getDefaultClassLoader();

	public void testInitDefaultPackgeModule() {
		ModuleOperation.loaderDefaultPackgeModule();
		Core core = ModuleStore.getModule(Core.class);
		System.out.println(core.getModuleName());
		System.out.println(core.getClass());
	}

	public static void main(String arg[]) {
		ModuleOperation.loaderDefaultPackgeModule();
		Module module = ModuleStore.getModule(Core.class);
		System.out.println(module.getModuleName());
		System.out.println(module.getClass());
		module = ModuleStore.getModule(Script.class);
		System.out.println(module.getModuleName());
		System.out.println(module.getClass());
		System.out.println(ModuleStore.getModuleStore());
	}

	public void testValidate() throws ScriptException {
		ScriptStore scriptStore = Script.createScriptEngine().analyze(
				ClassLoader.getSystemResourceAsStream("zhq-config.js"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "script");
		Object obj = scriptStore.invoke("validate", map);
		System.out.println(obj);
	}
}
