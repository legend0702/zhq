package cn.zhuhongqing.util.struct.collect;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

import cn.zhuhongqing.util.IteratorUtils;

abstract class IteratorAbstractMap<K, V> extends AbstractMap<K, V> {
	@Override
	public abstract int size();

	abstract Iterator<Entry<K, V>> entryIterator();

	Spliterator<Entry<K, V>> entrySpliterator() {
		return Spliterators.spliterator(entryIterator(), size(), Spliterator.SIZED | Spliterator.DISTINCT);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return new MapEntrySet<K, V>() {
			@Override
			Map<K, V> map() {
				return IteratorAbstractMap.this;
			}

			@Override
			public Iterator<Entry<K, V>> iterator() {
				return entryIterator();
			}

			@Override
			public Spliterator<Entry<K, V>> spliterator() {
				return entrySpliterator();
			}

			@Override
			public void forEach(Consumer<? super Entry<K, V>> action) {
				forEachEntry(action);
			}
		};
	}

	void forEachEntry(Consumer<? super Entry<K, V>> action) {
		entryIterator().forEachRemaining(action);
	}

	@Override
	public void clear() {
		IteratorUtils.clear(entryIterator());
	}
}
