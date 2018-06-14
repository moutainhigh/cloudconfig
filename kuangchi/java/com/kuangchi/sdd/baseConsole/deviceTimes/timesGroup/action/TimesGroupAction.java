package com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.action;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.service.DeviceTimesService;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.service.TimesGroupService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.sun.mail.iap.ConnectionException;

/**
 * 时段组Action
 * 
 * @author yuman.gao
 */
@Controller("deviceTimesGroupAction")
public class TimesGroupAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource(name = "deviceTimesGroupService")
	transient private TimesGroupService timesGroupService;

	@Resource(name = "DevicetimesServiceImpl")
	private DeviceTimesService devicetimesService;

	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-12-26 下午7:08:48
	 * @功能描述: 导出用户时段
	 * @参数描述:
	 */
	public void exportDeviceTimeGroup() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		List<TimesGroup> timesGroupList = timesGroupService
				.getTimesGroupByDevice1(device_num);
		HttpServletResponse response = getHttpServletResponse();
		OutputStream out = null;

		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("用户时段组表");
		// 设置 列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> cloTitleList = new ArrayList<String>();
		List<String> content = new ArrayList<String>();

		cloTitleList.add("查看内容");
		cloTitleList.add("时段一");
		cloTitleList.add("时段二");
		cloTitleList.add("时段三");
		cloTitleList.add("时段四");
		cloTitleList.add("时段五");
		cloTitleList.add("时段六");
		cloTitleList.add("时段七");
		cloTitleList.add("时段八");

		content.add("节假日");
		content.add("星期一");
		content.add("星期二");
		content.add("星期三");
		content.add("星期四");
		content.add("星期五");
		content.add("星期六");
		content.add("星期日");

		colList.add("");
		colList.add("times_one_num");
		colList.add("times_two_num");
		colList.add("times_three_num");
		colList.add("times_four_num");
		colList.add("times_five_num");
		colList.add("times_six_num");
		colList.add("times_seven_num");
		colList.add("times_eight_num");

		String[] colTitles = new String[cloTitleList.size()];
		String[] cols = new String[colList.size()];
		String[] contents = new String[content.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = cloTitleList.get(i);
		}
		for (int y = 0; y < content.size(); y++) {
			contents[y] = content.get(y);
		}
		for (int i = 0; i < colTitles.length; i++) {
			sheet.setColumnWidth(i, 5000);
		}
		int columnsNum = cols.length;

		// 大标题样式
		Row row0 = sheet.createRow(0);
		Cell cell00 = row0.createCell(0);
		cell00.setCellValue("用户时段组表");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnsNum - 1));
		Font titleFont = wb.createFont();
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setFontHeightInPoints((short) (24));
		CellStyle titleCellStyle = wb.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT
				.getIndex());
		titleCellStyle.setBorderBottom((short) 1);
		titleCellStyle.setBorderLeft((short) 1);
		titleCellStyle.setBorderRight((short) 1);
		titleCellStyle.setBorderTop((short) 1);
		cell00.setCellStyle(titleCellStyle);

		// 小标题样式
		Font columnTitleFont = wb.createFont();
		columnTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle columnTitleStyle = wb.createCellStyle();
		columnTitleStyle.setFont(columnTitleFont);
		columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		columnTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		columnTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		columnTitleStyle.setBorderBottom((short) 1);
		columnTitleStyle.setBorderLeft((short) 1);
		columnTitleStyle.setBorderRight((short) 1);
		columnTitleStyle.setBorderTop((short) 1);

		// 时段组名称样式
		Font deviceTimeFont = wb.createFont();
		deviceTimeFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle deviceTimeCellStyle = wb.createCellStyle();
		deviceTimeCellStyle.setFont(deviceTimeFont);
		deviceTimeCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		deviceTimeCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		deviceTimeCellStyle
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		deviceTimeCellStyle.setBorderBottom((short) 1);
		deviceTimeCellStyle.setBorderLeft((short) 1);
		deviceTimeCellStyle.setBorderRight((short) 1);
		deviceTimeCellStyle.setBorderTop((short) 1);

		// 主要内容样式
		CellStyle contentStyle = wb.createCellStyle();
		contentStyle.setBorderBottom((short) 1);
		contentStyle.setBorderLeft((short) 1);
		contentStyle.setBorderRight((short) 1);
		contentStyle.setBorderTop((short) 1);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);

		for (int j = 0; j < timesGroupList.size(); j++) {
			List<TimesGroup> timesGroupList2 = timesGroupService
					.getTimesGroupsByParam1(device_num, timesGroupList.get(j)
							.getGroup_name());
			DeviceTimes times = null;
			List<DeviceTimes> timesList = null;

			// 时段组名称栏
			int t1 = j * 10 + 1; // 时段组栏行数的下标
			int t2 = t1 + 1; // 小标题行数的下标

			Row row1 = sheet.createRow(t1);
			Cell cell1 = row1.createCell(0);
			cell1.setCellValue("时段组名称:"
					+ timesGroupList2.get(0).getGroup_name());// 设置时段组标题
			sheet.addMergedRegion(new CellRangeAddress(t1, t1, 0,
					columnsNum - 1));
			cell1.setCellStyle(deviceTimeCellStyle);

			Row row2 = sheet.createRow(t2);
			for (int i = 0; i < columnsNum; i++) {
				Cell cell = row2.createCell(i);
				cell.setCellValue(colTitles[i]);
				cell.setCellStyle(columnTitleStyle);// 设置小标题名称
			}

			for (TimesGroup timesGroup : timesGroupList2) {
				times = new DeviceTimes();
				Class<? extends TimesGroup> tg = timesGroup.getClass();
				Field[] fields = tg.getDeclaredFields();
				for (int i = 3; i <= 10; i++) {
					fields[i].setAccessible(true);
					try {
						if (fields[i].get(timesGroup) == null) {
							fields[i].set(timesGroup, "");
						} else {
							times.setTimes_num((String) fields[i]
									.get(timesGroup));
							times.setDevice_num(device_num);
							timesList = timesGroupService
									.getTimesByParamPageSortByBeginTime1(times);
							for (DeviceTimes time : timesList) {
								Class<? extends DeviceTimes> t = time
										.getClass();
								String tfBegin = "";
								String tfEnd = "";
								try {
									Field tfb = t
											.getDeclaredField("begin_time");
									Field tfe = t.getDeclaredField("end_time");
									tfb.setAccessible(true);
									tfe.setAccessible(true);
									tfBegin = (String) tfb.get(time);
									tfEnd = (String) tfe.get(time);
									tfb.set(time, tfBegin.substring(0, 2) + ":"
											+ tfBegin.substring(2, 4) + " - "
											+ tfEnd.substring(0, 2) + ":"
											+ tfEnd.substring(2, 4));
									fields[i].set(timesGroup,
											(String) tfb.get(time));
								} catch (NoSuchFieldException e) {
									e.printStackTrace();
								} catch (SecurityException e) {
									e.printStackTrace();
								}
							}
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}

			}

			for (int k = 0; k < timesGroupList2.size(); k++) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) GsonUtil
						.getListFromJson(GsonUtil.toJson(timesGroupList2),
								ArrayList.class);
				Row row = sheet.createRow(t2 + k + 1);
				for (int c = 0; c < columnsNum; c++) {
					Cell cell = row.createCell(c);
					cell.setCellStyle(contentStyle);
					if (c == 0) {
						cell.setCellValue(contents[k]);
					} else {
						Object d;
						d = list.get(k).get(cols[c]);
						if (d != null && !"00:00 - 00:00".equals(d)) {
							cell.setCellValue(d.toString());
						}
					}
				}
			}
		}

		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "用户时段信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			wb.write(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据参数查询时段组
	 * 
	 * @author yuman.gao
	 */
	public void getTimesGroupByParam() {
		// String group_id = getHttpServletRequest().getParameter("group_id");
		String group_name = getHttpServletRequest().getParameter("group_name");
		String group_num = getHttpServletRequest().getParameter("group_num");
		String device_num = getHttpServletRequest().getParameter("device_num");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("group_name", group_name);
		map.put("group_num", group_num);
		map.put("device_num", device_num);
		List<TimesGroup> timesGroupList = timesGroupService
				.getTimesGroupByParam(map);
		Integer timesGroupCount = timesGroupService.getTimesGroupCount(map);

		Grid<TimesGroup> grid = new Grid<TimesGroup>();
		grid.setTotal(timesGroupCount);
		grid.setRows(timesGroupList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * 查询所有时段
	 * 
	 * @author yuman.gao
	 */
	public void getTimes() {
		String device_num = getHttpServletRequest().getParameter("device_num");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("times_num", null);
		map.put("device_num", device_num);
		List<DeviceTimes> timesList = timesGroupService
				.getAllTimesSortByBeginTime(map);

		for (DeviceTimes time : timesList) {
			if ("0000".equals(time.getBegin_time())
					&& "0000".equals(time.getEnd_time())) {
				time.setBegin_time("None");
			} else {
				time.setBegin_time(time.getBegin_time().substring(0, 2) + ":"
						+ time.getBegin_time().substring(2, 4) + " - "
						+ time.getEnd_time().substring(0, 2) + ":"
						+ time.getEnd_time().substring(2, 4));
			}
		}
		printHttpServletResponse(GsonUtil.toJson(timesList));
	}

	/**
	 * 设置时段组
	 * 
	 * @author yuman.gao
	 */
	public void modifyTimesGroup() {
		JsonResult result = new JsonResult();
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String group_name = getHttpServletRequest().getParameter("groupName");
		List<TimesGroup> timesGroupList = new ArrayList<TimesGroup>();
		for (int i = 0; i < 8; i++) {
			String beanObject = getHttpServletRequest()
					.getParameter("data" + i);
			TimesGroup timesGroup = GsonUtil.toBean(beanObject,
					TimesGroup.class);
			timesGroup.setCreate_user(loginUser.getYhMc());
			timesGroup.setGroup_name(group_name);
			timesGroupList.add(timesGroup);
		}

		boolean modifyResult = timesGroupService.modifyTimesGroup(
				timesGroupList, loginUser.getYhMc());
		if (modifyResult) {
			result.setSuccess(true);
			result.setMsg("设置成功");
		} else {
			result.setSuccess(false);
			result.setMsg("设置失败");
		}

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 查看时段组（包含时段详细信息）
	 * 
	 * @author yuman.gao
	 */
	public void getTimesGroupView() {
		String group_num = getHttpServletRequest().getParameter("group_num");
		String device_num = getHttpServletRequest().getParameter("device_num");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_num", device_num);
		param.put("group_num", group_num);
		List<TimesGroup> timesGroupList = timesGroupService
				.getTimesGroupDetailByNum(param);

		/*
		 * List<DeviceTimes> timesList = null; for (TimesGroup timesGroup :
		 * timesGroupList) { Class<? extends TimesGroup> tg =
		 * timesGroup.getClass(); Field[] fields = tg.getDeclaredFields();
		 * for(int i=3;i<=10;i++){ fields[i].setAccessible(true); try {
		 * if(fields[i].get(timesGroup)==null){ fields[i].set(timesGroup, "");
		 * }else{ Map<String, Object> map = new HashMap<String, Object>();
		 * map.put("times_num", (String)fields[i].get(timesGroup));
		 * map.put("device_num", device_num); timesList =
		 * timesGroupService.getAllTimesSortByBeginTime(map); for (DeviceTimes
		 * time : timesList) { Class<? extends DeviceTimes> t = time.getClass();
		 * String tfBegin = ""; String tfEnd = ""; try { Field tfb =
		 * t.getDeclaredField("begin_time"); Field tfe =
		 * t.getDeclaredField("end_time"); tfb.setAccessible(true);
		 * tfe.setAccessible(true); tfBegin = (String)tfb.get(time); tfEnd =
		 * (String)tfe.get(time); tfb.set(time, tfBegin.substring(0,
		 * 2)+":"+tfBegin.substring(2, 4)+" - "+tfEnd.substring(0,
		 * 2)+":"+tfEnd.substring(2, 4)); fields[i].set(timesGroup,
		 * (String)tfb.get(time)); } catch (NoSuchFieldException e) {
		 * e.printStackTrace(); } catch (SecurityException e) {
		 * e.printStackTrace(); } } } } catch (IllegalArgumentException e) {
		 * e.printStackTrace(); } catch (IllegalAccessException e) {
		 * e.printStackTrace(); } } }
		 */
		printHttpServletResponse(GsonUtil.toJson(timesGroupList));
	}

	/**
	 * 根据编号查询时段组（只包含时段编号）
	 * 
	 * @author yuman.gao
	 */
	public void getDeviceTimesGroupByNum() {
		String group_num = getHttpServletRequest().getParameter("group_num");
		String device_num = getHttpServletRequest().getParameter("device_num");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_num", device_num);
		param.put("group_num", group_num);
		List<TimesGroup> timesGroupList = timesGroupService
				.getDeviceTimesGroupByNum(param);

		printHttpServletResponse(GsonUtil.toJson(timesGroupList));
	}

	/**
	 * 时段组下发
	 * 
	 * @author yuman.gao
	 */
	public void issuedTimesGroup() {
		JsonResult result = new JsonResult();
		try {
			String device_mac = getHttpServletRequest().getParameter(
					"device_mac");
			String device_type = getHttpServletRequest().getParameter(
					"device_type");
			String device_num = getHttpServletRequest().getParameter(
					"device_num");
			devicetimesService.issuedTime(device_num);
			boolean issuedResult = timesGroupService.issuedTimesGroup(
					device_num, device_mac, device_type);
			if (issuedResult) {
				result.setMsg("下发成功");
				result.setSuccess(true);
			} else {
				result.setMsg("下发失败");
				result.setSuccess(false);
			}

		} catch (ConnectionException e) {
			result.setMsg("连接异常");
			result.setSuccess(false);
		} catch (Exception e) {
			result.setMsg("下发失败，检查设备是否在线");
			result.setSuccess(false);
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * 读取已下发用户时段组信息
	 * 
	 * @author yuman.gao
	 */
	public void getUserGroup() {
		JsonResult result = new JsonResult();
		try {
			String device_mac = getHttpServletRequest().getParameter(
					"device_mac");
			String device_num = getHttpServletRequest().getParameter(
					"device_num");
			String group_num = getHttpServletRequest()
					.getParameter("group_num");
			List<TimesGroup> timesGroup = timesGroupService.getUserGroup(
					device_num, device_mac, group_num);
			printHttpServletResponse(GsonUtil.toJson(timesGroup));

		} catch (ConnectionException e) {
			result.setMsg("读取失败，检查设备是否在线");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
			result.setMsg("读取失败");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * 检查设备是否在线
	 * 
	 * @author yuman.gao
	 */
	public void isConnected() {
		JsonResult result = new JsonResult();
		try {
			String device_mac = getHttpServletRequest().getParameter(
					"device_mac");
			timesGroupService.isConnected(device_mac);
			result.setSuccess(true);
			printHttpServletResponse(GsonUtil.toJson(result));

		} catch (Exception e) {
			result.setMsg("读取失败，检查设备是否在线");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * 复制时段组
	 * 
	 * @author yuman.gao
	 */
	public void copyTimesGroup() {
		JsonResult result = new JsonResult();
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);

		String source_num = getHttpServletRequest().getParameter(
				"source_device_num");
		String[] target_nums = getHttpServletRequest().getParameter(
				"target_device_nums").split(",");

		boolean copyResult = false;

		for (String target_num : target_nums) {
			target_num = target_num.replace("'", "");
			copyResult = timesGroupService.copyTimes(source_num, target_num,
					loginUser.getYhMc());
			copyResult = timesGroupService.copyTimesGroup(source_num,
					target_num, loginUser.getYhMc());
		}
		if (copyResult) {
			result.setMsg("保存成功");
			result.setSuccess(true);
		} else {
			result.setMsg("保存失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 复制并下发时段组
	 * 
	 * @author yuman.gao
	 */
	public void copyIssuedTimesGroup() {
		JsonResult result = new JsonResult();

		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String source_num = getHttpServletRequest().getParameter(
				"source_device_num");
		String[] target_nums = getHttpServletRequest().getParameter(
				"target_device_nums").split(",");

		StringBuffer failDeviceNum = new StringBuffer();
		for (String target_num : target_nums) {
			target_num = target_num.replace("'", "");
			Map<String, Object> device = timesGroupService
					.getDeviceInfoByNum(target_num);
			if (device != null) {
				String target_mac = (String) device.get("device_mac");
				String target_type = (String) device.get("device_type");
				String target_name = (String) device.get("device_name");
				try {
					timesGroupService.copyIssuedTimesGroup(source_num,
							target_num, target_mac, target_type,
							loginUser.getYhMc());
				} catch (Exception e) {
					failDeviceNum.append(target_name + ",");
					e.printStackTrace();
				}
			}
		}
		if (failDeviceNum.length() != 0) {
			failDeviceNum.setLength(failDeviceNum.length() - 1);
			result.setMsg("以下设备无法连接，下发失败：" + failDeviceNum);
			result.setSuccess(false);
		} else {
			result.setMsg("保存并下发成功");
			result.setSuccess(true);
		}

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 根据设备查询组名称是否存在
	 * 
	 * @author yuman.gao
	 */
	public void getTimesGroupByName() {
		String group_name = getHttpServletRequest().getParameter("group_name");
		String device_num = getHttpServletRequest().getParameter("device_num");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_num", device_num);
		param.put("group_name", group_name);
		if (timesGroupService.getTimesGroupByName(param) > 0) {
			printHttpServletResponse(GsonUtil.toJson(1));
		} else {
			printHttpServletResponse(GsonUtil.toJson(0));
		}
	}
}
