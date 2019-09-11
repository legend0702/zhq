package cn.zhuhongqing.bean;

import java.util.Collection;
import java.util.Collections;

import cn.zhuhongqing.util.scan.ClassScan;

public interface BeanClassScanConfig {

	static final ClassScan CLASS_SCAN = ClassScan.INSTANCE;

	default String startPackage() {
		return getClass().getPackage().getName();
	}

	default Collection<Class<?>> getScanClass() {
		return Collections.emptyList();
	}

}