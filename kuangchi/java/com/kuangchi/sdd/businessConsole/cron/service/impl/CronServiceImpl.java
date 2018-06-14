package com.kuangchi.sdd.businessConsole.cron.service.impl;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.cron.dao.ICronDao;
import com.kuangchi.sdd.businessConsole.cron.model.Cron;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

@Transactional
@Service("cronServiceImpl")
public class CronServiceImpl extends BaseServiceSupport implements ICronService {

	@Resource(name = "cronDaoImpl")
	ICronDao cronDao;

	@Resource(name = "LogDaoImpl")
	LogDao logDao;

	@Override
	public Cron selectIP(String sys_key) {
		Cron cron = cronDao.selectIP(sys_key);
		
		// 若数据库没有指定的值，则从配置文件获取
		if(cron == null || cron.getSys_value() == null || "".equals(cron.getSys_value())){
			Map<String, String> propMap =
					PropertiesToMap.propertyToMap("timer_ip.properties"); 
			// 这里是为了减少代码修改，根据 Action所传参数进行判断
			String ip = "";
			if("ip".equals(sys_key)){
				ip = propMap.get("timer_ip");
			} else if("ip2".equals(sys_key)){
				ip = propMap.get("comm_timer_ip");
			}
			cron = new Cron();
			cron.setSys_key(sys_key);
			cron.setSys_value(ip);
		}
		return cron;
	}

	@Override
	public boolean updateCronIP(Cron cron, String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = cronDao.updateCronIP(cron);
		log.put("V_OP_NAME", "IP地址管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "修改执行的IP地址");
		if (result) {
			log.put("V_OP_TYPE", "业务");
		} else {
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public boolean compareIP() {
		boolean result = false;
		// 获取请求的服务器ip
		Enumeration<NetworkInterface> allNetInterfaces;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			List<String> arr = new ArrayList<String>();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				// System.out.println(netInterface.getName()); //网关
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						arr.add("'" + ip.getHostAddress() + "'");
					}
				}
			}
			String str = arr.toString().substring(1,
					arr.toString().length() - 1);
			Cron cron = new Cron();
			cron.setSys_value(str);
			Integer count = cronDao.compareIP(cron);
			if (count >= 1) { // IP相同
				result = true;
			}
			return result;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
	}

}
