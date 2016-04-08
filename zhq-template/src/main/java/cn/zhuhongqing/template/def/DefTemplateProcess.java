package cn.zhuhongqing.template.def;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import cn.zhuhongqing.template.TemplateProcess;

/**
 * 默认生成器
 * 
 * 无脑暴力直接用${key}进行替换
 * 
 * 默认1MB空间
 * 
 * @author HongQing.Zhu
 *
 */

@Deprecated
public class DefTemplateProcess implements TemplateProcess {

	@Override
	public void render(Reader reader, Writer writer, Object model)
			throws IOException {
		// String read = StreamUtil.toString(reader);
	}
}