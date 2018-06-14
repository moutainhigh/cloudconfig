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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.event.service.EventService;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorRecordInfo;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.service.ElevatorRecordReportService;
import com.kuangchi.sdd.elevatorConsole.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.interfaceConsole.JedisCache.service.impl.JedisCache;
import com.kuangchi.sdd.util.cacheUtils.CacheUtils;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.datastructure.BoundedQueueUsedForDisplay;
import com.kuangchi.sdd.util.file.DownloadFile;
@Controller("elevatorRecordReportAction")
public class ElevatorRecordReportAction extends BaseActionSupport{

	@Resource(name="elevatorRecordReportServiceImpl")
	private ElevatorRecordReportService elevatorRecordReportService;
	
	@Autowired
	EventService eventService;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取电梯刷卡信息报表-Action
	 */
	public void getElevatorRecordinfo(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<ElevatorRecordInfo> grid = new Grid<ElevatorRecordInfo>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		grid = elevatorRecordReportService.getElevatorRecordinfo(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27 
	 * @功能描述: 导出电梯刷卡信息-Action
	 */
		public void exportElevatorRecordinfo() {
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("exportData");
			Map map = GsonUtil.toBean(beanObject, HashMap.class);
			List<ElevatorRecordInfo> elevatorRecordInfo=elevatorRecordReportService.exportElevatorRecordinfo(map);
			for(ElevatorRecordInfo recordInfo:elevatorRecordInfo){
				if(null==recordInfo.getStaff_name()){
					recordInfo.setStaff_name("-");
				}
				if(null==recordInfo.getCard_num()){
					recordInfo.setCard_num("-");
				}
				
				//刷卡标记
					if("2".equals(recordInfo.getCard_type_name()) ||"11".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("用户卡");
					}else if("3".equals(recordInfo.getCard_type_name())||"12".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("接待卡");
					}else if("4".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("计次卡");
					}else if("21".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("有效用户密码");
					}else if("22".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("有效接待密码");
					}else if("15".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("收费卡");
					}else if("25".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("有效收费密码");
					}else if("51".equals(recordInfo.getCard_type_name())||"91".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("无权限卡");
					}else if("61".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("无权限用户密码");
					}else if("52".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("无权限接待卡");
					}else if("62".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("无权限接待密码");
					}else if("55".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("无权限收费卡");
					}else if("65".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("无权限收费密码");
					}else if("92".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("无效密码");
					}else if("93".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("过期卡");
					}else if("94".equals(recordInfo.getCard_type_name())){
						recordInfo.setCard_type_name("过期密码");
					}else {
						recordInfo.setCard_type_name("-");
					}
				
				//电梯状态
					if("00".equals(recordInfo.getFloor_state())){
						recordInfo.setFloor_state("正常");
					}else if("01".equals(recordInfo.getFloor_state())){
						recordInfo.setFloor_state("异常");
					}else if("b0".equals(recordInfo.getFloor_state())){
						recordInfo.setFloor_state("读卡器通讯正常");
					}else if("b1".equals(recordInfo.getFloor_state())){
						recordInfo.setFloor_state("梯控输入电压欠压");
					}else if("b2".equals(recordInfo.getFloor_state())){
						recordInfo.setFloor_state("继电器驱动电压");
					}else if("b3".equals(recordInfo.getFloor_state())||"b4".equals(recordInfo.getFloor_state())||
							 "b5".equals(recordInfo.getFloor_state())||"b6".equals(recordInfo.getFloor_state())||
							"b7".equals(recordInfo.getFloor_state())){
						recordInfo.setFloor_state("预留");
					}else{
						recordInfo.setFloor_state("-");
					}
					
					//设备动作
					if("0".equals(recordInfo.getEvent_type())){
						recordInfo.setCheck_type("-");
					}else if("1".equals(recordInfo.getEvent_type())){
							if("01".equals(recordInfo.getCheck_type())){
								recordInfo.setCheck_type("消防联动触发");
							}else if("81".equals(recordInfo.getCheck_type())){
								recordInfo.setCheck_type("消防联动结束");
							}else if("02".equals(recordInfo.getCheck_type())){
								recordInfo.setCheck_type("楼层开放启动");
							}else if("82".equals(recordInfo.getCheck_type())){
								recordInfo.setCheck_type("楼层开放结束");
							}else if("03".equals(recordInfo.getCheck_type())){
								recordInfo.setCheck_type("梯控异常机制启动");
							}else if("83".equals(recordInfo.getCheck_type())){
								recordInfo.setCheck_type("梯控异常机制结束");
							}else if("04".equals(recordInfo.getCheck_type())){
								recordInfo.setCheck_type("对讲联动开门");
							}else{
								recordInfo.setCheck_type("-");
							}
					}
				//楼层名称
					if("1".equals(recordInfo.getEvent_type())){
						if(null==recordInfo.getFloor_name()||"".equals(recordInfo.getFloor_name())){
							recordInfo.setFloor_name("全部楼层");
						}
					}else if(null==recordInfo.getFloor_name()||"".equals(recordInfo.getFloor_name())){
						recordInfo.setFloor_name("-");
					}
				//事件类型
				if("0".equals(recordInfo.getEvent_type())){
					recordInfo.setEvent_type("刷卡事件");
				}else if("1".equals(recordInfo.getEvent_type())){
					recordInfo.setEvent_type("触发事件");
				} 
				//卡类型
				if(null==recordInfo.getType_name()||"".equals(recordInfo.getType_name())){
					recordInfo.setType_name("-");
				}
				
				
				
			}
			
			String jsonList = GsonUtil.toJson(elevatorRecordInfo);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						
						colTitleList.add("部门名称");
						colTitleList.add("员工工号");
						colTitleList.add("员工名称");
						colTitleList.add("卡号");
						colTitleList.add("卡类型");
						colTitleList.add("卡标记");
						colTitleList.add("设备名称");
						colTitleList.add("设备IP地址");
						colTitleList.add("事件类型");
						colTitleList.add("刷卡时间");
						colTitleList.add("MAC地址");
					/*	colTitleList.add("楼层名称");
						colTitleList.add("设备动作");
						colTitleList.add("设置状态");
						*/
						colList.add("BM_MC");
						colList.add("staff_no");
						colList.add("staff_name");
						colList.add("card_num");
						colList.add("type_name");
						colList.add("card_type_name");
						colList.add("device_name");
						colList.add("device_ip");
						colList.add("event_type");
						colList.add("check_time");
						colList.add("mac");
						/*colList.add("floor_name");
						colList.add("check_type");
						colList.add("floor_state");
						*/
						
						
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
							String fileName="电梯刷卡信息.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecialCount.exportExcel("电梯刷卡信息", colTitles, cols, list);
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
		
		@Value("${redisConnectIp}")
		private String redisConnectIp;
		@Value("${redisConnectPort}")
		private String redisConnectPort;
		
		/**
		 * 从缓存中读取最新的刷卡记录
		 * @author minting.he
		 */
		public void latestElevatorRecord(){
			List<Object> list = new ArrayList<Object>();
			CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(redisConnectIp, Integer.valueOf(redisConnectPort));
			BoundedQueueUsedForDisplay l = cacheQueue.getObject("eleSysLatest");
			if(l==null){
				l = new BoundedQueueUsedForDisplay(5);
				cacheQueue.saveObject("eleSysLatest", l);
			}
			list = l.list();
			for(Object obj : list){
				Map<String, Object> map = (Map<String, Object>) obj;
				if("0".equals(map.get("event_type").toString())){
					map.put("event_type", "刷卡事件");
					if(map.get("floor_num")==null || "".equals(map.get("floor_num").toString())){	
						map.put("floor_num", "-");
					}
				}else {
					map.put("event_type", "触发事件");
					if(map.get("floor_num")==null || "".equals(map.get("floor_num").toString())){	//电梯开放
						map.put("floor_num", "全部楼层");
					}
				}
				if(map.get("card_num")==null || "".equals(map.get("card_num").toString())){	
					map.put("staff_no", "-");
					map.put("staff_name", "-");
					map.put("card_num", "-");
				}else if(map.get("staff_no")==null || "".equals(map.get("staff_no").toString())){	
					map.put("staff_no", "-");
					map.put("staff_name", "-");
				}
				if(map.get("card_num")!=null && !"".equals(map.get("card_num").toString())){
					String staff_img = eventService.getStaffImg(map.get("card_num").toString());
					map.put("staff_img", staff_img);
				}
			}
	    	printHttpServletResponse(GsonUtil.toJson(list));
			
		/*	
		  	List<ElevatorRecordInfo> list = new ArrayList<ElevatorRecordInfo>();
			list = elevatorRecordReportService.latestElevatorRecord();
			for(ElevatorRecordInfo r : list){
				if("0".equals(r.getEvent_type())){
					r.setEvent_type("刷卡事件");
					if("".equals(r.getFloor_num()) || r.getFloor_num()==null){	
						r.setFloor_num("-");
					}
				}else {
					r.setEvent_type("触发事件");
					if("".equals(r.getFloor_num()) || r.getFloor_num()==null){	//电梯开放
						r.setFloor_num("全部楼层");
					}
				}
				if("".equals(r.getCard_num()) || r.getCard_num()==null){
					r.setStaff_no("-");
					r.setStaff_name("-");
					r.setCard_num("-");
				}else if("".equals(r.getStaff_no()) || r.getStaff_no()==null) {
					r.setStaff_no("-");
					r.setStaff_name("-");
				}
			}
	    	printHttpServletResponse(GsonUtil.toJson(list));
	    */
		}
		
	
}
