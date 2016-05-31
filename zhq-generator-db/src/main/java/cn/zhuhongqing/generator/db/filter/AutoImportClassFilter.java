package cn.zhuhongqing.generator.db.filter;

import java.util.HashSet;

import cn.zhuhongqing.dbmeta.struct.Column;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.filter.GeneratorFilter;
import cn.zhuhongqing.utils.BeanWrap;
import cn.zhuhongqing.utils.StringPool;

/**
 * 主要用于{@link Column}渲染文件时 自动根据{@link Column#getJavaType()}去引入相关的Class<br/>
 * 
 * 调用{@link BeanWrap#putExParam(String, Object)}进行参数追加<br/>
 * 
 * 参数名为{@link #IMPORT_KEY}<br/>
 * 
 * <b>注:如果模板中是直接用Class全名的话 那么就不需要import了</b>
 */

public class AutoImportClassFilter implements GeneratorFilter {

	public static final GeneratorFilter INSTANCE = new AutoImportClassFilter();

	private static final String IMPORT_KEY = "JAVA_IMPORT";

	private static final Package LANG_PACKAGE = Package.getPackage("java.lang");

	private AutoImportClassFilter() {
	};

	@Override
	public boolean beforeAll(BeanWrap modelWrap) {
		Table t = (Table) modelWrap.get();
		HashSet<String> types = new HashSet<String>();
		t.getColumns().forEach(c -> {
			Class<?> javaType = c.getJavaType();
			if (LANG_PACKAGE.equals(javaType.getPackage())) {
				return;
			}
			types.add(StringPool.IMPORT + StringPool.SPACE + javaType.getName() + StringPool.SEMICOLON
					+ StringPool.RETURN);
		});
		StringBuilder importText = new StringBuilder();
		types.forEach(s -> {
			importText.append(s);
		});
		modelWrap.putExParam(IMPORT_KEY, importText.toString());
		return true;
	}

}
