package com.kuangchi.sdd.baseConsole.books.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.books.dao.IBooksDao;
import com.kuangchi.sdd.baseConsole.books.model.Books;
import com.kuangchi.sdd.baseConsole.books.model.Param;
import com.kuangchi.sdd.baseConsole.books.service.IBooksService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.log.service.LogService;

@Transactional
@Service("booksServiceImpl")
public class BooksServiceImpl extends BaseServiceSupport implements IBooksService {
	
	@Resource(name="booksDaoImpl")
	private IBooksDao booksDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public int addNewBooks(Books books) {
		int resultCode;// 0 表示新增失败    1 成功
		int count=this.booksDao.getBooksByValue(books.getWordbook_value()).size();
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "数据字典维护");
	    log.put("V_OP_FUNCTION", "新增");
	    log.put("V_OP_ID", books.getCreate_user());
	    log.put("V_OP_MSG", "新增数据字典");
		if(count==0){
			resultCode=1;
			this.booksDao.addNewBooks(books);
			log.put("V_OP_TYPE", "业务");
		}else{
			resultCode=0;
			log.put("V_OP_TYPE", "异常");
		}
        logDao.addLog(log);
        return resultCode;
	}

	@Override
	public void modifyBooks(Books books) {
		this.booksDao.modifyBooks(books);
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "数据字典维护");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", books.getCreate_user());
        log.put("V_OP_TYPE", "业务");
        log.put("V_OP_MSG", "修改数据字典");
        logDao.addLog(log);		
	}

	@Override
	public void deleteBooks(String id,String user_id) {
		this.booksDao.deleteBooks(id);
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "数据字典维护");
        log.put("V_OP_FUNCTION", "删除");
        log.put("V_OP_ID", user_id);
        log.put("V_OP_TYPE", "业务");
        log.put("V_OP_MSG", "删除数据字典");
        logDao.addLog(log);		
	}

	@Override
	public Grid getAllBooksByParam(Param param, String page, String size) {
		int count=this.booksDao.getAllBooksCount(param);
		List<Books> allbooks=this.booksDao.getAllBooksByParam(param, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(allbooks);
		return grid;
	}

	@Override
	public List<Books> getWorkBookByNameAndType(String name, String type) {
 		return booksDao.getWorkBookByNameAndType(name, type);
	}

}
