package cn.zhuhongqing.dbmeta;

/**
 * 默认的操作类
 * 
 * 找匹配不到的时候就会使用该类进行数据库元信息获取
 * 
 */

public final class DBMetaInfoDef extends DBMetaInfo {

	@Override
	protected String getDataBaseName() {
		return "Not Found";
	}

	@Override
	protected double getVersion() {
		return -1;
	}

}
