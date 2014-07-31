package cn.zhuhongqing.script;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import cn.zhuhongqing.utils.StringPool;

/**
 * Analyzed script.
 * 
 * Use to get data and do function.
 * 
 * @author HongQing.Zhu
 */

public class ScriptStore {

	private static final ToJson TO_JSON = new ToJson();
	ScriptEngine scriptEngine;
	Invocable invocable;

	public ScriptStore(ScriptEngine script) {
		this.scriptEngine = script;
		this.invocable = (Invocable) script;
	}

	public String get(String key) {
		Object returnValue = null;
		if (!key.contains(StringPool.DOT)) {
			returnValue = scriptEngine.get(key);
		} else {
			try {
				returnValue = scriptEngine.eval(key);
			} catch (javax.script.ScriptException e) {
				return null;
			}
		}
		return TO_JSON.toJson(returnValue);
	}

	public String invoke(String methodName, Object... args) {
		try {
			return TO_JSON.toJson(invocable.invokeFunction(methodName, args));
		} catch (NoSuchMethodException | javax.script.ScriptException e) {
			return null;
		}
	}

}