package cn.zhuhongqing.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import cn.zhuhongqing.util.struct.collect.CaseInsensitiveMap;

public class MapUtils {

	public static <K, V> Map<K, V> of(K k, V v) {
		Map<K, V> map = new HashMap<>();
		map.put(k, v);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
		Map<K, V> map = new HashMap<>();
		map.put(k1, v1);
		map.put(k2, v2);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> map = new HashMap<>();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		return map;
	}
	
	public static <K, V> Entry<K, V> entry(K key, V val) {
		return new SimpleEntry<>(key, val);
	}
	
	public static <K, V> Entry<K, V> immutableEntry(K key, V val) {
		return new SimpleImmutableEntry<>(key, val);
	}

	public static <V> Map<String, V> caseInsensitiveMap(Map<String, V> map) {
		return CaseInsensitiveMap.of(map);
	}
	
	/**
	 * Creates an {@code LinkedHashMap<String, String>} from a {@code Properties} instance. 
	 * Properties normally derive from {@code Map<String, String>}, but they typically contain strings, which is awkward. 
	 * This method lets you get a plain-old-{@code Map} out of a {@code Properties}.
	 *
	 * @param properties a {@code Properties} object to be converted
	 * @return an immutable map containing all the entries in {@code properties}
	 * @throws ClassCastException   if any key in {@code Properties} is not a {@code String}
	 * @throws NullPointerException if any key or value in {@code Properties} is null
	 */
	public static Map<String, String> fromProperties(Properties properties) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();

		for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			map.put(key, properties.getProperty(key));
		}

		return map;
	}
	
	/**
	 * Returns a live {@link Map} whose keys are the contents of {@code set} and whose values are computed on demand using {@code valueFunction}. 
	 *
	 * <p>
	 * Specifically, for each {@code k} in the backing set, the returned map has an entry mapping {@code k} to {@code function.apply(k)}. 
	 * The {@code keySet}, {@code values}, and {@code entrySet} values of the returned map iterate in the same order as the backing set.
	 *
	 * <p>
	 * Modifications to the backing set are read through to the returned map. 
	 * The returned map supports removal operations if the backing set does. 
	 * Removal operations write through to the backing set. 
	 * The returned map does not support put operations.(Just suggest)
	 *
	 * <p>
	 * <b>Warning:</b> 
	 * If the function rejects {@code null}, caution is required to make sure the set does not contain {@code null}, 
	 * because the map cannot stop {@code null} from being added to the set.
	 *
	 * <p>
	 * <b>Warning:</b> 
	 * This method assumes that for any instance {@code k} of key type {@code K}, {@code k.equals(k2)} implies that {@code k2} is also of type {@code K}. 
	 * Using a key type for which this may not hold, such as {@code ArrayList}, may risk a {@code ClassCastException} when calling methods on the resulting map view.
	 *
	 */
	public static <K, V> Map<K, V> toMap(Set<K> set, Function<? super K, V> valueFunction) {
		LinkedHashMap<K, V> map = new LinkedHashMap<>();
		for (K key : set) 
			map.put(key, valueFunction.apply(key));
		return map;
	}
	
	/**
	 * Returns an immutable map whose keys are the distinct elements of {@code keys} and whose value for each key was computed by {@code valueFunction}. 
	 * The map's iteration order is the order of the first appearance of each key in {@code keys}.
	 *
	 * <p>
	 * When there are multiple instances of a key in {@code keys}, 
	 * it is unspecified whether {@code valueFunction} will be applied to more than one instance of that key and, 
	 * if it is, which result will be mapped to that key in the returned map.
	 *
	 * @throws NullPointerException if any element of {@code keys} is {@code null}, or if {@code valueFunction} produces {@code null} for any key
	 */
	public static <K, V> Map<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
		// Using LHM instead of a builder so as not to fail on duplicate keys
		Map<K, V> map = new LinkedHashMap<>();
		while (keys.hasNext()) {
			K key = keys.next();
			map.put(key, valueFunction.apply(key));
		}
		return map;
	}
	
	public static <K, V> Map<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
		return toMap(keys.iterator(), valueFunction);
	}
	
	/**
	 * Delegates to {@link Map#get}. 
	 * Returns {@code null} on {@code ClassCastException} and {@code NullPointerException}.
	 */
	public static <V> V safeGet(Map<?, V> map, Object key) {
		try {
			return map.get(key);
		} catch (ClassCastException | NullPointerException e) {
			return null;
		}
	}

	/**
	 * Delegates to {@link Map#containsKey}. 
	 * Returns {@code false} on {@code ClassCastException} and {@code NullPointerException}.
	 */
	public static boolean safeContainsKey(Map<?, ?> map, Object key) {
		try {
			return map.containsKey(key);
		} catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}

	/**
	 * Delegates to {@link Map#remove}. 
	 * Returns {@code null} on {@code ClassCastException} and {@code NullPointerException}.
	 */
	public static <V> V safeRemove(Map<?, V> map, Object key) {
		try {
			return map.remove(key);
		} catch (ClassCastException | NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Delegates to {@link Map#remove}. 
	 */
	public static void safeRemoveAll(Map<?, ?> map, Object ...keys) {
		for (Object key : keys) {
			safeRemove(map, key);
		}
	}
	

	/**
	 * Delegates to {@link Map#remove}. 
	 */
	public static void safeRemoveAll(Map<?, ?> map, Collection<?> keys) {
		for (Object key : keys) {
			safeRemove(map, key);
		}
	}
	
	/**
	 * It will return a new LinkedHashMap.
	 */
	public static <K, V, R> Map<K, R> transValues(Map<K, V> map, Function<V, R> valueTrans) {
		LinkedHashMap<K, R> newMap = new LinkedHashMap<K, R>();
		for (K key : map.keySet()) {
			R val = valueTrans.apply(map.get(key));
			newMap.put(key, val);
		}
		return newMap;
	}
	
	/**
	 * It will return a new LinkedHashMap.
	 */
	public static <K, V, R> Map<K, R> transValueByEntries(Map<K, V> map, Function<Entry<K, V>, R> entryTrans) {
		LinkedHashMap<K, R> newMap = new LinkedHashMap<K, R>();
		for (Entry<K, V> entry : map.entrySet()) {
			R val = entryTrans.apply(entry);
			newMap.put(entry.getKey(), val);
		}
		return newMap;
	}
	
	/**
	 * It will update origin map.
	 * 
	 * {@code keyPredicate.test()} return false will be filtered. 
	 */
	public static <K, V> Map<K, V> filterByKeys(Map<K, V> unfiltered, Predicate<K> keyPredicate) {
		for (K key : unfiltered.keySet()) {
			if (keyPredicate.test(key)) 
				continue;
			unfiltered.remove(key);
		}
		return unfiltered;
	}
	
	/**
	 * It will update origin map.
	 * 
	 * {@code valuePredicate.test()} return false will be filtered. 
	 */
	public static <K, V> Map<K, V> filterByValues(Map<K, V> unfiltered, Predicate<V> valuePredicate) {
		for (K key : unfiltered.keySet()) {
			if (valuePredicate.test(unfiltered.get(key))) 
				continue;
			unfiltered.remove(key);
		}
		return unfiltered;
	}

	/**
	 * It will return a new LinkedHashMap.
	 * 
	 * {@code entryPredicate.test()} return false will be filtered.  
	 */
	public static <K, V> Map<K, V> filterByEntries(Map<K, V> unfiltered, Predicate<Entry<K, V>> entryPredicate) {
		LinkedHashMap<K, V> newMap = new LinkedHashMap<K, V>();
		for (Entry<K, V> entry : unfiltered.entrySet()) {
			if (entryPredicate.test(entry)) 
				continue;
			newMap.put(entry.getKey(), entry.getValue());
		}
		return newMap;
	}

	/** An implementation of {@link Map#toString}. */
	public static String toString(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder().append('{');
		boolean first = true;
		for (Entry<?, ?> entry : map.entrySet()) {
			if (!first) {
				sb.append(", ");
			}
			first = false;
			sb.append(entry.getKey()).append('=').append(entry.getValue());
		}
		return sb.append('}').toString();
	}
	
}
