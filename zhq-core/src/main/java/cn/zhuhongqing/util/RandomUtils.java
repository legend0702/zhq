package cn.zhuhongqing.util;

import java.util.Random;

public class RandomUtils {

	private static Random _random = new Random();

	public static Integer nextInt() {
		return nextInt(Integer.MAX_VALUE);
	}
	
	/**
	 * Returns 0 to (max-1).
	 */
	public static Integer nextInt(int max) {
		return _random.nextInt(max);
	}
	
	/**
	 * Returns min to (max-1).
	 */
	public static Integer nextInt(int min, int max) {
		return nextInt(max - min + 1) + min;
	}
	/**
	 * <p>
	 * Returns the next pseudorandom, uniformly distributed long value from the
	 * Math.random() sequence.
	 * </p>
	 * Identical to <code>nextLong(Long.MAX_VALUE)</code>
	 * <p>
	 * <b>N.B. All values are >= 0.<b>
	 * </p>
	 * 
	 * @return the random long
	 */
	public static Long nextLong() {
		return nextLong(Long.MAX_VALUE);
	}

	/**
	 * <p>
	 * Returns a pseudorandom, uniformly distributed long value between
	 * <code>0</code> (inclusive) and the specified value (exclusive), from the
	 * Math.random() sequence.
	 * </p>
	 *
	 * @param n the specified exclusive max-value
	 * @return the random long
	 * @throws IllegalArgumentException when <code>n &lt;= 0</code>
	 */
	public static Long nextLong(long n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Upper bound for nextInt must be positive");
		}
		// Code adapted from Harmony Random#nextInt(int)
		if ((n & -n) == n) { // n is power of 2
			// dropping lower order bits improves behaviour for low values of n
			return next63bits() >> 63 // drop all the bits
					- bitsRequired(n - 1); // except the ones we need
		}
		// Not a power of two
		long val;
		long bits;
		do { // reject some values to improve distribution
			bits = next63bits();
			val = bits % n;
		} while (bits - val + (n - 1) < 0);
		return val;
	}

	/**
	 * <p>
	 * Returns the next pseudorandom, uniformly distributed boolean value from the
	 * Math.random() sequence.
	 * </p>
	 *
	 * @return the random boolean
	 */
	public static boolean nextBoolean() {
		return _random.nextBoolean();
	}

	/**
	 * <p>
	 * Returns the next pseudorandom, uniformly distributed float value between
	 * <code>0.0</code> and <code>1.0</code> from the Math.random() sequence.
	 * </p>
	 *
	 * @return the random float
	 */
	public static Float nextFloat() {
		return _random.nextFloat();
	}

	/**
	 * <p>
	 * Synonymous to the Math.random() call.
	 * </p>
	 *
	 * @return the random double
	 */
	public static Double nextDouble() {
		return _random.nextDouble();
	}

	/**
	 * Get the next unsigned random long
	 * 
	 * @return unsigned random long
	 */
	private static long next63bits() {
		// drop the sign bit to leave 63 random bits
		return _random.nextLong() & 0x7fffffffffffffffL;
	}

	/**
	 * Count the number of bits required to represent a long number.
	 * 
	 * @param num long number
	 * @return number of bits required
	 */
	private static int bitsRequired(long num) {
		// Derived from Hacker's Delight, Figure 5-9
		long y = num; // for checking right bits
		int n = 0; // number of leading zeros found
		while (true) {
			// 64 = number of bits in a long
			if (num < 0) {
				return 64 - n; // no leading zeroes left
			}
			if (y == 0) {
				return n; // no bits left to check
			}
			n++;
			num = num << 1; // check leading bits
			y = y >> 1; // check trailing bits
		}
	}

}