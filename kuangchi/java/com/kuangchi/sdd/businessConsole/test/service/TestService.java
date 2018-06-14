package com.kuangchi.sdd.businessConsole.test.service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.test.model.TestBean;

import java.util.List;

/**
 * Created by jianhui.wu on 2016/2/3.
 */
public interface TestService {
    public Grid<TestBean> listTestBeansByName(TestBean testBean);

    public void saveTestBean(TestBean testBean);

    public void delTestBean(String del_ids);
    public TestBean getTestBeanById(String id);
    public void updateTestBean(TestBean testBean);
}
