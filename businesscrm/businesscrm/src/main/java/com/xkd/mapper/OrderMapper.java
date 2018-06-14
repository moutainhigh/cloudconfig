package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface OrderMapper {

    Integer saveUserOrder(Map<String, Object> map);

    Integer saveOrderTicket(Map<String, Object> map);

    Integer deleteOrderBymhtOrderNoByPayAgain(@Param("mhtOrderNo") String mhtOrderNo);
}
