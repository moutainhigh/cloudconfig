package com.kuangchi.sdd.consum.action;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.consum.bean.ParameterDownResponse;
import com.kuangchi.sdd.consum.bean.ParameterUpResponse;
import com.kuangchi.sdd.consum.bean.ResultMsg;
import com.kuangchi.sdd.consum.bean.ResultMsg2;
import com.kuangchi.sdd.consum.bean.SinglePersonCard;
import com.kuangchi.sdd.consum.businessBean.UpToDownBusiness;
import com.kuangchi.sdd.consum.container.ConnectorFactory;
import com.kuangchi.sdd.consum.container.ParameterPool;
import com.kuangchi.sdd.consum.handler.serverHandler.ping.CheckOnLineThread;
import com.kuangchi.sdd.consum.handler.serverHandler.ping.SendPersonDownThread;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("comsumDeviceAction")
public class ComsumDeviceAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2561333382202397836L;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getParamByDevNum() {
		String DevNum = getHttpServletRequest().getParameter("DevNum");
		String req = getHttpServletRequest().getParameter("req");

		UpToDownBusiness upToDownBusiness = new UpToDownBusiness();
		ResultMsg rm = new ResultMsg();
		if ("1".equals(req)) {
			Date currDate = new Date();
			if ((currDate.getTime() - (ConnectorFactory.lastOperateTimeMap
					.get(DevNum) == null ? 1
					: ConnectorFactory.lastOperateTimeMap.get(DevNum).getTime())) > 30000) {
				try {
					upToDownBusiness.sendParameterRequest(DevNum);// 请求设备，将拿到的结果放在ParameterPool中
					rm.setResult_code("0");
					rm.setResult_msg("设备在线，获取消费终端参数成功");
				} catch (Exception e) {
					e.printStackTrace();
					rm.setResult_code("1");
					rm.setResult_msg("设备不在线，获取消费终端参数失败");
				}
			} else {
				rm.setResult_code("1");
				rm.setResult_msg("设备处于忙碌状态，请稍后再试");
			}
			printHttpServletResponse(new Gson().toJson(rm));
		} else {
			try {
				Thread.sleep(3000);
				ParameterUpResponse paramUp = ParameterPool
						.getParameter(DevNum);

				if (paramUp == null) {
					rm.setResult_code("1");
					rm.setResult_msg("获取消费终端参数失败,请重试");
				} else {

					rm.setParameterUpResponse(paramUp);
					rm.setResult_code("0");
					rm.setResult_msg("获取消费终端参数成功");
				}
			} catch (Exception e) {
				rm.setResult_code("1");
				rm.setResult_msg("获取消费终端参数失败");
				e.printStackTrace();
			}

			printHttpServletResponse(new Gson().toJson(rm));
		}
	}

	public void setTerminalParameter() {
		String param = getHttpServletRequest().getParameter("paramDown");
		ResultMsg result = new ResultMsg();
		try {
			ParameterDownResponse paramDown = GsonUtil.toBean(param,
					ParameterDownResponse.class);
			paramDown.setHeader(0x0a09);
			UpToDownBusiness upToDownBusiness = new UpToDownBusiness();
			upToDownBusiness.setTerminalParameter(paramDown);
			result.setResult_code("0");
			result.setResult_msg("设置消费参数成功");
		} catch (Exception e) {
			result.setResult_code("1");
			result.setResult_msg("设置消费参数失败");
			e.printStackTrace();
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	public void applyPersonCard() {// 下发名单
		/*
		 * String machine = getHttpServletRequest().getParameter("machine");//
		 * 将要下发的机器编号 UpToDownBusiness upToDownBusiness = new UpToDownBusiness();
		 * ResultMsg result = new ResultMsg(); try {
		 * upToDownBusiness.sendPersonCardDown(machine);
		 * 
		 * // 将设备改为忙碌状态 ResultMsg2 resultMsg = new ResultMsg2(); try { String
		 * consumUrl = PropertiesToMap.propertyToMap(
		 * "photoncard_interface.properties").get("url") +
		 * "interface/deviceState/modifyBusyState.do";
		 * 
		 * String str = HttpRequest.sendPost(consumUrl, "deviceNum=" + machine +
		 * "&busyState=1");// 调用一卡通的方法将设备改为忙碌状态 resultMsg = GsonUtil.toBean(str,
		 * ResultMsg2.class); System.out.println("+++++++++++++++" +
		 * resultMsg.getMsg()); } catch (Exception e) {
		 * System.out.println("+++++++++++++++" + resultMsg.getMsg());
		 * e.printStackTrace(); } result.setResult_code("0");
		 * result.setResult_msg(machine); } catch (Exception e) {
		 * e.printStackTrace(); result.setResult_code("1");
		 * result.setResult_msg(machine); }
		 * printHttpServletResponse(GsonUtil.toJson(result));
		 */
		ResultMsg2 resultMsg = new ResultMsg2();
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		try {
			SinglePersonCard singlePersonCard = GsonUtil.toBean(data,
					SinglePersonCard.class);

			UpToDownBusiness upToDownBusiness = new UpToDownBusiness();
			// singlePersonCard.setMachine(8844);
			// singlePersonCard.setCardNum(Integer.parseInt("1003")); //
			// 卡号
			// singlePersonCard.setPersonNum(123);
			singlePersonCard.setLostFlowNum(0x00);
			// singlePersonCard.setFlag(0xff); // 字符串 00 表示删除 ff表示有效
			// singlePersonCard.setDeposit(1000);
			singlePersonCard.setRetain(0x00);
			// singlePersonCard.setNum("123456789"); // 工号
			// singlePersonCard.setName("小明"); // 姓名

			upToDownBusiness.sendSinglePersonCardDown(singlePersonCard);
			resultMsg.setResult("0");
			printHttpServletResponse(GsonUtil.toJson(resultMsg));
		} catch (Exception e) {

			resultMsg.setResult("1");
			printHttpServletResponse(GsonUtil.toJson(resultMsg));
			e.printStackTrace();
		}
	}

	/**
	 * 检测消费设备是否在线
	 * 
	 * @author xuewen.deng
	 */
	public void checkOnLineT() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		try {
			CheckOnLineThread checkOnLineTh = new CheckOnLineThread();
			checkOnLineTh.start();
			Date heartBeatTime = null;
			String machine = null;
			Date now = new Date();
			for (Map.Entry<String, Date> entry : ConnectorFactory.channelHandlerContextTimeMap
					.entrySet()) {
				machine = entry.getKey();
				if (device_num != null && device_num.equals(machine)) {
					heartBeatTime = ConnectorFactory.channelHandlerContextTimeMap
							.get(machine);// 上一次心跳时间
					break;
				}
			}
			if (heartBeatTime != null
					&& ConnectorFactory.getConnection(machine) != null) {
				Long interval = (now.getTime() - heartBeatTime.getTime()) / 1000;
				if (interval > 16) {// 如果大于10秒
					printHttpServletResponse(1);// 离线
				} else {
					printHttpServletResponse(0);// 在线
				}
			} else {
				printHttpServletResponse(1);// 离线
			}

		} catch (Exception e) {
			printHttpServletResponse(1);
			e.printStackTrace();
		}
	}

	/**
	 * 下发名单线程
	 * 
	 * @author xuewen.deng
	 */
	public void SendPersonDownT() {
		try {
			SendPersonDownThread sendPersonDownTh = new SendPersonDownThread();
			sendPersonDownTh.start();
			printHttpServletResponse(0);
		} catch (Exception e) {
			printHttpServletResponse(1);
			e.printStackTrace();
		}
	}
}
