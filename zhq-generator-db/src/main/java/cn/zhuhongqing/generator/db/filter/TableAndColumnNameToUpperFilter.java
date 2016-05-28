package cn.zhuhongqing.generator.db.filter;

import java.util.Collection;

import cn.zhuhongqing.dbmeta.struct.Column;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.db.GeneratorDBConfig;

/**
 * 将表名以及列名转换成大写
 */

public class TableAndColumnNameToUpperFilter implements GeneratorDBFilter {

	public static final TableAndColumnNameToUpperFilter INSTANCE = new TableAndColumnNameToUpperFilter();

	@Override
	public void beforExecute(GeneratorDBConfig config, Collection<Table> tables) {
		for (Table t : tables) {
			t.setName(t.getName().toUpperCase());
			for (Column c : t.getColumns()) {
				c.setName(c.getName().toUpperCase());
			}
		}
	}

}
