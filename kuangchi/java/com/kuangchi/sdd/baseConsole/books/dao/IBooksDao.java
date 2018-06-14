package com.kuangchi.sdd.baseConsole.books.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.books.model.Books;
import com.kuangchi.sdd.baseConsole.books.model.Param;

public interface IBooksDao {
	/**
	 * Description:新增数据字典
	 * date:2016年3月24日
	 * @param books
	 */
	 void addNewBooks(Books books);
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
	 void deleteBooks(String id);
	 /**
	  * 
	  * Description:根据参数进行数据字典数据查询
	  * date:2016年3月24日
	  * @param param
	  * @param page
	  * @param size
	  * @return
	  */
	 List<Books> getAllBooksByParam(Param param,String page,String size);
	 /**
	  * 
	  * Description:根据数据字典值查询数据
	  * date:2016年3月24日
	  * @param value
	  * @return
	  */
	 List<Books> getBooksByValue(String value);
	 
	 int getAllBooksCount(Param param);
	 
	 List<Books> getWorkBookByNameAndType(String name,String type);
}
