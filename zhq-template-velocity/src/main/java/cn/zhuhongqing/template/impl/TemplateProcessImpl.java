package cn.zhuhongqing.template.impl;

import java.io.Reader;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cn.zhuhongqing.template.TemplateProcess;
import cn.zhuhongqing.template.exception.TemplateException;
import cn.zhuhongqing.utils.bean.BeanUtil;

public class TemplateProcessImpl implements TemplateProcess {

	@Override
	public void render(Reader reader, Writer writer, Object model)
			throws Exception {
		VelocityEngine ve = new VelocityEngine();
		boolean flag = ve.evaluate(
				new VelocityContext(BeanUtil.beanToMap(model)), writer,
				"Velocity", reader);
		if (!flag) {
			throw new TemplateException("It's failed.See console log :(");
		}
	}
}
