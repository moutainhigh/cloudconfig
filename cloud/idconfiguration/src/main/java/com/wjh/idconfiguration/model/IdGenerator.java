package com.wjh.idconfiguration.model;

import com.wjh.common.model.RedisKeyConstant;
import com.wjh.utils.redis.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IdGenerator {

    @Autowired
    RedisCacheUtil redisCacheUtil;

    @Autowired
    IdService idService;

    public long generateId() {
        Long id = (Long) redisCacheUtil.rightPop(RedisKeyConstant.ID_QUEUE);
        if (id == null) {
            id = idService.generateId();
        }
        return id;
    }

}
