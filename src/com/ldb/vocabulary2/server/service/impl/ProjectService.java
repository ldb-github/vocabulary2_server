package com.ldb.vocabulary2.server.service.impl;

import java.sql.SQLException;

import com.ldb.vocabulary2.server.dao.ICommonDao;
import com.ldb.vocabulary2.server.dao.impl.CommonDao;
import com.ldb.vocabulary2.server.domain.Constants;
import com.ldb.vocabulary2.server.domain.SystemParameter;
import com.ldb.vocabulary2.server.service.IProjectService;


public class ProjectService implements IProjectService{

	private ICommonDao commonDao = new CommonDao();
	
	@Override
	public void initProject() {
		StringBuilder log = new StringBuilder("系统初始化开始...\n");
		SystemParameter parameter = new SystemParameter();
		try {
			parameter.clear();
			if(!commonDao.isExist(Constants.PARAMETER_CATEGORY_ID)){
				parameter.setName(Constants.PARAMETER_CATEGORY_ID);
				parameter.setValue("1");
				parameter.setExplanation("词汇类别id，作为CATEGORY的主键");
				commonDao.addParameter(parameter);
			}
			parameter.clear();
			if(!commonDao.isExist(Constants.PARAMETER_VOCABULARY_ID)){
				parameter.setName(Constants.PARAMETER_VOCABULARY_ID);
				parameter.setValue("1");
				parameter.setExplanation("词汇id，作为VOCABULARY的主键");
				commonDao.addParameter(parameter);
			}
		} catch (SQLException e) {
			log.append("系统初始化失败:\n");
			if(parameter.getName() != null){
				log.append("参数" + parameter.getName() + "初始化失败\n");
				log.append(e.getMessage() + "\n");
			}
		}finally{
			log.append("系统初始化结束\n");
			// TODO 输出日志
			System.out.println(log.toString());
		}
	}

	
}
