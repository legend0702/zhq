package cn.zhuhongqing;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import cn.zhuhongqing.dbmeta.DBMetaConfig;
import cn.zhuhongqing.dbmeta.DBMetaInfo;
import cn.zhuhongqing.dbmeta.exception.DBMetaException;

/**
 * 一个读取数据库元属性工具包
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 * 
 */

public class DBMeta {

	/**
	 * @see DBMeta#createDBMetaInfo(DBMetaConfig)
	 */

	public static DBMetaInfo createDBMetaInfo(String propPath) {
		return createDBMetaInfo(new DBMetaConfig(propPath));
	}

	/**
	 * @see DBMeta#createDBMetaInfo(DBMetaConfig)
	 */

	public static DBMetaInfo createDBMetaInfo(String driver, String url, String user, String password) {
		return createDBMetaInfo(new DBMetaConfig(driver, url, user, password));
	}

	/**
	 * @see DBMetaInfo#getDBMetaInfo(DBMetaConfig)
	 */

	public static DBMetaInfo createDBMetaInfo(DBMetaConfig config) {
		return DBMetaInfo.getDBMetaInfo(config);
	}

	/**
	 * @see DBMeta#createDBMetaInfo(Connection)
	 */

	public static DBMetaInfo createDBMetaInfo(DataSource dataSource) {
		try {
			return createDBMetaInfo(dataSource.getConnection());
		} catch (SQLException e) {
			throw new DBMetaException(e);
		}
	}

	/**
	 * @see DBMetaInfo#getDBMetaInfo(Connection)
	 */

	public static DBMetaInfo createDBMetaInfo(Connection conn) {
		return DBMetaInfo.getDBMetaInfo(conn);
	}

}
