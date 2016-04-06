package cn.zhq.dbmeta.utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.zhq.dbmeta.cfg.ConnectionInfo;
import cn.zhq.dbmeta.excetion.DBMetaException;
import cn.zhuhongqing.utils.ArraysUtil;
import cn.zhuhongqing.utils.ReflectUtil;
import cn.zhuhongqing.utils.StringUtil;

/**
 * 链接帮助类
 * 
 * @author HongQing.Zhu
 *
 */

public class DBUtil {

	public static Connection createConnection(ConnectionInfo info) {
		String driverStr = info.getDriver();
		if (StringUtil.isEmpty(driverStr)) {
			throw new DBMetaException("驱动类不能为空!");
		}
		if (StringUtil.isEmpty(info.getUrl())) {
			throw new DBMetaException("链接不能为空!");
		}
		if (StringUtil.isEmpty(info.getUser())) {
			throw new DBMetaException("用户名不能为空!");
		}
		Driver driver = (Driver) ReflectUtil.newInstance(driverStr);
		try {
			return driver.connect(info.getUrl(), info);
		} catch (SQLException e) {
			throw new DBMetaException(e);
		}
	}

	/**
	 * 执行一段SQL 只返回第一个结果 不会关闭链接
	 * 
	 * 用的是PreparedStatement 对于有参数的sql请用参数占位符
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */

	public static String queryForString(Connection conn, String sql,
			Object... params) {
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			s = conn.prepareStatement(sql);
			if (!ArraysUtil.isEmpty(params)) {
				for (int i = 0; i < params.length; i++) {
					s.setObject(i + 1, params[i]);
				}
			}
			rs = s.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
			return null;
		} catch (SQLException e) {
			return null;
		} finally {
			CloseHelper.close(s, rs);
		}
	}

	public static Set<String> queryForStringSet(Connection conn, String sql) {
		Statement s = null;
		ResultSet rs = null;
		Set<String> set = new LinkedHashSet<String>();
		try {
			s = conn.createStatement();
			rs = s.executeQuery(sql);
			while (rs.next()) {
				set.add(rs.getString(1));
			}
			return set;
		} catch (SQLException e) {
			return set;
		} finally {
			CloseHelper.close(s, rs);
		}
	}

	public static class CloseHelper {
		public static void close(Connection conn) {
			try {
				if (conn != null && !conn.isClosed()) {
					try {
						conn.close();
					} catch (Exception e) {
						// ignore
					}
				}
			} catch (SQLException e) {
				// ignore
			}
		}

		public static void close(PreparedStatement s) {
			try {
				if (s != null && !s.isClosed()) {
					try {
						s.close();
					} catch (Exception e) {
						// ignore
					}
				}
			} catch (SQLException e) {
				// ignore
			}
		}

		public static void close(Statement s) {
			try {
				if (s != null && !s.isClosed()) {
					try {
						s.close();
					} catch (Exception e) {
						// ignore
					}
				}
			} catch (SQLException e) {
				// ignore
			}
		}

		public static void close(ResultSet s) {
			try {
				if (s != null && !s.isClosed()) {
					try {
						s.close();
					} catch (Exception e) {
						// ignore
					}
				}
			} catch (SQLException e) {
				// ignore
			}
		}

		public static void close(Connection conn, ResultSet rs) {
			close(conn);
			close(rs);
		}

		public static void close(Connection conn, PreparedStatement ps,
				ResultSet rs) {
			close(conn);
			close(ps);
			close(rs);
		}

		public static void close(PreparedStatement ps, ResultSet rs) {
			close(ps);
			close(rs);
		}

		public static void close(Connection conn, Statement s, ResultSet rs) {
			close(conn);
			close(s);
			close(rs);
		}

		public static void close(Statement s, ResultSet rs) {
			close(s);
			close(rs);
		}

		public static void rollback(Connection conn) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					throw new RuntimeException("rollback occer error", e);
				}
			}
		}
	}

}
