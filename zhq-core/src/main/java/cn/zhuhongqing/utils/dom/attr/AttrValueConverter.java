package cn.zhuhongqing.utils.dom.attr;

public interface AttrValueConverter {

	default Object convert(String value, Class<?> targetType) {
		return value;
	}

}