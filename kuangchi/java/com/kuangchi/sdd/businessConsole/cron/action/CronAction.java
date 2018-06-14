package com.kuangchi.sdd.businessConsole.cron.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.businessConsole.cron.model.Cron;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * 页面定时任务执行
 */
@Controller("cronAction")
public class CronAction extends BaseActionSupport {

	private Cron model;

	public CronAction() {
		this.model = new Cron();
	}

	@Resource(name = "cronServiceImpl")
	private ICronService cronService;

	@Override
	public Object getModel() {
		return model;
	}

	/**
	 * 显示IP地址
	 * 
	 * @author minting.he
	 */
	public void selectIP() {
		Cron pageCron = new Cron();
		Cron pageCron2 = new Cron();
		pageCron = cronService.selectIP("ip");
		pageCron2 = cronService.selectIP("ip2");
		pageCron.setDescription(pageCron2.getSys_value());
		printHttpServletResponse(GsonUtil.toJson(pageCron));
	}

	/**
	 * 修改页面执行定时任务的IP地址
	 * 
	 * @author minting.he
	 */
	public void updateCronIP() {
		boolean r;
		boolean r2;
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		try {
			HttpServletRequest request = getHttpServletRequest();
			Cron pageCron = new Cron();
			Cron pageCron2 = new Cron();
			BeanUtils.copyProperties(model, pageCron);
			if (EmptyUtil.atLeastOneIsEmpty(pageCron.getId())) {
				result.setMsg("数据错误");
			} else {
				User loginUser = (User) request.getSession().getAttribute(
						GlobalConstant.LOGIN_USER);
				String create_user = loginUser.getYhMc();
				pageCron.setSys_key("ip");
				r = cronService.updateCronIP(pageCron, create_user);
				pageCron2.setSys_value(request.getParameter("sys_value2"));
				pageCron2.setSys_key("ip2");
				r2 = cronService.updateCronIP(pageCron2, create_user);

				if (r && r2) {
					result.setSuccess(true);
				}
			}
		} catch (Exception e) {
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

}
