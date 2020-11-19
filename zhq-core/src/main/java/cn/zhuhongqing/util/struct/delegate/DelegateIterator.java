package cn.zhuhongqing.util.struct.delegate;

import java.util.Iterator;

public abstract class DelegateIterator<E> extends DelegateObject implements Iterator<E> {

	/** Constructor for use by subclasses. */
	protected DelegateIterator() {}

	@Override
	protected abstract Iterator<E> delegate();

	@Override
	public boolean hasNext() {
		return delegate().hasNext();
	}

	@Override
	public E next() {
		return delegate().next();
	}

	@Override
	public void remove() {
		delegate().remove();
	}

}
