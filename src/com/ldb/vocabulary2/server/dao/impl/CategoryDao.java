package com.ldb.vocabulary2.server.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.ldb.util.jdbc.JdbcUtil_DBUtils;
import com.ldb.vocabulary2.server.dao.ICategoryDao;
import com.ldb.vocabulary2.server.domain.Category;
import com.ldb.vocabulary2.server.domain.Vocabulary;

public class CategoryDao implements ICategoryDao{

	@Override
	public List<Category> getCategoryList(int page, int pageSize, String[] sort, 
			String[] sortType) throws SQLException {
		int minNo = pageSize * (page - 1) + 1;
		int maxNo = pageSize * page;
		int i = 0;
		String sortStr = "";
		if(sort != null && sort.length > 0){
			while(i < sort.length){
				if(sort[i].equals("f")){
					sortStr = sortStr + ", FAVORITECOUNT " + (sortType[i].equals("d") ? "DESC" : "ASC");
				}else if(sort[i].equals("w")){
					sortStr = sortStr + ", WORDCOUNT " + (sortType[i].equals("d") ? "DESC" : "ASC");
				}else if(sort[i].equals("t")){
					sortStr = sortStr + ", CREATETIME " + (sortType[i].equals("d") ? "DESC" : "ASC");
				}
				i++;
			}
		}
		if(!sortStr.equals("")){
			sortStr = "        ORDER BY " + sortStr.substring(1);
		}
		
		StringBuilder sql = new StringBuilder();
		sql
		.append("SELECT ID, NAME, IMAGE, IMAGEREMOTE, FAVORITECOUNT, WORDCOUNT, ")
		.append("USERNAME, CREATETIME, TRANSLATION FROM (")
		.append("    SELECT ROWNUM NO, ID, NAME, IMAGE, IMAGEREMOTE, FAVORITECOUNT,")
		.append("    WORDCOUNT, USERNAME, CREATETIME, TRANSLATION FROM (")
		.append("        SELECT * FROM CATEGORY ")
		.append(sortStr)
		.append("    )")
		.append(") WHERE NO >= ? AND NO <= ?");
		
		Object[] params = {minNo, maxNo};
		ResultSetHandler<List<Category>> rsh = new BeanListHandler<Category>(Category.class);
		QueryRunner qr = new QueryRunner();
		List<Category> categories = qr.query(JdbcUtil_DBUtils.getConnection(), sql.toString(), rsh, params);

		return categories;
	}

	@Override
	public void addCategory(Category category) throws SQLException {
		String sql = " INSERT INTO CATEGORY(ID, NAME, IMAGE, IMAGEREMOTE, FAVORITECOUNT, WORDCOUNT, " + 
					 " USERNAME, CREATETIME, TRANSLATION) " + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ";  
		Object[] params = {
				category.getId(),
				category.getName(),
				category.getImage(),
				category.getImageRemote(),
				category.getFavoriteCount(),
				category.getWordCount(),
				category.getUsername(),
				category.getCreateTime(),
				category.getTranslation()};
		
		QueryRunner qr = new QueryRunner();
		qr.update(JdbcUtil_DBUtils.getConnection(), sql, params);
	}

	
	@Override
	public void addVocabulary(Vocabulary vocabulary) throws SQLException {
		String sql = " INSERT INTO VOCABULARY(ID, CID, NAME, IMAGE, IMAGEREMOTE, " + 
				 " USERNAME, CREATETIME, TRANSLATION) " + " VALUES(?, ?, ?, ?, ?, ?, ?, ?) "; // 
		Object[] params = {
				vocabulary.getId(),
				vocabulary.getCId(),
				vocabulary.getName(),
				vocabulary.getImage(),
				vocabulary.getImageRemote(),
				vocabulary.getUsername(),
				vocabulary.getCreateTime(),
				vocabulary.getTranslation()};
		
		QueryRunner qr = new QueryRunner();
		qr.update(JdbcUtil_DBUtils.getConnection(), sql, params);
	}

	@Override
	public void addCategoryVocabulary(Vocabulary vocabulary) throws SQLException {
		String cId = vocabulary.getCId();
		
		String sql = " INSERT INTO CATEGORY_VOCABULARY(CID, VID) " + 
		      " VALUES(?, ?, ?, ?) ";
		Object[] params = {
				cId,
				vocabulary.getId()};
		
		QueryRunner qr = new QueryRunner();
		qr.update(JdbcUtil_DBUtils.getConnection(), sql, params);
	}

	@Override
	public List<Vocabulary> getVocabularyList(String categoryId, int page, int pageSize) throws SQLException {
		int minNo = pageSize * (page - 1) + 1;
		int maxNo = pageSize * page;
		
		StringBuilder sql = new StringBuilder();
		sql
		.append("SELECT ID, NAME, IMAGE, IMAGEREMOTE, USERNAME, CREATETIME, TRANSLATION FROM(")
		.append("    SELECT ROWNUM NO, ID, NAME, IMAGE, IMAGEREMOTE, USERNAME, CREATETIME, TRANSLATION FROM(")
		.append("    	SELECT * FROM VOCABULARY WHERE CID = ? ORDER BY ID ")
		.append("    )")
		.append(")WHERE NO >= ? AND NO <= ?");

		Object[] params = {categoryId, minNo, maxNo};
		ResultSetHandler<List<Vocabulary>> rsh = new BeanListHandler<Vocabulary>(Vocabulary.class);
		QueryRunner qr = new QueryRunner();
		List<Vocabulary> vocabularies = qr.query(JdbcUtil_DBUtils.getConnection(), sql.toString(), rsh, params);

		return vocabularies;
	}

	
	
}
