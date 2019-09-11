package cn.zhuhongqing.util.struct;

import java.io.Serializable;

import cn.zhuhongqing.util.StringPool;

/**
 * Protocol and Version.
 * <p>
 * Use a "major.minor" numbering scheme to indicate versions of the protocol.
 * </p>
 * 
 * <pre>
 *     Protocol-Version = Protocol "/" major "." minor
 * </pre>
 * 
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public interface ProtocolVersion extends Comparable<ProtocolVersion>, Serializable {

	public static ProtocolVersion of(String protocol, int major, int minor) {
		return new DefaultProtocolVersion(protocol, major, minor);
	}

	/**
	 * Returns the name of the protocol.
	 */
	String protocol();

	/**
	 * Returns the name of the protocol.
	 */
	int majorVersion();

	/**
	 * Returns the name of the protocol.
	 */
	int minorVersion();

	/**
	 * Returns the version of the protocol.
	 */

	default String toVersion() {
		return majorVersion() + StringPool.DOT + minorVersion();
	}

	/**
	 * Returns the true when protocol、major、minor all same.
	 */

	default boolean match(String protocol, int major, int minor) {
		return (majorVersion() == major && minorVersion() == minor && protocol().equals(protocol));
	}

	/**
	 * Returns the full protocol version text.
	 */
	default String protocolVersion() {
		return protocol() + StringPool.SLASH + majorVersion() + StringPool.DOT + minorVersion();
	}

	default int compareTo(ProtocolVersion o) {
		int v = protocol().compareTo(o.protocol());
		if (v != 0) {
			return v;
		}
		v = majorVersion() - o.majorVersion();
		if (v != 0) {
			return v;
		}
		return minorVersion() - o.minorVersion();
	}

	public static class DefaultProtocolVersion implements ProtocolVersion {

		private static final long serialVersionUID = 1L;

		private final String protocol;
		private final int major;
		private final int minor;

		public DefaultProtocolVersion(String protocol, int major, int minor) {
			this.protocol = protocol;
			this.major = major;
			this.minor = minor;
		}

		@Override
		public String protocol() {
			return protocol;
		}

		@Override
		public int majorVersion() {
			return major;
		}

		@Override
		public int minorVersion() {
			return minor;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + major;
			result = prime * result + minor;
			result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			return (0 == compareTo((ProtocolVersion) obj));
		}

	}

}
