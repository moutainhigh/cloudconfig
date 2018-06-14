package com.kuangchi.sdd.consumeConsole.consumeType.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.consumeType.model.ConsumeTypeModel;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:47:19
 * @功能描述: 消费类型管理-业务层
 */
public interface IConsumeTypeService {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:44:37
	 * @功能描述: 根据参数查询消费类型[分页]
	 * @参数描述:
	 */
	public List<ConsumeTypeModel> getConsumeTypeByParamPage(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:44:37
	 * @功能描述: 根据参数查询消费类型
	 * @参数描述:
	 */
	public List<ConsumeTypeModel> getConsumeTypeByParam(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-4 上午11:22:33
	 * @功能描述: 根据参数查询消费类型[总数]
	 * @参数描述:
	 */
	public Integer getConsumeTypeByParamCount(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午5:24:52
	 * @功能描述: 新增消费类型
	 * @参数描述:
	 */
	public boolean addConsumeType(ConsumeTypeModel consumeType, String loginUserName);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午7:07:28
	 * @功能描述: 修改消费类型
	 * @参数描述:
	 */
	public boolean modifyConsumeType(ConsumeTypeModel consumeType, String loginUserName);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午8:06:40
	 * @功能描述: 删除消费类型
	 * @参数描述:
	 */
	public boolean removeConsumeType(String delete_ids, String loginUserName);

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:30:51
	 * @功能描述: 根据编号查询消费类型（用于判断类型代码是否存在）
	 * @参数描述:
	 */
	public List<ConsumeTypeModel> getConsumeTypeByNum(String consume_type_num);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:30:51
	 * @功能描述: 根据id查询消费类型
	 * @参数描述:
	 */
	public ConsumeTypeModel getConsumeTypeById(String id);

	public List<MealModel> getMealNum();//获取餐次编号
	
	/**
	 * 根据参数查询消费类型[分页]
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public List<ConsumeTypeModel> getConsumeSameType(Map<String, Object> map);
	

	/**
	 * 根据参数查询消费类型[总数]
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public Integer getConsumeSameTypeCount(Map<String, Object> map);
}
