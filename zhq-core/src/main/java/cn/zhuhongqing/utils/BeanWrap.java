package cn.zhuhongqing.utils;

import java.util.HashMap;
import java.util.Map;

import cn.zhuhongqing.bean.BeanUtil;

/**
 * 包装一个Java Bean 以不改变原始Java Bean结构的情况下 追加参数
 * 
 * 在特定的场景下需要使用
 *
 */

public class BeanWrap implements Cloneable {

	private Object origin;

	private Map<String, Object> exParams = new HashMap<String, Object>();

	public BeanWrap(Object origin) {
		this.origin = origin;
	}

	public Object get() {
		return origin;
	}

	public void putExParam(String key, Object val) {
		exParams.put(key, val);
	}

	public Object getExParam(String key) {
		return exParams.get(key);
	}

	public Object removeExParam(String key) {
		return exParams.remove(key);
	}

	public Map<String, Object> getExParams() {
		return exParams;
	}

	@Override
	public BeanWrap clone() {
		BeanWrap clone = new BeanWrap(origin);
		clone.getExParams().putAll(getExParams());
		return clone;
	}

	/**
	 * 合并{@link #origin}跟{@link #exParams},创建并返回一个新的Map对象.
	 */

	public Map<String, Object> merge() {
		Map<String, Object> mergeMap = new HashMap<String, Object>(exParams.size() + 8);
		mergeMap.putAll(BeanUtil.beanToMap(origin));
		mergeMap.putAll(exParams);
		return mergeMap;
	}

}
