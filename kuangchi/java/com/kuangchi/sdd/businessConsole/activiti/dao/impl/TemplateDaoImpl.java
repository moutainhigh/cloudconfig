package com.kuangchi.sdd.businessConsole.activiti.dao.impl;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.activiti.dao.TemplateDao;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;
import com.kuangchi.sdd.businessConsole.activiti.model.Template;
import com.kuangchi.sdd.businessConsole.test.model.TestBean;

@Repository("templateDao")
public class TemplateDaoImpl  extends BaseDaoImpl<Template> implements TemplateDao{

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.Activiti";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Template getTemplateById(String id) {
		return  (Template) this.getSqlMapClientTemplate().queryForObject("getTemplateById",id);
	}

	@Override
	public void updateTemplate(Template template) {
		this.getSqlMapClientTemplate().update("updateTemplate",template);
		
	}


	
	

}
