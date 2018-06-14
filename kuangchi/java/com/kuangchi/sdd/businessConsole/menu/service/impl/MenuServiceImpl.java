package com.kuangchi.sdd.businessConsole.menu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.businessConsole.menu.dao.IMenuDao;
import com.kuangchi.sdd.businessConsole.menu.model.Menu;
import com.kuangchi.sdd.businessConsole.menu.model.MenuAttribute;
import com.kuangchi.sdd.businessConsole.menu.service.IMenuService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

@Transactional
@Service("menuServiceImpl")
public class MenuServiceImpl extends BaseServiceSupport implements IMenuService {

	@Resource(name = "menuDaoImpl")
	private IMenuDao menuDao;

	public List<Tree> getUserMenu(String userId, String roleId, String menuPId,
			String cdcc, String cdFlag) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("roleId", roleId);
		params.put("menuPId", menuPId);

		int cdccInt;

		try {
			cdccInt = Integer.parseInt(cdcc);
		} catch (Exception e) {
			cdccInt = 1;
		}
		params.put("cdcc", cdccInt);

		params.put("cdFlag", cdFlag);
		List<Menu> menus = null;
		if (cdFlag != null) {
			menus = menuDao.getUserMenu(params);
		} else {
			menus = menuDao.getUserMenuSon(params);
		}

		List<Tree> treeMenu = new ArrayList<Tree>();
		for (Menu menu : menus) {
			Tree temp = new Tree();
			temp.setId(menu.getCdDM());
			temp.setChecked(false);
			temp.setIconCls("");
			temp.setPid(menu.getFcdDm());
			temp.setState(menuHas(menu.getLx()));// 是否含有子节点
			temp.setText(menu.getCdMc());
			if ("2".equals(menu.getLx())) {
				temp.setIconCls("icon-folder_go");
			} else {
				temp.setIconCls("icon-book_next");
			}
			MenuAttribute attribute = new MenuAttribute();
			attribute.setUrl(menu.getCdUrl());

			attribute.setCdcc(menu.getCdcc());
			attribute.setLx(menu.getLx());
			temp.setAttributes(attribute);
			treeMenu.add(temp);
		}
		return treeMenu;

	}

	private String menuHas(String lx) {
		if (lx.equals("1")) {
			return "open";
		} else {
			return "closed";
		}
	}

	private String menuHasChildren(String userId, String menuPId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("menuPId", menuPId);
		int count = menuDao.menuHasChildren(params);
		if (count > 0) {
			return "closed";
		} else {
			return "open";
		}
	}

	public List<Menu> getSystemMenu(String pid) {
		List<Menu> sysmenus = menuDao.getSystemMenu(pid);
		for (Menu m : sysmenus) {
			m.setState(menuHas(m.getLx()));
		}
		return sysmenus;
	}

	private String sysMenuHasChildren(String menuPId) {

		int count = menuDao.sysMenuHasChildren(menuPId);
		if (count > 0) {
			return "closed";
		} else {
			return "open";
		}
	}

	public void addNewMenu(Menu menu) {
		// 获取新增菜单显示排序
		String xspx = "";
		if (0 == menu.getXspx()) {
			xspx = menuDao.getMaxChildrenXspx(menu.getFcdDm());
			if (null == xspx) {
				xspx = "1";
			} else {
				xspx = (Integer.parseInt(xspx) + 1) + "";
			}
			menu.setXspx(Integer.parseInt(xspx));
		}

		menu.setUUID(UUIDUtil.uuidStr());
		menu.setCdDM(UUIDUtil.uuidStr());

		menu.setZfBj(GlobalConstant.ZF_BJ_N);
		menu.setLrSj(DateUtil.getSysTimestamp());
		menuDao.addNewMenu(menu);

	}

	@Override
	public Tree getAllMenu(String rootid, Map map) {
		// 获取所有菜单
		List<Menu> allMenus = menuDao.getAllMenu(map);
		Tree rootTree = new Tree();
		initTree(rootTree, allMenus, rootid);
		return rootTree;
	}
	
//***************快捷菜单使用的树 -开始- chudan.guo*********************************
	@Override
	public Tree getMenuByFlag(String rootId, String CDFlag, String userNum,String jsdm) {
		List<Menu> getMenuByFlag = menuDao.getMenuByFlag(CDFlag, userNum,jsdm);
		Tree rootTree = new Tree();
		initTreeStart(rootTree, getMenuByFlag, rootId,CDFlag);
		return rootTree;
	}
	
	@Override
	public Tree getMenuByFlagA(String rootId, String CDFlag, String userNum,String jsdm) {
		List<Menu> getMenuByFlag = menuDao.getMenuByFlagA(CDFlag, userNum,jsdm);
		Tree rootTree = new Tree();
		initTreeStart(rootTree, getMenuByFlag, rootId,CDFlag);
		return rootTree;
	}

	private void initTreeStart(Tree rootTree, List<Menu> allMenus, String selfCode,String CDFlag) {
		Iterator<Menu> menuIterator = allMenus.iterator();
		while (menuIterator.hasNext()) {
			Menu tmp = menuIterator.next();
			// 初始化自身节点
			if (tmp.getCdDM().equals(selfCode)) {
				menuToTreeStart(tmp, rootTree,CDFlag);
			} else if (tmp.getFcdDm().equals(selfCode)) {
				// 初始化子节点
				Tree children = new Tree();
				menuToTreeStart(tmp, children,CDFlag);
				rootTree.getChildren().add(children);
				// 递归
				initTreeStart(children, allMenus, tmp.getCdDM(),CDFlag);
			}
		}
	}
	
	private void menuToTreeStart(Menu menu, Tree tree,String CDFlag) {
		tree.setId(menu.getCdDM());
		tree.setPid(menu.getFcdDm());
		tree.setIconCls("");
		tree.setText(menu.getCdMc());
		tree.setState("open");
		if ("100".equals(menu.getCdDM())) {
			tree.setIconCls("icon-menu_root");
			switch(Integer.valueOf(CDFlag)){
			case 2:tree.setText("门禁系统");break;
			case 3:tree.setText("考勤系统");break;
			case 4:tree.setText("梯控系统");break;
			case 5:tree.setText("消费系统");break;
			case 6:tree.setText("访客系统");break;
			case 7:tree.setText("光子锁");break;
			}
		} else if ("100".equals(menu.getFcdDm())) {
			tree.setIconCls("icon-catalog");
		} else {
			tree.setIconCls("icon-menu");
		}
		
		MenuAttribute attribute = new MenuAttribute();
		attribute.setLx(menu.getLx());
		attribute.setCdcc(menu.getCdcc());
		tree.setAttributes(attribute);
	}
//***************快捷菜单使用的树 -结束- chudan.guo*********************************
	
	private void initTree(Tree rootTree, List<Menu> allMenus, String selfCode) {
		Iterator<Menu> menuIterator = allMenus.iterator();
		while (menuIterator.hasNext()) {
			Menu tmp = menuIterator.next();
			// 初始化自身节点
			if (tmp.getCdDM().equals(selfCode)) {
				menuToTree(tmp, rootTree);

			} else if (tmp.getFcdDm().equals(selfCode)) {
				// 初始化子节点
				Tree children = new Tree();
				menuToTree(tmp, children);
				rootTree.getChildren().add(children);

				// 递归
				initTree(children, allMenus, tmp.getCdDM());
			}
		}
	}

	private void menuToTree(Menu menu, Tree tree) {
		tree.setId(menu.getCdDM());
		tree.setPid(menu.getFcdDm());
		tree.setIconCls("");
		tree.setText(menu.getCdMc());
		tree.setState("open");
		if ("100".equals(menu.getCdDM())) {
			tree.setIconCls("icon-menu_root");
		} else if ("100".equals(menu.getFcdDm())) {
			tree.setIconCls("icon-catalog");
		} else {
			tree.setIconCls("icon-menu");
		}
		MenuAttribute attribute = new MenuAttribute();
		attribute.setLx(menu.getLx());
		attribute.setCdcc(menu.getCdcc());
		tree.setAttributes(attribute);
	}

	@Override
	public Menu getMenuDetail(String cmDm) {

		return menuDao.getMenuDetail(cmDm);
	}

	@Override
	public void modifyMenu(Menu menu) {
		menuDao.modifyMenu(menu);

	}

	@Override
	public void deleteMenu(String menuId) {
		menuDao.deleteRoleMenu(menuId);// 删除角色关联菜单
		menuDao.deleteAdditionMenu(menuId);// 删除用户关联额外菜单

		menuDao.deleteMenu(menuId);// 删除菜单

	}

	@Override
	public Boolean getFatherDM(String menuId) {
		if (menuDao.getFatherDM(menuId) > 0) {
			return true;
		} else {
			return false;
		}
	}

}
