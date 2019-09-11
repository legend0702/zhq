package cn.zhuhongqing.util.loader;

import java.io.File;
import java.io.InputStream;

import cn.zhuhongqing.util.ClassUtils;

/**
 * ClassPath loader.
 * 
 * @author HongQing.Zhu
 * 
 */

public class ClassPathResourceLoader implements ResourceLoader {

	static ClassLoader loader = ClassUtils.getDefaultClassLoader();

	public File loadAsFile(String path) {
		return new File(loader.getResource(path).getPath());
	}

	public InputStream loaderAsStream(String path) {
		return loader.getResourceAsStream(path);
	}

}
