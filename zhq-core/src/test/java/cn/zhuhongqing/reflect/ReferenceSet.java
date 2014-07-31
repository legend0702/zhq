// Copyright (c) 2003-2013, Jodd Team (jodd.org). All Rights Reserved.

package cn.zhuhongqing.reflect;

/**
 * Reference set build over {@link jodd.util.ref.ReferenceMap}.
 */
public class ReferenceSet<E> extends SetMapAdapter<E> {

	public ReferenceSet(ReferenceType valueReferenceType) {
		super(new ReferenceMap<E, Object>(valueReferenceType,
				ReferenceType.STRONG));
	}

}
