package cn.zhuhongqing.utils.dom;

import org.w3c.dom.Node;

public class DocumentBeanConversion<T> {

	private Class<T> bean;

	public static <R> DocumentBeanConversion<R> of(Class<R> beanClass) {
		return new DocumentBeanConversion<R>(beanClass);
	}

	private DocumentBeanConversion(Class<T> beanClass) {
		this.bean = beanClass;
	}

	public static void main(String[] args) {
		DocumentBeanConversion<Object> a = DocumentBeanConversion.of(Object.class);
	}
	

}
