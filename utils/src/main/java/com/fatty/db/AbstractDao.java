package com.fatty.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractDao<T> 
{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected abstract T readFromRS(ResultSet rs) throws SQLException;
	protected abstract Connection openConn();
	
	protected int executeNoneQuery(String sql) {
		return executeNoneQuery(sql, null);
	}

	protected int executeNoneQuery(String sql, Object[] params) {
		DbWatch watch = new DbWatch();
		int result = -1;
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(sql);
			return result;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			prepareCommand(pstmt, params);
			result = pstmt.executeUpdate();
		} catch (Exception ex) {
			String msg = String.format(DbMessageType.Format3, DbMessageType.Sql_Error, ex.getMessage(), "调用Sql语句" + sql + "出错");
			logger.error(msg, ex);
			ex.printStackTrace();
		} finally {
			logger.info(sql + "\t" + params + "\t"+ result);
			closeConnection(conn, pstmt);
			watch.commit(sql);
		}
		return result;
	}

	protected List<T> executeQueryList(String sql) {
		return executeQueryList(sql, null);
	}

	protected List<T> executeQueryList(String sql, Object[] params) 
	{
		DbWatch watch = new DbWatch();
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(sql);
			return null;
		}
		List<T> list = new ArrayList<T>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{
			pstmt = conn.prepareStatement(sql);
			prepareCommand(pstmt, params);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				list.add(readFromRS(rs));
			}
		} 
		catch (Exception ex) 
		{
			String msg = String.format(DbMessageType.Format3, DbMessageType.Sql_Error, ex.getMessage(), "调用Sql语句" + sql + "出错");
			logger.error(msg, ex);
		} 
		finally 
		{
			logger.info(sql + "\t" + params + "\t"+ list);
			watch.commit(sql);
			closeConnection(conn,pstmt,rs);
		}
		return list;
	}
	
	protected T executeQuerySingle(String sql) {
		return executeQuerySingle(sql, null);
	}

	protected T executeQuerySingle(String sql, Object[] params) 
	{
		DbWatch watch = new DbWatch();
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(sql);
			return null;
		}
		T t = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{
			pstmt = conn.prepareStatement(sql);
			prepareCommand(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				t = readFromRS(rs);
			}
		} 
		catch (Exception ex) 
		{
			String msg = String.format(DbMessageType.Format3, DbMessageType.Sql_Error, ex.getMessage(), "调用Sql语句" + sql + "出错");
			logger.error(msg, ex);
		} 
		finally 
		{
			logger.info(sql + "\t" + params +  "\t"+ t);
			watch.commit(sql);
			closeConnection(conn,pstmt,rs);
		}
		return t;
	}
	
	protected int executeQueryCount(String sql, Object[] params) {
		DbWatch watch = new DbWatch();
		int result = -1;
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(sql);
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			prepareCommand(pstmt, params);
			rs = pstmt.executeQuery();
			if ((rs != null) && (rs.next())) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			String msg = "统计Count出错, 调用Sql语句 : " + sql + "出错";
			logger.error(msg, e);
			result = -1;
		} finally {
			logger.info(sql + "\t" + params +  "\t"+ result);
			closeConnection(conn, pstmt, rs);
			watch.commit(sql);
		}
		return result;
	}

	protected int executeLastId(String sql, Object[] params) {
		DbWatch watch = new DbWatch();
		int result = -1;
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(sql);
			return result;
		}
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prepareCommand(pstmt, params);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception ex) {
			String msg = "获取自增id出错";
			logger.error(msg, ex);
			result = -1;
		} finally {
			logger.info(sql + "\t" + params +  "\t"+ result);
			closeConnection(conn, pstmt, rs);
			watch.commit(sql);
		}
		return result;
	}

	protected int executeLastId(String sql) {
		return executeLastId(sql, null);
	}

	protected boolean sqlBatch(String tableName, List<String> sqlComm) {
		if (sqlComm == null || sqlComm.size() == 0)
			return true;

		DbWatch watch = new DbWatch();
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(tableName);
			return false;
		}
		Statement stmt = null;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (String sqlString : sqlComm) {
				stmt.addBatch(sqlString);
			}
			stmt.executeBatch();
			return true;
		} catch (SQLException e) {
			String msg = "批处理执行SQL语句出错";
			logger.error(msg, e);
		} finally {
			closeConnection(conn, stmt);
			watch.commit(tableName);
		}
		return false;
	}

	protected String getDbName() {
		DbWatch watch = new DbWatch();		
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit("get DataBase Name");
			return "";
		}
		String temp = "";
		try {
			temp = conn.getCatalog();
			
		} catch (SQLException e) {
			logger.error("获取数据库名出错", e);
		}
		finally
		{
			closeConnection(conn);
		}
		return temp;

	}

	private void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.error(DbMessageType.Connect_Close, e);
		}
	}

	private void closeConnection(Connection conn, PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.clearParameters();
				pstmt.close();
				pstmt = null;
			}
		} catch (SQLException e) {
			logger.error(DbMessageType.Connect_Close, e);
		}
		closeConnection(conn);
	}
	
	private void closeConnection(Connection conn, Statement statement) {
		if (statement != null) {
			try {
				statement.clearBatch();
				statement.close();
			} catch (SQLException e) {
				logger.error(DbMessageType.Connect_Close, e);
			}
		}
		closeConnection(conn);
	}

	private void closeConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			logger.error(DbMessageType.Connect_Close, e);
		}
		closeConnection(conn, pstmt);
	}

	

	private void closeConnection(PreparedStatement pstmt, ResultSet rs) {
		try {
			if (pstmt != null) {
				Connection conn = pstmt.getConnection();
				closeConnection(conn, pstmt, rs);
			}
		} catch (SQLException e) {
			logger.error("关闭db连接时异常", e);
		}
	}

	private void prepareCommand(PreparedStatement pstmt, Object[] parms) throws SQLException {
		if (pstmt == null || parms == null)
			return;
		for (int i = 0; i < parms.length; i++) {
			pstmt.setObject(i, parms[i]);
		}
	}

}
