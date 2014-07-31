package cn.zhuhongqing.script;

import java.io.File;
import java.io.InputStream;

/**
 * ScriptEngine.
 * 
 * It's used to load and analyze script.
 * 
 * {@link #load(File)} and {@link #load(InputStream)} is used to load script and
 * save it for all last use.
 * 
 * {@link #analyze(File)} and {@link #analyze(InputStream)} is used to analyze
 * script use once time,and return a {@link ScriptStore}.
 * 
 * 
 * @author HongQing.Zhu
 * @since 1.6
 */

public interface ScriptEngine {

	public void load(InputStream scriptInputStream) throws ScriptException;

	public void load(File scriptFile) throws ScriptException;

	public ScriptStore analyze(InputStream scriptInputStream)
			throws ScriptException;

	public ScriptStore analyze(File scriptFile) throws ScriptException;

}
