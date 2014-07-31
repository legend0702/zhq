package cn.zhuhongqing.script;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import cn.zhuhongqing.Script;

/**
 * JavaScriptEngine.
 * 
 * Use to load and analyze JavaScript.
 * 
 * JavaScriptEngine's type is {@link Script#SCRIPT_TYPE}.
 * 
 * @author HongQing.Zhu
 * @since 1.6
 */

public class JavaScriptEngine implements cn.zhuhongqing.script.ScriptEngine {

	private static ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
	private ScriptContext SCRIPT_CONTEXT = new SimpleScriptContext();

	public JavaScriptEngine() {
		SCRIPT_CONTEXT.setBindings(new SimpleBindings(),
				ScriptContext.GLOBAL_SCOPE);
	}

	private javax.script.ScriptEngine createScriptEngine() {
		javax.script.ScriptEngine engine = SCRIPT_ENGINE_MANAGER
				.getEngineByName(Script.SCRIPT_TYPE);
		engine.setContext(SCRIPT_CONTEXT);
		return engine;
	}

	private javax.script.ScriptEngine createScriptEngine(InputStream inputStream)
			throws ScriptException {
		javax.script.ScriptEngine engine = createScriptEngine();
		try {
			engine.eval(new InputStreamReader(inputStream));
		} catch (javax.script.ScriptException e) {
			throw new ScriptException(e);
		}
		return engine;
	}

	@Override
	public void load(InputStream scriptInputStream)
			throws cn.zhuhongqing.script.ScriptException {
		javax.script.ScriptEngine scriptEngine = createScriptEngine();
		try {
			scriptEngine.eval(new InputStreamReader(scriptInputStream),
					SCRIPT_CONTEXT.getBindings(ScriptContext.GLOBAL_SCOPE));
		} catch (javax.script.ScriptException e) {
			throw new ScriptException(e);
		}
	}

	@Override
	public void load(File scriptFile)
			throws cn.zhuhongqing.script.ScriptException {
		try {
			load(new FileInputStream(scriptFile));
		} catch (FileNotFoundException e) {
			throw new ScriptException(e);
		}
	}

	@Override
	public ScriptStore analyze(InputStream scriptInputStream)
			throws ScriptException {
		return new ScriptStore(createScriptEngine(scriptInputStream));
	}

	@Override
	public ScriptStore analyze(File scriptFile) throws ScriptException {
		try {
			return new ScriptStore(createScriptEngine(new FileInputStream(
					scriptFile)));
		} catch (FileNotFoundException e) {
			throw new ScriptException(e);
		}
	}

}
