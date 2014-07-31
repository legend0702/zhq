package cn.zhuhongqing;

import cn.zhuhongqing.module.Module;
import cn.zhuhongqing.script.JavaScriptEngine;
import cn.zhuhongqing.script.ScriptEngine;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.SystemUtil;

/**
 * JavaScript Module.
 * 
 * 
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 * @since 1.7
 */

public class Script extends Module {

	/**
	 * Module's name.
	 */

	public static String SCRIPT = Module.getModuleName(Script.class);

	/**
	 * @see #getModule(Class)
	 */

	public static Script getScriptModule() {
		return getModule(Script.class);
	}

	/**
	 * Create a scriptEngine to load and analyze script.
	 * 
	 */

	public static ScriptEngine createScriptEngine() {
		return new JavaScriptEngine();
	}

	/**
	 * File suffix.
	 */

	public static String FILE_SUFFIX = ".js";

	/**
	 * Script type.
	 * 
	 * If the Java-Version is lower than 1.8, use rhino, or use nashorn.
	 */

	public static String SCRIPT_TYPE = Double.valueOf(SystemUtil
			.getJavaVersion().substring(0,
					SystemUtil.getJavaVersion().lastIndexOf(StringPool.DOT))) < 1.8 ? "JavaScript"
			: "nashorn";

	public String getFileSuffix() {
		return SCRIPT_TYPE;
	}

	public void setFileSuffix(String fileSuffix) {
		Script.FILE_SUFFIX = fileSuffix;
	}

	public String getScriptType() {
		return SCRIPT_TYPE;
	}

	public void setScriptType(String scriptType) {
		Script.SCRIPT_TYPE = scriptType;
	}

}