package cn.zhuhongqing.dbmeta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.zhuhongqing.DBMetaConst;
import cn.zhuhongqing.call.CallBackThr;
import cn.zhuhongqing.dbmeta.exception.DBMetaException;
import cn.zhuhongqing.dbmeta.struct.Column;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.dbmeta.struct.TableType;
import cn.zhuhongqing.dbmeta.type.SQLTypeMapping;
import cn.zhuhongqing.dbmeta.utils.DBUtil;
import cn.zhuhongqing.dbmeta.utils.DBUtil.CloseHelper;
import cn.zhuhongqing.dbmeta.utils.UnCatchSQLExceptionUtil;
import cn.zhuhongqing.exception.ValidationException;
import cn.zhuhongqing.util.GeneralUtils;
import cn.zhuhongqing.util.ReflectUtils;
import cn.zhuhongqing.util.StringUtils;

/**
 * 描述数据库元信息<br/>
 * 
 * 该类为主要的信息获取类 其他数据库类型请继承该类 由该类统一进行管理<br/>
 * 
 * 尽量遵循jdbc规范 以便用最少的代价实现信息获取功能<br/>
 * 
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 */

public abstract class DBMetaInfo implements Cloneable, DBMetaConst {

	/** 数据库对象的类型 */
	private static final String[] _TABLE_TYPE = new String[] { TableType.TABLE.name(), TableType.VIEW.name() };

	private Connection conn;
	private DatabaseMetaData dbMetaData;
	private DBMetaConfig config = DBMetaConfig.EMPTY_INSTANCE;
	/** @see #_TABLE_TYPE */
	private String[] tableType = _TABLE_TYPE;
	/** 默認目錄名 */
	private String catalog;
	/** 默认表空间名 */
	private String schema;
	private Set<String> catalogs;
	private Set<String> schemas;

	// ---------------------------------------- Static Method

	/**
	 * 根据链接类型返回对应的DBMetaInfo实例<br/>
	 * 链接生命周期由用户自行管理<br/>
	 * 如果在调用方法前关闭链接 会造成该方法调用失败 请注意<br/>
	 */

	public static final DBMetaInfo getDBMetaInfo(Connection conn) {
		try {
			String name = conn.getMetaData().getDatabaseProductName();
			return DBMetaPool.get(name, DBUtil.concatMajorAndMinor(conn.getMetaData())).init(conn);
		} catch (Exception e) {
			throw new DBMetaException(e);
		}
	}

	/**
	 * 追加设置配置信息
	 * 
	 * @see #getDBMetaInfo(Connection)
	 */

	public static final DBMetaInfo getDBMetaInfo(DBMetaConfig config) {
		DBMetaInfo info = getDBMetaInfo(config.createConn());
		info.setDBMetaConfig(config);
		return info;
	}

	// ---------------------------------------- Static Method End

	// ---------------------------------------- Final Method
	/**
	 * 获取默认表目录
	 */

	public final String getCatalog() {
		return catalog;
	}

	/**
	 * 获取所有能获取的目录
	 */

	public final Set<String> getCatalogs() {
		return catalogs;
	}

	/**
	 * 获取默认表空间
	 */

	public final String getSchema() {
		return schema;
	}

	/**
	 * 获取所有能获取的表空间
	 */

	public final Set<String> getSchemas() {
		return schemas;
	}

	/**
	 * 获取链接
	 */
	public final Connection getConnection() {
		return conn;
	}

	/**
	 * 获取数据库元信息
	 */

	protected final DatabaseMetaData getDatabaseMetaData() {
		return dbMetaData;
	}

	public DBMetaConfig getDBMetaConfig() {
		return config;
	}

	public void setDBMetaConfig(DBMetaConfig config) {
		this.config = config;
	}

	/**
	 * @see DBMetaInfo#tableType
	 */
	public final String[] getTableType() {
		return tableType;
	}

	/**
	 * @see DBMetaInfo#tableType
	 */
	public final void setTableType(String[] tableType) {
		this.tableType = tableType;
	}

	// ---------------------------------------- Final Method End

	// ---------------------------------------- General Method

	/**
	 * 根据默认的Catalog以及Schema获取表信息 <br>
	 * 
	 * @param catalog
	 * @param schema
	 * @return
	 * 
	 * @see DBMetaInfo#getTables(String, String)
	 */
	public Set<Table> getTables() {
		return getTables(getCatalog(), getSchema());
	}

	/**
	 * 根据默认的Catalog以及Schema获取指定的表信息 <br>
	 * 
	 * @param tableName
	 * @return
	 * 
	 * @see DBMetaInfo#getTable(String, String, String)
	 */

	public Table getTable(String tableName) {
		return getTable(getCatalog(), getSchema(), tableName);
	}

	/**
	 * 根据指定的Catalog以及Schema获取表信息 <br>
	 * 
	 * @param catalog
	 * @param schema
	 * @return
	 * 
	 * @see #getTables0(String, String, String)
	 */

	public Set<Table> getTables(String catalog, String schema) {
		if (StringUtils.hasEmpty(catalog, schema)) {
			throw new ValidationException("参数不能为空!");
		}
		return getTables0(catalog, schema, null);
	}

	/**
	 * 根据指定的Catalog以及Schema以及Table获取表信息 <br>
	 * 
	 * @param catalog
	 * @param shcema
	 * @param table
	 * @return
	 * 
	 * @see #getTables0(String, String, String)
	 */

	public Table getTable(String catalog, String schema, String table) {
		if (StringUtils.hasEmpty(catalog, schema, table)) {
			throw new ValidationException("参数不能为空!");
		}
		Set<Table> t = getTables0(catalog, schema, table);
		if (t.isEmpty()) {
			return null;
		}
		return t.iterator().next();
	}

	// ---------------------------------------- General Method End

	// ---------------------------------------- Abstract/Override Method
	/** 描述用于哪种数据库的 用于匹配数据库类型 */
	protected abstract String getDataBaseName();

	/**
	 * 用于多少版本的
	 * 
	 * 用int表示主要是想到适配的问题
	 * 
	 * 优先用最合适的版本 如果没有找到则选用当前最高的版本
	 * 
	 */
	protected abstract double getVersion();

	/**
	 * 返回默认目录<br/>
	 * 不能为null或者空值
	 */

	protected String initDefCatalog() throws Exception {
		return conn.getCatalog();
	}

	/**
	 * 返回所有目录<br/>
	 * 没有请返回空列表
	 */

	protected Set<String> initCatalogs() throws Exception {
		return UnCatchSQLExceptionUtil.getReslutSetOneColumn(dbMetaData.getCatalogs(), TABLE_CATALOG);
	}

	/**
	 * 返回默认表空间<br/>
	 * 不能为null或者空值
	 */

	protected String initDefSchema() throws Exception {
		return conn.getSchema();
	}

	/**
	 * 返回所有表空间<br/>
	 * 没有请返回空列表
	 */

	protected Set<String> initSchemas() throws Exception {
		return UnCatchSQLExceptionUtil.getReslutSetOneColumn(dbMetaData.getSchemas(), TABLE_SCHEME);
	}

	/**
	 * 初始化参数
	 */

	protected DBMetaInfo init(Connection conn) throws Exception {
		this.conn = conn;
		this.dbMetaData = conn.getMetaData();
		this.catalog = initDefCatalog();
		this.schema = initDefSchema();
		this.catalogs = initCatalogs();
		this.schemas = initSchemas();
		return this;
	}

	/**
	 * 根据指定的Catalog以及Schema以及Table获取表信息 <br>
	 * 以{@link #TABLE_TYPE}为获取类型进行获取
	 * 
	 * @param catalog
	 *            如果为空 则获取全部
	 * @param schema
	 *            如果为空 则获取全部
	 * @param table
	 *            如果为空 则获取全部
	 * @return
	 */

	private Set<Table> getTables0(String catalog, String schema, String table) {
		ResultSet rs = null;
		try {
			rs = getTableResultSet(catalog, schema, table);
			Set<Table> tables = new LinkedHashSet<Table>();
			UnCatchSQLExceptionUtil.reslutSetNext(rs, new CallBackThr<ResultSet>() {
				@Override
				public void invokeThr(ResultSet r) throws Exception {
					Table t = createTable(r, catalog, schema);
					t.setColumn(getColumns0(t));
					initPrimaryKeys(t);
					tables.add(t);
				}
			});
			return tables;
		} catch (Exception e) {
			throw new DBMetaException(e);
		} finally {
			CloseHelper.close(rs);
		}
	}

	/**
	 * 从数据库获取指定Catalog、Schema、Table的表信息<br/>
	 * 返回结果为ResultSet 与{@link DBMetaInfo#createTable(ResultSet)}相对应<br/>
	 * 如果子类重写了该方法 并且修改了返回的Key值
	 * 请将{@link DBMetaInfo#createTable(ResultSet)}也进行重写<br/>
	 * 
	 * 默认Key值与
	 * {@link DatabaseMetaData#getTables(String, String, String, String[])}一致<br/>
	 * 
	 * 
	 * @param catalog
	 * @param schema
	 * @param table
	 * @return
	 * @throws Exception
	 */

	protected ResultSet getTableResultSet(String catalog, String schema, String table) throws Exception {
		return UnCatchSQLExceptionUtil.getTables(dbMetaData, catalog, schema, table, tableType);
	}

	/**
	 * 返回Table对象时,请将catalog以及schema参数注入到该对象中 使Table对象完整 避免后续操作出现问题
	 * 
	 * @param rs
	 * @param schema
	 * @param catalog
	 * @throws Exception
	 * @see DBMetaInfo#getTableResultSet(String, String, String)
	 */

	protected Table createTable(ResultSet rs, String catalog, String schema) throws Exception {
		String tableName = rs.getString(TABLE_NAME);
		String tableType = rs.getString(TABLE_TYPE);
		String remarks = rs.getString(TABLE_REMARKS);
		Table t = new Table();
		t.setCatalog(catalog);
		t.setSchema(schema);
		t.setName(tableName);
		t.setType(TableType.valueOf(tableType));
		t.setRemarks(remarks);
		return t;
	}

	/**
	 * 根据指定Table信息 获取该表中的列信息
	 * 
	 * @param table
	 *            name(表名) 不应该为空 为空可能出现异常情况
	 *            如果name在数据库中存在重复情况,那么请指定catalog以及schema,避免出现异常情况
	 * @return
	 */

	private Set<Column> getColumns0(Table table) {
		ResultSet rs = null;
		try {
			rs = getColumnResultSet(table.getCatalog(), table.getSchema(), table.getName(), null);
			Set<Column> columns = new LinkedHashSet<Column>();
			UnCatchSQLExceptionUtil.reslutSetNext(rs, new CallBackThr<ResultSet>() {
				@Override
				public void invokeThr(ResultSet r) throws Exception {
					Column c = createColumn(r, table);
					columns.add(c);
				}
			});
			return columns;
		} catch (Exception e) {
			throw new DBMetaException(e);
		} finally {
			CloseHelper.close(rs);
		}
	}

	/**
	 * 从数据库获取指定Catalog、Schema、Table的列信息<br/>
	 * 返回结果为ResultSet 与{@link DBMetaInfo#createColumn(ResultSet)}相对应<br/>
	 * 如果子类重写了该方法 并且修改了返回的Key值
	 * 请将{@link DBMetaInfo#createColumn(ResultSet)}也进行重写<br/>
	 * 
	 * 默认Key值与
	 * {@link DatabaseMetaData#getColumns(String, String, String, String)}一致<br/>
	 * 
	 * @param catalog
	 * @param schema
	 * @param table
	 * @param column
	 * @return
	 * @throws Exception
	 */
	protected ResultSet getColumnResultSet(String catalog, String schema, String table, String column)
			throws Exception {
		return UnCatchSQLExceptionUtil.getColumns(dbMetaData, catalog, schema, table, column);
	}

	/**
	 * 返回Column对象时,请将table参数注入到该对象中 使Column对象完整 避免后续操作出现问题
	 * 
	 * @param rs
	 * @param table
	 * @see #getColumnResultSet(String, String, String, String)
	 */

	protected Column createColumn(ResultSet rs, Table table) throws Exception {
		String name = rs.getString(COLUMN_NAME);
		int sqlTypeInt = rs.getInt(COLUMN_DATA_TYPE);
		String typeName = rs.getString(COLUMN_TYPE_NAME);
		SQLType sqlType = _getSQLType(sqlTypeInt);
		Class<?> javaType = convertSQLTypeToJavaType(sqlTypeInt, typeName, sqlType);
		String defVal = rs.getString(COLUMN_DEF);
		String remarks = rs.getString(COLUMN_REMARKS);
		boolean isNullable = (DatabaseMetaData.columnNullable == rs.getInt(COLUMN_NULLABLE));
		int size = rs.getInt(COLUMN_SIZE);
		int decDigits = rs.getInt(COLUMN_DECIMAL_DIGITS);
		return new Column(table, name, sqlTypeInt, typeName, sqlType, javaType, isNullable, defVal, remarks, size,
				decDigits);
	}

	private SQLType _getSQLType(int sqlTypeInt) {
		SQLType type = getSQLType(sqlTypeInt);
		if (GeneralUtils.isNotNull(type)) {
			return type;
		}
		return SQLTypeMapping.getSQLType(sqlTypeInt);
	}

	/**
	 * 根据数据库int标识 返回一个{@link SQLType}
	 */

	protected SQLType getSQLType(int sqlTypeInt) {
		return null;
	}

	/**
	 * 根据{@link SQLType}以及TypeName找出对应的Java Type<br/>
	 * 1.根据用户的配置进行匹配<br/>
	 * 2.根据用户的全局配置进行匹配<br/>
	 * 2.根据子类的配置匹配<br/>
	 * 3.根据默认配置匹配<br/>
	 * 4.没匹配到的返回 Object.class<br/>
	 * 
	 */

	private Class<?> convertSQLTypeToJavaType(int sqlTypeInt, String typeName, SQLType type) {
		Class<?> jType = config.findJavaType(type);
		if (GeneralUtils.isNotNull(jType)) {
			return jType;
		}
		jType = config.findJavaType(typeName);
		if (GeneralUtils.isNotNull(jType)) {
			return jType;
		}
		jType = SQLTypeMapping.get(typeName);
		if (GeneralUtils.isNotNull(jType)) {
			return jType;
		}
		jType = findJavaType(sqlTypeInt, type);
		if (GeneralUtils.isNotNull(jType)) {
			return jType;
		}
		jType = findJavaType(typeName);
		if (GeneralUtils.isNotNull(jType)) {
			return jType;
		}
		jType = SQLTypeMapping.get(type);
		if (GeneralUtils.isNotNull(jType)) {
			return jType;
		}
		return Object.class;
	}

	/**
	 * 定义一些特殊的(比如该数据库特有的)类型映射
	 */
	protected Class<?> findJavaType(int sqlTypeInt, SQLType type) {
		return null;
	}

	/**
	 * 定义一些特殊的(比如该数据库特有的)类型映射
	 */
	protected Class<?> findJavaType(String typeName) {
		return null;
	}

	protected void initPrimaryKeys(Table table) {
		ResultSet rs = null;
		try {
			rs = getPrimaryKeysResultSet(table.getCatalog(), table.getSchema(), table.getName());
			UnCatchSQLExceptionUtil.reslutSetNext(rs, new CallBackThr<ResultSet>() {
				@Override
				public void invokeThr(ResultSet r) throws Exception {
					setPrimaryKeys(table, r);
				}
			});
		} catch (Exception e) {
			throw new DBMetaException(e);
		} finally {
			CloseHelper.close(rs);
		}
	}

	protected ResultSet getPrimaryKeysResultSet(String catalog, String schema, String table) throws Exception {
		return UnCatchSQLExceptionUtil.getPrimaryKeys(dbMetaData, catalog, schema, table);
	}

	protected void setPrimaryKeys(Table table, ResultSet r) throws SQLException {
		table.addPrimaryColumn(r.getString(COLUMN_NAME));
	}

	@Override
	protected DBMetaInfo clone() {
		try {
			return (DBMetaInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			return ReflectUtils.newInstanceWithoutArgs(this.getClass());
		}
	}

}
