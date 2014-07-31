package cn.zhuhongqing.utils.scan;

import java.io.File;

import cn.zhuhongqing.Script;

/**
 * Scan ScriptFile.
 * 
 * @author HongQing.Zhu
 * @see Script#FILE_SUFFIX
 */

public class ScriptFileScan extends FileAbstractScan<File> {

	@Override
	File convert(File file) {
		if (file.getName().endsWith(Script.FILE_SUFFIX)) {
			return file;
		}
		return null;
	}
}
