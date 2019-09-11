package cn.zhuhongqing.loader;

import java.io.IOException;

import cn.zhuhongqing.util.file.FileUtils;
import cn.zhuhongqing.util.loader.ClassPathResourceLoader;

public class ClassPathLoaderTest {

	ClassPathResourceLoader classPathLoader = new ClassPathResourceLoader();

	static String DEFAULT_CONFIG_PREFIX = "default-";

	public void loadFile() throws IOException {
		System.out.println(classPathLoader.loadAsFile(DEFAULT_CONFIG_PREFIX
				+ "core.js"));
	}

	public void readFile() throws IOException {
		System.out.println(FileUtils.readString(classPathLoader
				.loadAsFile(DEFAULT_CONFIG_PREFIX + "core.js")));
	}

	public void loadStream() throws IOException {
		System.out.println(classPathLoader.loaderAsStream(DEFAULT_CONFIG_PREFIX
				+ "core.js"));
	}

	public void readStream() throws IOException {
		System.out.println(FileUtils.readUTFString(classPathLoader
				.loaderAsStream(DEFAULT_CONFIG_PREFIX + "core.js")));
	}

}
