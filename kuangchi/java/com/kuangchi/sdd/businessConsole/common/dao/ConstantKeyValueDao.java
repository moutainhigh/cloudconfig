package com.kuangchi.sdd.businessConsole.common.dao;

import com.kuangchi.sdd.businessConsole.common.model.ConstantKeyValue;

import java.util.List;
import java.util.Map;

/**
 * Created by jianhui.wu on 2016/2/16.
 */
public interface ConstantKeyValueDao {
    public List<ConstantKeyValue> getConstantKeyValue(String type);
}
