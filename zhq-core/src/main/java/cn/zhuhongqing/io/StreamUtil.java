package cn.zhuhongqing.io;

// Copyright (c) 2003-2013, Jodd Team (jodd.org). All Rights Reserved.
import static cn.zhuhongqing.Core.BYTE_SIZE;
import static cn.zhuhongqing.ZHQ.DEFAULT_ENCODING;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import cn.zhuhongqing.exception.UtilsException;

/**
 * Optimized byte and character stream utilities.
 */
public class StreamUtil {

	// ---------------------------------------------------------------- silent
	// close

	/**
	 * Closes an input stream and releases any system resources associated with
	 * this stream. No exception will be thrown if an I/O error occurs.
	 */
	public static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException ioex) {
				// ignore
			}
		}
	}

	/**
	 * Closes an output stream and releases any system resources associated with
	 * this stream. No exception will be thrown if an I/O error occurs.
	 */
	public static void close(OutputStream out) {
		if (out != null) {
			try {
				out.flush();
			} catch (IOException ioex) {
				// ignore
			}
			try {
				out.close();
			} catch (IOException ioex) {
				// ignore
			}
		}
	}

	/**
	 * Closes a character-input stream and releases any system resources
	 * associated with this stream. No exception will be thrown if an I/O error
	 * occurs.
	 */
	public static void close(Reader in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException ioex) {
				// ignore
			}
		}
	}

	/**
	 * Closes a character-output stream and releases any system resources
	 * associated with this stream. No exception will be thrown if an I/O error
	 * occurs.
	 */
	public static void close(Writer out) {
		if (out != null) {
			try {
				out.flush();
			} catch (IOException ioex) {
				// ignore
			}
			try {
				out.close();
			} catch (IOException ioex) {
				// ignore
			}
		}
	}

	// ---------------------------------------------------------------- copy

	/**
	 * Copies input stream to output stream using buffer. Streams don't have to
	 * be wrapped to buffered, since copying is already optimized.
	 */
	public static int copy(InputStream input, OutputStream output) {
		byte[] buffer = new byte[BYTE_SIZE];
		int count = 0;
		int read;
		try {
			while (true) {
				read = input.read(buffer, 0, BYTE_SIZE);
				if (read == -1) {
					break;
				}
				output.write(buffer, 0, read);
				count += read;
			}
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(input);
			close(output);
		}
		return count;
	}

	/**
	 * Copies specified number of bytes from input stream to output stream using
	 * buffer.
	 */
	public static int copy(InputStream input, OutputStream output, int byteCount) {
		byte buffer[] = new byte[BYTE_SIZE];
		int count = 0;
		int read;
		try {
			while (byteCount > 0) {
				if (byteCount < BYTE_SIZE) {
					read = input.read(buffer, 0, byteCount);
				} else {
					read = input.read(buffer, 0, BYTE_SIZE);
				}
				if (read == -1) {
					break;
				}
				byteCount -= read;
				count += read;
				output.write(buffer, 0, read);
			}
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(input);
			close(output);
		}
		return count;
	}

	/**
	 * Copies input stream to writer using buffer.
	 */
	public static void copy(InputStream input, Writer output) {
		copy(input, output, DEFAULT_ENCODING);
	}

	/**
	 * Copies specified number of bytes from input stream to writer using
	 * buffer.
	 */
	public static void copy(InputStream input, Writer output, int byteCount) {
		copy(input, output, DEFAULT_ENCODING, byteCount);
	}

	/**
	 * Copies input stream to writer using buffer and specified encoding.
	 */
	public static void copy(InputStream input, Writer output, String encoding) {
		try {
			copy(new InputStreamReader(input, encoding), output);
		} catch (UnsupportedEncodingException e) {
			throw new UtilsException(e);
		}
	}

	/**
	 * Copies specified number of bytes from input stream to writer using buffer
	 * and specified encoding.
	 */
	public static void copy(InputStream input, Writer output, String encoding, int byteCount) {
		try {
			copy(new InputStreamReader(input, encoding), output, byteCount);
		} catch (UnsupportedEncodingException e) {
			throw new UtilsException(e);
		}
	}

	/**
	 * Copies reader to writer using buffer. Streams don't have to be wrapped to
	 * buffered, since copying is already optimized.
	 */
	public static int copy(Reader input, Writer output) {
		char[] buffer = new char[BYTE_SIZE];
		int count = 0;
		int read;
		try {
			while ((read = input.read(buffer, 0, BYTE_SIZE)) >= 0) {
				output.write(buffer, 0, read);
				count += read;
			}
			output.flush();
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(input);
			close(output);
		}
		return count;
	}

	/**
	 * Copies specified number of characters from reader to writer using buffer.
	 */
	public static int copy(Reader input, Writer output, int charCount) {
		char buffer[] = new char[BYTE_SIZE];
		int count = 0;
		int read;
		try {
			while (charCount > 0) {
				if (charCount < BYTE_SIZE) {
					read = input.read(buffer, 0, charCount);
				} else {
					read = input.read(buffer, 0, BYTE_SIZE);
				}
				if (read == -1) {
					break;
				}
				charCount -= read;
				count += read;
				output.write(buffer, 0, read);
			}
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(input);
			close(output);
		}
		return count;
	}

	/**
	 * Copies reader to output stream using buffer.
	 */
	public static void copy(Reader input, OutputStream output) {
		copy(input, output, DEFAULT_ENCODING);
	}

	/**
	 * Copies specified number of characters from reader to output stream using
	 * buffer.
	 */
	public static void copy(Reader input, OutputStream output, int charCount) {
		copy(input, output, DEFAULT_ENCODING, charCount);
	}

	/**
	 * Copies reader to output stream using buffer and specified encoding.
	 */
	public static void copy(Reader input, OutputStream output, String encoding) {
		Writer out = null;
		try {
			out = new OutputStreamWriter(output, encoding);
			copy(input, out);
			out.flush();
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(out);
		}
	}

	/**
	 * Copies specified number of characters from reader to output stream using
	 * buffer and specified encoding.
	 */
	public static void copy(Reader input, OutputStream output, String encoding, int charCount) {
		Writer out = null;
		try {
			out = new OutputStreamWriter(output, encoding);
			copy(input, out, charCount);
			out.flush();
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(out);
		}
	}

	// ---------------------------------------------------------------- read
	// bytes

	/**
	 * Reads all available bytes from InputStream as a byte array. Uses
	 * <code>in.available()</code> to determine the size of input stream. This
	 * is the fastest method for reading input stream to byte array, but depends
	 * on stream implementation of <code>available()</code>. Buffered
	 * internally.
	 */
	public static byte[] readAvailableBytes(InputStream in) {
		try {
			int l = in.available();
			byte byteArray[] = new byte[l];
			int i = 0, j;
			while ((i < l) && (j = in.read(byteArray, i, l - i)) >= 0) {
				i += j;
			}
			if (i < l) {
				throw new IOException("Failed to completely read input stream");
			}
			return byteArray;
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(in);
		}
	}

	public static byte[] readBytes(InputStream input) {
		FastByteArrayOutputStream output = new FastByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static byte[] readBytes(InputStream input, int byteCount) {
		FastByteArrayOutputStream output = new FastByteArrayOutputStream();
		copy(input, output, byteCount);
		return output.toByteArray();
	}

	public static byte[] readBytes(Reader input) {
		FastByteArrayOutputStream output = new FastByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static byte[] readBytes(Reader input, int byteCount) {
		FastByteArrayOutputStream output = new FastByteArrayOutputStream();
		copy(input, output, byteCount);
		return output.toByteArray();
	}

	public static byte[] readBytes(Reader input, String encoding) {
		FastByteArrayOutputStream output = new FastByteArrayOutputStream();
		copy(input, output, encoding);
		return output.toByteArray();
	}

	public static byte[] readBytes(Reader input, String encoding, int byteCount) {
		FastByteArrayOutputStream output = new FastByteArrayOutputStream();
		copy(input, output, encoding, byteCount);
		return output.toByteArray();
	}

	public static String toString(InputStream input, String encoding) {
		try {
			return (null == encoding) ? toString(new InputStreamReader(input))
					: toString(new InputStreamReader(input, encoding));
		} catch (UnsupportedEncodingException e) {
			throw new UtilsException(e);
		}
	}

	public static String toString(Reader reader) {
		CharArrayWriter sw = new CharArrayWriter();
		copy(reader, sw);
		return sw.toString();
	}

	// ---------------------------------------------------------------- compare
	// content

	/**
	 * Compares the content of two byte streams.
	 * 
	 * @return <code>true</code> if the content of the first stream is equal to
	 *         the content of the second stream.
	 */
	public static boolean compare(InputStream input1, InputStream input2) {
		if (!(input1 instanceof BufferedInputStream)) {
			input1 = new BufferedInputStream(input1);
		}
		if (!(input2 instanceof BufferedInputStream)) {
			input2 = new BufferedInputStream(input2);
		}
		try {
			int ch = input1.read();
			while (ch != -1) {
				int ch2 = input2.read();
				if (ch != ch2) {
					return false;
				}
				ch = input1.read();
			}
			int ch2 = input2.read();
			return (ch2 == -1);
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(input1);
			close(input2);
		}
	}

	/**
	 * Compares the content of two character streams.
	 * 
	 * @return <code>true</code> if the content of the first stream is equal to
	 *         the content of the second stream.
	 */
	public static boolean compare(Reader input1, Reader input2) {
		if (!(input1 instanceof BufferedReader)) {
			input1 = new BufferedReader(input1);
		}
		if (!(input2 instanceof BufferedReader)) {
			input2 = new BufferedReader(input2);
		}

		try {
			int ch = input1.read();
			while (ch != -1) {
				int ch2 = input2.read();
				if (ch != ch2) {
					return false;
				}
				ch = input1.read();
			}
			int ch2 = input2.read();
			return (ch2 == -1);
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			close(input1);
			close(input2);
		}
	}

}
