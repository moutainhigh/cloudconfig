package com.kuangchi.sdd.consumeConsole.xfComm.dao;

import java.util.List;
import java.util.Map;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-07
 * @功能描述: 消费通讯服务器-dao层
 */

public interface XfCommDao {

	/**
	 * 查询所有消费通讯服务器信息 by huixian.pan
	 */
	public List<Map> getXfCommIpMess(Map map);

	/**
	 * 查询所有消费通讯服务器信息条数 by huixian.pan
	 */
	public Integer countXfCommIpMess(Map map);

	/**
	 * 通过消费通讯服务器id查询通讯服务器信息 by huixian.pan
	 */
	public Map getXfCommIpById(String id);

	/**
	 * 新增消费通讯服务器信息 by huixian.pan
	 */
	public boolean addXfCommIp(Map map);

	/**
	 * 删除消费通讯服务器信息 by huixian.pan
	 * */
	public boolean delXfCommIp(String id);

	/**
	 * 修改消费通讯服务器信息 by huixian.pan
	 * */
	public boolean updateXfCommIp(Map map);

	/**
	 * 查询所有消费通讯服务器Ip by huixian.pan
	 * */
	public List<Map> getXfCommIp();

	/**
	 * 通过设备编号查询消费通讯服务器IP地址，port端口号 by huixian.pan
	 * */
	public Map getXfCommMessByNum(Map map);

	/**
	 * 通过消费通讯服务器ip和端口号查询已有通讯服务器条数 by huixian.pan
	 * */
	public Integer getXfCommIpCountByIp(Map map);

	/**
	 * 通过消费通讯服务器ip和端口号查询已有通讯服务器条数 by huixian.pan
	 * */
	public Integer ifXfCommIpUsed(String id);

	/**
	 * 查询所有消费通讯服务器信息 by xuewen.deng
	 */
	public String getXfCommIpIdByMap(Map map);

	/**
	 * 获取当前表中最大的id by xuewen.deng
	 */
	public String getXfCommIpMaxId();
}
