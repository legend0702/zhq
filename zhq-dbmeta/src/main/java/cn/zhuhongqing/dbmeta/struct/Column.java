package cn.zhuhongqing.dbmeta.struct;

import java.sql.SQLType;

public class Column {

	private Table table;
	private String name;
	private Integer sqlTypeInt;
	private String typeName;
	private SQLType sqlType;
	private Class<?> javaType;
	private String remarks;
	private boolean primary;
	private boolean nullable;
	private Object defValue;
	private int size;
	private int decimalDigits;

	public Column() {
	}

	public Column(Table table, String name, Integer sqlTypeInt, String typeName, SQLType sqlType, Class<?> javaType,
			boolean nullable, Object defValue, String remarks, int size, int decimalDigits) {
		this.table = table;
		this.name = name;
		this.sqlTypeInt = sqlTypeInt;
		this.typeName = typeName;
		this.sqlType = sqlType;
		this.javaType = javaType;
		this.nullable = nullable;
		this.defValue = defValue;
		this.remarks = remarks;
		this.size = size;
		this.decimalDigits = decimalDigits;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SQLType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SQLType sqlType) {
		this.sqlType = sqlType;
	}

	public Integer getSqlTypeInt() {
		return sqlTypeInt;
	}

	public void setSqlTypeInt(Integer sqlTypeInt) {
		this.sqlTypeInt = sqlTypeInt;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public Object getDefValue() {
		return defValue;
	}

	public void setDefValue(Object defValue) {
		this.defValue = defValue;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Column other = (Column) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Column [table=" + table + ", name=" + name + ", sqlType=" + sqlType + ", typeName=" + typeName
				+ ", javaType=" + javaType + ", remarks=" + remarks + ", primary=" + primary + ", nullable=" + nullable
				+ ", defValue=" + defValue + ", size=" + size + ", decimalDigits=" + decimalDigits + "]";
	}

}
