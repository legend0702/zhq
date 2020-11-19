package cn.zhuhongqing.util.struct.collect;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HashTable<R, C, V> extends StandardTable<R, C, V> {
	private static final long serialVersionUID = 0;

	private static class Factory<C, V> implements Supplier<Map<C, V>>, Serializable {
		private static final long serialVersionUID = 0;

		final int expectedSize;

		Factory(int expectedSize) {
			this.expectedSize = expectedSize;
		}

		@Override
		public Map<C, V> get() {
			return new LinkedHashMap<>(expectedSize);
		}

	}

	/** Creates an empty {@code HashTable}. */
	public static <R, C, V> HashTable<R, C, V> of() {
		return new HashTable<>(new LinkedHashMap<R, Map<C, V>>(), new Factory<C, V>(0));
	}

	/**
	 * Creates a {@code HashTable} with the same mappings as the specified table.
	 *
	 * @param table the table to copy
	 * @throws NullPointerException if any of the row keys, column keys, or values in {@code table} is null
	 */
	public static <R, C, V> HashTable<R, C, V> of(Table<? extends R, ? extends C, ? extends V> table) {
		HashTable<R, C, V> result = of();
		result.putAll(table);
		return result;
	}

	HashTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
		super(backingMap, factory);
	}

}
