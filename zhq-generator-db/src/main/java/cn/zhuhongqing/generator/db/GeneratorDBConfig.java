package cn.zhuhongqing.generator.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.db.filter.GeneratorDBFilter;

/**
 *
 */
public class GeneratorDBConfig {

	static final GeneratorDBConfig EMPTY_CONFIG = new GeneratorDBConfig();

	/**
	 * 希望以指定的表范围进行生成
	 * 
	 * 如果没有添加(空集合) 那么就会以当前默认用户空间下所有的表进行生成
	 */
	private Collection<Table> tables = new ArrayList<>();

	/**
	 * 设置全局的参数 以便在渲染时可以调用
	 */
	private Map<String, Object> globalData = new HashMap<>();

	/**
	 * 用来在执行前做一些操作
	 */

	private Collection<GeneratorDBFilter> filters = new ArrayList<>();

	public void addGlobalData(String key, Object data) {
		globalData.put(key, data);
	}

	public Object removeGlobalData(String key) {
		return globalData.remove(key);
	}

	public Map<String, Object> getGlobalData() {
		return globalData;
	}

	public void setGlobalData(Map<String, Object> globalData) {
		this.globalData = globalData;
	}

	public Collection<Table> getTables() {
		return tables;
	}

	public void setTables(Collection<Table> tables) {
		this.tables = tables;
	}

	public void addTable(String tableName) {
		tables.add(new Table(tableName));
	}

	public void addTable(Table table) {
		tables.add(table);
	}

	public void addFilter(GeneratorDBFilter filter) {
		this.filters.add(filter);
	}

	public Collection<GeneratorDBFilter> getFilters() {
		return filters;
	}

	public void setFilters(Collection<GeneratorDBFilter> filters) {
		this.filters = filters;
	}

}
