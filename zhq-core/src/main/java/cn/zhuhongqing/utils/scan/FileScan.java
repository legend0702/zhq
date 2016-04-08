package cn.zhuhongqing.utils.scan;

import java.io.File;

import cn.zhuhongqing.anno.NotThreadSafe;

/**
 * File scan.<br/>
 * 
 * @author HongQing.Zhu
 * 
 */

@NotThreadSafe
public class FileScan extends FileAbstractScan<File> {

	@Override
	File convert(File file) {
		return file;
	}

}
