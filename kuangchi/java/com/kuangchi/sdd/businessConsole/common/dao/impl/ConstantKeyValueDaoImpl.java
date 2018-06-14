package com.kuangchi.sdd.businessConsole.common.dao.impl;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.common.dao.ConstantKeyValueDao;
import com.kuangchi.sdd.businessConsole.common.model.ConstantKeyValue;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jianhui.wu on 2016/2/16.
 */
@Repository("constantKeyValueDao")
public class ConstantKeyValueDaoImpl extends BaseDaoImpl<ConstantKeyValue> implements ConstantKeyValueDao {
    @Override
    public List<ConstantKeyValue> getConstantKeyValue(String type) {
        return  this.getSqlMapClientTemplate().queryForList("getConstantKeyValue",type);
    }

    @Override
    public String getNameSpace() {
        return "common.Common";
    }

    @Override
    public String getTableName() {
        return null;
    }
}
