package com.fatty.db2code;

import java.sql.Connection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fatty.db.BoneCPDBPoolMgr;


public class Db2codeTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Test
	public void main() throws Exception {

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
		logger.info("nihao");
		util.db2code(table,destPath);
		
	}
}
