package cn.zhuhongqing.generator.db.filter;

import java.util.Collection;

import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.db.GeneratorDBConfig;

/**
 * 在具体执行前做一些操作
 *
 */

public interface GeneratorDBFilter {

	public default void beforExecute(GeneratorDBConfig config, Collection<Table> tables) {

	}

}
