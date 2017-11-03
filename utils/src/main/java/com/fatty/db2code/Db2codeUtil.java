package com.fatty.db2code;
import java.io.File;
import java.sql.Connection;  
import java.sql.DatabaseMetaData;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;  
import java.util.Set;

import com.fatty.common.StringUtil;
import com.fatty.db2code.bean.Column;
import com.fatty.db2code.bean.Table;
import com.fatty.db2code.resolver.JavaTypeResolver;
import com.fatty.freemarker.FreemarkerUtils;

import freemarker.template.Configuration;  
  
/** 
 *  
 * <p>Description: 获取数据库基本信息的工具类</p> 
 *  
 * @author qxl 
 * @date 2016年7月22日 下午1:00:34 
 */  
public abstract class Db2codeUtil{
	
	protected abstract Connection getConnection();
    
	public void db2code(String tablePattern, String destDirPath) throws Exception {
    	File f = new File(destDirPath);
    	if(!f.exists())f.mkdir();
    	String templatePath=getClass().getResource("/").getFile().toString();
		//组装ftl需要的参数
    	HashMap<String, Object> root = new HashMap<String, Object>();
    	List<Table> tableList = getTableInfo(tablePattern);
    	for(Table t : tableList) {
			root.put("table", t);
			root.put("sysTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String templates[] =new String[]{
					"javaBean.ftl@"+t.getClassName()+".java"};
			Configuration cfg = FreemarkerUtils.getConfiguration(templatePath);
			for (int i = 0; i < templates.length; i++) {
				String arr[] = templates[i].split("@");
				FreemarkerUtils.createFile(cfg,arr[0],root,destDirPath+arr[1]);
			}
    	}
   	}
	
    /** 
     * 根据数据库的连接参数，获取指定表的基本信息：字段名、字段类型、字段注释
     * 目前只是支持单个表或者全部表，后续考虑支持include，exclude
     * @param table 表名 
     * @return Map集合 
     */  
    public List<Table> getTableInfo(String tablePattern){  
        Connection conn = null;
        DatabaseMetaData dbmd = null;
        
        List<Table> result = new ArrayList<Table>();

        try {
            conn = getConnection();
            dbmd = conn.getMetaData();
            ResultSet resultSet = dbmd.getTables(null, "%", tablePattern, new String[] { "TABLE" });
            while (resultSet.next()) {
                String tableName=resultSet.getString("TABLE_NAME");
                System.out.println(tableName);
                Table table = new Table();
                table.setTableName(tableName);
                if(tableName.equals(tablePattern) || "%".equals(tablePattern)){
                    ResultSet rs = conn.getMetaData().getColumns(null, getSchema(conn),tableName.toUpperCase(), "%");  

                    while(rs.next()){
                        Column c = new Column();
                        String colName = rs.getString("COLUMN_NAME");		// 字段名称
                        c.setColumnName(colName);
                        int columnSize = rs.getInt("COLUMN_SIZE");	// 字段长度
                        c.setSize(columnSize);
                        int precision = rs.getInt("DECIMAL_DIGITS");	// 小数部分的位数
                        c.setPrecision(precision);
                        String remarks = rs.getString("REMARKS");			// 字段注释(如果没有注释，则为字段名称)
                        if(StringUtil.isEmpty(remarks)){
                            remarks = colName;
                        }
                        c.setRemarks(remarks);
                        String columnType = rs.getString("TYPE_NAME");			// 字段类型
                        c.setColumnType(columnType);
                        boolean nullable = rs.getInt("NULLABLE") == 1 ? true : false;		// 0:不可为空，1：可以为空
                        c.setNullable(nullable);
                        table.addColumn(c);
                    }
                }
                table.setImportList(getImport(table.getColumns()));
                result.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }
    
	private  Set<String> getImport(List<Column> colums){
		Set<String> importSet = new HashSet<String>();
		for (Column c :colums){
			try {
				importSet.add(JavaTypeResolver.getInstance().getJavaFullType(c.getJdbcType()));
			} catch (Exception e) {
			}
		}
		return importSet;
	}

    private static String getSchema(Connection conn) throws Exception {
        String schema;
        schema = conn.getMetaData().getUserName();
        if ((schema == null) || (schema.length() == 0)) {
            throw new Exception("数据库模式不允许为空");
        }
        return schema.toUpperCase().toString();

    }

}  