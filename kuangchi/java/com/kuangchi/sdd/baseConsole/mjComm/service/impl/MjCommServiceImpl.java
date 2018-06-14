package com.kuangchi.sdd.baseConsole.mjComm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.dao.MjCommDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-06 
 * @功能描述: 门禁通讯服务器-service实现层
 */

@Transactional
@Service("mjCommServiceImpl")
public class MjCommServiceImpl implements MjCommService{

	@Resource(name = "mjCommDaoImpl")
	private MjCommDao mjCommDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	 /**
	  * 查询所有门禁通讯服务器信息 
	  * by huixian.pan
	  */
   public Grid  getMjCommIpMess(Map map){
	   List<Map> list=mjCommDao.getMjCommIpMess(map);
	   Grid grid=new Grid();
	   grid.setRows(list);
	   if (null != list) {
           grid.setTotal(mjCommDao.countMjCommIpMess(map));
       } else {
           grid.setTotal(0);
       }
	   return grid;
   }
   
   /**
    * 通过门禁通讯服务器id查询通讯服务器信息  
    * by huixian.pan
    */
	 public Map getMjCommIpById(String id){
		 return mjCommDao.getMjCommIpById(id);
	 }
	
	 /**
	  * 新增门禁通讯服务器信息 
	  * by huixian.pan
	  */
	 public boolean addMjCommIp(Map map,String create_user){
		 boolean result=mjCommDao.addMjCommIp(map);
		 Map<String,String> log = new HashMap<String,String>();
		 if(result){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "门禁通讯服务器信息");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增门禁通讯服务器信息成功");
				logDao.addLog(log);
		 }else{
			    log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "门禁通讯服务器信息");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增门禁通讯服务器信息失败");
				logDao.addLog(log);
				return false;
		 }
		 return true;
	 }
	 
	 /**
	  * 删除门禁通讯服务器信息
	  * by huixian.pan 
	  * */
   public boolean delMjCommIp(String id,String create_user){
	   boolean result=mjCommDao.delMjCommIp(id);
	   Map<String,String> log = new HashMap<String,String>();
		 if(result){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "门禁通讯服务器信息");
				log.put("V_OP_FUNCTION", "删除");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "删除门禁通讯服务器信息成功");
				logDao.addLog(log);
		 }else{
			    log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "门禁通讯服务器信息");
				log.put("V_OP_FUNCTION", "删除");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "删除门禁通讯服务器信息失败");
				logDao.addLog(log);
				return false;
		 }
		 return true;
   }
   
   /**
    * 修改门禁通讯服务器信息
    * by huixian.pan
    * */
   public boolean updateMjCommIp(Map map,String create_user){
	   boolean result=mjCommDao.updateMjCommIp(map);
	   Map<String,String> log = new HashMap<String,String>();
		 if(result){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "门禁通讯服务器信息");
				log.put("V_OP_FUNCTION", "修改");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "修改门禁通讯服务器信息成功");
				logDao.addLog(log);
		 }else{
			    log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "门禁通讯服务器信息");
				log.put("V_OP_FUNCTION", "修改");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "修改门禁通讯服务器信息失败");
				logDao.addLog(log);
				return false;
		 }
		 return true;
   }



	
   /**
	  * 查询所有门禁通讯服务器Ip 
	  * by huixian.pan
	  * */
	 public List<Map>  getMjCommIp(){
		 return  mjCommDao.getMjCommIp();
	 }
	 
	 /**
	  *  通过设备编号获取门禁通讯服务器Url 
	  *  by huixian.pan
	  *  */
	 public String  getMjCommUrl(String deviceNum){
		 String commUrl="";
		 if(!"".equals(deviceNum) && null != deviceNum){
			 Map map1=new HashMap();
			 map1.put("deviceNum", deviceNum);
			 Map map=mjCommDao.getMjCommMessByNum(map1);
			 if(map != null && map.size()>0){
				commUrl="http://"+map.get("ip")+":"+map.get("port")+"/comm/";
			 }
		 }
		 return  commUrl;
	 }
	 
	 
	 /**
	  * 通过设备Mac地址获取设备编号   
	  * by huixian.pan
	  * */
	 public String  getTkDevNumByMac(String mac){
		 String deviceNum="";
		 if(!"".equals(mjCommDao.getMjDevNumByMac(mac)) && null != mjCommDao.getMjDevNumByMac(mac)){
			 deviceNum=mjCommDao.getMjDevNumByMac(mac);
		 }
		 return deviceNum;
	 }
	
	 /**
	  *  判断是否已有通讯服务器
	  *  by huixian.pan
	  *  */
	 public boolean  getMjCommIpCountByIp(Map map){
		if(mjCommDao.getMjCommIpCountByIp(map)>0){
			return true;
		}else{
			return false;
		} 
	 } 
	 
	 public boolean  getMjCommIpCountByIpA(Map map){
		 if(mjCommDao.getMjCommIpCountByIpA(map)>0){
			 return true;
		 }else{
			 return false;
		 } 
	 } 
	 
	 /** 
	  * 判断是否有设备使用此通讯服务器 
	  * by huixian.pan
	  * */
	 public boolean  ifMjCommIpUsed(String id){
		 if(mjCommDao.ifMjCommIpUsed(id)>0){
				return true;
			}else{
				return false;
			} 
	 }
	 
}
