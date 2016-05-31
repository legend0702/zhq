package cn.zhuhongqing;

import cn.zhuhongqing.dbmeta.DBMetaInfo;
import cn.zhuhongqing.generator.GenConfig;
import cn.zhuhongqing.generator.db.GeneratorDBConfig;
import cn.zhuhongqing.generator.db.GeneratorDBProcess;

/**
 * 根据数据库表以及表中列的元数据信息
 * 
 * 配合Generator以及Template生成想要的文件(比如.java文件以及配置文件等等)
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class GeneratorDB extends Generator {

	public static String GENERATOR = getModuleName(Generator.class);

	/**
	 * @param connPropPath
	 *            链接信息
	 * @param tempFileRoot
	 *            模板文件根地址
	 * @param outFileRoot
	 *            生成文件根地址
	 * @param generatorDBConfig
	 *            生成器配置
	 */

	public static GeneratorDBProcess create(String connPropPath, String tempFileRoot, String outFileRoot,
			GeneratorDBConfig generatorDBConfig) {
		return create(DBMeta.createDBMetaInfo(connPropPath), new GenConfig(tempFileRoot, outFileRoot),
				generatorDBConfig);
	}

	/**
	 * @param dbMetaInfo
	 *            数据库信息
	 * @param genConfig
	 *            模板生成器配置
	 * @param generatorDBConfig
	 *            生成器配置
	 */

	public static GeneratorDBProcess create(DBMetaInfo dbMetaInfo, GenConfig genConfig,
			GeneratorDBConfig generatorDBConfig) {
		return new GeneratorDBProcess(dbMetaInfo, genConfig, generatorDBConfig);
	}

}