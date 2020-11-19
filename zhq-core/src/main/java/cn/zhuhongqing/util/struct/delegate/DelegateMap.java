package cn.zhuhongqing.util.struct.delegate;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class DelegateMap<K, V> extends DelegateObject implements Map<K, V> {
	// TODO(lowasser): identify places where thread safety is actually lost

	/** Constructor for use by subclasses. */
	protected DelegateMap() {}

	@Override
	protected abstract Map<K, V> delegate();

	@Override
	public int size() {
		return delegate().size();
	}

	@Override
	public boolean isEmpty() {
		return delegate().isEmpty();
	}

	@Override
	public V remove(Object object) {
		return delegate().remove(object);
	}

	@Override
	public void clear() {
		delegate().clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return delegate().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return delegate().containsValue(value);
	}

	@Override
	public V get(Object key) {
		return delegate().get(key);
	}

	@Override
	public V put(K key, V value) {
		return delegate().put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		delegate().putAll(map);
	}

	@Override
	public Set<K> keySet() {
		return delegate().keySet();
	}

	@Override
	public Collection<V> values() {
		return delegate().values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return delegate().entrySet();
	}

	@Override
	public boolean equals(Object object) {
		return object == this || delegate().equals(object);
	}

	@Override
	public int hashCode() {
		return delegate().hashCode();
	}

}
