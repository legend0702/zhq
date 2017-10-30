package cn.zhuhongqing.utils.dom;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import cn.zhuhongqing.bean.BeanInfoUtil;
import cn.zhuhongqing.utils.ReflectUtil;
import cn.zhuhongqing.utils.generic.GenericUtil;
import lombok.Data;

@Data(staticConstructor = "of")
public class DocumentBeanConverterHole {

	private List<ElementEvent> names;

	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		Class<?> clazz = DocumentBeanConverterHole.class;
		Field field = ReflectUtil.getSupportedField(clazz, "names");
		Type fc = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
		if (fc instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) fc;
			Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0]; //
			if (genericClazz.getName().startsWith("java.lang") // 设置list的终止类型
					|| genericClazz.getName().startsWith("java.util.Date") || genericClazz.getName().startsWith("javax")
					|| genericClazz.getName().startsWith("com.sun") || genericClazz.getName().startsWith("sun")
					|| genericClazz.getName().startsWith("boolean") || genericClazz.getName().startsWith("double")
					|| genericClazz.getName().startsWith("int")) {
				System.out.println("AA");
				return;
			}
			// System.out.println(genericClazz);
			// 得到泛型里的class类型对象。
			System.out.println(genericClazz);
		}
	}

}