package cn.zhuhongqing.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Some utilities for {@link Number}
 * 
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class NumberUtil {

	public static final BigInteger LONG_MIN = BigInteger
			.valueOf(Long.MIN_VALUE);

	public static final BigInteger LONG_MAX = BigInteger
			.valueOf(Long.MAX_VALUE);

	/**
	 * Convert {@link Number} to {@link String}.
	 * 
	 * Use String type to keep.
	 * 
	 * double 123456789.123456 == > String 123456789.123456
	 */

	public static String toString(Number number) {
		if (number instanceof Float) {
			return new BigDecimal(number.toString()).toString();
		}
		if (number instanceof Double) {
			return new BigDecimal(number.toString()).toString();
		}
		return number.toString();
	}

	// ---------------------------------------------------------------- judge
	/**
	 * <p>
	 * Checks whether the <code>String</code> contains only digit characters.
	 * </p>
	 * 
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str
	 *            the <code>String</code> to checkF
	 * @return <code>true</code> if str contains only unicode numeric
	 */
	public static boolean isDigits(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether the String a valid Java number.
	 * </p>
	 * 
	 * <p>
	 * Valid numbers include hexadecimal marked with the <code>0x</code>
	 * qualifier, scientific notation and numbers marked with a type qualifier
	 * (e.g. 123L).
	 * </p>
	 * 
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str
	 *            the <code>String</code> to check
	 * @return <code>true</code> if the string is a correctly formatted number
	 */
	public static boolean isNumber(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		char[] chars = str.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = (chars[0] == '-') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9')
							&& (chars[i] < 'a' || chars[i] > 'f')
							&& (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
				// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another
		// digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (!allowSigns
					&& (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				// not allowing L with an exponent
				return foundDigit && !hasExp;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't
		// pass
		return !allowSigns && foundDigit;
	}

	// ---------------------------------------------------------------- convert

	/**
	 * Convert the given number into an instance of the given target class.
	 * 
	 * @param number
	 *            the number to convert
	 * @param targetClass
	 *            the target class to convert to
	 * @return the converted number
	 * @throws IllegalArgumentException
	 *             if the target class is not supported (i.e. not a standard
	 *             Number subclass as included in the JDK)
	 * @see java.lang.Byte
	 * @see java.lang.Short
	 * @see java.lang.Integer
	 * @see java.lang.Long
	 * @see java.math.BigInteger
	 * @see java.lang.Float
	 * @see java.lang.Double
	 * @see java.math.BigDecimal
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T convertNumberToTargetClass(
			Number number, Class<T> targetClass)
			throws IllegalArgumentException {

		if (targetClass.isInstance(number)) {
			return (T) number;
		} else if (targetClass.equals(Byte.class)) {
			long value = number.longValue();
			if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return (T) new Byte(number.byteValue());
		} else if (targetClass.equals(Short.class)) {
			long value = number.longValue();
			if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return (T) new Short(number.shortValue());
		} else if (targetClass.equals(Integer.class)) {
			long value = number.longValue();
			if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return (T) new Integer(number.intValue());
		} else if (targetClass.equals(Long.class)) {
			BigInteger bigInt = null;
			if (number instanceof BigInteger) {
				bigInt = (BigInteger) number;
			} else if (number instanceof BigDecimal) {
				bigInt = ((BigDecimal) number).toBigInteger();
			}
			// Effectively analogous to JDK 8's BigInteger.longValueExact()
			if (bigInt != null
					&& (bigInt.compareTo(LONG_MIN) < 0 || bigInt
							.compareTo(LONG_MAX) > 0)) {
				raiseOverflowException(number, targetClass);
			}
			return (T) new Long(number.longValue());
		} else if (targetClass.equals(BigInteger.class)) {
			if (number instanceof BigDecimal) {
				// do not lose precision - use BigDecimal's own conversion
				return (T) ((BigDecimal) number).toBigInteger();
			} else {
				// original value is not a Big* number - use standard long
				// conversion
				return (T) BigInteger.valueOf(number.longValue());
			}
		} else if (targetClass.equals(Float.class)) {
			return (T) new Float(number.floatValue());
		} else if (targetClass.equals(Double.class)) {
			return (T) new Double(number.doubleValue());
		} else if (targetClass.equals(BigDecimal.class)) {
			// always use BigDecimal(String) here to avoid unpredictability of
			// BigDecimal(double)
			// (see BigDecimal javadoc for details)
			return (T) new BigDecimal(number.toString());
		} else {
			throw new IllegalArgumentException("Could not convert number ["
					+ number + "] of type [" + number.getClass().getName()
					+ "] to unknown target class [" + targetClass.getName()
					+ "]");
		}
	}

	/**
	 * Raise an overflow exception for the given number and target class.
	 * 
	 * @param number
	 *            the number we tried to convert
	 * @param targetClass
	 *            the target class we tried to convert to
	 */
	private static void raiseOverflowException(Number number,
			Class<?> targetClass) {
		throw new IllegalArgumentException("Could not convert number ["
				+ number + "] of type [" + number.getClass().getName()
				+ "] to target class [" + targetClass.getName() + "]: overflow");
	}

	// ---------------------------------------------------------------- parse

	/**
	 * Parse the given text into a number instance of the given target class,
	 * using the corresponding {@code decode} / {@code valueOf} methods.
	 * <p>
	 * Trims the input {@code String} before attempting to parse the number.
	 * Supports numbers in hex format (with leading "0x", "0X" or "#") as well.
	 * 
	 * @param text
	 *            the text to convert
	 * @param targetClass
	 *            the target class to parse into
	 * @return the parsed number
	 * @throws IllegalArgumentException
	 *             if the target class is not supported (i.e. not a standard
	 *             Number subclass as included in the JDK)
	 * @see Byte#decode
	 * @see Short#decode
	 * @see Integer#decode
	 * @see Long#decode
	 * @see #decodeBigInteger(String)
	 * @see Float#valueOf
	 * @see Double#valueOf
	 * @see java.math.BigDecimal#BigDecimal(String)
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T parseNumber(String text,
			Class<T> targetClass) {
		String trimmed = StringUtil.trimAllWhitespace(text);

		if (targetClass.equals(Byte.class)) {
			return (T) (isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte
					.valueOf(trimmed));
		} else if (targetClass.equals(Short.class)) {
			return (T) (isHexNumber(trimmed) ? Short.decode(trimmed) : Short
					.valueOf(trimmed));
		} else if (targetClass.equals(Integer.class)) {
			return (T) (isHexNumber(trimmed) ? Integer.decode(trimmed)
					: Integer.valueOf(trimmed));
		} else if (targetClass.equals(Long.class)) {
			return (T) (isHexNumber(trimmed) ? Long.decode(trimmed) : Long
					.valueOf(trimmed));
		} else if (targetClass.equals(BigInteger.class)) {
			return (T) (isHexNumber(trimmed) ? decodeBigInteger(trimmed)
					: new BigInteger(trimmed));
		} else if (targetClass.equals(Float.class)) {
			return (T) Float.valueOf(trimmed);
		} else if (targetClass.equals(Double.class)) {
			return (T) Double.valueOf(trimmed);
		} else if (targetClass.equals(BigDecimal.class)
				|| targetClass.equals(Number.class)) {
			return (T) new BigDecimal(trimmed);
		} else {
			throw new IllegalArgumentException("Cannot convert String [" + text
					+ "] to target class [" + targetClass.getName() + "]");
		}
	}

	/**
	 * Parse the given text into a number instance of the given target class,
	 * using the given NumberFormat. Trims the input {@code String} before
	 * attempting to parse the number.
	 * 
	 * @param text
	 *            the text to convert
	 * @param targetClass
	 *            the target class to parse into
	 * @param numberFormat
	 *            the NumberFormat to use for parsing (if {@code null}, this
	 *            method falls back to {@code parseNumber(String, Class)})
	 * @return the parsed number
	 * @throws IllegalArgumentException
	 *             if the target class is not supported (i.e. not a standard
	 *             Number subclass as included in the JDK)
	 * @see java.text.NumberFormat#parse
	 * @see #convertNumberToTargetClass
	 * @see #parseNumber(String, Class)
	 */
	public static <T extends Number> T parseNumber(String text,
			Class<T> targetClass, NumberFormat numberFormat) {
		if (numberFormat != null) {
			DecimalFormat decimalFormat = null;
			boolean resetBigDecimal = false;
			if (numberFormat instanceof DecimalFormat) {
				decimalFormat = (DecimalFormat) numberFormat;
				if (BigDecimal.class.equals(targetClass)
						&& !decimalFormat.isParseBigDecimal()) {
					decimalFormat.setParseBigDecimal(true);
					resetBigDecimal = true;
				}
			}
			try {
				Number number = numberFormat.parse(StringUtil
						.trimAllWhitespace(text));
				return convertNumberToTargetClass(number, targetClass);
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Could not parse number: "
						+ ex.getMessage());
			} finally {
				if (resetBigDecimal) {
					decimalFormat.setParseBigDecimal(false);
				}
			}
		} else {
			return parseNumber(text, targetClass);
		}
	}

	/**
	 * Determine whether the given value String indicates a hex number, i.e.
	 * needs to be passed into {@code Integer.decode} instead of
	 * {@code Integer.valueOf} (etc).
	 */
	private static boolean isHexNumber(String value) {
		int index = (value.startsWith("-") ? 1 : 0);
		return (value.startsWith("0x", index) || value.startsWith("0X", index) || value
				.startsWith("#", index));
	}

	/**
	 * Decode a {@link java.math.BigInteger} from a {@link String} value.
	 * Supports decimal, hex and octal notation.
	 * 
	 * @see BigInteger#BigInteger(String, int)
	 */
	private static BigInteger decodeBigInteger(String value) {
		int radix = 10;
		int index = 0;
		boolean negative = false;

		// Handle minus sign, if present.
		if (value.startsWith("-")) {
			negative = true;
			index++;
		}

		// Handle radix specifier, if present.
		if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
			index += 2;
			radix = 16;
		} else if (value.startsWith("#", index)) {
			index++;
			radix = 16;
		} else if (value.startsWith("0", index) && value.length() > 1 + index) {
			index++;
			radix = 8;
		}

		BigInteger result = new BigInteger(value.substring(index), radix);
		return (negative ? result.negate() : result);
	}

}
