package cn.zhuhongqing.utils.dom.attr;

public class DefaultValueConverter implements AttrValueConverter {

	@Override
	public Object convert(String value, Class<?> type) {
		return value;
	}

	public static void main(String[] args) {
		System.out.println(int[].class);
	}

}