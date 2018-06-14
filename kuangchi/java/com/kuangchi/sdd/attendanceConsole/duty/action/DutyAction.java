package com.kuangchi.sdd.attendanceConsole.duty.action;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.duty.service.DutyService;
import com.kuangchi.sdd.attendanceConsole.dutyuser.service.DutyUserService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * 工作班次维护
 *
 */
@Controller("dutyAction")
public class DutyAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getModel() {
		return null;
	}

	@Resource(name = "dutySerivceImpl")
	private DutyService dutyService;
	

	Duty dutySearch = null;
	
	public Duty getDutySearch() {
		return dutySearch;
	}
	public void setDutySearch(Duty dutySearch) {
		this.dutySearch = dutySearch;
	}

	/**
	 * 查询班次
	 * @author minting.he
	 */
	public void selAllDuty(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<Duty> dutyGrid = null;
        String beanObject = request.getParameter("data");			
        dutySearch = GsonUtil.toBean(beanObject, Duty.class);
        if(BeanUtil.isNotEmpty(dutySearch)){
	        if(!EmptyUtil.atLeastOneIsEmpty(dutySearch.getDuty_name())){
	        	dutySearch.setDuty_name(dutySearch.getDuty_name().trim().replace("<","&lt").replace(">","&gt"));
	        }
        }
        dutySearch.setPage(Integer.parseInt(request.getParameter("page")));
        dutySearch.setRows(Integer.parseInt(request.getParameter("rows")));
        dutyGrid = dutyService.getDutyByParamPage(dutySearch);
        printHttpServletResponse(GsonUtil.toJson(dutyGrid));
	}
	
	/**
	 * 通过id查看班次信息
	 * @author minting.he
	 */
	public void selDutyById(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
        String id = request.getParameter("id");	
        if(EmptyUtil.atLeastOneIsEmpty(id)){
        	result.setSuccess(false);
    	    result.setMsg("查询失败，数据不合法");
        }
        Duty duty = dutyService.getDutyById(id);
        printHttpServletResponse(GsonUtil.toJson(duty));
	}

	/**
	 * 查询有没排班 决定是否启用
	 * @author minting.he
	 */
	public void dutyStatus(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String id = request.getParameter("id");
        if(EmptyUtil.atLeastOneIsEmpty(id)){
        	result.setSuccess(false);
    	    result.setMsg("查询失败，数据不合法");
        }else {
        	Integer count = dutyService.getDutyUserByDutyId(id);
    		result.setSuccess(true);
    	    result.setMsg(count);
        }
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除班次
	 * @author minting.he
	 */
	public void delDuty(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String id = request.getParameter("id");
		if(EmptyUtil.atLeastOneIsEmpty(id)){
			result.setSuccess(false);
    	    result.setMsg("删除失败，数据不合法");
		}else {
			User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
			if(loginUser == null){
				result.setSuccess(false);
			    result.setMsg("操作失败，请先登录");
			} else {
				String create_user = loginUser.getYhMc();
				boolean r = dutyService.deleteDutyById(id, create_user);
				if(r){
					result.setSuccess(true);
				    result.setMsg("删除成功");
				}else {
					result.setSuccess(false);
				    result.setMsg("删除失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 查询班次名称唯一
	 * @author minting.he
	 */
	public void selDutyName(){
		HttpServletRequest request = getHttpServletRequest();
		String duty_name = request.getParameter("duty_name");
		Duty duty = new Duty();
		duty.setDuty_name(duty_name.trim().replace("<","&lt").replace(">","&gt"));
		Integer count;
		if("".equals(request.getParameter("id"))){
			count = dutyService.getDutyByParamCounts(duty);
		}else {
			Integer id = Integer.valueOf(request.getParameter("id"));
			duty.setId(id);
	        count = dutyService.getDutyByParamCounts(duty);
		}
		printHttpServletResponse(GsonUtil.toJson(count));
	}
	
	/**
	 * 修改班次
	 * @author minting.he
	 */
	public void updateDuty(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
        String beanObject = request.getParameter("dataForm");
        Duty duty = GsonUtil.toBean(beanObject, Duty.class);
        String is_default = duty.getIs_default();
        String id = request.getParameter("dutyId");
        String is_elastic = request.getParameter("isElastic");	
        duty.setDuty_name(duty.getDuty_name().trim().replace("<","&lt").replace(">","&gt"));
        duty.setId(Integer.valueOf(id.trim()));
        if(BeanUtil.isEmpty(duty) || EmptyUtil.atLeastOneIsEmpty(id, is_elastic) || dutyService.getDutyByParamCounts(duty)!=0){
        	result.setSuccess(false);
    	    result.setMsg("修改失败，数据不合法");
        }else {
            duty.setIs_elastic(is_elastic);
            if(EmptyUtil.atLeastOneIsEmpty(duty.getVocation())){
            	duty.setVocation("7");
            }
            if(EmptyUtil.atLeastOneIsEmpty(duty.getIs_default())){
            	duty.setIs_default("1");
            }
            User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
            if(loginUser == null){
				result.setSuccess(false);
			    result.setMsg("操作失败，请先登录");
			} else {
				String create_user = loginUser.getYhMc();
	    		if("1".equals(is_default)){
	    			dutyService.updateAllIsDefault(id);
	    		}
				boolean r = dutyService.updateDuty(duty, create_user);
	            if(r){
	            	result.setSuccess(true);
	        	    result.setMsg("修改成功");
	            }else {
	            	result.setSuccess(false);
	        	    result.setMsg("修改失败");
	            }
			}
        }
        printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 新增班次
	 * @author minting.he
	 */
	public void insertDuty(){
			HttpServletRequest request = getHttpServletRequest();
	        String beanObject = request.getParameter("dataForm");			
	        Duty duty = GsonUtil.toBean(beanObject, Duty.class);
	        JsonResult result = new JsonResult();
	        duty.setDuty_name(duty.getDuty_name().trim().replace("<","&lt").replace(">","&gt"));
	        if(BeanUtil.isEmpty(duty) || dutyService.getDutyByParamCounts(duty)!=0){
	        	result.setSuccess(false);
	     	    result.setMsg("新增失败，数据不合法");
	        }else {
	        	if(duty.getVocation()==null || "".equals(duty.getVocation())){
	            	duty.setVocation("7");
	            }
	            User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
	            if(loginUser == null){
					result.setSuccess(false);
				    result.setMsg("操作失败，请先登录");
				} else {
					String create_user = loginUser.getYhMc();
					
					boolean r = dutyService.insertDuty(duty, create_user);
		            if(r){
		            	result.setSuccess(true);
		        	    result.setMsg("新增成功");
		            }else {
		            	result.setSuccess(false);
		        	    result.setMsg("新增失败");
		            }
				}
	        }
        	printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 设置为默认班次
	 * @author minting.he
	 */
	public void setDefaultDuty(){
		HttpServletRequest request = getHttpServletRequest();
        final String new_id = request.getParameter("id");			
        JsonResult result = new JsonResult();
        if(EmptyUtil.atLeastOneIsEmpty(new_id)){
        	result.setSuccess(false);
     	    result.setMsg("设置失败，数据不合法");
        }else {
            User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
            if(loginUser == null){
				result.setSuccess(false);
			    result.setMsg("设置失败，请先登录");
			} else {
				final String create_user = loginUser.getYhMc();
				boolean r = dutyService.setDefaultDuty(new_id, create_user);
				if(r){
					result.setSuccess(true);
				    result.setMsg("设置成功");
				}else {
					result.setSuccess(false);
				    result.setMsg("设置失败");
				}
			}
        }
    	printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 导出勾选的班次
	 * @author minting.he
	 */
	public void exportDuty(){
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		List<Duty> dutyList = dutyService.getDutyByParam(dutySearch);
        for(int i=0; i<dutyList.size(); i++){
        	String is_elastic = dutyList.get(i).getIs_elastic();
        	if("0".equals(is_elastic)){	//是否弹性
        		dutyList.get(i).setIs_elastic("否");
        	} else {
        		dutyList.get(i).setIs_elastic("是");
        	}
        	String flag_status = dutyList.get(i).getFlag_status(); //是否启用
        	if("0".equals(flag_status)){
        		dutyList.get(i).setFlag_status("否");
        	} else{
        		dutyList.get(i).setFlag_status("是");
        	}
        	String is_default=dutyList.get(i).getIs_default(); //默认班次
        	if("0".equals(is_default)){
        		dutyList.get(i).setIs_default("否");
        	}else{
        		dutyList.get(i).setIs_default("是");
        	}
        	String vocation = dutyList.get(i).getVocation(); //节假日
        	String[] str = vocation.split(",");
        	vocation = "";
        	for(int j=0; j<str.length; j++){
        		if("0".equals(str[j])){
        			vocation += "星期日 ";
        		}else if("1".equals(str[j])){
        			vocation += "星期一 ";
        		}else if("2".equals(str[j])){
        			vocation += "星期二 ";
        		}else if("3".equals(str[j])){
        			vocation += "星期三 ";
        		}else if("4".equals(str[j])){
        			vocation += "星期四 ";
        		}else if("5".equals(str[j])){
        			vocation += "星期五 ";
        		}else if("6".equals(str[j])){
        			vocation += "星期六 ";
        		}else if("7".equals(str[j])) {
        			vocation += "无 ";
        			break;
        		}
        	}
        	dutyList.get(i).setVocation(vocation);
        }
	    String jsonList= GsonUtil.toJson(dutyList);
	    //list中的map为 ：    列键----列值的方式
	    List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
	    OutputStream out = null;
	    
	    Field[] fields = Duty.class.getDeclaredFields();
	    List<String> colList=new ArrayList<String>();
	    List<String> colTitleList =new ArrayList<String>();
	    
	    for (int i = 0; i < fields.length; i++) {
	    	String fieldName=fields[i].getName();
			if("duty_name".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("班次名称");
			}else if("vocation".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("公休日");
			}else if("is_elastic".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("是否弹性工作");
			}else if("duty_start_check_point".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("开始统计打卡时间");
			}else if("duty_end_check_point".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("结束统计打卡时间");
			}else if("duty_time1".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("上班时间");
			}else if("duty_time2".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("中途下班时间");
			}else if("duty_time3".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("中途上班时间");
			}else if("duty_time4".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("下班时间");
			}else if("duty_time1_point".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("上班迟到时间");
			}else if("duty_time2_point".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("中途下班早退时间");
			}else if("duty_time3_point".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("中途上班迟到时间");
			}else if("duty_time4_point".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("下班早退时间");
			}else if("duty_time1_absent".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("上班旷工时间");
			}else if("duty_time2_absent".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("中途下班旷工时间");
			}else if("duty_time3_absent".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("中途上班旷工时间");
			}else if("duty_time4_absent".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("下班旷工时间");
			}else if("over_time_start".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("加班开始时间");
			}else if("over_time_end".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("加班结束时间");
			}else if("elastic_default_duty_time1".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("弹性上班时间");
			}else if("elastic_default_duty_time2".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("弹性下班时间");
			}else if("elastic_duty_time1".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("弹性工作必须在岗时间起");
			}else if("elastic_duty_time2".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("弹性工作必须在岗时间止");
			}else if("elastic_time_absent_time".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("弹性至少工作时间");
			}else if("is_default".equals(fieldName)){
				colList.add(fieldName);
				colTitleList.add("是否默认班次");
			}
		}   
	    //列数据键
	    String[] cols=new String[colList.size()];
	    //列表题
	    String[] colTitles=new String[colList.size()];    	        
	    for (int i=0; i<colList.size(); i++) {
			cols[i]=colList.get(i);
			colTitles[i]=colTitleList.get(i);
	    }
	    try {
	    	out = response.getOutputStream();
	        response.setContentType("application/x-msexcel");
	        String fileName="工作班次信息表.xls";
	        response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
	        Workbook workbook = ExcelUtilSpecial.exportExcel("工作班次信息表",colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	    	try {
	    		if(out!=null){
	    			out.close();
	    		}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	
}
