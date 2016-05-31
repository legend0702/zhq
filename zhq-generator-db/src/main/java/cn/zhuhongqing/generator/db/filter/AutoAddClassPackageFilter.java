package cn.zhuhongqing.generator.db.filter;

import cn.zhuhongqing.generator.filter.GeneratorFilter;
import cn.zhuhongqing.io.FileIOParams;
import cn.zhuhongqing.io.FileUtil;
import cn.zhuhongqing.utils.BeanWrap;
import cn.zhuhongqing.utils.StringPool;
import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.URIUtil;

/**
 * 针对每个.java模板文件追加package信息<br/>
 * 由于{@link #beforeGen(BeanWrap, FileIOParams, FileIOParams)}执行在渲染前<br/>
 * 获得的都是模型文件的参数,而不是目标文件的参数,因此现在只支持路径上没有渲染位的package追加<br/>
 */

public class AutoAddClassPackageFilter implements GeneratorFilter {

	/** 没有BasePackage参数的package信息 */
	public static final AutoAddClassPackageFilter INSTANCE = new AutoAddClassPackageFilter("");

	private String basePackage;

	private static final String JAVA_PACKAGE = "JAVA_PACKAGE";

	public AutoAddClassPackageFilter(String basePackage) {
		this.basePackage = basePackage;
	};

	@Override
	public boolean beforeGen(BeanWrap modelWrap, FileIOParams tempRootParams, FileIOParams currentInParams) {
		StringBuilder pkg = new StringBuilder();
		pkg.append(StringPool.PACKAGE).append(StringPool.SPACE);
		pkg.append(basePackage);
		// 如果是根目录文件 那么就不追加模板文件的路径到package路径中
		if (FileUtil.isParent(tempRootParams.getFile(), currentInParams.getFile())) {
			// 既没有basePackage 也没有在模板文件路径 那么返回空字符串
			if (StringUtil.isEmpty(basePackage)) {
				modelWrap.putExParam(JAVA_PACKAGE, StringPool.EMPTY);
				return true;
			}
		} else {
			String subPkg = StringUtil.cutStartToLastIndex(URIUtil.getSchemeSpecificPart(currentInParams.getFile()),
					URIUtil.getSchemeSpecificPart(tempRootParams.getFile()), StringPool.SLASH);
			if (StringUtil.isNotEmpty(subPkg)) {
				if (StringUtil.isNotEmpty(basePackage)) {
					pkg.append(StringPool.DOT);
				}
				pkg.append(StringUtil.replaceSlashToDot(subPkg));
			}
		}
		modelWrap.putExParam(JAVA_PACKAGE, pkg.append(StringPool.SEMICOLON).toString());
		return true;
	}

}
