package com.xkd.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.xkd.mapper.*;
import com.xkd.model.*;
import com.xkd.utils.ChartsFactory;
import com.xkd.utils.PropertiesUtil;
import com.xkd.utils.SysUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

@Service
public class ExerciseService {

	@Autowired
	ExerciseMapper m;

	@Autowired
	QuestionMapper q;

	@Autowired
	QuestionOptionMapper o;

	@Autowired
	UserAnswerMapper a;

	@Autowired
	UserExamMapper e;
	
	@Autowired
	ExerciseCommentMapper exerciseCommentMapper;


	public Exercise getExercise(String id,String openId) {

		Exercise eobj = m.getExercise(id,openId);
		if (eobj == null) {
			return null;
		}
		List<Question> qobj = null;
		String cssType = null;
		if(StringUtils.isNotBlank(eobj.getCssType()) && eobj.getCssType().equals("to")){
			qobj = q.getShowQuerstionList(id);
			cssType = "to";
		}else{
			qobj = q.getQuerstionList(id);
		}
		int index = 1;
		int oindex = 1;
		for (Question dc_WJ_Question : qobj) {
			int type = dc_WJ_Question.getTtype();
			if (type < 3 || (type >8 && type <15)) {
				dc_WJ_Question.setChild(o.getQuerstionOptList(dc_WJ_Question.getId(),cssType));
			}
			dc_WJ_Question.setKey(oindex++);
			if (type != 4 && type !=6 && type != 8) {// 描述题不需要题号
				dc_WJ_Question.setQindex(index++);
			}
		}
		eobj.setQuestion(qobj);
		if("comment".equals(eobj.getCssType()) ||"pxt".equals(eobj.getCssType())){
			eobj.setComment(exerciseCommentMapper.getCommentListByEid(eobj.getId()));
		}
		return eobj;
	}

	public Map<String, Object> getExerciseToString(String id) {

		Map<String, Object> map = new HashMap<>();
		List<Question> qobj = q.getQuerstionList(id+"");
		for (Question dc_WJ_Question : qobj) {

			int type = dc_WJ_Question.getTtype();
			if (type < 3 || (type >8 && type <15)) {
				List<QuestionOption> option =  o.getQuerstionOptList(dc_WJ_Question.getId(),null);
				for (QuestionOption dc_WJ_QuestionOption : option) {
					map.put(dc_WJ_Question.getId()+"_"+dc_WJ_QuestionOption.getId(),dc_WJ_QuestionOption.toString());
				}
			}
			map.put("question"+dc_WJ_Question.getId(), dc_WJ_Question.toString());

		}
		return map;

	}
	public List<Map<String, List>> getTablesList() {
		List<String> tables = new  ArrayList<String>();
		tables.add("dc_company");
		tables.add("dc_user_info");
		tables.add("dc_company_project");
		tables.add("dc_user_info");
		tables.add("dc_meeting_user");
		tables.add("dc_user_address");

		List<Map<String, List>> tablesList = new ArrayList<>();
		Map<String, List> maps;
		List<String> label;
		List<Map<String, String>> value = new ArrayList<>();
		for (String tableName : tables) {
			//获取列信息
			value = getTablesColumsList(tableName);
			label = new ArrayList<>();
			label.add(tableName);

			maps = new HashMap<>();

			maps.put("value", label);
			maps.put("label", label);
			maps.put("children", value);
			tablesList.add(maps);
		}
		//单独处理dc_label-------------------begin----------------
		maps = new HashMap<>();
		List<Map<String, String>> dc_label = new ArrayList<>();
		Map<String, String> map = new HashMap();
		map.put("label", "ttype=1拥有的资源");
		map.put("value", "1");

		Map<String, String> map2 = new HashMap();
		map2.put("label", "ttype=2企业家资源");
		map2.put("value", "2");

		Map<String, String> map3 = new HashMap();
		map3.put("label", "ttype=3需要的资源");
		map3.put("value", "3");

		dc_label.add(map);
		dc_label.add(map2);
		dc_label.add(map3);

		label = new ArrayList<>();
		label.add("dc_label");

		maps.put("value", label);
		maps.put("label", label);
		maps.put("children", dc_label);
		tablesList.add(maps);
		//单独处理dc_label-------------------end----------------

		return tablesList;
	}

	public List<Map<String, String>> getTablesColumsList(String tableName) {
		List<String> columns = (List<String>) SysUtils.selectTablesDropdown("show full columns from " + tableName);
		List<Map<String, String>> tableColums2List = new ArrayList<>();
		for (String string : columns) {
			String tables1 = string.substring(5, string.indexOf("    ", string.indexOf("    ") + 1));
			String tableColums = string.substring(string.lastIndexOf(" ") + 1);
			tables1 = tables1.replaceAll("   ", "");
			tableColums = tableColums.replaceAll(" ", "");
			String colums = "";
			String colums2 = "";
			Map<String, String> maps = new HashMap<>();
			if (colums.equals("") || colums == null) {
				if (tableColums.equals("")) {
					colums = tables1;
					maps = new HashMap<>();
					maps.put("value", colums);
					maps.put("label", colums);
				} else {
					colums = tables1 + "," + tableColums;
					colums2 = tables1;
					maps = new HashMap<>();
					maps.put("value", colums2);
					maps.put("label", colums);
				}

			} else {
				colums += tables1 + tableColums;
				colums2 += tables1;
				maps = new HashMap<>();
				maps.put("value", colums2);
				maps.put("label", colums);
			}
			if (colums == null || colums.equals("")) {
			} else {
				tableColums2List.add(maps);
			}
		}
		return tableColums2List;
	}

	public Exercise getExerciseAnswer(String id, String openId,String meetingId) {

		Exercise eobj = m.getExercise(id,openId);
		if (eobj == null) {
			return null;
		}

		meetingId = StringUtils.isNotBlank(meetingId)?meetingId:null;
		UserExam exam = e.getNewsExer(id, openId,meetingId);// 获取用户最新一次做题记录
		String examId = exam == null ? "0" : exam.getId();
		eobj.setSubmitDate(exam == null ? null:exam.getSubmitDate());
		List<Question> qobj = q.getQuerstionAnswerList(id, examId);
		int index = 1;
		int oindex = 1;
		int pxtCnt = 0;
		for (Question dc_WJ_Question : qobj) {
			int type = dc_WJ_Question.getTtype();
			if (type  == 1 || type ==2 || (type >8 && type <15)||type==16) {
				List<QuestionOption> listOption = null;
				if(type == 10){
					listOption = o.getQuerstionOptAnswerList(dc_WJ_Question.getId(), examId,"o.grade");
					pxtCnt +=listOption.size();
				}else if(type == 12){
					
					List<UserAnswer> aa = a.getDuoXuanAnser(dc_WJ_Question.getId() + "", examId);
					dc_WJ_Question.setAnswerList(aa);
				}else if (type==16){
					List<UserAnswer> aa = a.getDuoXuanAnser(dc_WJ_Question.getId() + "", examId);
					dc_WJ_Question.setAnswerList(aa);
				}else{
					listOption = o.getQuerstionOptAnswerList(dc_WJ_Question.getId(), examId,"o.level");
				}
				dc_WJ_Question.setChild(listOption);// 题目下的但选题选项
				/*if (type == 2) {// 多选题的答案集合
					List<UserAnswer> aa = a.getDuoXuanAnser(dc_WJ_Question.getId() + "", examId);
					dc_WJ_Question.setUserAnswer(aa.toString());
				}*/
			}
			dc_WJ_Question.setKey(oindex++);

			if (type  != 4 && type !=6 && type != 8) {// 描述题不需要题号
				dc_WJ_Question.setQindex(index++);
			}
		}
		if( StringUtils.isNotBlank(eobj.getCssType()) && eobj.getCssType().equals("pxt") ){
			int grade = exerciseCommentMapper.getPaixutiGrade(examId, eobj.getId()+"");
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(2);
			Map<String, Object> comment = exerciseCommentMapper.getCommentByGrade(grade+"",eobj.getId());
			if(null != comment){
				
				eobj.setPrompt(0 == grade ?(comment.get("nameDes")+"").replace("[ScoreX]", "0").replace("[ScoreY]","0%"):
					(comment.get("nameDes")+"").replace("[ScoreX]",grade+"").replace("[ScoreY]",(numberFormat.format( (Float.valueOf(grade) /  (float) pxtCnt)*100))+"%"));
			}else if(StringUtils.isNotBlank(eobj.getPrompt())){
				eobj.setPrompt(0 == grade ?eobj.getPrompt().replace("[ScoreX]", "0").replace("[ScoreY]","0%"):
					eobj.getPrompt().replace("[ScoreX]",grade+"").replace("[ScoreY]",(numberFormat.format( (Float.valueOf(grade) /  (float) pxtCnt)*100))+"%"));
			}
		}
		eobj.setQuestion(qobj);
		return eobj;

	}

	@Transactional
	public String saveExercise(Exercise eobj,String uid) {
	    if(StringUtils.isBlank(eobj.getTitle())){

	    	 return "0";
	    }
	    //开始保存试卷
		if(StringUtils.isBlank(eobj.getId()) && !eobj.getId().equals("0")){
			eobj =  m.getExercise(eobj.getId()+"",null);
		}
		if(eobj == null){
			eobj = new Exercise();
		}
		eobj.setCreatedBy(uid);
		
		Map<String, Object> mapS = new HashMap();
		if(StringUtils.isNotBlank(eobj.getId()) &&!eobj.getId().equals("0")){
			m.editExercise(eobj);//修改试卷
			mapS = getExerciseToString(eobj.getId()+"");
		}else{
			eobj.setId(UUID.randomUUID().toString());
			m.saveExercise(eobj);//保存试卷
		}
		List<Question> qobj= (List<Question>) eobj.getQuestion();
		//保存模块or没有模块的题目
		for (int i = 0; i < qobj.size(); i++) {

			int type = qobj.get(i).getTtype();
			String content = qobj.get(i).getName();
			if(StringUtils.isBlank(type+"")||(StringUtils.isBlank(content) && type != 8)){
				return "0";
			}
			/*Question qobj = new Question();

			int level = map.get(i).getLevel();
			qobj.setId(map.get(i).getId());
			qobj.setExerciseId(eobj.getId());
			qobj.setLevel(level);
			qobj.setName(content);
			qobj.setTtype(type);
			Object lengthSize = map.get(i).getLengthSize();
			Object checkType = map.get(i).getCheckType();
			if(lengthSize !=null){
				qobj.setLengthSize(Integer.valueOf(lengthSize.toString()));
			}
			Object score = map.get(i).getScore();
			if(score !=null){
				qobj.setScore(Integer.valueOf(score.toString()));
			}
			qobj.setCheckType(checkType == null ? null:checkType.toString());
			Object size = map.get(i).getSize();
			qobj.setSize(size == null ? "" : size.toString());
			Object placeholder = map.get(i).getPlaceholder();
			qobj.setPlaceholder(placeholder == null ? "" :placeholder.toString());
			Object isCheck = map.get(i).getIsCheck();
			qobj.setIsCheck(isCheck == null ? null:isCheck.toString());
			qobj.setRemark(map.get(i).getRemark() == null ? "" :map.get(i).getRemark());
			int flag = (int)map.get(i).getFlag();//是否配置数据源 1为配置
			if(type == 2){
				Object answerType = map.get(i).getAnswerType();
				if(null != answerType){
					qobj.setAnswerType(Integer.valueOf(answerType.toString()));
				}
			}
			if(flag == 1 && (type <3 || type == 5 || type ==7)){
				qobj.setFlag(flag);
				String tableAndColumn1 =  (String) map.get(i).getTableAndColumn();
				String [] tableAndColumn = (tableAndColumn1).split(",");//获取表和列
				if(tableAndColumn.length > 1){
					qobj.setTableColum(tableAndColumn[1]);
					qobj.setTableName(tableAndColumn[0]);
				}
			}*/
			System.out.println(qobj.get(i).toString());
			System.out.println(mapS.get("question"+qobj.get(i).getId()));
			qobj.get(i).setExerciseId(eobj.getId());
			if(qobj.get(i).getId().equals("0") ){
				qobj.get(i).setId(UUID.randomUUID().toString());
				q.saveQuerstion(qobj.get(i));
			}else if(!qobj.toString().equals(mapS.get("question"+qobj.get(i).getId()))){
				mapS.remove("question"+qobj.get(i).getId());
				q.editQuerstion(qobj.get(i));
			}else{
				mapS.remove("question"+qobj.get(i).getId());
			}

			List<QuestionOption> opt =  qobj.get(i).getOpt();



				//没有模块的单选跟多选的选项或者得分题和排序题
				if(type < 3 || (type >8 && type <15)){
					for (int j = 0; j < opt.size(); j++) {
						if(StringUtils.isNotBlank(opt.get(j).getOpt())){//如果选项值为空就不保存拉
							opt.get(j).setQuestionId(qobj.get(i).getId());
							if(opt.get(j).getId().equals("0") ){
								o.saveQuerstionOpt(opt.get(j));
							}else if(!opt.get(j).toString().equals(mapS.get(qobj.get(i).getId()+"_"+opt.get(j).getId()))){
								mapS.remove(qobj.get(i).getId()+"_"+opt.get(j).getId());
								o.editQuerstionOpt(opt.get(j));
							}else{
								mapS.remove(qobj.get(i).getId()+"_"+opt.get(j).getId());
							}
						}
					}
				}

		}
		if(("comment").equals(eobj.getCssType())||("pxt").equals(eobj.getCssType())){
			List<Map<String, Object>> commentList = (List<Map<String, Object>>) eobj.getComment();
			if(null == commentList){
				return "0";
			}
			if(mapS!=null){
				//
				exerciseCommentMapper.deleteCommentByEid(eobj.getId());
			}
			for (Map<String, Object> map2 : commentList) {
				Map<String, Object> comment = new HashMap();
				comment.put("eid",eobj.getId());
				comment.put("name", map2.get("name"));
				comment.put("nameDes", map2.get("nameDes"));
				comment.put("beginGrade", map2.get("beginGrade"));
				comment.put("endGrade", map2.get("endGrade"));
				exerciseCommentMapper.saveComment(comment);
			}


		}
		if(mapS !=null){
			Set<Entry<String, Object>> entries = mapS.entrySet();
			for (Entry<String, Object> map2 : entries) {
				if(map2.getKey().contains("_")){
					o.delOptById(map2.getKey().split("_")[1]);
				}else if(map2.getKey().contains("question")){
					q.delQuerstionOptByQid(map2.getKey().split("question")[1]);
				}
			}
		}


		return eobj.getId();
	}

	public int delExerciseById(List<String> ids) {
		return m.delExerciseById(ids);
	}

	public List<Exercise> getUserExercise(Map<String,Object> map) {


		return m.getUserExercise(map);
	}

	public int saveUserExam(UserExam obj) {
		e.deleteByMeetingId(obj.getMeetingId());
		return e.saveUserExam(obj);

	}

	public Exercise getExerciseChart(String id) {
		Exercise eobj = m.getExercise(id,null);
		if (eobj == null) {
			return null;
		}
		List<Question> qobj = q.getQuerstionList(id);
		int index = 1;
		int oindex = 1;
		for (Question dc_WJ_Question : qobj) {
			int type = dc_WJ_Question.getTtype();
			Map<String, Object> params = new HashMap<>();
			dc_WJ_Question.setKey(oindex++);
			String qid = dc_WJ_Question.getId();
			String cnt = eobj.getCnt();//做题人数
			if (type ==1 || type ==2 ) {
				dc_WJ_Question.setQindex(index++);
				params.put("qid", qid);//通过qid和cnt去统计用户答案百分比分析
				params.put("cnt", cnt);
				dc_WJ_Question.setChild(o.getQuerstionOptChart(params));
			}else if (type == 5 || type == 7 || type == 12) {//直接加载答案
				dc_WJ_Question.setQindex(index++);
				dc_WJ_Question.setAnswerList(a.getUserAnswerByQid(qid));
			}else if(type == 10){//排序题
				dc_WJ_Question.setQindex(index++);
				dc_WJ_Question.setChild(o.getPaiXTTongJi(qid,cnt));
			}else if(type == 11){
				dc_WJ_Question.setQindex(index++);
				List<QuestionOption> qu = o.getDeFTTongJi(qid,cnt);
				HashMap<Integer, QuestionOption> quMap = new HashMap<>();
				if(null != qu && qu.size() > 0){
					for (QuestionOption q : qu) {
						quMap.put(q.getGrade(), q);
					}
				}
					for (int i = 0; i <= dc_WJ_Question.getScore(); i++) {
						if(null == quMap.get(i)){
							QuestionOption q = new QuestionOption();
							q.setGrade(i);
							q.setOrderNumber(0);
							q.setChart(new BigDecimal(0));
							qu.add(q);
						}
					}

				dc_WJ_Question.setChild(qu);

			} /*
			模块下的题目
			else if (type == 6) {
				List<DC_WJ_Question> qobj2 = q.getQuerstionOptListByPid(dc_WJ_Question.getId());
				dc_WJ_Question.setOpt(qobj2);

				for (DC_WJ_Question dc_WJ_Question2 : qobj2) {
					int type2 = dc_WJ_Question2.getTtype();
					if (type2 < 3 || type2 == 9 || type2 == 10) {// 只有单选跟多选才需要去加载选项
						params.put("qid", dc_WJ_Question2.getId());
						params.put("cnt", eobj.getCnt());
						dc_WJ_Question2.setChild(o.getQuerstionOptChart(params));
					} else if (type2 == 3) {
						List<DC_WJ_Question> queList = q.getQuerstionOptListByPid(dc_WJ_Question2.getId());
						dc_WJ_Question2.setAnswerList(a.getUserAnswerByQid(dc_WJ_Question2.getId()));
						for (DC_WJ_Question dc_WJ_Question3 : queList) {
							dc_WJ_Question3.setAnswerList(a.getUserAnswerByQid(dc_WJ_Question3.getId()));// 查询复合题的答案
						}
						dc_WJ_Question2.setOpt(queList);
					} else if (type2 == 5) {
						dc_WJ_Question2.setAnswerList(a.getUserAnswerByQid(dc_WJ_Question2.getId()));
					}
					dc_WJ_Question2.setKey(oindex++);
					if (type2 != 4 && type2 !=6 && type2 != 8) {// 描述题不需要题号
						dc_WJ_Question2.setQindex(index++);
					}
				}
			}

			if (type != 4 && type !=6 && type != 8) {// 描述题不需要题号
				dc_WJ_Question.setQindex(index++);
			}
			if (type == 5 || type == 7 || type == 11) {
				dc_WJ_Question.setAnswerList(a.getUserAnswerByQid(dc_WJ_Question.getId()));
			}*/
		}
		eobj.setQuestion(qobj);
		return eobj;
	}

	public Exercise getExerciseById(String eid) {
		return m.getExercise(eid,null);
	}

	public Exercise getUserListAnswer(String id) {
		List<HashMap<String, Object>> map = a.getUserListAnswer(id);//查询出所有的用户
		for (HashMap<String, Object> hashMap : map) {
			hashMap.put("userAnswer", a.getUserLists(hashMap.get("eid")));
		}
		Exercise eobj = m.getExercise(id,null);
		if (eobj == null) {
			return null;
		}
		eobj.setAnswerList(map);
		return eobj;

	}

	public Exercise getUserAnswer(String id, String openId,String uid,String meetingId) {
		Exercise eobj = m.getExercise(id,null);
		if (eobj == null) {
			return null;
		}
		UserExam exam = e.getNewsExer(id, openId,meetingId);
		   
		eobj.setAnswerList(a.getUserLists(exam == null ?null:exam.getId()));
		return eobj;
	}

	public List<HashMap<String, Object>> getExerciseZhengQueLv(String id) {
		
		return a.getExerciseZhengQueLv(id);
	}

	public Exercise getUserAnswerZhiZhuTu(String openId, String id) {
		UserExam exam = e.getNewsExer(id, openId,null);// 获取用户最新一次做题记录
		String examId = exam == null ? "0" : exam.getId();
		List<HashMap<String, Object>> map = q.getUserAnswerZhiZhuTu(examId);
		Exercise obj = m.getExercise(id,null);
		obj.setAnswerList(map);
		return obj;
	}

	public void copyExercise(String id,String uid) {
			Exercise obj = m.getExercise(id,null);
			obj.setId(UUID.randomUUID().toString());
			obj.setTitle(obj.getTitle().contains("(1)") ? (obj.getTitle()+"-副本" ):(obj.getTitle()+"(1)"));
			if(StringUtils.isNotBlank(obj.getCssType()) && obj.getCssType().equals("to")){
				obj.setCssType("wcdt");
			}
			obj.setCreatedBy(uid);
			m.saveExercise(obj);
			List<Question> questionList = q.getQuerstionList(id);
			for (Question dc_WJ_Question : questionList) {
				dc_WJ_Question.setExerciseId(obj.getId());
				String qid = dc_WJ_Question.getId();
				dc_WJ_Question.setId(UUID.randomUUID().toString());

				q.saveQuerstion(dc_WJ_Question);
				if(dc_WJ_Question.getTtype() < 3 || dc_WJ_Question.getTtype() == 7 || (dc_WJ_Question.getTtype() >8 && dc_WJ_Question.getTtype() < 15)){
					List<QuestionOption> optionList = o.getQuerstionOptList(qid,null);
					for (QuestionOption dc_WJ_QuestionOption : optionList) {
						dc_WJ_QuestionOption.setQuestionId(dc_WJ_Question.getId());
						dc_WJ_QuestionOption.setToQuestion("");
						o.saveQuerstionOpt(dc_WJ_QuestionOption);
					}
				}
			}
			if(null != obj.getCssType() && (obj.getCssType().equals("comment")|| obj.getCssType().equals("pxt"))){
				List<Map<String, Object>> commentList = exerciseCommentMapper.getCommentListByEid(id);
				for (Map<String, Object> comment : commentList) {
					comment.put("eid", obj.getId());
					exerciseCommentMapper.saveComment(comment);
				}
			}
	}

	public int getUserExerciseTotal(Map<String,Object> map) {
		return m.getUserExerciseTotal(map);
	}

	public List<HashMap<String, Object>> getExerciseUserAll(String id,String uname,int pageNo,
			int pageSize) {

		return m.getExerciseUserAll(id,uname,pageNo,pageSize);
	}

	public int getExerciseUserAllTotal(String id, String uname) {
		return m.getExerciseUserAllTotal(id, uname);
	}

	public void deleteExamAnswer(String id, String uid) {

		e.deleteExamAnswer(id,uid);
	}

	public List<Exercise> getMeetingExercise(List<String> ttypes, String title, String meetingId, int pageNo,
			int pageSize) {

		return m.getMeetingExercise(ttypes,title,meetingId,pageNo,pageSize);
	}

	public int ggetMeetingExerciseTotal(List<String> ttypes, String title, String meetingId, int pageNo,
			int pageSize) {
		return m.getMeetingExerciseTotal(ttypes,title,meetingId,pageNo,pageSize);
	}

	public Object exerciseWritePdf(String id,String openId) {
		Exercise exercise = m.getExercise(id, null);
		String pdfUrl = PropertiesUtil.FILE_UPLOAD_PATH +"exercise/"+exercise.getTitle()+".pdf";
		File file = new File(pdfUrl);
		
		
		try {
			Document pdfDoc = new Document();  
			FileOutputStream pdfFile = new FileOutputStream(file);
			BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED); 
			Font titleFont = new Font(bf, 14, Font.NORMAL);
		    Font fontS = new Font(bf, 12, Font.NORMAL);
		    
		    Font okFont = new Font(bf, 12, Font.NORMAL);
		    okFont.setColor(BaseColor.BLUE);
		    
		    
			PdfWriter.getInstance(pdfDoc, pdfFile);  
			pdfDoc.open();  // 打开 Document 文档 
			
			//试卷标题
			Paragraph paragraph = new Paragraph(exercise.getTitle(),titleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			pdfDoc.add(paragraph); 
			
			//添加一行空格
			Paragraph paragraph2 = new Paragraph("              ",fontS);
			pdfDoc.add(paragraph2);
			
			//添加用户答题时间
			UserExam exam = e.getNewsExer(id, openId,null);// 获取用户最新一次做题记录
			String examId = exam == null ? "0" : exam.getId();
			String uname = exercise.getUname();
			Paragraph paragraph1 = new Paragraph("答题用户："+uname+"   答题时间："+exam.getSubmitDate()+"  答题题数："+exercise.getQuestionSum(),fontS);
			paragraph1.setAlignment(Element.ALIGN_CENTER);
			pdfDoc.add(paragraph1); 
			
			//添加一行空格
			Paragraph paragraph20 = new Paragraph("              ",fontS);
			pdfDoc.add(paragraph20);
			
			
			if(StringUtils.isNotBlank(exercise.getChildTitle())){
				//添加试卷前言
				Paragraph paragraph3 = new Paragraph(exercise.getChildTitle(),fontS);
				pdfDoc.add(paragraph3);
			}
			if(exercise.getTtype().equals("zzt")){
				//封装雷达图参数
				List<HashMap<String, Object>> map = q.getUserAnswerZhiZhuTu(examId);
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				
				for (HashMap<String, Object> hashMap : map) {
					dataset.addValue(Integer.valueOf(hashMap.get("grade").toString()), uname, hashMap.get("name").toString());
				}
				
				String imgUrl = PropertiesUtil.FILE_UPLOAD_PATH +"exercise/images/"+exercise.getId()+".png";
				ChartsFactory.saveAsFile(imgUrl, 900, 450,dataset);
				Image image = Image.getInstance(imgUrl); 
				image.scaleAbsolute(600,300);// 直接设定显示尺寸
				image.setAlignment(Image.ALIGN_CENTER);
				paragraph2.add(image);
				pdfDoc.add(paragraph2);
			}
			Paragraph paragraph5 = null;
			
			List<Question> questions = q.getQuerstionAnswerList(id, examId);
			int index = 0;
			Map<Integer, String> exerciseType = new HashMap<>();
			exerciseType.put(1, "单选题");
			exerciseType.put(2, "多选题");
			exerciseType.put(5, "问答题");
			exerciseType.put(7, "标签题");
			exerciseType.put(10, "排序题");
			exerciseType.put(11, "分值题");
			exerciseType.put(12, "填空题");
			
			for (Question dc_WJ_Question : questions) {
				int type = dc_WJ_Question.getTtype();
				
				paragraph5 = new Paragraph("                            ");
				pdfDoc.add(paragraph5);
				if (type  != 4 && type != 8) {// 描述题不需要题号
					dc_WJ_Question.setQindex(index++);
					//序号,题干
					if(12 == type){
						Chunk chunk1 = new Chunk("This text is underlined", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.UNDERLINE));
						pdfDoc.add(chunk1);
					}else{
						paragraph5 = new Paragraph(" Q"+index+"、【"+exerciseType.get(type)+"】 "+dc_WJ_Question.getName(),fontS);
						pdfDoc.add(paragraph5);
					}
					
				}else{
					//描述题
					if(4 == type){
						paragraph5 = new Paragraph(" "+dc_WJ_Question.getName(),fontS);
						pdfDoc.add(paragraph5);
					}else{//虚线
						paragraph5 = new Paragraph("———————————————————————————————————————————");
						pdfDoc.add(paragraph5);
					}
				}
				//答案
				if(type == 5 || type == 7){
					paragraph5 = new Paragraph("    回答： "+dc_WJ_Question.getUserAnswer()+"\r\n",okFont);
					pdfDoc.add(paragraph5);
				}else if(type == 1 || type == 2  || type == 10 || type == 11 ){
					List<QuestionOption> listOption = o.getQuerstionOptAnswerList(dc_WJ_Question.getId(), examId,"o.level");
					String okAnswer = " ";
					String errorAnswer = " ";
					for (QuestionOption questionOption : listOption) {
						if(10 == type){
							okAnswer +=questionOption.getGrade()+"、";
							errorAnswer+=questionOption.getOrderNumber()+"、";
							paragraph5 = new Paragraph("     "+questionOption.getOpt()+"\r\n",fontS);
							pdfDoc.add(paragraph5);
						}else{
							if(StringUtils.isNotBlank(questionOption.getUserAnswer())){
								paragraph5 = new Paragraph("     "+questionOption.getOpt()+"\r\n",okFont);
								pdfDoc.add(paragraph5);
							}else{
								paragraph5 = new Paragraph("     "+questionOption.getOpt()+"\r\n",fontS);
								pdfDoc.add(paragraph5);
							}
						}
						
					}
					if(11 == type){
						paragraph5 = new Paragraph("    回答： "+dc_WJ_Question.getUserAnswer()+"\r\n",okFont);
						pdfDoc.add(paragraph5);
					}else if(10 == type){
						paragraph5 = new Paragraph("  正确答案： "+okAnswer.substring(0, errorAnswer.length()-1)+"\r\n",fontS);
						pdfDoc.add(paragraph5);
						paragraph5 = new Paragraph("  用户答案： "+errorAnswer.substring(0, errorAnswer.length()-1)+"\r\n",okFont);
						pdfDoc.add(paragraph5);
					}
				}else if(type == 12){
					List<UserAnswer> aa = a.getDuoXuanAnser(dc_WJ_Question.getId() + "", examId);
					for (UserAnswer userAnswer : aa) {
						paragraph5 = new Paragraph("    回答： "+(userAnswer.getOrderNumber()+1)+"   "+userAnswer.getAnswer()+"             ",okFont);
						pdfDoc.add(paragraph5);
					}
				}
				
			}
			pdfDoc.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PropertiesUtil.FILE_HTTP_PATH +"exercise/"+exercise.getTitle()+".pdf";
	}

	public Object exerciseWriteExcel(String id) {
		Exercise exercise = m.getExercise(id, null);


		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(exercise.getTitle());
		sheet.setDefaultColumnWidth(500);  
		sheet.setDefaultRowHeightInPoints(20);  
		XSSFCellStyle style_title = wb.createCellStyle();
		XSSFFont fontHeader=wb.createFont();
		fontHeader.setFontHeightInPoints((short)12);
		fontHeader.setBold(true);
		//字体名称
		fontHeader.setFontName("宋体");
		style_title.setFont(fontHeader);
		style_title.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("index","序号");
		map.put("uname","用户姓名");
		map.put("mobile","联系方式");
		map.put("submitDate","答题时间");
		map.put("questionSum","答题数");

		XSSFRow row = sheet.createRow(0);
		row.setHeightInPoints(30);
		XSSFCell cell = null;
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		cell = row.createCell(0);
		cell.setCellValue(exercise.getTitle());
		cell.setCellStyle(style);
		
		row = sheet.createRow(1);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
		cell = row.createCell(0);
		cell.setCellValue("本题目由 "+exercise.getUname()+" 创建；创建时间为"+exercise.getCreateDate()+"；共 "+exercise.getQuestionSum()+" 题");
		cell.setCellStyle(style);
		
		
		
		row = sheet.createRow(2);
		int cell_0 = 0;
		for (String key : map.keySet()) {
			cell = row.createCell(cell_0);
			cell.setCellValue(map.get(key));
			cell.setCellStyle(style_title);
			cell_0 ++;
		}
		List<HashMap<String, Object>> list = m.getExerciseUserAll(id,null,0,1000);
		if(null != list && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(i+3);
				cell_0 = 0;

				for (String key : map.keySet()) {
					cell = row.createCell(cell_0);
					if(key.equals("index")){
						cell.setCellValue((i+1));
					}else{
						Object value = list.get(i).get(key);
						if(null == value){
							cell.setCellValue("--");
						}else{
							cell.setCellValue(value+"");
						}
					}
					cell.setCellStyle(style);
					cell_0 ++;
				}
			}
		}

		String path =PropertiesUtil.FILE_UPLOAD_PATH+"/exercise/"+id+".xlsx";
		FileOutputStream outputStream = null;
		try {
			// //给文件夹设置读写修改操作
			File  dir = new File(PropertiesUtil.FILE_UPLOAD_PATH);
			String os = System.getProperty("os.name");
			if (!dir.exists()) {
				dir.mkdirs();  //如果文件不存在  在目录中创建文件夹   这里要注意mkdir()和mkdirs()的区别
				if(!os.toLowerCase().startsWith("win")){
					Runtime.getRuntime().exec("chmod 777 " + dir.getPath());
				}
			}

			outputStream = new FileOutputStream(path);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

			//给文件设置读写修改操作
			File targetFile = new File(path);

			if(targetFile.exists()){

				targetFile.setExecutable(true);//设置可执行权限
				targetFile.setReadable(true);//设置可读权限
				targetFile.setWritable(true);//设置可写权限
				String saveFilename = targetFile.getPath();
				if(!os.toLowerCase().startsWith("win")){

					Runtime.getRuntime().exec("chmod 777 " + saveFilename);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return null;
		}

		return PropertiesUtil.FILE_HTTP_PATH+"/exercise/"+id+".xlsx";
	
	}

	public Exercise getShowExercise(String id, String openId) {
		Exercise eobj = m.getExercise(id,openId);
		if (eobj == null) {
			return null;
		}
		List<Question> qobj = q.getShowQuerstionList(id);
		int index = 1;
		int oindex = 1;
		for (Question dc_WJ_Question : qobj) {
			int type = dc_WJ_Question.getTtype();
			if (type < 3 || (type >8 && type <15)) {
				dc_WJ_Question.setChild(o.getQuerstionOptList(dc_WJ_Question.getId(),null));
			}
			dc_WJ_Question.setKey(oindex++);
			if (type != 4 && type !=6 && type != 8) {// 描述题不需要题号
				dc_WJ_Question.setQindex(index++);
			}
		}
		eobj.setQuestion(qobj);
		if("comment".equals(eobj.getCssType()) ||"pxt".equals(eobj.getCssType())){
			eobj.setComment(exerciseCommentMapper.getCommentListByEid(eobj.getId()));
		}
		return eobj;
	}
}
