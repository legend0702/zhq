package cn.zhuhongqing.io;

// Copyright (c) 2003-2013, Jodd Team (jodd.org). All Rights Reserved.

import static cn.zhuhongqing.ZHQ.DEFAULT_ENCODING;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;

import cn.zhuhongqing.ZHQ;
import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.utils.ArraysUtil;
import cn.zhuhongqing.utils.SchemeAndProtocol;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.SystemUtil;
import cn.zhuhongqing.utils.URIUtil;
import cn.zhuhongqing.utils.matcher.URIMatch;

/**
 * File utilities.
 */
public class FileUtil {

	private static final String MSG_NOT_A_DIRECTORY = "Not a directory: ";
	private static final String MSG_CANT_CREATE = "Can't create: ";
	private static final String MSG_NOT_FOUND = "Not found: ";
	private static final String MSG_NOT_A_FILE = "Not a file: ";
	private static final String MSG_ALREADY_EXISTS = "Already exists: ";
	private static final String MSG_UNABLE_TO_DELETE = "Unable to delete: ";

	private static FileUtilParams fileUtilParams = new FileUtilParams();

	/**
	 * Simple factory for <code>File</code> objects.
	 */
	private static File file(String fileName) {
		return new File(fileName);
	}

	/**
	 * Simple factory for <code>File</code> objects.
	 */
	private static File file(File parent, String fileName) {
		return new File(parent, fileName);
	}

	// ---------------------------------------------------------------- misc
	// shortcuts

	/**
	 * Checks if two files points to the same file.
	 */
	public static boolean equals(String file1, String file2) {
		return equals(file(file1), file(file2));
	}

	/**
	 * Checks if two files points to the same file.
	 */
	public static boolean equals(File file1, File file2) {
		try {
			file1 = file1.getCanonicalFile();
			file2 = file2.getCanonicalFile();
		} catch (IOException ignore) {
			return false;
		}
		return file1.equals(file2);
	}

	/**
	 * Converts file URLs to file. Ignores other schemes and returns
	 * <code>null</code>.
	 */
	public static File toFile(URL url) {
		String fileName = toFileName(url);
		if (fileName == null) {
			return null;
		}
		return file(fileName);
	}

	public static JarFile toJarFile(URI uri) {
		URLConnection con;
		try {
			con = uri.toURL().openConnection();
			if (con instanceof JarURLConnection) {
				// Should usually be the case for traditional JAR files.
				JarURLConnection jarCon = (JarURLConnection) con;
				return jarCon.getJarFile();
			} else {
				// No JarURLConnection -> need to resort to URL file parsing.
				// We'll assume URLs of the format "jar:path!/entry", with the
				// protocol
				// being arbitrary as long as following the entry format.
				// We'll also handle paths with and without leading "file:"
				// prefix.
				String urlFile = uri.toURL().getFile();
				int separatorIndex = urlFile.indexOf(SchemeAndProtocol.JAR_PATH_SEPARATOR);
				if (separatorIndex != -1) {
					return toJarFile(urlFile.substring(0, separatorIndex));
				} else {
					return new JarFile(urlFile);
				}
			}
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Resolve the given jar file URL into a JarFile object.
	 */
	public static JarFile toJarFile(String jarFileUrl) {
		try {
			if (jarFileUrl.startsWith(SchemeAndProtocol.FILE_PATH_PREFIX)) {

				return new JarFile(URIUtil.toURI(jarFileUrl).getSchemeSpecificPart());
			} else {
				return new JarFile(jarFileUrl);
			}
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Converts file to URL in a correct way. Returns <code>null</code> in case
	 * of error.
	 */
	public static URL toURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new UtilsException(e);
		}
	}

	/**
	 * Convert files to URI in correct way.
	 */

	public static URI[] toURIs(File... files) {
		URI[] uris = new URI[files.length];
		for (int i = 0; i < uris.length; i++) {
			uris[i] = files[i].toURI();
		}
		return uris;
	}

	public static String toURISchemeSpecificPart(File file) {
		return file.toURI().getSchemeSpecificPart();
	}

	/**
	 * Converts file URLs to file name. Accepts only URLs with 'file' protocol.
	 * Otherwise, for other schemes returns <code>null</code>.
	 */
	public static String toFileName(URL url) {
		if ((url == null) || (url.getProtocol().equals("file") == false)) {
			return null;
		}
		String filename = url.getFile().replace('/', File.separatorChar);

		try {
			return java.net.URLDecoder.decode(filename, DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	// ---------------------------------------------------------------- mkdirs

	/**
	 * Creates all folders at once.
	 * 
	 * @see #mkdirs(java.io.File)
	 */
	public static void mkdirs(String dirs) {
		mkdirs(file(dirs));
	}

	/**
	 * Creates all folders at once.
	 */
	public static void mkdirs(File dirs) {
		if (dirs.exists()) {
			if (dirs.isDirectory() == false) {
				throw new UtilsException(new IOException(MSG_NOT_A_DIRECTORY + dirs));
			}
			return;
		}
		if (dirs.mkdirs() == false) {
			throw new UtilsException(new IOException(MSG_CANT_CREATE + dirs));
		}
	}

	/**
	 * Creates single folder.
	 * 
	 * @see #mkdir(java.io.File)
	 */
	public static void mkdir(String dir) {
		mkdir(file(dir));
	}

	/**
	 * Creates single folders.
	 */
	public static void mkdir(File dir) {
		if (dir.exists()) {
			if (dir.isDirectory() == false) {
				throw new UtilsException(new IOException(MSG_NOT_A_DIRECTORY + dir));
			}
			return;
		}
		if (dir.mkdir() == false) {
			throw new UtilsException(new IOException(MSG_CANT_CREATE + dir));
		}
	}

	// ---------------------------------------------------------------- touch

	/**
	 * @see #touch(java.io.File)
	 */
	public static void touch(String file) {
		touch(file(file));
	}

	/**
	 * Implements the Unix "touch" utility. It creates a new file with size 0
	 * or, if the file exists already, it is opened and closed without modifying
	 * it, but updating the file date and time.
	 */
	public static void touch(File file) {
		if (file.exists() == false) {
			try {
				StreamUtil.close(new FileOutputStream(file));
			} catch (FileNotFoundException e) {
				throw new UtilsException(e);
			}
		}
		file.setLastModified(System.currentTimeMillis());
	}

	// ---------------------------------------------------------------- params

	/**
	 * Creates new {@link FileUtilParams} instance by cloning current default
	 * params.
	 */
	public static FileUtilParams cloneParams() {
		return fileUtilParams.clone();
	}

	/**
	 * Creates new {@link FileUtilParams} instance with default values.
	 */
	public static FileUtilParams params() {
		return new FileUtilParams();
	}

	// ---------------------------------------------------------------- copy
	// file to file

	/**
	 * @see #copyFile(java.io.File, java.io.File, FileUtilParams)
	 */
	public static void copyFile(String src, String dest) {
		copyFile(file(src), file(dest), fileUtilParams);
	}

	/**
	 * @see #copyFile(java.io.File, java.io.File, FileUtilParams)
	 */
	public static void copyFile(String src, String dest, FileUtilParams params) {
		copyFile(file(src), file(dest), params);
	}

	/**
	 * @see #copyFile(java.io.File, java.io.File, FileUtilParams)
	 */
	public static void copyFile(File src, File dest) {
		copyFile(src, dest, fileUtilParams);
	}

	/**
	 * Copies a file to another file with specified {@link FileUtilParams copy
	 * params}.
	 */
	public static void copyFile(File src, File dest, FileUtilParams params) {
		checkFileCopy(src, dest, params);
		doCopyFile(src, dest, params);
	}

	private static void checkFileCopy(File src, File dest, FileUtilParams params) {
		if (src.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + src));
		}
		if (src.isFile() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_FILE + src));
		}
		if (equals(src, dest) == true) {
			throw new UtilsException(new IOException("Files '" + src + "' and '" + dest + "' are equal"));
		}

		File destParent = dest.getParentFile();
		if (destParent != null && destParent.exists() == false) {
			if (params.createDirs == false) {
				throw new UtilsException(new IOException(MSG_NOT_FOUND + destParent));
			}
			if (destParent.mkdirs() == false) {
				throw new UtilsException(new IOException(MSG_CANT_CREATE + destParent));
			}
		}
	}

	/**
	 * Internal file copy when most of the pre-checking has passed.
	 */
	private static void doCopyFile(File src, File dest, FileUtilParams params) {
		if (dest.exists()) {
			if (dest.isDirectory()) {
				throw new UtilsException(new IOException("Destination '" + dest + "' is a directory"));
			}
			if (params.overwrite == false) {
				throw new UtilsException(new IOException(MSG_ALREADY_EXISTS + dest));
			}
		}

		// do copy file
		try {
			StreamUtil.copy(new FileInputStream(src), new FileOutputStream(dest));
		} catch (FileNotFoundException e) {
			throw new UtilsException(e);
		}

		// done

		if (src.length() != dest.length()) {
			throw new UtilsException(
					new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes"));
		}
		if (params.preserveDate) {
			dest.setLastModified(src.lastModified());
		}
	}

	// ---------------------------------------------------------------- copy
	// file to directory

	/**
	 * @see #copyFileToDir(java.io.File, java.io.File, FileUtilParams)
	 */
	public static File copyFileToDir(String src, String destDir) {
		return copyFileToDir(file(src), file(destDir), fileUtilParams);
	}

	/**
	 * @see #copyFileToDir(java.io.File, java.io.File, FileUtilParams)
	 */
	public static File copyFileToDir(String src, String destDir, FileUtilParams params) {
		return copyFileToDir(file(src), file(destDir), params);
	}

	/**
	 * @see #copyFileToDir(java.io.File, java.io.File, FileUtilParams)
	 */
	public static File copyFileToDir(File src, File destDir) {
		return copyFileToDir(src, destDir, fileUtilParams);
	}

	/**
	 * Copies a file to folder with specified copy params and returns copied
	 * destination.
	 */
	public static File copyFileToDir(File src, File destDir, FileUtilParams params) {
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_DIRECTORY + destDir));
		}
		File dest = file(destDir, src.getName());
		copyFile(src, dest, params);
		return dest;
	}

	// ---------------------------------------------------------------- copy dir

	public static void copyDir(String srcDir, String destDir) {
		copyDir(file(srcDir), file(destDir), fileUtilParams);
	}

	public static void copyDir(String srcDir, String destDir, FileUtilParams params) {
		copyDir(file(srcDir), file(destDir), params);
	}

	public static void copyDir(File srcDir, File destDir) {
		copyDir(srcDir, destDir, fileUtilParams);
	}

	/**
	 * Copies directory with specified copy params.
	 */
	public static void copyDir(File srcDir, File destDir, FileUtilParams params) {
		checkDirCopy(srcDir, destDir);
		doCopyDirectory(srcDir, destDir, params);
	}

	private static void checkDirCopy(File srcDir, File destDir) {
		if (srcDir.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + srcDir));
		}
		if (srcDir.isDirectory() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_DIRECTORY + srcDir));
		}
		if (equals(srcDir, destDir) == true) {
			throw new UtilsException(
					new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are equal"));
		}
	}

	private static void doCopyDirectory(File srcDir, File destDir, FileUtilParams params) {
		if (destDir.exists()) {
			if (destDir.isDirectory() == false) {
				throw new UtilsException(new IOException(MSG_NOT_A_DIRECTORY + destDir));
			}
		} else {
			if (params.createDirs == false) {
				throw new UtilsException(new IOException(MSG_NOT_FOUND + destDir));
			}
			if (destDir.mkdirs() == false) {
				throw new UtilsException(new IOException(MSG_CANT_CREATE + destDir));
			}
			if (params.preserveDate) {
				destDir.setLastModified(srcDir.lastModified());
			}
		}

		File[] files = srcDir.listFiles();
		if (files == null) {
			throw new UtilsException(new IOException("Failed to list contents of: " + srcDir));
		}

		for (File file : files) {
			File destFile = file(destDir, file.getName());
			if (file.isDirectory()) {
				if (params.recursive == true) {
					doCopyDirectory(file, destFile, params);
				}
			} else {
				doCopyFile(file, destFile, params);
			}
		}
	}

	// ---------------------------------------------------------------- move
	// file

	public static void moveFile(String src, String dest) {
		moveFile(file(src), file(dest), fileUtilParams);
	}

	public static void moveFile(String src, String dest, FileUtilParams params) {
		moveFile(file(src), file(dest), params);
	}

	public static void moveFile(File src, File dest) {
		moveFile(src, dest, fileUtilParams);
	}

	public static void moveFile(File src, File dest, FileUtilParams params) {
		checkFileCopy(src, dest, params);
		doMoveFile(src, dest, params);
	}

	private static void doMoveFile(File src, File dest, FileUtilParams params) {
		if (dest.exists()) {
			if (dest.isFile() == false) {
				throw new UtilsException(new IOException(MSG_NOT_A_FILE + dest));
			}
			if (params.overwrite == false) {
				throw new UtilsException(new IOException(MSG_ALREADY_EXISTS + dest));
			}
			dest.delete();
		}

		final boolean rename = src.renameTo(dest);
		if (!rename) {
			doCopyFile(src, dest, params);
			src.delete();
		}
	}

	// ---------------------------------------------------------------- move
	// file to dir

	public static void moveFileToDir(String src, String destDir) {
		moveFileToDir(file(src), file(destDir), fileUtilParams);
	}

	public static void moveFileToDir(String src, String destDir, FileUtilParams params) {
		moveFileToDir(file(src), file(destDir), params);
	}

	public static void moveFileToDir(File src, File destDir) {
		moveFileToDir(src, destDir, fileUtilParams);
	}

	public static void moveFileToDir(File src, File destDir, FileUtilParams params) {
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_DIRECTORY + destDir));
		}
		moveFile(src, file(destDir, src.getName()), params);
	}

	// ---------------------------------------------------------------- move dir

	public static void moveDir(String srcDir, String destDir) {
		moveDir(file(srcDir), file(destDir));
	}

	public static void moveDir(File srcDir, File destDir) {
		checkDirCopy(srcDir, destDir);
		doMoveDirectory(srcDir, destDir);
	}

	private static void doMoveDirectory(File src, File dest) {
		if (dest.exists()) {
			if (dest.isDirectory() == false) {
				throw new UtilsException(new IOException(MSG_NOT_A_DIRECTORY + dest));
			}
			dest = file(dest, dest.getName());
			dest.mkdir();
		}

		final boolean rename = src.renameTo(dest);
		if (!rename) {
			doCopyDirectory(src, dest, params());
			deleteDir(src);
		}
	}

	// ---------------------------------------------------------------- delete
	// file

	public static void deleteFile(String dest) {
		deleteFile(file(dest));
	}

	public static void deleteFile(File dest) {
		if (dest.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + dest));
		}
		if (dest.isFile() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_FILE + dest));
		}
		if (dest.delete() == false) {
			throw new UtilsException(new IOException(MSG_UNABLE_TO_DELETE + dest));
		}
	}

	// ---------------------------------------------------------------- delete
	// dir

	public static void deleteDir(String dest) {
		deleteDir(file(dest), fileUtilParams);
	}

	public static void deleteDir(String dest, FileUtilParams params) {
		deleteDir(file(dest), params);
	}

	public static void deleteDir(File dest) {
		deleteDir(dest, fileUtilParams);
	}

	/**
	 * Deletes a directory.
	 */
	public static void deleteDir(File dest, FileUtilParams params) {
		cleanDir(dest, params);
		// if (dest.delete() == false) {
		// throw new UtilsException( new IOException(MSG_UNABLE_TO_DELETE +
		// dest);
		// }
	}

	public static void cleanDir(String dest) {
		cleanDir(file(dest), fileUtilParams);
	}

	public static void cleanDir(String dest, FileUtilParams params) {
		cleanDir(file(dest), params);
	}

	public static void cleanDir(File dest) {
		cleanDir(dest, fileUtilParams);
	}

	/**
	 * Cleans a directory without deleting it.
	 */
	public static void cleanDir(File dest, FileUtilParams params) {
		if (dest.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + dest));
		}

		if (dest.isDirectory() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_DIRECTORY + dest));
		}

		File[] files = dest.listFiles();
		if (files == null) {
			throw new UtilsException(new IOException("Failed to list contents of: " + dest));
		}

		for (File file : files) {
			if (file.isDirectory()) {
				if (params.recursive == true) {
					deleteDir(file, params);
				}
				file.delete();
			} else {
				file.delete();
			}
		}

	}

	/**
	 * Finds files and directories within a given directory.
	 * 
	 * @see FileUtil#listFiles(Collection, File, FileFilter)
	 */

	public static void listFiles(Collection<File> files, File rootDirectory) {
		listFiles(files, rootDirectory, TrueFileFilter.INSTANCE);
	}

	/**
	 * Finds files within a given directory (and optionally its
	 * sub-directories).<br/>
	 * All files found are filtered by an FileFilter.<br/>
	 *
	 * @param files
	 *            the collection of files found.(Include directories)
	 * @param directory
	 *            the directory to search in.(Root Directory)
	 * @param filter
	 *            the filter to apply to files and directories.
	 */

	public static void listFiles(Collection<File> files, File rootDirectory, FileFilter filter) {
		if (rootDirectory.exists() && rootDirectory.isDirectory() == false) {
			throw new IllegalArgumentException(MSG_NOT_A_DIRECTORY + rootDirectory);
		}
		innerListFiles(files, rootDirectory, filter);
	}

	/**
	 * Finds files within a given directory (and optionally its
	 * sub-directories).<br/>
	 * All files found are filtered by an FileFilter.<br/>
	 *
	 * @param files
	 *            the collection of files found.(Include directories)
	 * @param directory
	 *            the directory to search in.(Root Directory)
	 * @param filter
	 *            the filter to apply to files and directories.
	 */

	private static void innerListFiles(Collection<File> files, File directory, FileFilter filter) {
		File[] found = directory.listFiles(filter);
		if (found != null) {
			for (int i = 0; i < found.length; i++) {
				files.add(found[i]);
				if (found[i].isDirectory()) {
					innerListFiles(files, found[i], filter);
				}
			}
		}
	}

	// ----------------------------------------------------------------
	// read/write chars

	public static char[] readUTFChars(String fileName) {
		return readUTFChars(file(fileName));
	}

	/**
	 * Reads UTF file content as char array.
	 * 
	 * @see UnicodeInputStream
	 */
	public static char[] readUTFChars(File file) {
		if (file.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + file));
		}
		if (file.isFile() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_FILE + file));
		}
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			len = Integer.MAX_VALUE;
		}
		UnicodeInputStream in = null;
		try {
			in = new UnicodeInputStream(new FileInputStream(file), null);
			FastCharArrayWriter fastCharArrayWriter = new FastCharArrayWriter((int) len);
			String encoding = in.getDetectedEncoding();
			if (encoding == null) {
				encoding = DEFAULT_ENCODING;
			}
			StreamUtil.copy(in, fastCharArrayWriter, encoding);
			return fastCharArrayWriter.toCharArray();
		} catch (FileNotFoundException e) {
			throw new UtilsException(e);
		} finally {
			StreamUtil.close(in);
		}
	}

	public static char[] readChars(String fileName) {
		return readChars(file(fileName), fileUtilParams.encoding);
	}

	public static char[] readChars(File file) {
		return readChars(file, fileUtilParams.encoding);
	}

	public static char[] readChars(String fileName, String encoding) {
		return readChars(file(fileName), encoding);
	}

	/**
	 * Reads file content as char array.
	 */
	public static char[] readChars(File file, String encoding) {
		if (file.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + file));
		}
		if (file.isFile() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_FILE + file));
		}
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			len = Integer.MAX_VALUE;
		}

		InputStream in = null;
		try {
			in = new FileInputStream(file);
			if (encoding.startsWith("UTF")) {
				in = new UnicodeInputStream(in, encoding);
			}
			FastCharArrayWriter fastCharArrayWriter = new FastCharArrayWriter((int) len);
			StreamUtil.copy(in, fastCharArrayWriter, encoding);
			return fastCharArrayWriter.toCharArray();
		} catch (FileNotFoundException e) {
			throw new UtilsException(e);
		} finally {
			StreamUtil.close(in);
		}
	}

	public static void writeChars(File dest, char[] data) {
		outChars(dest, data, DEFAULT_ENCODING, false);
	}

	public static void writeChars(String dest, char[] data) {
		outChars(file(dest), data, DEFAULT_ENCODING, false);
	}

	public static void writeChars(File dest, char[] data, String encoding) {
		outChars(dest, data, encoding, false);
	}

	public static void writeChars(String dest, char[] data, String encoding) {
		outChars(file(dest), data, encoding, false);
	}

	protected static void outChars(File dest, char[] data, String encoding, boolean append) {
		if (dest.exists() == true) {
			if (dest.isFile() == false) {
				throw new UtilsException(new IOException(MSG_NOT_A_FILE + dest));
			}
		}
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest, append), encoding))) {
			out.write(data);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	// ----------------------------------------------------------------
	// read/write string

	public static String readUTFString(String fileName) {
		return readUTFString(file(fileName));
	}

	/**
	 * Detects optional BOM and reads UTF string from a file. If BOM is missing,
	 * UTF-8 is assumed.
	 * 
	 * @see UnicodeInputStream
	 */
	public static String readUTFString(File file) {
		if (file.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + file));
		}
		if (file.isFile() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_FILE + file));
		}
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			len = Integer.MAX_VALUE;
		}
		try (UnicodeInputStream in = new UnicodeInputStream(new FileInputStream(file), null);
				FastCharArrayWriter out = new FastCharArrayWriter((int) len)) {
			String encoding = in.getDetectedEncoding();
			if (encoding == null) {
				encoding = DEFAULT_ENCODING;
			}
			StreamUtil.copy(in, out, encoding);
			return out.toString();
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	/**
	 * Detects optional BOM and reads UTF string from an input stream. If BOM is
	 * missing, UTF-8 is assumed.
	 */
	public static String readUTFString(InputStream inputStream) {
		try (UnicodeInputStream in = new UnicodeInputStream(inputStream, null);
				FastCharArrayWriter out = new FastCharArrayWriter()) {
			String encoding = in.getDetectedEncoding();
			if (encoding == null) {
				encoding = DEFAULT_ENCODING;
			}
			StreamUtil.copy(in, out, encoding);
			return out.toString();
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	public static String readString(String source) {
		return readString(file(source), fileUtilParams.encoding);
	}

	public static String readString(String source, String encoding) {
		return readString(file(source), encoding);
	}

	public static String readString(File source) {
		return readString(source, fileUtilParams.encoding);
	}

	/**
	 * Reads file content as string encoded in provided encoding. For UTF
	 * encoded files, detects optional BOM characters.
	 */
	public static String readString(File file, String encoding) {
		if (file.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + file));
		}
		if (file.isFile() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_FILE + file));
		}
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			len = Integer.MAX_VALUE;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			if (encoding.startsWith("UTF")) {
				in = new UnicodeInputStream(in, encoding);
			}
			FastCharArrayWriter out = new FastCharArrayWriter((int) len);
			StreamUtil.copy(in, out, encoding);
			return out.toString();
		} catch (FileNotFoundException e) {
			throw new UtilsException(e);
		} finally {
			StreamUtil.close(in);
		}
	}

	public static void writeString(String dest, String data) {
		outString(file(dest), data, fileUtilParams.encoding, false);
	}

	public static void writeString(String dest, String data, String encoding) {
		outString(file(dest), data, encoding, false);
	}

	public static void writeString(File dest, String data) {
		outString(dest, data, fileUtilParams.encoding, false);
	}

	public static void writeString(File dest, String data, String encoding) {
		outString(dest, data, encoding, false);
	}

	public static void appendString(String dest, String data) {
		outString(file(dest), data, fileUtilParams.encoding, true);
	}

	public static void appendString(String dest, String data, String encoding) {
		outString(file(dest), data, encoding, true);
	}

	public static void appendString(File dest, String data) {
		outString(dest, data, fileUtilParams.encoding, true);
	}

	public static void appendString(File dest, String data, String encoding) {
		outString(dest, data, encoding, true);
	}

	protected static void outString(File dest, String data, String encoding, boolean append) {
		if (dest.exists() == true) {
			if (dest.isFile() == false) {
				throw new UtilsException(new IOException(MSG_NOT_A_FILE + dest));
			}
		}
		try (FileOutputStream out = new FileOutputStream(dest, append)) {
			out.write(data.getBytes(encoding));
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	// ---------------------------------------------------------------- stream

	public static void writeStream(File dest, InputStream in) {
		try (FileOutputStream out = new FileOutputStream(dest)) {
			StreamUtil.copy(in, out);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	public static void writeStream(String dest, InputStream in) {
		writeStream(new File(dest), in);
	}

	// ----------------------------------------------------------------
	// read/write string lines

	public static String[] readLines(String source) {
		return readLines(file(source), fileUtilParams.encoding);
	}

	public static String[] readLines(String source, String encoding) {
		return readLines(file(source), encoding);
	}

	public static String[] readLines(File source) {
		return readLines(source, fileUtilParams.encoding);
	}

	/**
	 * Reads lines from source files.
	 */
	public static String[] readLines(File file, String encoding) {
		if (file.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + file));
		}
		if (file.isFile() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_FILE + file));
		}
		List<String> list = new ArrayList<String>();

		InputStream in = null;
		BufferedReader br = null;
		try {
			in = new FileInputStream(file);
			if (encoding.startsWith("UTF")) {
				in = new UnicodeInputStream(in, encoding);
			}
			br = new BufferedReader(new InputStreamReader(in, encoding));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				list.add(strLine);
			}
		} catch (IOException e) {
			throw new UtilsException(e);
		} finally {
			StreamUtil.close(in);
			StreamUtil.close(br);
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Return an Iterator for the lines in a <code>File</code> using the default
	 * encoding({@link ZHQ#DEFAULT_ENCODING}).
	 *
	 * @param file
	 *            the file to open for input, must not be <code>null</code>
	 * @return an Iterator of the lines in the file, never <code>null</code>
	 * @throws IOException
	 *             in case of an I/O error (file closed)
	 * @see #lineIterator(File, String)
	 */
	public static ReadLineIterator lineIterator(File file) {
		return lineIterator(file, ZHQ.DEFAULT_ENCODING);
	}

	/**
	 * Return an Iterator for the lines in a <code>File</code>.
	 * <p>
	 * This method opens an <code>InputStream</code> for the file. When you have
	 * finished with the iterator you should close the stream to free internal
	 * resources. This can be done by calling the
	 * {@link ReadLineIterator#close()} or
	 * {@link ReadLineIterator#closeQuietly(ReadLineIterator)} method.
	 * <p>
	 * The recommended usage pattern is:
	 * 
	 * <pre>
	 * ReadLineIterator it = FileUtil.lineIterator(file, &quot;UTF-8&quot;);
	 * try {
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// / do something with line
	 * 	}
	 * } finally {
	 * 	ReadLineIterator.closeQuietly(iterator);
	 * }
	 * </pre>
	 * <p>
	 * If an exception occurs during the creation of the iterator, the
	 * underlying stream is closed.
	 *
	 * @param file
	 *            the file to open for input, must not be <code>null</code>
	 * @param encoding
	 *            the encoding to use, <code>null</code> means platform default
	 * @return an Iterator of the lines in the file, never <code>null</code>
	 * @throws IOException
	 *             in case of an I/O error (file closed)
	 */
	public static ReadLineIterator lineIterator(File file, String encoding) {
		try (InputStream in = new FileInputStream(file)) {
			return new ReadLineIterator(new InputStreamReader(in, encoding));
		} catch (IOException ex) {
			throw new UtilsException(ex);
		}
	}

	// ----------------------------------------------------------------
	// read/write bytearray

	public static byte[] readBytes(String file) {
		return readBytes(file(file));
	}

	public static byte[] readBytes(File file) {
		if (file.exists() == false) {
			throw new UtilsException(new FileNotFoundException(MSG_NOT_FOUND + file));
		}
		if (file.isFile() == false) {
			throw new UtilsException(new IOException(MSG_NOT_A_FILE + file));
		}
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			throw new UtilsException(new IOException("File is larger then max array size"));
		}

		byte[] bytes = new byte[(int) len];
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");) {
			randomAccessFile.readFully(bytes);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
		return bytes;
	}

	public static void writeBytes(String dest, byte[] data) {
		outBytes(file(dest), data, 0, data.length, false);
	}

	public static void writeBytes(String dest, byte[] data, int off, int len) {
		outBytes(file(dest), data, off, len, false);
	}

	public static void writeBytes(File dest, byte[] data) {
		outBytes(dest, data, 0, data.length, false);
	}

	public static void writeBytes(File dest, byte[] data, int off, int len) {
		outBytes(dest, data, off, len, false);
	}

	public static void appendBytes(String dest, byte[] data) {
		outBytes(file(dest), data, 0, data.length, true);
	}

	public static void appendBytes(String dest, byte[] data, int off, int len) {
		outBytes(file(dest), data, off, len, true);
	}

	public static void appendBytes(File dest, byte[] data) {
		outBytes(dest, data, 0, data.length, true);
	}

	public static void appendBytes(File dest, byte[] data, int off, int len) {
		outBytes(dest, data, off, len, true);
	}

	protected static void outBytes(File dest, byte[] data, int off, int len, boolean append) {
		if (dest.exists() == true) {
			if (dest.isFile() == false) {
				throw new UtilsException(new IOException(MSG_NOT_A_FILE + dest));
			}
		}
		try (FileOutputStream out = new FileOutputStream(dest, append)) {
			out.write(data, off, len);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	// ---------------------------------------------------------------- equals
	// content

	public static boolean compare(String file1, String file2) {
		return compare(file(file1), file(file2));
	}

	/**
	 * Compare the contents of two files to determine if they are equal or not.
	 * <p>
	 * This method checks to see if the two files are different lengths or if
	 * they point to the same file, before resorting to byte-by-byte comparison
	 * of the contents.
	 * <p>
	 * Code origin: Avalon
	 */
	public static boolean compare(File file1, File file2) {
		boolean file1Exists = file1.exists();
		if (file1Exists != file2.exists()) {
			return false;
		}

		if (file1Exists == false) {
			return true;
		}

		if ((file1.isFile() == false) || (file2.isFile() == false)) {
			throw new UtilsException(new IOException("Only files can be compared"));
		}

		if (file1.length() != file2.length()) {
			return false;
		}

		if (equals(file1, file2)) {
			return true;
		}

		try (InputStream input1 = new FileInputStream(file1); InputStream input2 = new FileInputStream(file2);) {
			return StreamUtil.compare(input1, input2);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	// ---------------------------------------------------------------- time

	public static boolean isNewer(String file, String reference) {
		return isNewer(file(file), file(reference));
	}

	/**
	 * Test if specified <code>File</code> is newer than the reference
	 * <code>File</code>.
	 * 
	 * @param file
	 *            the <code>File</code> of which the modification date must be
	 *            compared
	 * @param reference
	 *            the <code>File</code> of which the modification date is used
	 * @return <code>true</code> if the <code>File</code> exists and has been
	 *         modified more recently than the reference <code>File</code>.
	 */
	public static boolean isNewer(File file, File reference) {
		if (reference.exists() == false) {
			throw new IllegalArgumentException("Reference file not found: " + reference);
		}
		return isNewer(file, reference.lastModified());
	}

	public static boolean isOlder(String file, String reference) {
		return isOlder(file(file), file(reference));
	}

	public static boolean isOlder(File file, File reference) {
		if (reference.exists() == false) {
			throw new IllegalArgumentException("Reference file not found: " + reference);
		}
		return isOlder(file, reference.lastModified());
	}

	/**
	 * Tests if the specified <code>File</code> is newer than the specified time
	 * reference.
	 * 
	 * @param file
	 *            the <code>File</code> of which the modification date must be
	 *            compared.
	 * @param timeMillis
	 *            the time reference measured in milliseconds since the epoch
	 *            (00:00:00 GMT, January 1, 1970)
	 * @return <code>true</code> if the <code>File</code> exists and has been
	 *         modified after the given time reference.
	 */
	public static boolean isNewer(File file, long timeMillis) {
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() > timeMillis;
	}

	public static boolean isNewer(String file, long timeMillis) {
		return isNewer(file(file), timeMillis);
	}

	public static boolean isOlder(File file, long timeMillis) {
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() < timeMillis;
	}

	public static boolean isOlder(String file, long timeMillis) {
		return isOlder(file(file), timeMillis);
	}

	// ---------------------------------------------------------------- smart
	// copy

	public static void copy(String src, String dest) {
		copy(file(src), file(dest), fileUtilParams);
	}

	public static void copy(String src, String dest, FileUtilParams params) {
		copy(file(src), file(dest), params);
	}

	public static void copy(File src, File dest) {
		copy(src, dest, fileUtilParams);
	}

	/**
	 * Smart copy. If source is a directory, copy it to destination. Otherwise,
	 * if destination is directory, copy source file to it. Otherwise, try to
	 * copy source file to destination file.
	 */
	public static void copy(File src, File dest, FileUtilParams params) {
		if (src.isDirectory() == true) {
			copyDir(src, dest, params);
			return;
		}
		if (dest.isDirectory() == true) {
			copyFileToDir(src, dest, params);
			return;
		}
		copyFile(src, dest, params);
	}

	// ---------------------------------------------------------------- smart
	// move

	public static void move(String src, String dest) {
		move(file(src), file(dest), fileUtilParams);
	}

	public static void move(String src, String dest, FileUtilParams params) {
		move(file(src), file(dest), params);
	}

	public static void move(File src, File dest) {
		move(src, dest, fileUtilParams);
	}

	/**
	 * Smart move. If source is a directory, move it to destination. Otherwise,
	 * if destination is directory, move source file to it. Otherwise, try to
	 * move source file to destination file.
	 */
	public static void move(File src, File dest, FileUtilParams params) {
		if (src.isDirectory() == true) {
			moveDir(src, dest);
			return;
		}
		if (dest.isDirectory() == true) {
			moveFileToDir(src, dest, params);
			return;
		}
		moveFile(src, dest, params);
	}

	// ---------------------------------------------------------------- smart
	// delete

	public static void delete(String dest) {
		delete(file(dest), fileUtilParams);
	}

	public static void delete(String dest, FileUtilParams params) {
		delete(file(dest), params);
	}

	public static void delete(File dest) {
		delete(dest, fileUtilParams);
	}

	/**
	 * Smart delete of destination file or directory.
	 */
	public static void delete(File dest, FileUtilParams params) {
		if (dest.isDirectory()) {
			deleteDir(dest, params);
			return;
		}
		deleteFile(dest);
	}

	// ---------------------------------------------------------------- misc

	public static boolean isJavaFile(File file) {
		return (file.getName().endsWith(StringPool.DOT_JAVA));
	}

	public static boolean isExisAndReadable(File file) {
		return (file.exists() && file.canRead());
	}

	public static boolean isExisDirectory(File file) {
		return (file.exists() && file.isDirectory());
	}

	/**
	 * 
	 * 1.处理不包括里的匹配信息 一旦匹配到 直接丢弃<br/>
	 * 2.判断是否有包括匹配信息<br/>
	 * 2.1 有 处理包括里的匹配信息 一旦匹配到 直接返回,没有匹配到 则丢弃<br/>
	 * 3.返回数据<br/>
	 */

	public static boolean isInOrExcludeFile(File file, String[] includePattern, String[] excludePattern) {
		String path = file.getAbsolutePath();
		String name = file.getName();
		if (!ArraysUtil.isEmpty(excludePattern)) {
			for (String str : excludePattern) {
				if (str.endsWith(StringPool.SLASH)) {
					if (path.contains(str)) {
						return false;
					}
				} else {
					if (URIMatch.INSTANCE.hasPattern(str)) {
						if (URIMatch.INSTANCE.match(str, name)) {
							return false;
						}
					} else {
						if (name.equals(str)) {
							return false;
						}
					}
				}
			}
		}
		if (!ArraysUtil.isEmpty(includePattern)) {
			for (String str : includePattern) {
				if (str.endsWith(StringPool.SLASH)) {
					if (path.contains(str)) {
						return true;
					}
				} else {
					if (URIMatch.INSTANCE.hasPattern(str)) {
						if (URIMatch.INSTANCE.match(str, name)) {
							return true;
						}
					} else {
						if (name.equals(str)) {
							return true;
						}
					}
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * The child's parent is equals parent;
	 */

	public static boolean isParent(File parent, File child) {
		return parent.equals(getParentFile(child));
	}

	/**
	 * Check if one file is an ancestor of second one.
	 * 
	 * @param strict
	 *            if <code>false</code> then this method returns
	 *            <code>true</code> if ancestor and file are equal
	 * @return <code>true</code> if ancestor is parent of file;
	 *         <code>false</code> otherwise
	 */
	public static boolean isAncestor(File ancestor, File file, boolean strict) {
		File parent = strict ? getParentFile(file) : file;
		while (true) {
			if (parent == null) {
				return false;
			}
			if (parent.equals(ancestor)) {
				return true;
			}
			parent = getParentFile(parent);
		}
	}

	/**
	 * Returns parent for the file. The method correctly processes "." and ".."
	 * in file names. The name remains relative if was relative before. Returns
	 * <code>null</code> if the file has no parent.
	 */
	public static File getParentFile(final File file) {
		int skipCount = 0;
		File parentFile = file;
		while (true) {
			parentFile = parentFile.getParentFile();
			if (parentFile == null) {
				return null;
			}
			if (StringPool.DOT.equals(parentFile.getName())) {
				continue;
			}
			if (StringPool.DOTDOT.equals(parentFile.getName())) {
				skipCount++;
				continue;
			}
			if (skipCount > 0) {
				skipCount--;
				continue;
			}
			return parentFile;
		}
	}

	public static boolean isFilePathAcceptable(File file, FileFilter fileFilter) {
		do {
			if (fileFilter != null && !fileFilter.accept(file)) {
				return false;
			}
			file = file.getParentFile();
		} while (file != null);
		return true;
	}

	// ---------------------------------------------------------------- symlink

	/**
	 * Determines whether the specified file is a symbolic link rather than an
	 * actual file. Always returns <code>false</code> on Windows.
	 */
	public static boolean isSymlink(final File file) {
		if (SystemUtil.isHostWindows()) {
			return false;
		}

		File fileInCanonicalDir;

		if (file.getParent() == null) {
			fileInCanonicalDir = file;
		} else {
			File canonicalDir;
			try {
				canonicalDir = file.getParentFile().getCanonicalFile();
			} catch (IOException e) {
				throw new UtilsException(e);
			}
			fileInCanonicalDir = new File(canonicalDir, file.getName());
		}

		try {
			return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}

	/**
	 * {@link FileUtil File utilities} parameters.
	 */
	static class FileUtilParams implements Cloneable {

		protected boolean preserveDate = true; // should destination file have
												// the
												// same timestamp as source
		protected boolean overwrite = true; // overwrite existing destination
		protected boolean createDirs = true; // create missing subdirectories of
												// destination
		protected boolean recursive = true; // use recursive directory copying
											// and
											// deleting
		protected boolean continueOnError = true; // don't stop on error and
													// continue job as much as
													// possible
		protected String encoding = DEFAULT_ENCODING; // default encoding for
														// reading/writing
														// strings

		public boolean isPreserveDate() {
			return preserveDate;
		}

		public FileUtilParams setPreserveDate(boolean preserveDate) {
			this.preserveDate = preserveDate;
			return this;
		}

		public boolean isOverwrite() {
			return overwrite;
		}

		public FileUtilParams setOverwrite(boolean overwrite) {
			this.overwrite = overwrite;
			return this;
		}

		public boolean isCreateDirs() {
			return createDirs;
		}

		public FileUtilParams setCreateDirs(boolean createDirs) {
			this.createDirs = createDirs;
			return this;
		}

		public boolean isRecursive() {
			return recursive;
		}

		public FileUtilParams setRecursive(boolean recursive) {
			this.recursive = recursive;
			return this;
		}

		public boolean isContinueOnError() {
			return continueOnError;
		}

		public FileUtilParams setContinueOnError(boolean continueOnError) {
			this.continueOnError = continueOnError;
			return this;
		}

		public String getEncoding() {
			return encoding;
		}

		public FileUtilParams setEncoding(String encoding) {
			this.encoding = encoding;
			return this;
		}

		// ------------------------------------------------------------ clone

		@Override
		public FileUtilParams clone() {
			try {
				return (FileUtilParams) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new UtilsException(e);
			}
		}
	}
}