package com.fatty.db2code.bean;

import java.io.Serializable;

import com.fatty.common.StringUtil;
import com.fatty.db2code.utils.JavaTypeResolver;
import com.fatty.db2code.utils.JdbcTypeResolver;

public class Column implements Serializable {

	private static final long serialVersionUID = 1L;

	private String columnName;		// 字段名称
	private String columnType;		// 数据库中的类型
	private int    precision;		// 小数点后的位数
	private String javaType;		// 对应的java类型
	private int size;			    // 字段长度
	private String remarks;			// 字段注释
	private boolean nullable;		// 是否可以为空
	
	private int jdbcType;			// jdbc的Types数字 
	private String javaProperty;
	private String javaPropertyForGetSet;

	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
        this.javaProperty = StringUtil.getCamelCaseString(columnName, false);
        this.javaPropertyForGetSet = StringUtil.getCamelCaseString(columnName, true);
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
		this.jdbcType = JdbcTypeResolver.getInstance().getJdbcType(columnType.toUpperCase());
		this.javaType = JavaTypeResolver.getInstance().getJavaType(getJdbcType());
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public String getJavaType() {
		return javaType;
	}
	public int getJdbcType() {
		return jdbcType;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getJavaProperty() {
		return javaProperty;
	}
	public String getJavaPropertyForGetSet() {
		return javaPropertyForGetSet;
	}
	public boolean getNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	@Override
	public String toString() {
		return "Column [columnName=" + columnName + ", columnType=" + columnType + ", precision=" + precision
				+ ", javaType=" + javaType + ", size=" + size + ", remarks=" + remarks + ", nullable=" + nullable
				+ ", jdbcType=" + jdbcType + ", javaProperty=" + javaProperty + ", javaPropertyForGetSet="
				+ javaPropertyForGetSet + "]";
	}
}
