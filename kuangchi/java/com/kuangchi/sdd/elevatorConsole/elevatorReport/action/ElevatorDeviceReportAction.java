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
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorDeviceInfo;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorRecordInfo;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.service.ElevatorDeviceReportService;
import com.kuangchi.sdd.elevatorConsole.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
@Controller("elevatorDeviceReportAction")
public class ElevatorDeviceReportAction extends BaseActionSupport{

	@Resource(name="elevatorDeviceReportServiceImpl")
	private ElevatorDeviceReportService elevatorDeviceReportService;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取梯控控制设备信息-Action
	 */
	public void getElevatorDeviceinfo(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<Map<String, Object>> grid = new Grid<Map<String, Object>>();
		String beanObject = request.getParameter("data");
		Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		grid = elevatorDeviceReportService.getElevatorDeviceinfo(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27 
	 * @功能描述: 导出梯控控制设备信息-Action
	 */
		public void exportElevatorDeviceinfo() {
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("exportData");
			Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
			List<Map<String, Object>> elevatorDeviceInfo=elevatorDeviceReportService.exportElevatorDeviceinfo(map);
			for(Map<String, Object> m : elevatorDeviceInfo){
				if(null!=m.get("online_state") && "0".equals(m.get("online_state").toString())){
					m.put("online_state", "离线");
				}else if(null!=m.get("online_state") && "1".equals(m.get("online_state").toString())){
					m.put("online_state", "在线");
				} 
				if(null!=m.get("device_port")){
					m.put("device_port", m.get("device_port").toString()+"");
				}
			}
			String jsonList = GsonUtil.toJson(elevatorDeviceInfo);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
			List<String> colList = new ArrayList<String>();
			List<String> colTitleList = new ArrayList<String>();
			
			colTitleList.add("设备名称");
			colTitleList.add("设备状态");
			colTitleList.add("MAC地址");
			colTitleList.add("通讯服务器IP地址");
			colTitleList.add("设备组");
			colTitleList.add("设备IP地址");
			colTitleList.add("设备端口号");
			colTitleList.add("子网掩码");
			colTitleList.add("网关");
			colTitleList.add("设备序列号");
			colTitleList.add("设备版本");
			colTitleList.add("创建时间");
			colTitleList.add("描述");
			
			colList.add("device_name");
			colList.add("online_state");
			colList.add("mac");
			colList.add("ip_address");
			colList.add("group_name");
			colList.add("device_ip");
			colList.add("device_port");
			colList.add("network_mask");
			colList.add("network_gateway");
			colList.add("device_sequence");
			colList.add("device_version");
			colList.add("create_time");
			colList.add("description");
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
				String fileName="电梯设备信息表.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				Workbook workbook = ExcelUtilSpecialCount.exportExcel("电梯设备信息表", colTitles, cols, list);
				workbook.write(out);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	
	
}
