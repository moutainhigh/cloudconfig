package com.kuangchi.sdd.businessConsole.test.dao;

import com.kuangchi.sdd.businessConsole.test.model.TestBean;

import java.util.List;

/**
 * Created by jianhui.wu on 2016/2/3.
 */
public interface TestDao {
    public List<TestBean> listTestBeansByName(TestBean testBean);

    public Integer getCountTestBeanByName(TestBean testBean);

    public void saveTestBean(TestBean testBean);

    public void delTestBean(String del_ids);

    public TestBean getTestBeanById(String id);

    public void updateTestBean(TestBean testBean);

}
