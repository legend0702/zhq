package cn.zhuhongqing.script;

import java.io.InputStream;

import cn.zhuhongqing.Script;
import cn.zhuhongqing.exception.UncheckedException;
import cn.zhuhongqing.utils.GeneralUtil;

/**
 * Encoders/Decoders JSON in JavaScript.
 * 
 * @author HongQing.Zhu
 * @see https://github.com/douglascrockford/JSON-js
 */

public class ToJson {
	private static ScriptStore TO_JSON;
	private static InputStream _TO_JSON = ClassLoader
			.getSystemResourceAsStream("JSON2.js");
	static {
		cn.zhuhongqing.script.ScriptEngine factory = Script
				.createScriptEngine();
		try {
			TO_JSON = factory.analyze(_TO_JSON);
		} catch (ScriptException e) {
			throw new UncheckedException(e);
		}
	}

	public String toJson(Object value) {
		if (GeneralUtil.isNull(value))
			return null;
		if (String.class.isAssignableFrom(value.getClass()))
			return (String) value;
		try {
			return (String) TO_JSON.invocable.invokeMethod(
					TO_JSON.scriptEngine.get("JSON"), "stringify", value);
		} catch (NoSuchMethodException | javax.script.ScriptException e) {
			return null;
		}
	}

}
