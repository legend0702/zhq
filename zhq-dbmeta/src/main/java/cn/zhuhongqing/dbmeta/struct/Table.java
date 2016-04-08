package cn.zhuhongqing.dbmeta.struct;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Table {

	private String catalog;
	private String schema;
	private String name;
	private TableType type;
	private String remarks;

	private Set<Column> columns = Collections.emptySet();
	private HashMap<String, Column> columnMap = new HashMap<String, Column>();

	public Table() {

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

	/**
	 * @return the type
	 */
	public TableType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
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
		this.columns = columns;
		columnMap.clear();
		for (Column c : columns) {
			columnMap.put(c.getName(), c);
		}
	}

	public Set<Column> getColumns() {
		return columns;
	}

	public Column getColumn(String name) {
		return columnMap.get(name);
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
		return "Table [catalog=" + catalog + ", schema=" + schema + ", name="
				+ name + ", type=" + type + ", remarks=" + remarks + "]";
	}

}
