package cn.zhuhongqing.anno;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

import cn.zhuhongqing.util.meta.MetaData;

public class AnnotationWrap {

	private Annotation anno;
	private ElementType eleType;
	private MetaData meta;

	/**
	 * 
	 * @param anno
	 * @param meta
	 *            The source of anno.
	 */

	public static AnnotationWrap of(Annotation anno, MetaData meta) {
		return new AnnotationWrap(anno, meta);
	}

	public AnnotationWrap(Annotation anno, MetaData meta) {
		this.anno = anno;
		this.meta = meta;
		eleType = AnnotationUtils.getElementType(meta);
	}

	public Class<? extends Annotation> getAnnotationType() {
		return anno.annotationType();
	}

	public Annotation getAnnotation() {
		return anno;
	}

	public AnnotationWrap setAnnotation(Annotation anno) {
		this.anno = anno;
		return this;
	}

	public ElementType getElementType() {
		return eleType;
	}

	public AnnotationWrap setElementType(ElementType elementType) {
		this.eleType = elementType;
		return this;
	}

	public MetaData getMetaData() {
		return meta;
	}

	public AnnotationWrap setMetaData(MetaData metaData) {
		this.meta = metaData;
		return this;
	}

}
