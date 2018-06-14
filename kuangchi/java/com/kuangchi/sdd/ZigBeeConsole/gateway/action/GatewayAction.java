package com.kuangchi.sdd.ZigBeeConsole.gateway.action;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;
import com.kuangchi.sdd.ZigBeeConsole.gateway.service.IGatewayService;
import com.kuangchi.sdd.ZigBeeConsole.gateway.util.ExcelExportTemplate;
import com.kuangchi.sdd.ZigBeeConsole.quartz.service.impl.ZBAuthorityManagerQuartz;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.holiday.model.Holiday;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * 光子锁记录 - action
 * @author guibo.chen
 */
@Controller("gatewayAction")
public class GatewayAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "gatewayServiceImpl")
	private IGatewayService gatewayService;
	
	private File uploadGatewayFile;
    
	public File getUploadGatewayFile() {
		return uploadGatewayFile;
	}

	public void setUploadGatewayFile(File uploadGatewayFile) {
		this.uploadGatewayFile = uploadGatewayFile;
	}
	
	@Override
	public Object getModel() {
		return null;
	}

	//跳转到门信息主页面
		public String toMyGateway(){
			return "success";
		}

	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-19上午9:50:27
	 * @功能描述: 根据参数查询光子锁记录[分页]
	 * @参数描述:
	 */
	public void getGatewayByParamPage(){
		HttpServletRequest request = getHttpServletRequest();
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String data=request.getParameter("data");
		GatewayModel device=GsonUtil.toBean(data, GatewayModel.class);
		Grid grid=gatewayService.getGatewayInfo(device, page, rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-19 下午13:50:27
	 * @功能描述: 添加网关
	 * @参数描述:
	 */
	public void addGatewayInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String data=request.getParameter("data");
		GatewayModel device=GsonUtil.toBean(data, GatewayModel.class);
		JsonResult result = new JsonResult();
		List<GatewayModel> list=gatewayService.getGatewayByIpAndPort(device);
		if(list.size()==0){
			String obj=gatewayService.addGatewayinfo(device);
			if("true".equals(obj)){
				result.setMsg("添加成功");
	  			result.setSuccess(true);
			}else if("Panidfalse".equals(obj)){
				result.setMsg("该网关Pan_id已经存在，请重新输入");
	  			result.setSuccess(false);
			}else{
				result.setMsg("该网关当前连接异常");
	  			result.setSuccess(false);
				}
		}else{
			result.setMsg("该网关地址已经存在，请重新输入");
  			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-19 下午13:50:27
	 * @功能描述: 点击确定的时候添加网关
	 * @参数描述:
	 */
	public void NewaddGatewayInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String data=request.getParameter("data");
		GatewayModel device=GsonUtil.toBean(data, GatewayModel.class);
		JsonResult result = new JsonResult();
		
			Boolean obj=gatewayService.NewaddGatewayinfo(device);
			if(obj){
				result.setMsg("添加成功");
	  			result.setSuccess(true);
			}else{
				result.setMsg("添加失败");
	  			result.setSuccess(false);
				}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//获取网关芯片id
	public void getPanId(){
		HttpServletRequest request = getHttpServletRequest();
		String id=request.getParameter("id");
		List<GatewayModel> list=gatewayService.getGatewayById(id);
		JsonResult result = new JsonResult();
		if(list.size()!=0){
			for (GatewayModel device : list) {
				String url="http://"+device.getRemote_ip()+":"+
		    			device.getRemote_port()+"/comm/zigBee/getPanId.do?";
				String data="remoteIp="+device.getGateway()+"&port="+device.getGateway_port();
				String str = HttpRequest.sendPost(url,data);
				if (str != null && !"".equals(str) && !"null".equals(str)){
					Map devices=GsonUtil.toBean(str, HashMap.class);
					String zigBeeId=devices.get("zigBeeId").toString();
					String panId=String.valueOf(Integer.parseInt(devices.get("panId").toString(), 16));
					if(!"".equals(zigBeeId)){
						device.setZigbee_id(zigBeeId);
						gatewayService.getPanIdInfo(device);//更新网关芯片ID
						result.setMsg("网关芯片ID:   "+zigBeeId+"</br></br>"+"PanId值:          "+panId);
			  			result.setSuccess(true);
					}else{
						result.setMsg("查询失败，请查看设备连接是否异常");
			  			result.setSuccess(false);
					}
				}else{
					result.setMsg("查询失败，请查看设备连接是否异常");
		  			result.setSuccess(false);
				}
			}
		}else{
			result.setMsg("查询失败，请查看设备连接是否异常");
  			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-27 下午13:50:27
	 * @功能描述: 删除网关
	 * @参数描述:
	 */
	public void deleteGatewayInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String gateway_id=request.getParameter("gateway_id");
		JsonResult result = new JsonResult();
		List<GatewayModel> list=gatewayService.getGatewayByGatewayId(gateway_id);
		if(list.size()==0){
			Boolean obj=gatewayService.deleteGatewayInfo(gateway_id);
			if(obj){
				result.setMsg("删除成功");
	  			result.setSuccess(true);
			}else{
				result.setMsg("删除失败");
	  			result.setSuccess(false);
				}
		}else{
			result.setMsg("该网关地址已经关联设备，不能删除");
  			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	//跳到修改网关页面
	public String toMyEditGateway(){
		HttpServletRequest request = getHttpServletRequest();
		String gateway_id=request.getParameter("gateway_id");
		List<GatewayModel> list=gatewayService.getGatewayById(gateway_id);
		for (GatewayModel gatewayModel : list) {
			request.setAttribute("gateway", gatewayModel);
		}
		return "success";
	}
	
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-27下午13:50:27
	 * @功能描述: 修改网关
	 * @参数描述:
	 */
	public void editGatewayInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String data=request.getParameter("data");
		GatewayModel device=GsonUtil.toBean(data, GatewayModel.class);
		
		JsonResult result = new JsonResult();
		// 判断网关唯一性
		List<GatewayModel> existGateway = new ArrayList<GatewayModel>();
		if(!device.getOld_remote_ip().equals(device.getRemote_ip())||
				!device.getGateway().equals(device.getOld_gateway())){
			existGateway = gatewayService.getGatewayByIpAndPort(device);
		}
		if(existGateway.size() != 0){
			result.setMsg("该网关地址已经存在，请重新输入");
  			result.setSuccess(false);
		} else {
  			
			try {
				//判断新PAN_ID的唯一性
				List<GatewayModel> existPanid = gatewayService.getGatewaypanId(device.getGateway_pan_id());
				if(!device.getGateway_pan_id().equals(device.getOld_gateway_pan_id()) 
						&& existPanid.size()!=0){
					result.setMsg("该网关Pan_id已经存在，请重新输入");
		  			result.setSuccess(false);
		  			
				} else {
					String url = "http://"+device.getRemote_ip()+":"+
			    			device.getRemote_port()+"/comm/zigBee/getPanId.do?";
					String datas = "remoteIp="+device.getGateway()+"&port="+device.getGateway_port();
					String str = HttpRequest.sendPost(url,datas);
					
					if(str == null || "".equals(str) || "null".equals(str)){
						result.setMsg("该网关当前连接异常");
			  			result.setSuccess(false);
					} else {
			  			Map devices = GsonUtil.toBean(str, HashMap.class);
			  			device.setFirmwareVersion((String)devices.get("firmwareVersion"));
			  			device.setSoftwareVersion((String)devices.get("softwareVersion"));
			  			String zigBeeId = (String)devices.get("zigBeeId");
						// pan_id=String.valueOf(Integer.parseInt(devices.get("panId").toString(), 16));
						if(zigBeeId != null){
							 device.setZigbee_id(zigBeeId);
							 if(setPanId(device)){
								 device.setState("0"); 
							 } else {
								 device.setState("1");
							 }
						} else {
							device.setZigbee_id(null);
							device.setState("1");
						}
						gatewayService.updateGatewayInfo(device);
						result.setMsg("修改成功");
			  			result.setSuccess(true);
					}
				}
				
			} catch (Exception e) {
				result.setMsg("修改失败");
	  			result.setSuccess(false);
				e.printStackTrace();
			} finally {
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		}
		
	}
	
	public Boolean setPanId(GatewayModel device){
		
		String pan_id=Integer.toHexString(Integer.valueOf(device.getGateway_pan_id()));
 		String pasParam = "0000";
 		pan_id = pasParam.substring(0, 4-pan_id.length()) + pan_id;
		
 		String url="http://"+device.getRemote_ip()+":"+
 				device.getRemote_port()+"/comm/zigBee/setPanId.do?";
		String data="remoteIp="+device.getGateway()+"&port="+device.getGateway_port()+
				"&zigBeeId="+device.getZigbee_id()+"&panId="+pan_id;
		String str = HttpRequest.sendPost(url, data);//返回锁ID和状态位0x00或0x01
		if (str != null && !"".equals(str) && !"null".equals(str)){
			Map deviceMap = GsonUtil.toBean(str, HashMap.class);
			if(0.0==(Double)deviceMap.get("status")){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-27下午13:50:27
	 * @功能描述: 点击确定之后修改网关
	 * @参数描述:
	 */
	public void NeweditGatewayInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String data=request.getParameter("data");
		GatewayModel device=GsonUtil.toBean(data, GatewayModel.class);
		JsonResult result = new JsonResult();
		device.setZigbee_id(null);
		//device.setGateway_pan_id(null);
		device.setState("1");
		Boolean obj=gatewayService.updateGatewayInfo(device);
		if(obj){
			result.setMsg("修改成功");
  			result.setSuccess(true);
		}else{
			result.setMsg("修改失败");
  			result.setSuccess(false);
			}
	printHttpServletResponse(GsonUtil.toJson(result));
	
	}
	
	//设置网关panid
	public void gatewayEditPanid(){
		HttpServletRequest request = getHttpServletRequest();
		String data=request.getParameter("data");
		GatewayModel device=GsonUtil.toBean(data, GatewayModel.class);
		JsonResult result = new JsonResult();
		List<GatewayModel> panid=gatewayService.getGatewaypanId(device.getGateway_pan_id());
		if(panid.size()!=0){
			result.setMsg("该网关Pan_id已经存在,请重新输入");
  			result.setSuccess(false);
  			printHttpServletResponse(GsonUtil.toJson(result));
  			return;
		}
		Boolean obj=gatewayService.updateGatewayPanIdByDeviceId(device);
		if(obj){
			result.setMsg("设置成功");
  			result.setSuccess(true);
		} else {
			result.setMsg("设置失败，请检查网关是否离线");
  			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//下载导入模板
	public void gatewaydownLoad(){
		
		HttpServletResponse response = getHttpServletResponse();
		OutputStream out = null;
	    ExcelExportTemplate excelExport = new ExcelExportTemplate();
	    List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		   
		 
	    TitleRowCell t1t = new TitleRowCell("通讯服务器IP",true);
	    TitleRowCell t2t = new TitleRowCell("通讯服务器端口",true);
	    TitleRowCell t3t = new TitleRowCell("网关IP",true);
	    TitleRowCell t4t = new TitleRowCell("网关端口",true);
	    TitleRowCell t5t = new TitleRowCell("网关Pan_id",true);
	    TitleRowCell t6t = new TitleRowCell("描述",false);
	   
	    titleRowCells.add(t1t);
	    titleRowCells.add(t2t);
	    titleRowCells.add(t3t);
	    titleRowCells.add(t4t);
	    titleRowCells.add(t5t);
	    titleRowCells.add(t6t);
		  
		excelExport.createLongTitleRow(titleRowCells);
	    try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="网关信息模版.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			excelExport.getWorkbook().write(out);
	        out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//批量导入
	public String uploadGateways(){
		HttpServletRequest request = getHttpServletRequest();
	   	FileInputStream fis=null;
	   	try {
	 		fis = new FileInputStream(uploadGatewayFile);
	   		HSSFWorkbook workbook = new HSSFWorkbook(fis);
	   		HSSFSheet sheet = workbook.getSheetAt(0);
	   		int totalRow = sheet.getLastRowNum();
	        List<GatewayModel> list = new ArrayList<GatewayModel>();
		
	        Cell  titleCell=sheet.getRow(0).getCell(0);
	        if ("网关表".equals(titleCell.getStringCellValue().trim())){

	            if(totalRow<7){
	            	request.setAttribute("message", "上传失败，文件内容为空！");
	            	return "success";
	            }
	            
	        	for (int i = 7; i <=totalRow; i++) {
	        		HSSFRow row = sheet.getRow(i);
	        		GatewayModel gatewayModel = new GatewayModel();
	        		
	        		String remote_ip = "";
	        		String remote_port = "";
	        		String gateway = "";
	        		String gateway_port = "";
	        		String gateway_pan_id = "";
	        		String description = "";
	        		
	        		
	        		// 通讯服务器IP判断
	        		Cell remote_ip_cell = row.getCell(0);
	        		if (null != remote_ip_cell) {
	        			remote_ip_cell.setCellType(Cell.CELL_TYPE_STRING);
	        			remote_ip = remote_ip_cell.getStringCellValue();
	        			if (null == remote_ip||"".equals(remote_ip)) {
	        				request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“通讯服务器IP”是否为空！");
	        				return "success";
	        			}
	        		}else{
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“通讯服务器IP”是否为空！");
	        			return "success";
	        		}
	        		
	        		// 通讯服务器端口判断
	        		Cell remote_port_cell = row.getCell(1);
	        		if (null != remote_port_cell) {
	        			remote_port_cell.setCellType(Cell.CELL_TYPE_STRING);
	        			remote_port = remote_port_cell.getStringCellValue();
	        			if (null == remote_port||"".equals(remote_port)) {
	        				request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“通讯服务器端口”是否为空！");
	        				return "success";
	        			}
	        		}else{
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“通讯服务器端口”是否为空！");
	        			return "success";
	        		}
	        		
	        		// 网关IP判断
	        		Cell gateway_cell = row.getCell(2);
	        		if (null != gateway_cell) {
	        			gateway_cell.setCellType(Cell.CELL_TYPE_STRING);
	        			gateway = gateway_cell.getStringCellValue();
	        			if (null == gateway||"".equals(gateway)) {
	        				request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“网关IP”是否为空！");
	        				return "success";
	        			}
	        		}else{
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“网关IP”是否为空！");
	        			return "success";
	        		}
	        		
	        		// 网关端口判断
	        		Cell gateway_port_cell = row.getCell(3);
	        		if (null != gateway_port_cell) {
	        			gateway_port_cell.setCellType(Cell.CELL_TYPE_STRING);
	        			gateway_port = gateway_port_cell.getStringCellValue();
	        			if (null == gateway_port||"".equals(gateway_port)) {
	        				request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“网关端口”是否为空！");
	        				return "success";
	        			}
	        		}else{
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“网关端口”是否为空！");
	        			return "success";
	        		}
	        		
	        		// pan_id判断
	        		Cell gateway_pan_id_cell = row.getCell(4);
	        		if (null != gateway_pan_id_cell) {
	        			gateway_pan_id_cell.setCellType(Cell.CELL_TYPE_STRING);
	        			gateway_pan_id = gateway_pan_id_cell.getStringCellValue();
	        			if (null == gateway_pan_id||"".equals(gateway_pan_id)) {
	        				request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“pan_id”是否为空！");
	        				return "success";
	        			}
	        		}else{
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“pan_id”是否为空！");
	        			return "success";
	        		}
	        		
	        		
	        		Cell description_cell = row.getCell(5);
	        		if (null != description_cell) {
		        		description_cell.setCellType(Cell.CELL_TYPE_STRING);
		        		description = description_cell.getStringCellValue();
	        		}else{
	        			description="";
	        		}	
	        	
	        		
	        		if(!remote_ip.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")){
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“通讯服务器IP”格式是否正确！");
	        			return "success";
	        		}
	        		if(!remote_port.matches("^[0-9]*$")){
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“通讯服务器端口”格式是否正确！");
	        			return "success";
	        		}
	        		if(!gateway.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")){
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“网关IP”格式是否正确！");
	        			return "success";
	        		}
	        		if(!gateway_port.matches("^[0-9]*$")){
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“网关端口”格式是否正确！");
	        			return "success";
	        		}
	        		if((!gateway_pan_id.matches("^[0-9]*$")) ||  Integer.parseInt(gateway_pan_id) > 65535){
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“pan_id”格式是否正确！");
	        			return "success";
	        		}
	        		
	        		gatewayModel.setRemote_ip(remote_ip);
	        		gatewayModel.setRemote_port(remote_port);
	        		gatewayModel.setGateway(gateway);
	        		gatewayModel.setGateway_port(gateway_port);
	        		gatewayModel.setGateway_pan_id(gateway_pan_id);
	        		gatewayModel.setDescription(description);
	        		
	        		List<GatewayModel> existGateWay = gatewayService.getGatewayByIpAndPort(gatewayModel);
	        		if(existGateWay != null && existGateWay.size() !=0){
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的网关地址是否已存在！");
	        			return "success";
	        		}
		        	
	        		
	        		List existPanId = gatewayService.getGatewaypanId(gateway_pan_id);
	        		if(existPanId != null && existPanId.size() !=0){
	        			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“pan_id”是否已存在！");
	        			return "success";
	        		}
		        	
	        		
	        		
	        		list.add(gatewayModel);	
	        	}
	        	
        		for (GatewayModel gateway : list) {
        			String flag = gatewayService.addGatewayinfo(gateway);
        			if("false".equals(flag)){
        				gatewayService.NewaddGatewayinfo(gateway);
        			}
        		}
	        	
	        	request.setAttribute("message", "<font size='5px'>导入成功!</font>");
	        	
		    } else {
		        request.setAttribute("message", "导入失败！请检查导入模板或导入数据是否正确！");
		        return "success";
		    }
		 } catch (Exception e) {
			request.setAttribute("message", "导入失败，请检查导入模板或导入数据是否正确！");
			e.printStackTrace();
			return "success";
		 } finally {
			if (null!=fis) {
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
	 * 配置网关端口参数
	 */
	public void setPortParam(){
		
		boolean setResult = false;
		HttpServletRequest request = getHttpServletRequest();
		String commandPort = request.getParameter("commandPort");
		String reportPort = request.getParameter("reportPort");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sys_key", "commandPort");
		map.put("sys_value", commandPort);
		map.put("description", "光子锁网关命令端口");
		setResult = gatewayService.setPortParam(map);
		
		map.put("sys_key", "reportPort");
		map.put("sys_value", reportPort);
		map.put("description", "光子锁网关上报端口");
		setResult = gatewayService.setPortParam(map);
		
		
		JsonResult result = new JsonResult();
		if(setResult){
			result.setMsg("设置成功");
			result.setSuccess(true);
		} else {
			result.setMsg("设置失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	public void getParamValueByKey(){
		HttpServletRequest request = getHttpServletRequest();
		String keys = request.getParameter("key");
		Map map = new HashMap();
		for (String key : keys.split(",")) {
			String value = gatewayService.getParamValueByKey(key);
			map.put(key, value);
		}
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	
}
