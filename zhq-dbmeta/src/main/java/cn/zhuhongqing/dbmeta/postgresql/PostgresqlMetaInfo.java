package cn.zhuhongqing.dbmeta.postgresql;

import cn.zhuhongqing.dbmeta.DBMetaInfo;

public class PostgresqlMetaInfo extends DBMetaInfo {

	@Override
	protected String getDBDesign() {
		return "postgresql";
	}

}
