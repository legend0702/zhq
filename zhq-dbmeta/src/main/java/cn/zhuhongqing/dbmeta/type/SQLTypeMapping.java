package cn.zhuhongqing.dbmeta.type;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.SQLType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理SQL Type ==> Java Class 的类型映射。
 * 
 * @author HongQing.Zhu
 *         <nl>
 *         <li>Mail:qwepoidjdj(a)gmail.com</li>
 *         <li>HomePage:www.zhuhongqing.cn</li>
 *         <li>Github:github.com/legend0702</li>
 *         </nl>
 *         PneumaticTokyo
 */

public final class SQLTypeMapping {

	private final Map<Integer, SQLTypeCouple> _SQL_TYPE_MAPPING = new HashMap<Integer, SQLTypeCouple>();
	/**
	 * 全局SQL_TYPE_NAME ==> Java Class的类型映射
	 * 
	 * 由用户手动调用{@link #register(String, Class)}方法进行注册
	 * 
	 */
	private final Map<String, Class<?>> _GLOBAL_SQL_TYPE_MAPPING = new HashMap<String, Class<?>>();

	private SQLTypeMapping() {
		// byte
		_put(JDBCType.TINYINT, Byte.class);
		_put(JDBCType.VARBINARY, byte[].class);
		_put(JDBCType.BINARY, byte[].class);
		_put(JDBCType.BLOB, byte[].class);
		_put(JDBCType.LONGVARBINARY, byte[].class);

		// boolean
		_put(JDBCType.BIT, Boolean.class);
		_put(JDBCType.BOOLEAN, Boolean.class);

		// number
		_put(JDBCType.SMALLINT, Short.class);
		_put(JDBCType.REAL, Float.class);
		_put(JDBCType.INTEGER, Integer.class);
		_put(JDBCType.BIGINT, Long.class);
		_put(JDBCType.DOUBLE, Double.class);
		_put(JDBCType.FLOAT, Double.class);
		_put(JDBCType.NUMERIC, BigDecimal.class);
		_put(JDBCType.DECIMAL, BigDecimal.class);

		// string
		_put(JDBCType.CHAR, String.class);
		_put(JDBCType.NCHAR, String.class);
		_put(JDBCType.VARCHAR, String.class);
		_put(JDBCType.NVARCHAR, String.class);
		_put(JDBCType.LONGVARCHAR, String.class);
		_put(JDBCType.CLOB, String.class);
		_put(JDBCType.NCLOB, String.class);
		_put(JDBCType.LONGNVARCHAR, String.class);

		// data
		_put(JDBCType.DATE, LocalDateTime.class); // java.utils.Date
		_put(JDBCType.TIME, LocalTime.class); // java.sql.Time
		_put(JDBCType.TIMESTAMP, LocalDateTime.class); // java.sql.Timestamp
		_put(JDBCType.TIME_WITH_TIMEZONE, ZonedDateTime.class);
		_put(JDBCType.TIMESTAMP_WITH_TIMEZONE, ZonedDateTime.class);

		// misc
		_put(JDBCType.ROWID, String.class);
		_put(JDBCType.SQLXML, String.class);
		_put(JDBCType.ARRAY, Object.class);
		_put(JDBCType.DATALINK, Object.class);
		_put(JDBCType.DISTINCT, Object.class);
		_put(JDBCType.JAVA_OBJECT, Object.class);

		// unknow
		_put(JDBCType.REF, Object.class);
		_put(JDBCType.REF_CURSOR, Object.class);
		_put(JDBCType.STRUCT, Object.class);
		_put(JDBCType.NULL, Object.class);
		_put(JDBCType.OTHER, Object.class);
	}

	void _put(SQLType t, Class<?> c) {
		_SQL_TYPE_MAPPING.put(t.getVendorTypeNumber(), new SQLTypeCouple(t.getVendorTypeNumber(), t, c));
	}

	/**
	 * 允许用户进行全局注册 不过通过该功能注册的对象优先级非常低 建议使用其他方式
	 */

	public static void put(SQLType type, Class<?> clazz) {
		SQLTypeMappingInstance.INSTANCE._put(type, clazz);
	}

	public static Class<?> get(SQLType type) {
		return SQLTypeMappingInstance.INSTANCE._SQL_TYPE_MAPPING.get(type.getVendorTypeNumber()).getClassType();
	}

	public static Class<?> get(Integer type) {
		return SQLTypeMappingInstance.INSTANCE._SQL_TYPE_MAPPING.get(type).getClassType();
	}

	public static SQLType getSQLType(Integer type) {
		return SQLTypeMappingInstance.INSTANCE._SQL_TYPE_MAPPING.get(type).getSQLType();
	}

	/**
	 * 注册全局SQL_TYPE_NAME ==> Java Class 映射
	 */

	public static void register(String typeName, Class<?> type) {
		SQLTypeMappingInstance.INSTANCE._GLOBAL_SQL_TYPE_MAPPING.put(typeName.toLowerCase(), type);
	}

	/**
	 * 获得全局的SQL_TYPE_NAME ==> Java Class 映射
	 */

	public static Class<?> get(String typeName) {
		return SQLTypeMappingInstance.INSTANCE._GLOBAL_SQL_TYPE_MAPPING.get(typeName.toLowerCase());
	}

	static class SQLTypeMappingInstance {
		static SQLTypeMapping INSTANCE = new SQLTypeMapping();
	}

}
