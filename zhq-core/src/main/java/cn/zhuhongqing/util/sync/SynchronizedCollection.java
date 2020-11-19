package cn.zhuhongqing.util.sync;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

class SynchronizedCollection<C extends Collection<E>, E> extends SynchronizedObject<C> implements Collection<E> {

	private static final long serialVersionUID = 0;
	
	SynchronizedCollection(C delegate, Object mutex) {
		super(delegate, mutex);
	}

    @Override
    public boolean add(E e) {
      synchronized (mutex) {
        return delegate().add(e);
      }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
      synchronized (mutex) {
        return delegate().addAll(c);
      }
    }

    @Override
    public void clear() {
      synchronized (mutex) {
        delegate().clear();
      }
    }

    @Override
    public boolean contains(Object o) {
      synchronized (mutex) {
        return delegate().contains(o);
      }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      synchronized (mutex) {
        return delegate().containsAll(c);
      }
    }

    @Override
    public boolean isEmpty() {
      synchronized (mutex) {
        return delegate().isEmpty();
      }
    }

    @Override
    public Iterator<E> iterator() {
      return delegate().iterator(); // manually synchronized
    }

    @Override
    public Spliterator<E> spliterator() {
      synchronized (mutex) {
        return delegate().spliterator();
      }
    }

    @Override
    public Stream<E> stream() {
      synchronized (mutex) {
        return delegate().stream();
      }
    }

    @Override
    public Stream<E> parallelStream() {
      synchronized (mutex) {
        return delegate().parallelStream();
      }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
      synchronized (mutex) {
        delegate().forEach(action);
      }
    }

    @Override
    public boolean remove(Object o) {
      synchronized (mutex) {
        return delegate().remove(o);
      }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      synchronized (mutex) {
        return delegate().removeAll(c);
      }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      synchronized (mutex) {
        return delegate().retainAll(c);
      }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
      synchronized (mutex) {
        return delegate().removeIf(filter);
      }
    }

    @Override
    public int size() {
      synchronized (mutex) {
        return delegate().size();
      }
    }

    @Override
    public Object[] toArray() {
      synchronized (mutex) {
        return delegate().toArray();
      }
    }

    @Override
    public <T> T[] toArray(T[] a) {
      synchronized (mutex) {
        return delegate().toArray(a);
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