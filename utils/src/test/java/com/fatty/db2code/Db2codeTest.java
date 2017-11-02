package com.fatty.db2code;

import java.sql.Connection;

import com.fatty.db.BoneCPDBPoolMgr;


public class Db2codeTest {
	public static void main(String[] args) throws Exception {

		String table = "finance";
		String destPath = Db2codeTest.class.getResource("").getFile().toString();
		Db2codeUtil util = new Db2codeUtil() {

			@Override
			protected Connection getConnection() {
				// TODO Auto-generated method stub
				BoneCPDBPoolMgr.init();
				return BoneCPDBPoolMgr.getConn();
			}
		};
		
		util.db2code(table,destPath);
		
	}
}
