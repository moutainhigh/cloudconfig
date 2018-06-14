package com.kuangchi.sdd.businessConsole.department.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.dao.IDepartmentDao;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentNode;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.dao.EmployeeDao;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeeTreeNode;
import com.kuangchi.sdd.consumeConsole.fundPool.model.JsonTreeResult;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.PeopleAuthorityInfoDao;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

@Transactional
@Service("departmentServiceImpl")
public class DepartmentServiceImpl extends BaseServiceSupport implements IDepartmentService {
	
	@Resource(name="peopleAuthorityDao")	
	PeopleAuthorityInfoDao peopleAuthorityDao;

    @Resource(name = "departmentDaoImpl")
    private IDepartmentDao departmentDao;
    @Resource(name="employeeDao")
    private EmployeeDao employeeDao;

    @Resource(name = "LogDaoImpl")
	private LogDao logDao;
    @Override
    public List<Department> getSystemDepartment(String pid) {
        List<Department> depAll = departmentDao. getSystemDepartment(pid);
        //是否有子部门
        for (Department dep:depAll) {

            dep.setState(isHasChileDep(dep.getBmDm()));
        }
        return depAll;
    }

    @Override
    public void addDepartment(Department dep,String login_user) {
        dep.setUUID(UUIDUtil.uuidStr());
        dep.setZfBj(GlobalConstant.ZF_BJ_N);
        departmentDao.addDepartment(dep);
        Map<String, String> log = new HashMap<String, String>();
    	log.put("V_OP_NAME", "部门管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "新增部门信息");
		log.put("V_OP_TYPE", "业务");
		logDao.addLog(log);
    }

    /**
     * 是否有子部门
     * @param depId
     * @return
     */
    private String isHasChileDep(String depId){
        int count = departmentDao.departmentHasChildren(depId);
        if(count > 0){
            return "closed";
        }else{
            return "open";
        }
    }

    @Override
    public Tree getDepartmentTree(String departmentRoot, String layerDeptNum) {
        //获取所有菜单
        List<Department> allDeparts = departmentDao.getAllDepart(layerDeptNum);
        List<EmployeeTreeNode> allEmployees=employeeDao.getAllEmployeeTreeNode();
        List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
        for (Department d:allDeparts){
            DepartmentNode departmentNode=new DepartmentNode();
            departmentNode.setUUID(d.getUUID());
            departmentNode.setBmDm(d.getBmDm());
            departmentNode.setSjbmDm(d.getSjbmDm());
            departmentNode.setMc(d.getBmMc());
            allNodes.add(departmentNode);
        }

        for (EmployeeTreeNode e:allEmployees){
            DepartmentNode departmentNode=new DepartmentNode();
            departmentNode.setUUID(e.getUUID());
            departmentNode.setMc(e.getYhMc());
            departmentNode.setBmDm(e.getYhDm());
            departmentNode.setSjbmDm(e.getBmDm());
            departmentNode.setIsDepartment(1);
            departmentNode.setXb(e.getXb());
            allNodes.add(departmentNode);
        }

        Tree rootTree = new Tree();
        initTree(rootTree,allNodes,departmentRoot);
//        initTreeA(rootTree,allNodes,departmentRoot);
        //如果通过id获取的某一个节点与其本身是相同的，则说明其没有子节点，返回null
        if (!departmentRoot.equals(GlobalConstant.DEPARTMENT_ROOT_BM_DM)&&rootTree.getId().equals(departmentRoot)){
         return  null;
        }
        return rootTree;
    }

    @Override
    public Tree getLazyDepartmentTree(String departmentRoot, String layerDeptNum) {
        //获取所有菜单
        List<Department> allDeparts = departmentDao.getDepartByParentBmDm(departmentRoot, layerDeptNum);
       // List<EmployeeTreeNode> allEmployees=employeeDao.getEmployeeTreeNodeByBmDm(departmentRoot);
        List<EmployeeTreeNode> allEmployees=new ArrayList<EmployeeTreeNode>();
        List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
        for (Department d:allDeparts){
            DepartmentNode departmentNode=new DepartmentNode();
            departmentNode.setUUID(d.getUUID());
            departmentNode.setBmDm(d.getBmDm());
            departmentNode.setSjbmDm(d.getSjbmDm());
            departmentNode.setMc(d.getBmMc());
            allNodes.add(departmentNode);
        }

        for (EmployeeTreeNode e:allEmployees){
            DepartmentNode departmentNode=new DepartmentNode();
            departmentNode.setUUID(e.getUUID());
            departmentNode.setMc(e.getYhMc());
            departmentNode.setBmDm(e.getYhDm());
            departmentNode.setSjbmDm(e.getBmDm());
            departmentNode.setIsDepartment(1);
            departmentNode.setXb(e.getXb());
            allNodes.add(departmentNode);
        }

        
        Department root=departmentDao.getDepartByBmDm(departmentRoot);
        DepartmentNode rootNode=new DepartmentNode();
        rootNode.setUUID(root.getUUID());
        rootNode.setBmDm(root.getBmDm());
        rootNode.setSjbmDm(root.getSjbmDm());
        rootNode.setMc(root.getBmMc());
        
        Tree rootTree = new Tree();
        departToTree(rootNode, rootTree);
        initTree(rootTree,allNodes,departmentRoot);
//        initTreeA(rootTree,allNodes,departmentRoot);
        //如果通过id获取的某一个节点与其本身是相同的，则说明其没有子节点，返回null

        
        if (allEmployees.size()==0&&allDeparts.size()==0) {
			return null;
		}
        return rootTree;
    }
   
    
      
    @Override
    public List<Tree> getDepartmentTreeList(String departmentRoot, String layerDeptNum) {
        //获取所有菜单
        List<Department> allDeparts = departmentDao.getDepartByParentBmDm(departmentRoot, layerDeptNum);
        List<EmployeeTreeNode> allEmployees=employeeDao.getEmployeeTreeNodeByBmDm(departmentRoot);
        List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
        for (Department d:allDeparts){
            DepartmentNode departmentNode=new DepartmentNode();
            departmentNode.setUUID(d.getUUID());
            departmentNode.setBmDm(d.getBmDm());
            departmentNode.setSjbmDm(d.getSjbmDm());
            departmentNode.setMc(d.getBmMc());
            allNodes.add(departmentNode);
        }

        for (EmployeeTreeNode e:allEmployees){
            DepartmentNode departmentNode=new DepartmentNode();
            departmentNode.setUUID(e.getUUID());
            departmentNode.setMc(e.getYhMc());
            departmentNode.setBmDm(e.getYhDm());
            departmentNode.setSjbmDm(e.getBmDm());
            departmentNode.setIsDepartment(1);
            departmentNode.setXb(e.getXb());
            allNodes.add(departmentNode);
        }

        List<Tree> treeList=new ArrayList<Tree>();
         for (int i = 0; i < allNodes.size(); i++) {
        	 Tree tree=new Tree();
             departToTree(allNodes.get(i), tree);
             treeList.add(tree);
		}
        

        return treeList;
    }
   
    @Override
    public List<Tree> getDepartmentCardTreeList(String departmentRoot, String layerDeptNum) {
    	//获取所有菜单
    	List<Department> allDeparts = departmentDao.getDepartByParentBmDm(departmentRoot, layerDeptNum);
    	List<EmployeeTreeNode> allEmployees=employeeDao.getEmployeeTreeNodeByBmDm(departmentRoot);
    	Map m=new HashMap();
    	m.put("empList",allEmployees);
    	List<Map>  allOrdinCards=peopleAuthorityDao.getStfOrdinCardB(m);
    	List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
    	for (Department d:allDeparts){
    		DepartmentNode departmentNode=new DepartmentNode();
    		departmentNode.setUUID(d.getUUID());
    		departmentNode.setBmDm(d.getBmDm());
    		departmentNode.setSjbmDm(d.getSjbmDm());
    		departmentNode.setMc(d.getBmMc());
    		allNodes.add(departmentNode);
    	}
    	
    	for (EmployeeTreeNode e:allEmployees){
    		DepartmentNode departmentNode=new DepartmentNode();
    		departmentNode.setUUID(e.getUUID());
    		departmentNode.setMc(e.getYhMc());
    		departmentNode.setBmDm(e.getYhDm());
    		departmentNode.setSjbmDm(e.getBmDm());
    		departmentNode.setIsDepartment(1);
    		departmentNode.setXb(e.getXb());
    		allNodes.add(departmentNode);
    	}
    	List<Tree> treeList=new ArrayList<Tree>();
    	for (int i = 0; i < allNodes.size(); i++) {
    		Tree tree=new Tree();
    		departToTreeA(allNodes.get(i), tree);
    		treeList.add(tree);
    	}
     return treeList;
    }
    
    @Override
    public List<Tree> getDepartmentCardTreeListA(String staffNum) {
    	List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
    	List<Map>  allOrdinCards=peopleAuthorityDao.getStfOrdinCardA(staffNum);
		for(Map map:allOrdinCards){
			DepartmentNode depNode=new DepartmentNode();
			depNode.setUUID((String)map.get("cardNum"));
			depNode.setMc((String)map.get("cardNum"));
			depNode.setBmDm((String)map.get("cardNum"));
			depNode.setSjbmDm((String)map.get("staffNum"));
			depNode.setIsDepartment(5);//5代表卡
			//depNode.setMc((String)map.get("cardNum"));
			allNodes.add(depNode);
		}
    	List<Tree> treeList=new ArrayList<Tree>();
    	for (int i = 0; i < allNodes.size(); i++) {
    		Tree tree=new Tree();
    		departToTreeA(allNodes.get(i), tree);
    		treeList.add(tree);
    	}
    	return treeList;
    }
    
   


    @Override
    public Tree getOnlyDepartmentTree(String departmentRoot,String layerDeptNum) {
        //获取所有菜单
        List<Department> allDeparts = departmentDao.getAllDepart(layerDeptNum);
        List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
        for (Department d:allDeparts){
            DepartmentNode departmentNode=new DepartmentNode();
            departmentNode.setUUID(d.getUUID());
            departmentNode.setBmDm(d.getBmDm());
            departmentNode.setSjbmDm(d.getSjbmDm());
            departmentNode.setMc(d.getBmMc());
            allNodes.add(departmentNode);
        }
        Tree rootTree = new Tree();
        initTree(rootTree,allNodes,departmentRoot);
//        initTreeA(rootTree,allNodes,departmentRoot);
        //如果通过id获取的某一个节点与其本身是相同的，则说明其没有子节点，返回null
        if (!departmentRoot.equals(GlobalConstant.DEPARTMENT_ROOT_BM_DM)&&rootTree.getId().equals(departmentRoot)){
            return  null;
        }
        return rootTree;
    }
    
    @Override
    public Tree getOnlyDepartmentTreeLazy(String departmentRoot,String layerDeptNum) {
    	//获取所有菜单
//        List<Department> allDeparts = departmentDao.getAllDepart(layerDeptNum);
    	List<Department> allDeparts = departmentDao.getDepartByParentBmDm(departmentRoot, layerDeptNum);	
    	List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
    	for (Department d:allDeparts){
    		DepartmentNode departmentNode=new DepartmentNode();
    		departmentNode.setUUID(d.getUUID());
    		departmentNode.setBmDm(d.getBmDm());
    		departmentNode.setSjbmDm(d.getSjbmDm());
    		departmentNode.setMc(d.getBmMc());
    		allNodes.add(departmentNode);
    	}
    	Tree rootTree = new Tree();
    	initTree(rootTree,allNodes,departmentRoot);
//        initTreeA(rootTree,allNodes,departmentRoot);
    	//如果通过id获取的某一个节点与其本身是相同的，则说明其没有子节点，返回null
    	if (!departmentRoot.equals(GlobalConstant.DEPARTMENT_ROOT_BM_DM)&&rootTree.getId().equals(departmentRoot)){
    		return  null;
    	}
    	return rootTree;
    }
    
    @Override
    public List<Tree> getOnlyDepartmentTreeList(String departmentRoot,String layerDeptNum) {
    	//获取所有菜单
    	List<Department> allDeparts = departmentDao.getDepartByParentBmDm(departmentRoot, layerDeptNum);	
    	List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
    	for (Department d:allDeparts){
    		DepartmentNode departmentNode=new DepartmentNode();
    		departmentNode.setUUID(d.getUUID());
    		departmentNode.setBmDm(d.getBmDm());
    		departmentNode.setSjbmDm(d.getSjbmDm());
    		departmentNode.setMc(d.getBmMc());
    		allNodes.add(departmentNode);
    	}
        List<Tree> treeList=new ArrayList<Tree>();
        for (int i = 0; i < allNodes.size(); i++) {
       	 Tree tree=new Tree();
            departToTree(allNodes.get(i), tree);
            treeList.add(tree);
		}
       return treeList;
    }



    /**
     * 递归所有部门生成部门tree
     * @param rootTree 生成的tree 即根
     * @param allDeparts 所有部门
     * @param selfCode 部门根代码
     */
/*    private void initTree(Tree rootTree, List<DepartmentNode> allDeparts,
                          String selfCode) {
        Iterator<DepartmentNode> menuIterator = allDeparts.iterator();
        while (menuIterator.hasNext()) {
            DepartmentNode tmp = menuIterator.next();
            // 初始化自身节点
            if (tmp.getBmDm().equals(selfCode)&&tmp.getIsDepartment()==0) {
                departToTree(tmp, rootTree);

            } else if (tmp.getSjbmDm().equals(selfCode)) {//tmp.getSjbmDm().equals(selfCode)
                // 初始化子节点
            	Tree children = new Tree();
            	departToTree(tmp, children);
            	rootTree.getChildren().add(children);
            	if(!("1".equals(tmp.getUUID())&&tmp.getIsDepartment()==5 || "2".equals(tmp.getUUID())&&tmp.getIsDepartment()==5)){
            		// 递归  这里需要排除 卡号为 1 ，2的两张卡，不然会引起死循环，原因：与部门编号1，2重叠了 by gengji.yang
            		initTree(children, allDeparts, tmp.getBmDm());
            	}
            }
        }
    }*/
    
    private void initTree(Tree rootTree, List<DepartmentNode> allDeparts,
    		String selfCode) {
    	Iterator<DepartmentNode> menuIterator = allDeparts.iterator();
    	while (menuIterator.hasNext()) {
    		DepartmentNode tmp = menuIterator.next();
    		// 初始化自身节点
    		if (tmp.getBmDm().equals(selfCode)&&tmp.getIsDepartment()==0) {
    			departToTree(tmp, rootTree);
    			
    		} else if (tmp.getSjbmDm().equals(selfCode)) {//tmp.getSjbmDm().equals(selfCode)
    			// 初始化子节点   找孩子
    			Tree children = new Tree();
    			departToTree(tmp, children);
    			rootTree.getChildren().add(children);
    			if(tmp.getIsDepartment()!=5){//如果tmp是卡，则停止递归
    				initTree(children, allDeparts, tmp.getBmDm());
    			}
    		}
    	}
    }
    
    private void initTreeAA(Tree rootTree, List<DepartmentNode> allDeparts,
    		String selfCode) {
    	Iterator<DepartmentNode> menuIterator = allDeparts.iterator();
    	while (menuIterator.hasNext()) {
    		DepartmentNode tmp = menuIterator.next();
    		// 初始化自身节点
    		if (tmp.getBmDm().equals(selfCode)&&tmp.getIsDepartment()==0) {
    			departToTreeA(tmp, rootTree);
    			
    		} else if (tmp.getSjbmDm().equals(selfCode)) {//tmp.getSjbmDm().equals(selfCode)
    			// 初始化子节点   找孩子
    			Tree children = new Tree();
    			departToTreeA(tmp, children);
    			rootTree.getChildren().add(children);
    			if(tmp.getIsDepartment()!=5){//如果tmp是卡，则停止递归
    				initTreeAA(children, allDeparts, tmp.getBmDm());
    			}
    		}
    	}
    }
    
    
    /**
     *部门对象转成tree
     * @param dep
     * @param tree
     */
    private void departToTree(DepartmentNode dep, Tree tree) {
        tree.setId(dep.getBmDm());
        tree.setPid(dep.getSjbmDm());
        tree.setText(dep.getMc());
        Map map=new HashMap();
        map.put("isDepartment",dep.getIsDepartment());
        map.put("parentId",dep.getSjbmDm());
        if("0".equals(dep.getSjbmDm())){
        	  tree.setState("open");
              tree.setIconCls("icon-enterprise");
        }
        else if (dep.getIsDepartment()==0) {
            tree.setState("closed");
            tree.setIconCls("icon-platform");
        }
        else if (dep.getIsDepartment()==5) {
            tree.setState("open");
            tree.setIconCls("icon-stafcard");
        }
        else{
        	
            tree.setState("open");
            if ("0".equals(dep.getXb())) {
                tree.setIconCls("icon-female");
			}else{
                tree.setIconCls("icon-male");
			}
        }
        tree.setAttributes(map);

    }
    
    /**
     *部门对象转成tree
     * @param dep
     * @param tree
     */
    private void departToTreeA(DepartmentNode dep, Tree tree) {
    	tree.setId(dep.getBmDm());
    	tree.setPid(dep.getSjbmDm());
    	tree.setText(dep.getMc());
    	Map map=new HashMap();
    	map.put("isDepartment",dep.getIsDepartment());
    	map.put("parentId",dep.getSjbmDm());
    	if("0".equals(dep.getSjbmDm())){
    		tree.setState("open");
    		tree.setIconCls("icon-enterprise");
    	}
    	else if (dep.getIsDepartment()==0) {
    		tree.setState("closed");
    		tree.setIconCls("icon-platform");
    	}
    	else if (dep.getIsDepartment()==5) {
    		tree.setState("open");
    		tree.setIconCls("icon-stafcard");
    	}
    	else{
    		
    		tree.setState("closed");
    		if ("0".equals(dep.getXb())) {
    			tree.setIconCls("icon-female");
    		}else{
    			tree.setIconCls("icon-male");
    		}
    	}
    	tree.setAttributes(map);
    	
    }

    @Override
    public String[] getUserDepartment(String yhDm) {

        return departmentDao.getUserDepartment(yhDm);
    }

    @Override
    public Department getDepartmentDet(String bmDm) {

        return departmentDao.getDepartmentDet(bmDm);
    }

    @Override
    public void modifyDepartment(Department dep,String login_user) {
        boolean result =  departmentDao.modifyDepartment(dep);
    	Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "部门管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "修改部门信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
    }

    @Override
    public JsonResult validDepartment(String bmNo,String bmDm) {
        JsonResult result = new JsonResult();
        result.setSuccess(true);
        String msg = null;
        //部门代码和员工代码不能重复
        int bmDmNum = departmentDao.isContainbmDM(bmNo,bmDm);
        if(bmDmNum > 0){
            result.setSuccess(false);
        msg="该代码已经被使用";
        }
        result.setMsg(msg);
        return result;
    }

    @Override
    public boolean delDepartment(String depIds) {

        	String empIdStr=getEmpIds(depIds);
        	 //这里是界面选员工的时候删除
            if (!"".equals(empIdStr)) {
            	//删除员工信息,不成功
               /* if(!employeeDao.deleteGyDm(empIdStr)){
                	return false;
                } 
                employeeDao.deleteEmployeePositionByYhDm(empIdStr);
               */
            	return false;
            }
            //删除用户关联的部门(t_xt_yh_bm)
            departmentDao.deleBmYh(depIds);
            //删除部门 (t_xt_bm)
            departmentDao.deleBm(depIds);
            //删除部门管理的岗位(t_xt_gw)
            departmentDao.deleDepGw(depIds);
            //删除管理管理的用户(t_xt_yh_gw)
            departmentDao.deleGwYh(depIds);  
            return true;
    }
    
    @Override
    public boolean delEmployee(String empIds) {
        //删除员工信息,不成功
        if(!employeeDao.deleteGyDm(empIds)){
        	return false;
        } 
        employeeDao.deleteEmployeePositionByYhDm(empIds);
        return true;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getEmpIds(String deptIds){
     
    	List<String> empDmList=employeeDao.getEmployeIds(deptIds);
    	StringBuffer sb=new StringBuffer("");
    	for (int i = 0; i < empDmList.size(); i++) {
			sb.append("'").append(empDmList.get(i)).append("'").append(",");
		}
    	if (sb.length()>1) {
		    	return sb.subSequence(0, sb.length()-1).toString();
		}
    	return "";
    }

	@Override
	public String selectBmdmByNo(String bmNo) {
		return departmentDao.selectBmdmByNo(bmNo);
	}

	@Override
	public List<Department> downWardDepartment(String bmDm, String layerDeptNum) {
		 List<Department> list=new ArrayList<Department>();
		 getChildDepartment(list, bmDm, layerDeptNum);
		 return list;
	}
	
	public void getChildDepartment(List<Department> list,String bmDm, String layerDeptNum){
		List<Department> childrenList=departmentDao.getDepartByParentBmDm(bmDm, layerDeptNum);
		if (childrenList.size()==0) {
			return;
		}else{
			list.addAll(childrenList);
		}
		for (int i = 0; i < childrenList.size(); i++) {	
			getChildDepartment(list, childrenList.get(i).getBmDm(), layerDeptNum);
		}
	}

	@Override
	public List<CardType> getCardtype() {
		return departmentDao.getCardtype();
	}

	
	public Tree getMyDeptTree(String organiztion_num) {
		Tree tree = new Tree();
		Department deptRoot = departmentDao.getDeptTreeRoot();//获取部门树根节点
		List<Department> list = departmentDao.getBandDeptByZjDm(organiztion_num);//获取资金池绑定的部门
		List<String> bmDmList=new ArrayList<String>();
		List<Department> lists = new ArrayList<Department>(); 
		Set<String> set = new HashSet<String>();
		for(Department dept:list){
			bmDmList.add(dept.getBmDm());
			set.add(dept.getBmDm());
		}
		 
		for(String str:bmDmList){
			set = isTreeTop(str,set);
		}
		
		for (String str : set) {  
			Department dept = departmentDao.getDepartByBmDm(str);
			lists.add(dept);
		}  
		
		buildTree(tree,lists,organiztion_num,deptRoot);
		return tree;
	}
	
	private Set<String> isTreeTop(String str, Set<String> set) {
		
		List<Department> sjDeptList = departmentDao.getSjBmByBmDm("'"+str+"'");//获取绑定的部门的上级部门代码
		if(!sjDeptList.isEmpty()){
			if(!"1".equals(sjDeptList.get(0).getBmDm())){
				set.add(sjDeptList.get(0).getBmDm());
			}else{
				return set;
			}
		}else{
			return set;
		}
		return isTreeTop(sjDeptList.get(0).getBmDm(),set);
	}

	
	public void buildTree(Tree t,List<Department> allMyDept,String organiztion_num,Department dRoot){
		 modelToTree(t,dRoot,organiztion_num);
		 List<Department> children = new ArrayList<Department>();
		 for(Department dept:allMyDept){
			 if(dept.getSjbmDm().equals(dRoot.getBmDm())){
				 children.add(dept);
			 }
		 }
		 
		 for(Department dpt:children){
			 Tree childTree = new Tree();
			 buildTree(childTree,allMyDept,organiztion_num,dpt);
			 t.getChildren().add(childTree);
		 }
	}
	
	public void modelToTree(Tree tree,Department dRoot,String organiztion_num){
		Map<String,String> map = new HashMap<String,String>();
		Department d = departmentDao.getDeptBySjDm(dRoot.getSjbmDm().toString());//判断是否存在上级部门
		Integer childCount = departmentDao.getChildrenBySjDm(dRoot.getBmDm().toString()).size();//获取孩子节点数量
		Integer count = departmentDao.checkSelectDeptIsBand(dRoot.getBmDm());//根据部门代码查看是否绑定资金池
		//List bmDmList = departmentDao.getBandBmByOrganiztionNum(organiztion_num);//根据选中资金池代码查找绑定的部门代码
		String organiztionNum = departmentDao.getOrganiztionNumByBmdm(dRoot.getBmDm());
		//List<Department> list = departmentDao.getBandDeptByZjDm(organiztion_num);//获取资金池绑定的部门
		if(organiztionNum==null){
			tree.setText(dRoot.getBmMc());
		}else if(null!=organiztionNum && !organiztion_num.equals(organiztionNum)){
			tree.setText(dRoot.getBmMc());
		}else{
			tree.setText(dRoot.getBmMc()+"(已绑定)");
			}
		if(dRoot.getSjbmDm().equals("0")){
			tree.setPid(null);
			tree.setIconCls("icon-enterprise");
			tree.setState("open");
			map.put("isLeaf", "0");
		}else{
			
			if(d!=null&&d.getSjbmDm().equals("0")&&childCount==0){
				tree.setIconCls("icon-department");
			}else if(childCount>0){
				tree.setIconCls("icon-platform");
			}else{
				tree.setIconCls("icon-department");
			}
			tree.setPid(dRoot.getSjbmDm().toString());
			
			if(childCount>0){  
				tree.setState("open");
				map.put("isLeaf", "0");
			}else if(d!=null&&d.getSjbmDm().equals("0")){
				tree.setState("open");
				map.put("isLeaf", "0");
			}else{
				tree.setState("close");
				map.put("isLeaf", "1");
			}
		}
		map.put("pid", dRoot.getSjbmDm().toString());
		tree.setAttributes(map);
		tree.setId(dRoot.getBmDm());
	}
	
	@Override
	public  JsonTreeResult getDeptTree(String organiztion_num, String layerDeptNum) {
		Tree tree = new Tree();
		JsonTreeResult jsonTreeResult = new JsonTreeResult();
		List<Department> list = departmentDao.getAllDepart(layerDeptNum);//获取全部部门节点
		Department deptRoot = departmentDao.getDeptTreeRoot();//获取部门树根节点
		JsonTreeResult jsonTreeResults = buildTree(tree,list,deptRoot,jsonTreeResult,organiztion_num);
		return jsonTreeResults;
	}
	
	public JsonTreeResult buildTree(Tree tree,List<Department> allDepts,Department dRoot,JsonTreeResult jsonTreeResult,String organiztion_num){
		JsonTreeResult treeResult = modelToTree(tree,dRoot,jsonTreeResult,organiztion_num);
		List<Department> child = new ArrayList<Department>();
		for(Department deptModel:allDepts){
			if(deptModel.getSjbmDm().equals(dRoot.getBmDm())){
				child.add(deptModel);
			}
		}
		for(Department deptChild:child){
			Tree t = new Tree();
			buildTree(t,allDepts,deptChild,treeResult,organiztion_num);
			tree.getChildren().add(t);
		}
		treeResult.setTree(tree);
		return treeResult;
	}
	
	public JsonTreeResult modelToTree(Tree tree,Department dRoot,JsonTreeResult jsonTreeResult,String organiztion_num ){
		Map<String,String> map = new HashMap<String,String>();
		Department d = departmentDao.getDeptBySjDm(dRoot.getSjbmDm().toString());//判断是否存在上级部门
		Integer childCount = departmentDao.getChildrenBySjDm(dRoot.getBmDm().toString()).size();//获取孩子节点数量
		if(dRoot.getSjbmDm().equals("0")){
			tree.setPid(null);
			tree.setIconCls("icon-enterprise");
			tree.setState("open");
			map.put("isLeaf", "0");
		}else{
			tree.setPid(dRoot.getSjbmDm().toString());
			if(d!=null&&d.getSjbmDm().equals("0")&&childCount==0){
				tree.setIconCls("icon-department");
			}else if(childCount>0){
				tree.setIconCls("icon-platform");
			}else{
				tree.setIconCls("icon-department");
			}
			if(childCount>0){  
				tree.setState("open");
				map.put("isLeaf", "0");
			}else if(d!=null&&d.getSjbmDm().equals("0")){
				tree.setState("open");
				map.put("isLeaf", "0");
			}else{
				tree.setState("close");
				map.put("isLeaf", "1");
			}
		}
		map.put("pid", dRoot.getSjbmDm().toString());
		tree.setAttributes(map);
		tree.setId(dRoot.getBmDm());
			List  zjcMcList = departmentDao.getOrganiztionNameByBmDm(dRoot.getBmDm());//根据部门代码查找对应的资金池名称
			Map zjcMcMap = (Map)zjcMcList.get(0);
			String organiztionName = (String) zjcMcMap.get("organiztion_name");
			
			Integer count = departmentDao.checkSelectDeptIsBand(dRoot.getBmDm());//根据部门代码查看是否绑定资金池
			
			
			if(count==0){//没有绑定资金池
				tree.setText(dRoot.getBmMc());
			}else {
				/*if(bmDmList.size()>0){
					for(int i=0;i<bmDmList.size();i++){
						Map bmdmMap = (Map) bmDmList.get(i);
						if(dRoot.getBmDm().equals(bmdmMap.get("bm_dm"))){
							tree.setText(dRoot.getBmMc()+"(已绑定)");
						}
					}
				}*/
					tree.setText(dRoot.getBmMc()+"(<font color='red'>"+organiztionName+"</font>绑定)");

				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("organiztion_num", organiztion_num);
				map1.put("bmDm", dRoot.getBmDm());
				Integer deptCount= departmentDao.getBandDeptByZjDmAndBmDm(map1);//判断是否自己绑定
				if(deptCount>0){
						jsonTreeResult.getMyBandList().add(dRoot);
				}else{
					Integer otherListCount = departmentDao.getOtherZjCBandDeptCount(map1);
					if(otherListCount>0){
						jsonTreeResult.getList().add(dRoot);
					}
				}
			}
		return jsonTreeResult;
	}
	

	@Override
	public boolean bandBalancePool(Map map) {
		 return departmentDao.bandBalancePool(map);
	}

	@Override
	public List<Department> getBandDeptByZjDm(String organiztion_num) {
		return departmentDao.getBandDeptByZjDm(organiztion_num);
	}

	@Override
	public boolean updateBalancePoolByZjDm(Map<String, String> map0) {
		return departmentDao.updateBalancePoolByZjDm(map0);
	}

	@Override
	public Integer checkSelectDeptIsBand(String bmDm) {
		return departmentDao.checkSelectDeptIsBand(bmDm);
	}

	@Override
	public List<String> getLeafChildrens(List<String> bmDms) {
		List<String> childrenList = new ArrayList<String>();
			for(String departDm:bmDms){
				getChildren(childrenList,departDm);
			}
		return childrenList;
	}
	
	public void getChildren(List<String> childrenSet,String bmDm){
		Integer count = departmentDao.getChildrenBySjDm(bmDm).size();
        if (count==0) {
        	childrenSet.add(bmDm);
        	return;
		}
	}

	@Override
	public Integer isBundDeptByOrganiztion_num(String organiztion_num) {
		return departmentDao.isBundDeptByOrganiztion_num(organiztion_num);
	}

	@Override
	public Tree getDepartmentCardTree(String departmentRoot, String layerDeptNum) {
//        List<Department> allDeparts = departmentDao.getAllDepart(layerDeptNum);
		List<Department> allDeparts = departmentDao.getDepartByParentBmDmA(departmentRoot, layerDeptNum);
//        List<EmployeeTreeNode> allEmployees=employeeDao.getAllEmployeeTreeNode();
        List<EmployeeTreeNode> allEmployees=new ArrayList<EmployeeTreeNode>();
        List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
        for (Department d:allDeparts){
            DepartmentNode departmentNode=new DepartmentNode();
            departmentNode.setUUID(d.getUUID());
            departmentNode.setBmDm(d.getBmDm());
            departmentNode.setSjbmDm(d.getSjbmDm());
            departmentNode.setMc(d.getBmMc());
            allNodes.add(departmentNode);
        }
		for (EmployeeTreeNode e:allEmployees){
			DepartmentNode departmentNode=new DepartmentNode();
			departmentNode.setUUID(e.getUUID());
			departmentNode.setMc(e.getYhMc());
			departmentNode.setBmDm(e.getYhDm());
			departmentNode.setSjbmDm(e.getBmDm());
			departmentNode.setIsDepartment(1);
			departmentNode.setXb(e.getXb());
			allNodes.add(departmentNode);
		}
        Tree rootTree = new Tree();
        initTreeAA(rootTree,allNodes,departmentRoot);
        //如果通过id获取的某一个节点与其本身是相同的，则说明其没有子节点，返回null
        if (!departmentRoot.equals(GlobalConstant.DEPARTMENT_ROOT_BM_DM)&&rootTree.getId().equals(departmentRoot)){
         return  null;
        }
        return rootTree;
	}
/*	@Override
	public Tree getDepartmentCardTree(String departmentRoot, String layerDeptNum) {
		
		//获取所有菜单
		List<Department> allDeparts = departmentDao.getAllDepart(layerDeptNum);
		List<EmployeeTreeNode> allEmployees=employeeDao.getAllEmployeeTreeNode();
//        List<EmployeeTreeNode> allEmployees=new ArrayList<EmployeeTreeNode>();
		List<Map>  allOrdinCards=peopleAuthorityDao.getStfOrdinCard();
		List<DepartmentNode>  allNodes=new ArrayList<DepartmentNode>();
		for (Department d:allDeparts){
			DepartmentNode departmentNode=new DepartmentNode();
			departmentNode.setUUID(d.getUUID());
			departmentNode.setBmDm(d.getBmDm());
			departmentNode.setSjbmDm(d.getSjbmDm());
			departmentNode.setMc(d.getBmMc());
			allNodes.add(departmentNode);
		}
		
		for (EmployeeTreeNode e:allEmployees){
			DepartmentNode departmentNode=new DepartmentNode();
			departmentNode.setUUID(e.getUUID());
			departmentNode.setMc(e.getYhMc());
			departmentNode.setBmDm(e.getYhDm());
			departmentNode.setSjbmDm(e.getBmDm());
			departmentNode.setIsDepartment(1);
			departmentNode.setXb(e.getXb());
			allNodes.add(departmentNode);
		}
		
		for(Map map:allOrdinCards){
			DepartmentNode depNode=new DepartmentNode();
			depNode.setUUID((String)map.get("cardNum"));
			depNode.setMc((String)map.get("cardNum"));
			depNode.setBmDm((String)map.get("cardNum"));
			depNode.setSjbmDm((String)map.get("staffNum"));
			depNode.setIsDepartment(5);//5代表卡
			//depNode.setMc((String)map.get("cardNum"));
			allNodes.add(depNode);
		}
		
		Tree rootTree = new Tree();
		initTree(rootTree,allNodes,departmentRoot);
//        initTreeA(rootTree,allNodes,departmentRoot);
		//如果通过id获取的某一个节点与其本身是相同的，则说明其没有子节点，返回null
		if (!departmentRoot.equals(GlobalConstant.DEPARTMENT_ROOT_BM_DM)&&rootTree.getId().equals(departmentRoot)){
			return  null;
		}
		return rootTree;
	}
*/	
	//=====================优化部门员工卡树开始==========================
	
    private void initTreeA(Tree rootTree, List<DepartmentNode> allDeparts,
    		String selfCode) {
    	Iterator<DepartmentNode> menuIterator = allDeparts.iterator();
    	while (menuIterator.hasNext()) {
    		DepartmentNode tmp = menuIterator.next();
    		// 初始化自身节点
    		if (tmp.getBmDm().equals(selfCode)&&tmp.getIsDepartment()==0) {
    			departToTree(tmp, rootTree);
    			
    		} else if (tmp.getSjbmDm().equals(selfCode)) {//tmp.getSjbmDm().equals(selfCode)
    			// 初始化子节点   找孩子
    			Tree children = new Tree();
    			departToTree(tmp, children);
    			rootTree.getChildren().add(children);
    			if(tmp.getIsDepartment()!=5){//如果tmp是卡，则停止递归
    				initTreeA(children, getChildDeparts(tmp.getIsDepartment(),tmp.getBmDm(),allDeparts), tmp.getBmDm());
    			}
    		}
    	}
    }
    
    /**
     * 优化树的构建
     * by gengji.yang
     */
    private List<DepartmentNode> getChildDeparts(Integer flag,String nodeDm,List<DepartmentNode> totalList){
    	List<DepartmentNode> list=new ArrayList<DepartmentNode>();
    	//List<DepartmentNode> restList=new ArrayList<DepartmentNode>();
    	if(flag==0){//部门父亲找孩子员工
    		for(DepartmentNode m:totalList){
    			if((m.getIsDepartment()==0|| m.getIsDepartment()==1) && nodeDm.equals(m.getSjbmDm())|| m.getIsDepartment()==5){
    				list.add(m);
    			}/*else{
    				restList.add(m);
    			}*/
    		}
    		//list.addAll(getChildDeparts(m.getIsDepartment(),m.getBmDm(),totalList));//找孙子
    	}else if(flag==1){//员工父亲找卡孩子
    		for(DepartmentNode m:totalList){
    			if(m.getIsDepartment()==5 && nodeDm.equals(m.getSjbmDm())){
    				list.add(m);
    			}
    		}
    	}
    	return list;
    }
    

    //===================优化部门员工卡树结束============================
	@Override
	public boolean setEncodeRule(Map map) {
		boolean staffFlag=departmentDao.setStaffNoRule(map);
		boolean deptFlag=departmentDao.setDeptNoRule(map);
		if(staffFlag&&deptFlag){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Map<String, String> selectRulesFromSystemParam() {
		String staffNoRule=departmentDao.selectStaffNoRule();
		String deptNoRule=departmentDao.selectDeptNoRule();
		Map<String,String> map = new HashMap<String,String>();
		map.put("staffNoRule", staffNoRule);
		map.put("deptNoRule", deptNoRule);
		return map;
	}

	@Override
	public String selectDeptNoRule() {
		return departmentDao.selectDeptNoRule();
	}

	@Override
	public boolean selectInfoByNum(String finalDeptNo1) {
		return departmentDao.selectInfoByBmbh(finalDeptNo1);
	}

	

	@Override
	public void updateDeptNoTemp(Map map) {
		departmentDao.updateDeptNoTemp(map);
	}

	@Override
	public void deleteAgricultureDeptData() {
		 departmentDao.deleteAgricultureDeptData();
		
	}

	@Override
	public List<Department> getAllDepart() {
		 
		return  departmentDao.getAllDepart();
	}

	@Override
	public String getLayerDeptNum(String yhDm, String jsDm) {
		StringBuffer deptNums = new StringBuffer();
		
		// 查询该用户创建的部门及子部门
		List<Department> allDepartment = departmentDao.getAllDepart();
		List<Department> depts = departmentDao.getDeptByLrryDm(yhDm);
		for (Department dept : depts) {
			deptNums.append("'" + dept.getBmDm() + "',");
			deptNums.append(getAllChildDeptByParent(dept.getBmDm(),allDepartment));
		}
		
		// 查询该用户角色授权部门
		List<String> roleDeptNums = departmentDao.getDeptByJsDm(jsDm);
		
		for (String roleDeptNum : roleDeptNums) {
			
			// 查询是否有该部门所有子部门的权限，若没有，则属于没有该部门权限
			List<String> childDeptNum = departmentDao.getChildDept(roleDeptNum);
			if(childDeptNum != null && childDeptNum.size() != 0){
				StringBuffer bmDms = new StringBuffer();
				for (String bmDm : childDeptNum) {
					bmDms.append("'"+bmDm+"',");
				}
				bmDms.setLength(bmDms.length() - 1);
				int jsBmCount = departmentDao.getJsBmCount(bmDms.toString(), jsDm);
				if(jsBmCount != childDeptNum.size()){
					continue;   //如果有权限的子部门总数和所有子部门总数不同，说明该角色没有该部门所有权限，不加入deptNums
				}
			}
			deptNums.append("'" + roleDeptNum + "',");
		}
		
		if(deptNums.length() > 0){
			deptNums.setLength(deptNums.length() - 1);
		}
		
		// 若deptNums为空，说明该用户没有任何部门的权限，为了不与"关闭分层功能"混淆，传入'none'字符串作为区分。
		return "".equals(deptNums.toString())?"'none'" : deptNums.toString();
	}
	
	
	@Override
	public String deptGetLayerDeptNum(String yhDm, String jsDm) {
		StringBuffer deptNums = new StringBuffer();
		
		// 查询该用户创建的部门及子部门
		List<Department> allDepartment = departmentDao.getAllDepart();
		List<Department> depts = departmentDao.getDeptByLrryDm(yhDm);
		for (Department dept : depts) {
			deptNums.append("'" + dept.getBmDm() + "',");
			deptNums.append(getAllChildDeptByParent(dept.getBmDm(),allDepartment));
		}
		
		// 查询该用户角色授权部门
		List<String> roleDeptNums = departmentDao.getDeptByJsDm(jsDm);
		for (String roleDeptNum : roleDeptNums) {
			deptNums.append("'" + roleDeptNum + "',");
		}
		
		if(deptNums.length() > 0){
			deptNums.setLength(deptNums.length() - 1);
		}
		
		// 若deptNums为空，说明该用户没有任何部门的权限，为了不与"关闭分层功能"混淆，传入'none'字符串作为区分。
		return "".equals(deptNums.toString())?"'none'" : deptNums.toString();
	}

	
	/**
	 * 查询该父部门下所有子部门
	 * @author yuman.gao
	 */
	public String getAllChildDeptByParent(String deptNum,List<Department> allDepartments) {
		StringBuffer deptNums = new StringBuffer();
		
		for (Department department:allDepartments) {
			if (department.getSjbmDm().equals(deptNum)) {
				deptNums.append("'" + department.getBmDm() +"',");
				deptNums.append(getAllChildDeptByParent(department.getBmDm(),allDepartments));
			}
		}
		
		return deptNums.toString();
	}

	@Override
	public boolean bandBalancePoolBybmDm(Map<String, String> map, String bmDms,String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "资金池管理");
		log.put("V_OP_FUNCTION", "绑定");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "绑定资金池");
		boolean result = false;
	
		if("".equals(bmDms)){
			log.put("V_OP_TYPE", "业务");
			result = true;
		}else{
			String[] bmdms= bmDms.split(",");
			try {
				for(String bmDm:bmdms){
					map.put("bmDm", bmDm);
					result = departmentDao.bandBalancePool(map);//新的部门与资金池绑定
					if(result){
						log.put("V_OP_TYPE", "业务");
					}else{
						log.put("V_OP_TYPE", "异常");
						break;
					}
				}
			} catch (Exception e) {
				log.put("V_OP_TYPE", "异常");
				result = false;
				e.printStackTrace();
			}
		}
		logDao.addLog(log);
		return result;
	}
}

