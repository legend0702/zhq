package cn.zhuhongqing.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * 
 * Some IP utilities.
 * 
 * Default format ==> IP:PORT.
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         </nl>
 */

public class IpUtil {

	public static SocketAddress string2SocketAddress(final String addr) {
		String[] s = addr.split(StringPool.COLON);
		InetSocketAddress isa = new InetSocketAddress(s[0],
				Integer.valueOf(s[1]));
		return isa;
	}

	public static String socketAddress2String(final SocketAddress addr) {
		StringBuilder sb = new StringBuilder();
		InetSocketAddress inetSocketAddress = (InetSocketAddress) addr;
		sb.append(inetSocketAddress.getAddress().getHostAddress());
		sb.append(StringPool.COLON);
		sb.append(inetSocketAddress.getPort());
		return sb.toString();
	}

	public static InetAddress getLocalAddress() {
		try {
			Enumeration<NetworkInterface> enumeration = NetworkInterface
					.getNetworkInterfaces();
			ArrayList<Inet4Address> ipv4Result = new ArrayList<Inet4Address>();
			ArrayList<Inet6Address> ipv6Result = new ArrayList<Inet6Address>();
			while (enumeration.hasMoreElements()) {
				final NetworkInterface networkInterface = enumeration
						.nextElement();
				final Enumeration<InetAddress> en = networkInterface
						.getInetAddresses();
				while (en.hasMoreElements()) {
					final InetAddress address = en.nextElement();
					if (!address.isLoopbackAddress()) {
						if (address instanceof Inet6Address) {
							ipv6Result.add((Inet6Address) address);
						}
						if (address instanceof Inet4Address) {
							ipv4Result.add((Inet4Address) address);
						}
					}
				}
			}

			// priority use ipv4
			if (!ipv4Result.isEmpty()) {
				for (Inet4Address ip : ipv4Result) {
					String host = ip.getHostAddress();
					if (host.startsWith("127") || host.startsWith("192")) {
						continue;
					}
					return ip;
				}
				// if all fail,return last one.
				return ipv4Result.get(ipv4Result.size() - 1);
			}
			// ipv6
			else if (!ipv6Result.isEmpty()) {
				return ipv6Result.get(0);
			}
			// All fail,use localhost. (:
			final InetAddress localHost = InetAddress.getLocalHost();
			return localHost;
		} catch (SocketException | UnknownHostException e) {
			// Log
			return InetAddress.getLoopbackAddress();
		}
	}

}
