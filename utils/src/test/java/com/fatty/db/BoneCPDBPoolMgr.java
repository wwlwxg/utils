package com.fatty.db;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fatty.common.Config;
import com.fatty.db.pool.BoneCPDBPool;
import com.fatty.db.pool.IDBPool;

/**
 * 数据库连接池管理类
 * 
 */
public class BoneCPDBPoolMgr {
	
	private static final Logger logger = LoggerFactory.getLogger(BoneCPDBPoolMgr.class);

	private static IDBPool pool;

	public static boolean init() {
		createDbPool();

		return pool != null;
	}

	private static void createDbPool() {
		String poolName = Config.getString("db.pool.name", "db");
		String url = Config.getString("db.url",
				"jdbc:mysql://localhost/ledger_test?characterEncoding=utf-8&autoReconnect=true&useSSL=false");
		String user = Config.getString("db.user", "root");
		String pass = Config.getString("db.pass", "");
		int minconn = Config.getInteger("db.minconn", 3);
		int maxconn = Config.getInteger("db.maxconn", 30);
		int maxfllow = Config.getInteger("db.fllow", 2);

		pool = createPools(poolName, url, user, pass, minconn, maxconn, maxfllow);
	}

	private static IDBPool createPools(String name, String url, String user, String pass, int minconn, int maxconn,
			int maxfllow) {
		try {
			IDBPool pool = new BoneCPDBPool(name, url, user, pass, minconn, maxconn, maxfllow);
			String msg = String.format("加载配置连接池:%s,URL:%s完成！", name, url);
			logger.info(msg);
			return pool;
		} catch (Exception e) {
			logger.error("创建db连接池失败 poolName : " + name, e);
			return null;
		}
	}

	/**
	 * 检查连接池状态是否挂掉，如挂了重新初始化
	 */
	public static void checkAndReset() {
		if (pool != null) {
			if (pool.getCurConns() <= 0) {
				logger.error("检查pool发现异常,pool:" + pool.getState());
				pool.shutdown();
				pool = null;
				logger.error("重新初始化pool.................");
				createDbPool();
			} else {
				logger.error("检查pool正常,pool:" + pool.getState() + " 可用连接数:" + pool.getCurConns());
			}
		} else {
			logger.error("pool未初始化, 初始化.................");
			createDbPool();
		}

	}

	public static void closePools() {
		if (pool != null) {
			pool.shutdown();
			pool = null;
		}

		logger.info("数据库连接池关闭成功");
	}

	public static IDBPool getPool() {
		return pool;
	}
	
	public static Connection getConn() {
		return pool.getConnection();
	}

	public static String getPoolState() {
		return pool == null ? "" : pool.getState();
	}
}
