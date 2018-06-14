package com.kuangchi.sdd.visitorConsole.checkUserLogin.dao;

import java.util.List;
import java.util.Map;

public interface CheckUserLoginDao {

	boolean checkUserLogin(Map map);//用户登录验证

	boolean ifBlackList(Map map);//根据证件类型和证件号码判断是否为黑名单查询

	List<Map> selectRecordInfoByCardnum(Map map);//根据已发卡号查询访客记录信息

	boolean ifPassiveBook(Map map);//判断被访人员是否被预约

	Map queryVisitorByNum(Map map);//根据主访号查询访客

	Map queryBookingVisitor(Map map);//根据手机号、证件号码查询预约访客记录

	List<Map> queryBookingFollowVisitor(Map map);

}
