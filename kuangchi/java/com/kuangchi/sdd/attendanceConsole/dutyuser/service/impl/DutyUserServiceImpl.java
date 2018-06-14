package com.kuangchi.sdd.attendanceConsole.dutyuser.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.duty.service.DutyService;
import com.kuangchi.sdd.attendanceConsole.dutyuser.dao.DutyUserDao;
import com.kuangchi.sdd.attendanceConsole.dutyuser.model.DutyUser;
import com.kuangchi.sdd.attendanceConsole.dutyuser.service.DutyUserService;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
@Service("dutyUserServiceImpl")
public class DutyUserServiceImpl implements DutyUserService {
	@Resource(name = "dutyUserDaoImpl")
	DutyUserDao dutyUserDao;
	
    @Resource(name="LogDaoImpl")
	private LogDao logDao;
    
    @Resource(name="dutyUserServiceImpl")
	DutyUserService dutyUserService;
    
    @Resource(name="dutySerivceImpl")
	DutyService  dutyService;
    
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;
	
	public List<DutyUser> getDutyUserByParam(DutyUser dutyUser){
		return dutyUserDao.getDutyUserByParam(dutyUser);
		
	}
	
	public Grid<DutyUser> getDutyUserByParamPage(DutyUser dutyUser){
		Grid<DutyUser> grid = new Grid<DutyUser>();
		List<DutyUser> resultList = dutyUserDao.getDutyUserByParamPage(dutyUser);
		grid.setRows(resultList);
		if(null !=resultList){
			grid.setTotal(dutyUserDao.getDutyUserByParamPageCounts(dutyUser));
		}else{
			grid.setTotal(0);
		}
		return grid;
	}
	
	public boolean deleteDutyUserById(String dutyUser_ids,String create_user){
		Map<String,String> log = new HashMap<String,String>();
		boolean result = dutyUserDao.deleteDutyUserById(dutyUser_ids);
		log.put("V_OP_NAME", "员工排班维护");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "删除排班信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	
	public boolean deleteDutyDeptById(String dutyDept_ids,String create_user){
		Map<String,String> log = new HashMap<String,String>();
		boolean result = dutyUserDao.deleteDutyDeptById(dutyDept_ids);
		log.put("V_OP_NAME", "部门排班维护");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "删除排班信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	
	public boolean updateDutyUser(DutyUser dutyUser,String create_user){
		
	
		boolean	result = dutyUserDao.updateDutyUser(dutyUser);
		
		Map<String,String> log = new HashMap<String,String>();
		
		log.put("V_OP_NAME", "员工排班维护");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "修改排班信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	
	public void updDutyUser(DutyUser dutyUser,String create_user,String today) throws Exception{
			dutyUserService.deleteDutyUserById(dutyUser.getId().toString(), create_user);
			dutyUserService.insertNewDutyWithoutConflict(dutyUser.getStaff_num(), today, dutyUser);
	}
	
	
	/**修改员工排班(修改的排班日期跨天的情况)  by huixian.pan
	 * @throws Exception */
	public void updDutyUserAmountToday(DutyUser updDateDutyUser,DutyUser insertDutyUser,String create_user,String today) throws Exception{
		   dutyUserDao.updateDutyUserEndDate(updDateDutyUser);
		   dutyUserService.insertNewDutyWithoutConflict(insertDutyUser.getStaff_num(), today, insertDutyUser);
		   
	}

	@Override
	public boolean insertDutyUser(List<DutyUser> dutyUsers,String today,String create_user){
		Map<String,String> log = new HashMap<String,String>();
		for(DutyUser  dutyUser:dutyUsers){
			try {
				dutyUserService.insertNewDutyWithoutConflict(dutyUser.getStaff_num(), today, dutyUser);
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "员工排班维护");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增排班信息");
				logDao.addLog(log);
			} catch (Exception e) {
				e.printStackTrace();
				log.put("V_OP_TYPE", "异常");
				log.put("V_OP_NAME", "员工排班维护");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增排班信息");
				logDao.addLog(log);
				return false;
			}
			
		}
		return true;
	}

	@Override
	public DutyUser getDutyUserById(String dutyUser_id) {
		
		return dutyUserDao.getDutyUserById(dutyUser_id);
	}

	@Override
	public Integer getDutyUserByParamCounts(DutyUser dutyUser) {
		return dutyUserDao.getDutyUserByParamCounts(dutyUser);
	}

	@Override
	public Integer getDutyIdByDutyName(String duty_name) {
		return dutyUserDao.getDutyIdByDutyName(duty_name);
	}

	@Override
	public Integer getCountByStaffNum(String staff_num) {
		return dutyUserDao.getCountByStaffNum(staff_num);
	}

	@Override
	public Integer getDutyUserCountsExceptId(DutyUser dutyUser) {
		return dutyUserDao.getDutyUserCountsExceptId(dutyUser);
	}
	
	
	
	//--------------门面模式开始  create by gengji.yang------------------------------------
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat sdg=new SimpleDateFormat("yyyy-MM-dd");
	
	//格式化时间   返回date time
	private Date formatToDateWithTime(String dateTimeStr) throws ParseException{
			return sdf.parse(dateTimeStr);
	}
	//格式化日期  返回 date
	 private Date formatToDate(String dateStr){
		 try {
			return sdg.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return null;
	 }
	
	//返回毫秒
	private long formatToTimes(String dateTimeStr) throws ParseException{
			return formatToDateWithTime(dateTimeStr).getTime();
	}
	
	//日期加一天  这里需要重新拼装日期后面的时间  不跨天
	private String addOneDay(String dateStr,String offerTimeStr){
		String[] strArr=dateStr.split(" ");
		String[] timeArr=offerTimeStr.split(" ");
		Calendar c=Calendar.getInstance();
		c.setTime(formatToDate(strArr[0]));
		c.add(Calendar.DAY_OF_MONTH, 1);
		dateStr=sdg.format(c.getTime())+" "+timeArr[1];
		return dateStr;
	}
	//日期加一天  这里需要重新拼装日期后面的时间  跨天
	private String noAddOneDay(String dateStr,String offerTimeStr){
		String[] strArr=dateStr.split(" ");
		String[] timeArr=offerTimeStr.split(" ");
		Calendar c=Calendar.getInstance();
		c.setTime(formatToDate(strArr[0]));
		dateStr=sdg.format(c.getTime())+" "+timeArr[1];
		return dateStr;
	}
	
	//日期减一天 这里需要重新拼装日期后面的时间 不跨天
	private String minusOneDay(String dateStr,String offerTimeStr){
		String[] strArr=dateStr.split(" ");
		String[] timeArr=offerTimeStr.split(" ");
		Calendar c=Calendar.getInstance();
		c.setTime(formatToDate(strArr[0]));
		c.add(Calendar.DAY_OF_MONTH, -1);
		dateStr=sdg.format(c.getTime())+" "+timeArr[1];
		return dateStr;
	}
	//日期减一天 这里需要重新拼装日期后面的时间 跨天
	private String noMinusOneDay(String dateStr,String offerTimeStr){
		String[] strArr=dateStr.split(" ");
		String[] timeArr=offerTimeStr.split(" ");
		Calendar c=Calendar.getInstance();
		c.setTime(formatToDate(strArr[0]));
		dateStr=sdg.format(c.getTime())+" "+timeArr[1];
		return dateStr;
	}
	
	//取时间间隔
	private long getTimeInterval(String startDateStr,String endDateStr) throws ParseException{
		return formatToTimes(endDateStr)-formatToTimes(startDateStr);
	}
	
	//比较时间间隔 返回状态码
	private int getStateAfterCompareLength(DutyUser oldDuty,DutyUser newDuty) throws ParseException{
		if(getTimeInterval(oldDuty.getBegin_time(),oldDuty.getEnd_time()) - getTimeInterval(newDuty.getBegin_time(),newDuty.getEnd_time())<0){//旧区间 小于 新区间
			return 0;
		}else if(getTimeInterval(oldDuty.getBegin_time(),oldDuty.getEnd_time()) - getTimeInterval(newDuty.getBegin_time(),newDuty.getEnd_time())>0){//旧区间 大于 新区间
			return 1;
		}else{
			return 2;
		}
	}
	
	//--------------------------------------------------------------------再次调整旧的排班区间的方法开始-----------------------------------------------
		//这里主要有两个主要分支  A<D 和 B>C 这两种情况均是有交集的情况，应该去除交集
	private DutyUser getOldDutyAfterFinalCheck(DutyUser oldDuty,DutyUser newDuty) throws Exception{
		String[] aDateArr=oldDuty.getBegin_time().split(" ");
		String[] bDateArr=oldDuty.getEnd_time().split(" ");
		String[] cDateArr=newDuty.getBegin_time().split(" ") ;
		String[] dDateArr=newDuty.getEnd_time().split(" ");
		
		if(aDateArr[0].equals(dDateArr[0])){
			if(formatToTimes(oldDuty.getBegin_time())<=formatToTimes(newDuty.getEnd_time())){//A=D+1,若A<D,则有交集
				//则A需要加多一天
				oldDuty.setBegin_time(addOneDay(oldDuty.getBegin_time(), oldDuty.getBegin_time()));
			}
		}else if(bDateArr[0].equals(cDateArr[0])){
			if(formatToTimes(oldDuty.getEnd_time())>=formatToTimes(newDuty.getBegin_time())){//B=C-1,若B>C，则有交集
				//则B需要减多一天
				oldDuty.setEnd_time(minusOneDay(oldDuty.getEnd_time(), oldDuty.getEnd_time()));
			}
		}
		//返回oldDuty之前，检查一下区间的合法性，若不合法，则返回空
		if(getTimeInterval(oldDuty.getBegin_time(),oldDuty.getEnd_time())<0){//若区间起点大于区间终点，则放弃这个 旧的排班区间
			oldDuty=null;
		}
		return oldDuty;
	}
//	private DutyUser getOldDutyAfterFinalCheck(DutyUser oldDuty,DutyUser newDuty) throws Exception{
//		String[] aDateArr=oldDuty.getBegin_time().split(" ");
//		String[] bDateArr=oldDuty.getEnd_time().split(" ");
//		String[] cDateArr=newDuty.getBegin_time().split(" ") ;
//		String[] dDateArr=newDuty.getEnd_time().split(" ");
//		
//		if(aDateArr[0].equals(dDateArr[0])||bDateArr[0].equals(cDateArr[0])){
//			if(formatToTimes(oldDuty.getBegin_time())<=formatToTimes(newDuty.getEnd_time())){//A=D+1,若A<D,则有交集
//				//则A需要加多一天
//				oldDuty.setBegin_time(addOneDay(oldDuty.getBegin_time(), oldDuty.getBegin_time()));
//			}else if(formatToTimes(oldDuty.getEnd_time())>=formatToTimes(newDuty.getBegin_time())){//B=C-1,若B>C，则有交集
//				//则B需要减多一天
//				oldDuty.setEnd_time(minusOneDay(oldDuty.getEnd_time(), oldDuty.getEnd_time()));
//			}
//			//返回oldDuty之前，检查一下区间的合法性，若不合法，则返回空
//			if(getTimeInterval(oldDuty.getBegin_time(),oldDuty.getEnd_time())<0){//若区间起点大于区间终点，则放弃这个 旧的排班区间
//				oldDuty=null;
//			}
//		}
//		return oldDuty;
//	}
	//--------------------------------------------------------------------再次调整旧的排班区间的方法结束-----------------------------------------------
	
	
	//旧排班区间[A,B] 新添加的排班区间[C,D] 
	private List<DutyUser> getNewSectionWithState(DutyUser oldDuty,DutyUser newDuty) throws Exception{//2017-06-07 09:00  2017-06-09 12:00
		oldDuty.setDept_num(newDuty.getDept_num());
		//先看看 原排班区间与新排班区间长度的大小关系,返回一个状态码
 		int state=getStateAfterCompareLength(oldDuty, newDuty);
		List<DutyUser> list=new ArrayList<DutyUser>();
		boolean flag=true;
//原排班区间长度小于新排班区间长度		
		if(state==0){
			//A>=C && B<=D
			if(formatToTimes(oldDuty.getBegin_time())>=formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getEnd_time())<=formatToTimes(newDuty.getEnd_time())){
				//不改区间 直接删除 oldDuty
				flag=false;
			//A<C && C<=B
			}else if(formatToTimes(oldDuty.getBegin_time())<formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getEnd_time())>=formatToTimes(newDuty.getBegin_time()) ){
				//[A,C-1] 需要考虑跨天的班
				if(getTimeInterval(oldDuty.getBegin_time(),noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()))>0){
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}else{
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;
				}
			// A<=D && D<B
			}else if(formatToTimes(oldDuty.getBegin_time())<=formatToTimes(newDuty.getEnd_time()) && formatToTimes(oldDuty.getEnd_time())>formatToTimes(newDuty.getEnd_time())){
				//[D+1,B]
				if(getTimeInterval(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()),oldDuty.getEnd_time())>0){
					oldDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}else{
					oldDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false; 
				}else{
					oldDuty=finalDuty;
				}
			}
//原排班区间长度大于新排班区间长度
		}else if(state==1){
			//  B<=D&& B>=C   
			if(formatToTimes(oldDuty.getEnd_time()) <= formatToTimes(newDuty.getEnd_time()) && formatToTimes(oldDuty.getEnd_time())>=formatToTimes(newDuty.getBegin_time())){
			// [A,C-1]
				if(getTimeInterval(oldDuty.getBegin_time(),noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()))>0){
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}else{
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;//2017-06-03 06:00  2017-06-06 23:59
				}
			// A<C && B>D
			}else if(formatToTimes(oldDuty.getBegin_time())<formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getEnd_time())>formatToTimes(newDuty.getEnd_time())){
				try {
					//对象 浅复制 
					DutyUser aDuty=(DutyUser) oldDuty.clone();
					DutyUser bDuty=(DutyUser) oldDuty.clone();
					boolean aFlag=true;
					boolean bFlag=true;
					//[A,C-1]
					if(getTimeInterval(oldDuty.getBegin_time(),noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()))>0){
//						aDuty.setEnd_time(minusOneDay(newDuty.getBegin_time(),aDuty.getEnd_time()));
						aDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),aDuty.getEnd_time()));
					}else{
						aDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),aDuty.getEnd_time()));
					}
					//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
					DutyUser finalaDuty=getOldDutyAfterFinalCheck(aDuty,newDuty);
					if(finalaDuty==null){
						aFlag=false;
					}else{
						aDuty=finalaDuty;
					}
					//[D+1,B]
					if(getTimeInterval(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()),oldDuty.getEnd_time())>0){
//						bDuty.setBegin_time(addOneDay(newDuty.getEnd_time(),bDuty.getBegin_time()));
						bDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),bDuty.getBegin_time()));
					}else{
						bDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),bDuty.getBegin_time()));
					}
					//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
					DutyUser finalbDuty=getOldDutyAfterFinalCheck(bDuty,newDuty);
					if(finalbDuty==null){
						bFlag=false;
					}else{
						bDuty=finalbDuty;
					}
					if(aFlag){
						list.add(aDuty);
					}else{//为了保证这个大分支内的list的size 总是等于2,放入多一次newDuty
						list.add(newDuty);
					}
					if(bFlag){
						list.add(bDuty);
					}else{
						list.add(newDuty);
					}
					flag=false;
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			//修改为 A>=C && A<=D
			}else if(formatToTimes(oldDuty.getBegin_time())>=formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getBegin_time())<= formatToTimes(newDuty.getEnd_time())){
				//[D+1,B]
				if(getTimeInterval(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()),oldDuty.getEnd_time())>0){
					oldDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}else{
					oldDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;
				}
			}
//原排班区间长度等于新排班区间长度			
		}else{
			//B>=C && A<C
			if(formatToTimes(oldDuty.getBegin_time())<formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getEnd_time())>=formatToTimes(newDuty.getBegin_time())){
			//[A,C-1]
				if(getTimeInterval(oldDuty.getBegin_time(),noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()))>0){
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}else{
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;
				}
			}
			// A<=D && B>D
			else if(formatToTimes(oldDuty.getBegin_time())<=formatToTimes(newDuty.getEnd_time()) && formatToTimes(oldDuty.getEnd_time())>formatToTimes(newDuty.getEnd_time())){
			//[D+1,B]
				if(getTimeInterval(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()),oldDuty.getEnd_time())>0){
					oldDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}else{
					oldDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;
				}
			}
			//A=C
			else if(formatToTimes(oldDuty.getBegin_time())==formatToTimes(newDuty.getBegin_time())){
			//直接删除 
				flag=false;
			}
		}
		if(flag){
			list.add(oldDuty);
		}
		return list;
	}
/*	
	//旧排班区间[A,B] 新添加的排班区间[C,D] 
	private List<DutyUser> getNewSectionWithState(DutyUser oldDuty,DutyUser newDuty) throws Exception{//2017-06-07 09:00  2017-06-09 12:00
		oldDuty.setDept_num(newDuty.getDept_num());
		//先看看 原排班区间与新排班区间长度的大小关系,返回一个状态码
		int state=getStateAfterCompareLength(oldDuty, newDuty);
		List<DutyUser> list=new ArrayList<DutyUser>();
		boolean flag=true;
//原排班区间长度小于新排班区间长度		
		if(state==0){
			//A>=C && B<=D
			if(formatToTimes(oldDuty.getBegin_time())>=formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getEnd_time())<=formatToTimes(newDuty.getEnd_time())){
				//不改区间 直接删除 oldDuty
				flag=false;
				//A<C && C<=B
			}else if(formatToTimes(oldDuty.getBegin_time())<formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getEnd_time())>=formatToTimes(newDuty.getBegin_time()) ){
				//[A,C-1] 需要考虑跨天的班
				if(getTimeInterval(oldDuty.getBegin_time(),minusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()))>0){
					oldDuty.setEnd_time(minusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}else{
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;
				}
				// A<=D && D<B
			}else if(formatToTimes(oldDuty.getBegin_time())<=formatToTimes(newDuty.getEnd_time()) && formatToTimes(oldDuty.getEnd_time())>formatToTimes(newDuty.getEnd_time())){
				//[D+1,B]
				if(getTimeInterval(addOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()),oldDuty.getEnd_time())>0){
					oldDuty.setBegin_time(addOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}else{
					oldDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false; 
				}else{
					oldDuty=finalDuty;
				}
			}
//原排班区间长度大于新排班区间长度
		}else if(state==1){
			//  B<=D&& B>=C   
			if(formatToTimes(oldDuty.getEnd_time()) <= formatToTimes(newDuty.getEnd_time()) && formatToTimes(oldDuty.getEnd_time())>=formatToTimes(newDuty.getBegin_time())){
				// [A,C-1]
				if(getTimeInterval(oldDuty.getBegin_time(),minusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()))>0){
					oldDuty.setEnd_time(minusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}else{
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;//2017-06-03 06:00  2017-06-06 23:59
				}
				// A<C && B>D
			}else if(formatToTimes(oldDuty.getBegin_time())<formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getEnd_time())>formatToTimes(newDuty.getEnd_time())){
				try {
					//对象 浅复制 
					DutyUser aDuty=(DutyUser) oldDuty.clone();
					DutyUser bDuty=(DutyUser) oldDuty.clone();
					boolean aFlag=true;
					boolean bFlag=true;
					//[A,C-1]
					if(getTimeInterval(oldDuty.getBegin_time(),minusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()))>0){
						aDuty.setEnd_time(minusOneDay(newDuty.getBegin_time(),aDuty.getEnd_time()));
					}else{
						aDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),aDuty.getEnd_time()));
					}
					//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
					DutyUser finalaDuty=getOldDutyAfterFinalCheck(aDuty,newDuty);
					if(finalaDuty==null){
						aFlag=false;
					}else{
						aDuty=finalaDuty;
					}
					//[D+1,B]
					if(getTimeInterval(addOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()),oldDuty.getEnd_time())>0){
						bDuty.setBegin_time(addOneDay(newDuty.getEnd_time(),bDuty.getBegin_time()));
					}else{
						bDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),bDuty.getBegin_time()));
					}
					//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
					DutyUser finalbDuty=getOldDutyAfterFinalCheck(bDuty,newDuty);
					if(finalbDuty==null){
						bFlag=false;
					}else{
						bDuty=finalbDuty;
					}
					if(aFlag){
						list.add(aDuty);
					}else{//为了保证这个大分支内的list的size 总是等于2,放入多一次newDuty
						list.add(newDuty);
					}
					if(bFlag){
						list.add(bDuty);
					}else{
						list.add(newDuty);
					}
					flag=false;
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				//修改为 A>=C && A<=D
			}else if(formatToTimes(oldDuty.getBegin_time())>=formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getBegin_time())<= formatToTimes(newDuty.getEnd_time())){
				//[D+1,B]
				if(getTimeInterval(addOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()),oldDuty.getEnd_time())>0){
					oldDuty.setBegin_time(addOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}else{
					oldDuty.setBegin_time(noAddOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;
				}
			}
//原排班区间长度等于新排班区间长度			
		}else{
			//B>=C && A<C
			if(formatToTimes(oldDuty.getBegin_time())<formatToTimes(newDuty.getBegin_time()) && formatToTimes(oldDuty.getEnd_time())>=formatToTimes(newDuty.getBegin_time())){
				//[A,C-1]
				if(getTimeInterval(oldDuty.getBegin_time(),minusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()))>0){
					oldDuty.setEnd_time(minusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}else{
					oldDuty.setEnd_time(noMinusOneDay(newDuty.getBegin_time(),oldDuty.getEnd_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;
				}
			}
			// A<=D && B>D
			else if(formatToTimes(oldDuty.getBegin_time())<=formatToTimes(newDuty.getEnd_time()) && formatToTimes(oldDuty.getEnd_time())>formatToTimes(newDuty.getEnd_time())){
				//[D+1,B]
				if(getTimeInterval(addOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()),oldDuty.getEnd_time())>0){
					oldDuty.setBegin_time(addOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}else{
					oldDuty.setBegin_time(addOneDay(newDuty.getEnd_time(),oldDuty.getBegin_time()));
				}
				//在这之后，还需再次检验一下oldDuty的区间调整之后old的起点与new的终点 或 old的终点与new 的起点是否还有交集
				DutyUser finalDuty=getOldDutyAfterFinalCheck(oldDuty,newDuty);
				if(formatToTimes(finalDuty.getBegin_time())>formatToTimes(finalDuty.getEnd_time())){
					finalDuty=null;
				}
				if(finalDuty==null){
					flag=false;
				}else{
					oldDuty=finalDuty;
				}
			}
			//A=C
			else if(formatToTimes(oldDuty.getBegin_time())==formatToTimes(newDuty.getBegin_time())){
				//直接删除 
				flag=false;
			}
		}
		if(flag){
			list.add(oldDuty);
		}
		return list;
	}
*/	
	@Override
	public void insertNewDutyWithoutConflict(String staffNum, String today,
		DutyUser newDutyUser) throws Exception {
		new NewDutyWithOutConflict().insertNewDutyWithoutConflict(staffNum, today, newDutyUser);
	}
	
	//判断欲新增排班区间是否跨天，若跨天，则将结束时间加一天
	public DutyUser ifAddEndTime(DutyUser dutyUser){
        Duty oldDuty=new Duty();
        oldDuty=dutyService.getDutyById(dutyUser.getDuty_id().toString());
        String  oldStartTime=oldDuty.getDuty_start_check_point();
        String  oldEndTime=oldDuty.getDuty_end_check_point();
        if(oldStartTime.compareTo(oldEndTime)>0){
        	String endDateTime=dutyUser.getEnd_time();
        	dutyUser.setEnd_time(addOneDay(endDateTime,endDateTime));
        }
		return dutyUser;
	}
	//--------------门面模式结束  create by gengji.yang------------------------------------
	
//-------------------------内部类开始，避免让service有状态，保持service的单例----------------------------------
	private class NewDutyWithOutConflict{
		private int count=1;
		public void insertNewDutyWithoutConflict(String staffNum, String today,
			DutyUser newDutyUser) throws Exception {
			//newDutyUser=ifAddEndTime(newDutyUser);
			List<DutyUser> listAfterToday=dutyUserDao.getDutyUserAfterToday(staffNum,today);//2016-06-13 17:03:54
			//查出跨了今天的班次
			DutyUser listAmongToday=dutyUserDao.getDutyUserAmongToday(staffNum, today);//2016-06-14 11:19:12
			if(listAmongToday!=null){
				listAfterToday.add(listAmongToday);
			}
			for(DutyUser ele:listAfterToday){
				List<DutyUser> list=getNewSectionWithState(ele,newDutyUser);
					int resultSize=list.size();  
					if(resultSize==0){
						dutyUserDao.deleteDutyUserById(ele.getId().toString());
					}else if(resultSize==1){
						if(formatToTimes(list.get(0).getBegin_time())<formatToTimes(list.get(0).getEnd_time())){
							dutyUserDao.updateDutyUser(list.get(0));///
						}
					}else if(resultSize==2){
						int countSame=0;//统计与NewDuty不同的个数
						//先做一个预判，然后再决定以下的分支走向
						for(DutyUser du:list){
							if(du.getBegin_time().equals(newDutyUser.getBegin_time())&&du.getEnd_time().equals(newDutyUser.getEnd_time())){
								countSame+=1;
							}
						}
						if(countSame==1){//有一个区间是NewDuty
							for(DutyUser du:list){
								if(du.getBegin_time().equals(newDutyUser.getBegin_time())&&du.getEnd_time().equals(newDutyUser.getEnd_time())){
									//这里是为了配合上一层的分支判断逻辑，newDutyUser在这里不插入，故这里无动作
								}else{
									dutyUserDao.updateDutyUser(du);
								}
							}
						}else if(countSame==2){//两个区间均是NewDuty,说明旧的区间拆分为两个区间均是非法的，此时应该删除旧的排班区间
							dutyUserDao.deleteDutyUserById(ele.getId().toString());
						}else{//两个区间均不是NewDuty,这里会在原本的newDuty的基础上再插入一个新的有效区间,所以下面执行完递归的插入之后要把标记量降回去，让原本的newDuty可以插入
							dutyUserDao.updateDutyUser(list.get(0));
							insertNewDutyWithoutConflict(staffNum, today,list.get(1));//这里就是递归的插入多一个有效的区间
							count--;
						}
					}
			}
			if(count==1){
				if(formatToTimes(newDutyUser.getBegin_time())<formatToTimes(newDutyUser.getEnd_time())){
					dutyUserDao.insertOneDutyUser(newDutyUser);
				}
				count++;
			}
		}
	}
		
		private class NewDeptDutyWithOutConflict{
			private int count=1;
			public void insertNewDeptDutyWithoutConflict(String deptNum, String today,
				DutyUser newDutyUser) throws Exception {
				//newDutyUser=ifAddEndTime(newDutyUser);
				List<DutyUser> listAfterToday=dutyUserDao.getDeptDutyUserAfterToday(deptNum,today);//2016-06-13 17:03:54
				//查出跨了今天的班次
				DutyUser listAmongToday=dutyUserDao.getDutyDeptAmongToday(deptNum, today);//2016-06-14 11:19:12
				if(listAmongToday!=null){
					listAfterToday.add(listAmongToday);
				}
				for(DutyUser ele:listAfterToday){
					List<DutyUser> list=getNewSectionWithState(ele,newDutyUser);
						int resultSize=list.size();  
						if(resultSize==0){
							dutyUserDao.deleteDutyDeptById(ele.getId().toString());
						}else if(resultSize==1){
							dutyUserDao.updateDutyDept(list.get(0));
						}else if(resultSize==2){
							int countSame=0;//统计与NewDuty不同的个数
							//先做一个预判，然后再决定以下的分支走向
							for(DutyUser du:list){
								if(du.getBegin_time().equals(newDutyUser.getBegin_time())&&du.getEnd_time().equals(newDutyUser.getEnd_time())){
									countSame+=1;
								}
							}
							if(countSame==1){//有一个区间是NewDuty
								for(DutyUser du:list){
									if(du.getBegin_time().equals(newDutyUser.getBegin_time())&&du.getEnd_time().equals(newDutyUser.getEnd_time())){
										//这里是为了配合上一层的分支判断逻辑，newDutyUser在这里不插入，故这里无动作
									}else{
										dutyUserDao.updateDutyDept(du);
									}
								}
							}else if(countSame==2){//两个区间均是NewDuty,说明旧的区间拆分为两个区间均是非法的，此时应该删除旧的排班区间
								dutyUserDao.deleteDutyDeptById(ele.getId().toString());
							}else{//两个区间均不是NewDuty,这里会在原本的newDuty的基础上再插入一个新的有效区间,所以下面执行完递归的插入之后要把标记量降回去，让原本的newDuty可以插入
								dutyUserDao.updateDutyDept(list.get(0));
								insertNewDeptDutyWithoutConflict(deptNum, today,list.get(1));//这里就是递归的插入多一个有效的区间
								count--;
							}
						}
				}
				if(count==1){
					if(formatToTimes(newDutyUser.getBegin_time())<formatToTimes(newDutyUser.getEnd_time())){
						dutyUserDao.insertOneDutyDept(newDutyUser);
					}
					count++;
				}
			}
//-------------------------内部类结束，避免让service有状态，保持service的单例----------------------------------
	}
	@Override
	public List<Map> getAllUserByDept_DM(String DeptDM) {
		return dutyUserDao.getAllUserByDept_DM(DeptDM);
	}

	@Override
	public List<String> getAllDutyName() {
		return dutyUserDao.getAllDutyName();
	}

	@Override
	public List<String> getAllStaffNo(String layerDeptNum) {
		return dutyUserDao.getAllStaffNo(layerDeptNum);
	}

	
	
	@Override
	public DutyUser getDutyIdByStaffNum(Map<String, Object> map) {
		return dutyUserDao.getDutyIdByStaffNum(map);
	}

	@Override
	public void insertNewDeptDutyWithoutConflict(String deptNum, String today,
			DutyUser newDutyUser) throws Exception {
		new NewDeptDutyWithOutConflict().insertNewDeptDutyWithoutConflict(deptNum, today, newDutyUser);
	}

	/**
	 * 新增部门排班  by huixian.pan
	 */
	@Override
	public boolean insertDutyDept(DutyUser dutyUser, String today,
			String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		String[] deptNums=dutyUser.getDept_num().split(",");
		for(String str:deptNums){
			DutyUser obj=new DutyUser();
			try {
				obj=(DutyUser) dutyUser.clone();
				obj.setDept_num(str.substring(1,str.length()-1));
				dutyUserService.insertNewDeptDutyWithoutConflict(str.substring(1,str.length()-1), today, obj);
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "部门排班维护");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增排班信息");
				logDao.addLog(log);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				log.put("V_OP_TYPE", "异常");
				log.put("V_OP_NAME", "部门排班维护");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增排班信息");
				logDao.addLog(log);
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				log.put("V_OP_TYPE", "异常");
				log.put("V_OP_NAME", "部门排班维护");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增排班信息");
				logDao.addLog(log);
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 删除部门排班   by  huixian.pan
	 */
	public boolean  deleteDutyDept(String staffIds,String deptIds,String create_user){
		
		//删除部门下的员工排班
		/*boolean  result1=dutyUserService.deleteDutyUserById(staffIds, create_user);*/
		
		//删除部门的排班信息
		boolean result2=dutyUserService.deleteDutyDeptById(deptIds, create_user);
		
		if(result2){
			return true;
		}else{
			return false;
		}
	}
	

	@Override
	public void getDutyFromDept(String deptNum, String staffNum) {
		String today=getToday();
        try {
			peopleAuthorityInfoService.makeDutyByDeptNum(deptNum, staffNum, today);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getToday(){
		 Date date=new Date();//创建当前系统时间
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today=format.format(date);
        return today;
	}

	
	
	  /*查询部门排班信息*/
    public  Grid getDutyDeptByParamPage(Map map){
    	Grid grid = new Grid();
		List<Map> resultList = dutyUserDao.getDutyDeptByParamPage(map);
		grid.setRows(resultList);
		if(null !=resultList){
			grid.setTotal(dutyUserDao.countDutyDeptByParam(map));
		}else{
			grid.setTotal(0);
		}
		return grid;
    }
    /*查询部门排班信息（导出用）*/
    public  List<Map> getDutyDeptByParam(Map map){
    	return  dutyUserDao.getDutyDeptByParam(map);
    }
    
    /*根据id查询部门排班*/
    public  Map getDutyDeptById(String ids){
    	return  dutyUserDao.getDutyDeptById(ids);
    }
    
    /*修改部门排班信息*/
    public  void  updDutyDept(DutyUser dutyDept,List<DutyUser> dutyUsers,String create_user,String today) throws Exception{
    	
    	/*修改部门排班信息（先删除原来那条部门排班信息，再重新插入新的部门排班信息）*/
    	boolean result=dutyUserDao.deleteDutyDeptById("'"+dutyDept.getId()+"'");
    	dutyUserService.insertNewDeptDutyWithoutConflict(dutyDept.getDept_num(), today, dutyDept);
    	
    	/*修改员工排班信息*/
    	/*if(dutyUsers !=null){
	    	for(DutyUser dutyUser:dutyUsers){
	    		updDutyUser(dutyUser, create_user, today);
	    	}
    	}*/
    }
    
	/*根据部门代码查询部门所有员工信息与排班id*/
	public List<Map> getStaffMessByDept_DM(String DeptDM){
		return   dutyUserDao.getStaffMessByDept_DM(DeptDM);
	}

	/*通过id修改员工排班结束日期 */
	public boolean updateDutyUserEndDate(List<DutyUser> dutyUserList,String deleteIds,String create_user) throws Exception {
		boolean flag=true;
		boolean flag2=true;
		for(DutyUser  dutyUser:dutyUserList){
			boolean result=dutyUserDao.updateDutyUserEndDate(dutyUser);
			if(result){
				flag=true;
			}else{
				flag=false;
			}
		}
		if(!"".equals(deleteIds)){
			flag2=dutyUserService.deleteDutyUserById(deleteIds, create_user);
		}
	    return  flag && flag2;
	}
	

	/*通过id修改部门排班结束日期 */
	public boolean updateDutyDeptEndDate(List<DutyUser> dutyUserList,String deleteIds,String create_user) throws Exception{
		boolean flag=true;
		boolean flag2=true;
		for(DutyUser  dutyUser:dutyUserList){
			boolean result=dutyUserDao.updateDutyDeptEndDate(dutyUser);
			if(result){
				flag=true;
			}else{
				flag=false;
			}
		}
		if(!"".equals(deleteIds)){
			flag2=dutyUserService.deleteDutyDeptById(deleteIds, create_user);
		}
	    return  flag && flag2;
	}
	
	
	
	
	@Override
	public void splitDutyUserByTimePoint(String yeahMonthDayHourMinute,
			String staff_num,String dept_num) {
		 DutyUser dutyUser=dutyUserDao.selectDutyByTimeAndStaffNum(yeahMonthDayHourMinute, staff_num);
		 if (dutyUser!=null) {
			 //获得要拆分的时间点
			 Date timePoint=getSimpleDate(yeahMonthDayHourMinute);
			 //要拆分的时间点的前一天
			 Date previousDayOfTimePoint=getPreviousDayTime(timePoint);
			 //要拆分的日期
			 String yyyyMMdd=DateUtil.getDateTimeString(timePoint);
			 //要拆分的日期的前一天日期
             String previousDayyyyyMmdd=DateUtil.getDateTimeString(previousDayOfTimePoint);
             
             //原来排班的开始时间
			 Date startDate=getSimpleDate(dutyUser.getBegin_time());
			 //原来排班的结束时间
			 Date endDate=getSimpleDate(dutyUser.getEnd_time());
			 
			 //排班开始时间时分
			 String startHHmm=getHHmm(startDate);
			 //排班结束时间时分
			 String endHHmm=getHHmm(endDate);
			 
			 
			 String firstEnd=previousDayyyyyMmdd+" "+endHHmm;
			 String secondStart=yyyyMMdd+" "+startHHmm;
			 
			 
			 if ( getSimpleDate(secondStart).compareTo(startDate)==0) {//如果排班开始时间当天就改了部门，则修改部门
				 dutyUserDao.updateDutyUserEndTime(dutyUser.getEnd_time(),dutyUser.getId(),dept_num);
			}else{
				 //拆分后的第一段
				 if (getSimpleDate(firstEnd).after(startDate)) {//已经执行的排班不能动部门
					 dutyUserDao.updateDutyUserEndTime(firstEnd, dutyUser.getId(),dutyUser.getDept_num());
				}
				 //如果拆分后的第二段开始时间要比结束时间小,则说明可以拆成第二段
				if (getSimpleDate(secondStart).after(startDate)&&(getSimpleDate(secondStart).before(endDate))) {
					dutyUser.setBegin_time(secondStart);
					dutyUser.setDept_num(dept_num); //未执行的排班为新部门
					dutyUserDao.insertOneDutyUser(dutyUser);
				}
				
			}
			 
			 

			
			
			
			
			
			
		} 
		
	}
	//获取日期  yyyy-MM-dd HH:mm
	public static Date getSimpleDate(String dateString){
		try {
	        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getyyyyMMddHHmm(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
	}
	
	public static String getHHmm(Date date){ 
		
		return new SimpleDateFormat("HH:mm").format(date);
	}

	
	//获取前一天的时间
	public static Date getPreviousDayTime(Date date){
					if(date==null){
						return null;
					}
					 Calendar calendar =Calendar.getInstance();
					 calendar.setTime(date);
					 calendar.add(Calendar.DAY_OF_MONTH, -1);
					 return calendar.getTime();
			}	
	
	
	/**
     * @throws Exception 
	 * @创建人　: 潘卉贤
     * @创建时间: 2016-12-27
     * @功能描述: 修改默认排班接口
     * @参数描述: oldDutyId(原来默认排班类型id)  newDutyId(新的默认排班类型id)  
     */
    public void  editDefaultDutyReg(String oldDutyId,String newDutyId) throws Exception{
    	//原来默认排班类型id
    	Map map1=new HashMap();
    	map1.put("duty_id", oldDutyId);
    	
    	//获取今天的日期
    	Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String today=format.format(date);
        
        //获取明天的日期
        Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		String tomorrow=format.format(cal.getTime());
		
		//获取后天的日期
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		cal2.add(Calendar.DATE, 2);
		String afterTomorrow=format.format(cal2.getTime());
    	
    	//获取新默认排班类型的开始时间和结束时间
    	Duty newDuty=new Duty();
    	newDuty=dutyService.getDutyById(newDutyId);
        String  newStatTime=newDuty.getDuty_start_check_point();
        String  newEndTime=newDuty.getDuty_end_check_point();
        
        //获取旧默认排班类型的开始时间和结束时间
        Duty oldDuty=new Duty();
        oldDuty=dutyService.getDutyById(oldDutyId);
        String  oldStartTime=oldDuty.getDuty_start_check_point();
        String  oldEndTime=oldDuty.getDuty_end_check_point();
    	
    	
    	List<Map> futureList=dutyUserDao.getFutureDutysByDutyId(map1);
    	List<Map> beforeList=dutyUserDao.getBeforeDutysByDutyId(map1);
    	
    	//对于未来的排班，直接修改其排班类型
        if(futureList.size()>0 || futureList != null){
        	for(Map m:futureList){
        		String  newBegin_time="";
        		String  newEnd_time="";
        		
        		//原来排班的开始日期和结束日期
        		String  originBeginDate=m.get("begin_time").toString().substring(0,10);
        		String  originEndDate=m.get("end_time").toString().substring(0,10);
        		
        		
        		//原来排班的开始日期等于今天，就要保留今天的旧默认排班，再去插入一条新的默认排班
        		if(today.equals(m.get("begin_time").toString().substring(0, 10))){
        			//如果原来的班次是跨天的类型的话，旧默认排班的结束日期更新到明天
        			if(oldStartTime.compareTo(oldEndTime)>0){
        				newBegin_time=afterTomorrow+" "+newStatTime;
        				newEnd_time=tomorrow+" "+oldEndTime;
        			}else{//否则更新到今天
        				newBegin_time=tomorrow+" "+newStatTime;
        				newEnd_time=today+" "+oldEndTime;
        			}
        			m.put("end_time", newEnd_time);
        			//1.修改原来排班结束日期
        			dutyUserDao.updDutyId(m);
        			
        			//2.插入新默认类型排班时用的model
	       		    String oldEnd_time=originEndDate+" "+newEndTime;
	       		    DutyUser dutyUser2=new DutyUser();
	   		        dutyUser2.setBegin_time(newBegin_time);
	   		        dutyUser2.setEnd_time(oldEnd_time);
	   		        dutyUser2.setDuty_id(Integer.parseInt(newDutyId));
	   		        dutyUser2.setStaff_num(m.get("staff_num").toString());
	   		        dutyUser2.setDept_num(m.get("dept_num").toString());
	   		        
	   		        dutyUserService.insertNewDutyWithoutConflict(dutyUser2.getStaff_num(), today, dutyUser2);
	   		        
        			
        		}else{
        			newBegin_time=originBeginDate+" "+newStatTime;
    				newEnd_time=originEndDate+" "+newEndTime;
    				m.put("begin_time", newBegin_time);
    				m.put("end_time", newEnd_time);
    				m.put("duty_id", newDutyId);
    				
        			dutyUserDao.updDutyId(m);
        		}
        		
        	}
        }
        
        //对于跨了今天的排班，先修改原来的排班结束日期为今天，然后再新插入一条排班类型为新默认排班类型的排班
        if(beforeList.size()>0 || beforeList != null){
        	for(Map m2:beforeList){
        		//1.修改原来排班结束日期为今天时用的model
        		 String  end_time=today+" "+oldEndTime;
    		     map1.put("begin_time", m2.get("begin_time"));
    		     map1.put("end_time", end_time);
    		     map1.put("id", m2.get("id"));
    		     
    		    //2.插入新默认类型排班时用的model
    		     String begin_time1=tomorrow+" "+newStatTime;
    		     String end_time1=m2.get("end_time").toString().substring(0,10)+" "+newEndTime;
    		     DutyUser dutyUser2=new DutyUser();
		         dutyUser2.setBegin_time(begin_time1);
		         dutyUser2.setEnd_time(end_time1);
		         dutyUser2.setDuty_id(Integer.parseInt(newDutyId));
		         dutyUser2.setStaff_num(m2.get("staff_num").toString());
		         dutyUser2.setDept_num(m2.get("dept_num").toString());
    		     
        		dutyUserDao.updDutyId(map1);
        		dutyUserService.insertNewDutyWithoutConflict(dutyUser2.getStaff_num(), today, dutyUser2);
        		
        	}
        }
    	
    }
    
    
    
}