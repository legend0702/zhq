package cn.zhuhongqing.utils.scan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;

import cn.zhuhongqing.utils.ArraysUtil;
import cn.zhuhongqing.utils.GeneralUtil;

public final class ResourceScanManager {

	private static Map<Class<?>, ResourceScan<?>> TYPE_MAPPING = new HashMap<Class<?>, ResourceScan<?>>();

	private ResourceScanManager() {
		TYPE_MAPPING.put(File.class, new FileScan());
		TYPE_MAPPING.put(JarEntry.class, new JarScan());
		TYPE_MAPPING.put(Class.class, new ClassScan());
	}

	@SuppressWarnings("unchecked")
	public static <R> R autoGetResource(String path, Class<R> type,
			ResourceFilter<R>... filter) {
		ResourceScan<R> rs = (ResourceScan<R>) TYPE_MAPPING.get(type);
		if (GeneralUtil.isNull(rs)) {
			return null;
		}
		if (ArraysUtil.isEmpty(filter)) {
			return rs.getResource(path);
		}
		return rs.getResource(path, filter[0]);
	}

	@SuppressWarnings("unchecked")
	public static <R> Set<R> autoGetResources(String path, Class<R> type,
			ResourceFilter<R>... filter) {
		ResourceScan<R> rs = (ResourceScan<R>) TYPE_MAPPING.get(type);
		if (GeneralUtil.isNull(rs)) {
			return null;
		}
		if (ArraysUtil.isEmpty(filter)) {
			return rs.getResources(path);
		}
		return rs.getResources(path, filter[0]);
	}

}
