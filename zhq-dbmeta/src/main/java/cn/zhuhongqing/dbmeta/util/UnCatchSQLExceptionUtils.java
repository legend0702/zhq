package cn.zhuhongqing.dbmeta.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.zhuhongqing.call.CallBackThr;
import cn.zhuhongqing.dbmeta.exception.DBMetaException;
import cn.zhuhongqing.dbmeta.util.DBUtils.CloseHelper;

public class UnCatchSQLExceptionUtils {

	public static ResultSet getTables(DatabaseMetaData metaData, String c, String s, String t, String[] types) {
		try {
			return metaData.getTables(c, s, t, types);
		} catch (SQLException e) {
			throw new DBMetaException(e);
		}
	}

	public static ResultSet getColumns(DatabaseMetaData metaData, String c, String s, String t, String col) {
		try {
			return metaData.getColumns(c, s, t, col);
		} catch (SQLException e) {
			throw new DBMetaException(e);
		}
	}

	public static ResultSet getPrimaryKeys(DatabaseMetaData metaData, String c, String s, String t) {
		try {
			return metaData.getPrimaryKeys(c, s, t);
		} catch (SQLException e) {
			throw new DBMetaException(e);
		}
	}

	public static void reslutSetNext(ResultSet rs, CallBackThr<ResultSet> cb) {
		try {
			while (rs.next()) {
				cb.invokeThr(rs);
			}
		} catch (Exception e) {
			throw new DBMetaException(e);
		} finally {
			try {
				CloseHelper.close(rs);
				CloseHelper.close(rs.getStatement());
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	public static Set<String> getReslutSetOneColumn(ResultSet rs, String columnName) {
		Set<String> set = new LinkedHashSet<String>();
		reslutSetNext(rs, new CallBackThr<ResultSet>() {
			@Override
			public void invokeThr(ResultSet r) throws SQLException {
				set.add(rs.getString(columnName));
			}
		});
		return set;
	}

}
