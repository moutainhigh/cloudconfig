package com.xkd.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.xkd.model.Menu;
import com.xkd.service.MenuService;

public class MenuUtil {

	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月27日 
	 * @功能描述:新增菜单，获得所有子菜单
	 * @param req
	 * @param rsp
	 * @return
	 */
	public static List<Map<String,Object>> getChild(String menuId, List<Menu> allMenu) {
		
	    // 子菜单
	    List<Map<String,Object>> childList = new ArrayList<>();
	    for (Menu menu : allMenu) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (StringUtils.isNotBlank(menu.getParentMenuId())) {
	        	
	            if (menu.getParentMenuId().equals(menuId)) {
	            	
	            	Map<String,Object> map = new HashMap<>();
	            	
	            	map.put("value",menu.getMenuId());
		        	map.put("label",menu.getMenuName());
	            	
	                childList.add(map);
	            }
	        }
	    }
	    
	    if (childList.size() == 0) {
	        return null;
	    }
	    
	    // 把子菜单的子菜单再循环一遍
	    for (Map<String,Object> menu : childList) {
				
			menu.put("children",getChild((String)menu.get("value"), allMenu));
            
	    } 
	    
	   
	    return childList;
	}

	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月27日 
	 * @功能描述:新增角色中获得菜单
	 * @param req
	 * @param rsp
	 * @return
	 */
	public static Object getRoleMenuChild(String menuId, List<Menu> allMenu) {
		
		
	    List<Map<String,Object>> childList = new ArrayList<>();
	    for (Menu menu : allMenu) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (StringUtils.isNotBlank(menu.getParentMenuId())) {
	        	
	            if (menu.getParentMenuId().equals(menuId)) {
	            	
	            	Map<String,Object> map = new HashMap<>();
	            	
	            	map.put("expand",true);
		        	map.put("title",menu.getMenuName());
		        	map.put("id",menu.getId());
		        	map.put("menuId",menu.getMenuId());
		        	map.put("checked",false);
	            	
	                childList.add(map);
	            }
	        }
	    }
	    
	    if (childList.size() == 0) {
	        return null;
	    }
	    
	    // 把子菜单的子菜单再循环一遍
	    for (Map<String,Object> menu : childList) {
				
			menu.put("children",getRoleMenuChild((String)menu.get("menuId"), allMenu));
	    } 
	   
	    return childList;
	}
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月27日 
	 * @功能描述:根据选中的菜单查出所有父菜单
	 * @param req
	 * @param rsp
	 * @return
	 */
	public static Object getRoleMenuParent(String menuId, List<Menu> allMenu,Set<Map<String, Object>> setMaps) {
		
		
	    List<Map<String,Object>> parentList = new ArrayList<>();
	    for (Menu menu : allMenu) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (StringUtils.isNotBlank(menu.getParentMenuId()) && StringUtils.isNotBlank(menu.getId()) && menu.getMenuId().equals(menuId)) {
	        	
	            	
	            	Map<String,Object> map = new HashMap<>();
	            	
	            	map.put("menuId",menu.getParentMenuId());
	            	
	            	setMaps.add(map);
	            }
	        }
	    
	    if (parentList.size() == 0) {
	        return null;
	    }
	    
	    // 把子菜单的子菜单再循环一遍
	    for (Map<String,Object> menu : parentList) {
				
			menu.put("children",getRoleMenuChild((String)menu.get("menuId"), allMenu));
	    } 
	   
	    return parentList;
	}
	
	/**
	 * 如果菜单之前已经被选中那么就选中
	 * @param menuId
	 * @param allMenu
	 * @return
	 */
	public static Object getRoleMenuChildChecked(String menuId, List<Menu> allMenu,String menuChecked) {
		
		
	    List<Map<String,Object>> childList = new ArrayList<>();
	    for (Menu menu : allMenu) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (StringUtils.isNotBlank(menu.getParentMenuId())) {
	        	
	            if (menu.getParentMenuId().equals(menuId)) {
	            	
	            	Map<String,Object> map = new HashMap<>();
	            	
	            	String id = menu.getId();
	            	
	            	if(StringUtils.isNotBlank(menuChecked) && StringUtils.isNotBlank(id) && menuChecked.indexOf(id) !=-1){
	            		
	            		map.put("checked",true);
	            	}else{
	            		map.put("checked",false);
	            	}
	            	
	            	map.put("expand",true);
		        	map.put("title",menu.getMenuName());
		        	map.put("id",menu.getId());
		        	map.put("menuId",menu.getMenuId());
	            	
	                childList.add(map);
	            }
	        }
	    }
	    
	    if (childList.size() == 0) {
	        return null;
	    }
	    
	    // 把子菜单的子菜单再循环一遍
	    for (Map<String,Object> menu : childList) {
				
			menu.put("children",getRoleMenuChildChecked((String)menu.get("menuId"), allMenu,menuChecked));
	    } 
	   
	    return childList;
	}
	
	/**
	 * 得到用户的角色菜单
	 * @param menuId
	 * @param allMenu
	 * @return
	 */
	public static Object getUserRoleMenus(String menuId, List<Menu> allMenu,String menuChecked) {
		
		
	    List<Map<String,Object>> childList = new ArrayList<>();
	    for (Menu menu : allMenu) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (StringUtils.isNotBlank(menu.getParentMenuId())) {
	        	
	            if (menu.getParentMenuId().equals(menuId)) {
	            	
	            	Map<String,Object> map = new HashMap<>();
	            	
	            	String id = menu.getId();
	            	
	            	if(StringUtils.isNotBlank(menuChecked) && StringUtils.isNotBlank(id) && menuChecked.indexOf(id) !=-1){
	            		
	            		map.put("title",menu.getMenuName());
	            		map.put("id",menu.getId());
	            		map.put("menuId",menu.getMenuId());
	            		map.put("path",menu.getPath());
	            		map.put("routerName",menu.getRouterName());
	            		map.put("routerPath",menu.getRouterPath());
	            		map.put("componentsPath",menu.getComponentsPath());
	            		map.put("iconNormalPath",menu.getIconNormalPath());
	            		map.put("iconCheckPath",menu.getIconCheckPath());
		            	
		                childList.add(map);
	            	}
	            }
	        }
	    }
	    
	    if (childList.size() == 0) {
	        return null;
	    }
	    
	    // 把子菜单的子菜单再循环一遍
	    for (Map<String,Object> menu : childList) {
				
			menu.put("children",getUserRoleMenus((String)menu.get("menuId"), allMenu,menuChecked));
	    } 
	   
	    return childList;
	}
	
}
