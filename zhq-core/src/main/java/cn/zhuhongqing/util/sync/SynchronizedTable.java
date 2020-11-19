package cn.zhuhongqing.util.sync;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import cn.zhuhongqing.util.MapUtils;
import cn.zhuhongqing.util.struct.collect.Table;

public class SynchronizedTable<R, C, V> extends SynchronizedObject<Table<R, C, V>> implements Table<R, C, V> {

	private static final long serialVersionUID = 0;
	
	SynchronizedTable(Table<R, C, V> delegate, Object mutex) {
		super(delegate, mutex);
	}
	
	@Override
    public boolean contains(Object rowKey,  Object columnKey) {
      synchronized (mutex) {
        return delegate().contains(rowKey, columnKey);
      }
    }

    @Override
    public boolean containsRow(Object rowKey) {
      synchronized (mutex) {
        return delegate().containsRow(rowKey);
      }
    }

    @Override
    public boolean containsColumn(Object columnKey) {
      synchronized (mutex) {
        return delegate().containsColumn(columnKey);
      }
    }

    @Override
    public boolean containsValue(Object value) {
      synchronized (mutex) {
        return delegate().containsValue(value);
      }
    }

    @Override
    public V get(Object rowKey,  Object columnKey) {
      synchronized (mutex) {
        return delegate().get(rowKey, columnKey);
      }
    }

    @Override
    public boolean isEmpty() {
      synchronized (mutex) {
        return delegate().isEmpty();
      }
    }

    @Override
    public int size() {
      synchronized (mutex) {
        return delegate().size();
      }
    }

    @Override
    public void clear() {
      synchronized (mutex) {
        delegate().clear();
      }
    }

    @Override
    public V put(R rowKey,  C columnKey,  V value) {
      synchronized (mutex) {
        return delegate().put(rowKey, columnKey, value);
      }
    }

    @Override
    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
      synchronized (mutex) {
        delegate().putAll(table);
      }
    }

    @Override
    public V remove(Object rowKey,  Object columnKey) {
      synchronized (mutex) {
        return delegate().remove(rowKey, columnKey);
      }
    }

    @Override
    public Map<C, V> row(R rowKey) {
      synchronized (mutex) {
        return Synchronized.map(delegate().row(rowKey), mutex);
      }
    }

    @Override
    public Map<R, V> column(C columnKey) {
      synchronized (mutex) {
        return Synchronized.map(delegate().column(columnKey), mutex);
      }
    }

    @Override
    public Set<Cell<R, C, V>> cellSet() {
      synchronized (mutex) {
        return Synchronized.set(delegate().cellSet(), mutex);
      }
    }

    @Override
    public Set<R> rowKeySet() {
      synchronized (mutex) {
        return Synchronized.set(delegate().rowKeySet(), mutex);
      }
    }

    @Override
    public Set<C> columnKeySet() {
      synchronized (mutex) {
        return Synchronized.set(delegate().columnKeySet(), mutex);
      }
    }

    @Override
    public Collection<V> values() {
      synchronized (mutex) {
        return Synchronized.collection(delegate().values(), mutex);
      }
    }

    @Override
    public Map<R, Map<C, V>> rowMap() {
      synchronized (mutex) {
    	Map<R, Map<C, V>> rowMap = delegate().rowMap();
    	MapUtils.transValues(rowMap, (map) -> {
			return Synchronized.map(map, mutex);
		});
        return Synchronized.map(rowMap, mutex);
      }
    }

    @Override
    public Map<C, Map<R, V>> columnMap() {
      synchronized (mutex) {
    	  Map<C, Map<R, V>> columnMap = delegate().columnMap();
    	  MapUtils.transValues(columnMap, (map) -> {
  			return Synchronized.map(map, mutex);
    	  });
          return Synchronized.map(columnMap, mutex);
      }
    }

    @Override
    public int hashCode() {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(obj);
      }
    }

}
