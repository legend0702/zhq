package cn.zhuhongqing.util.sync;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

class SynchronizedMap<K, V> extends SynchronizedObject<Map<K, V>> implements Map<K, V> {

	private static final long serialVersionUID = 0;
	
	transient Set<K> keySet;
    transient Collection<V> values;
    transient Set<Entry<K, V>> entrySet;
	
	SynchronizedMap(Map<K, V> delegate, Object mutex) {
		super(delegate, mutex);
	}
	
	@Override
    public void clear() {
      synchronized (mutex) {
        delegate().clear();
      }
    }

    @Override
    public boolean containsKey(Object key) {
      synchronized (mutex) {
        return delegate().containsKey(key);
      }
    }

    @Override
    public boolean containsValue(Object value) {
      synchronized (mutex) {
        return delegate().containsValue(value);
      }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
      synchronized (mutex) {
        if (entrySet == null) {
          entrySet = Synchronized.set(delegate().entrySet(), mutex);
        }
        return entrySet;
      }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
      synchronized (mutex) {
        delegate().forEach(action);
      }
    }

    @Override
    public V get(Object key) {
      synchronized (mutex) {
        return delegate().get(key);
      }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
      synchronized (mutex) {
        return delegate().getOrDefault(key, defaultValue);
      }
    }

    @Override
    public boolean isEmpty() {
      synchronized (mutex) {
        return delegate().isEmpty();
      }
    }

    @Override
    public Set<K> keySet() {
      synchronized (mutex) {
        if (keySet == null) {
          keySet = Synchronized.set(delegate().keySet(), mutex);
        }
        return keySet;
      }
    }

    @Override
    public V put(K key, V value) {
      synchronized (mutex) {
        return delegate().put(key, value);
      }
    }

    @Override
    public V putIfAbsent(K key, V value) {
      synchronized (mutex) {
        return delegate().putIfAbsent(key, value);
      }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
      synchronized (mutex) {
        return delegate().replace(key, oldValue, newValue);
      }
    }

    @Override
    public V replace(K key, V value) {
      synchronized (mutex) {
        return delegate().replace(key, value);
      }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
      synchronized (mutex) {
        return delegate().computeIfAbsent(key, mappingFunction);
      }
    }

    @Override
    public V computeIfPresent(
        K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
      synchronized (mutex) {
        return delegate().computeIfPresent(key, remappingFunction);
      }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
      synchronized (mutex) {
        return delegate().compute(key, remappingFunction);
      }
    }

    @Override
    public V merge(
        K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
      synchronized (mutex) {
        return delegate().merge(key, value, remappingFunction);
      }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
      synchronized (mutex) {
        delegate().putAll(map);
      }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
      synchronized (mutex) {
        delegate().replaceAll(function);
      }
    }

    @Override
    public V remove(Object key) {
      synchronized (mutex) {
        return delegate().remove(key);
      }
    }

    @Override
    public boolean remove(Object key, Object value) {
      synchronized (mutex) {
        return delegate().remove(key, value);
      }
    }

    @Override
    public int size() {
      synchronized (mutex) {
        return delegate().size();
      }
    }

    @Override
    public Collection<V> values() {
      synchronized (mutex) {
        if (values == null) {
          values = Synchronized.collection(delegate().values(), mutex);
        }
        return values;
      }
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }

    @Override
    public int hashCode() {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }

}