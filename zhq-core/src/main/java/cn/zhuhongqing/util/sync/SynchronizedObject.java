package cn.zhuhongqing.util.sync;

import java.io.Serializable;

import static cn.zhuhongqing.util.Assert.notNull;

abstract class SynchronizedObject<O> implements Serializable {

	private static final long serialVersionUID = 0;
	
	final O delegate;
    final Object mutex;

    SynchronizedObject(O delegate, Object mutex) {
      this.delegate = notNull(delegate);
      this.mutex = (mutex == null) ? this : mutex;
    }

    O delegate() {
      return delegate;
    }

    // No equals and hashCode; see ForwardingObject for details.

    @Override
    public String toString() {
      synchronized (mutex) {
        return delegate.toString();
      }
    }
}
