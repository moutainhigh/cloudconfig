package com.kuangchi.sdd.baseConsole.books.service;


import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.books.model.Books;
import com.kuangchi.sdd.baseConsole.books.model.Param;

public interface IBooksService {
	/**
	 * Description:新增数据字典
	 * date:2016年3月24日
	 * @param books
	 */
	 int addNewBooks(Books books);
	 /**
	  * 
	  * Description:修改数据字典
	  * date:2016年3月24日
	  * @param books
	  */
	 void modifyBooks(Books books);
	 /**
	  * 
	  * Description:根据ID删除数据字典数据
	  * date:2016年3月24日
	  * @param id
	  */
	 void deleteBooks(String id,String user_id);
	 /**
	  * 
	  * Description:根据参数进行数据字典数据查询
	  * date:2016年3月24日
	  * @param param
	  * @param page
	  * @param size
	  * @return
	  */
	 Grid getAllBooksByParam(Param param,String page,String size);
	 
	 List<Books> getWorkBookByNameAndType(String name,String type);
}
