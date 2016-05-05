package cn.zhuhongqing.utils.scan;

import java.net.URI;

import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.StringUtil;

public class ClassScan extends AbstractScan<Class<?>> {

	@Override
	Class<?> convert(URI uri, AbstractScan<Class<?>>.PathCouple couple)
			throws Exception {
		String maybeClass = uri.getSchemeSpecificPart().substring(
				couple.currentURI.getSchemeSpecificPart().length());
		if (ClassUtil.isClassFile(maybeClass)) {
			String pkg = StringUtil.replaceSlashToDot(couple.rootURI
					.getSchemeSpecificPart());
			if (GeneralUtil.isPackageName(StringUtil.cutSuffix(pkg,
					StringPool.DOT))) {
				return ClassUtil.forName(ClassUtil.cleanSuffixAndToClass(pkg
						.concat(maybeClass)));
			}
		}
		return null;
	}
}