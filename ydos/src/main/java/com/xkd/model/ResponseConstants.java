package com.xkd.model;

public class ResponseConstants {

	public static final ResponseDbCenter SUCCESS = new ResponseDbCenter("S0000","SUCCESS");
	
	public static final ResponseDbCenter MISSING_PARAMTER = new ResponseDbCenter("CRM1001","参数缺失");
			
	public static final ResponseDbCenter ILLEGAL_PARAM = new ResponseDbCenter("CRM1002", "参数非法");
	
	public static final ResponseDbCenter FUNC_SERVERERROR = new ResponseDbCenter("CRM1003", "服务器异常");

	
	public static final ResponseDbCenter PERMITTION_DENIE = new ResponseDbCenter("CRM1007", "权限拒绝");


	public static final ResponseDbCenter USER_NOTEXIST = new ResponseDbCenter("CRM1009", "用户不存在");
	
	public static final ResponseDbCenter MOBILE_EXIST = new ResponseDbCenter("CRM1010", "手机号码已经存在");
	
	public static final ResponseDbCenter EMAIL_EXIST = new ResponseDbCenter("CRM1011", "邮箱已经存在");
	
	public static final ResponseDbCenter FUNC_COMPANY_EXIST = new ResponseDbCenter("CRM1012", "该企业名已经存在，请更换其他企业名称");


	public static final ResponseDbCenter FUNC_UPDATENOID = new ResponseDbCenter("CRM1021", "修改表数据，没有传入id");


	public static final ResponseDbCenter FUNC_USER_OPENID_FAIL = new ResponseDbCenter("CRM1029", "根据code获得openId失败");


	public static final ResponseDbCenter FUNC_DBCENTER_TOKENISWRONG = new ResponseDbCenter("CRM1038", "服务器缓存中没有该token，该token已经无效");


	public static final ResponseDbCenter FUNC_USER_NOTOKEN = new ResponseDbCenter("CRM1040", "请传入token令牌");

	public static final ResponseDbCenter FUNC_USER_MOBILEHASREGISTER = new ResponseDbCenter("CRM1041", "该手机号已经注册，请修改手机号");


	public static final ResponseDbCenter ACTIVE_ERROR = new ResponseDbCenter("CRM1043", "操作失败");
	

	
	public static final ResponseDbCenter FUNC_UPLOADFILEFAIL = new ResponseDbCenter("CRM1050", "文件上传失败");


	public static final ResponseDbCenter CHILDREN_ROLE_EXISTS_ERROR = new ResponseDbCenter("CRM1054", "还有子菜单没有被删除，请先删除子菜单");
	
	public static final ResponseDbCenter PASSWORD_NOT_SAME = new ResponseDbCenter("CRM1055", "两次输入密码不一致");

	
	public static final ResponseDbCenter DICTIONARY_HAS_EXISTS = new ResponseDbCenter("CRM1058", "该数据已经在数据字典中存在了");
	
	public static final ResponseDbCenter PASSWORD_ERROR = new ResponseDbCenter("CRM1059", "原始密码输入有误");
	
	public static final ResponseDbCenter PAGERFILE_NAME_ERROR = new ResponseDbCenter("CRM1100", "当前文件已存在");
	
	public static final ResponseDbCenter PAGERFILE_PAGERFILE_NAME_ERROR = new ResponseDbCenter("CRM1101", "当前文件夹已存在");


	
	public static final ResponseDbCenter SUPER_ROLE_ERROR = new ResponseDbCenter("CRM1104", "不允许对超级管理员进行修改！");
	
	public static final ResponseDbCenter NOT_PERMITTED = new ResponseDbCenter("CRM1105", "无权限！");



	public static final ResponseDbCenter DATA_DEPARTMENT_HAS_DATA = new ResponseDbCenter("CRM1125", "部门下有未删除的数据，请先将该部门下的数据迁出再删除");

	public static final ResponseDbCenter DATA_OPERATIONCODE_REPEATED = new ResponseDbCenter("CRM1126", "权限编码重复");


	public static final ResponseDbCenter USER_UPLOAD_ERROR = new ResponseDbCenter("CRM1128", "该文件被占用无法重命名！");


	public static final ResponseDbCenter NOT_AUTHORIZED_TO_MODIFY_SUPERROLE = new ResponseDbCenter("CRM1130", "非超级管理员不能改变超级管理员用户的角色！");


	public static final ResponseDbCenter ADMIN_ROLE_CANNOT_DELETE = new ResponseDbCenter("CRM1129", "管理员角色不能删除!");




	public static final ResponseDbCenter CANNOT_ADD_CHILD = new ResponseDbCenter("CRM1132", "该处不能添加子结点");
	public static final ResponseDbCenter DEPARTMENT_HAS_USER = new ResponseDbCenter("CRM1133", "请先将该部门下的员工转出!");
	public static final ResponseDbCenter DEPARTMENT_NOT_NULL = new ResponseDbCenter("CRM1134", "部门不能为空");



	public static final ResponseDbCenter TEAM_ALREADY_EXISTS = new ResponseDbCenter("CRM1201", "已经存在该名称的班组！");


	//WJ
	
	public static final ResponseDbCenter MISSING_PARAMTER_WJ = new ResponseDbCenter("S1001","参数缺失");
				

	public static final ResponseDbCenter NAME_PWD_ERROR_WJ = new ResponseDbCenter("S1004","用户名或密码输入错误");
	
	public static final ResponseDbCenter USER_EXIST_ERROR_WJ = new ResponseDbCenter("S1005","用户不存在或已失效");
	

	public static final ResponseDbCenter TEL_CODE_ERROR = new ResponseDbCenter("S1010", "验证码错误");

	public static final ResponseDbCenter FUNC_BP_TOKENISWRONG = new ResponseDbCenter("S1038", "服务器缓存中没有该token，该token已经无效");
	
	public static final ResponseDbCenter USER_ERROR_SIGN = new ResponseDbCenter("S1011", "用户未注册");
	




	public static final ResponseDbCenter CONTRACT_NO_EXITS = new ResponseDbCenter("YD1000", "合同号已被占用！");
	public static final ResponseDbCenter USER_REGISTER_CODE_OUTOT_DATE = new ResponseDbCenter("YD2001", "验证码已失效");
	public static final ResponseDbCenter USER_MOBILE_NO_REGISTER = new ResponseDbCenter("YD2002", "该手机号未注册");
	public static final ResponseDbCenter INVALID_BOXID = new ResponseDbCenter("YD2003", "无效的云盒ID");
	public static final ResponseDbCenter BOX_ID_ALREADY_EXIST = new ResponseDbCenter("YD2004", "云盒ID已经存在");

}
