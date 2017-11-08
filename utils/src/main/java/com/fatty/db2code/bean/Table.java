package com.fatty.db2code.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fatty.common.StringUtil;


/**
 * 数据库表的抽象，目前只是抽象了一个表名，以后可以继续扩展
 * @author coco
 *
 */
public class Table implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String tableName;
	private List<Column> columns = new ArrayList<Column>();
	
	private String tableAlias;		// 数据库表的别名，默认为表名，如果加了t_，v_前缀，则是除掉前缀的名字
	private String className;		// JAVA属性名称 大写开头
	private String javaProperty;	// JAVA属性名称 小写开头
    private Set<String> importList = new HashSet<String>();	// 导入的类的列表
    private Column primaryKey;

    /**
     * 此方法需要在构造Table的columns和keys完成之后调用
     */
    public List<Column> getBaseColumns() {
    	List<Column> baseColumns = new ArrayList<Column>();
    	baseColumns.addAll(columns);
    	baseColumns.remove(primaryKey);
    	return baseColumns;
    }
    
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
        this.tableName = tableName;
        if(tableName.startsWith("t_")||tableName.startsWith("v_")){
        	tableAlias=tableName.substring(2,tableName.length());
        }else{
        	this.tableAlias = tableName;
        }
        this.className = StringUtil.getCamelCaseString(tableAlias, true);
        this.javaProperty = StringUtil.getCamelCaseString(tableAlias, false);
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	public void addColumn(Column c) {
		columns.add(c);
	}
	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getJavaProperty() {
		return javaProperty;
	}

	public void setJavaProperty(String javaProperty) {
		this.javaProperty = javaProperty;
	}
	public Set<String> getImportList() {
		return importList;
	}

	public void setImportList(Set<String> importList) {
		this.importList = importList;
	}
	
	public Column getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Column primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public String toString() {
		return "Table [tableName=" + tableName + ", columns=" + columns + ", tableAlias=" + tableAlias + ", className="
				+ className + ", javaProperty=" + javaProperty + ", importList=" + importList + ", primaryKey="
				+ primaryKey + "]";
	}

}
