package cn.zhuhongqing.util.condition;

import java.util.ArrayList;
import java.util.Map;

import cn.zhuhongqing.util.ArraysUtils;
import cn.zhuhongqing.util.CollectionUtils;
import cn.zhuhongqing.util.struct.collect.HashTable;
import cn.zhuhongqing.util.struct.collect.Table;

/**
 * 用于储存、取得符合条件的数据
 * 
 * 按照level区分对待不同pattern的情况 lv0代表顶级match(类似*)
 * 
 */

abstract class AbstractMatchCondition<K, V> implements KeyValueMatchCondition<K, V> {

	protected Table<Integer, K, V> keyValHole = HashTable.of();

	abstract Integer level(K key);
	
	abstract boolean match(K pattern, K key);
	
	@Override
	public boolean put(K key, V value) {
		int lv = level(key);
		if (lv < 0)
			return false;
		keyValHole.put(lv, key, value);
		return true;
	}
	
	@Override
	public V remove(K key) {
		return keyValHole.remove(level(key), key);
	}

	@Override
	public V[] getMatchingCondition(K key) {
		Integer lv = level(key);
		V val = keyValHole.get(lv, key);
		if (val != null)
			return ArraysUtils.to(val);
		ArrayList<V> vals = new ArrayList<>();
		while(lv >= 0) {
			Map<K, V> row = keyValHole.row(lv);
			lv--;
			if (CollectionUtils.isEmpty(row))
				continue;
			for (K k : row.keySet()) {
				if (match(k, key)) 
					vals.add(row.get(k));
			}
			if (!vals.isEmpty())
				break;
		}
		return CollectionUtils.toArray(vals);
	}
	
}
