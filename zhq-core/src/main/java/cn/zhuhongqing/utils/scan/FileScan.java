package cn.zhuhongqing.utils.scan;

import java.io.File;

/**
 * File scan.
 * 
 * @author HongQing.Zhu
 * 
 */

public class FileScan extends FileAbstractScan<File> {

	@Override
	File convert(File file) {
		return file;
	}

}
