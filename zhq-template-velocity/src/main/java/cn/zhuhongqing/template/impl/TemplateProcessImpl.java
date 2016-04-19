package cn.zhuhongqing.template.impl;

import java.io.Reader;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;

import cn.zhuhongqing.template.TemplateProcess;
import cn.zhuhongqing.template.exception.TemplateException;
import cn.zhuhongqing.utils.bean.BeanUtil;

public class TemplateProcessImpl implements TemplateProcess {

	private VelocityEngine ve;

	public TemplateProcessImpl() {
		ve = new VelocityEngine();
		// 关闭错误日志输出
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,
				new NullLogChute());
	}

	@Override
	public void render(Reader reader, Writer writer, Object model)
			throws Exception {
		boolean flag = ve.evaluate(
				new VelocityContext(BeanUtil.beanToMap(model)), writer,
				this.toString(), reader);
		if (!flag) {
			throw new TemplateException("Render failed.See logs :(");
		}
	}

	public VelocityEngine getVelocityEngine() {
		return ve;
	}

}
