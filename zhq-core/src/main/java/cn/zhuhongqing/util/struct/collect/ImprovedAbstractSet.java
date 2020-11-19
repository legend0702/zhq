package cn.zhuhongqing.util.struct.collect;

import java.util.AbstractSet;
import java.util.Collection;

import cn.zhuhongqing.util.CollectionUtils;

/**
 * {@link AbstractSet} substitute without the potentially-quadratic {@code removeAll} implementation.
 */
abstract class ImprovedAbstractSet<E> extends AbstractSet<E> {
	@Override
	public boolean removeAll(Collection<?> c) {
		return CollectionUtils.safeRemove(this, c);
	}

}