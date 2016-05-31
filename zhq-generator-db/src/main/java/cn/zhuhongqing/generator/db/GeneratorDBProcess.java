package cn.zhuhongqing.generator.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhuhongqing.Generator;
import cn.zhuhongqing.dbmeta.DBMetaInfo;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.GenConfig;
import cn.zhuhongqing.generator.GenProcess;
import cn.zhuhongqing.generator.db.filter.AddGlobalDataFilter;
import cn.zhuhongqing.generator.db.filter.GeneratorDBFilter;
import cn.zhuhongqing.utils.CollectionUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.StringUtil;

/**
 * 具体生成执行器
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class GeneratorDBProcess {

	private static Logger LOG = LoggerFactory.getLogger(GenProcess.class);

	private DBMetaInfo dbInfo;
	private GenProcess gen;
	private GenConfig genConfig;
	private GeneratorDBConfig config;

	/**
	 * 该构造函数创建的对象缺少关键属性:<br/>
	 * 
	 * {@link #getDbMetaInfo()}、{@link #getGenConfig()}<br/>
	 * 
	 * 执行前必须进行设置,不然必然出错
	 */

	public GeneratorDBProcess() {
		this(null, null, GeneratorDBConfig.EMPTY_CONFIG);
	}

	public GeneratorDBProcess(DBMetaInfo dbMetaInfo, GenConfig genConfig) {
		this(dbMetaInfo, genConfig, GeneratorDBConfig.EMPTY_CONFIG);
	}

	public GeneratorDBProcess(DBMetaInfo dbMetaInfo, GenConfig genConfig, GeneratorDBConfig generatorDBConfig) {
		setDbMetaInfo(dbMetaInfo);
		setGenConfig(genConfig);
		setGeneratorDBConfig(generatorDBConfig);
		gen = Generator.create();
	}

	/**
	 * 根据当前配置信息进行生成
	 */

	public void execute() {
		LOG.info("-----------------Start GeneratorDB--------------------");
		LOG.info("-----------------Loading Tables-----------------------");
		Collection<Table> tables = getTables();
		if (tables.isEmpty()) {
			throw new GeneratorDBException("Can't found any table to generate");
		}
		LOG.info("-----------------Found [" + tables.size() + "] Tables--------------------");
		LOG.info("-----------------Init Generator--------------------");
		gen.init(genConfig, tables);
		beforeExecute(tables);
		LOG.info("-----------------Start Generate!!!--------------------");
		gen.execute();
		afterExecute(tables);
		LOG.info("-----------------Generate end--------------------");
	}

	/**
	 * 根据当前配置信息 对默认用户空间下的指定的表名进行生成
	 */

	public void execute(String... tableNames) {
		Collection<Table> bk = config.getTables();
		config.setTables(new ArrayList<Table>(tableNames.length));
		for (String name : tableNames) {
			config.addTable(name);
		}
		execute();
		config.setTables(bk);
	}

	private void beforeExecute(Collection<Table> tables) {
		for (GeneratorDBFilter filter : config.getFilters()) {
			filter.beforExecute(config, tables);
		}
		genConfig.getFilters().add(0, new AddGlobalDataFilter(config));
	}

	private void afterExecute(Collection<Table> tables) {
		genConfig.getFilters().remove(0);
	}

	private Collection<Table> getTables() {
		Collection<Table> cTables = config.getTables();
		if (CollectionUtil.isEmpty(cTables)) {
			return dbInfo.getTables();
		}
		Collection<Table> reTables = new LinkedHashSet<Table>(cTables.size());
		for (Table t : cTables) {
			Table db = null;
			if (StringUtil.isNotEmpty(t.getCatalog()) && StringUtil.isNotEmpty(t.getSchema())) {
				db = dbInfo.getTable(t.getCatalog(), t.getSchema(), t.getName());
			} else {
				db = dbInfo.getTable(t.getName());
			}
			if (GeneralUtil.isNotNull(db)) {
				reTables.add(db);
			}
		}
		return reTables;
	}

	public DBMetaInfo getDbMetaInfo() {
		return dbInfo;
	}

	public void setDbMetaInfo(DBMetaInfo dbMetaInfo) {
		this.dbInfo = dbMetaInfo;
	}

	public GenConfig getGenConfig() {
		return genConfig;
	}

	public void setGenConfig(GenConfig genConfig) {
		this.genConfig = genConfig;
	}

	public GeneratorDBConfig getGeneratorDBConfig() {
		return config;
	}

	public void setGeneratorDBConfig(GeneratorDBConfig generatorDBConfig) {
		this.config = generatorDBConfig;
	}

}
