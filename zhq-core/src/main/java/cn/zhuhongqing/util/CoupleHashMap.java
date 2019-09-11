package cn.zhuhongqing.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 用Value取Key的HashMap
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 * @param <K>
 * @param <V>
 */

public class CoupleHashMap<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = 1L;

	HashMap<V, K> valueKey;

	public CoupleHashMap() {
		super();
		valueKey = new HashMap<V, K>();
	}

	public CoupleHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		valueKey = new HashMap<V, K>(initialCapacity, loadFactor);
	}

	public CoupleHashMap(int initialCapacity) {
		super(initialCapacity);
		valueKey = new HashMap<V, K>(initialCapacity);
	}

	public CoupleHashMap(Map<? extends K, ? extends V> m) {
		super(m);
		valueKey = new HashMap<V, K>(m.size());
		putAllToVal(m);
	}

	public K getKey(V val) {
		return valueKey.get(val);
	}

	@Override
	public V put(K key, V value) {
		valueKey.put(value, key);
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
		putAllToVal(m);
	}

	@Override
	public V remove(Object key) {
		valueKey.remove(key);
		return super.remove(key);
	}

	@Override
	public void clear() {
		super.clear();
		valueKey.clear();
	}

	@Override
	public V putIfAbsent(K key, V value) {
		valueKey.putIfAbsent(value, key);
		return super.putIfAbsent(key, value);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		valueKey.remove(oldValue);
		valueKey.put(newValue, key);
		return super.replace(key, oldValue, newValue);
	}

	@Override
	public V replace(K key, V value) {
		valueKey.replace(value, key);
		return super.replace(key, value);
	}

	@Override
	public boolean remove(Object key, Object value) {
		valueKey.remove(value, key);
		return super.remove(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CoupleHashMap<K, V> clone() {
		CoupleHashMap<K, V> clone = (CoupleHashMap<K, V>) super.clone();
		clone.valueKey = (HashMap<V, K>) valueKey.clone();
		return clone;
	}

	void putAllToVal(Map<? extends K, ? extends V> m) {
		for (java.util.Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			valueKey.put(e.getValue(), e.getKey());
		}
	}

}
