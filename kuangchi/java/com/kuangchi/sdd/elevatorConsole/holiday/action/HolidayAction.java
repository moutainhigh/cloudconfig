package com.kuangchi.sdd.elevatorConsole.holiday.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.elevatorConsole.device.service.ITKDeviceService;
import com.kuangchi.sdd.elevatorConsole.holiday.model.Holiday;
import com.kuangchi.sdd.elevatorConsole.holiday.service.HolidayService;
import com.kuangchi.sdd.elevatorConsole.holiday.util.ExcelExportTemplate;
import com.kuangchi.sdd.elevatorConsole.holiday.util.HolidayUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * 节假日模块Action
 * 
 * @author yuman.gao
 */
@Controller("elevatorHolidayAction")
public class HolidayAction extends BaseActionSupport implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(HolidayAction.class);

	private File uploadHolidayFile;

	public File getUploadHolidayFile() {
		return uploadHolidayFile;
	}

	public void setUploadHolidayFile(File uploadHolidayFile) {
		this.uploadHolidayFile = uploadHolidayFile;
	}

	@Resource(name = "elevatorHolidayServiceImpl")
	transient private HolidayService holidayService;

	@Resource(name = "tkDeviceService")
	private ITKDeviceService tkDeviceService;

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 根据参数查询节假日（分页）
	 * 
	 * @author yuman.gao
	 */
	public void getHolidayByParamPage() {
		JsonResult result = new JsonResult();
		String page = getHttpServletRequest().getParameter("page");
		String rows = getHttpServletRequest().getParameter("rows");
		String beanObject = getHttpServletRequest().getParameter("data");
		if (EmptyUtil.isEmpty(beanObject)) {
			result.setMsg("请输入参数");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
		Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
		map.put("page", page == null ? 1 : (Integer.valueOf(page) - 1)
				* (rows == null ? 10 : Integer.valueOf(rows)));
		map.put("rows", rows == null ? 10 : Integer.valueOf(rows));

		List<Holiday> holidayList = holidayService.getHolidayByParamPage(map);
		int holidayCount = holidayService.getHolidayByParamCount(map);
		Grid<Holiday> grid = new Grid<Holiday>();
		grid.setTotal(holidayCount);
		grid.setRows(holidayList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * 修改节假日
	 * 
	 * @author xuewen.deng
	 */
	public void modifyHoliday() {
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);

		String data = request.getParameter("data");
		String device_num = request.getParameter("device_num");
		String holiday_d = request.getParameter("holiday_d");
		Map<String, Object> holiday = GsonUtil.toBean(data, HashMap.class);

		try {
			if (!(holiday_d.equals(holiday.get("holiday_date").toString()))) {
				/*
				 * if (holidayService.isSend(holiday_d)) {
				 * result.setMsg("修改失败，已下发的节假日不能修改日期");
				 * result.setSuccess(false); } else { if
				 * (holidayService.isExistHoli(device_num,
				 * holiday.get("holiday_date").toString())) {
				 * result.setMsg("修改失败，节假日日期已存在"); result.setSuccess(false); }
				 * else { boolean modifyResult = holidayService.modifyHoliday(
				 * holiday, loginUser.getYhMc()); if (modifyResult) {
				 * result.setMsg("修改成功"); result.setSuccess(true); } else {
				 * result.setMsg("修改失败"); result.setSuccess(true); } } }
				 */

				if (holidayService.isExistHoli(device_num,
						holiday.get("holiday_date").toString())) {
					result.setMsg("修改失败，节假日日期已存在");
					result.setSuccess(false);
				} else {
					// 先重新下发，成功后再修改数据库
					List<com.kuangchi.sdd.elevatorConsole.device.model.Holiday> holidayList = new ArrayList<com.kuangchi.sdd.elevatorConsole.device.model.Holiday>();
					com.kuangchi.sdd.elevatorConsole.device.model.Holiday holid = new com.kuangchi.sdd.elevatorConsole.device.model.Holiday();
					holid.setDevice_num(device_num);
					holid.setHoliday_date(holiday.get("holiday_date")
							.toString());

					holidayList.add(holid);
					if (tkDeviceService.issuedHoliForModify(device_num,
							holiday_d, holidayList, loginUser.getYhMc())) {
						boolean modifyResult = holidayService.modifyHoliday(
								holiday, loginUser.getYhMc());
						if (modifyResult) {
							result.setMsg("修改成功");
							result.setSuccess(true);
						} else {
							result.setMsg("修改失败");
							result.setSuccess(false);
						}
					} else {
						result.setMsg("修改失败");
						result.setSuccess(false);
					}
				}
			} else {
				boolean modifyResult = holidayService.modifyHoliday(holiday,
						loginUser.getYhMc());
				if (modifyResult) {
					result.setMsg("修改成功");
					result.setSuccess(true);
				} else {
					result.setMsg("修改失败");
					result.setSuccess(false);
				}
			}
		} catch (Exception e) {
			result.setMsg("操作失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 删除节假日信息
	 * 
	 * @author xuewen.deng
	 */
	public void deleteHoliday() {

		JsonResult result = new JsonResult();

		HttpServletRequest request = getHttpServletRequest();
		String delete_ids = request.getParameter("delete_ids");
		String device_num = request.getParameter("device_num");
		// String sendFlag = request.getParameter("sendFlag");
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		/*
		 * if ("1".equals(sendFlag)) {// 如果是未下发的，直接删数据库 if
		 * (holidayService.deleteHolidayById(delete_ids, create_user)) {
		 * result.setMsg("删除成功"); result.setSuccess(true); } else {
		 * result.setMsg("删除失败"); result.setSuccess(false); }
		 * 
		 * } else {// 如果有已经下发的，除了删数据库还要重新下权限 if
		 * (holidayService.deleteHolidayById2(device_num, delete_ids,
		 * create_user)) { result.setMsg("删除成功"); result.setSuccess(true); }
		 * else { result.setMsg("删除失败"); result.setSuccess(false); } }
		 */
		// 除了删数据库还要重新下权限
		if (holidayService.deleteHolidayById2(device_num, delete_ids,
				create_user)) {
			result.setMsg("删除成功");
			result.setSuccess(true);
		} else {
			result.setMsg("删除失败");
			result.setSuccess(false);
		}

		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 新增节假日信息
	 * 
	 * @author xuewen.deng
	 */
	public void addHoliday() {
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		String device_num = request.getParameter("device_num");
		if (EmptyUtil.isEmpty(beanObject)) {
			result.setMsg("带*号为必填项");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
		map.put("holiday_num", UUID.randomUUID().toString());
		try {
			if (holidayService.isExistHoli(device_num, map.get("holiday_date")
					.toString())) {
				result.setMsg("新增失败,节假日日期已存在");
				result.setSuccess(false);
			} else {
				List<com.kuangchi.sdd.elevatorConsole.device.model.Holiday> holidayList = new ArrayList<com.kuangchi.sdd.elevatorConsole.device.model.Holiday>();
				com.kuangchi.sdd.elevatorConsole.device.model.Holiday holid = new com.kuangchi.sdd.elevatorConsole.device.model.Holiday();
				holid.setDevice_num(device_num);
				holid.setHoliday_date(map.get("holiday_date").toString());
				holid.setDescription(map.get("description") == null ? "" : map
						.get("description").toString());
				holid.setHoliday_num(map.get("holiday_num").toString());
				holidayList.add(holid);
				if (tkDeviceService.issuedHoliday(device_num, holidayList,
						loginUser.getYhMc())) {
					boolean addResult = holidayService.addHoliday(map,
							loginUser.getYhMc());
					if (addResult) {
						result.setMsg("新增成功");
						result.setSuccess(true);
					} else {
						result.setMsg("新增失败");
						result.setSuccess(false);
					}
				} else {
					result.setMsg("新增失败");
					result.setSuccess(false);
				}
			}
		} catch (Exception e) {
			result.setMsg("操作失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 根据编号查询节假日
	 * 
	 * @author yuman.gao
	 */
	public void getHolidayById() {
		String id = getHttpServletRequest().getParameter("id");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Holiday holiday = holidayService.getHolidayByNum(map);
		printHttpServletResponse(GsonUtil.toJson(holiday));
	}

	/**
	 * 下载批量导入模板
	 * 
	 * @author yuman.gao
	 */
	public void downloadHolidayTemplate() {
		HttpServletResponse response = getHttpServletResponse();
		OutputStream out = null;
		ExcelExportTemplate excelExport = new ExcelExportTemplate();
		List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();

		TitleRowCell t1t = new TitleRowCell("节假日日期", true);
		TitleRowCell t2t = new TitleRowCell("描述", false);

		titleRowCells.add(t1t);
		titleRowCells.add(t2t);
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

	/**
	 * @创建人　: xuewen.deng
	 * @创建时间: 2016-10-24
	 * @功能描述: 根据模板导入节假日
	 */
	public String uploadHolidayByTemplate() {

		HttpServletRequest request = getHttpServletRequest();
		String device_numStr = request.getParameter("device_num");
		String[] deviceArr = device_numStr.split(",");

		int isExistSucc = 0;
		int isExistFail = 0;

		FileInputStream fis = null;
		try {
			if(uploadHolidayFile==null){
				request.setAttribute("message", "请选择需要上传文件！");
				return "success";
			}
			fis = new FileInputStream(uploadHolidayFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int totalRow = sheet.getLastRowNum();
			List<Map> list = new ArrayList<Map>();

			if (totalRow < 5) {
				request.setAttribute("message", "上传失败，文件内容为空！");
				return "success";
			}

			for (String device_num : deviceArr) {
				List<String> holiList = holidayService
						.getHoliByDevice2(device_num);
				for (int i = 5; i <= totalRow; i++) {

					String holiday_date = "";// 假期时间
					String description = "";// 描述

					HSSFRow row = sheet.getRow(i);
					Cell holiday_date_cell = row.getCell(0);
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");
					Date dDate = new Date();
					String dStr = dateformat.format(dDate);

					if (null != holiday_date_cell) {
						holiday_date = holiday_date_cell.getStringCellValue();
						if (null == holiday_date || "".equals(holiday_date)) {
							request.setAttribute("message", "导入失败，表格第"
									+ (i + 1) + "行的节假日日期不能为空。");
							return "success";

						} else if (dStr.compareTo(holiday_date) > 0) {
							request.setAttribute("message", "导入失败，表格第"
									+ (i + 1) + "行的节假日日期不能小于当前系统时间。");
							return "success";
						} else if (!holiday_date
								.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")) {
							request.setAttribute("message", "导入失败，表格第"
									+ (i + 1) + "行的节假日日期格式不正确。");
							return "success";
						} else if (holidayService.isExistHoli(device_num,
								holiday_date)
								|| HolidayUtil.isExistHoliInList(holiList,
										holiday_date) != 0) {
							request.setAttribute("message", "导入失败，表格第"
									+ (i + 1) + "行的节假日日期已存在。");
							return "success";
						}
					} else {
						request.setAttribute("message", "导入失败，表格第" + (i + 1)
								+ "行的节假日日期不能为空。");
						return "success";
					}

					Cell description_cell = row.getCell(1);
					if (null != description_cell) {
						description_cell.setCellType(Cell.CELL_TYPE_STRING);
						description = description_cell.getStringCellValue();
						if (description.length() > 150) {
							request.setAttribute("message", "描述不能超过150个字符。");
							return "success";
						}
					}

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("device_num", device_num);
					map.put("holiday_num", UUIDUtil.getUUID32());
					map.put("holiday_date", holiday_date);
					holiList.add(holiday_date);
					map.put("description", description == null ? ""
							: description);
					list.add(map);
				}

				if (tkDeviceService.commHoliday(device_num, holiList)) {// 调用接口往设备插节假日数据
					// 成功后往数据库插节假日数据
					holidayService.batchAddHoliday(list);
					isExistSucc = 1;
				} else {
					isExistFail = 1;
				}

			}
			if (isExistSucc == 1 && isExistFail == 0) {
				request.setAttribute("message", "全部导入成功。");
			} else if (isExistSucc == 0 && isExistFail == 1) {
				request.setAttribute("message", "导入失败。");
			} else if (isExistSucc == 1 && isExistFail == 1) {
				request.setAttribute("message", "部分设备导入失败。");
			} else {
				request.setAttribute("message", "导入过程出现异常。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "导入失败！");
			return "success";
		} finally {
			if (null != fis) {
				try {
					fis.close();
					uploadHolidayFile=null;
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
	/*
	 * public void reportData() {
	 * 
	 * // 包含查询条件的holidayBean HttpServletResponse response =
	 * getHttpServletResponse(); HttpServletRequest request =
	 * getHttpServletRequest(); String data = request.getParameter("data");
	 * Holiday holiday = GsonUtil.toBean(data, Holiday.class);
	 * 
	 * // 获取查询结果下的holiday集合,将类型编号转为类型名称 holiday.setValidity_flag("0");
	 * List<Holiday> holidays = holidayService.getHolidayByParam(holiday);
	 * 
	 * for (int i = 0; i < holidays.size(); i++) { HolidayType holidayType = new
	 * HolidayType();
	 * holidayType.setHoliday_type_num(holidays.get(i).getHoliday_type_num());
	 * 
	 * String holidayTypeName =
	 * holidayService.getHolidayTypeByParam(holidayType).get(0)
	 * .getHoliday_name(); holidays.get(i).setHoliday_type_num(holidayTypeName);
	 * 
	 * } String jsonList = GsonUtil.toJson(holidays); List list =
	 * GsonUtil.getListFromJson(jsonList, ArrayList.class);
	 * 
	 * // 设置列表头和列数据键 List<String> colList = new ArrayList<String>();
	 * List<String> colTitleList = new ArrayList<String>();
	 * colTitleList.add("节假日类型"); colTitleList.add("假期开始时间");
	 * colTitleList.add("假期结束时间"); colTitleList.add("作用范围");
	 * colTitleList.add("描述"); colTitleList.add("创建人");
	 * colTitleList.add("创建时间");
	 * 
	 * colList.add("holiday_type_num"); colList.add("holiday_begin_date");
	 * colList.add("holiday_end_date"); colList.add("holiday_scope");
	 * colList.add("description"); colList.add("create_user");
	 * colList.add("create_time");
	 * 
	 * String[] colTitles = new String[colList.size()]; String[] cols = new
	 * String[colList.size()]; for (int i = 0; i < colList.size(); i++) {
	 * cols[i] = colList.get(i); colTitles[i] = colTitleList.get(i); }
	 * 
	 * // 导出到Excel try { OutputStream out = response.getOutputStream();
	 * response.setContentType("application/x-msexcel"); String
	 * fileName="节假日信息表.xls"; response.setHeader("content-Disposition",
	 * "attachment;filename="+DownloadFile.toUtf8String(fileName)); Workbook
	 * workbook = ExcelUtilSpecial.exportExcel("节假日信息表",colTitles, cols, list);
	 * workbook.write(out); out.flush(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
}
