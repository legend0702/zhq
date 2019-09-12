package cn.zhuhongqing.dbmeta.struct;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.zhuhongqing.util.GeneralUtils;

public class Table {

	private String catalog;
	private String schema;
	private String name;
	private TableType type;
	private String remarks;

	private Set<Column> primaryColumns = new LinkedHashSet<Column>(4);
	private LinkedHashMap<String, Column> columnMap = new LinkedHashMap<String, Column>();

	public Table() {

	}

	public Table(String tableName) {
		this.name = tableName;
	}

	public Table(String catalog, String schema) {
		this.catalog = catalog;
		this.schema = schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TableType getType() {
		return type;
	}

	public void setType(TableType type) {
		this.type = type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setColumn(Set<Column> columns) {
		primaryColumns.clear();
		columnMap.clear();
		for (Column c : columns) {
			columnMap.put(c.getName(), c);
		}
	}

	public Set<Column> getPrimaryColumns() {
		return new LinkedHashSet<Column>(primaryColumns);
	}

	public Column addPrimaryColumn(String primaryColumnName) {
		Column c = getColumn(primaryColumnName);
		if (GeneralUtils.isNotNull(c)) {
			c.setPrimary(true);
			this.primaryColumns.add(c);
		}
		return c;
	}

	public Column getColumn(String name) {
		return columnMap.get(name);
	}

	public Set<Column> getColumns() {
		return new LinkedHashSet<Column>(columnMap.values());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
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
		Table other = (Table) obj;
		if (catalog == null) {
			if (other.catalog != null)
				return false;
		} else if (!catalog.equals(other.catalog))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (schema == null) {
			if (other.schema != null)
				return false;
		} else if (!schema.equals(other.schema))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Table [catalog=" + catalog + ", schema=" + schema + ", name=" + name + ", type=" + type + ", remarks="
				+ remarks + "]";
	}

}
