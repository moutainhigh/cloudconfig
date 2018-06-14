package com.kuangchi.sdd.baseConsole.books.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.books.model.Books;
import com.kuangchi.sdd.baseConsole.books.model.Param;
import com.kuangchi.sdd.baseConsole.books.service.IBooksService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
@Controller("booksAction")
public class BooksAction extends BaseActionSupport {

	private static final  Logger LOG = Logger.getLogger(BooksAction.class);
	
	private Books model;
	
	public BooksAction(){
		model=new Books();
	}
	@Resource(name="booksServiceImpl")
	private IBooksService booksService;
	
	@Override
	public Object getModel() {
		return model;
	}
	

	/**
	 * 新增，修改方法
	 */
	
	public void operateBooks(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject=request.getParameter("data");
	 	JsonResult result = new JsonResult();
	 	String flag=request.getParameter("flag");
	 	Books booksPage=GsonUtil.toBean(beanObject,Books.class);

	 	User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		 booksPage.setCreate_user(loginUser.getYhMc());
		 try {
			 if ("add".equals(flag)){
				 	int resultCode=booksService.addNewBooks(booksPage);
				 	if(resultCode==1){
				 		result.setMsg("添加成功");
						result.setSuccess(true);
				 	}else{
				 		result.setMsg("字典值已被用，请修改字典值");
					     result.setSuccess(false);
				 	}
			        
			  }else if("update".equals(flag)) {
				   booksPage.setWordbook_info_id(Integer.valueOf(booksPage.getWordbook_info_id()));
			        booksService.modifyBooks(booksPage);
			        result.setMsg("修改成功");
					result.setSuccess(true);
			   }  
		} catch (Exception e) {
			result.setMsg("操作失败!!!");
		     result.setSuccess(false);
		}
		
	printHttpServletResponse(GsonUtil.toJson(result));
	
	}
	
	/**
	 * 获取所有信息
	 */
	public void getAllBooksByParam(){
		String beanObject = getHttpServletRequest().getParameter("data");//获取前台序列化的数据
		Param param = GsonUtil.toBean(beanObject, Param.class);
		
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		param.setType(param.getType());
		param.setValue(param.getValue());
		param.setBegin_time(param.getBegin_time());
		param.setEnd_time(param.getEnd_time());
		
		Grid<Books> allBooks=booksService.getAllBooksByParam(param, page, rows);
		printHttpServletResponse(GsonUtil.toJson(allBooks));
	}
	
	 //删除字典信息
    public void deleteBooks(){
    	
        HttpServletRequest request = getHttpServletRequest();
        String del_ids=request.getParameter("data_ids");
        User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
        booksService.deleteBooks(del_ids,loginUser.getYhMc());
        JsonResult result = new JsonResult();
        result.setMsg("删除成功");
        result.setSuccess(true);
        printHttpServletResponse(GsonUtil.toJson(result));
    }
	
	//主页面显示
    public String toBooksPage(){
    	
    	return "success";
    }

	

}
