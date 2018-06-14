package com.kuangchi.sdd.consum.action;

import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.time.model.ResultMsg;
import com.kuangchi.sdd.consum.bean.DataDown;
import com.kuangchi.sdd.consum.bean.ParameterDownResponse;
import com.kuangchi.sdd.consum.bean.ResultMsg2;
import com.kuangchi.sdd.consum.bean.SinglePersonCard;
import com.kuangchi.sdd.consum.businessBean.UpToDownBusiness;
import com.kuangchi.sdd.consum.container.ConnectorFactory;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.network.HttpRequest;

@Controller("testAction")
public class TestAction extends BaseActionSupport {

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void doSomething() {
		UpToDownBusiness upToDownBusiness = new UpToDownBusiness();
		upToDownBusiness.sendParameterRequest("8998");

		String mac = getHttpServletRequest().getParameter("mac");
		String date = getHttpServletRequest().getParameter("date");
		int i = 0;
		ResultMsg rm = new ResultMsg();
		if (i == 1) {
			rm.setResult_code("1");
			rm.setResult_msg("设置时间失败");
		} else {
			rm.setResult_code("0");
			rm.setResult_msg("设置时间成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}

	public void setTerminalParameter() {
		ParameterDownResponse parameterDownResponse = new ParameterDownResponse();
		parameterDownResponse.setHeader(0x0a09);
		parameterDownResponse.setMachine(Integer.valueOf("8998"));
		DataDown dataDown = new DataDown();

		dataDown.setMoney(1000);
		dataDown.setLimit(2000);
		dataDown.setConfirm(1);
		dataDown.setMultiUse(0);
		dataDown.setPassword(0x000000);
		dataDown.getMeal1()[0] = 6;
		dataDown.getMeal1()[1] = 3;
		dataDown.getMeal1()[2] = 7;
		dataDown.getMeal1()[3] = 3;

		dataDown.getMeal2()[0] = 9;
		dataDown.getMeal2()[1] = 3;
		dataDown.getMeal2()[2] = 10;
		dataDown.getMeal2()[3] = 3;

		dataDown.getMeal3()[0] = 13;
		dataDown.getMeal3()[1] = 3;
		dataDown.getMeal3()[2] = 14;
		dataDown.getMeal3()[3] = 3;

		dataDown.getMeal4()[0] = 19;
		dataDown.getMeal4()[1] = 3;
		dataDown.getMeal4()[2] = 20;
		dataDown.getMeal4()[3] = 3;

		dataDown.setGroupsuport(0);

		dataDown.setOnOffLineWay(0);

		dataDown.setTimeLimit(1);

		parameterDownResponse.setDataDown(dataDown);
		UpToDownBusiness upToDownBusiness = new UpToDownBusiness();
		upToDownBusiness.setTerminalParameter(parameterDownResponse);
	}

	public void applyPersonCard() {
		UpToDownBusiness upToDownBusiness = new UpToDownBusiness();
		upToDownBusiness.sendPersonCardDown("8844");
//		SinglePersonCard singlePersonCard=new SinglePersonCard();

//		singlePersonCard.setMachine(8844);
//		singlePersonCard.setCardNum(Integer.parseInt("1003"));  //卡号  
//		singlePersonCard.setPersonNum(123);
//		singlePersonCard.setLostFlowNum(0x00);
//		singlePersonCard.setFlag(0xff);  //字符串  00 表示删除   ff表示有效
//		singlePersonCard.setDeposit(1000);
//		singlePersonCard.setRetain(0x00);
//		singlePersonCard.setNum("123456789");     //工号
//		singlePersonCard.setName("小明");    //姓名
//		
//		upToDownBusiness.sendSinglePersonCardDown( singlePersonCard);
//		
		
//		for (Map.Entry<String, ChannelHandlerContext> entry : ConnectorFactory.connections
//				.entrySet()) {
//
//			System.out.println(">>>>>>>>>>>>>>>>>"+entry.getValue().pipeline().channel().isActive());
//		}
		
		
	}

}
