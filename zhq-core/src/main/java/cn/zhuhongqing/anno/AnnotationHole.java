package cn.zhuhongqing.anno;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import cn.zhuhongqing.util.CollectionUtils;
import cn.zhuhongqing.util.meta.MetaData;

public class AnnotationHole {

	private HashMap<Annotation, AnnotationWrap> all = new HashMap<>();
	private HashMap<Class<?>, Collection<AnnotationWrap>> hole = new HashMap<>();

	public static AnnotationHole of(MetaData meta, Annotation... annos) {
		return new AnnotationHole().add(meta, annos);
	}

	AnnotationHole() {
	};

	public boolean isEmpty() {
		return all.isEmpty();
	}

	public boolean isSingleType() {
		return 1 == hole.size();
	}

	public AnnotationHole add(MetaData meta, Annotation[] annos) {
		for (Annotation anno : annos) {
			add(meta, anno);
		}
		return this;
	}

	public AnnotationHole add(MetaData meta, Annotation anno) {
		Class<? extends Annotation> annoClass = anno.annotationType();
		if (AnnotationUtils.isRepeatable(annoClass)) {
			add(meta, AnnotationUtils.getRepeatableValues(anno));
			return this;
		}
		Collection<AnnotationWrap> annos = getAnnotationWraps(annoClass);
		if (CollectionUtils.isEmpty(annos)) {
			annos = new ArrayList<>();
			hole.put(annoClass, annos);
		}
		AnnotationWrap wrap = AnnotationWrap.of(anno, meta);
		annos.add(wrap);
		all.put(anno, wrap);
		return this;
	}

	public AnnotationHole remove(Annotation anno) {
		Collection<AnnotationWrap> annos = getAnnotationWraps(anno.annotationType());
		if (CollectionUtils.isEmpty(annos))
			return this;
		annos.remove(all.remove(anno));
		return this;
	}

	public AnnotationHole remove(Class<? extends Annotation> annoClass) {
		Collection<AnnotationWrap> annos = getAnnotationWraps(annoClass);
		if (CollectionUtils.isEmpty(annos))
			return this;
		for (AnnotationWrap wrap : hole.remove(annoClass)) {
			all.remove(wrap.getAnnotation());
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Annotation> Collection<T> getAnnotations(Class<T> annoClass) {
		Collection<AnnotationWrap> wraps = hole.get(annoClass);
		ArrayList<T> annos = new ArrayList<>(wraps.size());
		for (AnnotationWrap wrap : wraps) {
			annos.add((T) wrap.getAnnotation());
		}
		return annos;
	}

	public Collection<AnnotationWrap> getAnnotationWraps(Class<? extends Annotation> annoClass) {
		return hole.get(annoClass);
	}

	public Collection<Annotation> getAnnotations() {
		return all.keySet();
	}

	@SuppressWarnings("unchecked")
	public <T extends Annotation> Collection<T> toAnnotations(Class<T> annoClass) {
		// isSingleType must true
		return (Collection<T>) all.keySet();
	}

	public Collection<AnnotationWrap> getAnnotationWraps() {
		return all.values();
	}

}