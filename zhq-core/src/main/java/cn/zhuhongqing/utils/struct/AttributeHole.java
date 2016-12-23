package cn.zhuhongqing.utils.struct;

import java.util.Collection;

public interface AttributeHole<K, V> {

	V setAttribute(K name, V value);

	V getAttribute(K name);

	V removeAttribute(K name);

	boolean hasAttribute(K name);

	Collection<K> attributeKeys();

	Collection<V> attributeValues();

	boolean isEmptyAttribute();
}