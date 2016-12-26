package cn.zhuhongqing.bean;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;

import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.meta.MetaData;

public class DefaultBeanProperty extends AbstractObjectState implements BeanProperty {

	private AnnotatedElement element;
	private Member member;
	private MetaData meta;
	private Field field;

	<M extends AnnotatedElement & Member> DefaultBeanProperty(M element) {
		this.element = element;
		this.member = element;
	}

	DefaultBeanProperty(MetaData meta) {
		this.element = meta;
		this.member = meta;
		this.meta = meta;
	}

	DefaultBeanProperty(Field field) {
		this.element = field;
		this.member = field;
		this.field = field;
	}

	@Override
	public Member getMember() {
		return member;
	}

	@Override
	public AnnotatedElement getAnnotatedElement() {
		return element;
	}

	@Override
	Object getTarget() {
		return GeneralUtil.isNotNull(meta) ? meta : GeneralUtil.isNotNull(field) ? field : member;
	}

	@Override
	public Class<?> getMetaType() {
		return GeneralUtil.isNotNull(meta) ? meta.getMetaType()
				: GeneralUtil.isNotNull(field) ? field.getType() : member.getDeclaringClass();
	}

}
