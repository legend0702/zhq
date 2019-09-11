package cn.zhuhongqing.util.struct;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.zhuhongqing.util.CollectionUtils;

public class MultiValueMapImpl<K, V> implements MultiValueMap<K, V> {

	private Map<K, List<V>> store;

	public MultiValueMapImpl() {
		this(new HashMap<K, List<V>>());
	}

	public MultiValueMapImpl(Map<K, List<V>> store) {
		this.store = store;
	}

	@Override
	public int size() {
		return getMap().size();
	}

	@Override
	public boolean isEmpty() {
		return getMap().isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return getMap().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return getMap().containsValue(value);
	}

	@Override
	public List<V> get(Object key) {
		return getMap().get(key);
	}

	@Override
	public List<V> put(K key, List<V> value) {
		return getMap().put(key, value);
	}

	@Override
	public List<V> remove(Object key) {
		return getMap().remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends List<V>> m) {
		getMap().putAll(m);
	}

	@Override
	public void clear() {
		getMap().clear();
	}

	@Override
	public Set<K> keySet() {
		return getMap().keySet();
	}

	@Override
	public Collection<List<V>> values() {
		return getMap().values();
	}

	@Override
	public Set<java.util.Map.Entry<K, List<V>>> entrySet() {
		return getMap().entrySet();
	}

	@Override
	public V getFirst(K key) {
		return getFirst(get(key));
	}

	@Override
	public void add(K key, V value) {
		List<V> values = get(key);
		if (values == null) {
			values = new LinkedList<V>();
			put(key, values);
		}
		values.add(value);
	}

	@Override
	public void set(K key, V value) {
		List<V> values = new LinkedList<V>();
		values.add(value);
		getMap().put(key, values);
	}

	@Override
	public void setAll(Map<K, V> values) {
		for (Entry<K, V> entry : values.entrySet()) {
			set(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Map<K, V> toFirstValueMap() {
		LinkedHashMap<K, V> firstValueMap = new LinkedHashMap<K, V>(size());
		for (Entry<K, List<V>> entry : entrySet()) {
			firstValueMap.put(entry.getKey(), getFirst(entry.getValue()));
		}
		return firstValueMap;
	}

	Map<K, List<V>> getMap() {
		return store;
	}

	private V getFirst(List<V> values) {
		return (CollectionUtils.isEmpty(values()) ? null : values.get(0));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiValueMapImpl<?, ?> other = (MultiValueMapImpl<?, ?>) obj;
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getMap().hashCode();
	}

	@Override
	public String toString() {
		return getMap().toString();
	}

}
