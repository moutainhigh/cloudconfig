package com.xkd.model;

public class ResponseConstants {

	public static final ResponseDbCenter SUCCESS = new ResponseDbCenter("S0000","SUCCESS");
	
	public static final ResponseDbCenter MISSING_PARAMTER = new ResponseDbCenter("CRM1001","参数缺失");
			
	public static final ResponseDbCenter ILLEGAL_PARAM = new ResponseDbCenter("CRM1002", "参数非法");
	
	public static final ResponseDbCenter FUNC_SERVERERROR = new ResponseDbCenter("CRM1003", "服务器异常");
	
	public static final ResponseDbCenter FUNC_GETCOMPANYERROR = new ResponseDbCenter("CRM1004", "根据企业id没有查询到企业信息");
	
	public static final ResponseDbCenter NAME_PWD_ERROR = new ResponseDbCenter("CRM1005","用户名或密码输入错误");
	
	public static final ResponseDbCenter USER_EXIST_ERROR = new ResponseDbCenter("CRM1006","用户不存在或已失效");
	
	public static final ResponseDbCenter PERMITTION_DENIE = new ResponseDbCenter("CRM1007", "权限拒绝");

	public static final ResponseDbCenter LOGIN_TIMEOUT = new ResponseDbCenter("CRM1008", "未登录或登陆已超时，请重新登录");
	
	public static final ResponseDbCenter USER_NOTEXIST = new ResponseDbCenter("CRM1009", "用户不存在");
	
	public static final ResponseDbCenter MOBILE_EXIST = new ResponseDbCenter("CRM1010", "手机号码已经存在");
	
	public static final ResponseDbCenter EMAIL_EXIST = new ResponseDbCenter("CRM1011", "邮箱已经存在");
	
	public static final ResponseDbCenter FUNC_COMPANY_EXIST = new ResponseDbCenter("CRM1012", "该企业名已经存在，请更换其他企业名称");
	
	public static final ResponseDbCenter FUNC_MODULE_NOTEQUELS = new ResponseDbCenter("CRM1013", "功能模块不能重复");
	
	public static final ResponseDbCenter FUNC_PARAM_NOID = new ResponseDbCenter("CRM1014", "传入后台ID不合理");
	
	public static final ResponseDbCenter FUNC_MODULE_INSERTNOTCOMPLETE = new ResponseDbCenter("CRM1015", "插入条件不完整，请检查bpId，状态等信息是否填充完整");
	
	public static final ResponseDbCenter FUNC_MODULE_INSERTERROR = new ResponseDbCenter("CRM1016", "表数据没有改变");
	
	public static final ResponseDbCenter FUNC_MODULE_DELETEERROR = new ResponseDbCenter("CRM1017", "删除失败");
	
	public static final ResponseDbCenter FUNC_MODULE_SERVERERROR = new ResponseDbCenter("CRM1018", "服务器异常，请稍后再试");
	
	public static final ResponseDbCenter FUNC_MODULE_TYPEISNULL = new ResponseDbCenter("CRM1019", "传入后台的ttype为空，无法查出数据");
	
	public static final ResponseDbCenter FUNC_TABLE_CHANGEPARAMERROR = new ResponseDbCenter("CRM1020", "改变表参数不完整，请检查是否填写完整");

	public static final ResponseDbCenter FUNC_UPDATENOID = new ResponseDbCenter("CRM1021", "修改表数据，没有传入id");

	public static final ResponseDbCenter FUNC_NOUSERID = new ResponseDbCenter("CRM1022", "请传入用户id");
	
	public static final ResponseDbCenter FUNC_DATE_ERROR = new ResponseDbCenter("CRM1023", "总金额与出让金额不匹配");
	
	public static final ResponseDbCenter FUNC_MODULE_CONTENT_DELETEERROR = new ResponseDbCenter("CRM1024", "模块内容删除失败");

	public static final ResponseDbCenter FUNC_GETMODELSEERROR = new ResponseDbCenter("CRM1025", "获得模块信息失败，请检查各模块表是否都应经初始化，表中是否都有数据");

	public static final ResponseDbCenter FUNC_SAVEMODELSORDERERROR = new ResponseDbCenter("CRM1026", "保存模板顺序,传入后台的id个数与排序序号个数对不上,请检查是否规范");
	
	public static final ResponseDbCenter FUNC_GETMODELSPARAMERROR = new ResponseDbCenter("CRM1027", "获得模块信息，参数错误，请检查是否缺少bpId、是否缺少天使轮，A轮、B轮数据参数");
	
	public static final ResponseDbCenter FUNC_USERLOGINPARAMERROR = new ResponseDbCenter("CRM1028", "code、手机号、验证码不能为空");

	public static final ResponseDbCenter FUNC_USER_OPENID_FAIL = new ResponseDbCenter("CRM1029", "根据code获得openId失败");

	public static final ResponseDbCenter FUNC_USER_REGISTERPARAM_ERROR = new ResponseDbCenter("CRM1030", "用户注册失败，请检查手机号、验证码、openId是否为空，是否有误");

	public static final ResponseDbCenter FUNC_USER_NOTREGISTER = new ResponseDbCenter("CRM1031", "该用户没有注册，请先注册");

	public static final ResponseDbCenter FUNC_USER_USEWECHATINTERFACEERROR = new ResponseDbCenter("CRM1032", "服务器调用微信接口发生异常");

	public static final ResponseDbCenter FUNC_USER_NOCODE = new ResponseDbCenter("CRM1033", "code为空，请传入code");

	public static final ResponseDbCenter FUNC_USER_NONICKNAMEANDHEADIMG = new ResponseDbCenter("CRM1034", "根据code无法获得微信的昵称和头像");

	public static final ResponseDbCenter FUNC_USER_SELECTMODELEXAMPLE_NOTTYPE = new ResponseDbCenter("CRM1035", "ttype为空，请传入ttype");

	public static final ResponseDbCenter FUNC_USER_VERIFYCODEISERROR = new ResponseDbCenter("CRM1036", "验证码输入有误,或者该验证码已经过期，请重新获取验证码");

	public static final ResponseDbCenter FUNC_USER_PHONEANDVERIFYCODEISNULL = new ResponseDbCenter("CRM1037", "手机号、验证码不能为空");

	public static final ResponseDbCenter FUNC_DBCENTER_TOKENISWRONG = new ResponseDbCenter("CRM1038", "服务器缓存中没有该token，该token已经无效");

	public static final ResponseDbCenter FUNC_USER_HEADANDNICKNAMEISERROR = new ResponseDbCenter("CRM1039", "头像、昵称、openId不能为空");

	public static final ResponseDbCenter FUNC_USER_NOTOKEN = new ResponseDbCenter("CRM1040", "请传入token令牌");

	public static final ResponseDbCenter FUNC_USER_MOBILEHASREGISTER = new ResponseDbCenter("CRM1041", "该手机号已经注册，请修改手机号");

	public static final ResponseDbCenter FUNC_USER_LOGINOTHERPLACE = new ResponseDbCenter("CRM1042", "该用户已经在其他地方登录");
	
	public static final ResponseDbCenter ACTIVE_ERROR = new ResponseDbCenter("CRM1043", "操作失败");
	
	public static final ResponseDbCenter FUNC_COMPANYREGISTER_PARAM_ERROR = new ResponseDbCenter("CRM1044", "请检查传入企业id、地址id、企业名称是否为空");
	
	public static final ResponseDbCenter NO_ROLE_MEETING = new ResponseDbCenter("CRM1045", "没有权限参加此会议");

	public static final ResponseDbCenter ERROR_EXPORTDATA_EXCELL = new ResponseDbCenter("CRM1046", "数据导出Excell失败");
	
	public static final ResponseDbCenter FUNC_DATANOTCHANGE = new ResponseDbCenter("CRM1111", "数据没有改变,或数据没有删除");
	
	public static final ResponseDbCenter FUNC_COMPANYHASPROJECTANDUSERINFO = new ResponseDbCenter("CRM1047", "企业中包含项目、联系人信息，确定删除？");
	
	public static final ResponseDbCenter FUNC_COMPANYHASPROJECT = new ResponseDbCenter("CRM1048", "企业中包含项目信息，确定删除？");
	
	public static final ResponseDbCenter FUNC_COMPANYHASUSERINFO = new ResponseDbCenter("CRM1049", "企业中包含联系人信息，确定删除？");
	
	public static final ResponseDbCenter FUNC_UPLOADFILEFAIL = new ResponseDbCenter("CRM1050", "文件上传失败");
	
	public static final ResponseDbCenter SCHEDULE_EXISTED = new ResponseDbCenter("CRM1051", "讲师行程已存在");

	public static final ResponseDbCenter SCHEDULE_TIME_CONFLICT = new ResponseDbCenter("CRM1052", "行程详情时间与行程起止时间冲突");
	
	public static final ResponseDbCenter SCHEDULE_DETAIL_TIME_CONFLICT = new ResponseDbCenter("CRM1053", "当前行程详情时间冲突");

	public static final ResponseDbCenter CHILDREN_ROLE_EXISTS_ERROR = new ResponseDbCenter("CRM1054", "还有子菜单没有被删除，请先删除子菜单");
	
	public static final ResponseDbCenter PASSWORD_NOT_SAME = new ResponseDbCenter("CRM1055", "两次输入密码不一致");
	
	public static final ResponseDbCenter MOBILE_EMAIL_MUSTHAVE = new ResponseDbCenter("CRM1056", "手机、邮箱必须有一样必填");
	
	public static final ResponseDbCenter XINGC_NOT_SAME = new ResponseDbCenter("CRM1057", "行程冲突");
	
	public static final ResponseDbCenter DICTIONARY_HAS_EXISTS = new ResponseDbCenter("CRM1058", "该数据已经在数据字典中存在了");
	
	public static final ResponseDbCenter PASSWORD_ERROR = new ResponseDbCenter("CRM1059", "原始密码输入有误");
	
	public static final ResponseDbCenter PAGERFILE_NAME_ERROR = new ResponseDbCenter("CRM1100", "当前文件已存在");
	
	public static final ResponseDbCenter PAGERFILE_PAGERFILE_NAME_ERROR = new ResponseDbCenter("CRM1101", "当前文件夹已存在");
	
	public static final ResponseDbCenter PAGER_NAME_ERROR = new ResponseDbCenter("CRM1102", "当前目录作此操作");
	
	public static final ResponseDbCenter EXCELL_IMPORT_ERROR = new ResponseDbCenter("CRM1103", "导入异常，请检查文件格式是否与模板一致");
	
	public static final ResponseDbCenter SUPER_ROLE_ERROR = new ResponseDbCenter("CRM1104", "不允许对超级管理员进行修改！");
	
	public static final ResponseDbCenter NOT_PERMITTED = new ResponseDbCenter("CRM1105", "无权限！");

	public static final ResponseDbCenter PROJECTNAME_EXISTS = new ResponseDbCenter("CRM1106", "该课程名称（项目名称）已经存在");
	
	public static final ResponseDbCenter PROJECTCODE_EXISTS = new ResponseDbCenter("CRM1107", "该项目编号已经存在");
	
	public static final ResponseDbCenter PROGRAMNAME_EXISTS = new ResponseDbCenter("CRM1108", "该方案名称已经存在");
	
	public static final ResponseDbCenter PROGRAMCODE_EXISTS = new ResponseDbCenter("CRM1109", "该方案编号已经存在");
	
	public static final ResponseDbCenter DEPARTMENT_EXISTS = new ResponseDbCenter("CRM1110", "该部门名称已经存在");

	public static final ResponseDbCenter ROLE_EXISTS = new ResponseDbCenter("CRM1111", "角色名称已经存在");
	
	public static final ResponseDbCenter USER_SET_COMPANY = new ResponseDbCenter("CRM1112", "请选择你要绑定的企业");

	public static final ResponseDbCenter MEETING_ATTEND_USER_REPEAT = new ResponseDbCenter("CRM1113", "参会人不可重复");

	public static final ResponseDbCenter USER_GIFT_HAS_GETGIFT = new ResponseDbCenter("CRM1114", "你已经抽过奖了");

	public static final ResponseDbCenter POINT_NAME_HAS_EXISTS = new ResponseDbCenter("CRM1115", "该网点名称已经存在");

	public static final ResponseDbCenter PROJECT_POINT_HAS_EXISTS = new ResponseDbCenter("CRM1116", "网点已经存在项目了");

	public static final ResponseDbCenter EORRER_AUTCION = new ResponseDbCenter("CRM1117", "sorry,你已领券,不能重复操作!");

	public static final ResponseDbCenter USER_QUAN_AUTCION = new ResponseDbCenter("CRM1118", "你还没有领券!");

	public static final ResponseDbCenter TICKET_NOT_EXISTS_BYID = new ResponseDbCenter("CRM1119", "根据票ID没有查到票");

	public static final ResponseDbCenter TICKET_HAS_SELL_OUT = new ResponseDbCenter("CRM1120", "余票已售完");

	public static final ResponseDbCenter TICKET_NUMBER_ZERO = new ResponseDbCenter("CRM1121", "选择的票数不能为零");

	public static final ResponseDbCenter TICKET_USER_EXISTS = new ResponseDbCenter("CRM1122", "该用户已参会");

	public static final ResponseDbCenter TICKET_NOT_ENOUGHT = new ResponseDbCenter("CRM1123", "余票不足");

	public static final ResponseDbCenter DATA_NOT_PERMITED = new ResponseDbCenter("CRM1124", "无该条数据权限，请联系管理员先设置可视数据权限");

	public static final ResponseDbCenter DATA_DEPARTMENT_HAS_DATA = new ResponseDbCenter("CRM1125", "部门下有未删除的数据，请先将该部门下的数据迁出再删除");

	public static final ResponseDbCenter DATA_OPERATIONCODE_REPEATED = new ResponseDbCenter("CRM1126", "权限编码重复");

	public static final ResponseDbCenter USER_ORDER_CANCELED = new ResponseDbCenter("CRM1127", "该订单因为15分钟没有完成支付，已经被取消");

	public static final ResponseDbCenter USER_UPLOAD_ERROR = new ResponseDbCenter("CRM1128", "该文件被占用无法重命名！");

	public static final ResponseDbCenter USER_SPREAD_EXISTS = new ResponseDbCenter("CRM1129", "该推广人在会务中已经存在！");

	public static final ResponseDbCenter NOT_AUTHORIZED_TO_MODIFY_SUPERROLE = new ResponseDbCenter("CRM1130", "非超级管理员不能改变超级管理员用户的角色！");


	public static final ResponseDbCenter ADMIN_ROLE_CANNOT_DELETE = new ResponseDbCenter("CRM1129", "管理员角色和模板角色不能删除!");

	public static final ResponseDbCenter ACCOUNT_EXPIRE = new ResponseDbCenter("CRM1130", "账号过期!");


	public static final ResponseDbCenter ACCOUNT_DIABLED = new ResponseDbCenter("CRM1131", "帐号已冻结");

	public static final ResponseDbCenter CANNOT_ADD_CHILD = new ResponseDbCenter("CRM1132", "该处不能添加子结点");
	public static final ResponseDbCenter DEPARTMENT_HAS_USER = new ResponseDbCenter("CRM1133", "请先将该部门下的员工转出!");
	public static final ResponseDbCenter DEPARTMENT_NOT_NULL = new ResponseDbCenter("CRM1134", "部门不能为空");
	public static final ResponseDbCenter  PROJECT_ALLREADY_DELETED = new ResponseDbCenter("CRM1135", "该关联项目已经删除,不能切换关联状态");

	public static final ResponseDbCenter NO_DELETE = new ResponseDbCenter("CRM1200", "你已删除，请不要重复操作！");

	public static final ResponseDbCenter NO_TUIKUAN = new ResponseDbCenter("CRM1201", "你退款的金额大于缴费金额，请认真核对！");

	public static final ResponseDbCenter CONTRACT_NO_EXISTS = new ResponseDbCenter("CRM1202", "合同号重复");

	public static final ResponseDbCenter RIVAL_NAME_EXISTS = new ResponseDbCenter("CRM1203", "竞争者名称已经存在");

	public static final ResponseDbCenter MEETING_MOBILE_NOT_EXISTS = new ResponseDbCenter("CRM1300", "会务中没有该手机号");
	public static final ResponseDbCenter MEETING_LOGIN_USER_NOT_EXISTS = new ResponseDbCenter("CRM1301", "code自动登录成功，但是用户没有参会买票");
	public static final ResponseDbCenter LOGIN_USER_NOT_EXISTS = new ResponseDbCenter("CRM1301", "code没有获得到用户");
	public static final ResponseDbCenter DICTIONARY_DATA_NOT_EXISTS = new ResponseDbCenter("CRM1305", "根据ID在数据字典中没有查到数据");
	public static final ResponseDbCenter GET_MONDY_BIGGER_THEN_ACCONUT_SAVING = new ResponseDbCenter("CRM1306", "提现金额大于账户余额");

	public static final ResponseDbCenter NO_MORE_THAN_1000 = new ResponseDbCenter("CRM1302", "一次导入不能超过1000条");

	public static final ResponseDbCenter COMPANY_NAME_NOT_NULL = new ResponseDbCenter("CRM1303", "客户名称不能为空");
	public static final ResponseDbCenter NO_SPREAD_USER_BY_OPENID = new ResponseDbCenter("CRM1304", "微信自动登录失败，根据openId没有查到该用户");


	//WJ
	
	public static final ResponseDbCenter MISSING_PARAMTER_WJ = new ResponseDbCenter("S1001","参数缺失");
				
	public static final ResponseDbCenter ACTIVE_ERROR_WJ = new ResponseDbCenter("S1003", "操作失败");
	
	public static final ResponseDbCenter NAME_PWD_ERROR_WJ = new ResponseDbCenter("S1004","用户名或密码输入错误");
	
	public static final ResponseDbCenter USER_EXIST_ERROR_WJ = new ResponseDbCenter("S1005","用户不存在或已失效");
	
	public static final ResponseDbCenter FUNC_USER_OPENID_FAIL_WJ = new ResponseDbCenter("S1007", "根据code获得openId失败");

	public static final ResponseDbCenter TEL_CODE_ERROR = new ResponseDbCenter("S1010", "验证码错误");

	public static final ResponseDbCenter FUNC_BP_TOKENISWRONG = new ResponseDbCenter("S1038", "服务器缓存中没有该token，该token已经无效");
	
	public static final ResponseDbCenter USER_ERROR_SIGN = new ResponseDbCenter("S1011", "用户未注册");
	
	public static final ResponseDbCenter USER_BIND_TEL = new ResponseDbCenter("S1012", "用户未绑定手机号码");
	
	public static final ResponseDbCenter USER_TEL_NOT_NULL = new ResponseDbCenter("S1013", "该手机号码已存在不能重复注册");
	
	public static final ResponseDbCenter USER_QIANDAO_SHENHE = new ResponseDbCenter("WJ1014", "你的申请暂未审核，请联系现场顾问进行审核！");
	
	public static final ResponseDbCenter USER_NO_CHECK = new ResponseDbCenter("WJ1045", "请申请成为参会人");
	
	public static final ResponseDbCenter USER_NOCHONGFU_SHENHE = new ResponseDbCenter("WJ1046", "你已报名");
	
	public static final ResponseDbCenter USER_SIGN_SUCCESS = new ResponseDbCenter("WJ1047", "签到成功");
	
	public static final ResponseDbCenter USER_SHENQING_SUCCESS = new ResponseDbCenter("WJ1048", "申请成功，请联系现在顾问进行审核！");
}
