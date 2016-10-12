package com.ldb.vocabulary2.server.controller.android;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ldb.vocabulary2.server.service.IProjectService;
import com.ldb.vocabulary2.server.service.impl.ProjectService;

public class ProjectInitListener implements ServletContextListener {

	IProjectService service = new ProjectService();
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		service.initProject();
	}
	

}
