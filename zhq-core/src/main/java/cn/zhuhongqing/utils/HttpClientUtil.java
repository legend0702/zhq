package cn.zhuhongqing.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import cn.zhuhongqing.io.StreamUtil;

/**
 * Simple HttpClient.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class HttpClientUtil {

	public static HttpResult Get(String url, List<String> headers,
			List<String> paramValues, String encoding, long readTimeoutMs)
			throws IOException {
		String encodedContent = encodingParams(paramValues, encoding);
		url += (null == encodedContent) ? "" : ("?" + encodedContent);

		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(500);
			conn.setReadTimeout((int) readTimeoutMs);
			setHeaders(conn, headers, encoding);

			conn.connect();
			int respCode = conn.getResponseCode(); // send
			String resp = null;

			if (HttpURLConnection.HTTP_OK == respCode) {
				resp = StreamUtil.toString(conn.getInputStream(), encoding);
			} else {
				resp = StreamUtil.toString(conn.getErrorStream(), encoding);
			}
			return new HttpResult(respCode, resp);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * @param url
	 * @param headers
	 *            able null.
	 * @param paramValues
	 *            able null.
	 * @param encoding
	 *            URL encode.
	 * @param readTimeout
	 * @throws java.io.IOException
	 */
	static public HttpResult Post(String url, List<String> headers,
			List<String> paramValues, String encoding, long readTimeout)
			throws IOException {
		String encodedContent = encodingParams(paramValues, encoding);

		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout((int) readTimeout);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			setHeaders(conn, headers, encoding);

			conn.getOutputStream().write(encodedContent.getBytes());

			int respCode = conn.getResponseCode(); // send
			String resp = null;

			if (HttpURLConnection.HTTP_OK == respCode) {
				resp = StreamUtil.toString(conn.getInputStream(), encoding);
			} else {
				resp = StreamUtil.toString(conn.getErrorStream(), encoding);
			}
			return new HttpResult(respCode, resp);
		} finally {
			if (null != conn) {
				conn.disconnect();
			}
		}
	}

	private static void setHeaders(HttpURLConnection conn,
			List<String> headers, String encoding) {
		if (null != headers) {
			for (Iterator<String> iter = headers.iterator(); iter.hasNext();) {
				conn.addRequestProperty(iter.next(), iter.next());
			}
		}
		conn.addRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=" + encoding);
	}

	private static String encodingParams(List<String> paramValues,
			String encoding) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		if (null == paramValues) {
			return null;
		}

		for (Iterator<String> iter = paramValues.iterator(); iter.hasNext();) {
			sb.append(iter.next()).append("=");
			sb.append(URLEncoder.encode(iter.next(), encoding));
			if (iter.hasNext()) {
				sb.append("&");
			}
		}
		return sb.toString();
	}

	public static class HttpResult {
		final public int code;
		final public String content;

		public HttpResult(int code, String content) {
			this.code = code;
			this.content = content;
		}
	}
}
