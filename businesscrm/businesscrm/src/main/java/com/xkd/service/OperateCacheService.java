package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.model.Operate;
import com.xkd.model.RedisTableKey;
import com.xkd.utils.RedisCacheUtil;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/5/3.
 */
@Service
public class OperateCacheService {

    @Autowired
    RedisCacheUtil redisCacheUtil;




    //保存缓存
    public void putUserOperates(String key, List<Operate> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, operateList);
        //默认过期时间30分钟
        redisCacheUtil.setCacheMap(RedisTableKey.OPERATE, map);

    }

    //获取缓存
    public List<Operate> getUserOperates(String key) {
        return (List<Operate>) redisCacheUtil.getCacheMapVaue(RedisTableKey.OPERATE, key);
    }

    public List<Operate> getAllOperates() {
        String str= (String) redisCacheUtil.getCacheObject(RedisTableKey.ALLOPERATE);
        List<Operate> list = JSON.parseObject(str, new TypeReference<List<Operate>>() {
        });
        return list;
    }


    public void putAllOperate(List<Operate> operateList) {
        redisCacheUtil.setCacheObject(RedisTableKey.ALLOPERATE, JSON.toJSONString(operateList));
    }

    public void clear() {
        redisCacheUtil.delete(RedisTableKey.ALLOPERATE);
        redisCacheUtil.delete(RedisTableKey.OPERATE);
    }

    public void clear(String token) {
        redisCacheUtil.delete(RedisTableKey.OPERATE, token);
    }


}
