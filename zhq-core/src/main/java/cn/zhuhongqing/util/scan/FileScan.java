package cn.zhuhongqing.util.scan;

import java.io.File;
import java.net.URI;

public class FileScan extends AbstractScan<File> {

	@Override
	File convert(URI uri, PathCouple couple) {
		return new File(uri);
	}

}
