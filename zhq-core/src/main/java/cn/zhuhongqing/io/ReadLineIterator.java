package cn.zhuhongqing.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterator over the lines in a <code>Reader</code>.
 * <p>
 * <code>ReadLineIterator</code> holds a reference to an open
 * <code>Reader</code>. When you have finished with the iterator you should
 * close the reader to free internal resources. This can be done by closing the
 * reader directly, or by calling the {@link #close()} or
 * {@link #closeQuietly(ReadLineIterator)} method on the iterator.
 * <p>
 * The recommended usage pattern is:
 * 
 * <pre>
 * ReadLineIterator it = FileUtil.lineIterator(file, &quot;UTF-8&quot;);
 * try {
 * 	while (it.hasNext()) {
 * 		String line = it.nextLine();
 * 		// / do something with line
 * 	}
 * } finally {
 * 	ReadLineIterator.closeQuietly(it);
 * }
 * </pre>
 *
 * @author Niall Pemberton
 * @author Stephen Colebourne
 * @author Sandy McArthur
 */
public class ReadLineIterator implements Iterator<String> {

	/** The reader that is being read. */
	private final BufferedReader bufferedReader;
	/** The current line. */
	private String cachedLine;
	/** A flag indicating if the iterator has been fully read. */
	private boolean finished = false;

	/**
	 * Constructs an iterator of the lines for a <code>Reader</code>.
	 *
	 * @param reader
	 *            the <code>Reader</code> to read from, not null
	 * @throws IllegalArgumentException
	 *             if the reader is null
	 */
	public ReadLineIterator(final Reader reader)
			throws IllegalArgumentException {
		if (reader == null) {
			throw new IllegalArgumentException("Reader must not be null");
		}
		if (reader instanceof BufferedReader) {
			bufferedReader = (BufferedReader) reader;
		} else {
			bufferedReader = new BufferedReader(reader);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Indicates whether the <code>Reader</code> has more lines. If there is an
	 * <code>IOException</code> then {@link #close()} will be called on this
	 * instance.
	 *
	 * @return <code>true</code> if the Reader has more lines
	 * @throws IllegalStateException
	 *             if an IO exception occurs
	 */
	public boolean hasNext() {
		if (cachedLine != null) {
			return true;
		} else if (finished) {
			return false;
		} else {
			try {
				while (true) {
					String line = bufferedReader.readLine();
					if (line == null) {
						finished = true;
						return false;
					} else if (isValidLine(line)) {
						cachedLine = line;
						return true;
					}
				}
			} catch (IOException ioe) {
				close();
				throw new IllegalStateException(ioe.toString());
			}
		}
	}

	/**
	 * Overridable method to validate each line that is returned.
	 *
	 * @param line
	 *            the line that is to be validated
	 * @return true if valid, false to remove from the iterator
	 */
	protected boolean isValidLine(String line) {
		return true;
	}

	/**
	 * Returns the next line in the wrapped <code>Reader</code>.
	 *
	 * @return the next line from the input
	 * @throws NoSuchElementException
	 *             if there is no line to return
	 */
	public String next() {
		return nextLine();
	}

	/**
	 * Returns the next line in the wrapped <code>Reader</code>.
	 *
	 * @return the next line from the input
	 * @throws NoSuchElementException
	 *             if there is no line to return
	 */
	public String nextLine() {
		if (!hasNext()) {
			throw new NoSuchElementException("No more lines");
		}
		String currentLine = cachedLine;
		cachedLine = null;
		return currentLine;
	}

	/**
	 * Closes the underlying <code>Reader</code> quietly. This method is useful
	 * if you only want to process the first few lines of a larger file. If you
	 * do not close the iterator then the <code>Reader</code> remains open. This
	 * method can safely be called multiple times.
	 */
	public void close() {
		finished = true;
		StreamUtil.close(bufferedReader);
		cachedLine = null;
	}

	/**
	 * Unsupported.
	 *
	 * @throws UnsupportedOperationException
	 *             always
	 */
	public void remove() {
		throw new UnsupportedOperationException(
				"Remove unsupported on LineIterator");
	}

	// -----------------------------------------------------------------------
	/**
	 * Closes the iterator, handling null and ignoring exceptions.
	 *
	 * @param iterator
	 *            the iterator to close
	 */
	public static void closeQuietly(ReadLineIterator iterator) {
		if (iterator != null) {
			iterator.close();
		}
	}

}
