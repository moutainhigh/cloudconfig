package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/27.
 */
public interface ContractMapper {
    public  int insertContract(Map<String,Object> map);

    public  int updateContract(Map<String,Object> map);

    public List<Map<String,Object>> searchContract(@Param("companyId")String companyId,@Param("departmentIdList")List<String> departmentIdList, @Param("searchValue")String searchValue,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("loginUserId")String  loginUserId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    public int searchContractCount(@Param("companyId")String companyId,@Param("departmentIdList")List<String> departmentIdList, @Param("searchValue")String searchValue,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("loginUserId")String  loginUserId);



    public Map<String,Object> contactDetail(@Param("id")String id);

    public int deleteContractByIds(@Param("idList")List<String > idList);

    public List<Map<String,Object>> selectContractAttachement(@Param("contractId")String contractId);

    public Map<String,Object> selectByContractNo(@Param("contractNo")String contractNo,@Param("pcCompanyId")String pcCompanyId);
}
