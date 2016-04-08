package cn.zhuhongqing.io;

import java.nio.charset.Charset;

/**
 * 文件IO帮助类
 * 
 * @author HongQing.Zhu
 *
 */

public class FileIOParams {

	private String path;
	private Charset cs = Charset.defaultCharset();

	public FileIOParams(String path) {
		this.path = path;
	}

	public FileIOParams(String path, Charset cs) {
		this.path = path;
		this.cs = cs;
	}

	public FileIOParams(String path, String csName) {
		this.path = path;
		this.cs = Charset.forName(csName);
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

}
