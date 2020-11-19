package cn.zhuhongqing.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.io.IOUtils;

public class CompressUtils {

	public static final String COMPRESS_GZIP = "gzip";
	public static final String COMPRESS_DEFLATER = "deflater";

	public static final Charset UTF_8 = Charset.forName(StringPool.UTF_8);
	public static final Charset ISO_8859_1 = Charset.forName(StringPool.ISO_8859_1);

	public static byte[] inflater(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] output = new byte[256];
		Inflater inflater = new Inflater();
		inflater.setInput(bytes);
		int dataLength = -1;
		try {
			do {
				dataLength = inflater.inflate(output);
				baos.write(output, 0, dataLength);
			} while (dataLength != 0);
		} catch (DataFormatException e) {
			throw new UtilsException(e);
		} finally {
			inflater.end();
		}
		return baos.toByteArray();
	}

	public static byte[] deflater(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] output = new byte[256];
		Deflater compresser = new Deflater();
		compresser.setInput(bytes);
		compresser.finish();
		int dataLength = -1;
		do {
			dataLength = compresser.deflate(output);
			baos.write(output, 0, dataLength);
		} while (dataLength != 0);
		compresser.end();
		return baos.toByteArray();
	}

	public static byte[] gzipForString(String str, Charset charset) {
		return gzip(str.getBytes(charset));
	}

	public static byte[] gzipForString(String str) throws IOException {
		return gzipForString(str, UTF_8);
	}

	public static byte[] gzip(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = (GZIPOutputStream) gzip(out);
		try {
			gzip.write(bytes);
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			IOUtils.close(gzip);
		}
		return out.toByteArray();
	}

	public static OutputStream gzip(OutputStream out) {
		try {
			return new GZIPOutputStream(out);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	public static String ungzipToString(byte[] bytes, Charset charset) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return new String(((ByteArrayOutputStream) ungzipForStream(new ByteArrayInputStream(bytes))).toByteArray(),
				charset);
	}

	public static String ungzipToString(byte[] bytes) {
		return ungzipToString(bytes, UTF_8);
	}

	public static byte[] ungzip(byte[] bytes) {
		return ungzip(new ByteArrayInputStream(bytes));
	}

	public static byte[] ungzip(InputStream in) {
		return ((ByteArrayOutputStream) ungzipForStream(in)).toByteArray();
	}

	public static OutputStream ungzipForStream(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (IOException e) {
			throw new UtilsException(e);
		}
		return out;
	}

}
