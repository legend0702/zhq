package cn.zhuhongqing.util.struct.delegate;

import java.util.Collection;
import java.util.Iterator;

import cn.zhuhongqing.util.CollectionUtils;

/**
 * A collection which forwards all its method calls to another collection. Subclasses should
 * override one or more methods to modify the behavior of the backing collection as desired per the
 * <a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p><b>Warning:</b> The methods of {@code DelegateCollection} forward <b>indiscriminately</b> to
 * the methods of the delegate. For example, overriding {@link #add} alone <b>will not</b> change
 * the behavior of {@link #addAll}, which can lead to unexpected behavior. 
 * In this case, you should override {@code addAll} as well, either providing your own implementation.
 *
 * <p><b>{@code default} method warning:</b> This class does <i>not</i> forward calls to {@code default} methods. 
 * Instead, it inherits their default implementations. 
 * When those implementations invoke methods, they invoke methods on the {@code DelegateCollection}.
 *
 * <p>The {@code standard} methods are not guaranteed to be thread-safe, even when all of the methods that they depend on are thread-safe.
 *
 * @author Kevin Bourrillion
 * @author Louis Wasserman
 */

public abstract class DelegateCollection<E> extends DelegateObject implements Collection<E> {
	// TODO(lowasser): identify places where thread safety is actually lost

	/** Constructor for use by subclasses. */
	protected DelegateCollection() {}

	@Override
	protected abstract Collection<E> delegate();

	@Override
	public Iterator<E> iterator() {
		return delegate().iterator();
	}

	@Override
	public int size() {
		return delegate().size();
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return delegate().removeAll(collection);
	}

	@Override
	public boolean isEmpty() {
		return delegate().isEmpty();
	}

	@Override
	public boolean contains(Object object) {
		return delegate().contains(object);
	}

	
	@Override
	public boolean add(E element) {
		return delegate().add(element);
	}

	
	@Override
	public boolean remove(Object object) {
		return delegate().remove(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return delegate().containsAll(collection);
	}

	
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		return delegate().addAll(collection);
	}

	
	@Override
	public boolean retainAll(Collection<?> collection) {
		return delegate().retainAll(collection);
	}

	@Override
	public void clear() {
		delegate().clear();
	}

	@Override
	public Object[] toArray() {
		return delegate().toArray();
	}

	
	@Override
	public <T> T[] toArray(T[] array) {
		return delegate().toArray(array);
	}

	/**
	 * A sensible definition of {@link #toString} in terms of {@link #iterator}.
	 * If you override {@link #iterator}, you may wish to override {@link #toString} to delegate to this implementation.
	 *
	 */
	protected String standardToString() {
		return CollectionUtils.toString(this);
	}

}
