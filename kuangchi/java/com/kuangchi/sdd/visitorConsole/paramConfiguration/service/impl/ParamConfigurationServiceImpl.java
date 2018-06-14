package com.kuangchi.sdd.visitorConsole.paramConfiguration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.visitorConsole.connCs.service.ConnCsService;
import com.kuangchi.sdd.visitorConsole.paramConfiguration.dao.ParamConfigurationDao;
import com.kuangchi.sdd.visitorConsole.paramConfiguration.service.ParamConfigurationService;

/**
 * @创建人:    huixian.pan
 * @创建时间:   2016-11-09
 * @创建内容:   参数配置Service实现类 
 */

@Transactional
@Service("paramConfigurationServiceImpl")
public class ParamConfigurationServiceImpl implements ParamConfigurationService{
	
	@Resource(name = "paramConfigurationDaoImpl")
	private ParamConfigurationDao paramConfigurationDao;
	
	@Resource(name = "connCsService")
	private ConnCsService connCsService;
	
   @Resource(name="LogDaoImpl")
	private LogDao logDao;
   
   
   /*查看来访事宜信息到主页面*/
   public  Grid getVisitMatterInfo(Map map){
	    Grid grid=new Grid();
		grid.setRows(paramConfigurationDao.getVisitMatterInfo(map));
		grid.setTotal(paramConfigurationDao.countVisitMatterInfo(map));
		return grid;
   }
   
   /*查看重复来访事宜条数*/
   public  Integer getRepeatVisitMatter(Map map){
	   return   paramConfigurationDao.getRepeatVisitMatter(map);
   }
   
   /*新增来访事宜*/
   public boolean  addVisitMatter(Map map,String today, String create_user){
	   
			Map<String,String> log = new HashMap<String,String>();
			boolean result=paramConfigurationDao.addVisitMatter(map);
			if(result){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "来访事宜");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增来访事宜成功");
				logDao.addLog(log);
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "来访事宜");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增来访事宜失败");
				logDao.addLog(log);
				return false;
			}
				
				
			return true;
   }
   
   /*修改来访事宜*/
   public boolean  editVisitMatter(Map map,String today, String create_user){
	   
			Map<String,String> log = new HashMap<String,String>();
			boolean result=paramConfigurationDao.editVisitMatter(map);
			if(result){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "来访事宜");
				log.put("V_OP_FUNCTION", "修改");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "修改来访事宜成功");
				logDao.addLog(log);
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "来访事宜");
				log.put("V_OP_FUNCTION", "修改");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "修改来访事宜失败");
				logDao.addLog(log);
				return false;
			}
				
			return true;
	   
   }
   
   /*删除来访事宜*/
   public boolean  delVisitMatter(Map map,String today, String create_user){
	   
			Map<String,String> log = new HashMap<String,String>();
				boolean result=paramConfigurationDao.delVisitMatter(map);
				if(result){
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "来访事宜");
					log.put("V_OP_FUNCTION", "删除");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "删除来访事宜成功");
					logDao.addLog(log);
				}else{
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "来访事宜");
					log.put("V_OP_FUNCTION", "删除");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "删除来访事宜失败");
					logDao.addLog(log);
					return false;
				}
				
			return true;
	   
   }
   /*查询所有来访事宜（接口）*/
   public List<Map> getVisitMatterReg(){
	   return paramConfigurationDao.getVisitMatterReg();
   }
   
   
   
   
   /*查看携带物品信息到主页面*/
   public  Grid getCarryGoodsInfo(Map map){
	    Grid grid=new Grid();
		grid.setRows(paramConfigurationDao.getCarryGoodsInfo(map));
		grid.setTotal(paramConfigurationDao.countCarryGoodsInfo(map));
		return grid;
   }
   
   /*查看重复携带物品条数*/
   public  Integer getRepeatCarryGoods(Map map){
	   return  paramConfigurationDao.getRepeatCarryGoods(map);
   }
   
   /*新增携带物品*/
   public boolean  addCarryGoods(Map map,String today, String create_user){
	   
			Map<String,String> log = new HashMap<String,String>();
				boolean result=paramConfigurationDao.addCarryGoods(map);
				if(result){
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "携带物品");
					log.put("V_OP_FUNCTION", "新增");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "新增携带物品成功");
					logDao.addLog(log);
				}else{
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "携带物品");
					log.put("V_OP_FUNCTION", "新增");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "新增携带物品失败");
					logDao.addLog(log);
					return false;
				}
				
			return true;
	   
   }
   
   /*修改携带物品*/
   public boolean  editCarryGoods(Map map,String today, String create_user){
	   
			Map<String,String> log = new HashMap<String,String>();
				boolean result=paramConfigurationDao.editCarryGoods(map);
				if(result){
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "携带物品");
					log.put("V_OP_FUNCTION", "修改");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "修改携带物品成功");
					logDao.addLog(log);
				}else{
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "携带物品");
					log.put("V_OP_FUNCTION", "修改");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "修改携带物品失败");
					logDao.addLog(log);
					return false;
				}
				
				
			return true;
	   
   }
   
   /*删除携带物品*/
   public boolean  delCarryGoods(Map map,String today, String create_user){
	
			Map<String,String> log = new HashMap<String,String>();
				boolean result=paramConfigurationDao.delCarryGoods(map);
				if(result){
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "携带物品");
					log.put("V_OP_FUNCTION", "删除");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "删除携带物品成功");
					logDao.addLog(log);
				}else{
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "携带物品");
					log.put("V_OP_FUNCTION", "删除");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "删除携带物品失败");
					logDao.addLog(log);
					return false;
				}
				
			return true;
   }
   
   /*查询所有携带物品（接口）*/
   public List<Map> getCarryGoodsReg(){
	   return paramConfigurationDao.getCarryGoodsReg();
   }
   
   
   
   /*查看黑名单信息到主页面*/
   public  Grid getBlackListInfo(Map map){
        Grid grid=new Grid();
 		grid.setRows(paramConfigurationDao.getBlackListInfo(map));
 		grid.setTotal(paramConfigurationDao.countBlackListInfo(map));
 		return grid;
   }
   /*通过ID查看黑名单*/
   public  Map getBlackListById(Map map){
	   return  paramConfigurationDao.getBlackListById(map);
   }
   /*查看重复黑名单条数*/
   public  Integer getRepeatBlackList(Map map){
	   return  paramConfigurationDao.getRepeatBlackList(map);
   }
   /*新增黑名单*/
   public boolean  addBlackList(Map map,String today, String create_user){

		Map<String,String> log = new HashMap<String,String>();
		    boolean result1=paramConfigurationDao.addMainVisitor(map);
		    String id=(String) map.get("m_visitor_num");
		    String visitorNum=paramConfigurationDao.getMVisitorNum(id);
		    map.put("visitorNum", visitorNum);
			boolean result=paramConfigurationDao.addBlackList(map);
			if(result && result1){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "黑名单");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增黑名单成功");
				logDao.addLog(log);
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "黑名单");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增黑名单失败");
				logDao.addLog(log);
				return false;
			}
			
		return true;
   }
   /*修改黑名单*/
   public boolean  editBlackList(Map map,String today, String create_user){
	   Map<String,String> log = new HashMap<String,String>();
		boolean result=paramConfigurationDao.editBlackList(map);
		boolean result1=paramConfigurationDao.editMainVisitor(map);
		if(result && result1){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "黑名单");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "修改黑名单成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "黑名单");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "修改黑名单失败");
			logDao.addLog(log);
			return false;
		}
		
		
	return true;
   }
   /*删除黑名单*/
   public boolean  delBlackList(Map map,String today, String create_user){
	   Map<String,String> log = new HashMap<String,String>();
		boolean result=paramConfigurationDao.delBlackList(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "黑名单");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "删除黑名单成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "黑名单");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "删除黑名单失败");
			logDao.addLog(log);
			return false;
		}
		
	return true;
   }
   /*查询所有黑名单（接口）*/
   public List<Map> getBlackListReg(){
	   return  paramConfigurationDao.getBlackListReg();
   }
   
   /*查看通知名单信息到主页面*/
   public  Grid getNotifyListInfo(Map map){
	    Grid grid=new Grid();
		grid.setRows(paramConfigurationDao.getNotifyListInfo(map));
		grid.setTotal(paramConfigurationDao.countNotifyList(map));
		return grid;
   }
   
   /*新增通知名单*/
   public  boolean addNotifyList(List<String> staffNums, String create_user){
	   Map<String,String> log = new HashMap<String,String>();
	   for(String staffNum:staffNums){
		    Map map=new HashMap();
		    map.put("staffNum", staffNum);
		    boolean result1=paramConfigurationDao.delRepeatNotifyList(map);
			boolean result=paramConfigurationDao.addNotifyList(map);
			if(result && result1){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "通知名单");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增通知名单成功");
				logDao.addLog(log);
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "通知名单");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增通知名单失败");
				logDao.addLog(log);
				return false;
			}
	   }
	return true;
   }
   /*删除通知名单*/
   public  boolean delNotifyList(Map map, String create_user){
	   Map<String,String> log = new HashMap<String,String>();
		boolean result=paramConfigurationDao.delNotifyList(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "通知名单");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "删除通知名单成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "通知名单");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "删除通知名单失败");
			logDao.addLog(log);
			return false;
		}
		
	return true;
   }
   /*---------------------------接口后台语句起---------------------------------*/
   /*查询权限组*/
   public List<Map>  queryAuthGroup(){
	   return  paramConfigurationDao.queryAuthGroup();
   }
   /*查询权限信息*/
   public Map  queryAuthInfo(String groupNum){
	   Map map=new HashMap();
	   String dorAreaName="";
	   String dorDeviceName="";
	   String dorDoorName="";
	   String dorDoorNum="";
	   String eleDeviceName="";
	   String eleFloors="";
	   List<Map> list=paramConfigurationDao.queryAuthInfo(groupNum);
	   //List<Map> list=paramConfigurationDao.queryAuthInfo("1b68f170-c353-11e6-9334-0050569f7086");
	   if(list.size()>0){
		   for(Map m:list){
			   if("0".equals(m.get("deviceType"))){
				   dorAreaName+=m.get("deviceAreaName")+"|";
				   dorDeviceName+=m.get("deviceName")+"|";
				   dorDoorName+=m.get("objectName")+"|";
				   dorDoorNum+=m.get("objectNum")+"|";
				   
			   }else{
				   eleDeviceName+=m.get("deviceName")+"|";
				   eleFloors+=m.get("objectNum")+"&";
			   }
		   }
		   dorAreaName=!"".equals(dorAreaName)==true?dorAreaName.substring(0, dorAreaName.length()-1):"";
		   dorDeviceName=!"".equals(dorDeviceName)==true?dorDeviceName.substring(0,dorDeviceName.length()-1):"";
		   dorDoorName=!"".equals(dorDoorName)==true?dorDoorName.substring(0, dorDoorName.length()-1):"";
		   dorDoorNum=!"".equals(dorDoorNum)==true?dorDoorNum.substring(0, dorDoorNum.length()-1):"";
		   eleDeviceName=!"".equals(eleDeviceName)==true?eleDeviceName.substring(0, eleDeviceName.length()-1):"";
		   eleFloors=!"".equals(eleFloors)==true?eleFloors.substring(0, eleFloors.length()-1):"";
		   map.put("dorAreaName", dorAreaName);
		   map.put("dorDeviceName", dorDeviceName);
		   map.put("dorDoorName", dorDoorName);
		   map.put("dorDoorNum", dorDoorNum);
		   map.put("eleDeviceName", eleDeviceName);
		   map.put("eleFloors", eleFloors);
	   }
	   return map;
   }
   /*验证卡号*/
   public boolean  validateCardNum(String cardNum){
	   if(paramConfigurationDao.validateCardNum(cardNum)>0){
		   return  false;
	   }else{
		   return  true;
	   }
   }
   
   /*查询历史被访人员*/
   public Map  getHisVisitedPerson(Map map){
	   Map m=paramConfigurationDao.getHisVisitedPerson(map);
	   if(m == null){
		   m=new HashMap();
		   m.put("result", "1");
	   }else{
		   if(m.size()>0){
			 m.put("result", "0");   
		   }else{
			 m.put("result", "1");
		   }
	   }
	   return m;
   }
   /*来访查询（查询今日访客（正在访问，已离开，预约））*/
   public Map  queryTodayVisitor(Map map){
	   Map map2=new HashMap();

	   String mVisitorNum="";
	   String mVisitorName="";
	   String mVisitorSex="";
	   String mPaperType="";
	   String mPaperNum="";
	   String mMobile="";
	   String mVisitorCompany="";
	   String mAddress="";
	   
	   String pVisitorNum="";
	   String pVisitorName="";
	   String pVisitorSex="";
	   String pOryName="";
	   String pRoom="";
	   String pMobile="";
	   String pPhone="";
	   String staffNo="";
	   
	   String recordNum="";
	   String visitorCard="";
	   String carInfo="";
	   String visitMatter="";
	   String carryGoods="";
	   String visitDate="";
	   String validityDate="";
	   String identifyPhoto="";
	   String catchPhoto="";
	   String visitNumber="";
	   String visitCardType="";
	   String visitNum="";
	   String visitState="";
	   String leftDate="";
	   String cardState="";
	   String createUser="";
	   
	   List<Map> list=paramConfigurationDao.queryTodayVisitor(map);
	   if(list.size()>0){
		   for(Map m:list){
			       mVisitorNum+=m.get("mVisitorNum")+"|";
			       mVisitorName+=m.get("mVisitorName")+"|";
			       mVisitorSex+=m.get("mVisitorSex")+"|";
			       mPaperType+=m.get("mPaperType")+"|";
			       mPaperNum+=m.get("mPaperNum")+"|";
			       mMobile+=m.get("mMobile")+"|";
			       mVisitorCompany+=m.get("mVisitorCompany")+"|";
			       mAddress+=m.get("mAddress")+"|";
			       
			       pVisitorNum+=m.get("pVisitorNum")+"|";
			       pVisitorName+=m.get("pVisitorName")+"|";
			       pVisitorSex+=m.get("pVisitorSex")+"|";
			       pOryName+=m.get("pOryName")+"|";
			       pRoom+=m.get("pRoom")+"|";
			       pMobile+=m.get("pMobile")+"|";
			       pPhone+=m.get("pPhone")+"|";
			       staffNo+=m.get("staffNo")+"|";
			       
			       recordNum+=m.get("recordNum")+"|";
			       visitorCard+=m.get("cardNum")+"|";
			       carInfo+=m.get("carInfo")+"|";
			       visitMatter+=m.get("visitMatter")+"|";
			       carryGoods+=m.get("carryGoods")+"|";
			       visitDate+=m.get("visitDate")+"|";
			       validityDate+=m.get("validityDate")+"|";
			       identifyPhoto+=m.get("identifyPhoto")+"|";
			       catchPhoto+=m.get("catchPhoto")+"|";
			       visitNumber+=m.get("visitNumber")+"|";
			       visitCardType+=m.get("visitCardType")+"|";
			       visitNum+=m.get("visitNum")+"|";
			       visitState+=m.get("visitState")+"|";
			       leftDate+=m.get("leaveTime")+"|";
			       cardState+=m.get("cardState")+"|";
			       createUser+=m.get("createUser")+"|";
		   }
		   mVisitorNum=mVisitorNum.substring(0, mVisitorNum.length()-1);
		   mVisitorName=mVisitorName.substring(0,mVisitorName.length()-1);
		   mVisitorSex=mVisitorSex.substring(0, mVisitorSex.length()-1);
		   mPaperType=mPaperType.substring(0, mPaperType.length()-1);
		   mPaperNum=mPaperNum.substring(0, mPaperNum.length()-1);
		   mMobile=mMobile.substring(0, mMobile.length()-1);
		   mVisitorCompany=mVisitorCompany.substring(0, mVisitorCompany.length()-1);
		   mAddress=mAddress.substring(0, mAddress.length()-1);
		   
		   
		   pVisitorNum=pVisitorNum.substring(0, pVisitorNum.length()-1);
		   pVisitorName=pVisitorName.substring(0, pVisitorName.length()-1);
		   pVisitorSex=pVisitorSex.substring(0, pVisitorSex.length()-1);
		   pOryName=pOryName.substring(0, pOryName.length()-1);
		   pRoom=pRoom.substring(0, pRoom.length()-1);
		   pMobile=pMobile.substring(0, pMobile.length()-1);
		   pPhone=pPhone.substring(0, pPhone.length()-1);
		   staffNo=staffNo.substring(0, staffNo.length()-1);
		   
		   recordNum=recordNum.substring(0, recordNum.length()-1);
		   visitorCard=visitorCard.substring(0, visitorCard.length()-1);
		   carInfo=carInfo.substring(0, carInfo.length()-1);
		   visitMatter=visitMatter.substring(0, visitMatter.length()-1);
		   carryGoods=carryGoods.substring(0, carryGoods.length()-1);
		   visitDate=visitDate.substring(0, visitDate.length()-1);
		   validityDate=validityDate.substring(0, validityDate.length()-1);
		   identifyPhoto=identifyPhoto.substring(0, identifyPhoto.length()-1);
		   catchPhoto=catchPhoto.substring(0, catchPhoto.length()-1);
		   visitNumber=visitNumber.substring(0, visitNumber.length()-1);
		   visitCardType=visitCardType.substring(0, visitCardType.length()-1);
		   visitNum=visitNum.substring(0, visitNum.length()-1);
		   visitState=visitState.substring(0, visitState.length()-1);
		   leftDate=leftDate.substring(0, leftDate.length()-1);
		   cardState=cardState.substring(0, cardState.length()-1);
		   createUser=createUser.substring(0, createUser.length()-1);
		   
		   map2.put("mVisitorNum", mVisitorNum);
		   map2.put("mVisitorName", mVisitorName);
		   map2.put("mVisitorSex", mVisitorSex);
		   map2.put("mPaperType", mPaperType);
		   map2.put("mPaperNum", mPaperNum);
		   map2.put("mMobile", mMobile);
		   map2.put("mVisitorCompany", mVisitorCompany);
		   map2.put("mAddress", mAddress);
		   
		   map2.put("pVisitorNum", pVisitorNum);
		   map2.put("pVisitorName", pVisitorName);
		   map2.put("pVisitorSex", pVisitorSex);
		   map2.put("pOryName", pOryName);
		   map2.put("pRoom", pRoom);
		   map2.put("pMobile", pMobile);
		   map2.put("pPhone", pPhone);
		   map2.put("staffNo", staffNo);
		   
		   map2.put("recordNum", recordNum);
		   map2.put("cardNum", visitorCard);
		   map2.put("carInfo", carInfo);
		   map2.put("visitMatter", visitMatter);
		   map2.put("carryGoods", carryGoods);
		   map2.put("visitDate", visitDate);
		   map2.put("validityDate", validityDate);
		   map2.put("identifyPhoto", identifyPhoto);
		   map2.put("catchPhoto", catchPhoto);
		   map2.put("visitNumber", visitNumber);
		   map2.put("visitCardType", visitCardType);
		   map2.put("visitNum", visitNum);
		   map2.put("visitState", visitState);
		   map2.put("leaveTime", leftDate);
		   map2.put("cardState", cardState);
		   map2.put("createUser", createUser);
		   
		   map2.put("result", "0");//存在记录
	   }else{
		   map2.put("result", "1");//不存在记录
	   }
	   return map2;
   }
   /*离开登记（修改访客记录状态）*/
   public boolean  visitorLeave(Map map){
	   Map<String,String> log = new HashMap<String,String>();
		boolean result=paramConfigurationDao.visitorLeave(map);
		Map m=paramConfigurationDao.getCardNumByMvisitorNum(map);
		boolean result3=connCsService.delAuthOnLeave(m);
		boolean result2=paramConfigurationDao.delVisitorBoundCard(map);
		connCsService.delCardRec(map);
		if(result && result2 && result3){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客记录状态");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", map.get("createUser").toString());
			log.put("V_OP_MSG", "修改访客记录状态成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客记录状态");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", map.get("createUser").toString());
			log.put("V_OP_MSG", "修改访客记录状态失败");
			logDao.addLog(log);
			return false;
		}
		return true; 
   }
   
   /*查询来访次数 */
   public Map  getHisVisitedCount(Map map){
	   Map m=paramConfigurationDao.getHisVisitedCount(map);
	   return m;
   }
   /*访客登记-新增主访人员*/
   public boolean  addMainVisitorReg(Map map){
	   Map<String,String> log = new HashMap<String,String>();
	   Map  m=paramConfigurationDao.getHisVisitedCount(map);
	   map.put("visitCount", m.get("visitCount"));
	   //map.put("visitCount", "0");
		boolean result=paramConfigurationDao.addMainVisitorReg(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客登记");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", map.get("createUser").toString());
			log.put("V_OP_MSG", "新增主访人员成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客登记");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", map.get("createUser").toString());
			log.put("V_OP_MSG", "新增主访人员失败");
			logDao.addLog(log);
			return false;
		}
		return true; 
   }
   /*访客登记-新增来访记录 */
   public boolean  addVisitRecord(Map map){
	   Map<String,String> log = new HashMap<String,String>();
		boolean result=paramConfigurationDao.addVisitRecord(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客登记");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", map.get("createUser").toString());
			log.put("V_OP_MSG", "新增来访记录成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客登记");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", map.get("createUser").toString());
			log.put("V_OP_MSG", "新增来访记录 失败");
			logDao.addLog(log);
			return false;
		}
		return true; 
   }
   /*访客登记-新增随访人员*/
   public boolean  addFollowVisitor(Map map){
	   Map<String,String> log = new HashMap<String,String>();
	   List<Map> list=resetFollowVisitorMap(map);
	   if(null != list){
		   for(Map m:list){
				boolean result=paramConfigurationDao.addFollowVisitor(m);
				if(result){
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "访客登记");
					log.put("V_OP_FUNCTION", "新增");
					log.put("V_OP_ID", map.get("createUser").toString());
					log.put("V_OP_MSG", "新增随访人员成功");
					logDao.addLog(log);
				}else{
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "访客登记"); 
					log.put("V_OP_FUNCTION", "新增");
					log.put("V_OP_ID", map.get("createUser").toString());
					log.put("V_OP_MSG", "新增随访人员失败");
					logDao.addLog(log);
					return false;
				}
		   }
	   }
	   return true; 
   }
   /*访客登记-新增被访人员 */
   public boolean  addPassiveVisitor(Map map){
	   Map<String,String> log = new HashMap<String,String>();
		boolean result=paramConfigurationDao.addPassiveVisitor(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客登记");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", map.get("createUser").toString());
			log.put("V_OP_MSG", "新增被访人员成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客登记");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", map.get("createUser").toString());
			log.put("V_OP_MSG", "新增被访人员失败");
			logDao.addLog(log);
			return false;
		}
		return true; 
   }
   
   /*访客登记*/
   public boolean  mainVisitorReg(Map map){
	   
		boolean flag1=addMainVisitorReg(map);
		boolean flag2=addVisitRecord(map);
		boolean flag3=addFollowVisitor(map);
		boolean flag4=addPassiveVisitor(map);
		
		if(flag1 && flag2 && flag3 && flag4){
			return true;
		}else{
			return false;
		}
		
   }
   
   /*来访查询*/
   public List<Map>  queryMainVisitorLis(Map map){
	   Map map2= resetVisitorParam(map);
	   List<Map> list=paramConfigurationDao.queryMainVisitorLis(map2);
	   return list;
   }
   
   /*被访人管理*/
   public List<Map>  queryPassiveVisitorByParam(Map map){
	   String[] keyArray=map.get("param").toString().split("\\|");
	   //被访人条件（0：组织机构 1：工号 2：职工姓名 3：电话号码（办公电话） 4：全部）
	   if("0".equals(keyArray[0])){
		   map.put("deptName", keyArray[1]);
	   }else if("1".equals(keyArray[0])){
		   map.put("staffNo", keyArray[1]);
	   }else if("2".equals(keyArray[0])){
		   map.put("staffName", keyArray[1]);
	   }else if("3".equals(keyArray[0])){
		   map.put("staffMobile", keyArray[1]);
	   }
	   List<Map> list=paramConfigurationDao.queryPassiveVisitorByParam(map);
	   return list;
   }
   
   /*被访人总条数*/
   public Integer  countPassiveVisitor(Map map){
	   String[] keyArray=map.get("param").toString().split("\\|");
	   //被访人条件（0：组织机构 1：工号 2：职工姓名 3：电话号码（办公电话） 4：全部）
	   if("0".equals(keyArray[0])){
		   map.put("deptName", keyArray[1]);
	   }else if("1".equals(keyArray[0])){
		   map.put("staffNo", keyArray[1]);
	   }else if("2".equals(keyArray[0])){
		   map.put("staffName", keyArray[1]);
	   }else if("3".equals(keyArray[0])){
		   map.put("staffMobile", keyArray[1]);
	   }
	   Integer count=paramConfigurationDao.countPassiveVisitor(map);
	   return count;
   }
   
   /*拆分来访人员的筛选条件*/
   public Map resetVisitorParam(Map map){
	 
	   //判断是来访时间段还是离开时间段
	   if("0".equals(map.get("timeFlag"))){
		   map.put("comeBeginDate", map.get("beginDate"));
		   map.put("comeEndDate", map.get("endDate"));
	   }else if("1".equals(map.get("timeFlag"))){
		   map.put("leaveBeginDate", map.get("beginDate"));
		   map.put("leaveEndDate", map.get("endDate"));
	   }
	   //如果访客性别为2（全部），visitorSex的值致空
	   if("2".equals(map.get("visitorSex"))){
		   map.put("visitorSex", "");
	   }
	   //如果访客状态为2（全部），visitorState的值致空
	   if("2".equals(map.get("visitorState"))){
		   map.put("visitorState", "");
	   }
	   
	   if(map.get("keyWord") !=null){
		   //拆开传来的关键字（格式：0|XXXXXXX ）
		   String[] keyArray=map.get("keyWord").toString().split("\\|");
		   
		   //判断关键字（0：访客编号 1：访客姓名 2：卡号 3：被访人姓名 4：来访事宜 5：证件号码 6：操作人）
		   if("0".equals(keyArray[0])){
			   map.put("visitorNum", keyArray[1]);
		   }else if("1".equals(keyArray[0])){
			   map.put("mVisitorName", keyArray[1]);
		   }else if("2".equals(keyArray[0])){
			   map.put("cardNum", keyArray[1]);
		   }else if("3".equals(keyArray[0])){
			   map.put("pVisitorName", keyArray[1]);
		   }else if("4".equals(keyArray[0])){
			   map.put("visitMatter", keyArray[1]);
		   }else if("5".equals(keyArray[0])){
			   map.put("mPaperNum", keyArray[1]);
		   }else if("6".equals(keyArray[0])){
			   map.put("creatUser", keyArray[1]);
		   }
	   }
	   return map;
   }
   
   /*拆装随访人员信息*/
   public  List<Map>  resetFollowVisitorMap(Map map){
	   List<Map> list=new ArrayList();
	   String[] fVisitNum=map.get("fVisitNum").toString().split("\\|");
	   String[] fVisitorName=map.get("fVisitorName").toString().split("\\|");
	   String[] fVisitorSex=map.get("fVisitorSex").toString().split("\\|");
	   String[] fPaperType=map.get("fPaperType").toString().split("\\|");
	   String[] fPaperNum=map.get("fPaperNum").toString().split("\\|");
	   String[] fMobile=map.get("fMobile").toString().split("\\|");
	   String[] fAddress=map.get("fAddress").toString().split("\\|");
	   String[] fIdentifyPhoto=map.get("fIdentifyPhoto").toString().split("\\|");
	   String[] fCatchPhoto=map.get("fCatchPhoto").toString().split("\\|");
	   for(int i=0;i<fVisitNum.length;i++){
		   Map map2=new HashMap();
		   map2.put("fVisitNum", fVisitNum.length>0?fVisitNum[i]:"");
		   map2.put("fVisitorName", fVisitorName.length>0?fVisitorName[i]:"");
		   map2.put("fVisitorSex", fVisitorSex.length>0?fVisitorSex[i]:"0");
		   map2.put("fPaperType", fPaperType.length>0?fPaperType[i]:"");
		   map2.put("fPaperNum", fPaperNum.length>0?fPaperNum[i]:"");
		   map2.put("fMobile", fMobile.length>0?fMobile[i]:"");
		   map2.put("fAddress", fAddress.length>0?fAddress[i]:"");
		   map2.put("fIdentifyPhoto", fIdentifyPhoto.length>i?fIdentifyPhoto[i]:"");
		   map2.put("fCatchPhoto", fCatchPhoto.length>i?fCatchPhoto[i]:"");
		   map2.put("recordNum", map.get("recordNum").toString());
		   list.add(map2);
	   }
	   return list;
   } 
   
   /*取消预约*/
   public boolean  cancelBooking(Map map){
	   Map<String,String> log = new HashMap<String,String>();
	   boolean result=paramConfigurationDao.cancelBooking(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客预约");
			log.put("V_OP_FUNCTION", "预约");
			log.put("V_OP_ID", "visitor_sys");
			log.put("V_OP_MSG", "预约成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "访客预约");
			log.put("V_OP_FUNCTION", "预约");
			log.put("V_OP_ID", "visitor_sys");
			log.put("V_OP_MSG", "预约失败");
			logDao.addLog(log);
			return false;
		}
		return true; 
   }
   /*---------------------------接口后台语句止---------------------------------*/
}
