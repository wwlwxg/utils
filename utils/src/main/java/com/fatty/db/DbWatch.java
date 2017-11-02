package com.fatty.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fatty.common.TimeUtil;

public class DbWatch {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private long first = 0;
	private long second = 0;

	public DbWatch() {
		first = TimeUtil.getSysCurTimeMillis();
	}

	public void getPool() {
		second = TimeUtil.getSysCurTimeMillis();
	}

	public void commit(String procName) {
		long end = TimeUtil.getSysCurTimeMillis();		
		long spendTime = end - first;
		if (spendTime > 1000) 
		{
			logger.error(String.format("执行语句%s花耗时间总时间 超过:%sms,获取连接：%sms,执行sql:%sms", procName, spendTime, second - first, end - second));
		}
	}
}
