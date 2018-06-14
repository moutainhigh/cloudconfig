package com.kuangchi.sdd.businessConsole.activiti.dao;

import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;
import com.kuangchi.sdd.businessConsole.activiti.model.Template;

public interface TemplateDao {

	
	
	
 public	Template  getTemplateById(String id);
	
 public void updateTemplate(Template template);
	

	
}
