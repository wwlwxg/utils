<#macro selectolums><#list table.columns as c>${c.columnName}<#if c_has_next>,</#if></#list></#macro>
<#macro insertFlag><#list table.baseColumns as c><#if c_has_next>?,<#else>?</#if></#list></#macro>
<#macro insertcolumns><#list table.baseColumns as c>${c.columnName}<#if c_has_next>,</#if></#list></#macro>
<#macro updateColums><#list table.baseColumns as c><#if c_has_next>${c.columnName}=?,<#else>${c.columnName}=?</#if></#list></#macro>
<#macro updateVals><#list table.baseColumns as c><#if c_has_next>${table.javaProperty}.get${c.javaPropertyForGetSet}(),<#else>${table.javaProperty}.get${c.javaPropertyForGetSet}()</#if></#list></#macro>


import java.util.List;
import java.util.ArrayList;
<#list table.importList as i>
import ${i};
</#list>
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import dyl.common.util.JdbcTemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import dyl.common.util.Page;
/**

 * @author Dyl
 * ${sysTime}
 */
@Service
@Transactional
public class ${table.className}Dao {
	@Resource
	private JdbcTemplateUtil jdbcTemplate;
	/**
	 * 说明：分页查询表${table.tableName}记录封装成List集合
	 * @return List<${table.className}
	 */
	public List<${table.className}>  find${table.className}List(Page page,${table.className} ${table.javaProperty}) throws Exception{
		String sql = "select <@selectolums/> from ${table.tableName} t where 1=1";
		List<Object> con = new ArrayList<Object>();
		<#list table.columns as c>
		<#if c.javaType =='String'>
		if(StringUtils.isNotEmpty(${table.javaProperty}.get${c.javaPropertyForGetSet}())){
			sql+=" and t.${c.columnName} like ?";
			con.add("%"+${table.javaProperty}.get${c.javaPropertyForGetSet}()+"%");
		}
		<#else>
		if(${table.javaProperty}.get${c.javaPropertyForGetSet}()!=null){
			sql+=" and t.${c.columnName}=?";
			con.add(${table.javaProperty}.get${c.javaPropertyForGetSet}());
		}
		</#if>
		</#list>
		List<${table.className}> ${table.javaProperty}List =  jdbcTemplate.queryForListBeanByPage(sql, con, ${table.className}.class, page);
		
		return ${table.javaProperty}List;
	}
	<#if hasEdit?? && hasEdit>
	/**
	 * 说明：根据主键查询表${table.tableName}中的一条记录 
	 * @return ${table.className}
	 */
	public ${table.className} get${table.className}(${table.primaryKey.javaType}  ${table.primaryKey.javaProperty}) throws Exception{
		String sql = "select <@selectolums/> from ${table.tableName} where ${table.primaryKey.columnName}=?";
		${table.className} ${table.javaProperty}  =  jdbcTemplate.queryForBean(sql, new Object[]{${table.primaryKey.javaProperty}},${table.className}.class); 
		return ${table.javaProperty};
	}
	/**
	 * 说明：往表${table.tableName}中插入一条记录
	 * @return int >0代表操作成功
	 */
	public int insert${table.className}(${table.className} ${table.javaProperty}) throws Exception{
		String sql = "insert into ${table.tableName}(<@insertcolumns/>) values(<@insertFlag/>)";
		int returnVal=jdbcTemplate.update(sql,new Object[]{<#list table.baseColumns as c>${table.javaProperty}.get${c.javaPropertyForGetSet}()<#if c_has_next>,</#if></#list>});
		return returnVal;
	}
	/**
	 * 说明：根据主键更新表${table.tableName}中的记录
	 * @return int >0代表操作成功
	 */
	public int update${table.className}(${table.className} ${table.javaProperty}) throws Exception{
		String sql = "update ${table.tableName} t set ";
		List<Object> con = new ArrayList<Object>();
		<#list table.baseColumns as c>
		if(${table.javaProperty}.get${c.javaPropertyForGetSet}()!=null){
			sql+="t.${c.columnName}=?,";
			con.add(${table.javaProperty}.get${c.javaPropertyForGetSet}());
		}
		</#list>
		sql=sql.substring(0,sql.length()-1);
		sql+=" where ${table.primaryKey.columnName}=?";
		con.add(${table.javaProperty}.get${table.primaryKey.javaPropertyForGetSet}());
		int returnVal=jdbcTemplate.update(sql,con.toArray());
		return returnVal;
	}
	</#if>
	<#if hasDelete?? && hasDelete>
	/**
	 * 说明：根据主键删除表${table.tableName}中的记录
	 * @return int >0代表操作成功
	 */
	public int[] delete${table.className}(String  ${table.primaryKey.javaProperty}s) throws Exception{
		String sql = "delete from  ${table.tableName} where ${table.primaryKey.columnName}=?";
		String[] ${table.primaryKey.javaProperty}Arr  =  ${table.primaryKey.javaProperty}s.split(",");
		List<Object[]> paraList = new ArrayList<Object[]>(); 
		for (int i = 0; i < ${table.primaryKey.javaProperty}Arr.length; i++) {
			paraList.add(new Object[]{${table.primaryKey.javaProperty}Arr[i]});
		}
		return jdbcTemplate.batchUpdate(sql,paraList);
	}
	</#if>
}
