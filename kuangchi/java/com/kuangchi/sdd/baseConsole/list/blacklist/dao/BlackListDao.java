package com.kuangchi.sdd.baseConsole.list.blacklist.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.list.blacklist.model.BlackList;
/**
 * 
 * @创建人　: 邓积辉
 * @创建时间: 2016-4-6 下午7:30:26
 * @功能描述:
 */
public interface BlackListDao {

	/**新增用户绑定卡*/
	boolean addBlackList(BlackList blackList);

	/**根据条件查询黑名单信息*/
	List<BlackList> getBlackListByParam(BlackList blackList);
	
	/** 根据条件查询黑名单信息 (分页) */
	List<BlackList> getBlackListByParamPage(BlackList blackList);
	
	 /** 根据条件查询黑名单信息总数 */
	int getBlackListByParamCount(BlackList blackList);
	
	/** 移除黑名单*/
	boolean deleteBlackListByStaffNum(String staffNum);

	/**根据用户下有权限的卡号删除权限表中的记录*/
	boolean deleteAutorityByCardNum(String cardNum);


	Integer getBlackListByStaffNum(String yhDm);

	boolean updateBlackListByStaffNum(String yhDm);

	Integer getBlackListByStaffNumAndCardNum(Map map);

	boolean updateBlackListByStaffNumAndCardNum(Map map);

	/*boolean insertDelAuthorityInfo(Map map);*/
	

}
