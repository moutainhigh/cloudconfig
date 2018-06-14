package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Menu;
import com.xkd.model.MenuTreeNode;


public interface MenuMapper {

	List<Menu> selectParentMenus();

	List<Menu> selectMenus(@Param("menuNameSql") String menuNameSql, @Param("menuGradeSql") String menuGradeSql,
						   @Param("pageSize") int pageSize, @Param("currentPage") int currentPage);

	Integer deleteMenusByIds(@Param("ids") String ids);

	Integer saveMenu(Map<String, Object> paramMap);

	String selectMaxMenuIdByParentId(@Param("parentMenuId") String parentMenuId);

	Integer updateMenuBy(Map<String, Object> paramMap);

	Menu selectMenuById(@Param("id") String id);

	List<Menu> selectMenusByParentMenuId(@Param("parentMenuId") String parentMenuId);

	List<Menu> selectMenuByIds(@Param("ids") String ids);

	Menu seleMenuByMenuId(@Param("menuId") String menuId);

	Integer selectMenusCount(@Param("menuNameSql") String menuNameSql, @Param("menuGradeSql") String menuGradeSql);

	List<Map<String,Object>> selectAllMenuByUserId(@Param("userId") String userId); 
	
	List<Map<String,Object>> selectAllMenu();

    List<MenuTreeNode> getMyChildrenMenu(@Param("userId") String userId);

	List<MenuTreeNode> getMyOneMenu(@Param("childrenMenu") List<MenuTreeNode> childrenMenu);
}
