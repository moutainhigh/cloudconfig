package com.kuangchi.sdd.businessConsole.common.service.impl;

import com.kuangchi.sdd.businessConsole.common.dao.ConstantKeyValueDao;
import com.kuangchi.sdd.businessConsole.common.model.ConstantKeyValue;
import com.kuangchi.sdd.businessConsole.common.service.ConstantKeyValueService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by jianhui.wu on 2016/2/16.
 */
@Service("constantKeyValueService")
public class ConstantKeyValueServiceImpl implements ConstantKeyValueService {

    @Resource(name = "constantKeyValueDao")
    ConstantKeyValueDao constantKeyValueDao;
    @Override
    public List<ConstantKeyValue> getConstantKeyValue(String type) {
        return constantKeyValueDao.getConstantKeyValue(type);
    }
}
