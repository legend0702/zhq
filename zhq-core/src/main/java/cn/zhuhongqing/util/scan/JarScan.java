package cn.zhuhongqing.util.scan;

import java.net.URI;
import java.util.jar.JarEntry;

public class JarScan extends AbstractScan<JarEntry> {

	@Override
	JarEntry convert(URI uri, AbstractScan<JarEntry>.PathCouple couple)
			throws Exception {
		return new JarEntry(uri.getSchemeSpecificPart());
	}

}
