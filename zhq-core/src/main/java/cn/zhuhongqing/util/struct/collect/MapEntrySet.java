package cn.zhuhongqing.util.struct.collect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

import cn.zhuhongqing.util.CollectionUtils;
import cn.zhuhongqing.util.MapUtils;


abstract class MapEntrySet<K, V> extends ImprovedAbstractSet<Entry<K, V>> {
	
	abstract Map<K, V> map();

	@Override
	public int size() {
		return map().size();
	}

	@Override
	public void clear() {
		map().clear();
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof Entry) {
			Entry<?, ?> entry = (Entry<?, ?>) o;
			Object key = entry.getKey();
			V value = MapUtils.safeGet(map(), key);
			return Objects.equals(value, entry.getValue()) && (value != null || map().containsKey(key));
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return map().isEmpty();
	}

	@Override
	public boolean remove(Object o) {
		if (contains(o)) {
			Entry<?, ?> entry = (Entry<?, ?>) o;
			return map().keySet().remove(entry.getKey());
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		try {
			return super.removeAll(c);
		} catch (UnsupportedOperationException e) {
			// if the iterators don't support remove
			return CollectionUtils.safeRemove(this, c.iterator());
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		try {
			return super.retainAll(c);
		} catch (UnsupportedOperationException e) {
			// if the iterators don't support remove
			Set<Object> keys = new HashSet<>(c.size() * 2);
			for (Object o : c) {
				if (contains(o)) {
					Entry<?, ?> entry = (Entry<?, ?>) o;
					keys.add(entry.getKey());
				}
			}
			return map().keySet().retainAll(keys);
		}
	}
}