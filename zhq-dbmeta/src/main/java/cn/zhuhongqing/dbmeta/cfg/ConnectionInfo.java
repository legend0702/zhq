package cn.zhuhongqing.dbmeta.cfg;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import cn.zhuhongqing.dbmeta.exception.DBMetaException;
import cn.zhuhongqing.dbmeta.utils.DBUtil;

/**
 * 链接数据库用的属性
 * 
 * @author HongQing.Zhu
 *
 */

public class ConnectionInfo extends Properties {

	private static final long serialVersionUID = 1L;

	private static final String DRIVER = "driver";
	private static final String URL = "url";
	private static final String USER = "user";
	private static final String PASSWORD = "password";

	public ConnectionInfo(String propPath) {
		try {
			load(ClassLoader.getSystemResourceAsStream(propPath));
		} catch (IOException e) {
			throw new DBMetaException(e);
		}
	}

	public ConnectionInfo(String driver, String url, String user,
			String password) {
		setDriver(driver);
		setUrl(url);
		setUser(user);
		setPassword(password);
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return getProperty(DRIVER);
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(String driver) {
		put(DRIVER, driver);
	}

	public String getUrl() {
		return getProperty(URL);
	}

	public void setUrl(String url) {
		put(URL, url);
	}

	public String getUser() {
		return getProperty(USER);
	}

	public void setUser(String user) {
		put(USER, user);
	}

	public String getPassword() {
		return getProperty(PASSWORD);
	}

	public void setPassword(String password) {
		put(PASSWORD, password);
	}

	public Connection createConn() {
		return DBUtil.createConnection(this);
	}

	@Override
	public String toString() {
		return "ConnectionInfo [driver=" + getDriver() + ", url=" + getUrl()
				+ ", user=" + getUser() + ", password=" + getPassword() + "]";
	}

}
