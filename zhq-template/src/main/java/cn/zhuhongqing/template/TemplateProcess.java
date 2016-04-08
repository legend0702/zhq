package cn.zhuhongqing.template;

import java.io.Reader;
import java.io.Writer;

/**
 * 模板执行接口
 * 
 * @author HongQing.Zhu
 *
 */

public interface TemplateProcess {

	/**
	 * 传入模板源 传入输出源 传入模型(JavaBean)<br/>
	 * 该方法会将模板以及模型进行结合 将结果输入到输出源中
	 * 
	 * @param reader
	 *            模板来源
	 * @param writer
	 *            输出对象
	 * @param model
	 *            模型
	 */

	public void render(Reader reader, Writer writer, Object model)
			throws Exception;

}
