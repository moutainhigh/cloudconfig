package com.kuangchi.sdd.businessConsole.process.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.businessConsole.process.service.DeleteProcessVAriableQartzInterface;
import com.kuangchi.sdd.businessConsole.process.service.ProcessInstanceService;
@Service("deleteProcessVariableQuartz")
public class DeleteProcessVariableQuartz implements DeleteProcessVAriableQartzInterface {
	@Resource(name="processInstanceService")
	ProcessInstanceService processInstanceService;
	@Resource(name="cronServiceImpl")
	private ICronService cronService;
	
	@Override
	public void deleteProcessVariableDeleteCache() {
		//集群访问时，只有与数据库中相同的IP地址可以执行页面定时器的业务操作
		boolean r = cronService.compareIP();	
		if (r) {	
			
			List<String> processInstanceIdList=processInstanceService.getProcessVariableDeleteCache();
			for (int i = 0; i < processInstanceIdList.size(); i++) {
				processInstanceService.deleteHistoryVariable(processInstanceIdList.get(i));
				processInstanceService.deleteProcessVariableDeleteCache(processInstanceIdList.get(i));
			}
		}
	}
}
