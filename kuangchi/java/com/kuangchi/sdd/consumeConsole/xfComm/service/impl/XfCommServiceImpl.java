package com.kuangchi.sdd.consumeConsole.xfComm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.xfComm.dao.XfCommDao;
import com.kuangchi.sdd.consumeConsole.xfComm.service.XfCommService;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-06
 * @功能描述: 消费通讯服务器-service实现层
 */

@Transactional
@Service("xfCommServiceImpl")
public class XfCommServiceImpl implements XfCommService {

	@Resource(name = "xfCommDaoImpl")
	private XfCommDao xfCommDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	/**
	 * 查询所有消费通讯服务器信息 by huixian.pan
	 */
	public Grid getXfCommIpMess(Map map) {
		List<Map> list = xfCommDao.getXfCommIpMess(map);
		Grid grid = new Grid();
		grid.setRows(list);
		if (null != list) {
			grid.setTotal(xfCommDao.countXfCommIpMess(map));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	/**
	 * 通过消费通讯服务器id查询通讯服务器信息 by huixian.pan
	 */
	public Map getXfCommIpById(String id) {
		return xfCommDao.getXfCommIpById(id);
	}

	/**
	 * 新增消费通讯服务器信息 by huixian.pan
	 */
	public boolean addXfCommIp(Map map, String create_user) {
		boolean result = xfCommDao.addXfCommIp(map);
		Map<String, String> log = new HashMap<String, String>();
		if (result) {
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "消费通讯服务器信息");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "新增消费通讯服务器信息成功");
			logDao.addLog(log);
		} else {
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "消费通讯服务器信息");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "新增消费通讯服务器信息失败");
			logDao.addLog(log);
			return false;
		}
		return true;
	}

	/**
	 * 删除消费通讯服务器信息 by huixian.pan
	 * */
	public boolean delXfCommIp(String id, String create_user) {
		boolean result = xfCommDao.delXfCommIp(id);
		Map<String, String> log = new HashMap<String, String>();
		if (result) {
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "消费通讯服务器信息");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "删除消费通讯服务器信息成功");
			logDao.addLog(log);
		} else {
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "消费通讯服务器信息");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "删除消费通讯服务器信息失败");
			logDao.addLog(log);
			return false;
		}
		return true;
	}

	/**
	 * 修改消费通讯服务器信息 by huixian.pan
	 * */
	public boolean updateXfCommIp(Map map, String create_user) {
		boolean result = xfCommDao.updateXfCommIp(map);
		Map<String, String> log = new HashMap<String, String>();
		if (result) {
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "消费通讯服务器信息");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "修改消费通讯服务器信息成功");
			logDao.addLog(log);
		} else {
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "消费通讯服务器信息");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "修改消费通讯服务器信息失败");
			logDao.addLog(log);
			return false;
		}
		return true;
	}

	/**
	 * 查询所有消费通讯服务器Ip by huixian.pan
	 * */
	public List<Map> getXfCommIp() {
		return xfCommDao.getXfCommIp();
	}

	/**
	 * 通过设备编号获取消费通讯服务器Url by huixian.pan
	 * */
	public String getXfCommUrl(String deviceNum) {
		Map map1 = new HashMap();
		map1.put("deviceNum", deviceNum);
		Map map = xfCommDao.getXfCommMessByNum(map1);
		String commUrl = "";
		if (map != null && map.size() > 0) {
			commUrl = "http://" + map.get("ip") + ":" + map.get("port")
					+ "/comm/";
		}
		return commUrl;
	}

	/**
	 * 判断是否已有通讯服务器 by huixian.pan
	 * */
	public boolean getXfCommIpCountByIp(Map map) {
		if (xfCommDao.getXfCommIpCountByIp(map) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过消费通讯服务器ip和端口号查询已有通讯服务器条数 by huixian.pan
	 * */
	public boolean ifXfCommIpUsed(String id) {
		if (xfCommDao.ifXfCommIpUsed(id) > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getXfCommIpIdByMap(Map map) {
		return xfCommDao.getXfCommIpIdByMap(map);
	}

	@Override
	public String getXfCommIpMaxId() {
		return xfCommDao.getXfCommIpMaxId();
	}

}
