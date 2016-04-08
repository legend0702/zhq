package cn.zhuhongqing.dbmeta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import cn.zhuhongqing.call.CallBack;
import cn.zhuhongqing.dbmeta.exception.DBMetaException;
import cn.zhuhongqing.dbmeta.struct.Column;
import cn.zhuhongqing.dbmeta.struct.Table;
import cn.zhuhongqing.dbmeta.struct.TableType;
import cn.zhuhongqing.dbmeta.utils.DBUtil.CloseHelper;
import cn.zhuhongqing.dbmeta.utils.UnCatchSQLExceptionUtil;
import cn.zhuhongqing.exception.ValidationException;
import cn.zhuhongqing.utils.ClassUtil;
import cn.zhuhongqing.utils.GeneralUtil;
import cn.zhuhongqing.utils.ReflectUtil;
import cn.zhuhongqing.utils.StringUtil;
import cn.zhuhongqing.utils.scan.ClassScan;

/**
 * 描述数据库元信息<br/>
 * 
 * 该类为主要的信息获取类 其他数据库类型请继承该类 由该类统一进行管理<br/>
 * 
 * 尽量遵循jdbc规范 以便用最少的代价实现信息获取功能<br/>
 * 
 * 请将子类包名设置为符合{@value #_DEF_PACKAGE}匹配符匹配的包名(例如cn.zhq.dbmeta.mysql) 以便被自动扫描加入管理<br/>
 * 
 * @author HongQing.Zhu
 *
 */

public abstract class DBMetaInfo implements Cloneable, DBMetaConst {

	/** 存储所有DBMetaInfo子类的实例化 子类必须要由无参的构造函数 不然初始化会失败 */
	private static final HashMap<String, DBMetaInfo> _META_MAP = new HashMap<String, DBMetaInfo>();
	/** 数据库对象的类型 */
	private static final String[] _TABLE_TYPE = new String[] {
			TableType.TABLE.name(), TableType.VIEW.name() };
	/** 默认子类的放置位置 */
	private static final String _DEF_PACKAGE = "cn.zhq.dbmeta.**";
	private static final String _DEF = "def";

	/** 扫描{DEF_PACKAGE}包下所有DBMetaInfo的子类 并注册进数据库信息管理中 */
	static {
		Set<Class<?>> classes = new ClassScan(c -> {
			if (ClassUtil.isOrdinaryAndDiectNewAndAssignable(DBMetaInfo.class,
					c)) {
				return c;
			}
			return null;
		}).getResources(_DEF_PACKAGE);
		classes.forEach(c -> {
			addDBMetaInfo((DBMetaInfo) ReflectUtil.autoNewInstance(c));
		});
	}

	private Connection conn;
	private DatabaseMetaData dbMetaData;
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
	 * 如果在调用方法前关闭链接 会造成方法调用失败 请注意<br/>
	 * 
	 * @param conn
	 * @return
	 */

	public static final DBMetaInfo getDBMetaInfo(Connection conn) {
		String url;
		DBMetaInfo info = null;
		try {
			url = conn.getMetaData().getURL();
			String urlLow = url.toLowerCase();
			Iterator<Entry<String, DBMetaInfo>> it = _META_MAP.entrySet()
					.iterator();
			while (it.hasNext()) {
				Entry<String, DBMetaInfo> e = it.next();
				if (urlLow.indexOf(e.getKey()) != -1) {
					info = e.getValue();
					break;
				}
			}
			if (GeneralUtil.isNull(info)) {
				info = _META_MAP.get(_DEF);
			}
			return info.clone().init(conn);
		} catch (Exception e) {
			throw new DBMetaException(e);
		}
		// throw new DBMetaException("没有找到链接为[" + url +
		// "]的DBMeta实现类,要不你来试着做一个?");
	}

	/**
	 * 增加一个新的DBMetaInfo实例
	 */

	public static final void addDBMetaInfo(DBMetaInfo metaInfo) {
		_META_MAP.put(metaInfo.getDBDesign().toLowerCase(), metaInfo);
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
	 * 
	 */

	public final Set<String> getSchemas() {
		return schemas;
	}

	/**
	 * 获取链接
	 * 
	 * 由外部自行关闭 内部操作时请不要关闭
	 */
	public final Connection getConnection() {
		return conn;
	}

	/**
	 * 获取数据库元信息
	 */

	public final DatabaseMetaData getDatabaseMetaData() {
		return dbMetaData;
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
		if (StringUtil.hasEmpty(catalog, schema)) {
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
		if (StringUtil.hasEmpty(catalog, schema, table)) {
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
	/** 描述该类是用于哪种数据库的 用于匹配数据库类型 */
	protected abstract String getDBDesign();

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
		return UnCatchSQLExceptionUtil.getReslutSetNextOneColumn(
				dbMetaData.getCatalogs(), TABLE_CATALOG);
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
		return UnCatchSQLExceptionUtil.getReslutSetNextOneColumn(
				dbMetaData.getSchemas(), TABLE_SCHEME);
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
			UnCatchSQLExceptionUtil.reslutSetNext(rs,
					new CallBack<ResultSet>() {
						@Override
						public void invokeThr(ResultSet r) throws Exception {
							Table t = createTable(r, catalog, schema);
							t.setColumn(getColumns0(t));
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
	 * 如果子类重写了该方法 并且修改了返回的Key值 请将{@link DBMetaInfo#createTable(ResultSet)}也进行重写<br/>
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

	protected ResultSet getTableResultSet(String catalog, String schema,
			String table) throws Exception {
		return UnCatchSQLExceptionUtil.getTables(dbMetaData, catalog, schema,
				table, tableType);
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

	protected Table createTable(ResultSet rs, String catalog, String schema)
			throws Exception {
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
			rs = getColumnResultSet(table.getCatalog(), table.getSchema(),
					table.getName(), null);
			Set<Column> columns = new LinkedHashSet<Column>();
			UnCatchSQLExceptionUtil.reslutSetNext(rs,
					new CallBack<ResultSet>() {
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
	 * 如果子类重写了该方法 并且修改了返回的Key值 请将{@link DBMetaInfo#createColumn(ResultSet)}也进行重写<br/>
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
	protected ResultSet getColumnResultSet(String catalog, String schema,
			String table, String column) throws Exception {
		return UnCatchSQLExceptionUtil.getColumns(dbMetaData, catalog, schema,
				table, column);
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
		String typeName = rs.getString(COLUMN_TYPE_NAME);
		int dataType = rs.getInt(COLUMN_DATA_TYPE);
		String defVal = rs.getString(COLUMN_DEF);
		String remarks = rs.getString(COLUMN_REMARKS);
		boolean isNullable = (DatabaseMetaData.columnNullable == rs
				.getInt(COLUMN_NULLABLE));
		int size = rs.getInt(COLUMN_SIZE);
		int decDigits = rs.getInt(COLUMN_DECIMAL_DIGITS);
		return new Column(table, isNullable, name, JDBCType.valueOf(dataType),
				typeName, defVal, remarks, size, decDigits);
	}

	@Override
	protected DBMetaInfo clone() {
		try {
			return (DBMetaInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			return ReflectUtil.newInstanceWithoutArgs(this.getClass());
		}
	}

}
