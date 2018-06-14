package com.kuangchi.sdd.visitorConsole.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.kuangchi.sdd.visitorConsole.connCs.service.ConnCsService;

public class UpdateVisitorState {
	@Autowired
	ConnCsService connCsService;
	
	static Semaphore semaphoreA = new Semaphore(1);
	
	/**
	 * 若访客超时，则更新改访客的状态
	 * by gengji.yang
	 */
	public synchronized void updateState(){
		boolean getResource = false;
		try {
			getResource = semaphoreA.tryAcquire(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try{
			if (getResource) {
				
				connCsService.updateVisitorState();//访问超时,手机光钥匙的不处理
				connCsService.updateVisitorStateA();//预约超时，手机光钥匙的不处理
				connCsService.updateShouJiVisiting();//手机访客：预约》》正在访问
				//清除访问过期的手机卡信息，手机绑卡信息
				connCsService.deleteShouJiLeaveBoundCard();
				connCsService.deleteShouJiLeaveCard();
				//先清除访问过期的手机卡信息，手机绑卡信息，再更新状态，防止清除卡信息，绑卡信息时受历史访问记录的影响
				connCsService.updateShouJiLeaving();//手机访客：正在访问》》离开
				//清除过期的访客，包含访客，手机两处的访客的权限记录
				connCsService.delGuoQiMenJinQuanXianJilu();//清除过期门禁权限记录
				connCsService.delGuoQiTkQuanXianJilu();//清楚过期梯控权限记录
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			semaphoreA.release();
		}
	}
}
