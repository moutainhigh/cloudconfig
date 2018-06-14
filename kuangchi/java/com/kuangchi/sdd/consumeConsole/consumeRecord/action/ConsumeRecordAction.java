package com.kuangchi.sdd.consumeConsole.consumeRecord.action;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.fop.datatypes.Numeric;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.attendanceConsole.attend.model.forgetcheckModel;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.count.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;
import com.kuangchi.sdd.consumeConsole.consumeRecord.service.IConsumeRecordService;
import com.kuangchi.sdd.consumeConsole.consumeRecord.service.impl.ConsumeRecordServiceImpl;
import com.kuangchi.sdd.consumeConsole.deviceType.model.DeviceType;

import com.kuangchi.sdd.util.algorithm.MD5;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("ConsumeRecordAction")
public class ConsumeRecordAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(ConsumeRecordAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "ConsumeRecordServiceImpl")
	private IConsumeRecordService consumeRecordService;
	
	private ConsumeRecord model;
	public ConsumeRecordAction(){
		model=new ConsumeRecord();
	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//跳转到门信息主页面
	public String toMyConsume(){
		return "success";
	}
	
	//查询全部信息
	public void getSelConsumeRecord(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		ConsumeRecord consume_record=GsonUtil.toBean(data,ConsumeRecord.class);
		Grid allDiscount=consumeRecordService.selectAllConsumeRecords(consume_record, page, rows);
		printHttpServletResponse(GsonUtil.toJson(allDiscount));
	
	}
	//导出消费记录
	public void exportConsumeRecord() {
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String data = getHttpServletRequest().getParameter("data");
		ConsumeRecord consume_record=GsonUtil.toBean(data,ConsumeRecord.class);
		List<ConsumeRecord> consumeRecord=consumeRecordService.exportConsumeRecords(consume_record);
		for(int i=0;i<consumeRecord.size();i++){
			if("0".equals(consumeRecord.get(i).getCard_num())){
				consumeRecord.get(i).setCard_num("-");
			}
			if(null==consumeRecord.get(i).getIsCancel()||"".equals(consumeRecord.get(i).getIsCancel())){
				consumeRecord.get(i).setIsCancel("-");
			}
			if(null==consumeRecord.get(i).getRefund()||"".equals(consumeRecord.get(i).getRefund())){
				consumeRecord.get(i).setRefund("-");
			}
		}
		String jsonList = GsonUtil.toJson(consumeRecord);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					
					colTitleList.add("消费时间");
					colTitleList.add("记录流水号");
					colTitleList.add("收支类型");
					colTitleList.add("退款状态");
					colTitleList.add("撤销状态");
					colTitleList.add("餐次名称");
					colTitleList.add("卡号");
					colTitleList.add("账户");
					
					colTitleList.add("收入金额(元)");
					colTitleList.add("支出金额(元)");
					colTitleList.add("余额(元)");
					colTitleList.add("部门名称");
					colTitleList.add("员工工号");
					colTitleList.add("员工名称");
					colTitleList.add("商品名称");
					colTitleList.add("商品类型名称");
					
					colTitleList.add("消费组名称");
					colTitleList.add("设备编号");
					colTitleList.add("设备名称");
					colTitleList.add("创建时间");
					colTitleList.add("卡流水号");
					colTitleList.add("备注");
					
					colList.add("consume_time");
					colList.add("record_flow_num");
					colList.add("type");
					colList.add("refund");
					colList.add("isCancel");
					colList.add("meal_name");
					colList.add("card_num");
					colList.add("account_num");
					colList.add("inbound");
					colList.add("outbound");
					colList.add("balance");
					colList.add("dept_name");
					colList.add("staff_no");
					colList.add("staff_name");
					colList.add("good_name");
					colList.add("good_type_name");
				
					colList.add("group_name");
					colList.add("device_num");
					colList.add("device_name");
					colList.add("create_time");
					colList.add("card_flow_no");
					colList.add("remark");
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
						String fileName="消费记录统计表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtilSpecialCount.exportExcel("消费记录统计表", colTitles, cols, list);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
	
	/**
	 * 电子地图显示最新3条记录
	 * @author minting.he
	 */
	public void getNewRecords(){
		List<ConsumeRecord> list = new ArrayList<ConsumeRecord>();
		list = consumeRecordService.getNewRecords();
    	printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-2 下午2:48:44
	 * @功能描述: 密码验证
	 */
	public void validPassword(){
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String yhDm = loginUser.getYhDm();
		String password = request.getParameter("password");
		String md5Password=MD5.getInstance().encryption(password);
		Integer user = consumeRecordService.getUser(yhDm, md5Password);
		if(user > 0){
			printHttpServletResponse(GsonUtil.toJson(true));
		} else {
			printHttpServletResponse(GsonUtil.toJson(false));
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-2 下午6:34:29
	 * @功能描述: 退款
	 * @参数描述:
	 */
	public void refund(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String[] recordFlowNums = request.getParameter("record_flow_nums").split(",");
		for (String recordFlowNum : recordFlowNums) {
			ConsumeRecord consumeRecord = consumeRecordService.getRecordByNum(recordFlowNum);
			boolean refundResult = consumeRecordService.refund(consumeRecord, "TK");
			if(refundResult){
				result.setMsg("退款成功");
				result.setSuccess(true);
			} else {
				result.setMsg("退款失败");
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-2 下午6:34:29
	 * @功能描述: 撤销消费
	 * @参数描述:
	 */
	public void cancelConsume(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String[] recordFlowNums = request.getParameter("record_flow_nums").split(",");
		for (String recordFlowNum : recordFlowNums) {
			ConsumeRecord consumeRecord = consumeRecordService.getRecordByNum(recordFlowNum);
			boolean refundResult = consumeRecordService.refund(consumeRecord, "CX");
			if(refundResult){
				result.setMsg("撤销成功");
				result.setSuccess(true);
			} else {
				result.setMsg("撤销失败");
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * 查看是否有已销户的账户
	 * @author minting.he
	 */
	public void ifCancelAccount(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String account_num = request.getParameter("account_num");
		if(EmptyUtil.atLeastOneIsEmpty(account_num)){
			result.setSuccess(false);
			result.setMsg(1);
		}else {
			Integer count = consumeRecordService.ifCancelAccount(account_num);
			result.setSuccess(true);
			result.setMsg(count);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
		
}