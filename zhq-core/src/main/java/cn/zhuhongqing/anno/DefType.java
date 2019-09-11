package cn.zhuhongqing.anno;

import cn.zhuhongqing.util.meta.Invokable;
import cn.zhuhongqing.util.meta.MetaData;

public enum DefType {

	CLASS_NAME, FIELD_NAME, METHOD_NAME, CLASS_TYPE, FIELD_TYPE, METHOD_RETURN_TYPE;

	public Object getDefValue(MetaData meta) {
		switch (this) {
		case CLASS_NAME:
			if (meta.isClass())
				return meta.getName();
			if (meta.isField() && meta.isMethod())
				return meta.getDeclaringClass().getName();
			break;
		case FIELD_NAME:
			if (meta.isField())
				return meta.getName();
			break;
		case METHOD_NAME:
			if (meta.isMethod())
				return meta.getName();
			break;
		case CLASS_TYPE:
			if (meta.isClass())
				return meta.getMetaType();
			if (meta.isField() && meta.isMethod())
				return meta.getDeclaringClass().getClass();
			break;
		case FIELD_TYPE:
			if (meta.isField())
				return meta.getMetaType();
			break;
		case METHOD_RETURN_TYPE:
			if (meta instanceof Invokable) {
				return ((Invokable) meta).getReturnType();
			}
			break;
		default:
			break;
		}
		return null;
	}

}
