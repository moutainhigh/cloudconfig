package com.kuangchi.sdd.consumeConsole.deviceType.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.deviceType.model.DeviceType;
import com.kuangchi.sdd.consumeConsole.deviceType.service.IDeviceTypeService;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * 设备类型管理
 * @author minting.he
 *
 */
@Controller("deviceTypeAction")
public class DeviceTypeAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource(name = "deviceTypeService")
	private IDeviceTypeService deviceTypeService;

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 按条件模糊查询设备类型（分页）
	 * @author minting.he
	 */
	public void getDeviceTypePage(){
		HttpServletRequest request = getHttpServletRequest();
        String beanObject = request.getParameter("data");
        DeviceType deviceTypeSearch = GsonUtil.toBean(beanObject, DeviceType.class);  
        if(BeanUtil.isNotEmpty(deviceTypeSearch)){
	        if(!EmptyUtil.atLeastOneIsEmpty(deviceTypeSearch.getDevice_type_name())){
	        	deviceTypeSearch.setDevice_type_name(deviceTypeSearch.getDevice_type_name().trim().replace("<","&lt").replace(">","&gt"));
	        }
        }
        deviceTypeSearch.setPage(Integer.parseInt(request.getParameter("page")));
        deviceTypeSearch.setRows(Integer.parseInt(request.getParameter("rows")));
        Grid<DeviceType> deviceTypeGrid = deviceTypeService.getDeviceTypePage(deviceTypeSearch);
    	printHttpServletResponse(GsonUtil.toJson(deviceTypeGrid));
	}
	
	/**
	 * 根据id查询设备类型
	 * @author minting.he
	 */
	public void selDeviceTypeById(){
		HttpServletRequest request = getHttpServletRequest();
        String id = request.getParameter("id");
        DeviceType deviceType = deviceTypeService.selDeviceTypeById(id);
        printHttpServletResponse(GsonUtil.toJson(deviceType));
	}
	
	/**
	 * 设备类型编号是否存在
	 * @author minting.he
	 */
	public void validNum(){
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
        String device_type_num = request.getParameter("device_type_num");
        DeviceType deviceType = new DeviceType();
        deviceType.setDevice_type_num(device_type_num);
        if(!EmptyUtil.atLeastOneIsEmpty(id)){
			deviceType.setId(id);
		}
        Integer count = deviceTypeService.validNum(deviceType);
		printHttpServletResponse(GsonUtil.toJson(count));
	}
	
	/**
	 * 新增设备类型
	 * @author minting.he
	 */
	public void insertDeviceType(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("dataForm");			
        DeviceType pageBean = GsonUtil.toBean(beanObject, DeviceType.class);
        JsonResult result = new JsonResult();
        pageBean.setDevice_type_name(pageBean.getDevice_type_name().trim().replace("<","&lt").replace(">","&gt"));
        if(BeanUtil.isEmpty(pageBean) || deviceTypeService.validNum(pageBean)!=0){
        	result.setSuccess(false);
     	    result.setMsg("新增失败，数据不合法");
        }else {
            User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
            if(loginUser == null){
				result.setSuccess(false);
			    result.setMsg("操作失败，请先登录");
			} else {
				String create_user = loginUser.getYhMc();
				boolean r = deviceTypeService.insertDeviceType(pageBean, create_user);
	            if(r){
	            	result.setSuccess(true);
	        	    result.setMsg("新增成功");
	            }else {
	            	result.setSuccess(false);
	        	    result.setMsg("新增失败");
	            }
			}
        }
        printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 修改设备类型
	 * @author minting.he
	 */
	public void updateDeviceType(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
        String beanObject = request.getParameter("dataForm");
        DeviceType pageBean = GsonUtil.toBean(beanObject, DeviceType.class);
        pageBean.setDevice_type_name(pageBean.getDevice_type_name().trim().replace("<","&lt").replace(">","&gt"));
        pageBean.setDevice_type_num(pageBean.getDevice_type_num());
        if(BeanUtil.isEmpty(pageBean) || deviceTypeService.validNum(pageBean)!=0){
        	result.setSuccess(false);
    	    result.setMsg("修改失败，数据不合法");
        }else {
            User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
            if(loginUser == null){
				result.setSuccess(false);
			    result.setMsg("操作失败，请先登录");
			} else {
				String create_user = loginUser.getYhMc();
				boolean r = deviceTypeService.updateDeviceType(pageBean, create_user);
	            if(r){
	            	result.setSuccess(true);
	        	    result.setMsg("修改成功");
	            }else {
	            	result.setSuccess(false);
	        	    result.setMsg("修改失败");
	            }
			}
        }
        printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除设备类型
	 * @author minting.he
	 */
	public void deleteDeviceType(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String ids = request.getParameter("ids");
		if(EmptyUtil.atLeastOneIsEmpty(ids)){
			result.setSuccess(false);
    	    result.setMsg("删除失败，数据不合法");
		}else {
			User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
			if(loginUser == null){
				result.setSuccess(false);
			    result.setMsg("操作失败，请先登录");
			} else {
				String create_user = loginUser.getYhMc();
				boolean r = deviceTypeService.deleteDeviceType(ids, create_user);
				if(r){
					result.setSuccess(true);
				    result.setMsg("删除成功");
				}else {
					result.setSuccess(false);
				    result.setMsg("删除失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 获取全部设备类型
	 */
	public void getAllDeviceType(){
		List<DeviceType> list = deviceTypeService.getAllDeviceType();
    	printHttpServletResponse(GsonUtil.toJson(list));
	}
}
