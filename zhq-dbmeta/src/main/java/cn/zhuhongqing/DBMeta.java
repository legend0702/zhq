package cn.zhuhongqing;

import java.sql.Connection;

import cn.zhuhongqing.dbmeta.DBMetaInfo;
import cn.zhuhongqing.dbmeta.cfg.ConnectionInfo;

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
 * @since 1.8
 *
 */

public class DBMeta {

	/**
	 * @see DBMeta#createDBMetaInfo(Connection)
	 */

	public static DBMetaInfo createDBMetaInfo(String propPath) {
		return createDBMetaInfo(new ConnectionInfo(propPath));
	}

	/**
	 * @see DBMeta#createDBMetaInfo(Connection)
	 */

	public static DBMetaInfo createDBMetaInfo(String driver, String url,
			String user, String password) {
		return createDBMetaInfo(new ConnectionInfo(driver, url, user, password));
	}

	/**
	 * @see DBMeta#createDBMetaInfo(Connection)
	 */

	public static DBMetaInfo createDBMetaInfo(ConnectionInfo info) {
		return createDBMetaInfo(info.createConn());
	}

	/**
	 * 根据链接类型获取一个{@link DBMetaInfo}实例
	 */

	public static DBMetaInfo createDBMetaInfo(Connection conn) {
		return DBMetaInfo.getDBMetaInfo(conn);
	}

}
