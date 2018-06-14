package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-3-5;
 */
public interface MsgDeviceCurrMapper {

    Map<String, Object> selectAbnormalDeviceMsg(@Param("userId") String userId, @Param("boxId") String boxId);

    Integer ignoreMessage(@Param("messageId") String messageId);

    Integer insertmsg(@Param("id") Integer id, @Param("userId") String userId, @Param("uuid") String uuid, @Param("eventId") Integer eventId,
                      @Param("boxId") String boxId, @Param("deviceId") String deviceId);

    Integer insertevent(@Param("eventId") Integer eventId, @Param("msg_zh") String msg_zh);

}
