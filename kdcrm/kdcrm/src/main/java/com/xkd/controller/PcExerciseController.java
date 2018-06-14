package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.model.DC_User;
import com.xkd.model.Exercise;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.ExerciseService;
import com.xkd.service.UserDataPermissionService;
import com.xkd.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping("/exercise")
@Transactional
public class PcExerciseController  /*extends BaseController*/{

	@Autowired
	ExerciseService exerciseService;
	@Autowired
	UserDataPermissionService userDataPermissionService;

	@Autowired
	UserService userService;

	
	
	public final static String tableAndColumToken = "b610a13a-a64d-43b0-9d52-8e48600e9dcc2017-5-24";//数据源表和列的token id
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:新增or修改练习卷
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveExercise")
	public ResponseDbCenter saveExercise(HttpServletRequest req,@RequestBody  String result)throws Exception{
		//try {
			Exercise map = (Exercise) JSON.parseObject(result, Exercise.class);
			
			String token = req.getParameter("token");
			if(StringUtils.isBlank(token) || map == null){
				return ResponseConstants.USER_EXIST_ERROR_WJ; 
			}
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();


			String uid = req.getSession().getAttribute(token)+"";
			Map<String, Object> userMap = (Map<String, Object>) req.getSession().getAttribute("user");
			userMap = userService.selectUserById(uid);
			map.setDepartmentId(StringUtils.isBlank(map.getDepartmentId()) ? userMap.get("departmentId")+"":map.getDepartmentId());
			map.setPcCompanyId(userService.selectUserById(uid).get("pcCompanyId").toString());
			String cnt = exerciseService.saveExercise(map,uid);
			if("0".equals(cnt)){
				return ResponseConstants.MISSING_PARAMTER_WJ; 
			}
			res.setResModel(cnt);
			return res;
		/*} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} */
		
	}
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:新增or修改练习卷
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editExercise")
	public ResponseDbCenter editExercise(HttpServletRequest req,@RequestBody  String result)throws Exception{
		//try {
			Exercise map = (Exercise) JSON.parseObject(result, Exercise.class);
			
			String token = req.getParameter("token");
			if(StringUtils.isBlank(token) || map == null){
				return ResponseConstants.USER_EXIST_ERROR_WJ; 
			}
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();
			String uid = req.getSession().getAttribute(token)+"";
			Map<String, Object> userMap = (Map<String, Object>) req.getSession().getAttribute("user");
			userMap = userService.selectUserById(uid);
			map.setDepartmentId(StringUtils.isBlank(map.getDepartmentId()) ? userMap.get("departmentId")+"":map.getDepartmentId());
			String cnt = exerciseService.saveExercise(map,uid);
			if("0".equals(cnt)){
				return ResponseConstants.MISSING_PARAMTER_WJ; 
			}
			res.setResModel(cnt);
			return res;
		/*} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} */
		
	}
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:查询用户创建的试卷集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserExerciseList")
	public ResponseDbCenter getUserExerciseList(HttpServletRequest req,String token)throws Exception{
		try {
			//String token = req.getParameter("token");
			String ttype = req.getParameter("ttype");
			String title = req.getParameter("title");
			//String meetingName = req.getParameter("meetingName");
			//String meetingType = req.getParameter("meetingType");
			
			String isMeeting = req.getParameter("isMeeting");
			
			String pageNo1 = req.getParameter("currentPage");
			String pageSize1 = req.getParameter("pageSize");
			String departmentId = req.getParameter("departmentId");
			
			int pageNo = 0;
			int pageSize = 60;
			if(StringUtils.isNotBlank(pageNo1)){
				pageNo = Integer.valueOf(pageNo1);
				if(StringUtils.isNotBlank(pageSize1)){
					pageSize = Integer.valueOf(pageSize1);
				}
				pageNo= (pageNo -1)*pageSize;
			}
			//String userid = (redisCache.getCacheObject(token));
			List<String> ttypes = null;
			if(StringUtils.isNotBlank(ttype)){
				ttypes = new ArrayList<>();
				for (String string : ttype.split(",")) {
					ttypes.add(string);
				}
			}
			
			/*List<String> meetingTypes = null;
			if(StringUtils.isNotBlank(meetingType)){
				meetingTypes = new ArrayList<>();
				for (String string : meetingType.split(",")) {
					meetingTypes.add(string);
				}
			}*/
			String uid = (String)req.getSession().getAttribute(token);


			List<String> depList = depList = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,uid);

			Map<String,Object> map = new HashMap();
			map.put("depList",depList);
			map.put("uid",null);
			map.put("ttype",ttypes);
			map.put("title",title);
			//map.put("meetingName",meetingName);
			//map.put("meetingType",meetingTypes);
			map.put("isMeeting",isMeeting);
			map.put("pageNo",pageNo);
			map.put("pageSize",pageSize);
			List<Exercise> list = exerciseService.getUserExercise(map);
			ResponseDbCenter res = new ResponseDbCenter();
			res.setTotalRows(exerciseService.getUserExerciseTotal(map)+"");
			res.setResModel(list);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	
	/**
	 * 
	 * @author: 高丁丁
	 * @2017年5月24日 
	 * @功能描述:添加的页面 
	 * @return 返回table 和 colmun
	 */
	@ResponseBody
	@RequestMapping("/exerciseAddPage")
	public ResponseDbCenter exerciseAddPage()throws Exception{
		try {
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();
			Exercise eobj = new Exercise();
			/*if(redisCache.getCacheObject(tableAndColumToken) == null){
				List<Map<String, List>>  tablesList = exerciseService.getTablesList();
				redisCache.setCacheObject(tableAndColumToken, tablesList);
			}
			eobj.setData(redisCache.getCacheObject(tableAndColumToken));//查询表和列*/
			res.setResModel(eobj);
			return res;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
		
	}
	
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:查询用户创建的试卷内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getExercise")
	public ResponseDbCenter getExercise(HttpServletRequest req)throws Exception{ //try {
			String id = req.getParameter("id");
			if(StringUtils.isBlank(id)){
				return ResponseConstants.MISSING_PARAMTER_WJ;
			}
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();
			Exercise eobj = exerciseService.getExercise(id,null);
			res.setResModel(eobj);
			return res;
//		} catch (Exception e) {
//			System.out.println(e);
//			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
//		}
		
	}


	

	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:删除试卷
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delUserExercise")
	public ResponseDbCenter delUserExercise(@RequestParam("token") String token,@RequestParam("id") String id)throws Exception{
		try {
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();
			List<String> list = new ArrayList<>();
			String ids [] = id.split(",");
			for (String string : ids) {
				list.add(string);
			}
			int cnt = exerciseService.delExerciseById(list);
			res.setResModel(cnt);
			return res;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
		
	}
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:图表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getExerciseChart")
	public ResponseDbCenter getExerciseChart(@RequestParam("token") String token,@RequestParam("id")String id)throws Exception{
		try {
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();
			Exercise list = exerciseService.getExerciseChart(id);
			res.setResModel(list);
			return res;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
		
	}
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:查询出所有做题的用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserListAnswer")
	public ResponseDbCenter getUserListAnswer(@RequestParam("id")String id,@RequestParam("token")String token)throws Exception{
		try {
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();
			
			Exercise list = exerciseService.getUserListAnswer(id);
			
			res.setResModel(list);
			return res;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
		
	}
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:图表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserAnswer")
	public ResponseDbCenter getUserAnswer(HttpServletRequest req,@RequestParam("id")String id,@RequestParam("token")String token)throws Exception{
		try {
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();
			String meetingId = req.getParameter("meetingId");
			DC_User user = (DC_User)req.getSession().getAttribute(token);
			Exercise list = exerciseService.getUserAnswer(id,user.getWeixin(),user.getId(),meetingId);
			
			res.setResModel(list);
			return res;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
		
	}
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:copy试卷
	 * id试卷主键id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/copyExercise")
	public ResponseDbCenter copyExercise(HttpServletRequest req,@RequestParam("token")String token,@RequestParam("id")String id)throws Exception{
		try {
			//返回数据总对象，0000表示成功，其它都表示错误
			exerciseService.copyExercise(id,req.getSession().getAttribute(token).toString());
			ResponseDbCenter res = new ResponseDbCenter();
			res.setResModel("SUCCESS");
			return res;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
		
	}
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:查询所有做这个问卷的用户
	 * id试卷主键id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getExerciseUserAll")
	public ResponseDbCenter getExerciseUserAll(HttpServletRequest req,@RequestParam("token")String token,@RequestParam("id")String id)throws Exception{
		try {
			//返回数据总对象，0000表示成功，其它都表示错误
			ResponseDbCenter res = new ResponseDbCenter();
			
			
			String uname = req.getParameter("uname");
			
			String pageNo1 = req.getParameter("currentPage");
			String pageSize1 = req.getParameter("pageSize");  
			
			int pageNo = 0;
			int pageSize = 60;
			if(StringUtils.isNotBlank(pageNo1)){
				pageNo = Integer.valueOf(pageNo1);
				pageNo = pageNo==0 ? 1:pageNo;
				if(StringUtils.isNotBlank(pageSize1)){
					pageSize = (Integer.valueOf(pageSize1));
				}
				pageNo= (pageNo-1)*pageSize;
			}
			res.setResModel(exerciseService.getExerciseUserAll(id,uname,pageNo,pageSize));
			res.setTotalRows(exerciseService.getExerciseUserAllTotal(id,uname)+"");
			return res;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
	
	}
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:查询没有被会务关联的试卷集合和已经被该问卷关联的会务
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMeetingExerciseList")
	public ResponseDbCenter getMeetingExerciseList(HttpServletRequest req)throws Exception{
		try {
			//String token = req.getParameter("token");
			String ttype = req.getParameter("ttype");
			String title = req.getParameter("title");
			
			String meetingId = req.getParameter("meetingId");
			
			//String isMeeting = req.getParameter("isMeeting");
			
			String pageNo1 = req.getParameter("currentPage");
			String pageSize1 = req.getParameter("pageSize");  
			
			int pageNo = 0;
			int pageSize = 60;
			if(StringUtils.isNotBlank(pageNo1)){
				pageNo = Integer.valueOf(pageNo1);
				pageNo = pageNo==0 ? 1:pageNo;
				if(StringUtils.isNotBlank(pageSize1)){
					pageSize = (Integer.valueOf(pageSize1));
				}
				pageNo= (pageNo-1)*pageSize;
			}
			//String userid = (redisCache.getCacheObject(token));
			List<String> ttypes = null;
			if(StringUtils.isNotBlank(ttype)){
				ttypes = new ArrayList<>();
				for (String string : ttype.split(",")) {
					ttypes.add(string);
				}
			}
			
			List<Exercise> list = exerciseService.getMeetingExercise(ttypes,title,meetingId,pageNo,pageSize);
			ResponseDbCenter res = new ResponseDbCenter();
			res.setTotalRows(exerciseService.ggetMeetingExerciseTotal(ttypes,title,meetingId,pageNo,pageSize)+"");
			
			res.setResModel(list);
			return res;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
		
		
	}
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:临时保存试卷基础信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/tempSaveExerciseInfo")
	public ResponseDbCenter tempSaveExerciseInfo(HttpServletRequest req,
			@RequestParam(required = false) String childTitle,
			String title,String cssType,String token,String codeTitle,String departmentId){
		
		Map<String, String> map = new HashMap<>();
		map.put("title", title);
		map.put("childTitle", childTitle);
		map.put("cssType", cssType);
		map.put("codeTitle",codeTitle);
		map.put("departmentId",departmentId);
		String key = UUID.randomUUID().toString();
		req.getSession().setAttribute(key, map);
		ResponseDbCenter res = new ResponseDbCenter();
		res.setResModel(key);
		return res;
	}
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:取出临时保存试卷基础信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTempSaveExerciseInfo")
	public ResponseDbCenter getTempSaveExerciseInfo(HttpServletRequest req,String token,String key){
		Map<String, String> map = (Map<String, String>) req.getSession().getAttribute(key);
		ResponseDbCenter res = new ResponseDbCenter();
		res.setResModel(map);
		return res;
	}

	/**
	 *
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:导出试卷详情
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exerciseWritePdf")
	public ResponseDbCenter exerciseWritePdf(HttpServletRequest req,String token,String id,String openId){
		ResponseDbCenter res = new ResponseDbCenter();
		res.setResModel(exerciseService.exerciseWritePdf(id,openId));
		return res;
	}
	/**
	 *
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:导出试卷详情
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exerciseWriteExcel")
	public ResponseDbCenter exerciseWriteExcel(HttpServletRequest req,String token,String id){
		ResponseDbCenter res = new ResponseDbCenter();
		res.setResModel(exerciseService.exerciseWriteExcel(id));
		return res;
	}

}
