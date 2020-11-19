package cn.zhuhongqing.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;


public class IteratorUtils {

	/**
	 * Returns the empty {@code Iterator}.
	 */
	// Casting to any type is safe since there are no actual elements.
	public static <T> Iterator<T> emptyIterator() {
		return Collections.emptyIterator();
	}
	
	/**
	 * Returns the number of elements remaining in {@code iterator}. The iterator
	 * will be left exhausted: its {@code hasNext()} method will return
	 * {@code false}.
	 */
	public static int size(Iterator<?> iterator) {
		int count = 0;
		while (iterator.hasNext()) {
			iterator.next();
			count++;
		}
		return count;
	}

	/** Returns {@code true} if {@code iterator} contains {@code element}. */
	public static boolean contains(Iterator<?> iterator, Object element) {
		if (element == null) {
			while (iterator.hasNext()) {
				if (iterator.next() == null) {
					return true;
				}
			}
		} else {
			while (iterator.hasNext()) {
				if (element.equals(iterator.next())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Traverses an iterator and removes every element that belongs to the provided collection. 
	 * The iterator will be left exhausted: its {@code hasNext()} method will return {@code false}.
	 *
	 * @param removeFrom       the iterator to (potentially) remove elements from
	 * @param elementsToRemove the elements to remove
	 * @return {@code true} if any element was removed from {@code iterator}
	 */
	public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
		boolean result = false;
		while (removeFrom.hasNext()) {
			if (elementsToRemove.contains(removeFrom.next())) {
				removeFrom.remove();
				result = true;
			}
		}
		return result;
	}

	/**
	 * Removes every element that satisfies the provided predicate from the iterator. 
	 * The iterator will be left exhausted: its {@code hasNext()} method will return {@code false}.
	 *
	 * @param removeFrom the iterator to (potentially) remove elements from
	 * @param predicate  a predicate that determines whether an element should be removed
	 * @return {@code true} if any elements were removed from the iterator
	 * @since 2.0
	 */
	public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
		boolean modified = false;
		while (removeFrom.hasNext()) {
			if (predicate.test(removeFrom.next())) {
				removeFrom.remove();
				modified = true;
			}
		}
		return modified;
	}

	/**
	 * Traverses an iterator and removes every element that does not belong to the provided collection. 
	 * The iterator will be left exhausted: its {@code hasNext()} method will return {@code false}.
	 *
	 * @param removeFrom       the iterator to (potentially) remove elements from
	 * @param elementsToRetain the elements to retain
	 * @return {@code true} if any element was removed from {@code iterator}
	 */
	public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
		boolean result = false;
		while (removeFrom.hasNext()) {
			if (!elementsToRetain.contains(removeFrom.next())) {
				removeFrom.remove();
				result = true;
			}
		}
		return result;
	}

	/**
	 * Determines whether two iterators contain equal elements in the same order.
	 * More specifically, this method returns {@code true} if {@code iterator1} and
	 * {@code iterator2} contain the same number of elements and every element of
	 * {@code iterator1} is equal to the corresponding element of {@code iterator2}.
	 *
	 * <p>
	 * Note that this will modify the supplied iterators, since they will have been
	 * advanced some number of elements forward.
	 */
	public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
		while (iterator1.hasNext()) {
			if (!iterator2.hasNext()) {
				return false;
			}
			Object o1 = iterator1.next();
			Object o2 = iterator2.next();
			if (!Objects.equals(o1, o2)) {
				return false;
			}
		}
		return !iterator2.hasNext();
	}

	/**
	 * Returns a string representation of {@code iterator}, with the format {@code [e1, e2, ..., en]}. 
	 * The iterator will be left exhausted: its {@code hasNext()} method will return {@code false}.
	 */
	public static String toString(Iterator<?> iterator) {
		StringBuilder sb = new StringBuilder().append('[');
		boolean first = true;
		while (iterator.hasNext()) {
			if (!first) {
				sb.append(", ");
			}
			first = false;
			sb.append(iterator.next());
		}
		return sb.append(']').toString();
	}

	/**
	 * Returns the single element contained in {@code iterator}.
	 *
	 * @throws NoSuchElementException   if the iterator is empty
	 * @throws IllegalArgumentException if the iterator contains multiple elements.
	 *                                  The state of the iterator is unspecified.
	 */
	public static <T> T getOnlyElement(Iterator<T> iterator) {
		T first = iterator.next();
		if (!iterator.hasNext()) {
			return first;
		}

		StringBuilder sb = new StringBuilder().append("expected one element but was: <").append(first);
		for (int i = 0; i < 4 && iterator.hasNext(); i++) {
			sb.append(", ").append(iterator.next());
		}
		if (iterator.hasNext()) {
			sb.append(", ...");
		}
		sb.append('>');

		throw new IllegalArgumentException(sb.toString());
	}

	/**
	 * Returns the single element contained in {@code iterator}, or {@code defaultValue} if the iterator is empty.
	 *
	 * @throws IllegalArgumentException if the iterator contains multiple elements.
	 *                                  The state of the iterator is unspecified.
	 */
	public static <T> T getOnlyElement(Iterator<? extends T> iterator, T defaultValue) {
		return iterator.hasNext() ? getOnlyElement(iterator) : defaultValue;
	}

	/**
	 * Copies an iterator's elements into a List. 
	 * The iterator will be left exhausted: its {@code hasNext()} method will return {@code false}.
	 *
	 * @param iterator the iterator to copy
	 * @return a newly-allocated list into which all the elements of the iterator have been copied
	 */
	public static <T> List<T> toList(Iterator<? extends T> iterator) {
		List<T> list = new ArrayList<T>();
		addAll(list, iterator);
		return list;
	}
	
	public static <T, K, V> Map<K, V> toMap(Iterator<? extends T> iterator, Function<T, Entry<K, V>> func) {
		Map<K, V> list = new HashMap<>();
		while (iterator.hasNext()) {
			Entry<K, V> entry = func.apply(iterator.next());
			list.put(entry.getKey(), entry.getValue());
		}
		return list;
	}
	
	public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
		return toList(iterator).toArray(ArraysUtils.emptyArray(type));
	}
	
	/**
	 * Adds all elements in {@code iterator} to {@code collection}. 
	 * The iterator will be left exhausted: its {@code hasNext()} method will return {@code false}.
	 *
	 * @return {@code true} if {@code collection} was modified as a result of this operation
	 */
	public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
		boolean wasModified = false;
		while (iterator.hasNext()) {
			wasModified |= addTo.add(iterator.next());
		}
		return wasModified;
	}

	/**
	 * Returns the number of elements in the specified iterator that equal the specified object. 
	 * The iterator will be left exhausted: its {@code hasNext()} method will return {@code false}.
	 *
	 */
	public static int frequency(Iterator<?> iterator, Object element) {
		int count = 0;
		while (contains(iterator, element)) {
			// Since it lives in the same class, we know contains gets to the element and then stops,
			// though that isn't currently publicly documented.
			count++;
		}
		return count;
	}
	
	/**
	 * Returns the index in {@code iterator} of the first element that satisfies the
	 * provided {@code predicate}, or {@code -1} if the Iterator has no such elements.
	 *
	 * <p>
	 * More formally, returns the lowest index {@code i} such that {@code predicate.apply(Iterators.get(iterator, i))} returns {@code true}, or {@code -1} if there is no such index.
	 *
	 * <p>
	 * If -1 is returned, the iterator will be left exhausted: its {@code hasNext()} method will return {@code false}. 
	 * Otherwise, the iterator will be set to the element which satisfies the {@code predicate}.
	 *
	 * @since 2.0
	 */
	public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
		for (int i = 0; iterator.hasNext(); i++) {
			T current = iterator.next();
			if (predicate.test(current)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns {@code true} if one or more elements returned by {@code iterator} satisfy the given predicate.
	 */
	public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
		return indexOf(iterator, predicate) != -1;
	}

	/**
	 * Returns {@code true} if every element returned by {@code iterator} satisfies the given predicate. 
	 * If {@code iterator} is empty, {@code true} is returned.
	 */
	public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
		while (iterator.hasNext()) {
			T element = iterator.next();
			if (!predicate.test(element)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the first element in {@code iterator} that satisfies the given predicate; 
	 * use this method only when such an element is known to exist. 
	 * If no such element is found, the iterator will be left exhausted: its {@code hasNext()} method will return {@code false}. 
	 * If it is possible that <i>no</i> element will match, use {@link #tryFind} or {@link #find(Iterator, Predicate, Object)} instead.
	 *
	 * @throws NoSuchElementException if no element in {@code iterator} matches the given predicate
	 */
	public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
		while (iterator.hasNext()) {
			T t = iterator.next();
			if (predicate.test(t)) {
				return t;
			}
		}
		throw new NoSuchElementException();
	}

	/**
	 * Returns the first element in {@code iterator} that satisfies the given
	 * predicate. If no such element is found, {@code defaultValue} will be returned
	 * from this method and the iterator will be left exhausted: its
	 * {@code hasNext()} method will return {@code false}. Note that this can
	 * usually be handled more naturally using
	 * {@code tryFind(iterator, predicate).or(defaultValue)}.
	 *
	 * @since 7.0
	 */
	public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, T defaultValue) {
		while (iterator.hasNext()) {
			T t = iterator.next();
			if (predicate.test(t)) {
				return t;
			}
		}
		return defaultValue;
	}
	
	/**
	 * Calls {@code next()} on {@code iterator}, either {@code numberToAdvance}
	 * times or until {@code hasNext()} returns {@code false}, whichever comes first.
	 *
	 * @return the number of elements the iterator was advanced
	 */
	public static int advance(Iterator<?> iterator, int numberToAdvance) {
		int i;
		for (i = 0; i < numberToAdvance && iterator.hasNext(); i++) {
			iterator.next();
		}
		return i;
	}

	/**
	 * Advances {@code iterator} {@code position + 1} times, returning the element at the {@code position}th position.
	 *
	 * @param position position of the element to return
	 * @return the element at the specified position in {@code iterator}
	 * @throws IndexOutOfBoundsException if {@code position} is negative or greater
	 *                                   than or equal to the number of elements
	 *                                   remaining in {@code iterator}
	 */
	public static <T> T get(Iterator<T> iterator, int position) {
		int skipped = advance(iterator, position);
		if (!iterator.hasNext()) {
			throw new IndexOutOfBoundsException("position (" + position
					+ ") must be less than the number of elements that remained (" + skipped + ")");
		}
		return iterator.next();
	}
	
	/**
	 * Advances {@code iterator} {@code position + 1} times, returning the element
	 * at the {@code position}th position or {@code defaultValue} otherwise.
	 *
	 * @param position     position of the element to return
	 * @param defaultValue the default value to return if the iterator is empty or
	 *                     if {@code position} is greater than the number of
	 *                     elements remaining in {@code iterator}
	 * @return the element at the specified position in {@code iterator} or
	 *         {@code defaultValue} if {@code iterator} produces fewer than
	 *         {@code position + 1} elements.
	 * @throws IndexOutOfBoundsException if {@code position} is negative
	 * @since 4.0
	 */
	public static <T> T get(Iterator<? extends T> iterator, int position, T defaultValue) {
		checkNonnegative(position);
		advance(iterator, position);
		return getNext(iterator, defaultValue);
	}

	static void checkNonnegative(int position) {
		if (position < 0) {
			throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
		}
	}

	/**
	 * Returns the next element in {@code iterator} or {@code defaultValue} if the iterator is empty. 
	 *
	 * @param defaultValue the default value to return if the iterator is empty
	 * @return the next element of {@code iterator} or the default value
	 * @since 7.0
	 */
	public static <T> T getNext(Iterator<? extends T> iterator, T defaultValue) {
		return iterator.hasNext() ? iterator.next() : defaultValue;
	}

	/**
	 * Advances {@code iterator} to the end, returning the last element.
	 *
	 * @return the last element of {@code iterator}
	 * @throws NoSuchElementException if the iterator is empty
	 */
	public static <T> T getLast(Iterator<T> iterator) {
		while (true) {
			T current = iterator.next();
			if (!iterator.hasNext()) {
				return current;
			}
		}
	}

	/**
	 * Advances {@code iterator} to the end, returning the last element or
	 * {@code defaultValue} if the iterator is empty.
	 *
	 * @param defaultValue the default value to return if the iterator is empty
	 * @return the last element of {@code iterator}
	 * @since 3.0
	 */
	public static <T> T getLast(Iterator<? extends T> iterator, T defaultValue) {
		return iterator.hasNext() ? getLast(iterator) : defaultValue;
	}

	
	/** Clears the iterator using its remove method. */
	public static void clear(Iterator<?> iterator) {
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}
	
	/**
	 * Adapts an {@code Iterator} to the {@code Enumeration} interface.
	 */
	public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
		return new Enumeration<T>() {
			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public T nextElement() {
				return iterator.next();
			}
		};
	}
	
	/**
	 * Adapts an {@code Enumeration} to the {@code Iterator} interface.
	 */
	public static <T> Iterator<T> asIterator(final Enumeration<T> enumeration) {
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return enumeration.hasMoreElements();
			}

			@Override
			public T next() {
				return enumeration.nextElement();
			}
		};
	}
	
	/**
	 * Deletes and returns the next value from the iterator, or returns {@code null}
	 * if there is no such value.
	 */
	public static <T> T pollNext(Iterator<T> iterator) {
		if (iterator.hasNext()) {
			T result = iterator.next();
			iterator.remove();
			return result;
		} else {
			return null;
		}
	}
	
	public static <V, R> Iterator<R> transValues(Iterator<V> iterator, Function<V, R> transFunc) {
		return new Iterator<R>() {
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public R next() {
				return transFunc.apply(iterator.next());
			}
		};
	}
	
}
