package cn.zhq.dbmeta.postgresql;

import cn.zhq.dbmeta.DBMetaInfo;

public class PostgresqlMetaInfo extends DBMetaInfo {

	@Override
	protected String getDBDesign() {
		return "postgresql";
	}

}
