package com.kuangchi.sdd.visitorConsole.paramConfiguration.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.visitorConsole.paramConfiguration.dao.ParamConfigurationDao;

/**
 * @创建人:    huixian.pan
 * @创建时间:   2016-11-09
 * @创建内容:   参数配置Dao实现类 
 */
@Repository("paramConfigurationDaoImpl")
public class ParamConfigurationDaoImpl extends BaseDaoImpl<Map> implements ParamConfigurationDao{
	
		@Override
		public String getNameSpace() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public String getTableName() {
			// TODO Auto-generated method stub
			return null;
		}
	
	   /*查看来访事宜信息*/
	   @Override
	   public  List<Map> getVisitMatterInfo(Map map){
		   return this.getSqlMapClientTemplate().queryForList("getVisitMatterInfo", map);
	   }
	   /*查看来访事宜条数*/
	   @Override
	   public  Integer countVisitMatterInfo(Map map){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("countVisitMatter", map);
	   }
	   /*查看重复来访事宜条数*/
	   @Override
	   public  Integer getRepeatVisitMatter(Map map){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("getRepeatVisitMatter", map);
	   }
	   /*新增来访事宜*/
	   @Override
	   public boolean  addVisitMatter(Map map){
		   return this.insert("addVisitMatter", map);
	   }
	   /*修改来访事宜*/
	   @Override
	   public boolean  editVisitMatter(Map map){
		   return this.update("editVisitMatter", map);
	   }
	   /*删除来访事宜*/
	   @Override
	   public boolean  delVisitMatter(Map map){
		   return this.delete("delVisitMatter", map);
	   }
	   /*查询所有来访事宜（接口）*/
	   public List<Map> getVisitMatterReg(){
		   return this.getSqlMapClientTemplate().queryForList("getVisitMatterReg");
	   }
	   
	   
	   
	   /*查看携带物品信息*/
	   @Override
	   public  List<Map> getCarryGoodsInfo(Map map){
		   return this.getSqlMapClientTemplate().queryForList("getCarryGoodsInfo", map);
	   }
	   /*查看携带物品条数*/
	   @Override
	   public  Integer countCarryGoodsInfo(Map map){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("countCarryGoods", map);
	   }
	   /*查看重复携带物品条数*/
	   @Override
	   public  Integer getRepeatCarryGoods(Map map){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("getRepeatCarryGoods", map);
	   }
	   /*新增携带物品*/
	   @Override
	   public boolean  addCarryGoods(Map map){
		   return this.insert("addCarryGoods", map);
	   }
	   /*修改携带物品*/
	   @Override
	   public boolean  editCarryGoods(Map map){
		   return this.update("editCarryGoods", map);
	   }
	   /*删除携带物品*/
	   @Override
	   public boolean  delCarryGoods(Map map){
		   return this.delete("delCarryGoods", map);
	   }
	   /*查询所有携带物品（接口）*/
	   public List<Map> getCarryGoodsReg(){
		   return this.getSqlMapClientTemplate().queryForList("getCarryGoodsReg");
	   }
	   
	   
	   /*查看黑名单信息*/
	   @Override
	   public  List<Map> getBlackListInfo(Map map){
		   return this.getSqlMapClientTemplate().queryForList("getBlackListInfo", map);
	   }
	   /*查看黑名单条数*/
	   @Override
	   public  Integer countBlackListInfo(Map map){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("countBlackList", map);
	   }
	   /*通过Id查看黑名单*/
	   @Override
	   public  Map getBlackListById(Map map){
		   return (Map) this.getSqlMapClientTemplate().queryForObject("getBlackListById", map);
	   }
	   /*查看重复黑名单条数*/
	   @Override
	   public  Integer getRepeatBlackList(Map map){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("getRepeatBlackList", map);
	   }
	   /*新增黑名单*/
	   @Override
	   public boolean  addBlackList(Map map){
		   return this.insert("addFkBlackList", map);
	   }
	   /*新增主访人员信息*/
	   @Override
	   public boolean  addMainVisitor(Map map){
		   return this.insert("addMainVisitor", map);
	   }
	   
	   /*修改黑名单*/
	   @Override
	   public boolean  editBlackList(Map map){
		   return this.update("editBlackList", map);
	   }
	   
	   /*修改主访人员信息*/
	   @Override
	   public boolean  editMainVisitor(Map map){
		   return this.update("editMainVisitor", map);
	   }
	   
	   /*删除黑名单*/
	   @Override
	   public boolean  delBlackList(Map map){
		   return this.delete("delBlackList", map);
	   }
	   /*通过id查询主访人员表里面的m_visitor_num*/
	   @Override
	   public String getMVisitorNum(String id) {
			return (String)getSqlMapClientTemplate().queryForObject("getMVisitorNum", id);
	   }
	   /*查询所有黑名单（接口）*/
	   public List<Map> getBlackListReg(){
		   return this.getSqlMapClientTemplate().queryForList("getBlackListReg");
	   }
	   
	   
	   /*查看通知名单信息*/
	   public  List<Map> getNotifyListInfo(Map map){
		   return this.getSqlMapClientTemplate().queryForList("getNotifyListInfo", map);
	   }
	   /*查看通知名单条数*/
	   public  Integer countNotifyList(Map map){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("countNotifyList", map);
	   }
	   /*新增通知名单*/
	   public  boolean addNotifyList(Map map){
		   return this.insert("addNotifyList", map);
	   }
	   /*删除重复的通知名单*/
	   public  boolean delRepeatNotifyList(Map map){
		   return this.delete("delRepeatNotifyList", map);
	   }
	   /*删除通知名单*/
	   public  boolean delNotifyList(Map map){
		   return this.delete("delNotifyList", map);
	   }
	   /*---------------------------接口后台语句起---------------------------------*/
	   /*查询权限组*/
	   public List<Map>  queryAuthGroup(){
		   return this.getSqlMapClientTemplate().queryForList("queryAuthGroup");
	   }
	   /*查询权限信息*/
	   public List<Map>  queryAuthInfo(String groupNum){
		   return this.getSqlMapClientTemplate().queryForList("queryAuthInfo",groupNum);
	   }
	   /*验证卡号*/
	   public Integer    validateCardNum(String cardNum){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("validateCardNum",cardNum);
	   }
	   /*查询历史被访人员*/
	   public Map  getHisVisitedPerson(Map map){
		   return  (Map) this.getSqlMapClientTemplate().queryForObject("getHisVisitedPerson", map);
	   }
	   /*来访查询（查询今日访客（正在访问，已离开，预约））*/
	   public List<Map>  queryTodayVisitor(Map map){
		   return  this.getSqlMapClientTemplate().queryForList("queryTodayVisitor", map);
	   }
	   /*离开登记（修改访客记录状态）*/
	   public boolean  visitorLeave(Map map){
		   return this.update("visitorLeave", map);
	   }
	   /*离开登记时删除人卡绑定信息 ）*/
	   public boolean  delVisitorBoundCard(Map map){
		   return this.delete("delVisitorBoundCard", map);
	   }
	   /*离开登记 -通过主访人员编号查询卡号*/
	   public Map  getCardNumByMvisitorNum(Map map){
		   return (Map) this.getSqlMapClientTemplate().queryForObject("getCardNumByMvisitorNum", map);
	   }
	   /*查询来访次数 */
	   public Map  getHisVisitedCount(Map map){
		   Map m=(Map) this.getSqlMapClientTemplate().queryForObject("getHisVisitedCount", map);
		   if(m==null){
			   m=new HashMap();
			   m.put("visitCount", "0");
		   }
		   return m;
	   }
	   /*访客登记-新增主访人员*/
	   public boolean  addMainVisitorReg(Map map){
		   return this.insert("addMainVisitorReg", map);
	   }
	   /*访客登记-新增来访记录 */
	   public boolean  addVisitRecord(Map map){
		   return this.insert("addVisitRecord", map);
	   }
	   /*访客登记-新增随访人员*/
	   public boolean  addFollowVisitor(Map map){
		   return this.insert("addFollowVisitor", map);
	   }
	   /*访客登记-新增被访人员 */
	   public boolean  addPassiveVisitor(Map map){
		   return this.insert("addPassiveVisitor", map);
	   }
	   /*来访查询*/
	   public List<Map>  queryMainVisitorLis(Map map){
		   return this.getSqlMapClientTemplate().queryForList("queryMainVisitorLis", map);
	   }
	   /*被访人管理*/
	   public List<Map>  queryPassiveVisitorByParam(Map map){
		   return this.getSqlMapClientTemplate().queryForList("queryPassiveVisitorByParam", map);
	   }
	   /*被访人总条数*/
	   public Integer  countPassiveVisitor(Map map){
		   return (Integer) this.getSqlMapClientTemplate().queryForObject("countPassiveVisitor", map);
	   }
	   
	   /*取消预约*/
	   public boolean  cancelBooking(Map map){
		   return this.update("cancelBooking", map);
	   }
	   /*---------------------------接口后台语句止---------------------------------*/
	

}
