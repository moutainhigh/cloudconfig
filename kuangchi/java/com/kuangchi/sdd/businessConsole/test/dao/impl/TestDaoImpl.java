package com.kuangchi.sdd.businessConsole.test.dao.impl;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.test.dao.TestDao;
import com.kuangchi.sdd.businessConsole.test.model.TestBean;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianhui.wu on 2016/2/3.
 */
@Repository("testDaoImpl")
public class TestDaoImpl extends BaseDaoImpl<TestBean> implements TestDao {

    @Override
    public List<TestBean> listTestBeansByName( TestBean testBean) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", testBean.getName());
        return this.getSqlMapClientTemplate().queryForList("listTestBeansByName", testBean);

    }

    @Override
    public Integer getCountTestBeanByName(TestBean testBean) {
        return  queryCount("getCountTestBeanByName",testBean);
    }

    @Override
    public void saveTestBean(TestBean testBean) {
           this.getSqlMapClientTemplate().insert("saveTestBean",testBean);
    }

    @Override
    public void delTestBean(String del_ids) {
        Map<String,String> map=new HashMap<String, String>();
        map.put("del_ids",del_ids);
        this.getSqlMapClientTemplate().delete("delTestBean",map);
    }

    @Override
    public TestBean getTestBeanById(String id) {
        Map<String,String>  map=new HashMap<String, String>();
        map.put("id",id);
      return  (TestBean) this.getSqlMapClientTemplate().queryForObject("getTestBeanById",map);
    }

    @Override
    public void updateTestBean(TestBean testBean) {
        this.getSqlMapClientTemplate().update("updateTestBean",testBean);
    }


    @Override
    public String getNameSpace() {
        return "common.Test";
    }

    @Override
    public String getTableName() {
        return null;
    }
}
