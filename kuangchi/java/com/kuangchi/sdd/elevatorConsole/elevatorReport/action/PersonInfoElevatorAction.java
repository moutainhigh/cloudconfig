package com.kuangchi.sdd.elevatorConsole.elevatorReport.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoElevator;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.service.PersonInfoElevatorService;
import com.kuangchi.sdd.elevatorConsole.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
@Controller("personInfoElevatorAction")
public class PersonInfoElevatorAction extends BaseActionSupport{

	@Resource(name="personInfoElevatorServiceImpl")
	private PersonInfoElevatorService personInfoElevatorServer;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取梯控人员名单-Action
	 */
	public void getPersonInfoElevator(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<PersonInfoElevator> grid = new Grid<PersonInfoElevator>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		grid = personInfoElevatorServer.getPersonInfoElevator(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27 
	 * @功能描述: 导出梯控人员名单-Action
	 */
		public void exportPersonInfoElevator() {
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("exportData");
			Map map = GsonUtil.toBean(beanObject, HashMap.class);
			List<PersonInfoElevator> personInfoElevator=personInfoElevatorServer.exportPersonInfoElevator(map);
			String jsonList = GsonUtil.toJson(personInfoElevator);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						colTitleList.add("设备名称");
						colTitleList.add("设备IP");
						colTitleList.add("设备Mac地址");
						colTitleList.add("员工工号");
						colTitleList.add("员工名称");
						colTitleList.add("员工卡号");
						colTitleList.add("开始生效时间");
						colTitleList.add("结束生效时间");
						
						colList.add("device_name");
						colList.add("device_ip");
						colList.add("mac");
						colList.add("staff_no");
						colList.add("staff_name");
						colList.add("card_num");
						colList.add("begin_valid_time");
						colList.add("end_valid_time");
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
							String fileName="人员梯控权限表.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecialCount.exportExcel("人员梯控权限表", colTitles, cols, list);
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
	
	
	
}
