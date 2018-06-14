package com.kuangchi.sdd.consum.handler.serverHandler.ping;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.consum.bean.PersonCardTask;
import com.kuangchi.sdd.consum.bean.SinglePersonCard;
import com.kuangchi.sdd.consum.businessBean.UpToDownBusiness;
import com.kuangchi.sdd.consum.container.ConnectorFactory;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.network.HttpRequest;

public class SendPersonDownThread extends Thread {
	public static PersonCardTask currentPersonCardTask = null;
	public static Semaphore semaphore = new Semaphore(1); // 单线程锁，为了保证名单下发后等到有结果返回或等待一段时间结果还不返回才下发第二条名单
	public static Semaphore semaphore2 = new Semaphore(1); // 单线程锁，为了保证名单下发后等到有结果返回或等待一段时间结果还不返回才下发第二条名单
	public static Boolean LockFlag = false; // 锁标志，当一次任务（5条记录）下发完成之后才为true

	@Override
	public synchronized void run() {

		// 比较ip是否是服务器ip
		// 获取请求的服务器ip
		// Enumeration<NetworkInterface> allNetInterfaces;
		try {
			/*
			 * allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			 * InetAddress ip = null; List<String> arr = new
			 * ArrayList<String>(); while (allNetInterfaces.hasMoreElements()) {
			 * NetworkInterface netInterface = (NetworkInterface)
			 * allNetInterfaces .nextElement(); //
			 * System.out.println(netInterface.getName()); //网关 Enumeration
			 * addresses = netInterface.getInetAddresses(); while
			 * (addresses.hasMoreElements()) { ip = (InetAddress)
			 * addresses.nextElement(); if (ip != null && ip instanceof
			 * Inet4Address) { arr.add("'" + ip.getHostAddress() + "'"); } } }
			 * String str = arr.toString().substring(1, arr.toString().length()
			 * - 1); Cron cron = new Cron(); cron.setSys_value(str);
			 * 
			 * String compareServerIP = PropertiesToMap.propertyToMap(
			 * "photoncard_interface.properties").get("url") +
			 * "interface/cDevice/compareIP.do"; String photoReturnCount =
			 * HttpRequest.sendPost(compareServerIP, "cron=" +
			 * GsonUtil.toJson(cron));
			 * 
			 * if (photoReturnCount != null && Integer.valueOf(photoReturnCount)
			 * >= 1) {
			 */
			// try {
			// IP相同
			// 下发名单线程
			// System.out.println("~~~~~~本机ip是服务器ip");
			boolean r = semaphore2.tryAcquire(26, TimeUnit.SECONDS); // 获取信号，可以等待：5秒*5+1=26秒
			if (r) {
				// 循环调用单个下发名单接口
				// 获取<=5条名单
				// 下发
				String consumUrl = PropertiesToMap.propertyToMap(
						"photoncard_interface.properties").get("url")
						+ "interface/cDevice/getSomeTask.do";
				String photoReturnstr = HttpRequest.sendPost(consumUrl, "1");// 调用一卡通的方法获取下发名单信息
				Gson gson = new Gson();
				List<PersonCardTask> taskList = gson.fromJson(photoReturnstr,
						new TypeToken<List<PersonCardTask>>() {
						}.getType());
				if (taskList != null && taskList.size() > 0) {
					SendPersonDownThread.LockFlag = false;
					for (PersonCardTask personCardTask : taskList) {
						ChannelHandlerContext conn = ConnectorFactory
								.getConnection(personCardTask.getDevice_num());// 上一次心跳时间
						if (conn != null) {

							boolean result = false;
							try {
								result = semaphore.tryAcquire(5,
										TimeUnit.SECONDS); // 获取信号
								// Thread.sleep(1000);
								if (result) {

									UpToDownBusiness upToDownBusiness = new UpToDownBusiness();
									SinglePersonCard singlePersonCard = new SinglePersonCard();
									// 机器号
									singlePersonCard.setMachine(Integer
											.parseInt(personCardTask
													.getDevice_num()));
									// 卡号
									singlePersonCard.setCardNum(Integer
											.parseInt(personCardTask
													.getCard_num()));
									// 人员Id
									singlePersonCard.setPersonNum(123456);
									// 工号
									singlePersonCard.setNum(personCardTask
											.getStaff_no());
									// 挂失流水号
									singlePersonCard.setLostFlowNum(0x00);
									// 删除标志
									singlePersonCard
											.setFlag("0".equals(personCardTask
													.getFlag()) ? 0xff : 0x00);
									// 余额
									Double deposit = Double
											.parseDouble(personCardTask
													.getBalance());
									singlePersonCard.setDeposit(deposit
											.intValue());
									// 保留位
									singlePersonCard.setRetain(0x00);
									// 姓名
									singlePersonCard.setName(personCardTask
											.getStaff_name()); // 姓名
									// 将当前任务存起来
									currentPersonCardTask = personCardTask;
									upToDownBusiness
											.sendSinglePersonCardDown(singlePersonCard);
									/*
									 * System.out.println(personCardTask
									 * .getTask_id() +
									 * "-----------1---------======" + new
									 * Date());
									 */

								} else {
									// 调用一卡通接口将名单删除并且插入kc_xf_person_card_task_history表中
									/*
									 * System.out
									 * .println("=================无法获取下发名单返回结果"
									 * );
									 */
									PersonCardTask personCardTaskH = SendPersonDownThread.currentPersonCardTask;
									personCardTaskH.setSuccess_flag(1);
									String consumUrl2 = PropertiesToMap
											.propertyToMap(
													"photoncard_interface.properties")
											.get("url")
											+ "interface/cDevice/insertPersonCardTaskHistoryList.do";
									HttpRequest.sendPost(consumUrl2, "data="
											+ GsonUtil.toJson(personCardTaskH));
									semaphore.release();
								}
							} catch (Exception e) {
								e.printStackTrace();
								/* System.out.println("=================执行失败"); */

								// 此处失败是本条失败
								PersonCardTask personCardTaskH = personCardTask;
								personCardTaskH.setSuccess_flag(1);
								String consumUrl2 = PropertiesToMap
										.propertyToMap(
												"photoncard_interface.properties")
										.get("url")
										+ "interface/cDevice/insertPersonCardTaskHistoryList.do";
								HttpRequest.sendPost(consumUrl2, "data="
										+ GsonUtil.toJson(personCardTaskH));
								semaphore.release();
							}

							ConnectorFactory.lastOperateTimeMap.put(
									personCardTask.getDevice_num(), new Date());

						}
						SendPersonDownThread.LockFlag = true;
					}
				}
			} else {
				SendPersonDownThread.semaphore2.release();// 开锁
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
			SendPersonDownThread.semaphore2.release();
		}

		/*
		 * } else { System.out.println("~~~~~~本机ip不是服务器ip"); }
		 * 
		 * } catch (SocketException e) { e.printStackTrace(); }
		 */

		/*
		 * String url = "http://localhost:8080/comm/CDevice/applyPersonCard.do"
		 * ; String str2 = HttpRequest.sendPost(url, null);// ResultMsg2
		 * resultMsg = GsonUtil.toBean(str2, ResultMsg2.class);
		 */

	}

}
