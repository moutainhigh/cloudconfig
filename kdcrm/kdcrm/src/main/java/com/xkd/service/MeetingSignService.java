package com.xkd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.MeetingSignMapper;
import com.xkd.model.Company;
import com.xkd.utils.DateUtils;

@Service
public class MeetingSignService {

	
	@Autowired
	private MeetingSignMapper meetingSignMapper;
	
	public List<HashMap<String, Object>> getCompanyList(String id,String uid,String province,String sonIndustryid, int pageNo, int pageSize) {
		
		
		return meetingSignMapper.getCompanyList(id,uid,province,sonIndustryid,pageNo,pageSize);
	}

	public int getTotalRows(String id,String province,String sonIndustryid){
		return meetingSignMapper.getTotalRows(id,province,sonIndustryid);
	}

	public HashMap<String, Object> getMyCompany(String uid,String companyId,String meetingid) {
		//我的企业信息or我查询的企业信息包含（地址，分组，试卷，是否关注，企业信息等）
		HashMap<String, Object> map = meetingSignMapper.getMyCompany(uid,companyId,meetingid);
		if(map == null){
			return null;
		}
		//课堂试卷
		//map.put("meetingExercise", meetingSignMapper.meetingExercise(meetingid));
		
		
		//我需要的资源，我有的资源
//		List<HashMap<String, Object>> getLabel = meetingSignMapper.getLable(companyId);
//		map.put("label1", "");
//		map.put("label2", "");
//		map.put("label3", "");
//		for (HashMap<String, Object> hashMap : getLabel) {
//			map.put("label"+hashMap.get("ttype"), hashMap.get("labels"));
//		}
		//猜你喜欢 我的关注人数
//		if(StringUtils.isBlank(companyId)){
//			List<HashMap<String, Object>> fllowMap = meetingSignMapper.getMeetFllowCnt(uid,companyId,meetingid);
//			map.put("ppCnt", 0);
//			map.put("gzCnt", 0);
//			for (HashMap<String, Object> fllow : fllowMap) {
//				map.put(fllow.get("ttype")+"Cnt", fllow.get("cnt"));
//			}
//		}
		companyId = map.get("id").toString();
		//我的顾问和总监
		List<HashMap<String, Object>> mapAdviser = meetingSignMapper.getCompanyAdviser(map.get("id"));
		map.put("adviser", mapAdviser);
 
		
		Object userlevel = map.get("userlevel");
		map.put("userlevel",userlevel == null ? "":userlevel);
		Object address = map.get("address");
		map.put("address",address == null ? "":address);
		Object label = map.get("label");
		map.put("label",label == null ? "":label);
		Object website = map.get("website");
		map.put("website",website == null ? "":website);
		Object establish_time = map.get("establish_time");
		map.put("establish_time",establish_time == null ? "":establish_time);
		Object representative = map.get("representative");
		map.put("representative",representative == null ? "":representative);
		Object manageScope = map.get("manageScope");
		map.put("manageScope",manageScope == null ? "":manageScope);
		Object content = map.get("content");
		map.put("content",content == null ? "":content);
		return map;
	}
	
	

	public int followCompany(String companyid, String uid,String meetingid) {
		
		HashMap<String, Object> follow = meetingSignMapper.getMeetingFollowById(uid,companyid,meetingid);
		if(null == follow){
			follow = new HashMap<>();
			follow.put("mycid",uid);
			follow.put("mid",meetingid);
			follow.put("cid", companyid);
			follow.put("ttype","gz");
			meetingSignMapper.insertMeetingFollow(follow);
			
			return 1;
		}else{
			meetingSignMapper.delMeetingFollow(follow.get("id"));
			
			return 0;
		}
		
	}

	public List<HashMap<String, Object>> getMyFollowCompany(String uid, String ttype,String meetingId) {
		List<HashMap<String, Object>> map = null;
		if(ttype.equals("gz")){
			map = meetingSignMapper.getMyFollowCompany(uid,meetingId);
		}else if(ttype.equals("pp")){
			map = meetingSignMapper.getMyCompanyPiPei(uid,meetingId);
		}else if(ttype.equals("group")){
			map = meetingSignMapper.getMyCompanyGroup(uid,meetingId);
		}
		
 
		return map;
	}

	public List<HashMap<String, Object>> meetingExercise(String meetingid) {
		return meetingSignMapper.meetingExercise(meetingid);
	}

	public int updateCompany(HashMap<String, Object> company) {
		meetingSignMapper.updateCompanyDetail(company);
		return meetingSignMapper.updateCompany(company);
		
	}

	/*public int updateCompanyLabels(HashMap<String, Object> label) {
		return meetingSignMapper.updateCompanyLabels(label);
	}*/

	public HashMap<String, List<HashMap<String, Object>>> getMeetingCompanyIndustryAndCity(String meetingId) {
		HashMap<String, List<HashMap<String, Object>>> map = new HashMap<>();
		map.put("Industry", meetingSignMapper.getMeetingCompanyIndustry(meetingId));
		map.put("City", meetingSignMapper.getMeetingCompanyCity(meetingId));
		return map;
	}

	public List<HashMap<String, Object>> getAdviserList() {
		List<HashMap<String, Object>> map = meetingSignMapper.getAdviserList();
		int index = 0;
		for (HashMap<String, Object> hashMap : map) {
			hashMap.put("index", index++);
		}
		return map;
	}

	public int saveDictionary(Object cacheObject, String content, String meetingId) {
		
		return 0;
	}

	/*public List<HashMap<String, Object>> getSignCompany(String meetingId, String begin_data, String end_date) {
		return meetingSignMapper.getSignCompany(meetingId,begin_data,end_date);
	}*/

	public List< HashMap<String, Object>> getUserFilterCompnay(String meetingId, String uid, List<String> hangye, List<String> xuqiu,String provinceId) {
		return meetingSignMapper.getUserFilterHangYe(meetingId,hangye,uid,xuqiu,provinceId);
	}

	public Company getCompanyByMeetingIdAndUid(String meetingId, String uid) {
		return meetingSignMapper.getCompanyByMeetingIdAndUid(meetingId,uid);
	}
}
