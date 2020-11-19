package cn.zhuhongqing.util.struct.collect;

import java.util.Map.Entry;
import java.util.Objects;

abstract class AbstractMapEntry<K, V> implements Entry<K, V> {

	@Override
	public abstract K getKey();

	@Override
	public abstract V getValue();

	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Entry) {
			Entry<?, ?> that = (Entry<?, ?>) object;
			return Objects.equals(this.getKey(), that.getKey()) && Objects.equals(this.getValue(), that.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		K k = getKey();
		V v = getValue();
		return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
	}

	/**
	 * Returns a string representation of the form {@code {key}={value}}.
	 */
	@Override
	public String toString() {
		return getKey() + "=" + getValue();
	}
}