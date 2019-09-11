package cn.zhuhongqing.util.struct;

import java.util.Collection;

public interface AttrHole<K, V> {

	V setAttr(K name, V value);

	V getAttr(K name);

	V removeAttr(K name);

	boolean hasAttr(K name);

	Collection<K> attrKeys();

	Collection<V> attrValues();

	boolean isEmptyAttr();

}