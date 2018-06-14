package com.kuangchi.sdd.baseConsole.holiday.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.holiday.model.Holiday;
import com.kuangchi.sdd.baseConsole.holiday.model.HolidayType;
import com.kuangchi.sdd.baseConsole.holiday.service.HolidayService;
import com.kuangchi.sdd.baseConsole.holiday.util.ExcelExportTemplate;
import com.kuangchi.sdd.baseConsole.holiday.util.ExcelUtilSpecial;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * @创建人　: 杨金林
 * @创建时间: 2016-3-25 上午9:32:05
 * @功能描述: 节假日模块Action
 */

@Controller("holidayAction")
public class HolidayAction extends BaseActionSupport implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(HolidayAction.class);

	private Holiday holiday;
	private HolidayType holidayType;

	private File uploadHolidayFile;

	public File getUploadHolidayFile() {
		return uploadHolidayFile;
	}

	public void setUploadHolidayFile(File uploadHolidayFile) {
		this.uploadHolidayFile = uploadHolidayFile;
	}

	public HolidayAction() {
		holiday = new Holiday();
	}

	@Resource(name = "holidayServiceImpl")
	transient private HolidayService holidayService;

	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;

	@Resource(name = "employeeService")
	EmployeeService employeeService;

	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;

	@Override
	public Object getModel() {
		return holiday;
	}

	/**
	 * @创建人　: 梁豆豆
	 * @创建时间: 2016-3-25 下午5:33:14
	 * @功能描述: 修改节假日信息
	 * @参数描述: login_User登陆的用户的名字
	 */
	public void modifyHoliday() {
		JsonResult result = new JsonResult(); // 声明“写入操作”响应对象
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();

		String data = request.getParameter("data");
		Holiday holiday = GsonUtil.toBean(data, Holiday.class);
		// holiday.setHoliday_scope("2");

		holiday.setHoliday_begin_date(holiday.getHoliday_begin_date()
				+ " 00:00:00");
		holiday.setHoliday_end_date(holiday.getHoliday_end_date() + " 23:59:59");
		holiday.setDept_num("," + holiday.getDept_num().replace("'", "") + ",");

		try {
			holidayService.modifyHoliday(holiday, login_User);
			result.setMsg("修改成功");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("操作失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 梁豆豆
	 * @创建时间: 2016-3-25 下午5:33:50
	 * @功能描述: 修改节假日类型信息
	 * @参数描述: login_User登陆的用户的名字
	 */
	public void modifyHolidayType() {
		JsonResult result = new JsonResult(); // 声明“写入操作”响应对象
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();

		String data = request.getParameter("data");
		HolidayType holidayType = GsonUtil.toBean(data, HolidayType.class);

		Integer holiday_type_id = Integer.valueOf(holidayType
				.getHoliday_type_id());
		String holiday_name = holidayType.getHoliday_name();
		String holiday_validity = holidayType.getHoliday_validity();

		if (EmptyUtil.atLeastOneIsEmpty(holiday_name, holiday_validity)) {
			result.setMsg("参数输入有误");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		List<HolidayType> ht = holidayService.getHolidayTypeByName(holidayType);

		if (ht.size() != 0
				&& !ht.get(0).getHoliday_type_id().equals(holiday_type_id)) {
			result.setMsg("修改失败：该类型名称已经存在！");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		try {
			holidayService.modifyHolidayType(holidayType, login_User);
			result.setMsg("修改成功");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("操作失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-3-25 下午6:16:07
	 * @功能描述: 删除节假日信息
	 * @参数描述:
	 */
	public void deleteHolidayInfo() {
		/*
		 * 1、从页面取得节假日Id和节假日编号
		 * 2、遍历节假日编号，如果对象时段组中是否有对象类型是节假日类型（OBJECT_TYPE=2），且对象编号与节假日编号相同
		 * ，则不可删除，否则按节假日id删除节假日
		 */
		JsonResult result = new JsonResult();

		HttpServletRequest request = getHttpServletRequest();
		String holiday_ids = request.getParameter("holiday_ids");
		String[] holiday_nums = request.getParameter("holiday_nums").split(","); // 节假日编号数组
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();

		/*
		 * for (int i = 0; i < holiday_nums.length; i++) { String holiday_num =
		 * holiday_nums[i]; if
		 * (holidayService.getObjectTimeInfo(holiday_num).size() != 0) {
		 * result.setMsg("对象时段组下包含该节假日" + "，不可删除"); result.setSuccess(false);
		 * printHttpServletResponse(GsonUtil.toJson(result)); return; } }
		 */

		holidayService.deleteHolidayById(holiday_ids, create_user);
		result.setMsg("删除成功");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-3-25 下午5:19:22
	 * @功能描述: 删除节假日类型(通过类型编号删除)
	 * @参数描述:
	 */
	public void deleteHolidayType() {
		/*
		 * 1、从页面取得节假日类型id和节假日类型编号
		 * 2、遍历节假日类型编号，判断是否有节假日的类型与该编号相同，若有，则不可删除，否则根据id删除节假日类型
		 */
		JsonResult result = new JsonResult();

		HttpServletRequest request = getHttpServletRequest();
		String holiday_type_ids = request.getParameter("holiday_type_ids");
		String[] holiday_type_nums = request.getParameter("holiday_type_nums")
				.split(",");
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();

		for (int i = 0; i < holiday_type_nums.length; i++) {
			String holiday_type_num = holiday_type_nums[i];
			Holiday holiday = new Holiday();
			holiday.setHoliday_type_num(holiday_type_num);

			if (holidayService.getHolidayByType(holiday).size() != 0) {
				HolidayType holidayType = new HolidayType();
				holidayType.setHoliday_type_num(holiday_type_num);
				String holiday_type_name = holidayService
						.getHolidayTypeByParam(holidayType).get(0)
						.getHoliday_name();
				result.setMsg(holiday_type_name + "类型下包含节假日信息，不可删除");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return;
			}
		}

		holidayService.deleteHolidayTypeById(holiday_type_ids, create_user);
		result.setMsg("删除成功");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-3-28 上午9:43:12
	 * @功能描述: 条件查询节假日类型
	 */
	public void getHolidayTypeByParam() {
		String beanObject = getHttpServletRequest().getParameter("data");// 获取前台序列化的数据
		HolidayType holidayType = GsonUtil
				.toBean(beanObject, HolidayType.class);
		List<HolidayType> htList = holidayService
				.getHolidayTypeByParam(holidayType);
		printHttpServletResponse(GsonUtil.toJson(htList));
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-3-28 上午9:43:12
	 * @功能描述: 条件查询节假日类型（分页）
	 */
	public void getHolidayTypeByParamPage() {

		String beanObject = getHttpServletRequest().getParameter("data");// 获取前台序列化的数据
		HolidayType holidayType = GsonUtil
				.toBean(beanObject, HolidayType.class);
		String page = getHttpServletRequest().getParameter("page");
		String size = getHttpServletRequest().getParameter("rows");

		String beginDate = getHttpServletRequest().getParameter("beginDate");
		String endDate = getHttpServletRequest().getParameter("endDate");

		List<HolidayType> htList = holidayService.getHolidayTypeByParamPage(
				holidayType, Integer.valueOf(page), Integer.valueOf(size),
				beginDate, endDate);
		int allCount = holidayService.getHolidayTypeByParamCount(holidayType,
				beginDate, endDate);
		Grid<HolidayType> grid = new Grid<HolidayType>();
		grid.setTotal(allCount);
		grid.setRows(htList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-3-28 上午9:43:12
	 * @功能描述: 条件查询节假日
	 */
	// 参数查询节假日
	public void getHolidayByParam() {
		String beanObject = getHttpServletRequest().getParameter("data");// 获取前台序列化的数据
		Holiday holiday = GsonUtil.toBean(beanObject, Holiday.class);
		List<Holiday> hList = holidayService.getHolidayByParam(holiday);
		printHttpServletResponse(GsonUtil.toJson(hList));

	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-3-28 上午9:43:12
	 * @功能描述: 条件查询节假日（分页）
	 */
	public void getHolidayByParamPage() {
		JsonResult result = new JsonResult();
		String beanObject = getHttpServletRequest().getParameter("data");// 获取前台序列化的数据
		if (EmptyUtil.isEmpty(beanObject)) {
			result.setMsg("请输入参数");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}

		Holiday holiday = GsonUtil.toBean(beanObject, Holiday.class);
		String page = getHttpServletRequest().getParameter("page");
		String size = getHttpServletRequest().getParameter("rows");

		List<Holiday> hList = holidayService.getHolidayByParamPage(holiday,
				Integer.valueOf(page), Integer.valueOf(size));
		for (Holiday holiday2 : hList) {
			holiday2.setHoliday_begin_date(holiday2.getHoliday_begin_date()
					.split(" ")[0]);
			holiday2.setHoliday_end_date(holiday2.getHoliday_end_date().split(
					" ")[0]);
		}

		int allCount = holidayService.getHolidayByParamCount(holiday);
		Grid<Holiday> grid = new Grid<Holiday>();
		grid.setTotal(allCount);
		grid.setRows(hList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * 
	 * @创建人　: 梁豆豆
	 * @创建时间: 2016-3-25 下午8:49:43
	 * @功能描述: 查询节假日所有类型并返回含有类型编号的list
	 * @参数描述: type 查到的所有类型的集合 list 所有类型编号的集合
	 */
	public void getHolidayTypeByParam1() {
		HolidayType holidayType = new HolidayType();
		holidayType.setValidity_flag("0");
		List<HolidayType> holidayTypeList = holidayService
				.getHolidayTypeByParam(holidayType);
		printHttpServletResponse(GsonUtil.toJson(holidayTypeList));
	}

	/**
	 * 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-3-25 下午6:07:07
	 * @功能描述: 新增节假日类型信息
	 * @参数描述:
	 */
	public void addHolidayType() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String data = request.getParameter("data");
		HolidayType holidayType = GsonUtil.toBean(data, HolidayType.class);

		String holiday_name = holidayType.getHoliday_name();
		String holiday_validity = holidayType.getHoliday_validity();
		String holiday_remark = holidayType.getHoliday_remark();

		if (EmptyUtil.atLeastOneIsEmpty(holiday_name, holiday_validity)) {
			result.setMsg("带*号为必填项");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		holidayType = new HolidayType();
		holidayType.setHoliday_name(holiday_name);
		holidayType.setHoliday_type_num(UUIDUtil.getUUID32());
		holidayType.setHoliday_validity(holiday_validity);
		holidayType.setHoliday_remark(holiday_remark);
		holidayType.setCreate_user(loginUser.getYhMc());

		if (holidayService.getHolidayTypeByName(holidayType).size() != 0) {
			result.setMsg("新增失败：该类型名称已经存在！");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		holidayService.addHolidayType(holidayType);
		try {
			result.setMsg("新增成功");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("操作失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-3-25 下午6:07:07
	 * @功能描述: 新增节假日信息
	 * @参数描述:
	 */
	public void addHolidayInfo() {
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		if (EmptyUtil.isEmpty(beanObject)) {
			result.setMsg("带*号为必填项");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		Holiday holiday = GsonUtil.toBean(beanObject, Holiday.class);
		holiday.setCreate_user(loginUser.getYhMc());
		holiday.setHoliday_num(UUIDUtil.getUUID32());
		holiday.setHoliday_type_num("1");
		holiday.setHoliday_begin_date(holiday.getHoliday_begin_date()
				+ " 00:00:00");
		holiday.setHoliday_end_date(holiday.getHoliday_end_date() + " 23:59:59");
		if (holiday.getDept_num() != null) {
			holiday.setDept_num("," + holiday.getDept_num().replace("'", "")
					+ ",");
		} else {
			StringBuffer deptStr = new StringBuffer();
			List<Department> allDept = departmentService.getAllDepart();
			for (Department dept : allDept) {
				deptStr.append("," + dept.getBmDm());
			}
			deptStr.append(",");
			holiday.setDept_num(deptStr.toString());
		}

		// holiday.setHoliday_scope("2"); //农行系统专用
		try {
			holidayService.addHoliday(holiday);
			result.setMsg("新增成功");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("操作失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-8 上午11:42:15
	 * @功能描述: 下载批量导入模板
	 * @参数描述:
	 */
	public void downloadHolidayTemplate() {
		HttpServletResponse response = getHttpServletResponse();
		OutputStream out = null;
		ExcelExportTemplate excelExport = new ExcelExportTemplate();
		List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();

		List<String> list = holidayService.getAllHolidayNameType();

		// List<String> list = dutyUserService.getAllDutyName();//获取全部排班类型
		// List<TitleRowCell> tRCs = new ArrayList<TitleRowCell>();
		// List<String> list = employeeService.getAllStaffDept();//获取全部部门代码
		/*
		 * TitleRowCell t1t = new TitleRowCell("节假日名称",true); for(String
		 * holiday_types:list){ t1t.addSuggest(holiday_types); }
		 */
		TitleRowCell t2t = new TitleRowCell("开始时间", true);
		TitleRowCell t3t = new TitleRowCell("结束时间", true);
		TitleRowCell t4t = new TitleRowCell("假日时段", true);
		t4t.addSuggest("全天");
		t4t.addSuggest("上午");
		t4t.addSuggest("下午");
		TitleRowCell t5t = new TitleRowCell("作用范围", true);
		t5t.addSuggest("全体员工");
		t5t.addSuggest("男员工");
		t5t.addSuggest("女员工");
		TitleRowCell t6t = new TitleRowCell("作用部门", true);

		// 查询是否分层
		boolean isLayer = roleService.isLayer();
		String layerDeptNum = null;
		if (isLayer) {
			Role role = (Role) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			layerDeptNum = departmentService.deptGetLayerDeptNum(
					user.getYhDm(), role.getJsDm());
		}
		// List<String> depts = employeeService.getAllStaffDept(layerDeptNum);//
		// 获取全部部门编号
		List<String> depts = employeeService.getAllStaffDeptNum(layerDeptNum);// 获取全部部门代码xuewen.deng2017-05-15
		for (String dept : depts) {
			t6t.addSuggest(dept);
		}

		TitleRowCell t7t = new TitleRowCell("设置类型", true);
		t7t.addSuggest("节假日");
		t7t.addSuggest("补班");
		TitleRowCell t8t = new TitleRowCell("描述", false);

		// titleRowCells.add(t1t);
		titleRowCells.add(t7t);
		titleRowCells.add(t2t);
		titleRowCells.add(t3t);
		titleRowCells.add(t4t);
		titleRowCells.add(t5t);
		titleRowCells.add(t6t);
		titleRowCells.add(t8t);
		excelExport.createLongTitleRow(titleRowCells);
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "节假日信息模版.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			excelExport.getWorkbook().write(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * /**
	 * 
	 * @创建人　: 陈凯颖
	 * 
	 * @创建时间: 2016-3-31 上午9:43:12
	 * 
	 * @功能描述: 下载holiday批量导入模板
	 */
	/*
	 * public void downloadHolidayTemplate(){ HttpServletRequest
	 * request=getHttpServletRequest(); HttpServletResponse
	 * response=getHttpServletResponse();
	 * response.setContentType("application/x-msexcel");
	 * response.setHeader("Content-Disposition",
	 * "attachment;filename=holiday_template.xls"); String path=
	 * request.getSession
	 * ().getServletContext().getRealPath("WEB-INF"+File.separator
	 * +"classes"+File
	 * .separator+"conf"+File.separator+"template"+File.separator);
	 * if(path==null) path =
	 * request.getSession().getServletContext().getRealPath
	 * ("/"+File.separator+".."
	 * +File.separator+".."+File.separator+".."+File.separator
	 * +"src"+File.separator
	 * +"main"+File.separator+"resources"+File.separator+"conf"
	 * +File.separator+"template"); String fileName="holiday_template.xls"; File
	 * file=new File(path+File.separator+fileName); FileInputStream fis=null;
	 * OutputStream out=null; //1.把下拉框数据写进模板 try { fis = new
	 * FileInputStream(file); HSSFWorkbook workbook=new HSSFWorkbook(fis);
	 * HSSFSheet sheet = workbook.getSheetAt(0); //第3行0列到300行0列下拉
	 * CellRangeAddressList regions = new CellRangeAddressList(3, 300, 0, 0);
	 * HolidayType newHolidayType = new HolidayType(); List<HolidayType>
	 * holidayTypeList = holidayService.getHolidayTypeByParam(newHolidayType);
	 * String[] holidayTypeName = new String[holidayTypeList.size()]; for(int
	 * i=0;i<holidayTypeList.size();i++){ holidayTypeName[i] =
	 * holidayTypeList.get(i).getHoliday_name(); } DVConstraint constraint =
	 * DVConstraint.createExplicitListConstraint(holidayTypeName); //绑定下来范围和下拉数据
	 * HSSFDataValidation data_validation = new
	 * HSSFDataValidation(regions,constraint);
	 * sheet.addValidationData(data_validation); out = new
	 * FileOutputStream(file); workbook.write(out); out.close(); } catch
	 * (FileNotFoundException e1) { e1.printStackTrace(); } catch (IOException
	 * e) { e.printStackTrace(); }
	 * 
	 * //2.把模板下载下来 try { fis=new FileInputStream(file);
	 * out=response.getOutputStream(); byte[] buffer=new byte[1024]; while
	 * (fis.read(buffer)!=-1) { out.write(buffer); } out.flush(); } catch
	 * (Exception e) { e.printStackTrace(); }finally{ if (null!=fis) { try {
	 * fis.close(); } catch (IOException e) { e.printStackTrace(); } } if
	 * (null!=out) { try { out.close(); } catch (IOException e) {
	 * e.printStackTrace(); } } } }
	 */

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-3-31 上午9:43:12
	 * @功能描述: 根据模板导入节假日
	 */
	public String uploadHolidayByTemplate() {
		HttpServletRequest request = getHttpServletRequest();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(uploadHolidayFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int totalRow = sheet.getLastRowNum();
			List<Holiday> list = new ArrayList<Holiday>();

			Cell titleCell = sheet.getRow(0).getCell(0);
			if ("节假日表".equals(titleCell.getStringCellValue().trim())) {

				if (totalRow < 6) {
					request.setAttribute("message", "上传失败，文件内容为空！");
					return "success";
				}

				for (int i = 6; i <= totalRow; i++) {
					HSSFRow row = sheet.getRow(i);
					Holiday holiday = new Holiday();

					String holidayName = "";// 节假日名称未解析
					String holiday_type_num = "";// 节假日名称已解析；类型代码
					String holiday_begin_date = "";// 开始时间
					String holiday_end_date = "";// 结束时间
					String holiday_scope = "";// 作用范围（全部|男|女）
					String type = "";// 类型：节假日|补班
					String description = "";// 描述
					String time_period = ""; // 时段
					String dept_num = "";// 作用部门

					// 节假日类型判断
					/*
					 * Cell holiday_name_cell= row.getCell(0); if (null !=
					 * holiday_name_cell) {
					 * holiday_name_cell.setCellType(Cell.CELL_TYPE_STRING);
					 * holidayName = holiday_name_cell.getStringCellValue(); if
					 * (null==holidayName||"".equals(holidayName)) {
					 * request.setAttribute("message",
					 * "导入失败，请检查第"+(i+1)+"行的“节假日名称”是否为空！"); return "success"; }
					 * else { // 根据名称查询节假日类型是否存在 HolidayType holidayType = new
					 * HolidayType(); holidayType.setHoliday_name(holidayName);
					 * holidayType.setValidity_flag("0"); List<HolidayType>
					 * typeList =
					 * holidayService.getHolidayTypeByName(holidayType);
					 * 
					 * if(typeList==null || typeList.size()==0){
					 * request.setAttribute("message",
					 * "导入失败，请检查第"+(i+1)+"行的“节假日名称”是否未定义!"); return "success"; }
					 * else { holiday_type_num =
					 * typeList.get(0).getHoliday_type_num(); } } } else {
					 * request.setAttribute("message", "导入失败，请检查的“节假日名称”是否为空！");
					 * return "success"; }
					 */

					// 类型判断
					Cell holiday_type = row.getCell(0);
					if (null != holiday_type) {
						holiday_type.setCellType(Cell.CELL_TYPE_STRING);
						type = holiday_type.getStringCellValue();
						if (null == type || "".equals(type)) {
							request.setAttribute("message", "导入失败，请检查第"
									+ (i + 1) + "行的“设置类型”是否为空！");
							return "success";
						}
					} else {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“设置类型”是否为空！");
						return "success";
					}

					// 开始时间判断
					Cell holiday_begin_date_cell = row.getCell(1);
					if (null != holiday_begin_date_cell) {
						// SimpleDateFormat dateformat = new
						// SimpleDateFormat("yyyy-MM-dd HH:mm");
						// Date dt =
						// HSSFDateUtil.getJavaDate(holiday_begin_date_cell.getNumericCellValue());//获取成DATE类型
						// holiday_begin_date =
						// dateformat.format(holiday_begin_date_cell.getStringCellValue());
						holiday_begin_date_cell
								.setCellType(Cell.CELL_TYPE_STRING);
						holiday_begin_date = holiday_begin_date_cell
								.getStringCellValue().trim() + " 00:00";
						if (null == holiday_begin_date
								|| "".equals(holiday_begin_date)) {
							request.setAttribute("message", "导入失败，请检查第"
									+ (i + 1) + "行的“开始时间”是否为空！");
							return "success";
						}
					} else {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“开始时间”是否为空！");
						return "success";
					}

					// 结束时间判断
					Cell holiday_end_date_cell = row.getCell(2);
					if (null != holiday_end_date_cell) {
						// SimpleDateFormat dateformat = new
						// SimpleDateFormat("yyyy-MM-dd HH:mm");
						// Date dt =
						// HSSFDateUtil.getJavaDate(holiday_end_date_cell.getNumericCellValue());//获取成DATE类型
						// holiday_end_date =
						// dateformat.format(holiday_end_date_cell.getStringCellValue());
						holiday_end_date_cell
								.setCellType(Cell.CELL_TYPE_STRING);
						holiday_end_date = holiday_end_date_cell
								.getStringCellValue().trim() + " 23:59";
						if (null == holiday_end_date
								|| "".equals(holiday_end_date)) {
							request.setAttribute("message", "导入失败，请检查第"
									+ (i + 1) + "行的“结束时间”是否为空！");
							return "success";
						}
					} else {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“结束时间”是否为空！");
						return "success";
					}

					// 时段判断
					Cell time_period_cell = row.getCell(3);
					if (null != time_period_cell) {
						time_period_cell.setCellType(Cell.CELL_TYPE_STRING);
						time_period = time_period_cell.getStringCellValue();
						if (null == time_period || "".equals(time_period)) {
							request.setAttribute("message", "导入失败，请检查第"
									+ (i + 1) + "行的“假日时段”是否为空！");
							return "success";
						} else {
							Cell type_cell = row.getCell(5);
							if ("补班".equals(type_cell.getStringCellValue())
									&& !"全天".equals(time_period)) {
								request.setAttribute("message",
										"导入失败，补班类型的节假日只能选择“全天”时段，请检查第"
												+ (i + 1) + "行的“假日时段”是否正确！");
								return "success";
							}
						}
					} else {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“假日时段”是否为空！");
						return "success";
					}

					// 作用范围判断
					Cell holiday_scope_cell = row.getCell(4);
					if (null != holiday_scope_cell) {
						holiday_scope_cell.setCellType(Cell.CELL_TYPE_STRING);
						holiday_scope = holiday_scope_cell.getStringCellValue();
						if (null == holiday_scope || "".equals(holiday_scope)) {
							request.setAttribute("message", "导入失败，请检查第"
									+ (i + 1) + "行的“作用范围”是否为空！");
							return "success";
						}
					} else {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“作用范围”是否为空！");
						return "success";
					}

					// 作用部门判断
					Cell dept_num_cell = row.getCell(5);
					if (null != dept_num_cell) {
						dept_num_cell.setCellType(Cell.CELL_TYPE_STRING);
						dept_num = dept_num_cell.getStringCellValue()
								.split(":")[1];
						if (null == dept_num || "".equals(dept_num)) {
							request.setAttribute("message", "导入失败，请检查第"
									+ (i + 1) + "行的“作用部门”是否为空！");
							return "success";
						}
					} else {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“作用部门”是否为空！");
						return "success";
					}

					// 描述判断
					Cell description_cell = row.getCell(6);
					if (null != description_cell) {
						description_cell.setCellType(Cell.CELL_TYPE_STRING);
						description = description_cell.getStringCellValue();
					}

					if (!holiday_begin_date
							.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")) {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“开始时间”格式是否正确！");
						return "success";
					}
					if (!holiday_end_date
							.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")) {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“结束时间”格式是否正确！");
						return "success";
					}
					if (holiday_begin_date.compareTo(holiday_end_date) > 0) {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“开始时间”不能大于“结束时间”！");
						return "success";
					}
					if (holidayService.getCrossHoliday(holiday_begin_date,
							holiday_end_date, null) > 0) {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的时间区间是否与已有节假日重叠！");
						return "success";
					}
					if (!holiday_scope
							.matches("^\u5168\u4F53\u5458\u5DE5|\u7537\u5458\u5DE5|\u5973\u5458\u5DE5$")) {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“作用范围”是否正确！");
						return "success";
					}
					if (!time_period
							.matches("^\u5168\u5929|\u4E0A\u5348|\u4E0B\u5348$")) {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“假日时段”是否正确！");
						return "success";
					}
					if (!type.matches("^\u8282\u5047\u65e5|\u8865\u73ED$")) {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“设置类型”是否正确！");
						return "success";
					}

					if (description.length() > 150) {
						request.setAttribute("message", "导入失败，请检查第" + (i + 1)
								+ "行的“描述”是否超过150个字符！");
						return "success";
					}
					User loginUser = (User) request.getSession().getAttribute(
							GlobalConstant.LOGIN_USER);

					if ("节假日".equals(type)) {
						holiday.setType("0");
					} else {
						holiday.setType("1");
					}

					if ("全天".equals(time_period)) {
						holiday.setTime_period("3");
					} else if ("上午".equals(time_period)) {
						holiday.setTime_period("1");
					} else if ("下午".equals(time_period)) {
						holiday.setTime_period("2");
					}

					holiday.setHoliday_type_num("1"); // 页面已隐藏节假日类型，插入默认节假日类型
					holiday.setHoliday_begin_date(holiday_begin_date + ":00");
					holiday.setHoliday_end_date(holiday_end_date + ":59");
					holiday.setDept_num("," + dept_num + ",");
					if ("男员工".equals(holiday_scope)) {
						holiday.setHoliday_scope("0");
					} else if ("女员工".equals(holiday_scope)) {
						holiday.setHoliday_scope("1");
					} else {
						holiday.setHoliday_scope("2");
					}
					holiday.setDescription(description);
					holiday.setCreate_user(loginUser.getYhMc());
					holiday.setHoliday_num(UUIDUtil.getUUID32());
					holiday.setValidity_flag("0");

					list.add(holiday);
				}

				holidayService.batchAddHoliday(list);
				request.setAttribute("message", "<font size='5px'>导入成功!</font>");

			} else {
				request.setAttribute("message", "导入失败！请检查导入模板或导入数据是否正确！");
				return "success";
			}
		} catch (Exception e) {
			request.setAttribute("message", "导入失败，请检查导入模板或导入数据是否正确！");
			return "success";
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";
	}

	/**
	 * 
	 * @创建人　: 高育漫
	 * @创建时间: 2016-3-31 上午9:28:15
	 * @功能描述: 导出查询条件下的节假日信息
	 * @参数描述:
	 */
	public void reportData() {

		// 包含查询条件的holidayBean
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Holiday holiday = GsonUtil.toBean(data, Holiday.class);

		// 获取查询结果下的holiday集合,将类型编号转为类型名称
		holiday.setValidity_flag("0");
		List<Holiday> holidays = holidayService.getHolidayByParam(holiday);

		for (int i = 0; i < holidays.size(); i++) {
			if ("2".equals(holidays.get(i).getHoliday_scope())) {
				holidays.get(i).setHoliday_scope("全天员工");
			} else if ("1".equals(holidays.get(i).getHoliday_scope())) {
				holidays.get(i).setHoliday_scope("女员工");
			} else if ("0".equals(holidays.get(i).getHoliday_scope())) {
				holidays.get(i).setHoliday_scope("男员工");
			}

			if ("0".equals(holidays.get(i).getType())) {
				holidays.get(i).setType("节假日");
			} else if ("1".equals(holidays.get(i).getType())) {
				holidays.get(i).setType("正常上班");
			} else {
				holidays.get(i).setType("-");
			}

			if ("3".equals(holidays.get(i).getTime_period())) {
				holidays.get(i).setTime_period("全天");
			} else if ("1".equals(holidays.get(i).getTime_period())) {
				holidays.get(i).setTime_period("上午");
			} else if ("2".equals(holidays.get(i).getTime_period())) {
				holidays.get(i).setTime_period("下午");
			} else {
				holidays.get(i).setTime_period("-");
			}

			HolidayType holidayType = new HolidayType();
			holidayType.setHoliday_type_num(holidays.get(i)
					.getHoliday_type_num());

			String holidayTypeName = holidayService
					.getHolidayTypeByParam(holidayType).get(0)
					.getHoliday_name();
			holidays.get(i).setHoliday_type_num(holidayTypeName);
			holidays.get(i).setHoliday_begin_date(
					holidays.get(i).getHoliday_begin_date().substring(0, 10));
			holidays.get(i).setHoliday_end_date(
					holidays.get(i).getHoliday_end_date().substring(0, 10));
		}
		String jsonList = GsonUtil.toJson(holidays);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);

		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		// colTitleList.add("节假日类型名称");
		colTitleList.add("设置类型");
		colTitleList.add("开始时间");
		colTitleList.add("结束时间");
		colTitleList.add("假日时段");
		colTitleList.add("作用范围"); // 农行系统去掉该项
		colTitleList.add("描述");
		colTitleList.add("创建人");
		colTitleList.add("创建时间");

		// colList.add("holiday_type_num");
		colList.add("type");
		colList.add("holiday_begin_date");
		colList.add("holiday_end_date");
		colList.add("time_period");
		colList.add("holiday_scope");
		colList.add("description");
		colList.add("create_user");
		colList.add("create_time");

		String[] colTitles = new String[colList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}

		// 导出到Excel
		try {
			OutputStream out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "节假日信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("节假日信息表",
					colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据ID查询节假日
	 * 
	 * @author yuman.gao
	 */
	public void getHolidayById() {
		String holiday_id = getHttpServletRequest().getParameter("holiday_id");
		Holiday holiday = holidayService.getHolidayById(holiday_id);
		holiday.setHoliday_begin_date(holiday.getHoliday_begin_date()
				.split(" ")[0]);
		holiday.setHoliday_end_date(holiday.getHoliday_end_date().split(" ")[0]);
		StringBuffer dept_num = new StringBuffer();
		for (String deptNumStr : holiday.getDept_num().split(",")) {
			dept_num.append("'" + deptNumStr + "',");
		}
		if (dept_num.length() != 0) {
			dept_num.setLength(dept_num.length() - 1);
		}
		holiday.setDept_num(dept_num.toString());
		printHttpServletResponse(GsonUtil.toJson(holiday));
	}

	/**
	 * 查询是否有交叉的节假日
	 * 
	 * @author yuman.gao
	 */
	public void getCrossHoliday() {
		String holiday_begin_date = getHttpServletRequest().getParameter(
				"holiday_begin_date")
				+ " 00:00:00";
		String holiday_end_date = getHttpServletRequest().getParameter(
				"holiday_end_date")
				+ " 23:59:59";
		String holiday_id = getHttpServletRequest().getParameter("holiday_id");
		int count = holidayService.getCrossHoliday(holiday_begin_date,
				holiday_end_date, holiday_id);

		printHttpServletResponse(GsonUtil.toJson(count));
	}
}
