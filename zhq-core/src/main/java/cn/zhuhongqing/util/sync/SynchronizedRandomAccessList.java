package cn.zhuhongqing.util.sync;

import java.util.List;
import java.util.RandomAccess;

class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess {

	private static final long serialVersionUID = 0;

	SynchronizedRandomAccessList(List<E> delegate, Object mutex) {
		super(delegate, mutex);
	}

}
