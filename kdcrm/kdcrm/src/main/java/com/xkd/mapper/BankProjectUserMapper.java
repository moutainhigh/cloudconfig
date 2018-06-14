package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/15.
 */
public interface BankProjectUserMapper {

    public int insertBankProjectUserList(@Param("list") List<Map<String,Object>> list);


   public  int deleteByBankIdAndProjectId(@Param("bankProjectId") String bankProjectId,@Param("pointId") String pointId,@Param("userId") String  userId);


    public List<Map<String,Object>> selectUserNotUnderProject(@Param("bankProjectId")String bankProjectId,@Param("pointId")String pointId,@Param("uname") String uname,@Param("sex")String sex,@Param("start") Integer start,@Param("pageSize") Integer pageSize);


    public int selectUserNotUnderProjectCount(@Param("bankProjectId")String bankProjectId,@Param("pointId")String pointId,@Param("uname") String uname,@Param("sex")String sex);
    public List<Map<String ,Object>>  selectUserUnderProject(@Param("bankProjectId")String bankProjectId,@Param("pointId")String pointId,@Param("uname") String uname,@Param("sex")String sex,@Param("start") Integer start,@Param("pageSize") Integer pageSize);
    public int  selectUserUnderProjectCount(@Param("bankProjectId")String bankProjectId,@Param("pointId")String pointId,@Param("uname") String uname,@Param("sex")String sex);

    public int deleteBankProjectUser(@Param("ids") List<String> idList);
}
