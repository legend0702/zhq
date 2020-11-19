package cn.zhuhongqing.util.struct.delegate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.zhuhongqing.util.Assert;

/**
 * A {@link Future} which forwards all its method calls to another future. Subclasses should
 * override one or more methods to modify the behavior of the backing future as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p>Most subclasses can just use {@link SimpleDelegateFuture}.
 *
 * @author Sven Mawson
 */
public abstract class DelegateFuture<V> extends DelegateObject implements Future<V> {
	/** Constructor for use by subclasses. */
	protected DelegateFuture() {}

	@Override
	protected abstract Future<? extends V> delegate();

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return delegate().cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return delegate().isCancelled();
	}

	@Override
	public boolean isDone() {
		return delegate().isDone();
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		return delegate().get();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return delegate().get(timeout, unit);
	}

	// TODO(cpovirk): Use standard Javadoc form for SimpleForwarding* class and
	// constructor
	/**
	 * A simplified version of {@link ForwardingFuture} where subclasses can pass in
	 * an already constructed {@link Future} as the delegate.
	 *
	 * @since 9.0
	 */
	public abstract static class SimpleDelegateFuture<V> extends DelegateFuture<V> {
		private final Future<V> delegate;

		protected SimpleDelegateFuture(Future<V> delegate) {
			this.delegate = Assert.notNull(delegate);
		}

		@Override
		protected final Future<V> delegate() {
			return delegate;
		}
	}

}
