package com.xkd.service;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xkd.utils.DateUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.BankProgramMapper;

@Service
public class BankProgramService {

	@Autowired
	private BankProgramMapper bankProgramMapper;
	
	public List<Map<String, Object>> selectBankProgramsByContent(Map<String, Object> paramMap) {
		
		return bankProgramMapper.selectBankProgramsByContent(paramMap);
		
	}

	public int insertSelective(Map<String, Object> map) {
		
		return bankProgramMapper.insertSelective(map);
		
	}

	public int updateByIdSelective(Map<String, Object> map) {
		
		return bankProgramMapper.updateByIdSelective(map);
		
	}

	public int deleteBankProgramByIds(String ids) {
		
		return bankProgramMapper.deleteBankProgramByIds(ids);
		
	}

	public List<Map<String, Object>> selectBankProgramByCodeAllStatus(String programCode) {
		
		return bankProgramMapper.selectBankProgramByCodeAllStatus(programCode);
		
	}

	public List<Map<String, Object>> selectBankProgramByNameAllStatus(String programName) {
		
		return bankProgramMapper.selectBankProgramByNameAllStatus(programName);
		
	}

	public Map<String, Object> selectBankProgramById(String id) {
		
		return bankProgramMapper.selectBankProgramById(id);
		
	}

	public int selectTotalBankProgramsByContent(Map<String, Object> paramMap) {
		
		return bankProgramMapper.selectTotalBankProgramsByContent(paramMap);
	}

	public Integer deleteBankProgramRealByName(String programName) {
		
		return bankProgramMapper.deleteBankProgramRealByName(programName);
	}


    public String getCountBankProgram() {
		return bankProgramMapper.getCountBankProgram();
    }

	public String getProgramCode(String programIndustry,int totalNum) {

		String dateTemt = DateUtils.dateToString(new Date(),"yyyy").substring(2,4);
		String dateNowTemt = DateUtils.dateToString(new Date(),"yyyyMMdd");
		String industryTemt = "";
		if("银行".equals(programIndustry)){
			industryTemt = "YH";
		}else if("证券".equals(programIndustry)){
			industryTemt = "ZJ";
		}else if("保险".equals(programIndustry)){
			industryTemt = "BX";
		}else if("企业".equals(programIndustry)){
			industryTemt = "QY";
		}else if("其他".equals(programIndustry)){
			industryTemt = "QT";
		}
		Format f = new DecimalFormat("0000");
		return  "FA-"+industryTemt+""+dateTemt+""+f.format(totalNum+1).toString()+"-"+dateNowTemt;
	}

    public Integer clearProjectProgramByIds(List<String> programList) {
		return bankProgramMapper.clearProjectProgramByIds(programList);
    }
}
