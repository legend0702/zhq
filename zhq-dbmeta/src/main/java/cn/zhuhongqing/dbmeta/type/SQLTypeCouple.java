package cn.zhuhongqing.dbmeta.type;

import java.sql.SQLType;

public class SQLTypeCouple {

	private int SQLTypeInt;
	private SQLType SQLType;
	private Class<?> classType;

	public SQLTypeCouple(int sQLTypeInt, java.sql.SQLType sQLType, Class<?> classType) {
		SQLTypeInt = sQLTypeInt;
		SQLType = sQLType;
		this.classType = classType;
	}

	public int getSQLTypeInt() {
		return SQLTypeInt;
	}

	public void setSQLTypeInt(int sQLTypeInt) {
		SQLTypeInt = sQLTypeInt;
	}

	public SQLType getSQLType() {
		return SQLType;
	}

	public void setSQLType(SQLType sQLType) {
		SQLType = sQLType;
	}

	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}

}
