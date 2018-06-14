package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.BankProgram;

public interface BankProgramMapper {

    int insert(BankProgram record);

    int insertSelective(Map<String, Object> map);

    BankProgram selectByPrimaryKey(String id);

    int updateByIdSelective(Map<String, Object> map);

    int updateById(Map<String, Object> map);
    
    List<Map<String, Object>> selectBankProgramsByContent(Map<String, Object> paramMap);

	int deleteBankProgramByIds(@Param("ids") String ids);

	List<Map<String, Object>> selectBankProgramByCodeAllStatus(@Param("programCode") String programCode);

	List<Map<String, Object>> selectBankProgramByNameAllStatus(@Param("programName") String programName);

	Map<String, Object> selectBankProgramById(@Param("id") String id);

	int selectTotalBankProgramsByContent(Map<String, Object> paramMap);

	Integer deleteBankProgramRealByName(@Param("programName") String programName);

    String getCountBankProgram();

    Integer clearProjectProgramByIds(@Param("projectProgramIds")List<String> projectProgramIds);
}