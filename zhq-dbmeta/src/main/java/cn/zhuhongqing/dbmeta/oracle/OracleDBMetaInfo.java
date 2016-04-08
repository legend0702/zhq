package cn.zhuhongqing.dbmeta.oracle;

import java.sql.ResultSet;

import cn.zhuhongqing.dbmeta.DBMetaInfo;
import cn.zhuhongqing.dbmeta.struct.Column;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.dbmeta.utils.DBUtil;

public class OracleDBMetaInfo extends DBMetaInfo {

	@Override
	protected String getDBDesign() {
		return "oracle";
	}

	/**
	 * Not supported
	 */

	@Override
	protected String initDefCatalog() throws Exception {
		return "UNSUPPORT";
	}

	@Override
	protected String initDefSchema() throws Exception {
		return DBUtil.queryForString(getConnection(),
				"SELECT USERNAME FROM USER_USERS");
	}

	@Override
	protected Table createTable(ResultSet rs, String catalog, String schema)
			throws Exception {
		Table t = super.createTable(rs, catalog, schema);
		t.setRemarks(DBUtil
				.queryForString(
						getConnection(),
						"SELECT COMMENTS FROM ALL_TAB_COMMENTS WHERE OWNER = ? AND TABLE_NAME = ?",
						schema, t.getName()));
		return t;
	}

	@Override
	protected Column createColumn(ResultSet rs, Table t) throws Exception {
		Column c = super.createColumn(rs, t);
		c.setRemarks(DBUtil
				.queryForString(
						getConnection(),
						"SELECT COMMENTS FROM ALL_COL_COMMENTS WHERE OWNER = ? AND TABLE_NAME = ? AND COLUMN_NAME = ?",
						t.getSchema(), t.getName(), c.getName()));
		return c;
	}

}
