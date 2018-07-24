package com.wjh.roleoperateservice.service;

import com.netflix.discovery.converters.Auto;
import com.wjh.common.model.ResponseModel;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import com.wjh.roleoperateservice.mapper.RoleOperateMapper;
import com.wjh.roleoperateservicemodel.model.RoleOperateDto;
import com.wjh.roleoperateservicemodel.model.RoleOperatePo;
import com.wjh.roleoperateservicemodel.model.RoleOperateVo;
import com.wjh.roleservicemodel.model.RoleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RoleOperateService {

    @Autowired
    RoleOperateMapper roleOperateMapper;
    @Autowired
    IdService idService;
    @Autowired
    RoleService roleService;

    @Autowired
    MenuOperateService menuOperateService;


    public int update(RoleOperateDto roleOperateDto, Long loginUserId) {
        Long roleId = roleOperateDto.getRoleId();
        List<Long> operateIdList = roleOperateDto.getOperateIdList();
        roleOperateMapper.deleteByRoleId(roleId);
        List<RoleOperatePo> roleOperatePoList = new ArrayList<RoleOperatePo>();
        for (int i = 0; i < operateIdList.size(); i++) {
            Long id = idService.generateId();
            Date date = new Date();
            RoleOperatePo roleOperatePo = new RoleOperatePo();
            roleOperatePo.setId(id);
            roleOperatePo.setRoleId(roleId);
            roleOperatePo.setOperateId(operateIdList.get(i));
            roleOperatePo.setCreateDate(date);
            roleOperatePo.setUpdateDate(date);
            roleOperatePo.setCreatedBy(loginUserId);
            roleOperatePo.setUpdatedBy(loginUserId);
            roleOperatePoList.add(roleOperatePo);
        }
        int count = roleOperateMapper.insertList(roleOperatePoList);
        return count;
    }

    public int deleteByRoleId(Long roleId) {
        return roleOperateMapper.deleteByRoleId(roleId);
    }

    public int deleteByOperateId(Long operateId) {
        return roleOperateMapper.deleteByOperateId(operateId);
    }

    public List<RoleOperateVo> listByRoleId(Long roleId) {
        List<RoleOperateVo> roleOperateVoList = roleOperateMapper.listByRoleId(roleId);
        List<Long> roleIdList = new ArrayList<Long>();
        List<Long> operateIdList = new ArrayList<Long>();
        for (int i = 0; i < roleOperateVoList.size(); i++) {
            roleIdList.add(roleOperateVoList.get(i).getRoleId());
            operateIdList.add(roleOperateVoList.get(i).getOperateId());
        }
        ResponseModel<List<RoleVo>> roleVoResponseModel = roleService.selectByIds(roleIdList);
        ResponseModel<List<OperateVo>> operateVoResponseModel = menuOperateService.selectByIds(operateIdList);

        List<RoleVo> roleVoList = (List<RoleVo>) roleVoResponseModel.getResModel();
        List<OperateVo> operateVoList = (List<OperateVo>) operateVoResponseModel.getResModel();
        if (roleOperateVoList.size() > 0) {
            for (int i = 0; i < roleOperateVoList.size(); i++) {
                RoleOperateVo roleOperateVo = roleOperateVoList.get(i);
                for (int j = 0; j < roleVoList.size(); j++) {
                    if (roleOperateVo.getRoleId().equals(roleVoList.get(j).getId())) {
                        roleOperateVo.setRoleName(roleVoList.get(j).getRoleName());
                        break;
                    }
                }
                for (int j = 0; j < operateVoList.size(); j++) {
                    if (roleOperateVo.getOperateId().equals(operateVoList.get(j).getId())) {
                        roleOperateVo.setOperateName(operateVoList.get(j).getOperateName());
                        break;
                    }
                }
            }
        }


        return roleOperateVoList;
    }

}
