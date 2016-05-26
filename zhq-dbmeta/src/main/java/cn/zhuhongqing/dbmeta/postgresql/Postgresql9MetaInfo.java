package cn.zhuhongqing.dbmeta.postgresql;

import cn.zhuhongqing.dbmeta.DBMetaInfo;

public class Postgresql9MetaInfo extends DBMetaInfo {

	@Override
	protected String getDataBaseName() {
		return "postgresql";
	}

	@Override
	protected double getVersion() {
		return 9.4;
	}

}
