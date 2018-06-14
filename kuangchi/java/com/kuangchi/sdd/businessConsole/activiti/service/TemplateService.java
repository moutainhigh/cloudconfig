package com.kuangchi.sdd.businessConsole.activiti.service;

import com.kuangchi.sdd.businessConsole.activiti.model.Template;

public interface TemplateService {
	
 public	Template  getTemplateById(String id);
	
 public void updateTemplate(Template template);
			
}
