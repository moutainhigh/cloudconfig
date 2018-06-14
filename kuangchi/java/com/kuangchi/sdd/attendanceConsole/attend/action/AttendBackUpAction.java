package com.kuangchi.sdd.attendanceConsole.attend.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.attend.model.AttendModel;
import com.kuangchi.sdd.attendanceConsole.attend.service.IAttendHandleService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.baseConsole.holiday.util.ExcelUtilSpecial;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-5-25 上午10:32:05
 * @功能描述: 考勤每月备份Action
 */
@Controller("attendBackUpAction")
public class AttendBackUpAction extends BaseActionSupport{	
	public Object getModel() {
		return null;
	}
	
	@Resource(name = "attendHandleServiceImpl")
	private IAttendHandleService attendanceServiceByBu;
	
	//备份文件绝对路径	
	@Value("${backupPath}")
	private String absolutePath;

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-5-25 上午10:32:05
	 * @功能描述: 指定月份考勤数据条目
	 */
	public void queryMonthAttend(){
		String upYear = getHttpServletRequest().getParameter("upYear");
		String upMonth = getHttpServletRequest().getParameter("upMonth");
		String fromTime = upYear+"-"+upMonth+"-00 00:00:00";
		String toTime = upYear+"-"+upMonth+"-32 23:59:59";
		AttendModel attendModel = new AttendModel();
		attendModel.setFrom_time(fromTime);
		attendModel.setTo_time(toTime);		
		int attendCount = attendanceServiceByBu.getAllAttendCount(attendModel);
		printHttpServletResponse(GsonUtil.toJson(attendCount));
	}		
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-5-25 上午10:32:05
	 * @功能描述: 考勤每月备份
	 */
	public void attendBackUp(){
		String upYear = getHttpServletRequest().getParameter("upYear");
		String upMonth = getHttpServletRequest().getParameter("upMonth");
		String fromTime = upYear+"-"+upMonth+"-00 00:00:00";
		String toTime = upYear+"-"+upMonth+"-32 23:59:59";
		AttendModel attendModel = new AttendModel();
		int rows = attendanceServiceByBu.getAllAttendCount(attendModel);
		attendModel.setFrom_time(fromTime);
		attendModel.setTo_time(toTime);
		attendModel.setPage(1);
		attendModel.setRows(rows);
		List<AttendModel> allAttendList = attendanceServiceByBu.getAllAttend(attendModel);
		
		String jsonList = GsonUtil.toJson(allAttendList);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("员工编号");
		colTitleList.add("员工名称");
		colTitleList.add("打卡时间");
		colTitleList.add("刷卡设备ID");
		colTitleList.add("刷卡设备名称");
		colTitleList.add("是否补打卡(0 否 1 是)");
		
		colList.add("staff_num");
		colList.add("staff_name");
		colList.add("checktime");
		colList.add("device_id");
		colList.add("device_name");
		colList.add("flag_status");
		
		String[] colTitles = new String[colList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		
		// 导出到Excel
		try {
			String fileName="月员工考勤备份";
			HttpServletRequest request = getHttpServletRequest();
			File file = new File(absolutePath+fileName+"-"+upYear+"-"+upMonth+".xls");
			FileOutputStream fos = new FileOutputStream(file);
			Workbook workbook = ExcelUtilSpecial.exportExcel(upYear+"年"+upMonth+"月员工考勤备份表",colTitles, cols, list);
			workbook.write(fos);
			fos.flush();
			fos.close();
			
			//备份后删除月记录
			attendanceServiceByBu.deleteAttendInfoByMonth(allAttendList);
			String result = "<font color='red'>"+upYear+"年"+upMonth+"月"+"</font>"+"的数据已经备份，且数据已经从数据库删除";
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
		}
	}
}
