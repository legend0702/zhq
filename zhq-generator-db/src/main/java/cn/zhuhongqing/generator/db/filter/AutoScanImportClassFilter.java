package cn.zhuhongqing.generator.db.filter;

import java.util.Collection;
import java.util.HashSet;

import cn.zhuhongqing.dbmeta.struct.Column;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.db.GeneratorDBConfig;
import cn.zhuhongqing.utils.StringPool;

/**
 * 主要用于{@link Column}渲染文件时 自动根据{@link Column}中的Java Type去引入相关的Class<br/>
 * 
 * 调用{@link GeneratorDBConfig#addGlobalData(String, Object)}进行参数追加<br/>
 * 
 * 参数名为"ENTITY_IMPORT"
 */

public class AutoScanImportClassFilter implements GeneratorDBFilter {

	public static final AutoScanImportClassFilter INSTANCE = new AutoScanImportClassFilter();

	private static final String IMPORT_KEY = "ENTITY_IMPORT";

	@Override
	public void beforExecute(GeneratorDBConfig config, Collection<Table> tables) {
		HashSet<String> types = new HashSet<String>();
		tables.forEach(t -> {
			t.getColumns().forEach(c -> {
				types.add(StringPool.IMPORT + StringPool.SPACE + c.getJavaType().getName() + StringPool.SEMICOLON
						+ StringPool.RETURN);
			});
		});
		StringBuilder importText = new StringBuilder();
		types.forEach(s -> {
			importText.append(s);
		});
		config.addGlobalData(IMPORT_KEY, importText.toString());
	}

}
