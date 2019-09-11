package cn.zhuhongqing.util.struct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapAttrHole<K, V> implements AttrHole<K, V> {

	private Map<K, V> hole;

	public MapAttrHole() {
		this(new HashMap<>());
	}

	public MapAttrHole(Map<K, V> instance) {
		hole = instance;
	}

	@Override
	public V setAttr(K name, V value) {
		return hole.put(name, value);
	}

	@Override
	public V getAttr(K name) {
		return hole.get(name);
	}

	@Override
	public V removeAttr(K name) {
		return hole.remove(name);
	}

	@Override
	public boolean hasAttr(K name) {
		return hole.containsKey(name);
	}

	@Override
	public Collection<K> attrKeys() {
		return hole.keySet();
	}

	@Override
	public Collection<V> attrValues() {
		return hole.values();
	}

	@Override
	public boolean isEmptyAttr() {
		return hole.isEmpty();
	}

	@Override
	public String toString() {
		return hole.toString();
	}

}
