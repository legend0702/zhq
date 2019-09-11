package cn.zhuhongqing.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import cn.zhuhongqing.ZHQ;
import cn.zhuhongqing.exception.UtilsException;
import cn.zhuhongqing.util.scan.ResourceScanManager;

/**
 * 文件IO帮助类
 * 
 * @author HongQing.Zhu
 *
 */

public class FileIOParams {

	private String path;
	private File file;
	private Charset cs;

	public FileIOParams(String path) {
		this(path, ZHQ.DEFAULT_CHARSET);
	}

	public FileIOParams(String path, String csName) {
		this(path, Charset.forName(csName));
	}

	public FileIOParams(String path, Charset cs) {
		this.path = path;
		this.cs = cs;
		this.setFile(ResourceScanManager.autoGetResource(path, File.class));
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Charset getCharset() {
		return cs;
	}

	public void setCharset(Charset cs) {
		this.cs = cs;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public FileInputStream toInStream() {
		try {
			return new FileInputStream(getFile());
		} catch (FileNotFoundException e) {
			throw new UtilsException(e);
		}
	}

	public InputStreamReader toInStreamReader() {
		return new InputStreamReader(toInStream(), getCharset());
	}

	public FileOutputStream toOutStream() {
		try {
			return new FileOutputStream(getFile());
		} catch (FileNotFoundException e) {
			throw new UtilsException(e);
		}
	}

	public OutputStreamWriter toOutStreamWriter() {
		return new OutputStreamWriter(toOutStream(), getCharset());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileIOParams other = (FileIOParams) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileIOParams [path=" + path + ", file=" + file + ", cs=" + cs + "]";
	}

}
