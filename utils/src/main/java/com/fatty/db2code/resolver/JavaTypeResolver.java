/*
 * Copyright 2014 ptma@163.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fatty.db2code.resolver;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;



public class JavaTypeResolver {

    private static Map<Integer, String> typeMap;
    private static Map<Integer, String> fullTypeMap;

    private static final String         TYPE_BYTE            = "Byte";
    private static final String         TYPE_SHORT           = "Short";
    private static final String         TYPE_INTEGER         = "Integer";
    private static final String         TYPE_LONG            = "Long";
    private static final String         TYPE_FLOAT           = "Float";
    private static final String         TYPE_DOUBLE          = "Double";
    private static final String         TYPE_STRING          = "String";
    private static final String         TYPE_BOOLEAN         = "Boolean";
    private static final String         TYPE_DATE            = "Date";	// date, time , timestamp
    private static final String         TYPE_BIGDECIMAL      = "BigDecimal";
    private static final String         TYPE_BINARY          = "Byte[]";
    private static final String         TYPE_OTHER           = "Object";

    private static final String         FULL_TYPE_BYTE       = "java.lang.Byte";
    private static final String         FULL_TYPE_SHORT      = "java.lang.Short";
    private static final String         FULL_TYPE_INTEGER    = "java.lang.Integer";
    private static final String         FULL_TYPE_LONG       = "java.lang.Long";
    private static final String         FULL_TYPE_FLOAT      = "java.lang.Float";
    private static final String         FULL_TYPE_DOUBLE     = "java.lang.Double";
    private static final String         FULL_TYPE_STRING     = "java.lang.String";
    private static final String         FULL_TYPE_BOOLEAN    = "java.lang.Boolean";
    private static final String         FULL_TYPE_DATE       = "java.util.Date";
    private static final String         FULL_TYPE_BIGDECIMAL = "java.math.BigDecimal";

    static {
        typeMap = new HashMap<Integer, String>();

        typeMap.put(Types.BIGINT, TYPE_LONG);
        typeMap.put(Types.BIT, TYPE_BOOLEAN);
        typeMap.put(Types.BOOLEAN, TYPE_BOOLEAN);
        typeMap.put(Types.CHAR, TYPE_STRING);
        typeMap.put(Types.DATE, TYPE_DATE);
        typeMap.put(Types.DOUBLE, TYPE_DOUBLE);
        typeMap.put(Types.FLOAT, TYPE_DOUBLE);
        typeMap.put(Types.INTEGER, TYPE_INTEGER);
        typeMap.put(Types.REAL, TYPE_FLOAT);
        typeMap.put(Types.SMALLINT, TYPE_SHORT);
        typeMap.put(Types.TIME, TYPE_DATE);
        typeMap.put(Types.TIMESTAMP, TYPE_DATE);
        typeMap.put(Types.TINYINT, TYPE_BYTE);
        typeMap.put(Types.VARCHAR, TYPE_STRING);
        typeMap.put(Types.DECIMAL, TYPE_BIGDECIMAL);
        typeMap.put(Types.NUMERIC, TYPE_INTEGER);
        typeMap.put(Types.BINARY, TYPE_BINARY);
        typeMap.put(Types.OTHER, TYPE_OTHER);

        fullTypeMap = new HashMap<Integer, String>();

        fullTypeMap.put(Types.BIGINT, FULL_TYPE_LONG);
        fullTypeMap.put(Types.BIT, FULL_TYPE_BOOLEAN);
        fullTypeMap.put(Types.BOOLEAN, FULL_TYPE_BOOLEAN);
        fullTypeMap.put(Types.CHAR, FULL_TYPE_STRING);
        fullTypeMap.put(Types.DATE, FULL_TYPE_DATE);
        fullTypeMap.put(Types.DOUBLE, FULL_TYPE_DOUBLE);
        fullTypeMap.put(Types.FLOAT, FULL_TYPE_DOUBLE);
        fullTypeMap.put(Types.INTEGER, FULL_TYPE_INTEGER);
        fullTypeMap.put(Types.REAL, FULL_TYPE_FLOAT);
        fullTypeMap.put(Types.SMALLINT, FULL_TYPE_SHORT);
        fullTypeMap.put(Types.TIME, FULL_TYPE_DATE);
        fullTypeMap.put(Types.TIMESTAMP, FULL_TYPE_DATE);
        fullTypeMap.put(Types.TINYINT, FULL_TYPE_BYTE);
        fullTypeMap.put(Types.VARCHAR, FULL_TYPE_STRING);
        fullTypeMap.put(Types.DECIMAL, FULL_TYPE_BIGDECIMAL);
        fullTypeMap.put(Types.NUMERIC, FULL_TYPE_INTEGER);
        fullTypeMap.put(Types.BINARY, FULL_TYPE_BYTE);
    }
    
    private static final JavaTypeResolver instance = new JavaTypeResolver();
    
    private JavaTypeResolver(){
    }
    
    public static JavaTypeResolver getInstance() {
    	return instance;
    }
    
    /**
     * 根据Types数字获取java的类名
     * @param jdbcType
     * @return
     */
    public String getJavaType(int jdbcType) {
    	return typeMap.get(jdbcType);
    }
    
    /**
     * 根据Types数字获取java的全限定名
     * @param jdbcType
     * @return
     */
    public String getJavaFullType(int jdbcType) {
    	return fullTypeMap.get(jdbcType);
    }
}
