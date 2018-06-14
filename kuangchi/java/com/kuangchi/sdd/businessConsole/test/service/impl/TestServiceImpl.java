package com.kuangchi.sdd.businessConsole.test.service.impl;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.test.dao.TestDao;
import com.kuangchi.sdd.businessConsole.test.model.TestBean;
import com.kuangchi.sdd.businessConsole.test.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jianhui.wu on 2016/2/3.
 */
@Transactional
@Service("testServiceImpl")
public class TestServiceImpl implements TestService {

    @Autowired
    TestDao testDao;

    @Override
    public Grid<TestBean> listTestBeansByName(TestBean testBean) {
        Grid<TestBean> grid = new Grid<TestBean>();
        List<TestBean> resultList = testDao
               .listTestBeansByName(testBean);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(testDao.getCountTestBeanByName(testBean));
        } else {
            grid.setTotal(0);
        }
        return grid;
    }

    @Override
    public void saveTestBean(TestBean testBean) {
        testDao.saveTestBean(testBean);
    }

    @Override
    public void delTestBean(String del_ids) {
         testDao.delTestBean(del_ids);
    }

    @Override
    public TestBean getTestBeanById(String id) {
        return testDao.getTestBeanById(id);
    }

    @Override
    public void updateTestBean(TestBean testBean) {
        testDao.updateTestBean(testBean);
    }

}
