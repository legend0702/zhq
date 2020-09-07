package cn.zhuhongqing.generator.db.filter;

import cn.zhuhongqing.generator.filter.GeneratorFilter;
import cn.zhuhongqing.util.BeanWrap;
import cn.zhuhongqing.util.StringPool;
import cn.zhuhongqing.util.StringUtils;
import cn.zhuhongqing.util.URIUtils;
import cn.zhuhongqing.util.file.FileIOParams;
import cn.zhuhongqing.util.file.FileUtils;

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
	private static final String JAVA_PACKAGE_NAME = "JAVA_PACKAGE_NAME";

	public AutoAddClassPackageFilter(String basePackage) {
		this.basePackage = basePackage;
	};

	public boolean beforeGen(BeanWrap modelWrap, FileIOParams tempRootParams, FileIOParams currentInParams) {
		StringBuilder pkgName = new StringBuilder();
		pkgName.append(basePackage);
		StringBuilder pkg = new StringBuilder();
		pkg.append(StringPool.PACKAGE).append(StringPool.SPACE);
		pkg.append(basePackage);
		// 如果是根目录文件 那么就不追加模板文件的路径到package路径中
		if (FileUtils.isParent(tempRootParams.getFile(), currentInParams.getFile())) {
			// 既没有basePackage 也没有在模板文件路径 那么返回空字符串
			if (StringUtils.isEmpty(basePackage)) {
				modelWrap.putExParam(JAVA_PACKAGE, StringPool.EMPTY);
				modelWrap.putExParam(JAVA_PACKAGE_NAME, pkgName.toString());
				return true;
			}
		} else {
			String subPkg = StringUtils.cutStartToLastIndex(URIUtils.getSchemeSpecificPart(currentInParams.getFile()),
					URIUtils.getSchemeSpecificPart(tempRootParams.getFile()), StringPool.SLASH);
			if (StringUtils.isNotEmpty(subPkg)) {
				if (StringUtils.isNotEmpty(basePackage)) {
					pkgName.append(StringPool.DOT);
					pkg.append(StringPool.DOT);
				}
				pkgName.append(StringUtils.replaceSlashToDot(subPkg));
				pkg.append(StringUtils.replaceSlashToDot(subPkg));
			}
		}
		modelWrap.putExParam(JAVA_PACKAGE_NAME, pkgName.toString());
		modelWrap.putExParam(JAVA_PACKAGE, pkg.append(StringPool.SEMICOLON).toString());
		return true;
	}

}
