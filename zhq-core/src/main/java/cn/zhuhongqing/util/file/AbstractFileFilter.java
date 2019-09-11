package cn.zhuhongqing.util.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public abstract class AbstractFileFilter implements FileFilter, FilenameFilter {

	@Override
	public boolean accept(File file) {
		return accept(file, file.getName());
	}

	@Override
	public boolean accept(File file, String name) {
		return accept(file);
	}

}