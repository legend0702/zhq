package cn.zhuhongqing.dbmeta.utils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.zhuhongqing.call.CallBack;
import cn.zhuhongqing.dbmeta.exception.DBMetaException;

public class UnCatchSQLExceptionUtil {

	public static ResultSet getTables(DatabaseMetaData metaData, String c,
			String s, String t, String[] types) {
		try {
			return metaData.getTables(c, s, t, types);
		} catch (SQLException e) {
			throw new DBMetaException(e);
		}
	}

	public static ResultSet getColumns(DatabaseMetaData metaData, String c,
			String s, String t, String col) {
		try {
			return metaData.getColumns(c, s, t, col);
		} catch (SQLException e) {
			throw new DBMetaException(e);
		}
	}

	public static void reslutSetNext(ResultSet rs, CallBack<ResultSet> cb) {
		try {
			while (rs.next()) {
				cb.invokeThr(rs);
			}
		} catch (Exception e) {
			throw new DBMetaException(e);
		}
	}

	public static Set<String> getReslutSetNextOneColumn(ResultSet rs,
			String columnName) {
		Set<String> set = new LinkedHashSet<String>();
		reslutSetNext(rs, new CallBack<ResultSet>() {
			@Override
			public void invokeThr(ResultSet r) throws SQLException {
				set.add(rs.getString(columnName));
			}
		});
		return set;
	}

}
