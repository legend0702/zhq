package cn.zhuhongqing.reflect;

import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.ref.SoftReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.zhuhongqing.exception.UncheckedException;

/**
 * <pre>
 * one Class's name-reference.
 * All reference are base on relation-mapping.
 * {@link #MEMBER_NAME_MAPPING} 
 * {@link #MEMBER_ANNOTATION_MAPPING}
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

public abstract class ClassReference<T extends Member> {

	/**
	 * Class's softReference.It may gc anytime!
	 */

	private SoftReference<Class<?>> ownClass;

	/**
	 * an emptySet for Members
	 */

	private final HashSet<MemberSoftReference> EMPTY_MEMBER_SET = new HashSet<MemberSoftReference>(
			0);

	/**
	 * key is Member's annotation-class,value is same annotationMembers.
	 */

	private final HashMap<Class<? extends Annotation>, HashSet<MemberSoftReference>> MEMBER_ANNOTATION_MAPPING = new HashMap<Class<? extends Annotation>, HashSet<MemberSoftReference>>();

	/**
	 * key is Member's name,value is same name Members.
	 */

	private final HashMap<String, HashSet<MemberSoftReference>> MEMBER_NAME_MAPPING = new HashMap<String, HashSet<MemberSoftReference>>();

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
		for (T t : members)
			add(t);
	}

	/**
	 * add a member to {@link #MEMBER_NAME_MAPPING} and
	 * {@link #MEMBER_ANNOTATION_MAPPING}.
	 * 
	 * @param t
	 * @return
	 */

	void add(T t) {
		String methodName = t.getName().intern();
		MemberSoftReference softMethod = new MemberSoftReference(t);
		HashSet<MemberSoftReference> members = MEMBER_NAME_MAPPING
				.get(methodName);
		if (members == null)
			members = new HashSet<MemberSoftReference>();
		members.add(softMethod);
		MEMBER_NAME_MAPPING.put(methodName, members);
		Annotation[] annotations = getAnnotations((AnnotatedElement) t);
		for (Annotation annotation : annotations) {
			HashSet<MemberSoftReference> aMembers = MEMBER_ANNOTATION_MAPPING
					.get(annotation.annotationType());
			if (aMembers == null) {
				aMembers = new HashSet<MemberSoftReference>();
			}
			aMembers.add(softMethod);
			MEMBER_ANNOTATION_MAPPING
					.put(annotation.annotationType(), aMembers);
		}
	}

	/**
	 * Get member's Annotations which your want pun into
	 * {@link #MEMBER_ANNOTATION_MAPPING}.
	 * 
	 * @param t
	 * @return
	 */

	Annotation[] getAnnotations(AnnotatedElement t) {
		return t.getDeclaredAnnotations();
	};

	/**
	 * get the same annotation members.
	 * 
	 * @param annotation
	 * @return
	 */

	public T[] getMember(Class<? extends Annotation> annotation) {
		return getMember(MEMBER_ANNOTATION_MAPPING, annotation);
	}

	/**
	 * get the same name members.
	 * 
	 * @param methodName
	 * @return
	 */

	public T[] getMember(String memberName) {
		return getMember(MEMBER_NAME_MAPPING, memberName);
	}

	@SuppressWarnings("unchecked")
	T[] getMember(HashMap<?, HashSet<MemberSoftReference>> mapping, Object key) {
		doCheckReference();
		HashSet<MemberSoftReference> meHashSet = mapping.get(key);
		if (meHashSet == null)
			meHashSet = EMPTY_MEMBER_SET;
		T[] returnMethods = (T[]) new Member[meHashSet.size()];
		Iterator<MemberSoftReference> itr = meHashSet.iterator();
		int index = 0;
		while (itr.hasNext()) {
			returnMethods[index] = itr.next().get();
			index++;
		}
		return returnMethods;
	}

	/**
	 * Get one Member base on annotation and relations
	 * 
	 * @param annotation
	 * @param relations
	 * @return
	 */

	public T getOneMember(Class<? extends Annotation> annotation,
			Class<?>... relations) {
		T[] members = getMember(annotation);
		return getOneMember(members, relations);
	}

	/**
	 * Get one Member base on memberName and relations
	 * 
	 * @param memberName
	 * @param relations
	 * @return
	 */

	public T getOneMember(String memberName, Class<?>... relations) {
		T[] members = getMember(memberName);
		return getOneMember(members, relations);
	}

	T getOneMember(T[] members, Class<?>... relations) {
		for (T member : members) {
			if (matchMember(member, relations))
				return member;
		}
		return null;
	}

	/**
	 * get all members about {@link #ownClass}
	 * 
	 * @return
	 */

	public Map<String, Set<T>> getAll() {
		doCheckReference();
		Map<String, Set<T>> memberMap = new HashMap<String, Set<T>>(
				MEMBER_NAME_MAPPING.size());
		Iterator<Entry<String, HashSet<MemberSoftReference>>> membersItr = MEMBER_NAME_MAPPING
				.entrySet().iterator();
		while (membersItr.hasNext()) {
			Entry<String, HashSet<MemberSoftReference>> entry = membersItr
					.next();
			HashSet<T> memberHashSet = new HashSet<T>(entry.getValue().size());
			Iterator<MemberSoftReference> softMethodItr = entry.getValue()
					.iterator();
			while (softMethodItr.hasNext())
				memberHashSet.add(softMethodItr.next().get());
			memberMap.put(entry.getKey().intern(), memberHashSet);
		}
		return memberMap;
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
	 */

	void doCheckReference() {
		if (!checkReference())
			throw new UncheckedException("ownClass had be reclaimed! ");
	}

	/**
	 * reinit this ClassMethods's instance.
	 * 
	 * @param clazz
	 */

	public void reinit(Class<?> clazz) {
		ownClass = new SoftReference<Class<?>>(clazz);
		MEMBER_NAME_MAPPING.clear();
		MEMBER_ANNOTATION_MAPPING.clear();
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
	 * match a member equals other one.
	 * 
	 * @param member
	 * @param memberName
	 * @param relations
	 * @return
	 */

	abstract boolean matchMember(T member, Class<?>[] relations);

	/**
	 * {@link MemberSoftReference#hashCode} init by this method.
	 * 
	 * @param referent
	 * @return
	 */

	abstract int createMemberSoftReferenceHashCode(T referent);

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
