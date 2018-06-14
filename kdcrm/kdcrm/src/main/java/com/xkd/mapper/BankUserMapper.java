package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/11.
 */
public interface BankUserMapper {
    public List<Map<String,Object>> selectUserInfoByPointId (@Param("pointId") String pointId);

    public Map<String,Object> selectUserPointById(@Param("id") String id);

    public int updateUserStationById(@Param("id")String id ,@Param("station") String station);

    public int deleteByUserIdAndPointId(@Param("userId") String userId,@Param("pointId") String pointId);

    public List<Map<String,Object>>  selectUserPointByMobileAndPointId(@Param("mobile")String mobile,@Param("pointId") String pointId);

    int insertBankUserInfo(Map<String,Object> map);


    int deleteBankUserInfo(@Param("ids") List<String> ids );


    List<Map<String,Object>>  selectBankUserList(@Param("pointId")String pointId,@Param("uname")String uname,@Param("sex") String sex,@Param("start") Integer start,@Param("pageSize") Integer pageSize);

    int  selectBankUserListCount(@Param("pointId")String pointId,@Param("uname")String uname,@Param("sex") String sex);

}
