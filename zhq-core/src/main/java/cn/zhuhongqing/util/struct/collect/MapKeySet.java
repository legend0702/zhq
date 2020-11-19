package cn.zhuhongqing.util.struct.collect;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import cn.zhuhongqing.util.Assert;
import cn.zhuhongqing.util.IteratorUtils;

public class MapKeySet<K, V> extends ImprovedAbstractSet<K> {

	final Map<K, V> map;

	MapKeySet(Map<K, V> map) {
		this.map = Assert.notEmpty(map);
	}

	Map<K, V> map() {
		return map;
	}

	@Override
	public Iterator<K> iterator() {
		return IteratorUtils.transValues(map().entrySet().iterator(), (e) -> {
			return e.getKey();
		});
	}

	@Override
	public void forEach(Consumer<? super K> action) {
		Assert.notNull(action);
		// avoids entry allocation for those maps that allocate entries on iteration
		map.forEach((k, v) -> action.accept(k));
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
		return map().containsKey(o);
	}

	@Override
	public boolean remove(Object o) {
		if (contains(o)) {
			map().remove(o);
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		map().clear();
	}
}
