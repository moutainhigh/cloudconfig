package com.kuangchi.sdd.consumeConsole.device.quardz;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.consumeConsole.xfComm.service.XfCommService;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;

/**
 * 清算账户、商户、员工、部门 定时器
 * 
 * @author minting.he
 * 
 */
public class SendPersonDownQuartz {
	@Resource(name = "xfCommServiceImpl")
	private XfCommService xfCommService;
	private ICronService cronService;

	public ICronService getCronService() {
		return cronService;
	}

	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}

	/**
	 * 消费设备下发名单任务
	 * 
	 * @author xuewen.deng
	 */
	public void SendPersonDownT() {
		try {
			// 集群访问时，只有与数据库中相同的IP地址可以执行定时器的业务操作
			boolean r = cronService.compareIP();
			if (r) {
				List<Map> commIpList = xfCommService.getXfCommIp();
				for (Map commIpMap : commIpList) {
					/*
					 * Map<String, String> map = PropertiesToMap
					 * .propertyToMap("comm_interface.properties"); String
					 * sendPersonDownUrl = map.get("comm_url") +
					 * "CDevice/SendPersonDownT.do?"; String
					 * sendPersonDownUrlStr = HttpRequest.sendPost(
					 * sendPersonDownUrl, "flag=1");
					 */
					String commUrl = "http://" + commIpMap.get("ip") + ":"
							+ commIpMap.get("port")
							+ "/comm/CDevice/SendPersonDownT.do?";
					String sendPersonDownUrlStr = HttpRequest.sendPost(commUrl,
							"flag=1");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
