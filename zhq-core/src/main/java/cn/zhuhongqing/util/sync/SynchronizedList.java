package cn.zhuhongqing.util.sync;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;

class SynchronizedList<E> extends SynchronizedCollection<List<E>, E> implements List<E> {

	private static final long serialVersionUID = 0;
	
	SynchronizedList(List<E> delegate, Object mutex) {
		super(delegate, mutex);
	}
	
	@Override
    public void add(int index, E element) {
      synchronized (mutex) {
        delegate().add(index, element);
      }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
      synchronized (mutex) {
        return delegate().addAll(index, c);
      }
    }

    @Override
    public E get(int index) {
      synchronized (mutex) {
        return delegate().get(index);
      }
    }

    @Override
    public int indexOf(Object o) {
      synchronized (mutex) {
        return delegate().indexOf(o);
      }
    }

    @Override
    public int lastIndexOf(Object o) {
      synchronized (mutex) {
        return delegate().lastIndexOf(o);
      }
    }

    @Override
    public ListIterator<E> listIterator() {
      return delegate().listIterator(); // manually synchronized
    }

    @Override
    public ListIterator<E> listIterator(int index) {
      return delegate().listIterator(index); // manually synchronized
    }

    @Override
    public E remove(int index) {
      synchronized (mutex) {
        return delegate().remove(index);
      }
    }

    @Override
    public E set(int index, E element) {
      synchronized (mutex) {
        return delegate().set(index, element);
      }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
      synchronized (mutex) {
        delegate().replaceAll(operator);
      }
    }

    @Override
    public void sort(Comparator<? super E> c) {
      synchronized (mutex) {
        delegate().sort(c);
      }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
      synchronized (mutex) {
        return Synchronized.list(delegate().subList(fromIndex, toIndex), mutex);
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