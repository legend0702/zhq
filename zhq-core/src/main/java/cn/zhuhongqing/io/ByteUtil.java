package cn.zhuhongqing.io;

/**
 * Some utils for byte or byte[].
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class ByteUtil {

	/**
	 * @param b
	 *            Length must be 4
	 */

	public static int byteArrayToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	/**
	 * @return A length is 4 byte[]
	 */

	public static byte[] intToByteArray(int i) {
		return new byte[] { (byte) ((i >> 24) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 8) & 0xFF),
				(byte) (i & 0xFF) };
	}

}
