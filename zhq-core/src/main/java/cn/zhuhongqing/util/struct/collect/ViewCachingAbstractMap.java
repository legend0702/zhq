package cn.zhuhongqing.util.struct.collect;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;


public abstract class ViewCachingAbstractMap<K, V> extends AbstractMap<K, V> {

	/**
     * Creates the entry set to be returned by {@link #entrySet()}. This method is invoked at most
     * once on a given map, at the time when {@code entrySet} is first called.
     */
    abstract Set<Entry<K, V>> createEntrySet();

    private transient Set<Entry<K, V>> entrySet;

    @Override
    public Set<Entry<K, V>> entrySet() {
      Set<Entry<K, V>> result = entrySet;
      return (result == null) ? entrySet = createEntrySet() : result;
    }

    private transient Set<K> keySet;

    @Override
    public Set<K> keySet() {
      Set<K> result = keySet;
      return (result == null) ? keySet = createKeySet() : result;
    }

    Set<K> createKeySet() {
      return new MapKeySet<>(this);
    }

    private transient Collection<V> values;

    @Override
    public Collection<V> values() {
      Collection<V> result = values;
      return (result == null) ? values = createValues() : result;
    }

    Collection<V> createValues() {
      return new MapValues<>(this);
    }
	
}
