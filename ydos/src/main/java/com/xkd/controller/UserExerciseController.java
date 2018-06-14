package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.mapper.ExerciseCommentMapper;
import com.xkd.mapper.UserExamMapper;
import com.xkd.model.*;
import com.xkd.service.DC_UserService;
import com.xkd.service.ExerciseService;
import com.xkd.service.QuestionService;
import com.xkd.service.UserAnswerService;
import com.xkd.utils.SolrLogger;
import com.xkd.utils.ThreadDataWJ;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@Transactional
@RequestMapping("/userExercise")
public class UserExerciseController  extends BaseController{

	@Autowired
	ExerciseService exerciseService;

	@Autowired
	UserExamMapper userExamMapper;

	@Autowired
	UserAnswerService userAnswerService;

	@Autowired
	QuestionService questionService;

	@Autowired
	ExerciseCommentMapper exerciseCommentMapper;

	@Autowired
	DC_UserService userService;
//	/**
//	 *
//	 * @author: gaoddO
//	 * @2017年3月22日
//	 * @功能描述:图表
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/getUserAnswerZhiZhuTu")
//	public ResponseDbCenter getUserAnswerZhiZhuTu(HttpServletRequest req, @RequestParam("token")String token,@RequestParam("id")String id,String uid){
//		//返回数据总对象，0000表示成功，其它都表示错误
//		ResponseDbCenter res = new ResponseDbCenter();
//		String openId = "";
//		if(StringUtils.isNotBlank(uid)){
//			openId = uid;
//		}else{
//			openId = (String)req.getSession().getAttribute("getOpenId"+token);
//		}
//		SolrLogger.loggerInfo("----------------------"+openId);
//		Exercise obj = exerciseService.getUserAnswerZhiZhuTu(openId,id);
//
//		res.setResModel(obj);
//		return res;
//	}


//	/**
//	 * -------------------------------------------------------------------------------------------------------------
//	 * @author: gaoddO
//	 * @2017年3月22日
//	 * @功能描述:用户以答案的形式获取用户试卷答题情况
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/getExerciseUserAnswer")
//	public ResponseDbCenter getExerciseUserAnswer(HttpServletRequest req,@RequestParam("id") String id,@RequestParam("token") String token){
//		//返回数据总对象，0000表示成功，其它都表示错误
//		ResponseDbCenter res = new ResponseDbCenter();
//
//		String meetingId = req.getParameter("meetingId");
//		DC_User user =(DC_User) req.getSession().getAttribute(token+"user");
//		Exercise eobj = exerciseService.getUserAnswer(id, user.getWeixin(),user.getId(),meetingId);
//
//		res.setResModel(eobj);
//		return res;
//	}
	/**
	 *
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:用户做题请求试卷
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserExercise")
	public ResponseDbCenter getUserExercise(HttpServletRequest req,
											@ApiParam(value = "id" ,required = false) @RequestParam(required = false) String id,
											@ApiParam(value = "token" ,required = false) @RequestParam(required = false) String token
	){
		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();


		if(StringUtils.isBlank(id)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}
		Exercise eobj = exerciseService.getExercise(id,null);
		res.setResModel(eobj);
		return res;
	}

//	/**
//	 * -------------------------------------------------------------------------------------------------------------
//	 * @author: gaoddO
//	 * @2017年3月22日
//	 * @功能描述:用户以试卷的形式获取用户试卷答题情况
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/getUserExerciseAnswer")
//	public ResponseDbCenter getUserExerciseAnswer(HttpServletRequest req,@RequestParam("id") String id,@RequestParam("token") String token){
//		String uid = req.getParameter("uid");
//		//返回数据总对象，0000表示成功，其它都表示错误
//		ResponseDbCenter res = new ResponseDbCenter();
//		String userid = StringUtils.isNotBlank(uid)?uid:req.getSession().getAttribute(token)+"";
//		String openId = userService.getUserById(userid).getWeixin();
//		String meetingId = req.getParameter("meetingId");
//		Exercise exercise = exerciseService.getExerciseAnswer(id, openId,meetingId);
//		if(null != exercise){
//			exercise.setUser(userService.getAnswerUser(id,openId));
//		}
//		res.setResModel(exercise);
//		return res;
//	}





//	/**
//	 * -------------------------------------------------------------------------------------------------------------
//	 * @author: gaoddO
//	 * @2017年3月22日
//	 * @功能描述：做问卷是收集用户信息
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/saveExerciseCollect")
//	public ResponseDbCenter saveExerciseCollect(HttpServletRequest req,@RequestParam("token") String token,
//															String uname,
//															@RequestParam("mobile") String mobile,
//															String companyName,
//															String email,
//															String profession,
//															String telCode){
//		//返回数据总对象，0000表示成功，其它都表示错误
//		ResponseDbCenter res = new ResponseDbCenter();
//		String code = (String) req.getSession().getAttribute("code"+mobile);
//		if(StringUtils.isBlank(telCode) || StringUtils.isBlank(code) || !code.equals(telCode) ){
//			return ResponseConstants.TEL_CODE_ERROR;
//		}
//		String userid = req.getSession().getAttribute(token)+"";
//		DC_User loginUser = userService.getUserById(userid);
//
//			loginUser.setUname(uname);
//			loginUser.setUpdatedBy(userid);
//
//			userService.saveUser(loginUser);
//
//			loginUser.setProfession(profession);
//			loginUser.setEmail(email);
//			loginUser.setMobile(mobile);
//			loginUser.setCompanyName(companyName);
//			userService.editUserDetail(loginUser);
//
//		return res;
//	}




//	/**
//	 *
//	 * @author: gaoddO
//	 * @2017年3月22日
//	 * @功能描述:保存用户答案
//	 * @return
//	 */
//	@Transactional
//	@ResponseBody
//	@RequestMapping("/saveUserAnswer")
//	public ResponseDbCenter saveUserAnswer(HttpServletRequest req,@RequestBody  String values){
//		ResponseDbCenter res = new ResponseDbCenter();
//		Map<String, Object> map1 = (Map<String, Object>) JSON.parseObject(values, Object.class);
//		//返回数据总对象，0000表示成功，其它都表示错误
//		String id = (String) map1.get("id");//试卷id
//		String token = req.getParameter("token");//用户id
//		String meetingId = (String) map1.get("meetingId");
//		//String clientName = (String) map1.get("clientName");
//		String uid = req.getSession().getAttribute(token)+"";
//		String openId = req.getSession().getAttribute("getOpenId"+token)+"";
//		map1.put("uid", uid);
//		exerciseService.deleteExamAnswer(id,openId);
//
//
//		List<Map> map = (List<Map>) map1.get("result");
//		UserExam exam  = new UserExam();
//		exam.setExerciseId(id);
//		exam.setOpenId(openId);
//		exam.setMeetingId(meetingId);
//		exam.setId(UUID.randomUUID().toString());
//		exerciseService.saveUserExam(exam);
//
//
//		for (int i = 0; i < map.size(); i++) {
//
//			UserAnswer obj = new UserAnswer();
//
//			String answer = (String) map.get(i).get("answer");
//			if(StringUtils.isNotBlank(answer)){
//				String qid = map.get(i).get("qid")+"";
//				int ttype = (int) map.get(i).get("ttype");
//
//				obj.setUserExamId(exam.getId());
//				obj.setQuestionId(qid);
//				obj.setTtype(ttype);
//				obj.setAnswer(answer);
//				obj.setOpenId(openId);
//				obj.setTextContent(null != map.get(i).get("textContent")?map.get(i).get("textContent")+"":"");
//				//obj.setOrderNumber(ttype == 10 ? (int)map.get(i).get("orderNumber"):0);
//				if(ttype == 10 || ttype == 12 ){
//					Object orderNumber2 = map.get(i).get("orderNumber");
//					if(null != orderNumber2){
//						int orderNumber = Integer.valueOf(orderNumber2.toString());
//						if(orderNumber==1000){
//							obj.setOrderNumber(0);
//						}else{
//							obj.setOrderNumber(orderNumber);
//						}
//					}
//				}else{
//					obj.setOrderNumber(0);
//				}
//
//				obj.setExerciseId(id);
//				obj.setSubmitDate(uid);
//				userAnswerService.saveUserAnswer(obj);
//			}
//		}
//
//
//		//map1.put("meetingId","62");
//		//根据表及其列插入数据
//		List<Map<String,Object>> qList = questionService.getQuerstionListByTranData(id);
//		if(qList != null && qList.size() > 0 && StringUtils.isNotBlank((String) map1.get("meetingId"))){
//			map1.put("questionList", qList);
//			map1.put("mobile",userService.getUserById(uid).getMobile());
//			ThreadDataWJ thread = new ThreadDataWJ(map1);
//			thread.start();
//		}
//		Exercise exercise = exerciseService.getExerciseById(id);
//		if("comment".equals(exercise.getCssType())){
//
//		}
//		res.setResModel(exercise.getPrompt());
//		return res;
//	}




	/**
	 *
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:保存用户答案
	 *
	 *   参数结构
	{
						"id": "06c08df6-8a34-4bef-9ac4-be1ba9faff4d",
						"meetingId": "06c08df6-8a34-4bef-9ac4-be1ba9faff4d",
						"result": [{
							"answer": "123",
							"qid": "06c08df6-8a34-4bef-9ac4-be1ba9faff4d",
							"ttype": "1",
							"textContent": "这里是内容",
							"orderNumber": 1

						}]
	}
	 *
	 *
	 * @return
	 */
	@ApiOperation(value = "保存试卷答案")
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/saveUserAnswer",method = RequestMethod.POST)
	public ResponseDbCenter saveUserAnswer(HttpServletRequest req,   @RequestBody   String values){
		String loginUserId = (String) req.getAttribute("loginUserId");

		ResponseDbCenter res = new ResponseDbCenter();
		Map<String, Object> map1 = (Map<String, Object>) JSON.parseObject(values, Object.class);
		//返回数据总对象，0000表示成功，其它都表示错误
		String id = (String) map1.get("id");//试卷id
 		String meetingId = (String) map1.get("meetingId");  //巡检任务ID
		//String clientName = (String) map1.get("clientName");
		String uid =loginUserId;
		//将openId用userId代替
		String openId = uid;
		map1.put("uid", uid);

		List<Map> map = (List<Map>) map1.get("result");
		UserExam exam  = new UserExam();
		exam.setExerciseId(id);
		exam.setOpenId(openId);
		exam.setMeetingId(meetingId);//认为它是巡检任务Id
		exam.setId(UUID.randomUUID().toString());
		exerciseService.saveUserExam(exam);


		for (int i = 0; i < map.size(); i++) {

			UserAnswer obj = new UserAnswer();

			String answer = (String) map.get(i).get("answer");
			if(StringUtils.isNotBlank(answer)){
				String qid = map.get(i).get("qid")+"";
				int ttype = (int) map.get(i).get("ttype");

				obj.setUserExamId(exam.getId());
				obj.setQuestionId(qid);
				obj.setTtype(ttype);
				obj.setAnswer(answer);
				obj.setOpenId(openId);
				obj.setTextContent(null != map.get(i).get("textContent")?map.get(i).get("textContent")+"":"");
				//obj.setOrderNumber(ttype == 10 ? (int)map.get(i).get("orderNumber"):0);
				if(ttype == 10 || ttype == 12 ){
					Object orderNumber2 = map.get(i).get("orderNumber");
					if(null != orderNumber2){
						int orderNumber = Integer.valueOf(orderNumber2.toString());
						if(orderNumber==1000){
							obj.setOrderNumber(0);
						}else{
							obj.setOrderNumber(orderNumber);
						}
					}
				}else{
					obj.setOrderNumber(0);
				}

				obj.setExerciseId(id);
				obj.setSubmitDate(uid);
				userAnswerService.saveUserAnswer(obj);
			}
		}


		//map1.put("meetingId","62");
		//根据表及其列插入数据
//		List<Map<String,Object>> qList = questionService.getQuerstionListByTranData(id);
//		if(qList != null && qList.size() > 0 && StringUtils.isNotBlank((String) map1.get("meetingId"))){
//			map1.put("questionList", qList);
//			map1.put("mobile",userService.getUserById(uid).getMobile());
//			ThreadDataWJ thread = new ThreadDataWJ(map1);
//			thread.start();
//		}
		Exercise exercise = exerciseService.getExerciseById(id);
		if("comment".equals(exercise.getCssType())){

		}
		res.setResModel(exercise.getPrompt());
		return res;
	}

//	/**
//	 * -------------------------------------------------------------------------------------------------------------
//	 * @author: gaoddO
//	 * @2017年3月22日
//	 * @功能描述:获取用户答题的对应区间评价
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/getUserComment")
//	public ResponseDbCenter getUserComment(HttpServletRequest req,@RequestParam("eid") String eid,@RequestParam("token") String token){
//		//返回数据总对象，0000表示成功，其它都表示错误
//		//主要用于测评类
//		ResponseDbCenter res = new ResponseDbCenter();
//		//String uid = redisCache.getCacheObject(token);
//		String openId =(String)req.getSession().getAttribute("getOpenId"+token);
//		Exercise exercise = exerciseService.getExerciseById(eid);
//		Map<String,String> gradeObj = exerciseCommentMapper.getCommentGrade(eid, openId);
//		String grade = "0";
//		if(null != gradeObj){
//			grade = gradeObj.get("grade");
//			Map<String, Object> comment = exerciseCommentMapper.getCommentByGrade(grade,eid);
//			if(null != comment){
//				String nameDes = comment.get("nameDes")+"";
//				exercise.setPrompt(nameDes.replace("[ScoreX]", grade+""));
//			}else if(null != exercise && StringUtils.isNotBlank(exercise.getPrompt())){
//				exercise.setPrompt(exercise.getPrompt().replace("[ScoreX]", grade+""));
//			}
//		}else{
//			if(null != exercise && StringUtils.isNotBlank(exercise.getPrompt())){
//				exercise.setPrompt(exercise.getPrompt().replace("[ScoreX]", grade+""));
//			}
//		}
//		exercise.setGrade(grade);
//		res.setResModel(exercise);
//		return res;
//	}

}
