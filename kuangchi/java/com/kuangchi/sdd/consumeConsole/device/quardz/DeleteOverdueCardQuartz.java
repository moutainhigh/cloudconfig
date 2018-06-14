package com.kuangchi.sdd.consumeConsole.device.quardz;

import java.util.List;

import javax.annotation.Resource;

import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;

/**
 * 消费机定时下发过期卡片名单(实际上是删除名单)
 * 
 * @author xuewen.deng
 * 
 */
public class DeleteOverdueCardQuartz {

	private ICronService cronService;
	@Resource(name = "cDeviceService")
	// 消费service
	IDeviceService deviceService;
	@Resource(name = "cardServiceImpl")
	private ICardService cardService;

	/*
	 * @Resource(name = "cDeviceService") IDeviceService deviceService;
	 * 
	 * @Resource(name = "deviceStateServiceImpl") private DeviceStateService
	 * deviceStateService;
	 */

	public ICronService getCronService() {
		return cronService;
	}

	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}

	/*
	 * private XfCommService xfCommService;
	 * 
	 * public XfCommService getXfCommService() { return xfCommService; }
	 * 
	 * public void setXfCommService(XfCommService xfCommService) {
	 * this.xfCommService = xfCommService; }
	 */

	/**
	 * 消费机下发过期卡片名单（删除名单）
	 * 
	 * @author xuewen.deng
	 */
	public void sendOverdueCard() {
		try {
			// 集群访问时，只有与数据库中相同的IP地址可以执行定时器的业务操作
			boolean r = cronService.compareIP();
			if (r) {
				// 获取所有设备
				List<String> devNumList = deviceService.getAllDevNum();
				// 获取所有过期的卡片
				List<String> overdueCardNumList = cardService
						.getOverdueCardNumber();
				for (String devNum : devNumList) {
					List<String> devCardList = deviceService
							.getNameListByDevice(devNum);// 获取消费设备当前已经存在（已下发）的卡片名单List
					for (String card_num : overdueCardNumList) {
						// 删除所有消费设备上的名单
						int existFlag = 1;// 是否存在标志(1不存在,2存在)
						for (String existCardNum : devCardList) {// 遍历消费设备所有存在的卡片，若已经删除的就不用再删，若还没删除则进行删除
							if (card_num.equals(existCardNum)) {
								existFlag = 2;// 表示存在
								break;
							}
						}
						if (existFlag == 2) {// 若名单已经存在，则删除名单
							deviceService.insertNameTask(devNum, card_num, "1",
									"9");
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
