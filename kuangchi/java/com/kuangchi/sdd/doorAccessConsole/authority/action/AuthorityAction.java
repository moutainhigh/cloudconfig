package com.kuangchi.sdd.doorAccessConsole.authority.action;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.model.CardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.SearchAuthorityBean;
import com.kuangchi.sdd.doorAccessConsole.authority.service.AthorityTaskService;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.doorAccessConsole.synchronizeFromComm.service.AllSynchronizeService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.file.DownloadFile;

@Scope("prototype")
@Controller("authorityAction")
public class AuthorityAction extends BaseActionSupport {

	@Override
	public Object getModel() {
		return null;
	}

	@Resource(name = "departmentServiceImpl")
    private IDepartmentService departmentService;
	  
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	@Resource(name = "athorityTaskServiceImpl")
	private AthorityTaskService athorityTaskService;
	
	
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;

	@Autowired
	DeviceService deviceService;
	
	@Autowired
	AllSynchronizeService allSynchronizeService;

	/* 组织权限查看 by huixian.pan*/
	public void searchDoorSysDeptAuth() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map  map = GsonUtil.toBean(data,
				HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		Grid grid=athorityTaskService.searchDoorSysDeptAuth(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/* 组织权限查看（下载用） by huixian.pan */
	public void downloadDoorSysDeptAuth() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data,HashMap.class);
		// 文件名获取
		HttpServletResponse response = getHttpServletResponse();
		List<Map> deptAuthoritylist=athorityTaskService.downloadDoorSysDeptAuth(map);
		
		for(Map map2:deptAuthoritylist){
			if(null == map2.get("groupName") || "".equals(map2.get("groupName").toString())){
				map2.put("groupName", "-");
			}
		}
		List list = GsonUtil.getListFromJson(GsonUtil.toJson(deptAuthoritylist), ArrayList.class);
		OutputStream out = null;
		
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
				colList.add("deptName");
				colTitleList.add("部门名称");
				colList.add("deviceName");
				colTitleList.add("设备名称");
				colList.add("deviceIp");
				colTitleList.add("设备Ip");
				colList.add("doorNum");
				colTitleList.add("门号");
				colList.add("doorName");
				colTitleList.add("门名称");
				colList.add("groupName");
				colTitleList.add("时段组");
				colList.add("startTime");
				colTitleList.add("有效时间起");
				colList.add("endTime");
				colTitleList.add("有效时间止");
				
		// 列数据键
		String[] cols = new String[colList.size()];
		// 列表题
		String[] colTitles = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="组织权限表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("组织权限表", colTitles,
					cols, list);
			workbook.write(out);
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 初始化权限列表
	public void initAuthorityList() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map  map = GsonUtil.toBean(data,
				HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		Grid grid=peopleAuthorityInfoService.searchDoorSysAuth(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	// 根据员工号初始化卡号下拉列表
	public void initCardSelect() {
		List<Map> list = new ArrayList<Map>();
		HttpServletRequest request = getHttpServletRequest();
		String staffNum = request.getParameter("staffNum");
		List<CardModel> cards = peopleAuthorityInfoService
				.getCardListByStaffNum(staffNum);
		for (CardModel card : cards) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("VALUE", card.getCardNum());
			map.put("TEXT", card.getCardName());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));

	}

	// 初始化门编号下拉列表
	public void initDoorSelect() {
		List<Map> list = new ArrayList<Map>();
		List<DoorModel> doors = peopleAuthorityInfoService.getDoorList();
		for (DoorModel door : doors) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("VALUE", door.getDoorNum());
			map.put("TEXT", door.getDoorName());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 查询卡下面的门权限
	 * by gengji.yang
	 */
	public void getDoorsByCardNum() {
		HttpServletRequest request = getHttpServletRequest();
		String cardNum = request.getParameter("cardNum");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String createUser = loginUser.getYhMc();
		List<DeviceModel> devices = peopleAuthorityInfoService
				.getDeviceModelByCardNum(cardNum);
		List deviceDoorList = new ArrayList();
		//上报控制器权限并更新 开始
		List<Map> allDevs=peopleAuthorityInfoService.allDevices();
		for(Map m:allDevs){
			if(peopleAuthorityInfoService.devOnLine((String)m.get("deviceNum"))){
				allSynchronizeService.uploadAndSynchronizeLimit((String)m.get("deviceNum"), cardNum, createUser);
				
			}
		}
		//上报控制器权限并更新 结束
		for (DeviceModel ele : devices) {
			for (Map map : peopleAuthorityInfoService
					.getDoorModelByDeviceNum(ele.getDeviceNum(),cardNum)) {
				deviceDoorList.add(map);
			}
		}
		printHttpServletResponse(GsonUtil.toJson(deviceDoorList));
	}

	
	//看到设备
	public void getDeviceByCardNum() {
		HttpServletRequest request = getHttpServletRequest();
		String cardNum = request.getParameter("cardNum");
		List<Map> mapList=peopleAuthorityInfoService.getDevicesOnCardNum(cardNum);//[{deviceNum=99ba56b1-36a8-4123-9e27-1fd5aaeae640, Ip=192.168.212.16, deviceName=85000a, mac=85000a}]
		printHttpServletResponse(GsonUtil.toJson(mapList));
	}

	public void getDoorByDeviceNum() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceNum = request.getParameter("deviceNum");//99ba56b1-36a8-4123-9e27-1fd5aaeae640
		String cardNum = request.getParameter("cardNum");//10838
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String createUser = loginUser.getYhMc();
		if(peopleAuthorityInfoService.devOnLine(deviceNum)){
			allSynchronizeService.uploadAndSynchronizeLimit(deviceNum, cardNum, createUser);
		}
		List<Map> doors = peopleAuthorityInfoService
				.getDoorModelByDeviceNum(deviceNum,cardNum);//[{deviceNum=99ba56b1-36a8-4123-9e27-1fd5aaeae640, validStartTime=2016-06-14 16:36, doorNum=1, groupNum=null, validEndTime=2016-06-30 16:36, doorName=1}, {deviceNum=99ba56b1-36a8-4123-9e27-1fd5aaeae640, validStartTime=2016-06-14 16:36, doorNum=2, groupNum=null, validEndTime=2016-06-30 16:36, doorName=2}]
		printHttpServletResponse(GsonUtil.toJson(doors));
	}

	// 获取门tree
	public void getDoorTree() {
		Tree doorTree = peopleAuthorityInfoService.getDoorTree();
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(doorTree));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}

	/**
	 * 获取原卡下的门权限
	 * by gengji.yang
	 */
	public void getSrcDoors() {
		HttpServletRequest request = getHttpServletRequest();
		String srcCardNums = request.getParameter("srcCardNums");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		List<Map> totalList = peopleAuthorityInfoService
				.getDoorModelByCardNum(srcCardNums, skip, rows);
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(totalList);
		if (null != totalList) {
			grid.setTotal(peopleAuthorityInfoService
					.getDoorModelCountByCardNum(srcCardNums));
		} else {
			grid.setTotal(0);
		}
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 获取目标卡
	 * by gengji.yang
	 */
	public void getDesCard() {
		HttpServletRequest request = getHttpServletRequest();
		String desCardNum = request.getParameter("desCardNum");
		CardModel model = peopleAuthorityInfoService.getDesCard(desCardNum);
		List<CardModel> list = new ArrayList();
		list.add(model);
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 开始复制
	 * by gengji.yang
	 */
	public void beginCopy() {
		HttpServletRequest request = getHttpServletRequest();
		String srcCardNums = request.getParameter("srcCardNums");
		String[] srcCardNumsArray = srcCardNums.split(",");
		List<String> beCopiedCardNumList = new ArrayList();
		for (int i = 0; i < srcCardNumsArray.length; i++) {
			beCopiedCardNumList.add(srcCardNumsArray[i]);
		}
		String desCardNum = request.getParameter("desCardNum");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String createUser = loginUser.getYhMc();
		JsonResult result = new JsonResult();
		//先删除 告诉通讯服务器 去删除目标卡中含有的 复制原卡中的门权限
		boolean isDelSuccess=true;
		boolean isAddSuccess=true;
		//存放原卡们的所有门权限
		List<Map> allAuthList=new ArrayList<Map>();
		
		for (int i = 0; i < beCopiedCardNumList.size(); i++) {
			List<Map> doorList = peopleAuthorityInfoService.getBeCopyCardDoors(beCopiedCardNumList.get(i));
			allAuthList.addAll(doorList);
		}
		for(Map ele:allAuthList){
			/*Map<String, String> map = PropertiesToMap.propertyToMap("comm_interface.properties");*/
			String delOldLimUrl = mjCommService.getMjCommUrl((String) ele.get("deviceNum"))+ "gateLimit/delGateLimit.do?";
			//----------准备参数起(for 先删除)-----------------
			Map deviceMap=deviceService.getMacByDeviceNum((String) ele.get("deviceNum"));
			String mac = (String) deviceMap.get("deviceMac");// 16
			String deviceType = (String) deviceMap.get("deviceType");// 16
			String gateId = (String) ele.get("doorNum");
			String s = desCardNum;
			Integer a = Integer.parseInt(s);
			String cardId = Integer.toHexString(a);
			//----------准备参数止(for 先删除)-----------------
			
			String delOldLimStr = HttpRequest.sendPost(delOldLimUrl, "mac="
					+ mac + "&cardId=" + cardId + "&gateId=" + gateId+"&device_type="+deviceType);

			Map delOldLimMap = GsonUtil.toBean(delOldLimStr, HashMap.class);
			if(delOldLimMap!=null){
				if ("1".equals(delOldLimMap.get("result_code"))) {
					isDelSuccess = false;
				}
			}else{
				isDelSuccess = false;
			}
			
			//----------准备参数起(for 后设置)-----------------
			String start = (String) ele.get("startTime");
			String end = (String) ele.get("endTime");
			String group=null;
			if(!"".equals((String) ele.get("groupNum"))&&null!=ele.get("groupNum")){
				group = toHexStr((String) ele.get("groupNum")) + "0000";
			}
			
			//----------准备参数止(for 后设置)-----------------
/*			Map<String, String> map1 = PropertiesToMap
					.propertyToMap("comm_interface.properties");*/
			String gateLimUrl = mjCommService.getMjCommUrl((String) ele.get("deviceNum"))
					+ "gateLimit/setGateLimit.do?";
			String gateLimStr = HttpRequest.sendPost(gateLimUrl, "mac="
					+ mac + "&cardId=" + cardId + "&gateId=" + gateId
					+ "&start=" + start + "&end=" + end + "&group=" + group+"&device_type="+deviceType);
			Map gateLimMap = GsonUtil.toBean(gateLimStr, HashMap.class);
			if(gateLimMap!=null){
				if("1".equals(gateLimMap.get("result_code"))){
					isAddSuccess=false;
				}
			}else{
				isAddSuccess=false;
			}
		}
		
		if(isDelSuccess&&isAddSuccess){
			try {
				peopleAuthorityInfoService.copyAuthority(beCopiedCardNumList,
						desCardNum, createUser);
				result.setSuccess(true);
				printHttpServletResponse(GsonUtil.toJson(result));
			} catch (Exception e) {
				e.printStackTrace();
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		}else{
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	// 初始化授权页面的门列表
	public void initGrantCard() {// ===============================================
		HttpServletRequest request = getHttpServletRequest();
		String cardNumsArr = request.getParameter("cardNumsArr");// 10287
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		List<CardModel> totalList = peopleAuthorityInfoService.getDesCardList(
				cardNumsArr, skip, rows);
		Grid<CardModel> grid = new Grid<CardModel>();
		grid.setRows(totalList);
		if (null != totalList) {
			grid.setTotal(peopleAuthorityInfoService.getDesCardListCount(cardNumsArr));
		} else {
			grid.setTotal(0);
		}
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * 工具方法 by Gengji.yang
	 * 
	 * @param jsonHexStr
	 * @return
	 */
	private static String encodeToJsonStr(String jsonHexStr) {
		String[] jsonHexArr = jsonHexStr.split(",");
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < jsonHexArr.length; i++) {
			String str;
			try {
				str = new String(jsonHexArr[i].getBytes("iso-8859-1"), "UTF-8");
				char ele = (char) Integer.parseInt(str, 16);
				build.append(ele);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String jsonStr = new String(build);
		return jsonStr;
	}

	/**
	 *初始化授权 门 列表
	 *直接将页面的数据带到另一个页面，无数据库操作
	 *by gengji.yang 
	 */
	public void initGrantDoor() {
		HttpServletRequest request = getHttpServletRequest();
		Gson gson = new Gson();
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		Map map=new HashMap();
	    map.put("rows", rows);
	    map.put("skip", skip);
		if(data!=null){
			List<LinkedTreeMap> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());//[{doorNum=1, deviceNum=2118a646-c04b-41f1-89fb-23cabe9e1d40, mac=85000a, deviceType=4}, {doorNum=2, deviceNum=7158026d-99c2-448b-a54d-2c34bb86ad0c, mac=11, deviceType=4}, {doorNum=3, deviceNum=7158026d-99c2-448b-a54d-2c34bb86ad0c, mac=11, deviceType=4}]
			map.put("devDorList", list);
			printHttpServletResponse(GsonUtil.toJson(peopleAuthorityInfoService.getDeviceDoorList(map)));
		}
	}
	
	/**
	 * 旧版的方法
	 * by gengji.yang
	 */
	public void initGrantDoor_old(){
		HttpServletRequest request = getHttpServletRequest();
		Gson gson = new Gson();
		String jsonHexStr = request.getParameter("deviceDoorObjs");
		List<LinkedTreeMap> list = gson.fromJson(encodeToJsonStr(jsonHexStr),new ArrayList<LinkedTreeMap>().getClass());//[{doorNum=1, deviceNum=2118a646-c04b-41f1-89fb-23cabe9e1d40, mac=85000a, deviceType=4}, {doorNum=2, deviceNum=7158026d-99c2-448b-a54d-2c34bb86ad0c, mac=11, deviceType=4}, {doorNum=3, deviceNum=7158026d-99c2-448b-a54d-2c34bb86ad0c, mac=11, deviceType=4}]
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	/**
	 * 删除门权限 
	 * by gengji.yang
	 */
	public void deleteAuth() {
		HttpServletRequest request = getHttpServletRequest();
		Gson gson = new Gson();
		String jsonHexStr = request.getParameter("deviceDoorObjs");
		String card = request.getParameter("card");
		//设备门，多少个门就有多少个  设备门对象 ，即list的size
		List<LinkedTreeMap> list = gson.fromJson(encodeToJsonStr(jsonHexStr),new ArrayList<LinkedTreeMap>().getClass());
		List<PeopleAuthorityInfoModel> authList = new ArrayList<PeopleAuthorityInfoModel>();
		for (int i = 0; i < list.size(); i++) {
				PeopleAuthorityInfoModel model = new PeopleAuthorityInfoModel();
				model.setDeviceNum((String) list.get(i).get("deviceNum"));
				model.setDoorNum((String) list.get(i).get("doorNum"));
				model.setCardNum(card);
				authList.add(model);
		}
		JsonResult result = new JsonResult();
		try {
			Boolean isSuccess = true;
			for (PeopleAuthorityInfoModel model : authList) {
				/*Map<String, String> map = PropertiesToMap.propertyToMap("comm_interface.properties");*/
				String delOldLimUrl = mjCommService.getMjCommUrl(model.getDeviceNum())+ "gateLimit/delGateLimit.do?";
				Map deviceMap=deviceService.getMacByDeviceNum(model.getDeviceNum());
				String mac = (String) deviceMap.get("deviceMac");// 16
				String deviceType = (String) deviceMap.get("deviceType");// 16
				String gateId = model.getDoorNum();
				String s = model.getCardNum();
				Integer a = Integer.parseInt(s);
				String cardId = a.toHexString(a);
				String delOldLimStr = HttpRequest.sendPost(delOldLimUrl, "mac="
						+mac + "&cardId=" + cardId + "&gateId=" + gateId+"&device_type="+deviceType);// {"result_code":"1","result_msg":"删除门禁权限失败"}

				Map delOldLimMap = GsonUtil.toBean(delOldLimStr, HashMap.class);
				
				if(delOldLimMap!=null){
					if ("1".equals(delOldLimMap.get("result_code"))) {
						isSuccess = false;
					}
				}else{
					isSuccess = false;
				}
			}

			if (isSuccess) {
				peopleAuthorityInfoService.delOldDoorAuth(authList);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	// 初始化 时段组 下拉框
	public void initTimesGroup() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceNum=request.getParameter("deviceNum");
		List<Map> groups = peopleAuthorityInfoService.getMjTimeGroup(deviceNum);//[{group_name=a, group_num=0}, {group_name=aaa, group_num=1}]
		printHttpServletResponse(GsonUtil.toJson(groups));
	}

	/**
	 * 把字符串 26转成1a的16进制 by gengji.yang
	 * 
	 * @param s
	 * @return
	 */
	private static String toHexStr(String s) {
		Integer a = Integer.parseInt(s);
		String c = a.toHexString(a);
		return c;
	}

	/**
	 * 将“2016-05-26” 转成 字符串数组 [16,05,26] 的 16进制数组 [10, 5, 1a] by gengji.yang
	 * 
	 * @param date
	 * @return
	 */

	private static String[] toHexStrArr(String date) {
		String[] dateArr = date.split("-");
		dateArr[0] = dateArr[0].substring(2, dateArr.length + 1);
		String[] dateHexArr = new String[dateArr.length];
		for (int i = 0; i < dateArr.length; i++) {
			dateHexArr[i] = toHexStr(dateArr[i]);
		}
		return dateHexArr;
	}

	/**
	 * 下发权限 
	 *by gengji.yang
	 */
	public void grantAuth() {
		HttpServletRequest request = getHttpServletRequest();
		String cards = request.getParameter("cards");
		String[] cardsArr = cards.split(",");
		Gson gson = new Gson();
		List<LinkedTreeMap> list = gson.fromJson(request.getParameter("rows"),new ArrayList<LinkedTreeMap>().getClass());
		List<PeopleAuthorityInfoModel> authList = new ArrayList<PeopleAuthorityInfoModel>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < cardsArr.length; j++) {
				PeopleAuthorityInfoModel model = new PeopleAuthorityInfoModel();
				model.setDeviceNum((String) list.get(i).get("deviceNum"));
				model.setDoorNum((String) list.get(i).get("doorNum"));
				model.setDoorName((String) list.get(i).get("doorName"));
				model.setTimeGroupNum((String) list.get(i).get("group_num"));
				model.setValidStartTime((String) list.get(i).get("validStartTime"));
				model.setValidEndTime((String) list.get(i).get("validEndTime"));
				model.setCardNum(cardsArr[j]);
				authList.add(model);
			}
		}
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String createUser = loginUser.getYhMc();
		JsonResult result = new JsonResult();
		try {
			Boolean isSuccess = true;
			for (PeopleAuthorityInfoModel model : authList) {
				/*Map<String, String> map = PropertiesToMap.propertyToMap("comm_interface.properties");*/
				String delOldLimUrl = mjCommService.getMjCommUrl(model.getDeviceNum())+ "gateLimit/delGateLimit.do?";
				String gateLimUrl = mjCommService.getMjCommUrl(model.getDeviceNum())+ "gateLimit/setGateLimit.do?";
				Map deviceMap=deviceService.getMacByDeviceNum(model.getDeviceNum());
				String mac = (String) deviceMap.get("deviceMac");// 16
				String deviceType = (String) deviceMap.get("deviceType");// 16
				String gateId = model.getDoorNum();
				String s = model.getCardNum();
				Integer a = Integer.parseInt(s);
				String cardId = a.toHexString(a);
				String start = model.getValidStartTime();
				String end = model.getValidEndTime();
				String group=null;
				if(!"".equals(model.getTimeGroupNum()) && null!=model.getTimeGroupNum()){
				 group = toHexStr(model.getTimeGroupNum());
				}
				// 调用通讯服务器接口
				String delOldLimStr = HttpRequest.sendPost(delOldLimUrl, "mac="
						+mac + "&cardId=" + cardId + "&gateId=" + gateId+"&device_type="+deviceType);// {"result_code":"1","result_msg":"删除门禁权限失败"}
				String gateLimStr = HttpRequest.sendPost(gateLimUrl, "mac="
						+ mac + "&cardId=" + cardId + "&gateId=" + gateId
						+ "&start=" + start + "&end=" + end  + "&group=" + group+"&device_type="+deviceType);

				Map delOldLimMap = GsonUtil.toBean(delOldLimStr, HashMap.class);
				Map gateLimMap = GsonUtil.toBean(gateLimStr, HashMap.class);
				
				if(delOldLimMap!=null&&gateLimMap!=null){
					if ("1".equals(delOldLimMap.get("result_code"))|| "1".equals(gateLimMap.get("result_code"))) {
						isSuccess = false;
					}
				}else{
					isSuccess = false;
				}
			}

			if (isSuccess){
				peopleAuthorityInfoService.delOldDoorAuth(authList);
				peopleAuthorityInfoService.dispatchAuthority(authList,createUser);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	// 按条件下载
	public void searchAuthorityDownLoad() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data,HashMap.class);
		// 文件名获取
		HttpServletResponse response = getHttpServletResponse();
		List<Map> peopleAuthoritylist=peopleAuthorityInfoService.searchAuthDownload(map);
		for(Map map2:peopleAuthoritylist){
			if(null == map2.get("groupName") ||"".equals(map2.get("groupName").toString())){
				map2.put("groupName", "-");
			}
			if(null == map2.get("createUser") ||"".equals(map2.get("createUser").toString())){
				map2.put("createUser", "-");
			}
		}
		List list = GsonUtil.getListFromJson(GsonUtil.toJson(peopleAuthoritylist), ArrayList.class);
		OutputStream out = null;
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
				colList.add("deptName");
				colTitleList.add("部门名称");
				colList.add("staffNo");
				colTitleList.add("员工工号");
				colList.add("staffName");
				colTitleList.add("员工名称");
				colList.add("cardNum");
				colTitleList.add("卡号");
				colList.add("deviceName");
				colTitleList.add("设备名称");
				colList.add("deviceIp");
				colTitleList.add("设备Ip");
				colList.add("doorNum");
				colTitleList.add("门号");
				colList.add("doorName");
				colTitleList.add("门名称");
				colList.add("groupName");
				colTitleList.add("时段组");
				colList.add("startTime");
				colTitleList.add("有效时间起");
				colList.add("endTime");
				colTitleList.add("有效时间止");
				colList.add("createTime");
				colTitleList.add("创建时间");
				colList.add("createUser");
				colTitleList.add("操作员");
			/*	colList.add("description");
				colTitleList.add("备注");*/
		// 列数据键
		String[] cols = new String[colList.size()];
		// 列表题
		String[] colTitles = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="权限表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("权限表", colTitles,
					cols, list);
			workbook.write(out);
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Resource(name = "employeeService")
    EmployeeService employeeService;
	
	/**
	 * 删除员工权限按钮 
	 * by gengji.yang
	 */
	public void deleteEmpAuth(){
		HttpServletRequest request = getHttpServletRequest();
		String empNum=request.getParameter("empNum");
		JsonResult result=new JsonResult();
		List cardListToDel=new ArrayList();
		//根据员工号拿到所有卡
		try{
			List<BoundCard> cardList=employeeService.selectBoundCardByYHDM(empNum).getRows();
			List<Map> totalList=new ArrayList<Map>();
			for(BoundCard card:cardList){
				cardListToDel.add(card.getCard_num());
				String cardNum=card.getCard_num();
				//获取卡下的 设备编号 和 门编号 列表
				List<Map> doorList=peopleAuthorityInfoService.getBeCopyCardDoors(cardNum);//[{startTime=2016-06-28 18:52, deviceNum=650b4f58-1cf2-4db8-b268-fc791ef3736a, description=, doorNum=1, groupNum=, endTime=2016-06-29 18:52}]
				for(Map ele:doorList){
					Map deviceMap=deviceService.getMacByDeviceNum(ele.get("deviceNum").toString());
					String mac = (String) deviceMap.get("deviceMac");// 16
					String deviceType = (String) deviceMap.get("deviceType");// 16
					Map m=new HashMap();
					m.put("gateId",ele.get("doorNum"));
					m.put("cardId",cardNum);
					m.put("mac",mac);
					m.put("deviceType",deviceType);
					m.put("deviceNum",ele.get("deviceNum").toString());
					totalList.add(m);
				}
			}
			Boolean isSuccess = true;
			for(Map ele:totalList){
				/*Map<String, String> map = PropertiesToMap.propertyToMap("comm_interface.properties");*/
				String delOldLimUrl = mjCommService.getMjCommUrl(ele.get("deviceNum").toString())+ "gateLimit/delGateLimit.do?";
				Integer cardId=Integer.parseInt((String)ele.get("cardId"));
				
				String delOldLimStr = HttpRequest.sendPost(delOldLimUrl, "mac="
						+ele.get("mac") + "&cardId=" +Integer.toHexString(cardId)+ "&gateId=" +ele.get("gateId")+"&device_type="+ele.get("deviceType"));
				Map delOldLimMap = GsonUtil.toBean(delOldLimStr, HashMap.class);
				
				if(delOldLimMap!=null){
					if ("1".equals(delOldLimMap.get("result_code"))) {
						isSuccess = false;
					}
				}else{
					isSuccess = false;
				}
			}
			if (isSuccess) {
				if(Arrays.toString(cardListToDel.toArray()).substring(1, Arrays.toString(cardListToDel.toArray()).length()-1).length()>0){
					peopleAuthorityInfoService.delAuthByCards(Arrays.toString(cardListToDel.toArray()).substring(1, Arrays.toString(cardListToDel.toArray()).length()-1));
				}
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	
	/**
	 * 门信息列表
	 * by gengji.yang
	 */
	public void getDoorsInfo(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Map map=GsonUtil.toBean(data,HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
	    map.put("rows", rows);
	    map.put("skip", skip);
	    Grid<Map> grid=peopleAuthorityInfoService.getDoorsInfoDynamic(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 保存组织权限
	 * by gengji.yang
	 */
	private void saveOrganAuth(List<String> groupList,List<Map> deviceDoorList){
		for(int i=0;i<groupList.size();i++){
			for(Map m:deviceDoorList){
				peopleAuthorityInfoService.deleteOrganAuth(groupList.get(i).substring(1,groupList.get(i).length()-1),m.get("deviceNum").toString(),m.get("doorNum").toString());
				m.put("deptNum",groupList.get(i).substring(1,groupList.get(i).length()-1));
				peopleAuthorityInfoService.saveOrganAuth(m);
			}	
		}
	}
	
	/**
	 * 保存组织权限
	 * by gengji.yang
	 */
	private void delOrganAuth(List<String> groupList,List<Map> deviceDoorList){
		for(int i=0;i<groupList.size();i++){
			for(Map m:deviceDoorList){
				peopleAuthorityInfoService.deleteOrganAuth(groupList.get(i).substring(1,groupList.get(i).length()-1),m.get("deviceNum").toString(),m.get("doorNum").toString());
			}	
		}
	}
	
	
	/**
	 * 组织授权
	 * by gengji.yang
	 */
	public void grantGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String deviceDoorArr=request.getParameter("deviceDoorArr");
		String groupIds=request.getParameter("groupIds");
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String createUser = loginUser.getYhMc();
		Gson g=new Gson();
		List<Map> deviceDoorList=g.fromJson(deviceDoorArr, new ArrayList<LinkedTreeMap>().getClass());//[{deviceNum=5da71ace-c158-4f3e-b4fa-f20aae524087, id=50.0, createTime=2016-09-08 17:23:50, deviceName=850034, deviceType=4, doorNum=2, doorName=2, mac=850034, device_ip=192.168.214.88, validStartTime=2016-09-30 09:22, validEndTime=2016-09-30 09:22, group_num=}]
		List<String> groupList=g.fromJson(groupIds, new ArrayList<String>().getClass());//[2, QD, 11223, 987]
		List<PeopleAuthorityInfoModel> authList = new ArrayList<PeopleAuthorityInfoModel>();
		JsonResult result=new JsonResult();
		boolean isSuccess=true;
		try{
			List<Map> cardList=peopleAuthorityInfoService.getAllCards(groupList);
			/*Map<String, String> urlMap = PropertiesToMap.propertyToMap("comm_interface.properties");*/
			
			for(Map cardMap:cardList){
				for(Map deviceDoorMap:deviceDoorList){
					//----------  数据库操作准备开始  -----------------
					PeopleAuthorityInfoModel model = new PeopleAuthorityInfoModel();
					model.setDeviceNum((String) deviceDoorMap.get("deviceNum"));
					model.setDoorNum((String) deviceDoorMap.get("doorNum"));
					model.setCardNum((String) cardMap.get("cardNum"));
					model.setValidStartTime((String)deviceDoorMap.get("validStartTime"));
					model.setValidEndTime((String)deviceDoorMap.get("validEndTime"));
					model.setTimeGroupNum((String)deviceDoorMap.get("group_num"));
					authList.add(model);
					//----------  数据库操作准备结束  -----------------
					
					//-----------  通讯服务器操作开始  --------------------------------
					Integer cardId=Integer.parseInt((String)cardMap.get("cardNum"));
					String group=null;
					if(!"".equals((String)deviceDoorMap.get("group_num")) && null!=(String)deviceDoorMap.get("group_num")){
					 group = toHexStr((String)deviceDoorMap.get("group_num"));
					}
					
					String delOldLimUrl = mjCommService.getMjCommUrl((String) deviceDoorMap.get("deviceNum"))+ "gateLimit/delGateLimit.do?";
					String gateLimUrl = mjCommService.getMjCommUrl((String) deviceDoorMap.get("deviceNum"))+ "gateLimit/setGateLimit.do?";
					
					String delOldLimStr = HttpRequest.sendPost(delOldLimUrl, "mac="+deviceDoorMap.get("mac") + "&cardId=" +Integer.toHexString(cardId)+ "&gateId=" +deviceDoorMap.get("doorNum")+"&device_type="+deviceDoorMap.get("deviceType"));
					String gateLimStr = HttpRequest.sendPost(gateLimUrl, "mac="+ deviceDoorMap.get("mac")+ "&cardId=" +Integer.toHexString(cardId) + "&gateId=" + deviceDoorMap.get("doorNum")+ "&start=" + deviceDoorMap.get("validStartTime") + "&end=" + deviceDoorMap.get("validEndTime")   + "&group=" + group+"&device_type="+deviceDoorMap.get("deviceType"));
					
					Map delOldLimMap = GsonUtil.toBean(delOldLimStr, HashMap.class);
					Map gateLimMap = GsonUtil.toBean(gateLimStr, HashMap.class);
					
					if(delOldLimMap!=null&&gateLimMap!=null){
						if ("1".equals(delOldLimMap.get("result_code"))|| "1".equals(gateLimMap.get("result_code"))) {
							isSuccess = false;
						}
					}else{
						isSuccess = false;
					}
					//-----------  通讯服务器操作开始  --------------------------------
				}
			}
			if (isSuccess){
				saveOrganAuth(groupList,deviceDoorList);
				peopleAuthorityInfoService.delOldDoorAuth(authList);
				peopleAuthorityInfoService.dispatchAuthority(authList,createUser);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
 	}
	
	/**
	 * 初始化目标门(暂未使用)
	 * by gengji.yang
	 */
	public void getDescDoors(){
		HttpServletRequest request=getHttpServletRequest();
		String doorNum=request.getParameter("doorNum");
		Grid<Map> g=peopleAuthorityInfoService.getDescDoors(doorNum);
		printHttpServletResponse(GsonUtil.toJson(g));
	}
	
	/**
	 * 将门权限复制到别的门上
	 * by gengji.yang
	 */
	public void copyDoors(){
		HttpServletRequest request=getHttpServletRequest();
		String desDoors=request.getParameter("desDoors");
		String srcDoor=request.getParameter("srcDoor");
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String createUser = loginUser.getYhMc();
		/*Map<String, String> urlMap = PropertiesToMap.propertyToMap("comm_interface.properties");*/
		
		Gson g=new Gson();
		boolean isSuccess=true;
		JsonResult result=new JsonResult();
		Map srcDoorMap=g.fromJson(srcDoor, HashMap.class);
		List<Map> desDoorsList=g.fromJson(encodeToJsonStr(desDoors), new ArrayList<LinkedTreeMap>().getClass());//[{doorNum=3, deviceNum=57726c6d-2960-4b9f-a9e6-78739856974b, mac=85000a, deviceType=4}, {doorNum=4, deviceNum=57726c6d-2960-4b9f-a9e6-78739856974b, mac=85000a, deviceType=4}, {doorNum=2, deviceNum=57726c6d-2960-4b9f-a9e6-78739856974b, mac=85000a, deviceType=4}]
		List<Map> srcDoorAuthList=peopleAuthorityInfoService.getAuthOfDoor(srcDoorMap);
		List<PeopleAuthorityInfoModel> authList = new ArrayList<PeopleAuthorityInfoModel>();
		for(Map desDoor:desDoorsList){
			for(Map srcAuth:srcDoorAuthList){
				//---------------数据库起------------------------
				PeopleAuthorityInfoModel model = new PeopleAuthorityInfoModel();
				model.setDeviceNum((String) desDoor.get("deviceNum"));
				model.setDoorNum((String) desDoor.get("doorNum"));
				model.setCardNum((String) srcAuth.get("cardNum"));
				model.setValidStartTime((String)srcAuth.get("validStartTime"));
				model.setValidEndTime((String)srcAuth.get("validEndTime"));
				model.setTimeGroupNum((String)srcAuth.get("groupNum"));
				authList.add(model);
				//--------------------数据库止-----------------------
				Integer cardId=Integer.parseInt((String)srcAuth.get("cardNum"));
				String gateLimUrl = mjCommService.getMjCommUrl((String) desDoor.get("deviceNum"))+ "gateLimit/setGateLimit.do?";
				String gateLimStr=HttpRequest.sendPost(gateLimUrl, "mac="+ desDoor.get("mac")+ "&cardId=" +Integer.toHexString(cardId) + "&gateId=" + desDoor.get("doorNum")+ "&start=" + srcAuth.get("validStartTime") + "&end=" + srcAuth.get("validEndTime")   + "&group=" + srcAuth.get("groupNum")+"&device_type="+desDoor.get("deviceType"));
				Map gateLimMap = GsonUtil.toBean(gateLimStr, HashMap.class);
				if(gateLimMap!=null){
					if ("1".equals(gateLimMap.get("result_code"))){
						isSuccess = false;
					}
				}else{
					isSuccess = false;
				}
			}
		}
		try{
			if(isSuccess){
				peopleAuthorityInfoService.delOldDoorAuth(authList);
				peopleAuthorityInfoService.dispatchAuthority(authList,createUser);
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 组织授权时，
	 * 判断是否有员工
	 * by gengji.yang
	 */
	public void hasEmp(){
		HttpServletRequest request=getHttpServletRequest();
		String deptNumStr=request.getParameter("deptNumStr");
		JsonResult result=new JsonResult();
		result.setSuccess(peopleAuthorityInfoService.hasEmp(deptNumStr));
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 点击设备门树，初始化右侧表格
	 * by gengji.yang
	 */
	public void initDeviceDoorTab(){
		HttpServletRequest request = getHttpServletRequest();
		Gson gson = new Gson();
		String data = request.getParameter("data");
		if(data!=null&&!"null".equals(data)){
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			Integer skip = (page - 1) * rows;
			Map map=new HashMap();
		    map.put("rows", rows);
		    map.put("skip", skip);
			List<Map> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
			if(list.size()==0){
				List<Map> nullList=new ArrayList<Map>();
				printHttpServletResponse(GsonUtil.toJson(nullList));
			}else{
				map.put("devDorList", list);
				printHttpServletResponse(GsonUtil.toJson(peopleAuthorityInfoService.getDeviceDoorList(map)));
			}
		}else{
			List<Map> nullList=new ArrayList<Map>();
			printHttpServletResponse(GsonUtil.toJson(nullList));
		}
	}
	
	/**
	 * 删除卡权限时所看到的表格
	 * by gengji.yang
	 */
	public void initAuthTab(){
		HttpServletRequest request = getHttpServletRequest();
 		Gson gson = new Gson();
		String data = request.getParameter("data");
		String cardNum = request.getParameter("cardNum");
		if(data!=null){
			List<Map> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
			if(list.size()>0){
				for(Map map:list){
					map.put("cardNum", cardNum);
				}
			}
			printHttpServletResponse(GsonUtil.toJson(peopleAuthorityInfoService.getAuthInfo(list)));
		}else{
			Grid grid=new Grid();
			grid.setRows(new ArrayList());
			grid.setTotal(0);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}
	}
	
	/**
	 * 改版后的新增权限
	 * 只管往授权任务表中加记录
	 * by gengji.yang
	 */
	public void addAuthTasks(){
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		Gson gson = new Gson();
		String data = request.getParameter("data");
		List<Map> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
		JsonResult result=new JsonResult();
		try{
			updateAuthState(0,list,login_user);
			peopleAuthorityInfoService.addAuthTasks(list,0);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 改版后的删除卡权限
	 * 只管往权限任务表中添加删除权限的任务
	 * by gengji.yang
	 */
	public void delAuthTasks(){
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		Gson gson = new Gson();
		String data = request.getParameter("data");
		List<Map> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
		JsonResult result=new JsonResult();
		try{
			updateAuthState(1,list,login_user);
			peopleAuthorityInfoService.addAuthTasks(list,1);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 改版后的删除员工权限
	 * by gengji.yang
	 */
	public void delEmpAuthTask(){
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String empNum=request.getParameter("empNum");
		JsonResult result=new JsonResult();
		//根据员工号拿到所有卡
		try{
			List<BoundCard> cardList=employeeService.selectBoundCardByYHDM(empNum).getRows();
			List<Map> totalList=new ArrayList<Map>();
			for(BoundCard card:cardList){
				String cardNum=card.getCard_num();
				//获取卡下的 设备编号 和 门编号 列表
				List<Map> doorList=peopleAuthorityInfoService.getBeCopyCardDoors(cardNum);//[{startTime=2016-06-28 18:52, deviceNum=650b4f58-1cf2-4db8-b268-fc791ef3736a, description=, doorNum=1, groupNum=, endTime=2016-06-29 18:52}]
				for(Map ele:doorList){
					Map deviceMap=deviceService.getMacByDeviceNum(ele.get("deviceNum").toString());
					deviceMap.put("doorNum",ele.get("doorNum"));
					deviceMap.put("deviceNum",ele.get("deviceNum"));
					deviceMap.put("cardNum",cardNum);
					deviceMap.put("groupNum",ele.get("groupNum"));
					totalList.add(deviceMap);
				}
			}
			if(totalList.size()>0){
				//此处先修改权限任务的状态值，让用户立马有一个感官上的认知
				updateAuthState(1,totalList,login_user);
				//接着执行任务，执行后再接着更改状态值
				peopleAuthorityInfoService.addAuthTasks(totalList,1);
			}
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除或者新增权限时
	 * 及时修改权限状态
	 * flag 0 新增，1 删除
	 * by gengji.yang
	 */
	public void updateAuthState(int flag,List<Map> authList,String yhmc){
		if(flag==0){//新增 先往权限表 插权限记录 标记好 task_state=00
			addWorker(authList,"00",yhmc);
		}else{//删除权限 先更新权限表 中 task_state==10
			updateWorker(authList,"10");
		}
	}
	
	/**
	 * 新增权限时
	 * 先插入权限记录，task_state=00
	 */
	public void addWorker(List<Map>authList,String state,String yhmc){
		for(Map map:authList){
			map.put("state",state);
			map.put("yhmc",yhmc);
			peopleAuthorityInfoService.delAuthRecord(map);
			peopleAuthorityInfoService.addAuthRecord(map);
		}
	}
	
	/**
	 * 删除权限前
	 * 更新每一个权限记录的worker
	 * param:state 10->待删除
	 * by gengji.yang
	 */
	public void updateWorker(List<Map> authList,String state){
		for(Map map:authList){
			map.put("state", state);
			peopleAuthorityInfoService.updateTskState(map);
		}
	}
	
	/**
	 * 改版后的复制权限
	 * by gengji.yang
	 */
	public void copyAuthTask(){
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String srcCardNums = request.getParameter("srcCardNums");
		String[] srcCardNumsArray = srcCardNums.split(",");
		String desCardNum = request.getParameter("desCardNum");
		List<String> beCopiedCardNumList = new ArrayList();
		for (int i = 0; i < srcCardNumsArray.length; i++) {
			beCopiedCardNumList.add(srcCardNumsArray[i]);
		}
		JsonResult result = new JsonResult();
		//存放原卡们的所有门权限
		List<Map> allAuthList=new ArrayList<Map>();
		for (int i = 0; i < beCopiedCardNumList.size(); i++) {
			List<Map> doorList = peopleAuthorityInfoService.getBeCopyCardDoors(beCopiedCardNumList.get(i));
			allAuthList.addAll(doorList);
		}
		for(Map ele:allAuthList){
			Map deviceMap=deviceService.getMacByDeviceNum((String) ele.get("deviceNum"));
			ele.put("deviceMac",deviceMap.get("deviceMac"));
			ele.put("deviceType",deviceMap.get("deviceType"));
			ele.put("cardNum",desCardNum);
		}
		try{
			if(allAuthList.size()>0){
				updateAuthState(0,allAuthList,login_user);
				peopleAuthorityInfoService.addAuthTasks(allAuthList,0);
			}
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 改版后的组织授权
	 * 门权限中的组织授权已改为
	 * 新增门权限
	 * by gengji.yang
	 */
	public void addGroupTask(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		final String login_user = loginUser.getYhMc();
		String deviceDoorArr=request.getParameter("deviceDoorArr");
		String groupIds=request.getParameter("groupIds");
		Gson g=new Gson();
		List<LinkedTreeMap> deviceDoorList=g.fromJson(deviceDoorArr, new ArrayList<LinkedTreeMap>().getClass());
		//页面中已经可以选到卡，故此处的groupList 存放的就是卡号
		List<String> groupList=g.fromJson(groupIds, new ArrayList<String>().getClass());
		final List<Map> authList=new ArrayList<Map>();
		final JsonResult result=new JsonResult();
		try{
			//List<Map> card1List=peopleAuthorityInfoService.getAllCards(groupList);
			for(String cardStr:groupList){
				for(Map deviceDoorMap:deviceDoorList){
					//----------  数据库操作准备开始  -----------------
					Map m=new HashMap();
					m.put("cardNum", cardStr);
					m.put("deviceNum",  deviceDoorMap.get("deviceNum"));
					m.put("doorNum", deviceDoorMap.get("doorNum"));
					m.put("deviceMac", deviceDoorMap.get("mac"));
					m.put("deviceType", deviceDoorMap.get("deviceType"));
					m.put("startTime", deviceDoorMap.get("validStartTime"));
					m.put("endTime", deviceDoorMap.get("validEndTime"));
					m.put("groupNum", deviceDoorMap.get("group_num"));
					authList.add(m);
					//----------  数据库操作准备结束  -----------------
				}
			}
			//saveOrganAuth(groupList,deviceDoorList);
			if(authList.size()>0){
				new Thread(new Runnable() {
					public void run() {
						try {
							updateAuthState(0,authList,login_user);
							peopleAuthorityInfoService.addAuthTasks(authList,0);
						} catch (Exception e) {
							e.printStackTrace();
							result.setSuccess(false);
						}
					}
				}).start();
			}
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 复制门权限前的判断
	 * by gengji.yang
	 */
	public void beforeCpyDor(){
		HttpServletRequest request=getHttpServletRequest();
		String srcDoor=request.getParameter("srcDoor");
		Gson g=new Gson();
		JsonResult result=new JsonResult();
		Map srcDoorMap=g.fromJson(srcDoor, HashMap.class);
		List<Map> srcDoorAuthList=peopleAuthorityInfoService.getAuthOfDoor(srcDoorMap);
		try{
			if(srcDoorAuthList.size()>0){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	
	}
	
	/**
	 * 改版后的复制门权限
	 * by gengji.yang
	 */
	public void cpyDoorTask(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String desDoors=request.getParameter("desDoors");
		String srcDoor=request.getParameter("srcDoor");
		Gson g=new Gson();
		JsonResult result=new JsonResult();
		Map srcDoorMap=g.fromJson(srcDoor, HashMap.class);
		List<Map> desDoorsList=g.fromJson(encodeToJsonStr(desDoors), new ArrayList<LinkedTreeMap>().getClass());//[{doorNum=3, deviceNum=57726c6d-2960-4b9f-a9e6-78739856974b, mac=85000a, deviceType=4}, {doorNum=4, deviceNum=57726c6d-2960-4b9f-a9e6-78739856974b, mac=85000a, deviceType=4}, {doorNum=2, deviceNum=57726c6d-2960-4b9f-a9e6-78739856974b, mac=85000a, deviceType=4}]
		List<Map> srcDoorAuthList=peopleAuthorityInfoService.getAuthOfDoor(srcDoorMap);
		List<Map> authList=new ArrayList<Map>();
		for(Map desDoor:desDoorsList){
			for(Map srcAuth:srcDoorAuthList){
				//---------------数据库起------------------------
				Map m=new HashMap();
				m.put("cardNum", srcAuth.get("cardNum"));
				m.put("deviceNum", desDoor.get("deviceNum"));
				m.put("doorNum", desDoor.get("doorNum"));
				m.put("deviceMac", desDoor.get("mac"));
				m.put("deviceType", desDoor.get("deviceType"));
				m.put("startTime", srcAuth.get("validStartTime"));
				m.put("endTime", srcAuth.get("validEndTime"));
				m.put("groupNum", srcAuth.get("groupNum"));
				authList.add(m);
				//--------------------数据库止-----------------------
			}
		}
		try{
			if(authList.size()>0){
				updateAuthState(0,authList,login_user);
				peopleAuthorityInfoService.addAuthTasks(authList,0);
			}
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 门权限管理
	 * 勾选一个门
	 * 就显示拥有该门权限的人员
	 * by gengji.yang
	 */
	public void getInfoOnDevDor(){
		HttpServletRequest request=getHttpServletRequest();
		String deviceDoorArr=request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		Gson g=new Gson();
		if(deviceDoorArr!=null){
			List<Map> deviceDoorList=g.fromJson(deviceDoorArr, new ArrayList<LinkedTreeMap>().getClass());
			Grid grid=peopleAuthorityInfoService.getInfoOnDevDor(deviceDoorList,skip,rows);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}else{
			Map map=new HashMap();
			map.put("deviceNum", "");
			map.put("doorNum", "");
			List<Map> list=new ArrayList<Map>();
			list.add(map);
			Grid grid=peopleAuthorityInfoService.getInfoOnDevDor(list,skip,rows);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}
	}
	
	/**
	 * 部门员工卡树
	 * by gengji.yang
	 */
    /**
     * 部门员工tree
     */
/*    public void getDepartmentCardTree() {
        HttpServletRequest request = getHttpServletRequest();
        String id = request.getParameter("id");
        Tree treeDeparts = null;
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        // 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
        boolean isLayer = roleService.isLayer();
		if(isLayer){
  			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
  			map.put("jsDm", role.getJsDm());
  			
  			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			map.put("yhDm", user.getYhDm());
  		} else {
  			map.put("jsDm", "0");
  		}
  		User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
  		map.put("yhDm", user.getYhDm());
        
        boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
        
	     if (null == id) {
	            //如果传过来的id为空的,则从根节点开始取
//	            treeDeparts = departmentService.getLazyDepartmentTree(GlobalConstant.DEPARTMENT_ROOT_BM_DM, layerDeptNum);
	            treeDeparts = departmentService.getDepartmentCardTree(GlobalConstant.DEPARTMENT_ROOT_BM_DM, layerDeptNum);
	            StringBuilder builder = new StringBuilder();
	            builder.append("[");
	            builder.append(new Gson().toJson(treeDeparts));
	            builder.append("]");
	            printHttpServletResponse(builder.toString());
	        } else {
	            //否则从id为传下来的id 的结点开始取
//	        List<Tree>  treeDeparts1 = departmentService.getDepartmentTreeList(id, layerDeptNum);
	        treeDeparts = departmentService.getDepartmentCardTreeList(id, layerDeptNum);
	        StringBuilder builder = new StringBuilder();
            builder.append("[");
            builder.append(new Gson().toJson(treeDeparts));
            builder.append("]");
            printHttpServletResponse(builder.toString());
	        }
    }*/
    
	/**
	 * 部门员工卡树
	 * by gengji.yang
	 */
    /**
     * 部门员工tree
     */
    public void getDepartmentCardTree() {
        HttpServletRequest request = getHttpServletRequest();
        String id = request.getParameter("id");
        String flag = request.getParameter("flag");
        Tree treeDeparts = null;
        
        /*Map<String, Object> map = new HashMap<String, Object>();
        
        // 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
        boolean isLayer = roleService.isLayer();
		if(isLayer){
  			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
  			map.put("jsDm", role.getJsDm());
  			
  			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			map.put("yhDm", user.getYhDm());
  		} else {
  			map.put("jsDm", "0");
  		}
  		User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
  		map.put("yhDm", user.getYhDm());*/
        
        boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
        
        if (null == id) {
            //如果传过来的id为空的,则从根节点开始取
            treeDeparts = departmentService.getDepartmentCardTree(GlobalConstant.DEPARTMENT_ROOT_BM_DM, layerDeptNum);
            StringBuilder builder = new StringBuilder();

            builder.append("[");
            builder.append(new Gson().toJson(treeDeparts));
            builder.append("]");
            printHttpServletResponse(builder.toString());
        } else {
        	List<Tree>	treeList=null;
            //否则从id为传下来的id 的结点开始取
        	if(flag!=null&&"0".equals(flag)){//部门
	        	treeList = departmentService.getDepartmentCardTreeList(id, layerDeptNum);
        	}else{
        		treeList = departmentService.getDepartmentCardTreeListA(id);
        	}
        	printHttpServletResponse(new Gson().toJson(treeList));
        }
    }
    
	/**
	 * 查询组织权限
	 * 组织权限管理页面中点击组织树动态查询
	 * by gengji.yang
	 */
	public void getOrgAuth(){
		HttpServletRequest request=getHttpServletRequest();
 		String data=request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		Map map=new HashMap();
		if("".equals(data)||null==data){
			map.put("deptStr","\'"+"\'");
		}else{
			map.put("deptStr",data);
		}
		map.put("skip",skip);
		map.put("rows",rows);
		Grid grid=peopleAuthorityInfoService.getOrgAuth(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 查询组织权限，用于新增组织权限时设备树的反选
	 * by gengji.yang
	 */
	public void getOrgAuthForTree(){
		HttpServletRequest request=getHttpServletRequest();
 		String data=request.getParameter("data");
 		Map map=new HashMap();
 		map.put("deptStr",data);
 		List<Map> resultList=peopleAuthorityInfoService.getOrgAuthForTree(map);
 		printHttpServletResponse(GsonUtil.toJson(resultList));
	}
	
	/**
	 * 独立出来的新增组织权限
	 * by gengji.yang
	 */
	public void newAddGroupTask(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String deviceDoorArr=request.getParameter("deviceDoorArr");
		String groupIds=request.getParameter("groupIds");
		Gson g=new Gson();
		List<Map> deviceDoorList=g.fromJson(deviceDoorArr, new ArrayList<LinkedTreeMap>().getClass());
		List<String> groupList=g.fromJson(groupIds, new ArrayList<String>().getClass());
		List<Map> card1List=peopleAuthorityInfoService.getAllCards(groupList);
		List<Map> authList=new ArrayList<Map>();
		JsonResult result=new JsonResult();
		try{
			for(Map cardMap:card1List){
				for(Map deviceDoorMap:deviceDoorList){
					//----------  数据库操作准备开始  -----------------
					Map m=new HashMap();
					m.put("cardNum",cardMap.get("cardNum"));
					m.put("deviceNum",  deviceDoorMap.get("deviceNum"));
					m.put("doorNum", deviceDoorMap.get("doorNum"));
					m.put("deviceMac", deviceDoorMap.get("mac"));
					m.put("deviceType", deviceDoorMap.get("deviceType"));
					m.put("startTime", deviceDoorMap.get("startTime"));
					m.put("endTime", deviceDoorMap.get("endTime"));
					m.put("groupNum", deviceDoorMap.get("groupNum"));
					authList.add(m);
					//----------  数据库操作准备结束  -----------------
				}
			}
			if(deviceDoorList.size()>0){
				saveOrganAuth(groupList,deviceDoorList);
			}
			if(authList.size()>0){
				updateAuthState(0,authList,login_user);
				peopleAuthorityInfoService.addAuthTasks(authList,0);
			}
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 复制组织权限前的判断
	 * by gengji.yang
	 */
	public void beforeCpyGTask(){
		HttpServletRequest request=getHttpServletRequest();
		String srcGroupStr=request.getParameter("groupIdsA");
		List<Map> deviceDoorList=peopleAuthorityInfoService.getSrcOrgAuth(srcGroupStr);
		JsonResult result=new JsonResult();
		try{
			if(deviceDoorList.size()>0){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 独立出来的复制组织权限
	 * by gengji.yang
	 */
	public void newCpyGroupTask(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String srcGroupStr=request.getParameter("srcGroupStr");
		String decGroupIds=request.getParameter("decGroupIds");
		Gson g=new Gson();
		List<String> decGroupList=g.fromJson(decGroupIds, new ArrayList<String>().getClass());
		List<Map> card1List=peopleAuthorityInfoService.getAllCards(decGroupList);
		List<Map> deviceDoorList=peopleAuthorityInfoService.getSrcOrgAuth(srcGroupStr);
		List<Map> authList=new ArrayList<Map>();
		JsonResult result=new JsonResult();
		try{
			for(Map cardMap:card1List){
				for(Map deviceDoorMap:deviceDoorList){
					Map m=new HashMap();
					//----------  数据库操作准备开始  -----------------
					deviceDoorMap.put("cardNum",cardMap.get("cardNum"));
					m.putAll(deviceDoorMap);
					authList.add(m);
					//----------  数据库操作准备结束  -----------------
					
				}
			}
			if(deviceDoorList.size()>0){
				saveOrganAuth(decGroupList,deviceDoorList);
			}
			if(authList.size()>0){
				updateAuthState(0,authList,login_user);
				peopleAuthorityInfoService.addAuthTasks(authList,0);
			}
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 独立出来的删除组织权限
	 * by gengji.yang
	 */
	public void newDelGroupTask(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		String groupIds=request.getParameter("groupIds");
		Gson g=new Gson();
		List<Map> deviceDoorList=g.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
		List<String> groupList=g.fromJson(groupIds, new ArrayList<String>().getClass());
		List<Map> card1List=peopleAuthorityInfoService.getAllCards(groupList);
		List<Map> authList=new ArrayList<Map>();
		JsonResult result=new JsonResult();
		try{
			for(Map cardMap:card1List){
				for(Map deviceDoorMap:deviceDoorList){
					//----------  数据库操作准备开始  -----------------
					Map authMap = new HashMap();	//此处不可直接使用deviceDoorMap，需重定义一个新的map，否则map中的值会受到动态影响	by yuman.gao
					authMap.putAll(deviceDoorMap);
					authMap.put("cardNum",cardMap.get("cardNum"));
					authList.add(authMap);
//					deviceDoorMap.put("cardNum",cardMap.get("cardNum"));
//					authList.add(deviceDoorMap);
					//----------  数据库操作准备结束  -----------------
				}
			}
			if(deviceDoorList.size()>0){
				delOrganAuth(groupList,deviceDoorList);
			}
			if(authList.size()>0){
				peopleAuthorityInfoService.addAuthTasks(authList,1);
			}
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 独立出来的保存组织权限
	 * by gengji.yang
	 */
	public void newSaveGroupTask(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String data=request.getParameter("data");
		String groupIds=request.getParameter("groupIds");
		Gson g=new Gson();
		List<Map> deviceDoorList=g.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
		List<String> groupList=g.fromJson(groupIds, new ArrayList<String>().getClass());
		List<Map> card1List=peopleAuthorityInfoService.getAllCards(groupList);
		List<Map> authList=new ArrayList<Map>();
		JsonResult result=new JsonResult();
		try{
			for(Map cardMap:card1List){
				for(Map deviceDoorMap:deviceDoorList){
					//----------  数据库操作准备开始  -----------------
					Map authMap = new HashMap();	//此处不可直接使用deviceDoorMap，需重定义一个新的map，否则map中的值会受到动态影响	by yuman.gao
					authMap.putAll(deviceDoorMap);
					authMap.put("cardNum",cardMap.get("cardNum"));
					authList.add(authMap);
//					deviceDoorMap.put("cardNum",cardMap.get("cardNum"));
//					authList.add(deviceDoorMap);
					//----------  数据库操作准备结束  -----------------
				}
			}
			if(deviceDoorList.size()>0){
				saveOrganAuth(groupList,deviceDoorList);
			}
			if(authList.size()>0){
				updateAuthState(0,authList,login_user);
				peopleAuthorityInfoService.addAuthTasks(authList,0);
			}
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除门权限 
	 * by gengji.yang
	 */
	public void delDorAuth(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String data=request.getParameter("data");
		Gson g=new Gson();
		List<Map> authList=g.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
		JsonResult result=new JsonResult();
		try{
			updateAuthState(1,authList,login_user);
			peopleAuthorityInfoService.addAuthTasks(authList,1);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 保存门权限
	 * by gengji.yang
	 */
	public void saveDorAuth(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String data=request.getParameter("data");
		Gson g=new Gson();
		List<Map> authList=g.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
		JsonResult result=new JsonResult();
		try{
			updateAuthState(0,authList,login_user);
			peopleAuthorityInfoService.addAuthTasks(authList,0);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
}

