package cn.zhuhongqing.utils;

import java.nio.ByteBuffer;

public class ByteBufferUtil {

	public static final byte[] toPositionByte(ByteBuffer byteBuffer) {
		int old = byteBuffer.position();
		byte[] returnByte = new byte[byteBuffer.position()];
		byteBuffer.position(0);
		byteBuffer.get(returnByte);
		byteBuffer.position(old);
		return returnByte;
	}

	public static byte[] toLimitByte(ByteBuffer byteBuffer) {
		int old = byteBuffer.position();
		byte[] returnByte = new byte[byteBuffer.limit()];
		byteBuffer.position(0);
		byteBuffer.get(returnByte);
		byteBuffer.position(old);
		return returnByte;
	}

	/**
	 * increase ByteBuffer
	 * 
	 * @param byteBuffer
	 * @return
	 */

	public static final ByteBuffer increaseByteBuffer(ByteBuffer byteBuffer,
			int increase) {
		int capacity = byteBuffer.limit() + increase;
		ByteBuffer result = (byteBuffer.isDirect() ? ByteBuffer
				.allocateDirect(capacity) : ByteBuffer.allocate(capacity));
		result.order(byteBuffer.order());
		byteBuffer.flip();
		result.put(byteBuffer);
		return result;
	}

}
