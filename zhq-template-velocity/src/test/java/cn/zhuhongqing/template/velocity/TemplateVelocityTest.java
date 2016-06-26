package cn.zhuhongqing.template.velocity;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import cn.zhuhongqing.template.TemplateProcess;
import cn.zhuhongqing.utils.SPIUtil;
import cn.zhuhongqing.utils.loader.ClassPathResourceLoader;

/**
 * Unit test for TemplateVelocity.
 */
public class TemplateVelocityTest {

	private TemplateProcess tp;

	@Before
	public void before() {
		tp = SPIUtil.load(TemplateProcess.class);
	}

	@Test
	public void velocity() throws Exception {
		HashMap<String, Object> model = new HashMap<String, Object>(1);
		model.put("w", "Velocity!");
		StringWriter sw = new StringWriter(1024);
		tp.render(new InputStreamReader(new ClassPathResourceLoader().loaderAsStream("velocityTemp")), sw, model);
		System.out.println(sw.toString());
	}

}
