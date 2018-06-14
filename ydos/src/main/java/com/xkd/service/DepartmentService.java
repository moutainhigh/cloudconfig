package com.xkd.service;

import com.xkd.mapper.DepartmentDao;
import com.xkd.model.DepartmentTreeNode;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.vendor.OpenJpaDialect;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.*;

@Service
public class DepartmentService {

    @Autowired
    DepartmentDao departmentDao;
    @Autowired
    UserService userService;


    /**
     * 查询部门树
     *
     * @return
     */
    public DepartmentTreeNode selectAllDepartment(Map<String, Object> loginUser) {

        List<Map<String, Object>> allDepartmentList = null;

        if ("1".equals(loginUser.get("roleId"))) {//如果是超级管理员，则查询所有树形结构
            allDepartmentList = departmentDao.selectAllDepartment();
        } else {
            //如果不是超级管理员，则查询该员工所有公司下的所有部门
            allDepartmentList = selectChildDepartmentsByDepartmentId((String) loginUser.get("pcCompanyId"));
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < allDepartmentList.size(); i++) {
            list.add((String) allDepartmentList.get(i).get("id"));
        }


        return buildTree(allDepartmentList);

    }






    /**
     * 将list列表构建成一颗树树形结构
     *
     * @param allDepartmentList
     * @return
     */
    public DepartmentTreeNode buildTree(List<Map<String, Object>> allDepartmentList) {
        DepartmentTreeNode rootTreeNode = getRootDepartmentTreeNode(allDepartmentList);
        if (rootTreeNode != null) {
            reshuffle(rootTreeNode, allDepartmentList);
        }
        LinkedList<DepartmentTreeNode> queue = new LinkedList<>();
        queue.offer(rootTreeNode);
        sortNode(rootTreeNode, queue);
        return rootTreeNode;
    }


    //将有同一层级有子节点的节点放在前面
    public void sortNode(DepartmentTreeNode departmentTreeNode, Queue<DepartmentTreeNode> queue) {
        DepartmentTreeNode node = queue.poll();
        if (null == node) {
            return;
        }
        List<DepartmentTreeNode> oldList = node.getChildrenList();

        List<DepartmentTreeNode> newList = new ArrayList<>();
        List<DepartmentTreeNode> newList1 = new ArrayList<>();
        List<DepartmentTreeNode> newList2 = new ArrayList<>();
        for (int i = 0; i < oldList.size(); i++) {
            DepartmentTreeNode node2 = oldList.get(i);
            if (node2.getChildrenList() != null && node2.getChildrenList().size() > 0) {
                newList1.add(node2);
            } else {
                newList2.add(node2);
            }
            queue.offer(node2);
        }
        newList.addAll(newList1);
        newList.addAll(newList2);
        node.setChildrenList(newList);

    }





    public DepartmentTreeNode getRootDepartmentTreeNode(List<Map<String, Object>> allDepartmentList) {

        for (int i = 0; i < allDepartmentList.size(); i++) {
            Map<String, Object> m = allDepartmentList.get(i);
            boolean isRoot = true;
            for (int j = 0; j < allDepartmentList.size(); j++) {
                if (m.get("parentId").equals(allDepartmentList.get(j).get("id"))) {
                    isRoot = false;
                }
            }

            if (isRoot) {
                DepartmentTreeNode node = new DepartmentTreeNode();
                node.setCanAddChild(true);
                node.setCanDelete(false);
                node.setDepartmentName((String) allDepartmentList.get(i).get("departmentName"));
                node.setId((String) allDepartmentList.get(i).get("id"));
                node.setParentId((String) allDepartmentList.get(i).get("parentId"));
                node.setPrincipalId((String) allDepartmentList.get(i).get("principalId"));
                node.setPrincipal((String) allDepartmentList.get(i).get("principal"));
                node.setRemark((String) allDepartmentList.get(i).get("remark"));
                node.setUserList((List<Map<String, Object>>) allDepartmentList.get(i).get("users"));
                return node;
            }
        }
        return null;

    }


    // 递归调用，构建树形 结构
    public void reshuffle(DepartmentTreeNode departmentTreeNode, List<Map<String, Object>> allDepartmentList) {


        for (int i = 0; i < allDepartmentList.size(); i++) {
            Map<String, Object> dep = allDepartmentList.get(i);
            if (departmentTreeNode.getId().equals(dep.get("parentId"))) {

                DepartmentTreeNode node = new DepartmentTreeNode();
                //根结点不能添加子结点，根结点的子结点表示客户公司，其应由帐户管理界面添加
                if ("1".equals((String) dep.get("id"))) {
                    node.setCanAddChild(false);//是否可以添加子结点
                    node.setCanDelete(false); //是否能删除
                } else {
                    if (1 == (Integer) dep.get("isTeam")) {//如果是班组
                        node.setCanAddChild(false);
                        node.setCanDelete(true);
                    } else {
                        node.setCanAddChild(true);
                        node.setCanDelete(true);
                    }
                }

                node.setDepartmentName((String) dep.get("departmentName"));
                node.setId((String) dep.get("id"));
                node.setParentId((String) dep.get("parentId"));
                node.setPrincipalId((String) dep.get("principalId"));
                node.setPrincipal((String) dep.get("principal"));
                node.setRemark((String) dep.get("remark"));
                node.setUserList((List<Map<String, Object>>) dep.get("users"));
                departmentTreeNode.getChildrenList().add(node);

            }
        }
        if (departmentTreeNode.getChildrenList().size() == 0) {
            return;
        }

        for (int i = 0; i < departmentTreeNode.getChildrenList().size(); i++) {
            reshuffle(departmentTreeNode.getChildrenList().get(i), allDepartmentList);
        }

    }


    /**
     * 查询某一个部门下所有子节点的Id
     *
     * @param departmentId
     * @return
     */
    public List<String> selectChildDepartmentIds(String departmentId, Map<String, Object> loginUser) {


        DepartmentTreeNode departmentTreeNode = selectAllDepartment(loginUser); //全树

        //找到子树根结点
        DepartmentTreeNode targetDepartmentTreeNode = findDepartmentTreeNode(departmentId, departmentTreeNode);
        List<String> childDepartmentIdList = new ArrayList<>();
        childDepartmentIdList.add(departmentId);//先加入子树根节点
        //递归查询子树然后获取子结点所有ID
        if (targetDepartmentTreeNode == null) {
            return childDepartmentIdList;
        }
        filterChildDepartmentId(targetDepartmentTreeNode, childDepartmentIdList);
        return childDepartmentIdList;

    }


    private void filterChildDepartmentId(DepartmentTreeNode departmentTreeNode, List<String> list) {

//	   if (departmentTreeNode.getChildrenList().size()==0) {
//		return;
//	  }
        for (int i = 0; i < departmentTreeNode.getChildrenList().size(); i++) {
            list.add(departmentTreeNode.getChildrenList().get(i).getId());//将子节点ID加入到list表中
        }
        for (int i = 0; i < departmentTreeNode.getChildrenList().size(); i++) {
            filterChildDepartmentId(departmentTreeNode.getChildrenList().get(i), list);//递归过滤
        }

    }

    /**
     * 查找指定部门Id的结点
     *
     * @param departmentTreeNodeId
     * @param departmentTreeNode
     * @return
     */
    private DepartmentTreeNode findDepartmentTreeNode(String departmentTreeNodeId, DepartmentTreeNode departmentTreeNode) {
        if (departmentTreeNodeId.equals(departmentTreeNode.getId())) {
            return departmentTreeNode;
        }

        DepartmentTreeNode target = null;
        for (int i = 0; i < departmentTreeNode.getChildrenList().size(); i++) {
            target = findDepartmentTreeNode(departmentTreeNodeId, departmentTreeNode.getChildrenList().get(i));
            if (target != null) {
                return target;
            }
        }

        return target;


    }


    public int update(Map<String, Object> map) {
        return departmentDao.update(map);
    }

    public int insert(Map<String, Object> map) {
        return departmentDao.insert(map);
    }


    public int delete(@Param("id") String id) {

        return departmentDao.delete(id);
    }

    public List<Map<String, Object>> searchTeamByPcCompanyId(String pcCompanyId, String departmentName, Integer currentPage, Integer pageSize) {
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        List<Map<String,Object>> list= departmentDao.searchTeamByPcCompanyId(pcCompanyId, departmentName, start, pageSize);
        for (int i = 0; i <list.size() ; i++) {
            list.get(i).put("no",i+1);
        }
        return list;
    }

    public Integer searchTeamCountByPcCompanyId(String pcCompanyId, String teamName) {
        return departmentDao.searchTeamCountByPcCompanyId(pcCompanyId, teamName);
    }


    public Map<String, Object> selectTeamByNameAndPcCompanyId(String teamName,String pcCompanyId) {
        return departmentDao.selectTeamByNameAndPcCompanyId(teamName, pcCompanyId);
    }



    public List<Map<String, Object>> selectDepartmentByIds(  List<String> idList,Integer currentPage,Integer pageSize) {
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        if (idList.size() > 0) {
            return departmentDao.selectDepartmentByIds(idList,start,pageSize);
        }
        return new ArrayList<>();
    }
    public Integer selectDepartmentCountByIds(@Param("idList") List<String> idList) {
        if (idList.size() > 0) {
            return departmentDao.selectDepartmentCountByIds(idList);
        }
        return 0;
    }






    public Map<String, Object> selectDepartmentById(String id) {
        return departmentDao.selectDepartmentById(id);
    }





    /**
     * 根据部门Id获取客户公司结点Id，即二级结点
     *
     * @param departmentId
     * @return
     */
    public Map<String, Object> getCompanyIdByDepartmentId(String departmentId) {
        Map<String, Object> departmentMap = departmentDao.selectDepartmentById(departmentId);
        return getDepartmentCompanyTreeNode(departmentMap);
    }

    /**
     * 获取代表公司的二级结点
     *
     * @param map
     * @return
     */
    private Map<String, Object> getDepartmentCompanyTreeNode(Map<String, Object> map) {
        if ("1".equals(map.get("parentId"))) { //如果父结点是根结点 ，说明该结点为二级结点，就是客户公司结点
            return map;
        } else if ("0".equals(map.get("parentId"))) {
            return null;
        } else {
            Map<String, Object> departmentMap = departmentDao.selectDepartmentById((String) map.get("parentId"));
            return getDepartmentCompanyTreeNode(departmentMap);
        }
    }


    /**
     * 查询出某一个部门下的子部门Id，通过迭代查询的方式
     *
     * @param departmentId
     * @return
     */
    public List<Map<String, Object>> selectChildDepartmentsByDepartmentId(String departmentId) {
        Queue<Map<String, Object>> queue = new LinkedList<>();
        List<Map<String, Object>> departmentIdList = new ArrayList<>();
        Map<String, Object> departmentM = departmentDao.selectDepartmentById(departmentId);
        queue.offer(departmentM);
        selectChildDepartmentIdsRecusive(departmentIdList, queue);
        return departmentIdList;
    }

    /**
     * 迭代，按层遍历查询子结点
     *
     * @param list
     * @param queue
     */
    private void selectChildDepartmentIdsRecusive(List<Map<String, Object>> list, Queue<Map<String, Object>> queue) {
        Map<String, Object> map = queue.poll();
        if (null != map) {
            String id = (String) map.get("id");
            list.add(map);
            List<Map<String, Object>> childList = departmentDao.getTreeByPid(id);
            for (int i = 0; i < childList.size(); i++) {
                queue.offer(childList.get(i));
            }
            selectChildDepartmentIdsRecusive(list, queue);
        }

    }






}
