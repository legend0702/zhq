package cn.zhuhongqing.util.condition;

public interface KeyValueMatchCondition<K, V> {
	
	/**
	 * The {@code key} must by full-real-key.
	 */
	V[] getMatchingCondition(K key);

	default V getFirstMatchingCondition(K key) {
		V[] v = getMatchingCondition(key);
		if (v == null || v.length == 0)
			return null;
		return v[0];
	}
	
	boolean put(K key, V value);
	
	V remove(K key);

}
