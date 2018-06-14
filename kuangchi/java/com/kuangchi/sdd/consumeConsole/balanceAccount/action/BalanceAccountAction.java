package com.kuangchi.sdd.consumeConsole.balanceAccount.action;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.balanceAccount.service.IBalanceAccountService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * guofei.lian
 * 2016-08-25
 * 清账action
 * */
@Controller("balanceAccountAction")
public class BalanceAccountAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "balanceAccountServiceImpl")
	private IBalanceAccountService balanceAccountService;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	
	//获取所有账户异常记录
	public void getBalanceAccountInfoList(){
		HttpServletRequest request = getHttpServletRequest();
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		Grid balanceAccount=balanceAccountService.getBalanceAccountInfoList(page,rows);
		printHttpServletResponse(GsonUtil.toJson(balanceAccount));
		
	}
	
	//获取时间段内的所有流水明细记录
	public void getAccountDetailList(){
		HttpServletRequest request = getHttpServletRequest();
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String previous_time=request.getParameter("previous_time");
		String current_time=request.getParameter("current_time");
		String account_num=request.getParameter("account_num");
		Grid accountDetailList=balanceAccountService.getAccountDetailList(previous_time,current_time,account_num,page,rows);
		printHttpServletResponse(GsonUtil.toJson(accountDetailList));
	}
	
	/**
	 * 查看流水异常时显示差额
	 * @author minting.he
	 */
	public void initBalance(){
		HttpServletRequest request = getHttpServletRequest();
		String previous_balance = request.getParameter("previous_balance");
		String current_balance = request.getParameter("current_balance");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(current_balance)) {
			result.setSuccess(false);
			result.setMsg("数据不合法");
		} else {
			BigDecimal balance = balanceAccountService.initBalance(new BigDecimal(previous_balance), new BigDecimal(current_balance));
			printHttpServletResponse(GsonUtil.toJson(balance.toString()));
		}
	}
	
	/**
	 * 平账，补差额
	 * @author minting.he
	 */
	public void supplyBalance(){
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("exceptionId");	//异常记录id
		String current_balance = request.getParameter("current_balance");	//余额
		String account_num = request.getParameter("account_num");	//账号
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(id, current_balance, account_num)) {
			result.setSuccess(false);
			result.setMsg("平账失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				boolean r = balanceAccountService.supplyBalance(id, current_balance, account_num, login_user);
				if(r){
					result.setSuccess(true);
					result.setMsg("平帐成功");
				} else {
					result.setSuccess(false);
					result.setMsg("平账失败");
				}
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		}
	}
	
	/**
	 * 手动清算账户
	 * @author minting.he
	 */
	public void clearAccounts(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				boolean r = balanceAccountService.clearAccounts(login_user);
				if(r){
					result.setSuccess(true);
				} else {
					result.setSuccess(false);
					result.setMsg("清账失败");
				}
			}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

}
