package com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.service.DeviceStateService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("CDeviceStateAction")
public class CDeviceStateAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "deviceStateServiceImpl")
	private DeviceStateService deviceStateService;

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-8-31 上午
	 * @功能描述: 修改消费设备忙碌状态-Action
	 */
	public void modifyOnlineState() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceNum = request.getParameter("deviceNum"); // 设备编号
		String onlineState = request.getParameter("onlineState"); // 在线状态 0 离线 1
																	// 在线
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == deviceNum || "".equals(deviceNum.trim())) {
				throw new com.kuangchi.sdd.base.exception.MyException(
						"设备编号不能为空");
			}
			if (null == onlineState || "".equals(onlineState.trim())) {
				throw new com.kuangchi.sdd.base.exception.MyException(
						"在线状态不能为空");
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			resultMap.put("msg", myException.getMessage());
			resultMap.put("result", "1");
			printHttpServletResponse(GsonUtil.toJson(resultMap));
			return;
		}
		boolean flag = deviceStateService.modifyOnlineState(deviceNum,
				Integer.valueOf(onlineState));
		if (flag) {
			resultMap.put("msg", "修改成功");
			resultMap.put("result", "0");
		} else {
			resultMap.put("msg", "修改失败");
			resultMap.put("result", "1");
		}

		printHttpServletResponse(GsonUtil.toJson(resultMap));
	}

	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-8-31 上午
	 * @功能描述: 修改消费设备在线状态-Action
	 */
	public void modifyBusyState() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceNum = request.getParameter("deviceNum"); // 设备编号
		String busyState = request.getParameter("busyState"); // 忙碌状态 0 空闲 1繁忙
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == deviceNum || "".equals(deviceNum.trim())) {
				throw new com.kuangchi.sdd.base.exception.MyException(
						"设备编号不能为空");
			}
			if (null == busyState || "".equals(busyState.trim())) {
				throw new com.kuangchi.sdd.base.exception.MyException(
						"忙绿状态不能为空");
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			resultMap.put("msg", myException.getMessage());
			resultMap.put("result", "1");
			printHttpServletResponse(GsonUtil.toJson(resultMap));
			return;
		}
		boolean flag = deviceStateService.modifyBusyState(deviceNum,
				Integer.valueOf(busyState));
		if (flag) {
			resultMap.put("msg", "修改成功");
			resultMap.put("result", "0");
		} else {
			resultMap.put("msg", "修改失败");
			resultMap.put("result", "1");
		}

		printHttpServletResponse(GsonUtil.toJson(resultMap));
	}

	/**
	 * @创建人　: xuewen.deng
	 * @创建时间: 2016-9-5 下午
	 * @功能描述: 将消费设备改为离线和空闲状态-Action
	 */
	public void initDevState() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = deviceStateService.initDevState();
		if (flag) {
			resultMap.put("msg", "修改成功");
			resultMap.put("result", "0");
		} else {
			resultMap.put("msg", "修改失败");
			resultMap.put("result", "1");
		}
		printHttpServletResponse(GsonUtil.toJson(resultMap));
	}

}
