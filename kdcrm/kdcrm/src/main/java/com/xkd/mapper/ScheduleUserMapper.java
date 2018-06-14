package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xkd.model.UserAction;
import org.apache.ibatis.annotations.Param;

import com.xkd.model.ScheduleUser;

public interface ScheduleUserMapper {

	/**
	 * 添加同行人
	 * @return
	 */
	Integer insertScheduleUser(ScheduleUser scheduleUser);
	/**
	 * 通过行程Id批量删除同行人
	 * @param scheduleId
	 * @return
	 */

	Integer deleteScheduleUserByScheduleId(String scheduleId);
	/**
	 * 根据行程Id，查出相应的同行人
	 * @return
	 */
	List<HashMap<String, Object>> selectUserByScheduleIds(@Param("scheduleIds")List<Integer> scheduleIds);


	
	void saveUserAction(UserAction userAction);

	void setUserActionRead(@Param("userId")String userId,@Param("id")String id);

    List<Map<String,String>> getMyHistoryUser(@Param("myUerId")String myUerId);

	void deleteHistoryUser(@Param("myUserId")String myUserId,@Param("userId")String userId);

	void saveHistoryUser(Map<String, Object> historyUserInfo);

    List<Map<String,String>> getMyHistoryUserInfo(@Param("myUserId")String myUserId);

	List<Map<String,String>> getPCUserIds();

	void setUserActionNoPrompt(@Param("userId")String userId);

    Map<String,Object> getUserActionById(@Param("id")String id);
}
