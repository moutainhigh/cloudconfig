package com.kuangchi.sdd.businessConsole.test.action;

import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticService;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.CheckDataService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.businessConsole.test.dao.TestDao;
import com.kuangchi.sdd.businessConsole.test.model.TestBean;
import com.kuangchi.sdd.businessConsole.test.service.TestService;
import com.kuangchi.sdd.businessConsole.test.util.ExcelUtil;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.PeopleAuthorityInfoDao;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.StringUtil;
import com.opensymphony.xwork2.util.ResolverUtil;

import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by jianhui.wu on 2016/2/2.
 */
@Controller("testAction")
public class TestAction extends BaseActionSupport {

    @Autowired
    TestService testService;
    @Autowired
    StatisticService statisticService;

    @Autowired
    CheckDataService checkDataService;
    
    @Autowired 
    PeopleAuthorityInfoService peopleAuthorityInfoService;

    public String toTestPage(){
       return  "success";
    }

    //对话框搜索
    public void test1(){
       /* HttpServletRequest request = getHttpServletRequest();
        Grid<TestBean> testBeanGrid=null;

        String beanObject = getHttpServletRequest().getParameter("data");				//获取前台序列化的数据
        TestBean testBeanPage = GsonUtil.toBean(beanObject, TestBean.class);	//将数据转化为javabean
        testBeanPage.setPage(Integer.parseInt(request.getParameter("page")));
        testBeanPage.setRows(Integer.parseInt(request.getParameter("rows")));


        testBeanGrid=testService.listTestBeansByName(testBeanPage);

        printHttpServletResponse(GsonUtil.toJson(testBeanGrid));*/
    	//statisticService.reStatisticByStaff(DateUtil.getDateTime("2016-05-23 08:10:10"), DateUtil.getDateTime("2016-05-23 08:10:10"), "ya");
//    	List<SqlServerCheckData>  list=checkDataService.getSqlServerCheckData();
    	
    }

//主页面搜索
    public void test2(){
        HttpServletRequest request = getHttpServletRequest();
        Grid<TestBean> testBeanGrid=null;

        String beanObject = getHttpServletRequest().getParameter("data");				//获取前台序列化的数据
        TestBean testBeanPage = GsonUtil.toBean(beanObject, TestBean.class);	//将数据转化为javabean
        testBeanPage.setPage(Integer.parseInt(request.getParameter("page")));
        testBeanPage.setRows(Integer.parseInt(request.getParameter("rows")));


        testBeanGrid=testService.listTestBeansByName(testBeanPage);

        printHttpServletResponse(GsonUtil.toJson(testBeanGrid));
    }


    public void addTest(){
        HttpServletRequest request = getHttpServletRequest();

        String flag=request.getParameter("flag");
        String data= request.getParameter("data");
        TestBean testBean=GsonUtil.toBean(data,TestBean.class);
        if ("add".equals(flag)){
        testService.saveTestBean(testBean);
        }else if("edit".equals(flag)) {
            testService.updateTestBean(testBean);
        }
        JsonResult result = new JsonResult();
        result.setMsg("添加成功");
        result.setSuccess(true);
        printHttpServletResponse(GsonUtil.toJson(result));
    }


    public void delTest(){
        HttpServletRequest request = getHttpServletRequest();
        String dataIds=request.getParameter("data_ids");


            testService.delTestBean(dataIds);


        JsonResult result = new JsonResult();
        result.setMsg("删除成功");
        result.setSuccess(true);
        printHttpServletResponse(GsonUtil.toJson(result));
    }


     public String toOperatePage(){
         HttpServletRequest request=getHttpServletRequest();

         String flag=request.getParameter("flag");
         String id=request.getParameter("id");
        TestBean testBean=testService.getTestBeanById(id);
         request.setAttribute("testBean", testBean);

         if ("view".equals(flag)){
             return "view";
         }else {
             return "success";
         }
     }


    public void reportData(){
// 文件名获取
        HttpServletResponse response = getHttpServletResponse();
        HttpServletRequest request = getHttpServletRequest();
        String selectDatas=request.getParameter("select");
        List list=  GsonUtil.getListFromJson(selectDatas, ArrayList.class);
        OutputStream out=null;
        try {
           out=response.getOutputStream();
            response.setContentType("application/x-msexcel");
            response.setHeader("Content-Disposition","attachment;filename=test.xls");
            ExcelUtil.export(list,out);
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    public void getDeviceTree(){
       Tree tree=	peopleAuthorityInfoService.getDoorTree();
    	
    }
    
    

    @Override
    public Object getModel() {
        return null;
    }
}
