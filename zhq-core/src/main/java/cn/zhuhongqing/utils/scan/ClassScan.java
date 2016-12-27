package cn.zhuhongqing.utils.scan;

import java.net.URI;

import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.StringUtil;

public class ClassScan extends AbstractScan<Class<?>> {

	public static final ClassScan INSTANCE = new ClassScan();

	@Override
	Class<?> convert(URI uri, AbstractScan<Class<?>>.PathCouple couple) throws Exception {
		String maybeClass = uri.getSchemeSpecificPart().substring(couple.currentURI.getSchemeSpecificPart().length());
		if (ClassUtil.isClassFile(maybeClass)) {
			String pkg = couple.rootURI.getSchemeSpecificPart();
			return ClassUtil
					.forName(ClassUtil.cleanSuffixAndToClass(StringUtil.replaceSlashToDot(pkg.concat(maybeClass))));
		}
		return null;
	}
}