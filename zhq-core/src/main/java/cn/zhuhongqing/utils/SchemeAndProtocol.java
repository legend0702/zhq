package cn.zhuhongqing.utils;

public interface SchemeAndProtocol {
	/** Scheme and protocol for a resource in the classPath: "classpath" */
	public static final String CLASSPATH = "classpath";

	/** Scheme and protocol for a file in the file system: "file" */
	public static final String FILE = "file";

	/** Scheme and protocol for an entry from a jar file: "jar" */
	public static final String JAR = "jar";

	/** Scheme and protocol for an entry from a zip file: "zip" */
	public static final String ZIP = "zip";

	/** Path prefix for loading from the class path: "classpath:" */
	public static final String CLASSPATH_PATH_PREFIX = "classpath:";

	/** Path prefix for loading from the file system: "file:" */
	public static final String FILE_PATH_PREFIX = "file:";

	/** Path prefix for loading from a jar file: "jar:" */
	public static final String JAR_PATH_PREFIX = "jar:";

	/** File extension for a regular jar file: ".jar" */
	public static final String JAR_FILE_EXTENSION = ".jar";

	/** Separator between JAR URL and file path within the JAR: "!/" */
	public static final String JAR_PATH_SEPARATOR = "!/";

	/** File extension for a regular jar file: ".class" */
	public static final String CLASS_FILE_EXTENSION = StringPool.DOT_CLASS;

}
