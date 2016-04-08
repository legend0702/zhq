package cn.zhuhongqing.template.velocity;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import cn.zhuhongqing.template.TemplateProcess;
import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.factory.SingleImplementFacadeFactory;

/**
 * Unit test for TemplateVelocity.
 */
public class TemplateVelocityTest {

	private TemplateProcess tp;

	@Before
	public void before() {
		tp = SingleImplementFacadeFactory
				.getSingleImplement(TemplateProcess.class);
	}

	@Test
	public void velocity() throws Exception {
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("w", "Velocity!");
		StringWriter sw = new StringWriter();
		tp.render(new InputStreamReader(ClassUtil.getDefaultClassLoader()
				.getResourceAsStream("velocityTemp")), sw, model);
		System.out.println(sw.toString());
	}

}
