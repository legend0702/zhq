package cn.zhuhongqing.generator.db;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhuhongqing.Generator;
import cn.zhuhongqing.dbmeta.DBMetaInfo;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.generator.GenConfig;
import cn.zhuhongqing.generator.GenProcess;
import cn.zhuhongqing.generator.db.filter.GeneratorDBFilter;
import cn.zhuhongqing.utils.CollectionUtil;
import cn.zhuhongqing.utils.StringUtil;

public class GeneratorDBProcess {

	private static Logger LOG = LoggerFactory.getLogger(GenProcess.class);

	private DBMetaInfo dbInfo;
	private GenProcess gen;
	private GenConfig genConfig;
	private GeneratorDBConfig config;

	public GeneratorDBProcess(DBMetaInfo dbMetaInfo, GenConfig genConfig) {
		this(dbMetaInfo, genConfig, GeneratorDBConfig.EMPTY_CONFIG);
	}

	public GeneratorDBProcess(DBMetaInfo dbMetaInfo, GenConfig genConfig, GeneratorDBConfig generatorDBConfig) {
		setDbMetaInfo(dbMetaInfo);
		setGenConfig(genConfig);
		setGeneratorDBConfig(generatorDBConfig);
		gen = Generator.create();
	}

	public void execute() {
		LOG.info("-----------------Start GeneratorDB--------------------");
		LOG.info("-----------------Loading Tables-----------------------");
		Collection<Table> tables = getTables();
		if (tables.isEmpty()) {
			throw new GeneratorDBException("Can't found any table to generate");
		}
		LOG.info("-----------------Found [" + tables.size() + "] Tables--------------------");
		LOG.info("-----------------Init Generator--------------------");
		beforeExecute(tables);
		gen.init(genConfig, tables);
		LOG.info("-----------------Start Generate!!!--------------------");
		gen.execute();
		afterExecute(tables);
		LOG.info("-----------------Generate end--------------------");
	}

	private void beforeExecute(Collection<Table> tables) {
		for (GeneratorDBFilter filter : config.getFilters()) {
			filter.beforExecute(config, tables);
		}
		genConfig.addFilter(new AddGlobalDataFilter(config));
	}

	private void afterExecute(Collection<Table> tables) {
		genConfig.getFilters().remove(genConfig.getFilters().size() - 1);
	}

	private Collection<Table> getTables() {
		Collection<Table> cTables = config.getTables();
		if (CollectionUtil.isEmpty(cTables)) {
			return dbInfo.getTables();
		}
		Collection<Table> reTables = new LinkedHashSet<Table>(cTables.size());
		for (Table t : cTables) {
			if (StringUtil.isNotEmpty(t.getCatalog()) && StringUtil.isNotEmpty(t.getSchema())) {
				reTables.add(dbInfo.getTable(t.getCatalog(), t.getSchema(), t.getName()));
			} else {
				reTables.add(dbInfo.getTable(t.getName()));
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
