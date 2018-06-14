package com.xkd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.MenuMapper;
import com.xkd.model.Menu;
import com.xkd.model.MenuTreeNode;

/**
 * 创建人：巫建辉
 * 创建时间:2017-11-23
 * 功能描述:菜单相关的服务
 */
@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;


    public List<Menu> selectMenusByParentMenuId(String parentMenuId) {

        List<Menu> menus = menuMapper.selectMenusByParentMenuId(parentMenuId);

        return menus;
    }


    public List<Menu> selectMenus(String menuNameSql, String menuGradeSql, int pageSizeInt, int currentPageInt) {

        List<Menu> menus = menuMapper.selectMenus(menuNameSql, menuGradeSql, pageSizeInt, currentPageInt);

        return menus;
    }

    public Integer deleteMenusByIds(String idsString) {


        Integer num = menuMapper.deleteMenusByIds(idsString);

        return num;

    }

    public Integer saveMenu(Map<String, Object> paramMap) {


        Integer num = menuMapper.saveMenu(paramMap);

        return num;

    }

    public String selectMaxMenuIdByParentId(String parentMenuId) {


        String maxMenuId = menuMapper.selectMaxMenuIdByParentId(parentMenuId);

        return maxMenuId;

    }

    public Integer updateMenuBy(Map<String, Object> paramMap) {


        Integer num = menuMapper.updateMenuBy(paramMap);

        return num;

    }

    public Menu selectMenuById(String id) {


        Menu menu = menuMapper.selectMenuById(id);

        return menu;

    }

    public List<Menu> selectMenuByIds(String ids) {

        List<Menu> menus = menuMapper.selectMenuByIds(ids);

        return menus;
    }


    public Integer selectMenusCount(String menuNameSql, String menuGradeSql) {


        Integer num = menuMapper.selectMenusCount(menuNameSql, menuGradeSql);

        return num;

    }

    /**
     * 查询所有叶结点菜单
     *
     * @return
     */
    public List<Map<String, Object>> selectAllLeafMenu() {
        List<Map<String, Object>> allMenuList = menuMapper.selectAllMenu();
        List<Map<String, Object>> leafMenuList = new ArrayList<>();
        for (int i = 0; i < allMenuList.size(); i++) {
            Map<String, Object> map = allMenuList.get(i);
            boolean hasChildren = false;
            for (int j = 0; j < allMenuList.size(); j++) {
                if (map.get("menuId").equals(allMenuList.get(j).get("parentMenuId"))) {
                    hasChildren = true;
                    break;
                }
            }
            if (!hasChildren) {
                leafMenuList.add(map);
            }
        }
        return leafMenuList;

    }


    /**
     * 查询某一个用户对应的可见菜单
     *
     * @param userId
     * @return
     */
    public List<MenuTreeNode> selectAllMenuByUserId(String userId) {
        List<Map<String, Object>> allUserMenuList = menuMapper.selectAllMenuByUserId(userId);
        List<Map<String, Object>> allMenuList = menuMapper.selectAllMenu();
        //从叶节点往上过滤出所有被引用过的节点
        Set<Map<String, Object>> set = new HashSet<>();
        for (int i = 0; i < allUserMenuList.size(); i++) {
            filterMenu(allUserMenuList.get(i), allMenuList, set);
        }

        //构造根结点
        MenuTreeNode rootNode = new MenuTreeNode();
        rootNode.setId("-1");
        rootNode.setMenuId("0");
        rootNode.setParentMenuId(null);
        reshuffle(rootNode, set);
        List<MenuTreeNode> list = rootNode.getChildren();
        java.util.Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            java.util.Collections.sort(list.get(i).getChildren());
        }
        return list;
    }

    //从子节点往上迭代找出父节点放到Set中去重
    public void filterMenu(Map<String, Object> map, List<Map<String, Object>> allMenuList, Set<Map<String, Object>> set) {
        set.add(map);
        if ("0".equals(map.get("parentMenuId"))) {
            return;
        }
        for (int i = 0; i < allMenuList.size(); i++) {
            if (map.get("parentMenuId").equals(allMenuList.get(i).get("menuId"))) {
                set.add(allMenuList.get(i));
                filterMenu(allMenuList.get(i), allMenuList, set);
            }
        }
    }

    /**
     * 内部方法
     *
     * @param menuTreeNode
     * @param set
     */
    private void reshuffle(MenuTreeNode menuTreeNode, Set<Map<String, Object>> set) {

        Iterator<Map<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> menu = iterator.next();
            if (menu.get("parentMenuId").equals(menuTreeNode.getMenuId())) {
                MenuTreeNode treeNode = new MenuTreeNode();
                treeNode.setId((String) menu.get("id"));
                treeNode.setComponentsPath((String) menu.get("componentsPath"));
                treeNode.setRouterPath((String) menu.get("routerPath"));
                treeNode.setTitle((String) menu.get("title"));
                treeNode.setParentMenuId((String) menu.get("parentMenuId"));
                treeNode.setMenuId((String) menu.get("menuId"));
                treeNode.setRouterName((String) menu.get("routerName"));
                treeNode.setIconNormalPath((String) menu.get("iconNormalPath"));
                treeNode.setIconCheckPath((String) menu.get("iconCheckPath"));
                treeNode.setMenuLevel((Integer) menu.get("menuLevel"));
                menuTreeNode.getChildren().add(treeNode);

            }
        }
        if (menuTreeNode.getChildren().size() == 0) {
            return;
        }
        for (int i = 0; i < menuTreeNode.getChildren().size(); i++) {
            reshuffle(menuTreeNode.getChildren().get(i), set);
        }


    }


    public List<MenuTreeNode> getMyMenu(String loginUserId) {
        //我有权限的所有二级菜单
        List<MenuTreeNode> childrenMenu = menuMapper.getMyChildrenMenu(loginUserId);
        if(childrenMenu.size() > 0){
            //一级
            List<MenuTreeNode> oneMenu = menuMapper.getMyOneMenu(childrenMenu);
            for (MenuTreeNode one:oneMenu) {
                for (MenuTreeNode children:childrenMenu) {
                    if(one.getMenuId().equals(children.getParentMenuId())){
                        List<MenuTreeNode> childrenList = one.getChildren();
                        if(childrenList.size() == 0){
                            childrenList = new ArrayList<>();
                        }
                        childrenList.add(children);
                        one.setChildren(childrenList);
                    }
                }
            }
            System.out.println(System.currentTimeMillis());
            return oneMenu;
        }else{
            return null;
        }
    }
}
