package cn.zhuhongqing.utils.struct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapAttrbuteHole<K, V> implements AttributeHole<K, V> {

	private Map<K, V> hole;

	public MapAttrbuteHole() {
		this(new HashMap<>());
	}

	public MapAttrbuteHole(Map<K, V> instance) {
		hole = instance;
	}

	@Override
	public V setAttribute(K name, V value) {
		return hole.put(name, value);
	}

	@Override
	public V getAttribute(K name) {
		return hole.get(name);
	}

	@Override
	public V removeAttribute(K name) {
		return hole.remove(name);
	}

	@Override
	public boolean hasAttribute(K name) {
		return hole.containsKey(name);
	}

	@Override
	public Collection<K> attributeKeys() {
		return hole.keySet();
	}

	@Override
	public Collection<V> attributeValues() {
		return hole.values();
	}

	@Override
	public boolean isEmptyAttribute() {
		return hole.isEmpty();
	}

	@Override
	public String toString() {
		return hole.toString();
	}

}
