package cn.zhuhongqing.utils.scan;

import java.io.File;
import java.net.URI;

public class FileScan extends AbstractScan<File> {

	@Override
	File convert(URI uri, PathCouple couple) {
		System.out.println(uri.toString());
		return new File(uri);
	}

}
