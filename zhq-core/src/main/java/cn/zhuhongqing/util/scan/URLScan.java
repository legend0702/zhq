package cn.zhuhongqing.util.scan;

import java.net.URI;
import java.net.URL;

public class URLScan extends AbstractScan<URL> {

	@Override
	URL convert(URI uri, AbstractScan<URL>.PathCouple couple) throws Exception {
		return uri.toURL();
	}

}
