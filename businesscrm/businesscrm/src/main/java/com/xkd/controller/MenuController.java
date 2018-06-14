package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.Menu;
import com.xkd.model.MenuTreeNode;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;

import com.xkd.service.MenuService;
import com.xkd.service.UserService;
import com.xkd.utils.MenuUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description = "菜单相关接口")
@Controller
@RequestMapping("/menu")
@Transactional
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;



    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年7月26日
     * @功能描述:根据id查询菜单信息
     */
    @ApiOperation(value = "查询菜单详情")
    @ResponseBody
    @RequestMapping(value = "/selectMenuById",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectMenuById(HttpServletRequest req, HttpServletResponse rsp ,
                                           @ApiParam(value = "菜单Id",required = true)@RequestParam(required = true) String id) {



        if (StringUtils.isBlank(id)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        Menu menu = null;

        try {

            menu = menuService.selectMenuById(id);


            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date createDate = menu.getCreateDate();
            Date updateDate = menu.getUpdateDate();

            if (createDate != null) {

                String createDateStr = sf.format(createDate);

                menu.setCreateDateStr(createDateStr);
            }

            if (updateDate != null) {

                String updateDateStr = sf.format(updateDate);

                menu.setUpdateDateStr(updateDateStr);
            }


            String createBy = menu.getCreateBy();
            String updateBy = menu.getUpdateBy();

            Map<String, Object> mmap = null;

            if (StringUtils.isNotBlank(createBy)) {

                mmap = userService.selectUserById(createBy);

                if (mmap != null) {

                    String name = (String) mmap.get("name");
                    menu.setCreateByName(name);
                }
            }

            if (StringUtils.isNotBlank(updateBy)) {

                mmap = userService.selectUserById(updateBy);

                if (mmap != null) {

                    String name = (String) mmap.get("name");
                    menu.setUpdateByName(name);
                }
            }

        } catch (Exception e) {

            System.out.println(e);
            return ResponseConstants.FUNC_SERVERERROR;
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(menu);

        return responseDbCenter;
    }


    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年7月25日
     * @功能描述:查询所有的菜单
     */
    @ApiOperation(value = "检索菜单")
    @ResponseBody
    @RequestMapping(value = "/selectAllMenus",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectAllMenus(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "菜单名",required = false)  @RequestParam(required = false) String menuName,
                                           @ApiParam(value = "菜单级别",required = false)  @RequestParam(required = false) String menuGrade,
                                           @ApiParam(value = "当前页",required = true)@RequestParam(required = true)String currentPage,
                                           @ApiParam(value = "每页多少条数",required = true)@RequestParam(required = true)String pageSize) {




        if (StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        int pageSizeInt = Integer.parseInt(pageSize);

        int currentPageInt = (Integer.parseInt(currentPage) - 1) * pageSizeInt;


        List<Menu> menus = null;
        Integer total = null;

        String menuNameSql = "";
        if (StringUtils.isNotBlank(menuName)) {

            menuNameSql = " and menuName like '%" + menuName + "%' ";
        }

        String menuGradeSql = "";
        if ("1".equals(menuGrade)) {

            menuGradeSql = " and char_length(menuId) = 3 ";

			/*
			 * char_length(menuId)
			 */
        } else if ("2".equals(menuGrade)) {

            menuGradeSql = " and char_length(menuId) = 5 ";

        } else if ("3".equals(menuGrade)) {

            menuGradeSql = " and char_length(menuId) = 7 ";

        } else if ("4".equals(menuGrade)) {

            menuGradeSql = " and char_length(menuId) = 9 ";
        }


        try {

            menus = menuService.selectMenus(menuNameSql, menuGradeSql, pageSizeInt, currentPageInt);

            total = menuService.selectMenusCount(menuNameSql, menuGradeSql);

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (Menu menu : menus) {

                String menuId = menu.getMenuId();

                if (StringUtils.isNotBlank(menuId)) {

                    if (menuId.length() == 3) {

                        menu.setMenuGrade(1);

                    } else if (menuId.length() == 5) {

                        menu.setMenuGrade(2);

                    } else if (menuId.length() == 7) {

                        menu.setMenuGrade(3);

                    } else if (menuId.length() == 9) {

                        menu.setMenuGrade(4);
                    }

                }

                Date createDate = menu.getCreateDate();
                Date updateDate = menu.getUpdateDate();

                if (createDate != null) {

                    String createDateStr = sf.format(createDate);

                    menu.setCreateDateStr(createDateStr);
                }

                if (updateDate != null) {

                    String updateDateStr = sf.format(updateDate);

                    menu.setUpdateDateStr(updateDateStr);
                }


                String createBy = menu.getCreateBy();
                String updateBy = menu.getUpdateBy();

                Map<String, Object> mmap = null;

                if (StringUtils.isNotBlank(createBy)) {

                    mmap = userService.selectUserById(createBy);

                    if (mmap != null) {

                        String name = (String) mmap.get("uname");
                        menu.setCreateByName(name);
                    }
                }

                if (StringUtils.isNotBlank(updateBy)) {

                    mmap = userService.selectUserById(updateBy);

                    if (mmap != null) {

                        String name = (String) mmap.get("uname");
                        menu.setUpdateByName(name);
                    }
                }
            }

        } catch (Exception e) {

            log.error("异常栈:",e);
            return ResponseConstants.FUNC_SERVERERROR;
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(menus);

        if (total != null) {

            responseDbCenter.setTotalRows(total.toString());

        } else {

            responseDbCenter.setTotalRows("0");
        }

        return responseDbCenter;
    }


    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年7月24日
     * @功能描述:查询所有菜单
     */
    @ApiOperation(value = "查询所有菜单列表")
    @ResponseBody
    @RequestMapping(value = "/selectMenus",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectMenus(HttpServletRequest req, HttpServletResponse rsp) {


        List<Menu> menus = null;

        List<Map<String, Object>> menuList = null;

        try {

            // 先找到所有的一级菜单
            menus = menuService.selectMenus("", "", 10000, 0);

            menuList = new ArrayList<Map<String, Object>>();

            for (Menu menu : menus) {
                // 一级菜单没有parentId
                if (StringUtils.isBlank(menu.getParentMenuId()) || "0".equals(menu.getParentMenuId())) {

                    Map<String, Object> map = new HashMap<>();

                    map.put("value", menu.getMenuId());
                    map.put("label", menu.getMenuName());

                    menuList.add(map);
                }
            }
			    
		    /*
		     * 循环一级目录
		     */
            for (Map<String, Object> menu : menuList) {

                menu.put("children", MenuUtil.getChild((String) menu.get("value"), menus));
            }

        } catch (Exception e) {

            System.out.println(e);
            return ResponseConstants.FUNC_SERVERERROR;
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(menuList);

        return responseDbCenter;
    }

    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年7月24日
     * @功能描述:删除菜单
     */
    @ApiOperation(value = "批量删除菜单")
    @ResponseBody
    @RequestMapping(value = "/deleteMenusByIds",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter deleteMenusByIds(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "ids 多个Id以逗号隔开 如'1','2'",required = true)  @RequestParam(required = true) String ids) throws Exception {



        if (StringUtils.isBlank(ids)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        String[] iids = ids.split(",");

        try {

            String idsString = "";
            for (int i = 0; i < iids.length; i++) {

                idsString += "'" + iids[i] + "',";
            }

            idsString = "id in (" + idsString.substring(0, idsString.length() - 1) + ")";

            List<Menu> menus = menuService.selectMenuByIds(idsString);

            for (Menu menu : menus) {

                List<Menu> menuss = menuService.selectMenusByParentMenuId(menu.getMenuId());

                if (menuss != null && menuss.size() > 0) {

                    return ResponseConstants.CHILDREN_ROLE_EXISTS_ERROR;
                }

            }


            menuService.deleteMenusByIds(idsString);

        } catch (Exception e) {

            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }

//    /**
//     * @param req
//     * @param rsp
//     * @return
//     * @author: xiaoz
//     * @2017年7月27日
//     * @功能描述:查询角色菜单
//     */
//    @ApiOperation(value = "获取角色对应的菜单")
//    @ResponseBody
//    @RequestMapping("/selectRoleMenus")
//    public ResponseDbCenter selectRoleMenus(HttpServletRequest req, HttpServletResponse rsp) {
//
//
//        List<Menu> menus = null;
//
//        List<Map<String, Object>> menuList = null;
//
//        try {
//
//            // 先找到所有的菜单
//            menus = menuService.selectMenus("", "", 10000, 0);
//
//            menuList = new ArrayList<Map<String, Object>>();
//
//            for (Menu menu : menus) {
//                // 一级菜单没有parentId
//                if (StringUtils.isBlank(menu.getParentMenuId()) || "0".equals(menu.getParentMenuId())) {
//
//                    Map<String, Object> map = new HashMap<>();
//
//                    map.put("expand", true);
//                    map.put("title", menu.getMenuName());
//                    map.put("id", menu.getId());
//                    map.put("menuId", menu.getMenuId());
//                    map.put("checked", false);
//
//                    menuList.add(map);
//                }
//            }
//
//		    /*
//		     * 循环一级目录
//		     */
//            for (Map<String, Object> menu : menuList) {
//
//                menu.put("children", MenuUtil.getRoleMenuChild((String) menu.get("menuId"), menus));
//            }
//
//        } catch (Exception e) {
//
//            System.out.println(e);
//            return ResponseConstants.FUNC_SERVERERROR;
//        }
//
//        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
//        responseDbCenter.setResModel(menuList);
//
//        return responseDbCenter;
//    }


    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年7月24日
     * @功能描述:添加菜单
     */
    @ApiOperation(value = "保存菜单")
    @ResponseBody
    @RequestMapping(value = "/saveMenu", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter saveMenu(HttpServletRequest req, HttpServletResponse rsp,String flag,String id, String menuId,String menuName,String menuLevel,String path
    ,String content,String updateBy,String createBy,String parentChain,String routerName,String routerPath,String componentsPath,String iconNormalPath, String iconCheckPath) throws Exception {


        if (StringUtils.isBlank(flag) || StringUtils.isBlank(menuName) || ("update".equals(flag) && StringUtils.isBlank(id))) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        String[] parentChainStrs=parentChain.split(",");
        menuId=parentChainStrs[parentChainStrs.length-1];


        try {

            if ("update".equals(flag)) {

                String maxMenuId = "";

                Map<String, Object> paramMap = new HashMap<>();

                Menu menuExists = menuService.selectMenuById(id);

                if (StringUtils.isBlank(menuId)) {

                    paramMap.put("parentMenuId", "0");
                    //如果父菜单没有改变，那么menuId不变
                    if ("0".equals(menuExists.getParentMenuId())) {

                        maxMenuId = menuExists.getMenuId();

                    } else {

                        maxMenuId = menuService.selectMaxMenuIdByParentId("0");

                        if (StringUtils.isNotBlank(maxMenuId)) {

                            maxMenuId = new Integer(maxMenuId).intValue() + 1 + "";

                        } else {

                            maxMenuId = "100";
                        }
                    }

                } else {

                    paramMap.put("parentMenuId", menuId);

                    //如果父菜单没有改变，那么menuId不变
                    if (menuId.equals(menuExists.getParentMenuId())) {

                        maxMenuId = menuExists.getMenuId();

                    } else {

                        maxMenuId = menuService.selectMaxMenuIdByParentId(menuId);

                        if (StringUtils.isNotBlank(maxMenuId)) {

                            maxMenuId = new Integer(maxMenuId).intValue() + 1 + "";

                        } else {

                            maxMenuId = menuId + "01";
                        }
                    }

                }

                paramMap.put("id", id);
                paramMap.put("menuName", menuName);
                paramMap.put("menuLevel", menuLevel);
                paramMap.put("path", path);
                paramMap.put("content", content);
                paramMap.put("updateBy", updateBy);
                paramMap.put("menuId", maxMenuId);
                paramMap.put("parentChain", parentChain);

                paramMap.put("routerName", routerName);
                paramMap.put("routerPath", routerPath);
                paramMap.put("componentsPath", componentsPath);
                paramMap.put("iconNormalPath", iconNormalPath);
                paramMap.put("iconCheckPath", iconCheckPath);


                if (maxMenuId.equals(menuId)) {

                    return ResponseConstants.ILLEGAL_PARAM;
                }

                menuService.updateMenuBy(paramMap);

            } else {




                //添加

                String maxMenuId = "";

                Map<String, Object> paramMap = new HashMap<>();

                if (StringUtils.isBlank(menuId)) {

                    paramMap.put("parentMenuId", "0");

                    maxMenuId = menuService.selectMaxMenuIdByParentId("0");

                    if (StringUtils.isNotBlank(maxMenuId)) {

                        maxMenuId = new Integer(maxMenuId).intValue() + 1 + "";

                    } else {

                        maxMenuId = "100";
                    }

                } else {

                    paramMap.put("parentMenuId", menuId);

                    maxMenuId = menuService.selectMaxMenuIdByParentId(menuId);

                    if (StringUtils.isNotBlank(maxMenuId)) {

                        maxMenuId = new Integer(maxMenuId).intValue() + 1 + "";

                    } else {

                        maxMenuId = menuId + "01";
                    }
                }


                //父菜单和子菜单不能一样，要不然会出现死循环
                if (maxMenuId.equals(menuId)) {

                    return ResponseConstants.ILLEGAL_PARAM;
                }

                String mId = UUID.randomUUID().toString();

                paramMap.put("id", mId);
                paramMap.put("menuName", menuName);
                paramMap.put("menuLevel", menuLevel);
                paramMap.put("path", path);
                paramMap.put("imagePath", iconNormalPath);
                paramMap.put("content", content);
                paramMap.put("createBy", createBy);

                paramMap.put("menuId", maxMenuId);
                paramMap.put("parentChain", parentChain);

                paramMap.put("routerName", routerName);
                paramMap.put("routerPath", routerPath);
                paramMap.put("componentsPath", componentsPath);
                paramMap.put("iconNormalPath", iconNormalPath);
                paramMap.put("iconCheckPath", iconCheckPath);

                menuService.saveMenu(paramMap);

            }

        } catch (Exception e) {

            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


    /**
     * 登录用户初始化菜单功能，只显示登录用户可见的菜单
     *
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取左侧菜单树")
    @ResponseBody
    @RequestMapping(value = "/initMenu", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter selectMenuByUserId(HttpServletRequest req, HttpServletResponse rsp
    )
            throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            List<MenuTreeNode> menuTreeNodes  = menuService.getMyMenu(loginUserId);
            map.put("menus", menuTreeNodes);
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(map);
        return responseDbCenter;
    }


    /**
     * 查询所有叶节点菜单列表
     *
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取所有叶结点菜单")
    @ResponseBody
    @RequestMapping(value = "/listLeafMenu", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter listLeafMenu(HttpServletRequest req, HttpServletResponse rsp
    )
            throws Exception {
        List<Map<String, Object>> leafList = null;
        try {
            leafList = menuService.selectAllLeafMenu();
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(leafList);
        return responseDbCenter;
    }

}
