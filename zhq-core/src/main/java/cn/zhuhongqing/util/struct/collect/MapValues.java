package cn.zhuhongqing.util.struct.collect;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import cn.zhuhongqing.util.Assert;
import cn.zhuhongqing.util.IteratorUtils;

public class MapValues<K, V> extends AbstractCollection<V> {

	final Map<K, V> map;

	MapValues(Map<K, V> map) {
		this.map = Assert.notEmpty(map);
	}

	final Map<K, V> map() {
		return map;
	}

	@Override
	public Iterator<V> iterator() {
		return IteratorUtils.transValues(map().entrySet().iterator(), (e) -> {
			return e.getValue();
		});
	}

	@Override
	public void forEach(Consumer<? super V> action) {
		Assert.notNull(action);
		// avoids allocation of entries for those maps that generate fresh entries on iteration
		map.forEach((k, v) -> action.accept(v));
	}

	@Override
	public boolean remove(Object o) {
		try {
			return super.remove(o);
		} catch (UnsupportedOperationException e) {
			for (Entry<K, V> entry : map().entrySet()) {
				if (Objects.equals(o, entry.getValue())) {
					map().remove(entry.getKey());
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		try {
			return super.removeAll(Assert.notEmpty(c));
		} catch (UnsupportedOperationException e) {
			Set<K> toRemove = new HashSet<>();
			for (Entry<K, V> entry : map().entrySet()) {
				if (c.contains(entry.getValue())) {
					toRemove.add(entry.getKey());
				}
			}
			return map().keySet().removeAll(toRemove);
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		try {
			return super.retainAll(Assert.notEmpty(c));
		} catch (UnsupportedOperationException e) {
			Set<K> toRetain = new HashSet<>();
			for (Entry<K, V> entry : map().entrySet()) {
				if (c.contains(entry.getValue())) {
					toRetain.add(entry.getKey());
				}
			}
			return map().keySet().retainAll(toRetain);
		}
	}

	@Override
	public int size() {
		return map().size();
	}

	@Override
	public boolean isEmpty() {
		return map().isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return map().containsValue(o);
	}

	@Override
	public void clear() {
		map().clear();
	}

}
