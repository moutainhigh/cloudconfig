package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.mapper.DepartmentDao;
import com.xkd.mapper.UserDataPermissionMapper;
import com.xkd.model.Company;
import com.xkd.model.DepartmentTreeNode;
import com.xkd.model.RedisTableKey;
import com.xkd.utils.RedisCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by dell on 2017/12/14.
 */
@Service
public class UserDataPermissionService {
    @Autowired
    UserDataPermissionMapper userDataPermissionMapper;
    @Autowired
    DepartmentDao departmentDao;


    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyRelativeUserService companyRelativeUserService;

    @Autowired
    RedisCacheUtil redisCacheUtil;

//   static ConcurrentHashMap<String,List<String>> dataPermission=new ConcurrentHashMap<>();

    static String lock = "lock";

    @PostConstruct
    void init() {
        //启动一个线程来每天清理一次权限缓存,主要是为了防止一些僵尸用户，登录进来以后却不退出
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                redisCacheUtil.delete(RedisTableKey.DATAPERMISSION);
            }
        }, new Date(), 1000 * 60 * 60 * 24);
    }

    /**
     * 获取用户有权限的部门Id集合与所选择部门的所有子部门的Id集合的交集
     * departmentId 如果为null则，返回用户的所有权限部门
     * userId 不能为空
     *
     * @return
     */
    public List<String> getDataPermissionDepartmentIdList(String departmentId, String userId) {

        Map<String, Object> loginUserMap = userService.selectUserById(userId);


        synchronized (lock) {
            String cacheStr = (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DATAPERMISSION, userId);

            if (cacheStr == null) {
                /**
                 * 加载权限
                 */
                List<String> list = selectDepartmentIdListByLoginUserId(userId, 1);

                Map<String, Object> map = new HashMap<>();
                map.put(userId, JSON.toJSONString(list));
                redisCacheUtil.setCacheMap(RedisTableKey.DATAPERMISSION, map);
            }

        }


        Map<String, Object> userMap = userService.selectUserById(userId);


        if (StringUtils.isBlank(departmentId)) {
            if ("1".equals(userMap.get("roleId"))) {//如果是超级管理员
                //所有部门权限
                return departmentService.selectChildDepartmentIds("1", loginUserMap);
            }
            String cacheStr = (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DATAPERMISSION, userId);
            List<String> cacheList = JSON.parseObject(cacheStr, new TypeReference<List<String>>() {
            });
            return cacheList;


        } else {

            //查询某一个部门下的所有子结点Id
            List<String> childIdList = departmentService.selectChildDepartmentIds(departmentId, loginUserMap);

            List<String> permitedIdList = new ArrayList<>();

            if ("1".equals(userMap.get("roleId"))) {
                permitedIdList = departmentService.selectChildDepartmentIds("1", loginUserMap);
            } else {
                String cacheStr = (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DATAPERMISSION, userId);
                List<String> cacheList = JSON.parseObject(cacheStr, new TypeReference<List<String>>() {
                });
                permitedIdList = cacheList;
            }

            /**
             * 求权限部门交集
             */
            Set<String> set = new HashSet<>();
            set.addAll(permitedIdList);
            set.retainAll(childIdList);

            Iterator<String> iterator = set.iterator();
            List<String> newList = new ArrayList<>();
            while (iterator.hasNext()) {
                newList.add(iterator.next());
            }
            return newList;
        }


    }


    /**
     * 查询某一个用户对哪些部门数据有权限，该部门Id包括子部门
     *
     * @param userId
     * @return
     * @Param flag 1 表是包含人员所在部门ID    0不包含
     */
    private List selectDepartmentIdListByLoginUserId(String userId, Integer flag) {

        Map<String, Object> userMap = userService.selectUserById(userId);

        Set<String> departmentIdSet = new HashSet<>();
        List<String> list = userDataPermissionMapper.selectDepartmentIdsByUserId(userId);

        if (1 == (Integer) userMap.get("isAdmin")) {//如果是公司管理员，则添加该管理员所在公司权限
            list.add((String) userMap.get("pcCompanyId"));
        }


        for (int i = 0; i < list.size(); i++) {
            /**
             * 查询子部门列表
             */
            List<String> childIds = departmentService.selectChildDepartmentIds(list.get(i), userMap);
            departmentIdSet.addAll(childIds);

        }
        Iterator<String> iterator = departmentIdSet.iterator();
        List<String> allDepartmentIds = new ArrayList<>();
        while (iterator.hasNext()) {
            allDepartmentIds.add(iterator.next());
        }


        if (1 == flag) {
            //添加用户所在部门Id
            String userDepartmentId = (String) userMap.get("departmentId");
            if (userDepartmentId != null) {
                /**
                 * 查询出用户所在部门下的子部门
                 */
                List<String> childIds = departmentService.selectChildDepartmentIds(userDepartmentId, userMap);
                for (int i = 0; i < childIds.size(); i++) {
                    if (!allDepartmentIds.contains(childIds.get(i))) {
                        allDepartmentIds.add(childIds.get(i));
                    }
                }

            }
        }
        return allDepartmentIds;

    }


    private void init(DepartmentTreeNode departmentTreeNode, List<String> list, String userDepartmentId) {
        if (list.contains(departmentTreeNode.getId())) {
            departmentTreeNode.setChecked(true);
        }
        if (departmentTreeNode.getId().equals(userDepartmentId)) {
            //如果是用户所在部门，则不允许用户取消选择
            departmentTreeNode.setCanchoose(false);
        }
        List<DepartmentTreeNode> childrenList = departmentTreeNode.getChildrenList();
        if (childrenList.size() > 0) {
            for (int i = 0; i < childrenList.size(); i++) {
                init(childrenList.get(i), list, userDepartmentId);
            }
        }
    }


    /**
     * 获取数据权限配置的全部树形结构
     *
     * @param userId
     * @return
     */
    public DepartmentTreeNode getCheckedUserDataPermissionDepartmentTree(String userId) {
        List<String> list = selectDepartmentIdListByLoginUserId(userId, 1);
        Map<String, Object> userMap = userService.selectUserById(userId);

        String userDepartmentId = (String) userMap.get("departmentId");

        DepartmentTreeNode treeNode = departmentService.selectAllDepartment(userMap);
        init(treeNode, list, userDepartmentId);
        return treeNode;
    }

    /**
     * 插入数据权限部门配置信息
     *
     * @param userId
     * @return departmentIdList
     */
    public int saveUserDataPermissionDepartmentIdList(String userId, List<String> departmentIdList) {
        userDataPermissionMapper.deleteUserDataPermissionByUserId(userId);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0; i < departmentIdList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("departmentId", departmentIdList.get(i));
            map.put("id", UUID.randomUUID().toString());
            mapList.add(map);
        }
        int count = 0;
        if (mapList.size() > 0) {
            count = userDataPermissionMapper.insertUserDataPermissionList(mapList);
            //重新加载用户的权限数据
        }
        reloadCacheData(userId);
        return count;
    }


    private void reloadCacheData(String userId) {
        List<String> list = selectDepartmentIdListByLoginUserId(userId, 1);
        Map<String, Object> map = new HashMap<>();
        map.put(userId, JSON.toJSONString(list));
        redisCacheUtil.setCacheMap(RedisTableKey.DATAPERMISSION, map);
    }

    public void clearCacheData(String userId) {
        redisCacheUtil.delete(RedisTableKey.DATAPERMISSION, userId);
    }


    public void clearCacheData() {
        redisCacheUtil.delete(RedisTableKey.DATAPERMISSION);
    }


    public List<Map<String, Object>> selectDepartmentByUserId(String userId) {
        List<String> list = selectDepartmentIdListByLoginUserId(userId, 1);
        return departmentService.selectDepartmentByIds(list);
    }


    public boolean hasPermission(String companyId, String userId) {

        /**
         * 判断企业是否有权限被修改
         */
        boolean hasPermission = false;

        Map<String, Object> userMap = userService.selectUserById(userId);
        if ("1".equals(userMap.get("roleId"))) {
            return true; //如果是超级管理员
        }


        List<String> permissionDepartmentIdList = getDataPermissionDepartmentIdList(null, userId);
        Company companyInDb = companyService.selectCompanyInfoById(companyId);


        List<String> companyIdList = new ArrayList<>();
        companyIdList.add(companyId);
        List<Map<String, Object>> relativeUserList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);

        String directorId = companyInDb.getCompanyDirectorId();
        String advisorId = companyInDb.getCompanyAdviserId();
        String createdBy = companyInDb.getCreatedBy();

        //如果是相关人员，则直接有编辑权限
        if (userId.equals(directorId) || userId.equals(advisorId) || userId.equals(createdBy) || relativeUserList.contains(userId)) {
            return true;
        }
        /**
         * 如果用户没有配某个部门下企业的权限，则即使用户配置了编辑企业的权限，也认为其没有权限，其编辑权限是相对自己
         * 所见视图而言的，举个例子,用户在企业列表页面不能看到某个企业，但从会务中可以看到该企业，那么从会务入口就不能
         * 编辑该企业
         */
        if (null != permissionDepartmentIdList && companyInDb != null) {
            if (null != companyInDb.getDepartmentId()) {
                if (permissionDepartmentIdList.contains(companyInDb.getDepartmentId())) {
                    hasPermission = true;
                }
            }
        }

        return hasPermission;
    }


    /**
     * 获取用户数据权限的子树，用户没有配置的结点不在该子树中
     *
     * @param userId
     * @return
     */
    public DepartmentTreeNode selectPermitedDepartmentTree(String userId) {
        //查询所完整树
        List<Map<String, Object>> allDepartmentList = departmentService.selectAllDepartmentMap(userId);

        //查询为某个用户配置的数据权限部门Id 列表，包括子部门Id
        List<String> permitedList = getDataPermissionDepartmentIdList(null, userId);


        /**
         * 从每个有权限的结点往上递归 ，并将其经过路径中的结点放到一个Set集合中，这样 可以取掉重复的结点
         */
        Set<Map<String, Object>> set = new HashSet<>();
        for (int i = 0; i < permitedList.size(); i++) {
            shuffle(permitedList.get(i), set, allDepartmentList);
        }


        Iterator<Map<String, Object>> iterator = set.iterator();
        List<Map<String, Object>> listMap = new ArrayList<>();
        while (iterator.hasNext()) {
            listMap.add(iterator.next());
        }
        /**
         * 构建树形结构
         */
        DepartmentTreeNode tree = departmentService.buildTree(listMap);


        return tree;

    }


    private void shuffle(String id, Set<Map<String, Object>> set, List<Map<String, Object>> allDepartmentList) {

        for (int i = 0; i < allDepartmentList.size(); i++) {
            Map<String, Object> map = allDepartmentList.get(i);
            if (id.equals(map.get("id"))) {
                set.add(map);
                String parentId = (String) map.get("parentId");
                //如果到达了树形结构根节点，则直接终止
                if ("0".equals(parentId)) {
                    break;
                }
                shuffle(parentId, set, allDepartmentList);
            }
            ;
        }

    }


    public int deleteUserDataPermissionByUserIdAndDepartmentId(String userId, String departmentId) {
        return userDataPermissionMapper.deleteUserDataPermissionByUserIdAndDepartmentId(userId, departmentId);
    }


    public int deleteUserDataPermissionByUserIds(List<String> userIdList) {
        return userDataPermissionMapper.deleteUserDataPermissionByUserIds(userIdList);
    }


    public int deleteUserDataPermissionByUserId(String userId) {
        return userDataPermissionMapper.deleteUserDataPermissionByUserId(userId);
    }


}
