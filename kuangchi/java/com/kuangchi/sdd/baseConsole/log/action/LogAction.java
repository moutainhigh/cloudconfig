package com.kuangchi.sdd.baseConsole.log.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.model.Log;
import com.kuangchi.sdd.baseConsole.log.service.LogService;
import com.kuangchi.sdd.baseConsole.log.util.ExcelUtilSpecialCountBig;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.kuangchi.sdd.util.file.PropertyUtils;
//import org.quartz.JobDetail;

/**
 * Created by weixuan.lu on 2016/3/24.
 */
@Controller("logAction")
public class LogAction extends BaseActionSupport {

     @Resource(name="LogServiceImpl")
     LogService logService;
     @Resource(name="startQuertz")
     Scheduler scheduler;
/*
     @Resource(name="operateLogBackupQuartzTask") 
     private JobDetail jobDetail;  
     */
	//备份文件绝对路径	
  	private String absolutePath;
  	
  	@Value("${backupPathWindows}")
  	private String absolutePathWindows;
  	
     @Value("${backupPathLinux}")
 	private String absolutePathLinux; 
    
    public String toLogPage(){
       return  "success";
    }
     
    //增加按钮跳转页面
    public String insertLog(){
    	return "success";
    }
    
    
    
    
    /**
     * chudan.guo
	 * 获取日志所有业务类型
	 */
	public void getOp_type(){
		List<Log> allLogType=logService.getOp_type();
		List<Map> list=new ArrayList<Map>();
        for (Log LogType : allLogType) {
           Map<String, String> map = new HashMap<String, String>();
           map.put("text",LogType.getOp_type());
           map.put("id",LogType.getOp_type());
           list.add(map);
        }
        printHttpServletResponse(GsonUtil.toJson(list));
	}
	/**
	 * chudan.guo
	 * 获取日志全部功能
	 */
	public void getOp_function(){
		List<Log> allLogFunction=logService.getOp_function();
		List<Map> list=new ArrayList<Map>();
		for (Log LogFunction : allLogFunction) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("text",LogFunction.getOp_function());
			map.put("id",LogFunction.getOp_function());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
    
    
    
    public void changeOperateLogBackupInterval(){
    	HttpServletRequest request = getHttpServletRequest();
    	String interval=request.getParameter("interval");
    	LOG.info(interval);
    	String cronExpression="0 0 0 1/"+interval+" * ?";
    	String propertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
        JsonResult result = new JsonResult();
        boolean flag=com.kuangchi.sdd.util.file.PropertyUtils.setProperties(propertyFile, "operateLogBackupQuartzCron", cronExpression, null);
    	
    	if(flag){
    	 try {	 
    		 TriggerKey triggerKey=new TriggerKey("operateLogBackupQuartzTaskTimer");
    		 CronTriggerImpl  trigger=(CronTriggerImpl ) scheduler.getTrigger(triggerKey);
			 trigger.setCronExpression(cronExpression);
			 scheduler.rescheduleJob(triggerKey, trigger);
		     result.setSuccess(true);
		     result.setMsg("修改成功");
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("修改失败");
			e.printStackTrace();
		}
    	}
         printHttpServletResponse(GsonUtil.toJson(result));     	
    }
    
    
    //获取设置的天数
    public void getInterval(){
    	HttpServletRequest request = getHttpServletRequest();
    	String propertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
    	Properties property=PropertyUtils.readProperties(propertyFile);
    	String olbqc=property.getProperty("operateLogBackupQuartzCron");
    	int index=olbqc.lastIndexOf("/");
    	String inter3=olbqc.substring(index+1,index+3);
    	HashMap<String,String> map=new HashMap<String,String>();
        map.put("interval", inter3);
        printHttpServletResponse(GsonUtil.toJson(map));
    }
    
    
    
    
    //weixuan.lu 新增日志 已删除该功能
    public void addLog(){
    	
    	HttpServletRequest request = getHttpServletRequest();
    	
    	Map<String, String> map=new HashMap<String, String>();
        String flag=request.getParameter("flag");
        String data= request.getParameter("data");
        Log log=GsonUtil.toBean(data,Log.class);
        
        String op_name=request.getParameter("op_name");
    	String op_msg=request.getParameter("op_msg");
    	String op_date=request.getParameter("op_date");
    	String op_type=request.getParameter("op_type");
    	String op_function=request.getParameter("op_function");
    	String op_id=request.getParameter("op_id");
        map.put("op_name", op_name);
    	map.put("op_msg", op_msg);
    	map.put("op_date", op_date);
    	map.put("op_type", op_type);
    	map.put("op_function", op_function);
    	map.put("op_id", op_id);
        
        if ("add".equals(flag)){
        	logService.addLog(map);
        }
        
        JsonResult result = new JsonResult();
        result.setMsg("添加成功");
        result.setSuccess(true);
        //printHttpServletResponse(GsonUtil.toJson(result));
        printHttpServletResponse(result);
    	
    }
    
    //weixuan.lu 查询日志
	public void getLogList(){
      	HttpServletRequest request=getHttpServletRequest();
    	Grid<Log> logGrid=null;
    	String beanObject = getHttpServletRequest().getParameter("data");
    	Log log = GsonUtil.toBean(beanObject,Log.class);
    	Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
    	String logGrid1 =logService.getLogList(log, rows, page);
        printHttpServletResponse(logGrid1);
    }
    
	@Override
	public Object getModel() {
		
		return null;
	}
	
	//导出日志记录
	public void exportLog() {
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		 String data= request.getParameter("exportData");
	       Log log=GsonUtil.toBean(data,Log.class);
		List<Log> logs=logService.ExportLog(log);
		String jsonList = GsonUtil.toJson(logs);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		
		colTitleList.add("操作功能");
		colTitleList.add("日志信息");
		colTitleList.add("操作时间");
		colTitleList.add("业务类型");
		colTitleList.add("日志功能");
		colTitleList.add("操作员");
		
		
		colList.add("op_name");
		colList.add("op_msg");
		colList.add("op_date");
		colList.add("op_type");
		colList.add("op_function");
		colList.add("op_id");
		
		String[] colTitles = new String[colList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="日志信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCountBig.exportExcel("日志信息表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * 时间区间备份日志
	 * @author minting.he
	 */
	public void backUpInterval(){
		
		try {
			String osName = System.getProperties().getProperty("os.name");
			absolutePath=absolutePathLinux;
		    if (osName.contains("Windows")){
		    	absolutePath= absolutePathWindows;
		    }
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			JsonResult result = new JsonResult();
			String startYear= request.getParameter("startYear");
			String endYear= request.getParameter("endYear");
			String startMonth= request.getParameter("startMonth");
			String endMonth= request.getParameter("endMonth");
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			String login_user = loginUser.getYhMc();
			if(EmptyUtil.atLeastOneIsEmpty(startYear, startMonth, endYear, endMonth)){
				result.setSuccess(false);
				result.setMsg("备份失败，数据不合法");
			}else if(EmptyUtil.atLeastOneIsEmpty(login_user)){
				result.setSuccess(false);
				result.setMsg("备份失败，请先登录");
			}else {
				SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
				Calendar cal = Calendar.getInstance();		//构造时间区间
				Date nowDate = cal.getTime();
				String nowTime = dateFormat2.format(nowDate); 
				cal.set(Integer.valueOf(startYear), Integer.valueOf(startMonth)-1, 01, 0, 0, 0);
				Date startDate = cal.getTime();
				String startStr = dateFormat1.format(startDate); 
				Integer endY = Integer.valueOf(endYear);
				Integer endM = Integer.valueOf(endMonth)+1;
				if(endM>=13){
					endY = endY+1;
					endM = 1;
				}
				cal.set(endY, endM-1, 01, 0, 0, 0);
				Date endDate = cal.getTime();
				String endStr = dateFormat1.format(endDate); 
				Map map = new HashMap();
				map.put("startDate", startStr);
				map.put("endDate", endDate);
				List<Log> logs = logService.getLogInterval(map);	//获取时间区间内的日志
				if(logs==null || logs.size()==0){
					result.setSuccess(true);
					result.setMsg("该时间区间内无日志记录");
					getHttpServletRequest().getSession().removeAttribute("downloadBackUp");
					getHttpServletRequest().getSession().removeAttribute("backUpName");
				}else {
					
					String jsonList = GsonUtil.toJson(logs);	//导出日志
					List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
					// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					colTitleList.add("操作功能");
					colTitleList.add("日志信息");
					colTitleList.add("操作时间");
					colTitleList.add("业务类型");
					colTitleList.add("日志功能");
					colTitleList.add("操作员");
					colList.add("op_name");
					colList.add("op_msg");
					colList.add("op_date");
					colList.add("op_type");
					colList.add("op_function");
					colList.add("op_id");
					String[] colTitles = new String[colList.size()];
					String[] cols = new String[colList.size()];
					for (int i = 0; i < colList.size(); i++) {
						cols[i] = colList.get(i);
						colTitles[i] = colTitleList.get(i);
					}
					String fileName=nowTime+"日志备份表";
					File file = new File(absolutePath+fileName+".xls");
					//备份成功，文件全路径包括名称放进session
					 request.getSession().setAttribute("downloadBackUp", absolutePath+fileName+".xls"); 
					 request.getSession().setAttribute("backUpName",fileName+".xls"); 
					 
					FileOutputStream fos = new FileOutputStream(file);
					Workbook workbook = ExcelUtilSpecialCountBig.exportExcel("日志备份表",colTitles, cols, list);
					workbook.write(fos);
					fos.flush();
					fos.close();
					result.setSuccess(true);
					result.setMsg("备份成功，该时间区间内的日志已被删除");
					boolean r = logService.delLogInterval(map, login_user);	//删除备份的日志
					if(r){
						result.setSuccess(true);
						result.setMsg("备份成功，该时间区间内的日志已被删除");
					}else {
						result.setSuccess(false);
						result.setMsg("备份失败");
					}
				}
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	 /**
	    * 下载备份文件工具方法  chudan.guo
	    * @param fileName
	    */
	   public void downloadBackup(){
		   HttpServletRequest request = getHttpServletRequest();
		   HttpServletResponse response = getHttpServletResponse();
		  String  downloadBackUp=(String) request.getSession().getAttribute("downloadBackUp");//从session中获取备份文件的全路径
		  String fileName=(String) request.getSession().getAttribute("backUpName");//从session中获取备份文件的名称
		  if(downloadBackUp!=null && fileName!=null ) {
			  try {
			   OutputStream out = null;
		       //通过文件路径获得File对象(文件夹路径+文件名称)  
		       File file = new File ( downloadBackUp);
		       // 以流的形式下载文件。
		       InputStream fis = new BufferedInputStream(new FileInputStream(file));
		       byte[] buffer = new byte[fis.available()];
		       fis.read(buffer);
		       fis.close();
			     //1.设置文件ContentType类型  
			   	response.setContentType("application/x-msdownload");
					String iso_filename = DownloadFile.toUtf8String(fileName);
				//2.设置文件头：fileName参数是设置下载文件名 
				response.setHeader("Content-Disposition","attachment;filename=" + iso_filename);
		       response.addHeader("Content-Length", "" + file.length());
		       OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		       toClient.write(buffer);
		       toClient.flush();
		       toClient.close();
			   } catch (IOException ex) {
			       ex.printStackTrace();
			   }
		  }
	   }
	   
	   
	
	
}