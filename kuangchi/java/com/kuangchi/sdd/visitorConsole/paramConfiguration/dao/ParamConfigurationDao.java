package com.kuangchi.sdd.visitorConsole.paramConfiguration.dao;

import java.util.List;
import java.util.Map;

/**
 * @创建人:    huixian.pan
 * @创建时间:   2016-11-09
 * @创建内容:   参数配置Dao层 
 */
public interface ParamConfigurationDao {
	
       /*查看来访事宜信息*/
	   public  List<Map> getVisitMatterInfo(Map map);
	   /*查看来访事宜条数*/
	   public  Integer countVisitMatterInfo(Map map);
	   /*查看重复来访事宜条数*/
	   public  Integer getRepeatVisitMatter(Map map);
	   /*新增来访事宜*/
	   public boolean  addVisitMatter(Map map);
	   /*修改来访事宜*/
	   public boolean  editVisitMatter(Map map);
	   /*删除来访事宜*/
	   public boolean  delVisitMatter(Map map);
	   /*查询所有来访事宜（接口）*/
	   public List<Map> getVisitMatterReg();
	   
	   /*查看携带物品信息*/
	   public  List<Map> getCarryGoodsInfo(Map map);
	   /*查看携带物品条数*/
	   public  Integer countCarryGoodsInfo(Map map);
	   /*查看重复携带物品条数*/
	   public  Integer getRepeatCarryGoods(Map map);
	   /*新增携带物品*/
	   public boolean  addCarryGoods(Map map);
	   /*修改携带物品*/
	   public boolean  editCarryGoods(Map map);
	   /*删除携带物品*/
	   public boolean  delCarryGoods(Map map);
	   /*查询所有携带物品（接口）*/
	   public List<Map> getCarryGoodsReg();
	   
	   
	   /*查看黑名单信息*/
	   public  List<Map> getBlackListInfo(Map map);
	   /*查看黑名单条数*/
	   public  Integer countBlackListInfo(Map map);
	   /*通过ID查看黑名单*/
	   public  Map getBlackListById(Map map);
	   /*查看重复黑名单条数*/
	   public  Integer getRepeatBlackList(Map map);
	   /*新增黑名单*/
	   public boolean  addBlackList(Map map);
	   /*新增主访人员信息*/
	   public boolean  addMainVisitor(Map map);
	   /*修改黑名单*/
	   public boolean  editBlackList(Map map);
	   /*修改主访人员信息*/
	   public boolean  editMainVisitor(Map map);
	   /*删除黑名单*/
	   public boolean  delBlackList(Map map);
	   /*通过id查询主访人员表里面的m_visitor_num*/
	   public String getMVisitorNum(String id);
	   /*查询所有黑名单（接口）*/
	   public List<Map> getBlackListReg();
	   
	   
	   /*查看通知名单信息*/
	   public  List<Map> getNotifyListInfo(Map map);
	   /*查看通知名单条数*/
	   public  Integer countNotifyList(Map map);
	   /*新增通知名单*/
	   public  boolean addNotifyList(Map map);
	   /*删除重复的通知名单*/
	   public  boolean delRepeatNotifyList(Map map);
	   /*删除通知名单*/
	   public  boolean delNotifyList(Map map);
	   
	   /*---------------------------接口后台语句起---------------------------------*/
	   /*查询权限组*/
	   public List<Map>  queryAuthGroup();
	   /*查询权限信息*/
	   public List<Map>  queryAuthInfo(String groupNum);
	   /*验证卡号*/
	   public Integer    validateCardNum(String cardNum);
	   /*查询历史被访人员*/
	   public Map  getHisVisitedPerson(Map map);
	   /*来访查询（查询今日访客（正在访问，已离开，预约））*/
	   public List<Map>  queryTodayVisitor(Map map);
	   /*离开登记（修改访客记录状态）*/
	   public boolean  visitorLeave(Map map);
	   /*离开登记时删除人卡绑定信息 ）*/
	   public boolean  delVisitorBoundCard(Map map);
	   /*离开登记 -通过主访人员编号查询卡号*/
	   public Map  getCardNumByMvisitorNum(Map map);
	   /*查询来访次数 */
	   public Map  getHisVisitedCount(Map map);
	   /*访客登记-新增主访人员*/
	   public boolean  addMainVisitorReg(Map map);
	   /*访客登记-新增来访记录 */
	   public boolean  addVisitRecord(Map map);
	   /*访客登记-新增随访人员*/
	   public boolean  addFollowVisitor(Map map);
	   /*访客登记-新增被访人员 */
	   public boolean  addPassiveVisitor(Map map);
	   /*来访查询*/
	   public List<Map>  queryMainVisitorLis(Map map);
	   /*被访人管理*/
	   public List<Map>  queryPassiveVisitorByParam(Map map);
	   /*被访人总条数*/
	   public Integer  countPassiveVisitor(Map map);
	   /*取消预约*/
	   public boolean  cancelBooking(Map map);
	   
	   /*---------------------------接口后台语句止---------------------------------*/
	
	
}
