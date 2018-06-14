package com.kuangchi.sdd.businessConsole.activiti.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.businessConsole.activiti.dao.TemplateDao;
import com.kuangchi.sdd.businessConsole.activiti.model.Template;
import com.kuangchi.sdd.businessConsole.activiti.service.TemplateService;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

	@Resource(name="templateDao")
	TemplateDao templateDao;
	@Override
	public Template getTemplateById(String id) {
		
		return templateDao.getTemplateById(id);
	}

	@Override
	public void updateTemplate(Template template) {
		templateDao.updateTemplate(template);
	}

	

	
	
}
