package com.kuangchi.sdd.visitorConsole.checkUserLogin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.visitorConsole.checkUserLogin.dao.CheckUserLoginDao;
import com.kuangchi.sdd.visitorConsole.checkUserLogin.service.CheckUserLoginService;

@Service("checkUserLoginServiceImpl")
public class CheckUserLoginServiceImpl implements CheckUserLoginService {
	@Resource(name = "checkUserLoginDaoImpl")
	private CheckUserLoginDao checkUserLogindao;
	@Override
	public boolean checkUserLogin(Map map) {
		return checkUserLogindao.checkUserLogin(map);
	}
	@Override
	public boolean ifBlackList(Map map) {
		return checkUserLogindao.ifBlackList(map);
	}
	@Override
	public List<Map> getRecordInfoByCardNum(Map map) {
			return checkUserLogindao.selectRecordInfoByCardnum(map);
		
	}
	@Override
	public boolean ifPassiveBook(Map map) {
		return checkUserLogindao.ifPassiveBook(map);
	}
	@Override
	public Map queryVisitorByNum(Map map) {
		Map visitRecordList=checkUserLogindao.queryVisitorByNum(map);
		Map m=new HashMap();
			List<Map> followVisitorList=checkUserLogindao.queryBookingFollowVisitor(m);
			String fVisitNum="";
			   String fVisitorName="";
			   String fVisitorSex="";
			   String fPaperType="";
			   String fPaperNum="";
			   String fMobile="";
			   String fAddress="";
			   String fIdentifyPhoto="";
			   String fCatchPhoto="";
			for(Map fv:followVisitorList){
				   fVisitNum+=fv.get("fVisitNum")+"|";
				   fVisitorName+=fv.get("fVisitorName")+"|";
				   fVisitorSex+=fv.get("fVisitorSex")+"|";
				   fPaperType+=fv.get("fPaperType")+"|";
				   fPaperNum+=fv.get("fPaperNum")+"|";
				   fMobile+=fv.get("fMobile")+"|";
				   fAddress+=fv.get("fAddress")+"|";
				   fIdentifyPhoto+=fv.get("fIdentifyPhoto")+"|";
				   fCatchPhoto+=fv.get("fCatchPhoto")+"|";
			}
			if(m!=null){
				m.put("fVisitNum", "".equals(fVisitNum) ? "":fVisitNum.substring(0, fVisitNum.length()-1));
				m.put("fVisitorName", "".equals(fVisitorName) ? "":fVisitorName.substring(0, fVisitorName.length()-1));
				m.put("fVisitorSex", "".equals(fVisitorSex) ? "":fVisitorSex.substring(0, fVisitorSex.length()-1));
				m.put("fPaperNum", "".equals(fPaperNum) ? "":fPaperNum.substring(0, fPaperNum.length()-1));
				m.put("fPaperType", "".equals(fPaperType) ? "":fPaperType.substring(0, fPaperType.length()-1));
				m.put("fMobile", "".equals(fMobile) ? "":fMobile.substring(0, fMobile.length()-1));
				m.put("fAddress", "".equals(fAddress) ? "":fAddress.substring(0, fAddress.length()-1));
				m.put("fIdentifyPhoto", "".equals(fIdentifyPhoto) ? "":fIdentifyPhoto.substring(0, fIdentifyPhoto.length()-1));
				m.put("fCatchPhoto", "".equals(fCatchPhoto) ? "":fCatchPhoto.substring(0, fCatchPhoto.length()-1));
			}
		
		return visitRecordList;
	}
	@Override
	public Map queryBookingVisitor(Map map) {
		if(!"".equals(map.get("mPaperNum"))&&map.get("mPaperNum")!=null&&!"".equals(map.get("mMobile"))&&map.get("mMobile")!=null){
			map.put("mMobile", "");
		}
		Map mainVisitor=checkUserLogindao.queryBookingVisitor(map);
		List<Map> followVisitorList=checkUserLogindao.queryBookingFollowVisitor(mainVisitor);
		 String fVisitNum="";
		   String fVisitorName="";
		   String fVisitorSex="";
		   String fPaperType="";
		   String fPaperNum="";
		   String fMobile="";
		   String fAddress="";
		   String fIdentifyPhoto="";
		   String fCatchPhoto="";
		for(Map m:followVisitorList){
			   fVisitNum+=m.get("fVisitNum")+"|";
			   fVisitorName+=m.get("fVisitorName")+"|";
			   fVisitorSex+=m.get("fVisitorSex")+"|";
			   fPaperType+=m.get("fPaperType")+"|";
			   fPaperNum+=m.get("fPaperNum")+"|";
			   fMobile+=m.get("fMobile")+"|";
			   fAddress+=m.get("fAddress")+"|";
			   fIdentifyPhoto+=m.get("fIdentifyPhoto")+"|";
			   fCatchPhoto+=m.get("fCatchPhoto")+"|";
		}
		if(mainVisitor!=null){
			mainVisitor.put("fVisitNum", "".equals(fVisitNum) ? "":fVisitNum.substring(0, fVisitNum.length()-1));
			mainVisitor.put("fVisitorName", "".equals(fVisitorName) ? "":fVisitorName.substring(0, fVisitorName.length()-1));
			mainVisitor.put("fVisitorSex", "".equals(fVisitorSex) ? "":fVisitorSex.substring(0, fVisitorSex.length()-1));
			mainVisitor.put("fPaperNum", "".equals(fPaperNum) ? "":fPaperNum.substring(0, fPaperNum.length()-1));
			mainVisitor.put("fPaperType", "".equals(fPaperType) ? "":fPaperType.substring(0, fPaperType.length()-1));
			mainVisitor.put("fMobile", "".equals(fMobile) ? "":fMobile.substring(0, fMobile.length()-1));
			mainVisitor.put("fAddress", "".equals(fAddress) ? "":fAddress.substring(0, fAddress.length()-1));
			mainVisitor.put("fIdentifyPhoto", "".equals(fIdentifyPhoto) ? "":fIdentifyPhoto.substring(0, fIdentifyPhoto.length()-1));
			mainVisitor.put("fCatchPhoto", "".equals(fCatchPhoto) ? "":fCatchPhoto.substring(0, fCatchPhoto.length()-1));
		}
		return  mainVisitor;
	}

}
