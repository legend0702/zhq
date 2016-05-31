package cn.zhuhongqing.generator.db.filter;

import java.util.Collection;

import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.db.GeneratorDBConfig;
import cn.zhuhongqing.generator.filter.GeneratorFilter;

/**
 * 在具体执行前做一些操作<br/>
 * 
 * 虽然基本可以用{@link GeneratorFilter}来代替 但是还是保留接口
 */

public interface GeneratorDBFilter {

	public default void beforExecute(GeneratorDBConfig config, Collection<Table> tables) {

	}

}
