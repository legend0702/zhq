package cn.zhuhongqing.utils.dom;

import java.util.Map;

import org.w3c.dom.Node;

import cn.zhuhongqing.bean.BeanUtil;
import cn.zhuhongqing.utils.ReflectUtil;

public interface NodeParser {

	Map<String, Object> parse(Node node);

	default <T> T convert(Node node, Class<T> claz) {
		T bean = ReflectUtil.autoNewInstance(claz);
		return bean;
	}

}