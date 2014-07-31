package cn.zhuhongqing.utils.loader;

import java.io.File;
import java.io.InputStream;

import cn.zhuhongqing.utils.ClassUtil;

/**
 * ClassPath loader.
 * 
 * @author HongQing.Zhu
 * 
 */

public class ClassPathResourceLoader implements ResourceLoader {

	static ClassLoader loader = ClassUtil.getDefaultClassLoader();

	public File loadAsFile(String path) {
		return new File(loader.getResource(path).getPath());
	}

	public InputStream loaderAsStream(String path) {
		return loader.getResourceAsStream(path);
	}

}
