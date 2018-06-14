package com.kuangchi.sdd.consumeConsole.xfComm.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-06
 * @功能描述: 消费通讯服务器-service层
 */
public interface XfCommService {

	/**
	 * 查询所有消费通讯服务器信息 by huixian.pan
	 */
	public Grid getXfCommIpMess(Map map);

	/**
	 * 通过消费通讯服务器id查询通讯服务器信息 by huixian.pan
	 */
	public Map getXfCommIpById(String id);

	/**
	 * 新增消费通讯服务器信息 by huixian.pan
	 */
	public boolean addXfCommIp(Map map, String create_user);

	/**
	 * 删除消费通讯服务器信息 by huixian.pan
	 * */
	public boolean delXfCommIp(String id, String create_user);

	/**
	 * 修改消费通讯服务器信息 by huixian.pan
	 * */
	public boolean updateXfCommIp(Map map, String create_user);

	/**
	 * 查询所有消费通讯服务器Ip by huixian.pan
	 * */
	public List<Map> getXfCommIp();

	/**
	 * 通过设备编号查询消费通讯服务器IP地址，port端口号 by huixian.pan
	 * */
	public String getXfCommUrl(String deviceNum);

	/**
	 * 判断是否已有通讯服务器 by huixian.pan
	 * */
	public boolean getXfCommIpCountByIp(Map map);

	/**
	 * 通过消费通讯服务器ip和端口号查询已有通讯服务器条数 by huixian.pan
	 * */
	public boolean ifXfCommIpUsed(String id);

	/**
	 * 查询所有消费通讯服务器信息 by xuewen.deng
	 */
	public String getXfCommIpIdByMap(Map map);

	/**
	 * 获取当前表中最大的id by xuewen.deng
	 */
	public String getXfCommIpMaxId();
}
