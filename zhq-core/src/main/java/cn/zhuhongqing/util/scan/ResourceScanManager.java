package cn.zhuhongqing.util.scan;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;

import cn.zhuhongqing.util.GeneralUtils;

public final class ResourceScanManager {

	protected Map<Class<?>, ResourceScan<?>> TYPE_MAPPING = new HashMap<Class<?>, ResourceScan<?>>();

	private ResourceScanManager() {
		TYPE_MAPPING.put(File.class, new FileScan());
		TYPE_MAPPING.put(JarEntry.class, new JarScan());
		TYPE_MAPPING.put(Class.class, new ClassScan());
		TYPE_MAPPING.put(URL.class, new URLScan());
	}

	public static <R> R autoGetResource(String path, Class<R> type) {
		ResourceScan<R> rs = (ResourceScan<R>) ResourceScanManagerInstance.get(type);
		if (GeneralUtils.isNull(rs)) {
			return null;
		}
		return rs.getResource(path);
	}

	public static <R> R autoGetResource(String path, Class<R> type, ResourceFilter<R> filter) {
		ResourceScan<R> rs = (ResourceScan<R>) ResourceScanManagerInstance.get(type);
		if (GeneralUtils.isNull(rs)) {
			return null;
		}
		return rs.getResource(path, filter);
	}

	public static <R> Set<R> autoGetResources(String path, Class<R> type) {
		ResourceScan<R> rs = (ResourceScan<R>) ResourceScanManagerInstance.get(type);
		if (GeneralUtils.isNull(rs)) {
			return Collections.emptySet();
		}
		return rs.getResources(path);
	}

	public static <R> Set<R> autoGetResources(String path, Class<R> type, ResourceFilter<R> filter) {
		ResourceScan<R> rs = (ResourceScan<R>) ResourceScanManagerInstance.get(type);
		if (GeneralUtils.isNull(rs)) {
			return Collections.emptySet();
		}
		return rs.getResources(path, filter);
	}

	public static <R> ResourceScan<R> getResourceScan(Class<R> type) {
		return (ResourceScan<R>) ResourceScanManagerInstance.get(type);
	}

	static class ResourceScanManagerInstance {
		static final ResourceScanManager INSTANCE = new ResourceScanManager();

		@SuppressWarnings("unchecked")
		public static <R> ResourceScan<R> get(Class<R> type) {
			return (ResourceScan<R>) INSTANCE.TYPE_MAPPING.get(type);
		}
	}

}
