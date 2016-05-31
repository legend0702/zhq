package cn.zhuhongqing.generator.db.filter;

import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.filter.GeneratorFilter;
import cn.zhuhongqing.utils.BeanWrap;

/**
 * 将表名以及列名转换成大写
 */

public class TableAndColumnNameToUpperFilter implements GeneratorFilter {

	public static final GeneratorFilter INSTANCE = new TableAndColumnNameToUpperFilter();

	private TableAndColumnNameToUpperFilter() {
	}

	@Override
	public boolean beforeAll(BeanWrap modelWrap) {
		Table t = (Table) modelWrap.get();
		t.setName(t.getName().toUpperCase());
		t.getColumns().forEach(c -> {
			c.setName(c.getName().toUpperCase());
		});
		return true;
	}

}
