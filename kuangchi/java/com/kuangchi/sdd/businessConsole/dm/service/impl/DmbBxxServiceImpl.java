package com.kuangchi.sdd.businessConsole.dm.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.businessConsole.dm.dao.IDmbBxxDao;
import com.kuangchi.sdd.businessConsole.dm.model.DmbBxx;
import com.kuangchi.sdd.businessConsole.dm.model.DmbLxx;
import com.kuangchi.sdd.businessConsole.dm.service.IDmbBxxService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.ChinesToPinyin;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;


@Service("dmbBxxService")
public class DmbBxxServiceImpl extends BaseServiceSupport implements IDmbBxxService {
	
	@Resource(name="dmbBxxDao")
	private IDmbBxxDao dmbBxxDao;
	
	private static final String  TABLE_NAME_PREFIX = "t_xt_zdydm_";

	@Override
	public Grid<DmbBxx> selectDmbBxx(DmbBxx dmbBxx) {
		
		Grid<DmbBxx> grid = new Grid<DmbBxx>();
		
		grid.setRows(dmbBxxDao.selectDmbBxx(dmbBxx));
		grid.setTotal(dmbBxxDao.countDmbBxx(dmbBxx));

		return grid;
	}

	@Override
	public boolean isExistDmbBmc(String dmbBmc) {
	
		String dmbBmcFull = TABLE_NAME_PREFIX + ChinesToPinyin.getPinYin(dmbBmc.trim(), true);
		
		int total = dmbBxxDao.isExistDmbBmc(dmbBmcFull);
		
		return total > 0 ? true:false;
	}

	@Override	
	@Transactional(rollbackFor=Exception.class)
	public void addDmB(User lrry, DmbBxx dmbBxx, List<DmbLxx> dmlList){
		
		Timestamp now = DateUtil.getSysTimestamp();
		
		dmbBxx.setUUID(UUIDUtil.uuidStr());
		dmbBxx.setZfBj(GlobalConstant.ZF_BJ_N);
		dmbBxx.setLrSj(now);	
		dmbBxx.setDmbBdmz(TABLE_NAME_PREFIX+ChinesToPinyin.getPinYin(dmbBxx.getDmbBmc(), true));
		dmbBxx.setLrryDm(lrry.getYhDm());
		
		
		
		
		
		for (int i = 0; i < dmlList.size(); i++) {
			DmbLxx tmp = dmlList.get(i);
			tmp.setUUID(UUIDUtil.uuidStr());
			tmp.setZfBj(GlobalConstant.ZF_BJ_N);
			tmp.setLrSj(now);
			tmp.setYwbBdmz(dmbBxx.getDmbBdmz());
			tmp.setDmbLDmz(ChinesToPinyin.getPinYin(tmp.getDmbLMc(), false));
			
			tmp.setLrryDm(lrry.getYhDm());
		}
		
		
		//判断是一级还是俩级
		
			if (dmbBxx.getDcDcBj().equals("1")) {
				DmbLxx parentLxx = new DmbLxx();
				parentLxx.setUUID(UUIDUtil.uuidStr());
				parentLxx.setZfBj(GlobalConstant.ZF_BJ_N);
				parentLxx.setLrSj(now);
				parentLxx.setYwbBdmz(dmbBxx.getDmbBdmz());
				parentLxx.setDmbLMc(dmbBxx.getDmbBmc() + "上级代码");
				
				parentLxx.setCd(36);
				parentLxx.setDmbLDmz(ChinesToPinyin.getPinYin(dmbBxx.getDmbBmc(), true)+"_sj");
				parentLxx.setLrryDm(lrry.getYhDm());
				
				dmlList.add(parentLxx);
			}
			
			
			DmbLxx primaryLxx = new DmbLxx();
			primaryLxx.setUUID(UUIDUtil.uuidStr());
			primaryLxx.setZfBj(GlobalConstant.ZF_BJ_N);
			primaryLxx.setLrSj(now);
			primaryLxx.setYwbBdmz(dmbBxx.getDmbBdmz());
			primaryLxx.setDmbLMc(dmbBxx.getDmbBmc() + "主键");
			
			primaryLxx.setCd(40);
			primaryLxx.setDmbLDmz(ChinesToPinyin.getPinYin(dmbBxx.getDmbBmc(), true)+"_uuid");
			primaryLxx.setLrryDm(lrry.getYhDm());
			
			dmlList.add(primaryLxx);
			
			
			
			
			dmbBxxDao.addDmbBxx(dmbBxx);
			
			
			
			dmbBxxDao.addDmblxx(dmlList);
			
			StringBuilder sql = new StringBuilder();
			
			sql.append("create table ");
			sql.append(dmbBxx.getDmbBdmz());
			sql.append(" ( ");
			
			
			
			for (int i = 0; i < dmlList.size()-1; i++) {
				DmbLxx lxx = dmlList.get(i);
				sql.append("`");
				sql.append(lxx.getDmbLDmz());
				sql.append("` ");
				sql.append(" varchar");
				sql.append("(");
				sql.append(lxx.getCd());
				sql.append(") ");
				
				
				sql.append(", ");
				
				
			}
			
			sql.append(" uuid varchar(40) ");
			
			sql.append(" ) ");
			
		
			 
			dmbBxxDao.createTableDmb(sql.toString());
			
			
	}
	
	
	

}
