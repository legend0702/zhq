package cn.zhuhongqing.anno;

import java.lang.annotation.Annotation;

import cn.zhuhongqing.util.meta.MetaData;

public interface AnnotationParserListener {

	public static AnnotationParserListener DEFAULT = new DefaultListener();

	Annotation[] onClass(MetaData meta);

	Annotation[] onField(MetaData meta);

	Annotation[] onMethod(MetaData meta);

	class DefaultListener implements AnnotationParserListener {

		@Override
		public Annotation[] onClass(MetaData meta) {
			return meta.getAnnotations();
		}

		@Override
		public Annotation[] onField(MetaData meta) {
			return meta.getAnnotations();
		}

		@Override
		public Annotation[] onMethod(MetaData meta) {
			return meta.getAnnotations();
		}

	}

}
