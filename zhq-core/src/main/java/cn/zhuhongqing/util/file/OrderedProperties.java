package cn.zhuhongqing.util.file;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 一个按照Prop文件key顺序的Prop对象
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */
public class OrderedProperties extends Properties {

	private static final long serialVersionUID = -4627607243846121965L;

	private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

	public Enumeration<Object> keys() {
		return Collections.<Object>enumeration(keys);
	}

	@Override
	public Object setProperty(String key, String value) {
		keys.add(key);
		return super.setProperty(key, value);
	}

	public Object put(Object key, Object value) {
		keys.add(key);
		return super.put(key, value);
	}

	public Set<Object> keySet() {
		return keys;
	}

	@Override
	public Object remove(Object key) {
		keys.remove(key);
		return super.remove(key);
	}

	public Set<String> stringPropertyNames() {
		Set<String> set = new LinkedHashSet<String>(keys.size());
		for (Object key : this.keys) {
			set.add((String) key);
		}
		return set;
	}
}