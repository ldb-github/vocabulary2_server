package com.ldb.vocabulary2.server.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.ldb.util.jdbc.JdbcUtil_DBUtils;
import com.ldb.vocabulary2.server.dao.ICommonDao;
import com.ldb.vocabulary2.server.domain.SystemParameter;

public class CommonDao implements ICommonDao{

	
	@Override
	public boolean isExist(String name) throws SQLException {
		String sql = " SELECT COUNT(*) COUNT FROM SYSTEMPARAMETER WHERE NAME = ? ";
		Object[] params = { name };
		
		ResultSetHandler<List<Object>> rsh = new ColumnListHandler<Object>("count");
		QueryRunner qr = new QueryRunner();
		List<Object> value = qr.query(JdbcUtil_DBUtils.getConnection(), sql, rsh, params);
		int count = Integer.parseInt(value.get(0).toString());
		return count > 0;
	}

	@Override
	public void addParameter(SystemParameter parameter) throws SQLException {
		String sql = " INSERT INTO SYSTEMPARAMETER(NAME, VALUE, EXPLANATION) VALUES(?, ?, ?) ";
		Object[] params = { 
				parameter.getName(),
				parameter.getValue(),
				parameter.getExplanation() };
		QueryRunner qr = new QueryRunner();
		qr.update(JdbcUtil_DBUtils.getConnection(), sql, params);
	}
	@Override
	public SystemParameter getParameterByName(String name) throws SQLException {
		String sql = " SELECT * FROM SYSTEMPARAMETER WHERE NAME = ? ";
		Object[] params = { name };
		
		ResultSetHandler<SystemParameter> rsh = new BeanHandler<>(SystemParameter.class);
		QueryRunner qr = new QueryRunner();
		SystemParameter parameter = qr.query(JdbcUtil_DBUtils.getConnection(), sql, rsh, params);
		if(parameter == null){
			throw new SQLException("参数不存在");
		}
		return parameter;
	}

	@Override
	public String getParameterValue(String name) throws SQLException {
		String sql = " SELECT VALUE FROM SYSTEMPARAMETER WHERE NAME = ? ";
		Object[] params = { name };
		
		ResultSetHandler<List<String>> rsh = new ColumnListHandler<String>("value");
		QueryRunner qr = new QueryRunner();
		List<String> value = qr.query(JdbcUtil_DBUtils.getConnection(), sql, rsh, params);
		if(value.isEmpty()){
			throw new SQLException("参数不存在");
		}
		return value.get(0);
	}
	
	/**
	 * 设计这个方法的意图：主要用来处理参数值需要递增并保持唯一性的业务参数，
	 * 防止业务层忘记调用{@link CommonDao#setParameter(String, String)}方法更新参数值.
	 */
	@Override
	public String getParameterValueForUpdate(String name, String update) throws SQLException {
		String oldValue = getParameterValue(name);
		String newValue = Integer.parseInt(oldValue) + Integer.parseInt(update) + "";
		setParameter(name, newValue);
		return oldValue;
	}

	@Override
	public void setParameter(String name, String value) throws SQLException {
		String sql = " UPDATE SYSTEMPARAMETER SET VALUE = ? WHERE NAME = ? ";
		Object[] params = { value, name };
		QueryRunner qr = new QueryRunner();
		qr.update(JdbcUtil_DBUtils.getConnection(), sql, params);
	}
	
}
