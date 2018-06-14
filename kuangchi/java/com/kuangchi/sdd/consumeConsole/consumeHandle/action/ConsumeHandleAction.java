package com.kuangchi.sdd.consumeConsole.consumeHandle.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.ConsumeRecordPack;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DepositDown;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.JsonResult;
import com.kuangchi.sdd.consumeConsole.consumeHandle.service.IConsumeHandleService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("consumeHandleAction")
public class ConsumeHandleAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "consumeHandleServiceImpl")
	private IConsumeHandleService consumeHandleService;

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-21 下午3:13:11
	 * @功能描述: 消费记录上报及处理
	 * @参数描述:
	 */
	public void recordReport(){
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		ConsumeRecordPack recordPack = GsonUtil.toBean(data, ConsumeRecordPack.class);
		JsonResult reportResult = consumeHandleService.recordReport(recordPack);
		printHttpServletResponse(GsonUtil.toJson(reportResult));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-24 下午5:25:58
	 * @功能描述: 下发消费余额
	 * @参数描述:
	 */
	public void issuedBalance(){
		HttpServletRequest request = getHttpServletRequest();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		String cardNum = request.getParameter("card_num");
		String deviceNum = request.getParameter("device_num");
		DepositDown deposit = consumeHandleService.issuedBalance(cardNum, deviceNum);
		
		if (deposit.getAlm() != 0){
			resultMap.put("msg", "下发失败，注意报警信息");
			resultMap.put("success", false);
			resultMap.put("depositDown", deposit);
		} else {
			resultMap.put("msg", "下发成功");
			resultMap.put("success", true);
			resultMap.put("depositDown", deposit);
		}
		printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
}
