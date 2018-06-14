package com.kuangchi.sdd.businessConsole.common.service;

import com.kuangchi.sdd.businessConsole.common.model.ConstantKeyValue;

import java.util.List;

/**
 * Created by jianhui.wu on 2016/2/16.
 */
public interface ConstantKeyValueService {
    public List<ConstantKeyValue> getConstantKeyValue(String type);
}
