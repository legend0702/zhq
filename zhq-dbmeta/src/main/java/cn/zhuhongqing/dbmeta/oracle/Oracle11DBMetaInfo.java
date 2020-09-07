package cn.zhuhongqing.dbmeta.oracle;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLType;

import cn.zhuhongqing.dbmeta.DBMetaInfo;
import cn.zhuhongqing.dbmeta.struct.Column;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.dbmeta.util.DBUtils;

public class Oracle11DBMetaInfo extends DBMetaInfo {

	/**
	 * Unsupported.
	 */

	@Override
	protected String initDefCatalog() throws Exception {
		return "UNSUPPORT";
	}

	@Override
	protected String initDefSchema() throws Exception {
		return DBUtils.queryForString(getConnection(), "SELECT USERNAME FROM USER_USERS");
	}

	@Override
	protected Table createTable(ResultSet rs, String catalog, String schema) throws Exception {
		Table t = super.createTable(rs, catalog, schema);
		t.setRemarks(DBUtils.queryForString(getConnection(),
				"SELECT COMMENTS FROM ALL_TAB_COMMENTS WHERE OWNER = ? AND TABLE_NAME = ?", schema, t.getName()));
		return t;
	}

	@Override
	protected Column createColumn(ResultSet rs, Table t) throws Exception {
		Column c = super.createColumn(rs, t);
		c.setRemarks(DBUtils.queryForString(getConnection(),
				"SELECT COMMENTS FROM ALL_COL_COMMENTS WHERE OWNER = ? AND TABLE_NAME = ? AND COLUMN_NAME = ?",
				t.getSchema(), t.getName(), c.getName()));
		return c;
	}

	/**
	 * Sample resolve.
	 */

	@Override
	protected SQLType getSQLType(int sqlTypeInt) {
		switch (sqlTypeInt) {
		case -101:
			return JDBCType.TIMESTAMP_WITH_TIMEZONE;
		case -102:
			return JDBCType.TIMESTAMP_WITH_TIMEZONE;
		default:
			return null;
		}
	}

	@Override
	protected String getDataBaseName() {
		return "oracle";
	}

	@Override
	protected double getVersion() {
		return 11.2;
	}

}
