/**
 * 自动生成
 * @author coco
 * ${sysTime}
*/

import java.io.Serializable;
//import com.alibaba.fastjson.annotation.JSONField;
<#list table.importList as i>
import ${i};
</#list>

public class ${table.className} implements Serializable {
    private static final long serialVersionUID = 1L;
<#list table.columns as c>
	/*字段说明：${c.remarks!''}
	*对应db字段名:${c.columnName} 类型:${c.columnType}(<#if c.columnType=='DECIMAL'>${c.size},${c.precision}<#else>${c.size}</#if>)
	*是否可以为空:${c.nullable?string('是','否')}
	*/
	<#if c.javaType =='Date'>//@JSONField(format = "yyyy-MM-dd HH:mm:ss")</#if>
	private  ${c.javaType}  ${c.javaProperty};
	
</#list>
<#list table.columns as c>
    public ${c.javaType} get${c.javaPropertyForGetSet}() {
        return ${c.javaProperty};
    }
    public void set${c.javaPropertyForGetSet}(${c.javaType} ${c.javaProperty}) {
        this.${c.javaProperty} = ${c.javaProperty};
    }
</#list>
	public String toString() {
		return <#list table.columns as c>
		"${c.javaProperty}:"+${c.javaProperty}+"\n"+
		</#list>"";
	}
}
