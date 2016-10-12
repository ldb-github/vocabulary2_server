package com.ldb.vocabulary2.server.dao;

import java.sql.SQLException;

import com.ldb.vocabulary2.server.domain.SystemParameter;

public interface ICommonDao {

	/**
	 * 判断参数是否已经存在
	 * @param name
	 * @return
	 * @throws SQLException 
	 */
	boolean isExist(String name) throws SQLException;
	/**
	 * 添加参数
	 * @param parameter
	 * @throws SQLException 
	 */
	void addParameter(SystemParameter parameter) throws SQLException;
	/**
	 * 根据参数名获取参数
	 * @param name
	 * @return
	 * @throws SQLException 
	 */
	SystemParameter getParameterByName(String name) throws SQLException;
	/**
	 * 获取参数值
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	String getParameterValue(String name) throws SQLException;
	/**
	 * 获取参数并更新
	 * @param name
	 * @param update
	 * @return
	 * @throws SQLException
	 */
	String getParameterValueForUpdate(String name, String update) throws SQLException;
	/**
	 * 设置参数
	 * @param name
	 * @param value
	 * @throws SQLException
	 */
	void setParameter(String name, String value) throws SQLException;
	
}
