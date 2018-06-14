package com.xkd.service;

import com.xkd.mapper.CustomerMapper;
import com.xkd.mapper.UserMapper;
import com.xkd.model.Operate;
import com.xkd.model.RedisTableKey;
import com.xkd.model.ResponseConstants;
import com.xkd.model.Role;
import com.xkd.utils.DateUtils;
import com.xkd.utils.RedisCacheUtil;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class UserService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private DepartmentService departmentService;


    @Autowired
    SysRoleOperateService roleOperateService;
    @Autowired
    SysUserOperateService sysUserOperateService;

    @Autowired
    CustomerService customerService;

    @Autowired
    UserDataPermissionService userDataPermissionService;

    @Autowired
    SysRoleOperateService sysRoleOperateService;


    @Autowired
    OperateCacheService operateCacheService;
    @Autowired
    SysOperateService operateService;


    @Autowired
    RedisCacheUtil redisCacheUtil;
    @Autowired
    RoleService roleService;

    public Map<String, Object> selectUserByMobile(String mobile, Integer platform) {

        Map<String, Object> map = userMapper.selectUserByMobile(mobile, platform);

        return map;
    }


    /*
     * platform：来源，0表示用户来自手机端、1表示来自pc端
     */
    public Map<String, Object> selectUserByEmail(String email, Integer platform) {

        Map<String, Object> maps = userMapper.selectUserByEmail(email, platform);

        return maps;
    }


    public Map<String, Object> selectUserById(String id) {
        /*Map<String, Object> userInCache = (Map<String, Object>) redisCacheUtil.getCacheMapVaue(RedisTableKey.USER, id);
        if (userInCache != null) {
            return userInCache;
        }*/
        Map<String, Object> map = userMapper.selectUserById(id);
        if (map==null){
            return null;
        }
        Map<String, Object> uM = new HashMap<>();
        uM.put((String) map.get("id"), map);
        redisCacheUtil.setCacheMap(RedisTableKey.USER, uM);
        return map;
    }


    public List<Map<String, Object>> selectUserByTeacherType(String teacherType, String loginUserId) {
        Map<String, Object> loginUserMap = userMapper.selectUserById(loginUserId);
        List<Map<String, Object>> maps = null;
        if ("1".equals(loginUserMap.get("roleId"))) {
            maps = userMapper.selectUserByTeacherType(teacherType, null);
        } else {
            maps = userMapper.selectUserByTeacherType(teacherType, (String) loginUserMap.get("pcCompanyId"));
        }

        return maps;
    }


    public List<Map<String, Object>> selectUsersByContent(String contentStr, int currentPageInt, int pageSizeInt) {

        List<Map<String, Object>> maps = userMapper.selectUsersByContent(contentStr, currentPageInt, pageSizeInt);

        return maps;
    }


    public Integer selectUsersByContentCount(String content) {

        Integer num = userMapper.selectUsersByContentCount(content);

        return num;
    }


    public Integer updateUserPasswordById(String id, String enPassWord) {

        Integer num = userMapper.updateUserPasswordById(id, enPassWord);
        //删除缓存中的数据
        redisCacheUtil.delete(RedisTableKey.USER, id);
        return num;
    }


    public Integer deleteUserRolesByRoles(List<String> roleIdList) {

        Integer num = userMapper.deleteUserRolesByRoles(roleIdList);

        return num;
    }


    public Integer updateDcUserDetail(Map<String, Object> user) {
        int count = userMapper.updateDcUserDetail(user);
        redisCacheUtil.delete(RedisTableKey.USER, (String) user.get("id"));
        return count;
    }

    public Integer insertDcUserDetail(Map<String, Object> user) {
        return userMapper.insertDcUserDetail(user);
    }

    public Integer updateDcUser(Map<String, Object> user) {
        int count = userMapper.updateDcUser(user);
        redisCacheUtil.delete(RedisTableKey.USER, (String) user.get("id"));
        return count;
    }

    public Integer insertDcUser(Map<String, Object> user) {
        return userMapper.insertDcUser(user);
    }


    public Integer repeatPcUserPasswordsByIds(List<String> idList, String encodeRepeatPassWord, String updateBy) {

        Integer num = userMapper.repeatPcUserPasswordsByIds(idList, encodeRepeatPassWord, updateBy);
        //删除缓存的用户数据
        for (int i = 0; i < idList.size(); i++) {
            redisCacheUtil.delete(RedisTableKey.USER, idList.get(i));
        }

        return num;
    }


    public Integer deleteUserByIds(List<String> idList) {

        Integer num = userMapper.deleteUserByIds(idList);
        //删除缓存中的数据
        for (int i = 0; i < idList.size(); i++) {
            redisCacheUtil.delete(RedisTableKey.USER, idList.get(i));
        }
        return num;
    }


    public List<Map<String, Object>> selectDirectUserByDepartmentId(String departmentId) {
        List<String> list = new ArrayList<>();
        list.add(departmentId);
        return userMapper.selecUserByDepartmentIds(list, null, 0, 100000);
    }


    public Map<String, Object> selectUserByOnlyMobile(String mobile) {
        return userMapper.selectUserByOnlyMobile(mobile);
    }


    public List<Map<String, Object>> selecUserByDepartmentId(String departmentId, String userName, @RequestParam Integer pageSize, @RequestParam Integer currentPage, String loginUserId) {

        Map<String, Object> loginUserMap = userMapper.selectUserById(loginUserId);


        if (currentPage < 1) {
            currentPage = 1;
        }
        Integer start = (currentPage - 1) * pageSize;


        if ("1".equals(loginUserMap.get("roleId"))) {//如果是超级管理员，则可以看到所有用户
            if (StringUtils.isBlank(departmentId)) {  //如果departmentId为空，则默认为根结点
                return userMapper.selecUserByDepartmentIds(departmentService.selectChildDepartmentIds("1", loginUserMap), userName, start, pageSize);
            } else {
                return userMapper.selecUserByDepartmentIds(departmentService.selectChildDepartmentIds(departmentId, loginUserMap), userName, start, pageSize);
            }
        } else { //非超级管理员
            if (StringUtils.isBlank(departmentId)) {//非超级管理员则按其所属公司显示
                return userMapper.selecUserByDepartmentIds(departmentService.selectChildDepartmentIds((String) loginUserMap.get("pcCompanyId"), loginUserMap), userName, start, pageSize);
            } else {
                return userMapper.selecUserByDepartmentIds(departmentService.selectChildDepartmentIds(departmentId, loginUserMap), userName, start, pageSize);
            }
        }
    }

    public Integer selecTotalUserByDepartmentId(String departmentId, String userName, String loginUserId) {
        Map<String, Object> loginUserMap = userMapper.selectUserById(loginUserId);


        if ("1".equals(loginUserMap.get("roleId"))) {//如果是超级管理员，则可以看到所有用户
            if (StringUtils.isBlank(departmentId)) {  //如果departmentId为空，则默认为根结点
                return userMapper.selecTotalUserByDepartmentIds(departmentService.selectChildDepartmentIds("1", loginUserMap), userName);
            } else {
                return userMapper.selecTotalUserByDepartmentIds(departmentService.selectChildDepartmentIds(departmentId, loginUserMap), userName);
            }
        } else { //非超级管理员
            if (StringUtils.isBlank(departmentId)) {//非超级管理员则按其所属公司显示
                return userMapper.selecTotalUserByDepartmentIds(departmentService.selectChildDepartmentIds((String) loginUserMap.get("pcCompanyId"), loginUserMap), userName);
            } else {
                return userMapper.selecTotalUserByDepartmentIds(departmentService.selectChildDepartmentIds(departmentId, loginUserMap), userName);
            }
        }
    }


    public List<Map<String, Object>> selectUsersUnderRole(String roleId, String userName, Integer pageNum, Integer pageSize) {

        if (pageNum < 1) {
            pageNum = 1;
        }
        return userMapper.selectUsersUnderRole(roleId, userName, (pageNum - 1) * pageSize, pageSize);

    }

    public Integer selectTotalUsersCountUnderRole(String roleId, String userName) {


        return userMapper.selectTotalUsersCountUnderRole(roleId, userName);
    }


    public List<Map<String, Object>> selectUsersNotUnderRole(String roleId, String userName, Integer pageNum, Integer pageSize, String departmentId, String loginUserId) {
        Map<String, Object> loginUserMap = userMapper.selectUserById(loginUserId);


        if (pageNum < 1) {
            pageNum = 1;
        }
        Integer excludeSuperRoleUser = 0;  //是否排除超级管理员用户 1 是

        List<String> departmentIds = null;
        if ("1".equals(loginUserMap.get("roleId"))) {
            //在此处，超级管理员只能修改本公司的角色成员
            departmentIds = departmentService.selectChildDepartmentIds((String) loginUserMap.get("pcCompanyId"), loginUserMap);
            excludeSuperRoleUser = 0;
        } else {
            if (StringUtils.isBlank(departmentId)) {
                departmentIds = departmentService.selectChildDepartmentIds((String) loginUserMap.get("pcCompanyId"), loginUserMap);
            } else {
                departmentIds = departmentService.selectChildDepartmentIds(departmentId, loginUserMap);
            }
            excludeSuperRoleUser = 1;
        }
        return userMapper.selectUsersNotUnderRole(roleId, userName, (pageNum - 1) * pageSize, pageSize, departmentIds, excludeSuperRoleUser);
    }

    public int selectTotalUsersCountNotUnderRole(String roleId, String userName, String departmentId, String loginUserId) {
        Map<String, Object> loginUserMap = userMapper.selectUserById(loginUserId);

        List<String> departmentIds = null;
        Integer excludeSuperRoleUser = 0; //是否排除超级管理员用户   1  是

        if ("1".equals(loginUserMap.get("roleId"))) {
            if (StringUtils.isBlank(departmentId)) {
                departmentIds = departmentService.selectChildDepartmentIds("1", loginUserMap);
            } else {
                departmentIds = departmentService.selectChildDepartmentIds(departmentId, loginUserMap);
            }
            excludeSuperRoleUser = 0;

        } else {
            if (StringUtils.isBlank(departmentId)) {
                departmentIds = departmentService.selectChildDepartmentIds((String) loginUserMap.get("pcCompanyId"), loginUserMap);
            } else {
                departmentIds = departmentService.selectChildDepartmentIds(departmentId, loginUserMap);
            }
            excludeSuperRoleUser = 1;

        }
        return userMapper.selectTotalUsersCountNotUnderRole(roleId, userName, departmentIds, excludeSuperRoleUser);
    }


    public List<Map<String, Object>> searchPcUserbyName(String userName, List<String> departmentIdList) {
        return userMapper.searchPcUserbyName(userName, departmentIdList);
    }


    public int changeDepartmentByUserIds(List<String> userIds, String departmentId, String pcCompanyId) {

        int count = userMapper.changeDepartmentByUserIds(userIds, departmentId, pcCompanyId);
        //删除缓存中的数据
        for (int i = 0; i < userIds.size(); i++) {
            redisCacheUtil.delete(RedisTableKey.USER, userIds.get(i));
        }
        return count;
    }


    public List<Operate> selectAllOperateByUSerId(String userId) {
        Map<String, Object> loginUser = selectUserById(userId);

        //读取用户对应的权限信息
        List<Operate> userOperateList = sysUserOperateService.selectOperateByUserId(userId);

        if (loginUser != null && loginUser.get("roleId") != null) {
            /**
             * 查询出用户角色下的权限列表
             */
            List<Operate> roleOperateList = sysRoleOperateService.selectOperateByRoleId(loginUser.get("roleId").toString());
            //合并角色对应的权限列表
            userOperateList.addAll(roleOperateList);
        }
        return userOperateList;
    }


    public boolean hasPrivatePermission(String token, String url) {
        List<Operate> list = operateCacheService.getUserOperates(token);
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (url.equals(list.get(i).getUrl())) {
                return true;
            }
        }
        return false;

    }


    public List<Map<String, Object>> searchUserNoTitleByName(String userName, int currentPageInt, int pageSizeInt, String loginUserId) {

        Map<String, Object> loginUserMap = userMapper.selectUserById(loginUserId);
        if ("1".equals(loginUserMap.get("roleId"))) {
            return userMapper.searchUserNoTitleByName(userName, currentPageInt, pageSizeInt, null);
        } else {
            return userMapper.searchUserNoTitleByName(userName, currentPageInt, pageSizeInt, (String) loginUserMap.get("pcCompanyId"));

        }
    }


    public Integer searchUserNoTitleByNameTotal(String userName, String loginUserId) {
        Map<String, Object> loginUserMap = userMapper.selectUserById(loginUserId);
        if ("1".equals(loginUserMap.get("roleId"))) {
            return userMapper.searchUserNoTitleByNameTotal(userName, null);

        } else {
            return userMapper.searchUserNoTitleByNameTotal(userName, (String) loginUserMap.get("pcCompanyId"));


        }
    }


    /**
     * @param map
     * @return 0更新失败，1更新成功，2手机号已经存在，3邮箱已经存在
     */
    public Integer updatePcUserInfoById(Map<String, Object> map) {

        if (map == null || map.size() == 0 || map.get("id") == null) {
            return 0;
        }

        String mobile = map.get("mobile") == null ? null : (String) map.get("mobile");
        String email = map.get("email") == null ? null : (String) map.get("email");
        String id = (String) map.get("id");

        if (mobile != null) {
            Map<String, Object> userMobileMap = selectUserByMobile(mobile, null);
            if (userMobileMap != null && userMobileMap.size() > 0) {
                String existsUserId = (String) userMobileMap.get("id");
                if (!id.equals(existsUserId)) {
                    return 2;
                }
            }
        }

        if (email != null) {
            Map<String, Object> userEmailMap = selectUserByEmail(email, null);
            if (userEmailMap != null && userEmailMap.size() > 0) {
                String existsUserId = (String) userEmailMap.get("id");
                if (!id.equals(existsUserId)) {
                    return 3;
                }
            }
        }
        updateDcUser(map);
        //删除缓存中的用户
        redisCacheUtil.delete(RedisTableKey.USER, (String) map.get("id"));
        return 1;
    }

    public List<Map<String, Object>> selectUsers(String pcCompanyId, String content) {
        return userMapper.selectUsers(pcCompanyId, content);
    }


    public List<Map<String, Object>> selectAllUsers() {
        return userMapper.selectAllUsers();
    }

    public List<String> selectUserByRoleIds(List<String> roleIdList) {
        if (roleIdList.size() == 0) {
            return new ArrayList<>(0);
        }
        return userMapper.selectUserIdByRoleIds(roleIdList);
    }


    public List<String> selectUserByRoleIds(String roleId) {
        List<String> roleIdList = new ArrayList<>();
        roleIdList.add(roleId);
        return userMapper.selectUserIdByRoleIds(roleIdList);
    }


    List<String> selectUserIdByDepartmentIds(List<String> departmentIdList) {
        if (departmentIdList.size() == 0) {
            return new ArrayList<>(0);
        }
        return userMapper.selectUserIdByDepartmentIds(departmentIdList);
    }

    List<String> selectUserIdByDepartmentId(String departmentId) {
        List<String> idList = new ArrayList<>();
        idList.add(departmentId);
        return userMapper.selectUserIdByDepartmentIds(idList);
    }

    List<String> selectUserIdByPcCompanyIds(String pcCompanyId) {
        List<String> idList = new ArrayList<>();
        idList.add(pcCompanyId);
        return userMapper.selectUserIdByPcCompanyIds(idList);
    }

    /**
     * 注册用户
     * @param platform
     * @param uname
     * @param tel
     * @param password
     * @param companyName
     */
    public void businessCrmRegister(String platform, String uname, String tel, String password, String companyName) {


        String companyId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String roleId = UUID.randomUUID().toString();

        Map<String,Object> map=new HashMap<>();
        Map<String,Object> userMap=new HashMap<>();

        map.put("id", companyId);
        map.put("parentId","1");
        map.put("departmentName",companyName);
        map.put("mobile",tel);
        map.put("dateFrom", DateUtils.dateToString(new Date(),"yyyy-MM-dd"));
        map.put("createDate",new Date());
        map.put("createdBy",userId);
        map.put("enableStatus",1);

        //在企业列表下，插入企业客户并创建管理员
        //customerService.savePcCompany(map,userId);
        /**
         * 添加该公司下的管理员帐号
         */
        Map<String,Object> roleMap=new HashMap<>();
        roleMap.put("id", roleId);
        roleMap.put("roleName","管理员");
        roleMap.put("content","管理员");
        roleMap.put("createdBy",userId);
        roleMap.put("createDate",new Date());
        roleMap.put("updatedBy",userId);
        roleMap.put("updateDate",new Date());
        roleMap.put("pcCompanyId",companyId);
        roleMap.put("isAdmin",1);

        customerMapper.insertAdminRole(roleMap);
        customerMapper.insertPcCompany(map);

        //默认的注册角色模板权限,创建新的角色，将操作权限赋值给新的角色
        List<Map<String,Object>>  roleOperateIdList = roleService.selectOperateIdRoleId("111111");

        if(roleOperateIdList != null && roleOperateIdList.size() > 0){
            for(Map<String,Object> m : roleOperateIdList){
                m.put("id",UUID.randomUUID().toString());
                m.put("roleId",roleId);
            }
        }
        roleOperateService.insertList(roleOperateIdList);

        userMap.put("id",userId);
        userMap.put("platform",1);
        userMap.put("uname",uname);
        userMap.put("platform",Integer.parseInt(platform));
        userMap.put("mobile", tel);
        //新注册的用户部门IDM,departmentId和公司ID,pcCompanyId一样的，因为是没有部门的
        userMap.put("departmentId",companyId);
        userMap.put("pcCompanyId",companyId);
        userMap.put("status", "0");  //恢复为未删除状态
        userMap.put("id", UUID.randomUUID().toString());
        userMap.put("createDate",new Date());
        userMap.put("password",password);
        userMap.put("createdBy", userId);
        userMap.put("roleId",roleId);

        userMapper.insertDcUser(userMap);
        userMapper.insertDcUserDetail(userMap);

    }
}
