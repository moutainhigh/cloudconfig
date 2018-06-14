package com.kuangchi.sdd.elevatorConsole.elevatorReport.action;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoSystem;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.service.PersonInfoSystemService;
import com.kuangchi.sdd.elevatorConsole.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
@Controller("personInfoSystemAction")
public class PersonInfoSystemAction extends BaseActionSupport{

	@Resource(name="personInfoSystemServiceImpl")
	private PersonInfoSystemService personInfoSystemServer;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取系统全部人员名单-Action
	 */
	public void getPersonInfoSystem(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<PersonInfoSystem> grid = new Grid<PersonInfoSystem>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		grid = personInfoSystemServer.getPersonInfoSystem(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27 
	 * @功能描述: 导出系统全部人员名单-Action
	 */
		public void exportPersonInfoSystem() {
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("exportData");
			Map map = GsonUtil.toBean(beanObject, HashMap.class);
			List<PersonInfoSystem> personInfoSystem=personInfoSystemServer.exportPersonInfoSystem(map);
			
			if(personInfoSystem.size()!=0){
				for (PersonInfoSystem personInfoSystems : personInfoSystem) {
					if(null!=personInfoSystems.getStaff_sex() && personInfoSystems.getStaff_sex().equals("0")){
						personInfoSystems.setStaff_sex("女");
					}else if(null!=personInfoSystems.getStaff_sex() && personInfoSystems.getStaff_sex().equals("1")){
						personInfoSystems.setStaff_sex("男");
					}
				}
			}
			String jsonList = GsonUtil.toJson(personInfoSystem);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						colTitleList.add("组织");
						colTitleList.add("员工工号");
						colTitleList.add("员工名称");
						colTitleList.add("性别");
						//colTitleList.add("出生日期");
						colTitleList.add("手机号");
						colTitleList.add("身份证");
						colTitleList.add("地址");
						colTitleList.add("描述");
						
						colList.add("BM_MC");
						colList.add("staff_no");
						colList.add("staff_name");
						colList.add("staff_sex");
						//colList.add("birthdate");
						colList.add("staff_mobile");
						colList.add("papers_num");
						colList.add("staff_address");
						colList.add("staff_remark");
						String[] colTitles = new String[colList.size()];
						String[] cols = new String[colList.size()];
						for (int i = 0; i < colList.size(); i++) {
							cols[i] = colList.get(i);
							colTitles[i] = colTitleList.get(i);
						}
						OutputStream out = null;
						try {
							out = response.getOutputStream();
							response.setContentType("application/x-msexcel");
							String fileName="人员信息表(系统).xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecialCount.exportExcel("人员信息表(系统)", colTitles, cols, list);
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
	
	
}
