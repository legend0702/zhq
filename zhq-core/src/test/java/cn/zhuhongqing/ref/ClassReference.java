package cn.zhuhongqing.ref;

import java.lang.instrument.Instrumentation;
import java.lang.ref.SoftReference;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <pre>
 * one Class's name-reference.
 * All reference are base on relation-mapping {@link #MEMBER_HASH_MAPPING}.
 * 
 * If the class had be reclaimed(gc),pls do {@link #reinit(Class)}.
 * {@link #checkReference()} will tell you gc already happened.
 * </pre>
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 * @param <?>
 * 
 */

public abstract class ClassReference<T extends AccessibleObject & Member> {

	/**
	 * Class's softReference.It may gc anytime!
	 */

	SoftReference<Class<?>> ownClass;

	/**
	 * key is member's relation hash,value is member.
	 * 
	 * A value may has key more than one.
	 */

	final HashMap<Integer, Set<MemberSoftReference>> MEMBER_HASH_MAPPING = new HashMap<Integer, Set<MemberSoftReference>>();

	/**
	 * Instance a reference for one class
	 * 
	 * @param clazz
	 */

	public ClassReference(Class<?> clazz) {
		ownClass = new SoftReference<Class<?>>(clazz);
		init(clazz);
	}

	/**
	 * @see {@link #add(T)}
	 * 
	 * @param members
	 */

	void addAll(T[] members) {
		for (T member : members)
			add(member);
	}

	/**
	 * Add a member to {@link #MEMBER_HASH_MAPPING}.
	 * 
	 * Key is create by {@link #createMemberHashCode(Member)}.
	 * 
	 * @param t
	 * @return
	 */

	void add(T member) {
		Set<Integer> hashCodes = createMemberHashCode(member);
		MemberSoftReference softReference = new MemberSoftReference(member);
		for (Integer hashCode : hashCodes) {
			Set<MemberSoftReference> values = MEMBER_HASH_MAPPING.get(hashCode);
			if (values == null)
				values = new HashSet<MemberSoftReference>();
			values.add(softReference);
			MEMBER_HASH_MAPPING.put(hashCode, values);
		}
	}

	/**
	 * If you confirm args while find one member,it's ok. or not,pls be careful.
	 * :)
	 * 
	 * @param objs
	 * @return
	 * @throws ReferenceTimeOut
	 */

	public T getOneMember(Object... objs) throws ReferenceTimeOut {
		Set<T> members = getMember(objs);
		if (members == null || members.size() < 1)
			return null;
		return members.iterator().next();
	}

	/**
	 * Get a collect of member by args
	 * 
	 * @param objs
	 * @return
	 * @throws ReferenceTimeOut
	 */

	public Set<T> getMember(Object... objs) throws ReferenceTimeOut {
		doCheckReference();
		return getMember(MEMBER_HASH_MAPPING.get(createHashCode(objs)));
	}

	/**
	 * Get all members about {@link #ownClass}
	 * 
	 * @return
	 * @throws ReferenceTimeOut
	 */

	public Set<T> getAll() throws ReferenceTimeOut {
		doCheckReference();
		Set<T> memberSet = new HashSet<T>();
		Iterator<Entry<Integer, Set<MemberSoftReference>>> membersItr = MEMBER_HASH_MAPPING
				.entrySet().iterator();
		while (membersItr.hasNext()) {
			Entry<Integer, Set<MemberSoftReference>> entry = membersItr.next();
			Iterator<MemberSoftReference> softMethodItr = entry.getValue()
					.iterator();
			while (softMethodItr.hasNext())
				memberSet.add(softMethodItr.next().get());
		}
		return memberSet;
	}

	/**
	 * Get method form collect
	 * 
	 * @param memberSoftReferences
	 * @return
	 */

	Set<T> getMember(Set<MemberSoftReference> memberSoftReferences) {
		Set<T> memberSet = new HashSet<T>();
		if (memberSoftReferences == null)
			return memberSet;
		Iterator<MemberSoftReference> softMethodItr = memberSoftReferences
				.iterator();
		while (softMethodItr.hasNext())
			memberSet.add(softMethodItr.next().get());
		return memberSet;
	}

	/**
	 * If class had be reclaimed by
	 * {@link Instrumentation#retransformClasses(Class...)}, we have to "reinit"
	 * this class.
	 * 
	 * true is ok,false must "reinit".
	 * 
	 * @return
	 */

	public boolean checkReference() {
		return (ownClass.get() != null) && (!ownClass.isEnqueued());
	}

	/**
	 * check {@link #checkReference()}
	 * 
	 * If you use it single, the exception will occur.
	 * 
	 * @throws ReferenceTimeOut
	 */

	void doCheckReference() throws ReferenceTimeOut {
		if (checkReference())
			return;
		throw new ReferenceTimeOut("ownClass had be reclaimed! ");
	}

	/**
	 * reinit this ClassMethods's instance.
	 * 
	 * @param clazz
	 */

	public void reinit(Class<?> clazz) {
		ownClass = new SoftReference<Class<?>>(clazz);
		MEMBER_HASH_MAPPING.clear();
		init(clazz);
	}

	/**
	 * one {@link Class},one {@link ClassReference}.
	 */

	@Override
	public int hashCode() {
		return ownClass.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!getClass().equals(obj.getClass()))
			return false;
		if (hashCode() == obj.hashCode())
			return true;
		return false;
	}

	/**
	 * do some thing init. :)
	 * 
	 * @param clazz
	 * @return
	 */

	abstract void init(Class<?> clazz);

	/**
	 * {@link #MEMBER_HASH_MAPPING} key init by this method.
	 * 
	 * @param member
	 * @return
	 */

	abstract Set<Integer> createMemberHashCode(T member);

	/**
	 * {@link MemberSoftReference#hashCode} init by this method.
	 * 
	 * @param referent
	 * @return
	 */

	abstract int createMemberSoftReferenceHashCode(T referent);

	/**
	 * create hashCode by args.
	 * 
	 * @param objs
	 * @return
	 */

	int createHashCode(Object... objs) {
		StringBuffer sb = new StringBuffer();
		for (Object obj : objs)
			sb.append(obj);
		return sb.toString().hashCode();
	}

	/**
	 * a softReference for T.
	 * 
	 * must implement
	 * {@link ClassReference#createMemberSoftReferenceHashCode(Member)}.
	 * 
	 * use {@link #hashCode()} to equals.
	 * 
	 * abide by first come first served. :)
	 * 
	 * @param <T>
	 * 
	 */

	private class MemberSoftReference extends SoftReference<T> {

		int hashCode;

		MemberSoftReference(T referent) {
			super(referent);
			hashCode = createMemberSoftReferenceHashCode(referent);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (this == obj)
				return true;
			if (!getClass().equals(obj.getClass()))
				return false;
			if (this.hashCode() == obj.hashCode())
				return true;
			return false;
		}

		@Override
		public int hashCode() {
			return hashCode;
		}
	}

}
