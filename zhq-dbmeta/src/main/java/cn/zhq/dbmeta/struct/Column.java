package cn.zhq.dbmeta.struct;

import java.sql.JDBCType;

public class Column {

	private Table table;
	private String name;
	private JDBCType dataType;
	private String typeName;
	private String remarks;
	private boolean nullable;
	private Object defValue;
	private int size;
	private int decimalDigits;

	public Column() {
	}

	public Column(Table table, boolean nullable, String name,
			JDBCType dataType, String typeName, Object defValue,
			String remarks, int size, int decimalDigits) {
		this.table = table;
		this.nullable = nullable;
		this.name = name;
		this.dataType = dataType;
		this.typeName = typeName;
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

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JDBCType getDataType() {
		return dataType;
	}

	public void setDataType(JDBCType dataType) {
		this.dataType = dataType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Object getDefValue() {
		return defValue;
	}

	public void setDefValue(Object defValue) {
		this.defValue = defValue;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
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
		return "Column [table=" + table + ", name=" + name + ", dataType="
				+ dataType + ", typeName=" + typeName + ", remarks=" + remarks
				+ ", nullable=" + nullable + ", defValue=" + defValue
				+ ", size=" + size + ", decimalDigits=" + decimalDigits + "]";
	}

}
