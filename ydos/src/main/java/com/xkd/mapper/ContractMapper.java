package com.xkd.mapper;

import com.xkd.service.ObjectNewsService;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/12.
 */
public interface ContractMapper {
 int  insertContact(Map<String,Object> map);


 int updateContact(Map<String,Object> map);

 int deleteContractByIdList(@Param("idList")List<String> idList);

 List<Map<String,Object>> selectContactByCompanyId(@Param("companyId")String companyId);

 List<Map<String,Object>> selectContractByContractNo(@Param("contractNo")String contractNo);

 Map<String,Object> selectContractByContractNoAndPcCompanyId(@Param("contractNo")String contractNo,@Param("pcCompanyId")String pcCompanyId);


 List<Map<String,Object>> searchContract(@Param("pcCompanyId")String pcCompanyId,@Param("companyIdList") List<String> companyIdList,@Param("contractName") String contractName,@Param("contractNo")String contractNo,@Param("type")Integer type,@Param("endDate")String endDate,@Param("start") Integer start,@Param("pageSize")Integer pageSize);
 Integer searchContractCount(@Param("pcCompanyId")String pcCompanyId,@Param("companyIdList") List<String> companyIdList,@Param("contractName") String contractName,@Param("contractNo")String contractNo,@Param("type")Integer type,@Param("endDate")String endDate );

 Map<String,Object> selectContractById(@Param("id")String  id);



 List<Map<String,Object>> selectContractByGroupId(@Param("groupId")String groupId,@Param("pcCompanyId")String pcCompanyId);


 Integer selectContractCountByGroupId(@Param("groupId")String groupId,@Param("pcCompanyId")String pcCompanyId);

 Integer   selectContractStatistic(@Param("fromTime")String fromTime,@Param("toTime")String toTime,@Param("pcCompanyId")String pcCompanyId);

 List<Map<String,Object> > selectDueContract(@Param("fromTime")String fromTime,@Param("toTime")String toTime,@Param("pcCompanyId")String pcCompanyId);

 Integer countContractByPcCompanyId(@Param("pcCompanyId") String pcCompanyId, @Param("start") Integer start, @Param("end") Integer end);

 List<Map<String, Object>> listExpiringContractByPcCompanyId(@Param("pcCompanyId") String pcCompanyId,
                                                             @Param("start") Integer start,
                                                             @Param("pageSize") Integer pageSize)
         ;


}
