package com.kuangchi.sdd.consumeConsole.consumeGroup.service;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.consumeGroup.model.ConsumeGroupModel;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:47:19
 * @功能描述: 消费组管理-业务层
 */
public interface IConsumeGroupService {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:44:37
	 * @功能描述: 根据参数查询消费组[分页]
	 * @参数描述:
	 */
	public List<ConsumeGroupModel> getConsumeGroupByParamPage(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:44:37
	 * @功能描述: 根据参数查询消费组
	 * @参数描述:
	 */
	public List<ConsumeGroupModel> getConsumeGroupByParam(Map<String, Object> map);
	
	/**
	 * 查询非默认的消费组
	 * @author yuman.gao
	 */
	public List<ConsumeGroupModel> getNonDefaultGroup(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-4 上午11:17:55
	 * @功能描述: 根据参数查询消费组[总数]
	 * @参数描述:
	 */
	public Integer getConsumeGroupByParamCount(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午5:24:52
	 * @功能描述: 新增消费组
	 * @参数描述:
	 */
	public boolean addConsumeGroup(ConsumeGroupModel consumeGroup, String loginUserName);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午7:07:28
	 * @功能描述: 修改消费组
	 * @参数描述:
	 */
	public boolean modifyConsumeGroup(ConsumeGroupModel consumeGroup, String loginUserName);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午8:06:40
	 * @功能描述: 删除消费组
	 * @参数描述:
	 */
	public boolean removeConsumeGroup(String delete_ids, String loginUserName);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:30:51
	 * @功能描述: 根据代码查询消费组（用于判断组代码是否存在）
	 * @参数描述:
	 */
	public List<ConsumeGroupModel> getConsumeGroupByNum(String group_num);

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:30:51
	 * @功能描述: 根据ID查询消费组
	 * @参数描述:
	 */
	public ConsumeGroupModel getConsumeGroupById(String id);
	
	/**
	 * 判断两个类型是否关联同一餐次
	 * @author minting.he
	 * @param old_type_num
	 * @param new_type_num
	 * @return
	 */
	public boolean ifTypeSameMeal(String old_type_num, String new_type_num);
}
