package com.xkd.service;

import com.xkd.mapper.ContactsUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContactsUserService {

    @Autowired
    ContactsUserMapper contactsUserMapper;

    public List<Map<String,String>> getContactsUserList(String queryType,String queryName,String loginUserId,Boolean showMobile,List<String> depList,int pageNo,int pageSize){
        return contactsUserMapper.getContactsUserList(queryType,queryName,loginUserId,showMobile,depList,pageNo,pageSize);
    }

    public List<Map<String,String>> getContactsUserByCompanyIdAndUserId(String companyId, String mobile) {
        return contactsUserMapper.getContactsUserByCompanyIdAndUserId(companyId,mobile);
    }

    public int getContactsUserListTotal(String queryType,String queryName,String loginUserId,Boolean showMobile, List<String> depList) {
        return contactsUserMapper.getContactsUserListTotal(queryType,queryName,loginUserId,showMobile,depList);
    }

    public List<Map<String,String>> getContactsStatistics(String loginUserId, String startDate, String endDate,List<String> depList) {
        return contactsUserMapper.getContactsStatistics(loginUserId,startDate,endDate,depList);
    }
}
