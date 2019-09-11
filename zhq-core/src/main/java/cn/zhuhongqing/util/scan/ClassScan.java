package cn.zhuhongqing.util.scan;

import java.net.URI;

import cn.zhuhongqing.util.ClassUtils;
import cn.zhuhongqing.util.StringUtils;

public class ClassScan extends AbstractScan<Class<?>> {

	public static final ClassScan INSTANCE = new ClassScan();

	@Override
	Class<?> convert(URI uri, AbstractScan<Class<?>>.PathCouple couple) throws Exception {
		String maybeClass = uri.getSchemeSpecificPart().substring(couple.currentURI.getSchemeSpecificPart().length());
		if (ClassUtils.isClassFile(maybeClass)) {
			String pkg = couple.rootURI.getSchemeSpecificPart();
			return ClassUtils
					.forName(ClassUtils.cleanSuffixAndToClass(StringUtils.replaceSlashToDot(pkg.concat(maybeClass))));
		}
		return null;
	}
}