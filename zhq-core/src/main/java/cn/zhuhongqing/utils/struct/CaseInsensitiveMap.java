package cn.zhuhongqing.utils.struct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveMap<V> implements Map<String, V> {
	private Map<String, V> map;

	public static <V> CaseInsensitiveMap<V> hashMap() {
		return of(new HashMap<>());
	}

	public static <V> CaseInsensitiveMap<V> LinkedHashMap() {
		return of(new java.util.LinkedHashMap<>());
	}

	public static <V> CaseInsensitiveMap<V> of(Map<String, V> map) {
		return new CaseInsensitiveMap<V>(map);
	}

	CaseInsensitiveMap(Map<String, V> map) {
		this.map = map;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return (key instanceof String && this.map.containsKey(convertKey((String) key)));
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		if (key instanceof String) {
			return this.map.get(convertKey((String) key));
		}
		return null;
	}

	@Override
	public V put(String key, V value) {
		if (key instanceof String) {
			return map.put(convertKey(key), value);
		}
		return null;
	}

	@Override
	public V remove(Object key) {
		if (key instanceof String) {
			return this.map.remove(convertKey((String) key));
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> m) {
		if (map.isEmpty()) {
			return;
		}
		for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, V>> entrySet() {
		return map.entrySet();
	}

	protected String convertKey(String key) {
		return key.toLowerCase();
	}

}
