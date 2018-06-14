package com.xkd.service;

import com.xkd.mapper.OrderMapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public Integer saveUserOrder(Map<String,Object> map){
        return orderMapper.saveUserOrder(map);
    }

    public Integer saveOrderTicket(Map<String, Object> map) {
        return orderMapper.saveOrderTicket(map);
    }

    public Integer deleteOrderBymhtOrderNoByPayAgain(String mhtOrderNo) {
        return orderMapper.deleteOrderBymhtOrderNoByPayAgain(mhtOrderNo);
    }
}
