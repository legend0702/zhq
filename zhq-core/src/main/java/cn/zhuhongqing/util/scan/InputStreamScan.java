package cn.zhuhongqing.util.scan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

import cn.zhuhongqing.io.IOUtils;
import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.URIUtils;

public class InputStreamScan extends AbstractScan<InputStream> {

	public static final InputStreamScan INSTANCE = new InputStreamScan();

	@Override
	InputStream convert(URI uri, AbstractScan<InputStream>.PathCouple couple) throws Exception {
		if (URIUtils.isFile(uri)) {
			return new FileInputStream(new File(uri));
		}
		if (URIUtils.isJar(uri)) {
			String resourceName = URIUtils.getJarRootEntryPath(uri);
			InputStream in = ClassUtils.getDefaultClassLoader().getResourceAsStream(resourceName);
			if (in.available() > 0) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(in, baos);
				return new ByteArrayInputStream(baos.toByteArray());
			}
		}
		return uri.toURL().openStream();
	}

}
