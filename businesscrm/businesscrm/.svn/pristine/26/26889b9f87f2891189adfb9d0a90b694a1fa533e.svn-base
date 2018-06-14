package com.xkd.mapper;

import com.xkd.model.DC_User;
import org.apache.ibatis.annotations.Param;

public interface DC_UserMapper {

    DC_User getUserByObj(@Param("id") String id, @Param("ttype") String ttype);

    int saveUser(DC_User obj);

    int saveUserDetail(DC_User obj);

    int editUser(DC_User obj);

    DC_User getUserById(Object userid);


}
