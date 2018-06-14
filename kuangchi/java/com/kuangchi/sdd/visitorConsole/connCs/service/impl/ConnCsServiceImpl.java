package com.kuangchi.sdd.visitorConsole.connCs.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.PeopleAuthorityManagerService;
import com.kuangchi.sdd.visitorConsole.connCs.dao.ConnCsDao;
import com.kuangchi.sdd.visitorConsole.connCs.service.ConnCsService;
import com.kuangchi.sdd.visitorConsole.paramConfiguration.service.ParamConfigurationService;
@Service("connCsService")
public class ConnCsServiceImpl implements ConnCsService{
	
	@Autowired
	private ConnCsDao connCsDao;
	
	
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;
	
	@Resource(name = "peopleAuthorityManagerServiceImpl")
	private PeopleAuthorityManagerService peopleAuthorityManagerService;

	@Override
	public boolean makeCard(Map map) {
		//判断是否手输入卡号
		if(connCsDao.isHandCardNum(map)){
			mpCardInfo(map.get("mVisitorNum").toString(),map.get("cardNum").toString(),map.get("visitCardType").toString(),0);
		}
		boolean dFlag=sendDoorAuth(map);
		boolean fFlag=sendFloorAuth(map);
		if(dFlag&&fFlag){
			updateCardNum(map);
			makeCardBack(map);
		}
		return dFlag&&fFlag; 
	}
	
	@Override
	public boolean makeCard_phone(Map map) {
		//判断是否手输入卡号
		if(connCsDao.isHandCardNum(map)){
			mpCardInfo(map.get("mVisitorNum").toString(),map.get("cardNum").toString(),map.get("visitCardType").toString(),0);
		}
		boolean dFlag=sendDoorAuth_phone(map);
		boolean fFlag=sendFloorAuth_phone(map);
		if(dFlag&&fFlag){
			updateCardNum(map);
			makeCardBack(map);
		}
		return dFlag&&fFlag; 
	}
	
	/**
	 *取出门禁权限并下发
	 *返回 是否成功
	 *by gengji.yang
	 */
	private boolean sendDoorAuth(Map map){
		try{
			Map tempMap=connCsDao.getTimeZone((String)map.get("mVisitorNum"));
			tempMap.put("cardNum", map.get("cardNum"));
			tempMap.put("groupNum",null);
			List<Map> list=connCsDao.getDoorAuth((String)map.get("groupNum"));
			if(list!=null&&list.size()>0){
				for(Map m:list){
					m.putAll(tempMap);
				}
				addWorker(list,"00");
				peopleAuthorityInfoService.addAuthTasks(list,0);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 *取出门禁权限并下发,用于手机光钥匙
	 *返回 是否成功
	 *by gengji.yang
	 */
	private boolean sendDoorAuth_phone(Map map){
		try{
			Map tempMap=new HashMap();
			tempMap.put("startTime", map.get("startTime"));
			tempMap.put("endTime", map.get("endTime"));
			tempMap.put("cardNum", map.get("cardNum"));
			tempMap.put("groupNum",null);
			List<Map> list=connCsDao.getDoorAuth((String)map.get("groupNum"));
			if(list!=null&&list.size()>0){
				for(Map m:list){
					m.putAll(tempMap);
				}
				addWorker(list,"00");
				peopleAuthorityInfoService.addAuthTasks(list,0);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 新增权限时
	 * 先插入权限记录，task_state=00
	 * 门禁
	 */
	public void addWorker(List<Map>authList,String state){
		for(Map map:authList){
			map.put("state",state);
			peopleAuthorityInfoService.delAuthRecord(map);
			peopleAuthorityInfoService.addAuthRecord(map);
		}
	}
	
	/**
	 * 新增人员权限前
	 * 梯控
	 */
	public void updateAuthState2(List<Map> list,String state){
		for(Map m:list){
			m.put("object_type","1");
			m.put("state", state);
			peopleAuthorityManagerService.addTkAuthRecord(m);
		}
	}
	
	/**
	 * 取出梯控权限并下发
	 * 返回是否成功
	 * by gengji.yang
	 */
	private boolean sendFloorAuth(Map map){
		try{
			String card_type="2";
			Map tempMap=connCsDao.getTimeZone((String)map.get("mVisitorNum"));
			tempMap.put("card_num", map.get("cardNum"));
			tempMap.put("card_type", card_type);
			tempMap.put("floor_group_num",null);
			tempMap.put("start_time", tempMap.get("startTime").toString().substring(0, 10));
			tempMap.put("end_time", tempMap.get("endTime").toString().substring(0, 10));
			tempMap.put("object_num", map.get("mVisitorNum"));
			List<Map> list=connCsDao.getFloorAuth((String)map.get("groupNum"));
			if(list!=null&&list.size()>0){
				for(Map m:list){
					m.putAll(tempMap);
				}
				updateAuthState2(list,"00");
				connCsDao.addTkAuthTask(list);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 取出梯控权限并下发
	 * 返回是否成功
	 * by gengji.yang
	 */
	private boolean sendFloorAuth_phone(Map map){
		try{
			String card_type="2";
			Map tempMap=new HashMap();
			tempMap.put("startTime", map.get("startTime"));
			tempMap.put("endTime", map.get("endTime"));
			tempMap.put("card_num", map.get("cardNum"));
			tempMap.put("card_type", card_type);
			tempMap.put("floor_group_num",null);
			tempMap.put("start_time", tempMap.get("startTime").toString().substring(0, 10));
			tempMap.put("end_time", tempMap.get("endTime").toString().substring(0, 10));
			tempMap.put("object_num", map.get("mVisitorNum"));
			List<Map> list=connCsDao.getFloorAuth((String)map.get("groupNum"));
			if(list!=null&&list.size()>0){
				for(Map m:list){
					m.putAll(tempMap);
				}
				updateAuthState2(list,"00");
				connCsDao.addTkAuthTask(list);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 门禁梯控权限都下发成功后，
	 * 更新主访人员表中卡号字段
	 * by gengji.yang
	 */
	private void updateCardNum(Map map){
		String groupName=connCsDao.getGroupNameByNum(map);
		map.put("groupName", groupName);
		connCsDao.updateCardNum(map);
	}

	@Override
	public void makeCardBack(Map map) {
		//修改卡状态
		map.put("state", "20");
		connCsDao.updateFkCardState(map);
	}

	@Override
	public boolean makeCardLost(Map map) {
		boolean dFlag=delDAuth(map);
		boolean fFlag=delFAuth(map);
		updateCardLostState(map);
		return dFlag&fFlag;
	}
	
	/**
	 * 删除门禁权限
	 * by gengji.yang
	 */
	public boolean delDAuth(Map map){
		try{
			//修改门禁权限状态
			connCsDao.updtDAuthState(map);
			//根据卡号获取权限，放到任务表，去执行删除
			List<Map> authList=connCsDao.getDAuthByCardNum(map);
			peopleAuthorityInfoService.addAuthTasks(authList,1);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 卡挂失时要修改卡状态
	 * by gengji.yang
	 */
	private void updateCardLostState(Map map){
		//修改卡状态
		map.put("state", "40");
		connCsDao.updateFkCardState(map);
	}
	
	/**
	 * 删除梯控权限
	 * by gengji.yang
	 */
	public boolean delFAuth(Map map){
		if(null!=map){
			try{
				//修改梯控权限状态
				connCsDao.updtTAuthState(map);
				if(null!=map.get("cardNum")){
					String cardType="2";
					//根据卡号获取权限，放到任务表，去执行删除
					List<Map> list=connCsDao.getFAuthByCardNum(map);
					for(Map m:list){
						m.put("cardType", cardType);
					}
					connCsDao.addTkTask(list);
				}
				return true;
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}else{
			return true;
		}

	}

	@Override
	public String getNewCardNum(Map map) {
		if("0".equals(map.get("visitCardType"))){//光子卡
			return numAddAuto((String)map.get("mVisitorNum"),(String)map.get("visitCardType"),connCsDao.getAllGZCard(),2147483647l,1);
		}else if("1".equals(map.get("visitCardType"))){//IC卡
			return numAddAuto((String)map.get("mVisitorNum"),(String)map.get("visitCardType"),connCsDao.getAllICCard(),2147483647l,1);
		}else{//手机光钥匙
			return numAddAuto((String)map.get("mVisitorNum"),(String)map.get("visitCardType"),connCsDao.getAllICCard(),2147483647l,1);
		}
	}
/*	@Override
	public String getNewCardNum(Map map) {
		if("0".equals(map.get("visitCardType"))){//光子卡
			return connCsDao.getGZCardNum()==null?
					numAddAuto((String)map.get("mVisitorNum"),(String)map.get("visitCardType"),connCsDao.getAllGZCard(),65534,32768):getAlreadyCardNum((String)map.get("mVisitorNum"),(String)map.get("visitCardType"));
		}else{//IC卡
			return connCsDao.getICCardNum()==null?
					numAddAuto((String)map.get("mVisitorNum"),(String)map.get("visitCardType"),connCsDao.getAllICCard(),999999999,65535):getAlreadyCardNum((String)map.get("mVisitorNum"),(String)map.get("visitCardType"));
		}
	}
*/	
	/**
	 *往kc_card_info,kc_bound_card_info补充信息
	 *by gengji.yang 
	 */
	private void mpCardInfo(String mVisitorNum,String cardNum,String cardType,int flag){
		if(flag==0){
			Map map=new HashMap();
			map.put("staffNum", mVisitorNum);
			map.put("cardNum", cardNum);
			String type=null;
			if("2".equalsIgnoreCase(cardType)){//手机光钥匙
				type=connCsDao.getCardTypeNum("1");
			}else{//光子卡，IC卡
				type=connCsDao.getCardTypeNum("0".equalsIgnoreCase(cardType)==true?"2":"3");
			}
			map.put("type", type);
			connCsDao.addBoundCardRe(map);
			connCsDao.addCardInfoRe(map);
		}else{
			Map map=new HashMap();
			map.put("staffNum", mVisitorNum);
			map.put("cardNum", cardNum);
			String type=null;
			if("2".equalsIgnoreCase(cardType)){//手机光钥匙
				type=connCsDao.getCardTypeNum("1");
			}else{//光子卡，IC卡
				type=connCsDao.getCardTypeNum("0".equalsIgnoreCase(cardType)==true?"2":"3");
			}
			map.put("type", type);
			connCsDao.addBoundCardRe(map);
		}
	}

	
	/**
	 *自动+1
	 *list 为卡号序列
	 *max,min限定了卡号范围
	 *此方法返回范围内最小的新卡号
	 *若[1,10]
	 *(1,4,5),返回2
	 *(1,2,3,4,5,6,7,8,9),返回10
	 *
	 *by gengji.yang 
	 */
	private String numAddAuto(String mVisitorNum,String cardType,List<String> list,Long max,Integer min){
		//这里排序
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return new Long((String) o1).compareTo(new Long((String) o2));
  			}
	    });
		String num=null;
		if(list.size()>0 && list.size()<(max-min)){
			if(list.size()==0){
				num=min+"";
			}else{
				boolean flag=false;//默认不存在断码
				for(int i=0;i<list.size();i++){
					if((i+1)<list.size()&&(Long.parseLong(list.get(i+1))-Long.parseLong(list.get(i))>1)){
						flag=true;//存在断码
						 num=(Long.parseLong(list.get(i))+1)+"";
						 break;
					}
				}
				if(flag==false){//不存在断码
					num=(Long.parseLong(list.get(list.size()-1))+1)>max?null:(Long.parseLong(list.get(list.size()-1))+1)+"";
				}
			}
		}else{
			num="1";
		/*	if("0".equals(cardType)){
				num="32768";
			}else{
				num="65535";
			}*/
		}
		if(num!=null){
			mpCardInfo(mVisitorNum,num,cardType,0);
		}
		return num;
	}

	/**
	 *  返回现有的可用的最小卡号
	 *  cardType 0:光子卡 1:IC 卡
	 *  by gengji.yang
	 */
	public String getAlreadyCardNum(String mVisitorNum,String cardType){
		String cardNum="";
		if("0".equals(cardType)){
			cardNum=connCsDao.getGZCardNum();
		}else if("1".equals(cardType)){
			cardNum=connCsDao.getICCardNum();
		}else{//手机光钥匙
			cardNum=connCsDao.getICCardNum();
		}
		mpCardInfo(mVisitorNum,cardNum,cardType,1);
		return cardNum;
	} 
	
	@Override
	public boolean overTimeLeave(Map map) {
		boolean lFlag=true;
		boolean bFlag=true;
		if("0".equals(map.get("lostFlag"))){
			lFlag=makeCardLost(map);
		}
		if("0".equals(map.get("blackFlag"))){
			try{
				connCsDao.addBlackName(map);
			}catch(Exception e){
				e.printStackTrace();
				bFlag=false;
			}
		}
		//更新访客状态为超时后离开
		connCsDao.updateMainVisitorState(map);
		return lFlag&bFlag;
	}

	@Override
	public void updateVisitorState() {
		connCsDao.updateVisitorState();
	}

	@Override
	public List<Map> getNYetEmailRecd() {
		return connCsDao.getNYetEmailRecd();
	}

	@Override
	public Integer countOverTimeRecd() {
		return connCsDao.countOverTimeRecd();
	}

	@Override
	public void updateEmailState(List<Map> list) {
		for(Map map:list){
			connCsDao.updateEmailState(map);
		}
	}

	@Override
	public List<Map> getInformList() {
		return connCsDao.getInformList();
	}

	@Override
	public boolean delAuthOnLeave(Map map) {
		boolean dFlag=delDAuth(map);
		boolean fFlag=delFAuth(map);
		return dFlag&fFlag;
	}

	@Override
	public void makeVisitorVisiting(Map map) {
		connCsDao.makeVisitorVisiting(map);
	}

	@Override
	public void recycleCard(Map map) {
		//删除卡权限
		delAuthOnLeave(map);
		//卡号可以再次被使用：删除人卡绑定记录，删除卡号记录
		connCsDao.delBoundCardRec(map);
		connCsDao.delCardRec(map);
	}

	@Override
	public void delCardRec(Map map) {
		connCsDao.delCardRec(map);
	}

	@Override
	public void updateVisitorStateA() {
		connCsDao.updateVisitorStateA();
	}

	@Override
	public void updateShouJiVisiting() {
		connCsDao.updateShouJiVisiting();
	}

	@Override
	public void updateShouJiLeaving() {
		connCsDao.updateShouJiLeaving();
	}

	@Override
	public void deleteShouJiLeaveCard() {
		connCsDao.deleteShouJiLeaveCard();
	}

	@Override
	public void deleteShouJiLeaveBoundCard() {
		connCsDao.deleteShouJiLeaveBoundCard();
	}

	@Override
	public void delGuoQiMenJinQuanXianJilu() {
		connCsDao.delGuoQiMenJinQuanXianJilu();
	}

	@Override
	public void delGuoQiTkQuanXianJilu() {
		connCsDao.delGuoQiTkQuanXianJilu();
	}
	
	
}
