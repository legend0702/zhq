package cn.zhuhongqing.util.sync;

import java.util.Set;

class SynchronizedSet<E> extends SynchronizedCollection<Set<E>, E> implements Set<E> {

	private static final long serialVersionUID = 0;
	
	SynchronizedSet(Set<E> delegate, Object mutex) {
		super(delegate, mutex);
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
