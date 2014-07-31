package cn.zhuhongqing.utils.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import cn.zhuhongqing.utils.StringPool;

/**
 * Load reource from jar which is under the WEB-INF/lib.(Suppose this jar is
 * inside your WEB-INF/lib)
 * 
 * @author HongQing.Zhu
 * 
 */

public class WebResourceLoader implements ResourceLoader {

	static String root;

	static {
		try {
			String path = ClassLoader.getSystemClassLoader()
					.getResource(StringPool.EMPTY).toURI().getPath();
			root = new File(path).getParentFile().getParentFile()
					.getCanonicalPath();
			root = root == null ? StringPool.EMPTY : root;
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}

	}

	public File loadAsFile(String path) {
		return new File(root, path);
	}

	public InputStream loaderAsStream(String path) {
		try {
			return new FileInputStream(loadAsFile(path));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

}
