package com.kuangchi.sdd.visitorConsole.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.util.mail.service.MailService;
import com.kuangchi.sdd.visitorConsole.connCs.service.ConnCsService;
public class EmailOfOverTimeLeave {
	
	@Resource(name = "MailServiceImpl")
	private MailService mailService;

	@Resource(name="cronServiceImpl")
	private ICronService cronService;
	    
	@Autowired
	ConnCsService connCsService;
	
	static Semaphore semaphoreA = new Semaphore(1);
	
	/**
	 * 将超时与超时后离开的记录发送给相关负责人
	 * by gengji.yang
	 */
	public void sendEmail(){
		boolean getResource = false;
		try {
			getResource = semaphoreA.tryAcquire(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try{
			if (getResource) {
				boolean r = cronService.compareIP();
				if(r){
					Integer total=connCsService.countOverTimeRecd();
					List<Map> list=connCsService.getNYetEmailRecd();
					if(list!=null&&list.size()>0){
						//此处预留查询通知人的邮箱列表
						List<Map> informPeopleList=connCsService.getInformList();
						if(informPeopleList!=null && informPeopleList.size()>0){
							for(Map m:informPeopleList){
								if(m.get("email")!=null){
									m.put("total", total);
									mailService.sendFkOverTimeEmail((String) m.get("email"),m);
									//break;//每次扫描发送一封邮件就可以
								}
							}
							
						}
						connCsService.updateEmailState(list);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			semaphoreA.release();
		}
	}




}
