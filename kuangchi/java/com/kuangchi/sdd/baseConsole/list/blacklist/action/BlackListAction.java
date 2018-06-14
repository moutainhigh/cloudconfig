package com.kuangchi.sdd.baseConsole.list.blacklist.action;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.baseConsole.holiday.model.Holiday;
import com.kuangchi.sdd.baseConsole.holiday.model.HolidayType;
import com.kuangchi.sdd.baseConsole.list.blacklist.model.BlackList;
import com.kuangchi.sdd.baseConsole.list.blacklist.service.BlackListService;
import com.kuangchi.sdd.baseConsole.list.whitelist.model.WhiteList;
import com.kuangchi.sdd.baseConsole.list.whitelist.service.WhiteListService;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("blackListAction")
public class BlackListAction extends BaseActionSupport {

	
   private BlackList blackList; 
   private Department model;
   private DepartmentPage departmentPage;
/*	
   @Resource(name = "departmentServiceImpl")
   private IDepartmentService departmentService;*/

   @Resource(name = "employeeService")
   EmployeeService employeeService;
   
   @Resource(name = "blackListServiceImpl")
   BlackListService blackListService;
   
   @Resource(name = "whiteListServiceImpl")
   WhiteListService whiteListService;
   
   @Resource(name = "peopleAuthorityService")
   PeopleAuthorityInfoService peopleAuthorityService;
   
	@Resource(name = "cardServiceImpl")
	private ICardService cardService;
   
   
   @Override
	public Object getModel() {
		return model;
	}
	
	/**
	 * 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-4-7 上午8:57:11
	 * @功能描述: 新增用户绑定的卡(移入黑名单)
	 * @参数描述:
	 */
	public  void  addBlackList(){
		    HttpServletRequest request = getHttpServletRequest();
		    User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String create_user = loginUser.getYhMc();
	        String yhDm = request.getParameter("yhDm");
	        String description = request.getParameter("description");
	        ResultMsg resultMsg = new ResultMsg();
	        if (EmptyUtil.atLeastOneIsEmpty(yhDm)) {
				resultMsg.setResult_msg("没有选择移入黑名单用户");
	        	resultMsg.setResult_code("1");
				printHttpServletResponse(GsonUtil.toJson(resultMsg));
				return;
	        }
	        Grid<BoundCard> gb = employeeService.selectBoundCardInfoByYHDM2(yhDm);//查询用户下绑定的卡
	        List<BoundCard> boundCardList = gb.getRows();
	        boolean result = true;
	        	//List<PeopleAuthorityInfoModel> authorityList =	peopleAuthorityService.getAuthorityInfoByStaffNum(yhDm);
	        	//blackListService.deleteAutorityCardByStaffNum(authorityList);//删除权限
	        	if(boundCardList!=null && !boundCardList.isEmpty()){
	        		for (BoundCard boundCard : boundCardList) {// 逐个删除权限
	    				if (cardService.frozenCard(boundCard.getCard_num(),
	    						loginUser.getYhMc()) == 0) {
	    					if(!blackListService.addBlackList(boundCard,yhDm,description,create_user)){
	    						result = false;
	    						break;
	    					}
	    				}else{
	    					result = false;
	    					break;
	    				}
	    			}
	        		if(!result){
	        			resultMsg.setResult_msg("移入黑名单失败");
	        			resultMsg.setResult_code("1");
	        		}else{
	        			resultMsg.setResult_msg("移入黑名单成功");
	        			resultMsg.setResult_code("0");
	        		}
        			printHttpServletResponse(GsonUtil.toJson(resultMsg));
	        	}else{
	        		boolean res = blackListService.addBlackListNoBoundCard(yhDm,description,create_user);
	        		if(res){
	        			resultMsg.setResult_msg("移入黑名单成功");
	    	        	resultMsg.setResult_code("0");
	        			printHttpServletResponse(GsonUtil.toJson(resultMsg));
	        		}else{
	        			resultMsg.setResult_msg("移入黑名单失败");
	    	        	resultMsg.setResult_code("1");
	        			printHttpServletResponse(GsonUtil.toJson(resultMsg));
	        		}
	        	}
	        }
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-4-7 下午4:00:57
	 * @功能描述:条件查询黑名单信息 
	 */
	public void getBlackListByParamPage(){
		HttpServletRequest request = getHttpServletRequest();
        String beanObject = request.getParameter("data");
        BlackList blackList = GsonUtil.toBean(beanObject, BlackList.class);    //将数据转化为javabean
        blackList.setPage(Integer.parseInt(request.getParameter("page")));
        blackList.setRows(Integer.parseInt(request.getParameter("rows")));
      /*  String staff_num =  employeeService.selectStaffNum(blackList.getStaff_num());
        blackList.setStaff_num(staff_num);*/
        Grid<BlackList> blackListGrid = blackListService.getBlackListByParamPage(blackList);
        printHttpServletResponse(GsonUtil.toJson(blackListGrid));
		
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-4-8 上午11:01:08
	 * @功能描述: 移出黑名单
	 */
	public void deleteBlackListByStaffNums(){
		ResultMsg resultMsg = new ResultMsg();
		HttpServletRequest request = getHttpServletRequest();
		
		if(EmptyUtil.isEmpty(request.getParameter("select_staff_nums"))){
			resultMsg.setResult_msg("移出失败，数据异常");
			resultMsg.setResult_code("1");
			printHttpServletResponse(GsonUtil.toJson(resultMsg));
			return;
		}
		String[] select_staff_nums = request.getParameter("select_staff_nums").split(",");
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean isSuccess = false;
		boolean isFail = false;
		boolean clearAuthority = true;
		for(String staffNum: select_staff_nums){
			Grid<BoundCard> gb = employeeService.selectBoundCardByYHDM352(staffNum);//查询用户下绑定的卡
		    List<BoundCard> boundCardList = gb.getRows();
			//List<PeopleAuthorityInfoModel> authorityList = peopleAuthorityService.getDelAuthorityInfoByStaffNum(staffNum);
			if(boundCardList!=null && !boundCardList.isEmpty()){
				for (BoundCard boundCard : boundCardList) {// 逐个新增权限
    				if (cardService.unfreezeCard(boundCard.getCard_num(),
    						loginUser.getYhMc()) != 0) {
    					clearAuthority = false;
    					break;
    				
    				}
				}
				if(clearAuthority){
					 if(!blackListService.deleteBlackListByStaffNum(staffNum,create_user)){
						 isFail = true;
						 break;
					 }else{
						 isSuccess = true;	
					 }
				}else{
					 isFail = true;
					 break;
				}
			}else{
				boolean res = blackListService.deleteBlackListByStaffNum(staffNum,create_user);
				if(!res){
					isFail=true;
					break;
				}else{
					isSuccess=true;
				}
			}
		}
		
		if(isFail&&isSuccess){
			resultMsg.setResult_msg("移出部分黑名单失败");
        	resultMsg.setResult_code("1");
		}else if(isSuccess&&!isFail){
			resultMsg.setResult_msg("移出黑名单成功");
        	resultMsg.setResult_code("0");
		}else if(!isSuccess&&isFail){
			resultMsg.setResult_msg("移出黑名单失败");
        	resultMsg.setResult_code("1");
		}else{
			//全部失败
			resultMsg.setResult_msg("移出黑名单失败");
        	resultMsg.setResult_code("1");
		}
		printHttpServletResponse(GsonUtil.toJson(resultMsg));
	}
	/*	boolean isSuccess = false;
		boolean isFail = false;
		for(String staffNum: select_staff_nums){
			List<PeopleAuthorityInfoModel> authorityList = peopleAuthorityService.getAuthorityInfoByStaffNum(staffNum);
			if(authorityList!=null && !authorityList.isEmpty()){
				resultMsg = blackListService.insertAutorityCardByStaffNum(authorityList);
				if("0".equals(resultMsg.getResult_code())){
					if(blackListService.deleteBlackListByStaffNum(staffNum,create_user)){
						isSuccess = true;
					}else{
						isFail = true;
					}
				}else{
    	        	isFail=true;
				}
			}else{
				boolean res = blackListService.deleteBlackListByStaffNum(staffNum,create_user);
				if(!res){
					isFail=true;
				}else{
					isSuccess=true;
				}
			}
		}
		if(isSuccess&&isFail){
			//部分成功
			resultMsg.setResult_msg("移出部分黑名单失败");
        	resultMsg.setResult_code("1");
		}else if(isSuccess&&!isFail){
			//全部成功
			resultMsg.setResult_msg("移出黑名单成功");
        	resultMsg.setResult_code("0");
		}else if(!isSuccess&&isFail){
			//全部失败
			resultMsg.setResult_msg("移出黑名单失败");
        	resultMsg.setResult_code("1");
		}else{
			//全部失败
			resultMsg.setResult_msg("移出黑名单失败");
        	resultMsg.setResult_code("1");
		}
		printHttpServletResponse(GsonUtil.toJson(resultMsg));
		*/
	
	
	
	 /**
     * @创建人　: 邓积辉
     * @创建时间: 2016-4-10 下午2:35:44
     * @功能描述: 导出查询条件下的黑名单信息
     * @参数描述:
     */
    public void downloadBlackListByParam() {   
    	        HttpServletResponse response = getHttpServletResponse();
    	        HttpServletRequest request = getHttpServletRequest();
    	        String beanObject = request.getParameter("data");
    	        BlackList blackList = GsonUtil.toBean(beanObject, BlackList.class); 
    	        
    	        List<BlackList> bList=blackListService.getBlackListByParam(blackList);
    	        for(int i=0;i < bList.size();i++){
                	String yhDm = bList.get(i).getStaff_num();
    	        	List<String> whiteListStaffName = whiteListService.getStaffNameByParam(yhDm);
    	        	bList.get(i).setStaff_num(whiteListStaffName.get(0));
    	        	if("0".equals(bList.get(i).getValidity_flag())){
    	        		bList.get(i).setValidity_flag("有效");
    	        	}else{
    	        		bList.get(i).setValidity_flag("无效");
    	        	}
    	        }
    	        String jsonList= GsonUtil.toJson(bList);
    	        //list中的map为 ：    列键----列值的方式
    	        List list =GsonUtil.getListFromJson(jsonList, ArrayList.class);
    	        OutputStream out = null;
    	        
    	        Field[] fields=BlackList.class.getDeclaredFields();
    	        List<String> colList=new ArrayList<String>();
    	        List<String> colTitleList =new ArrayList<String>();
    	        
    	        for (int i = 0; i < fields.length; i++) {
    	        	String fieldName=fields[i].getName();
    	        	if("staff_no".equals(fieldName)){
    	        		colList.add(fieldName);
    	        		colTitleList.add(0, "员工工号");
    	        	}else if("staff_num".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("员工名称");
					}else if("card_num".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("卡号");
					}else if("validity_flag".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("是否有效");
					}else if("create_user".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("创建人");
					}else if("create_time".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("创建时间");
					}else if("update_time".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("修改时间");
					}else if("description".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("描述");
					}
				}   
    	        //列数据键
    	        String[] cols=new String[colList.size()];
    	        //列表题
    	        String[] colTitles=new String[colList.size()];    	        
    	       for (int i = 0; i < colList.size(); i++) {
				cols[i]=colList.get(i);
				colTitles[i]=colTitleList.get(i);
			}
    	        try {
    	            out = response.getOutputStream();
    	            response.setContentType("application/x-msexcel");
    	            String fileName="黑名单信息表.xls";
    	            response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
    	            Workbook workbook = ExcelUtilSpecial.exportExcel("黑名单表",colTitles, cols, list);
    	            workbook.write(out);
    	            out.flush();
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	    }

	
	
	
	
}
