package cn.zhuhongqing.dbmeta;

import java.sql.Connection;
import java.sql.SQLType;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.zhuhongqing.dbmeta.type.SQLTypeMapping;
import cn.zhuhongqing.dbmeta.util.DBUtils;
import cn.zhuhongqing.util.GeneralUtils;

/**
 * DBMeta配置.
 *
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *
 */

public class DBMetaConfig {

	static final DBMetaConfig EMPTY_INSTANCE = new DBMetaConfig();
	/** 链接信息 */
	static final String DRIVER = "driver";
	static final String URL = "url";
	static final String USER = "user";
	static final String PASSWORD = "password";
	private Properties prop;

	/**
	 * 用于自定义的SQLType->Java Class<br/>
	 * 
	 * 优先级比{@link SQLTypeMapping}高
	 */
	private Map<SQLType, Class<?>> sqlTypeMapping = new HashMap<SQLType, Class<?>>();

	/**
	 * 用于自定义的SQLTypeName->Java Class<br/>
	 * 
	 * 当{@link #sqlTypeMapping}匹配不到明确的类型时 将会按名字进行匹配
	 */
	private Map<String, Class<?>> sqlTypeNameMapping = new HashMap<String, Class<?>>();

	DBMetaConfig() {
	};

	public DBMetaConfig(String propPath) {
		prop = GeneralUtils.loadProp(propPath);
	}

	public DBMetaConfig(String driver, String url, String user, String password) {
		setDriver(driver);
		setUrl(url);
		setUser(user);
		setPassword(password);
	}

	public String getDriver() {
		return get(DRIVER);
	}

	public void setDriver(String driver) {
		put(DRIVER, driver);
	}

	public String getUrl() {
		return get(URL);
	}

	public void setUrl(String url) {
		put(URL, url);
	}

	public String getUser() {
		return get(USER);
	}

	public void setUser(String user) {
		put(USER, user);
	}

	public String getPassword() {
		return get(PASSWORD);
	}

	public void setPassword(String password) {
		put(PASSWORD, password);
	}

	private String get(String key) {
		return prop.getProperty(key);
	}

	private void put(String key, String val) {
		prop.setProperty(key, val);
	}

	public Properties getConnectProp() {
		return prop;
	}

	public void setConnectProp(Properties prop) {
		this.prop = prop;
	}

	public Connection createConn() {
		return DBUtils.createConnection(this);
	}

	public Map<SQLType, Class<?>> getSqlTypeMapping() {
		return sqlTypeMapping;
	}

	public void setSqlTypeMapping(Map<SQLType, Class<?>> sqlTypeMapping) {
		this.sqlTypeMapping = sqlTypeMapping;
	}

	public void registerSQLTypeMapping(SQLType type, Class<?> jType) {
		sqlTypeMapping.put(type, jType);
	}

	public Class<?> findJavaType(SQLType type) {
		return sqlTypeMapping.get(type);
	}

	public Map<String, Class<?>> getSqlTypeNameMapping() {
		return sqlTypeNameMapping;
	}

	public void setSqlTypeNameMapping(Map<String, Class<?>> sqlTypeNameMapping) {
		this.sqlTypeNameMapping = sqlTypeNameMapping;
	}

	public void registerSQLTypeNameMapping(String type, Class<?> jType) {
		sqlTypeNameMapping.put(type, jType);
	}

	public Class<?> findJavaType(String typeName) {
		return sqlTypeNameMapping.get(typeName);
	}

}
