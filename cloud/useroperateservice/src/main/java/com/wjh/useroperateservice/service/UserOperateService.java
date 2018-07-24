package com.wjh.useroperateservice.service;

import com.netflix.discovery.converters.Auto;
import com.wjh.common.model.ResponseModel;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import com.wjh.useroperateservice.mapper.UserOperateMapper;
import com.wjh.useroperateservicemodel.model.UserOperateDto;
import com.wjh.useroperateservicemodel.model.UserOperatePo;
import com.wjh.useroperateservicemodel.model.UserOperateVo;
import com.wjh.userservicemodel.model.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserOperateService {

    @Autowired
    UserOperateMapper userOperateMapper;

    @Autowired
    IdService idService;


    @Autowired
    MenuOperateService menuOperateService;

    @Autowired
    UserService userService;

    public int update(UserOperateDto userOperateDto, Long loginUserId) {
        Long userId=userOperateDto.getUserId();
        List<Long> operateIdList=userOperateDto.getOperateIdList();

        //删除旧的映射
        userOperateMapper.deleteByUserId(userId);
        //添加新的映射
        List<UserOperatePo> userOperatePoList=new ArrayList<UserOperatePo>();
        for (int i = 0; i < operateIdList.size(); i++) {
            UserOperatePo userOperatePo = new UserOperatePo();
            userOperatePo.setUserId(userId);
            userOperatePo.setOperateId(operateIdList.get(i));
            Long id = idService.generateId();
            userOperatePo.setId(id);
            Date date = new Date();
            userOperatePo.setCreateDate(date);
            userOperatePo.setUpdateDate(date);
            userOperatePo.setUpdatedBy(loginUserId);
            userOperatePo.setCreatedBy(loginUserId);
            userOperatePoList.add(userOperatePo);
        }
        int count = userOperateMapper.insertList(userOperatePoList);
        return count;
    }

    public int deleteByUserId(Long userId, Long loginUserId) {
        return userOperateMapper.deleteByUserId(userId);
    }

    public int deleteByOperateId(Long operateId, Long loginUserId) {
        return userOperateMapper.deleteByOperateId(operateId);
    }


    public List<UserOperateVo> listByUserId(Long userId) {


        List<UserOperateVo> userOperateVoList = userOperateMapper.listByUserId(userId);
        List<Long> userIdList = new ArrayList<Long>();
        List<Long> operateIdList = new ArrayList<Long>();
        for (int i = 0; i < userOperateVoList.size(); i++) {
            UserOperateVo userOperateVo = userOperateVoList.get(i);
            userIdList.add(userOperateVo.getUserId());
            operateIdList.add(userOperateVo.getOperateId());
        }

        if (userOperateVoList.size() > 0) {
            ResponseModel<List<OperateVo>> operateVoResponseModel = menuOperateService.selectByIds(operateIdList);
            List<OperateVo> operateVoList = (List<OperateVo>) operateVoResponseModel.getResModel();
            ResponseModel<List<UserVo>> userVoResponseModel = userService.selectByIds(userIdList);
            List<UserVo> userVoList = (List<UserVo>) userVoResponseModel.getResModel();
            for (int i = 0; i < userOperateVoList.size(); i++) {
                UserOperateVo userOperateVo = userOperateVoList.get(i);
                for (int j = 0; j < operateVoList.size(); j++) {
                    if (userOperateVo.getOperateId().equals(operateVoList.get(j).getId())) {
                        userOperateVo.setOperateName(operateVoList.get(j).getOperateName());
                        break;
                    }
                }

                for (int j = 0; j < userVoList.size(); j++) {
                    if (userOperateVo.getUserId().equals(userVoList.get(j).getId())) {
                        userOperateVo.setUserName(userVoList.get(j).getName());
                        break;
                    }
                }

            }

        }


        return userOperateVoList;
    }

}
