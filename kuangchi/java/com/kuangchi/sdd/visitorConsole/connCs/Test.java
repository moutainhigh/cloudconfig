package com.kuangchi.sdd.visitorConsole.connCs;

import com.kuangchi.sdd.util.commonUtil.HttpRequest;

public class Test {
	public static void main(String[] args) {
		/*注册*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/userForAPP/addNewUserWithoutCompany.do";
		String param = "user_phone=15088132434"
				+ "&user_pwd=E10ADC3949BA59ABBE56E057F20F883E"
				+ "&imei_code=aimei860410030518583"
				+ "&realname=陈老师";*/
		
		/*登录*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/userForAPP/userLoginWithCom.do";
		String param = "user_phone=15088132434"
				+ "&user_pwd=E10ADC3949BA59ABBE56E057F20F883E"
				+ "&imei_code=aimei860410030518583";*/
		
		/*查询用户更新信息*/
		/*String ipAndport = "http://192.168.12.229:8090/LightKey_ZZB/";	
		String interfaceName = "app/userForAPP/getUpdateInfoForUser.do";
		String param = "userid=566"
				+ "&imei_code=aimei357143049098624";*/
		
		/*绑定企业*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/userForAPP/bindCompany.do";
		String param = "userid=891"
				+ "&realname=陈凯颖"
				+ "&imei_code=aimei860410030518583"
				+ "&childCompanyid=600006"
				+ "&empNo=01563";*/
		
		/*用户使用信息-iOS*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/userForAPP/userPhoneInfo.do";
		String param = "userid=112"
				+ "&app_version=3.2.0"
				+ "&brand=iOS"
				+ "&model_name=iPhone7,1"
				+ "&sys_version=10.2"
				+ "&imei_code=FC5BB668D4B34BB28385586A3D668ADA";*/
		
		/*用户使用信息-Android*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/userForAPP/userPhoneInfo.do";
		String param = "userid=112"
				+ "&app_version=V3.2.0.12"
				+ "&brand=Android"
				+ "&model_name=oppo a53m"
				+ "&sys_version=5.1.1"
				+ "&imei_code=aimei860793035869170";*/
		
		/*打卡记录*/
		/*String ipAndport = "http://192.168.12.123:8089/LightKey_Business/";	
		String interfaceName = "app/attend/attend_list.do";
		String param = "userid=3"
				+ "&cardId=222585"
				+ "&clockTime=2017-03-28";*/
		
		/*企业版注册*/
		/*String ipAndport = "http://localhost:8089/LightKey_Business/";	
		String interfaceName = "app/user/register_company_child_https.htm";
		String param = "userPhone=15088132434"
				+ "&userPwd=E10ADC3949BA59ABBE56E057F20F883E"
				+ "&companyid=1"
				+ "&empNo=5566"
				+ "&userEmail=kaiying.chen@kuang-chi.com"
				+ "&userName=小胖1号"
				+ "&childCompanyid=101";*/
		
		/*同步光子卡*/
		/*String ipAndport = "http://localhost:8097/LightKey_Communication/";	
		String interfaceName = "company/door/getPhotonIdByEmpNoAndName.do";
		String param = "doorType=7"
				+ "&empNo=01563"
				+ "&username=陈凯颖";*/
		
		/*查询已申请通过且未过期的记录*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/nvisit.do";
		String param = "vid=578";*/
		
		/*BS访客-查询权限组*/
		/*String ipAndport = "http://10.4.91.84:8080/photoncard/";	
		String interfaceName = "interface/queryAuthGroup.do";
		String param = "";*/
		
		/*BS访客-权限组详细信息*/
		/*String ipAndport = "http://10.4.91.84:8080/photoncard/";	
		String interfaceName = "interface/queryGroupInfo.do";
		String param = "groupNum=04f9712a-1e9c-11e7-a4f4-0050569a7999";*/
		
		
		/*String interfaceName = "interface/getNewCardNum.do";
		String param = "mVisitorNum="+vUUID
				+ "&cardType=2";*/
		/*访客下发权限过程：1拿卡号；2登记访客；3发卡*/
		/*String ipAndport = "http://10.4.91.84:8080/photoncard/";	
		String interfaceName = "interface/mainVisitorLogReg.do";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS"); 
		BS_Visitor bsv = new BS_Visitor();

		bsv.setRecordNum(UUIDUtil.uuidStr());
		//来访
		bsv.setmVisitorNum(UUIDUtil.uuidStr());
		bsv.setmVisitorName("岛主");
		bsv.setmPaperType("其他");
		bsv.setmPaperNum("15088132434");
		bsv.setmMobile("15088132434");
		bsv.setmVisitorCompany("光启总部");
		bsv.setCardNum("22589");
		bsv.setVisitDate("2017-04-13 13:00:00");
		bsv.setValidityDate("2017-04-13 14:00:00");
		bsv.setVisitNum(sdf.format(new Date()));
		bsv.setVisitState("2");
		bsv.setVisitNumber("1");
		
		//被访
		bsv.setpVisitorNum(UUIDUtil.uuidStr());
		bsv.setpVisitorName("鲁班");
		bsv.setpMobile("15088132435");
		bsv.setpOryName("光启东莞");*/
		
		
		/*bsv.setTodayTime("2017-04-10 12:00:00");
		bsv.setmVisitorSex("0");
		bsv.setFloor("0.0");
		bsv.setCreateUser("15088132434");
		bsv.setStaffNo("01563");
		bsv.setVisitCardType("2");
		bsv.setFSex("0.0");
		bsv.setPSex("0.0");
		bsv.setfVisitorSex("0");
		bsv.setpVisitorSex("0");
		bsv.setVSex("0.0");
		bsv.setVisitNumber("1");
		
		String param = GsonUtil.toJson(bsv);*/
		
		/*String ipAndport = "http://localhost:8097/LightKey_Communication/";	
		String interfaceName = "company/visitorArea/queryGroupInfo.do";
		String param = "";*/
		
		/*String ipAndport = "http://localhost:8097/LightKey_Communication/";	
		String interfaceName = "company/visitorArea/queryAuthGroup.do";
		String param = "groupsNum=04f9712a-1e9c-11e7-a4f4-0050569a7888";*/
		
		
		/*String ipAndport = "http://192.168.12.89:8089/photoncard/";	
		String interfaceName = "interface/visitMatterReg.do";
		String interfaceName = "interface/carryGoodsReg.do";
		String param = "";*/
		
		/*String ipAndport = "http://localhost:8097/LightKey_Communication/";	
		String interfaceName = "company/visitorArea/getCarryGoodsAndVisitMatter.do";
		String param = "";*/
		
		/*String ipAndport = "http://10.4.91.12:8080/photoncard/";	
		String interfaceName = "interface/getNewCardNum.do";
		String param = "mVisitorNum12213&cardType=2";
		
		Map paramMap=new HashMap();
		paramMap.put("mVisitorNum", "122138");
		paramMap.put("cardType", "2");
		
		String paramStr=JSONUtil.toJSONString(paramMap);*/
		

		/**
		 * 本地测试
		 */
		/*查询员工是否存在以及是否有通讯服务器，用以判定是否可以被访问*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/isexist.do";
		String param = "phoneNum=15820723956"
				+ "&intervieweeName=陈锦彭";*/
		
		//查询权限组信息
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/queryAuthGroup.do";
		String param = "companyID=600006";*/
		
		/*申请访问*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/application.do";
		String param = "vid=106"
				+ "&type=00"
				+ "&companyName=光启总部（软件大厦）"
				+ "&intervieweeName=宋阳阳"
				+ "&phoneNum=13005401339"
				+ "&reason=商务访问"
				+ "&startTime=2017-04-19 21:30:00"
				+ "&endTime=2017-04-19 22:30:00";*/
		
		//员工查看待授权/已授权/已拒绝的访客申请
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/fapproveinfo.do";
		String param = "phoneNum=15820723956"
				+ "&approved=01"
				+ "&page="
				+ "&rows=";*/
		
		/*查询访客所有访问记录*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/qhistory.do";
		String param = "vid=112"
				+ "&type=00"
				+ "&pageNow="
				+ "&rows=";*/
		
		/*授权访客申请*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/authorization_area.do";
		String param = "id=2301"
				+ "&authorization=02"
				+ "&groupNums=04f9712a-1e9c-11e7-a4f4-0050569a7999";*/
		
		/*查询已申请通过且未过期的记录*/
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/nvisit.do";
		String param = "vid=112";*/
		
		/*邀请访客来访问*/
		String ipAndport = "http://10.4.91.69:8090/LightKey_ZZB/";	
		String interfaceName = "app/visitor/invitation_area.do";
		String param = "phoneNum=15820723956"
				+ "&intervieweeName=宋阳阳"
				+ "&companyName=光启（总部）"
				+ "&reason=商务拜访"
				+ "&startTime=2017-04-19 17:29:00"
				+ "&endTime=2017-04-19 17:30:00"
				+ "&type=01"
				+ "&intervieweePhoneNum=13005401339"
				+ "&groupNums=ce290512-2407-11e7-9f1e-085700e88b44";
		
		/*String ipAndport = "http://localhost:8090/LightKey_ZZB/";	
		String interfaceName = "app/userForAPP/checkPhoneAndPwd.do";
		String param = "phone=15820723956"
				+ "&password=E10ADC3949BA59ABBE56E057F20F883y";*/
		
		/**
		 * 测试
		 */
		
		/*查询员工是否存在以及是否有通讯服务器，用以判定是否可以被访问*/
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/isexist.do";
		String param = "phoneNum=18898749926"
				+ "&intervieweeName=罗文";*/
		
		//查询权限组信息
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/queryAuthGroup.do";
		String param = "companyID=600006";*/
		
		/*申请访问*/
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/application.do";
		String param = "vid=585"
				+ "&type=00"
				+ "&companyName=光启总部（软件大厦）"
				+ "&intervieweeName=罗文"
				+ "&phoneNum=18898749926"
				+ "&reason=商务访问"
				+ "&startTime=2017-04-14 21:30:00"
				+ "&endTime=2017-04-14 22:30:00";*/
		
		//员工查看待授权/已授权/已拒绝的访客申请
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/fapproveinfo.do";
		String param = "phoneNum=18898749926"
				+ "&approved=00"
				+ "&page="
				+ "&rows=";*/
		
		/*查询访客所有访问记录*/
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/qhistory.do";
		String param = "vid=585"
				+ "&type=00"
				+ "&pageNow="
				+ "&rows=";*/
		
		/*授权访客申请*/
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/authorization_area.do";
		String param = "id=2248"
				+ "&authorization=02"
				+ "&groupNums=04f9712a-1e9c-11e7-a4f4-0050569a7999";*/
		
		/*查询已申请通过且未过期的记录*/
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/nvisit.do";
		String param = "vid=585";*/
		
		/*邀请访客来访问*/
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/invitation_area.do";
		String param = "phoneNum=15820723956"
				+ "&intervieweeName=罗文"
				+ "&companyName=光启（总部）"
				+ "&reason=商务拜访"
				+ "&startTime=2017-04-14 20:12:00"
				+ "&endTime=2017-04-14 21:00:00"
				+ "&type=01"
				+ "&intervieweePhoneNum=18898749926"
				+ "&groupNums=04f9712a-1e9c-11e7-a4f4-0050569a7999";*/
		
		/*String ipAndport = "http://192.168.12.229:8089/LightKey_ZZB/";	
		String interfaceName = "app/visitor/invitation_area.do";
		String param = "phoneNum=13553695114"
				+ "&intervieweeName=陈老师"
				+ "&companyName=光启总部(软件大厦)"
				+ "&reason=商务恰谈"
				+ "&startTime=2017-04-18 10:00:29"
				+ "&endTime=2017-04-20 10:00:29"
				+ "&type=01"
				+ "&intervieweePhoneNum=15820723956"
				+ "&groupNums=04f9712a-1e9c-11e7-a4f4-0050569a7999";*/
		String result = HttpRequest.sendPost(ipAndport+interfaceName, param);
		System.out.println(result);
	}
}
