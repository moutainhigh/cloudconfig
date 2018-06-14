package com.kuangchi.sdd.baseConsole.list.blacklist.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.list.blacklist.model.BlackList;
import com.kuangchi.sdd.baseConsole.list.whitelist.model.WhiteList;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;

public interface BlackListService {

	/**根据条件查询黑名单信息*/
	List<BlackList> getBlackListByParam(BlackList blackList);
	
	/**根据员工代码移出黑名单*/
	public boolean deleteBlackListByStaffNum(String staff_num, String create_user);
	
	
	/**根据用户代码，卡号检验是否调通了信服务器*/
	void insertAutorityCardByStaffNum(List<PeopleAuthorityInfoModel> authorityList);
	
	
	/**根据条件搜索黑名单信息(分页)*/
	public  Grid<BlackList> getBlackListByParamPage(BlackList blackList);
	
	/**根据用户代码，卡号检验是否调通了信服务器
	 * @param authorityList */
	public void deleteAutorityCardByStaffNum(List<PeopleAuthorityInfoModel> authorityList);
	
	/**新增用户绑定卡
	 * @param description */
	boolean addBlackList(BoundCard boundCard,String yhDm, String create_user, String description);
	
	/**根据用户下有权限的卡号删除权限表中的记录*/
	boolean deleteAutorityByCardNum(String cardNum);

	boolean addBlackListNoBoundCard(String yhDm,String description,String create_user);
	
	
	
}
