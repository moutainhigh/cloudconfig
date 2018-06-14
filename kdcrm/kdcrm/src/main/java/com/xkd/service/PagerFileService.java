package com.xkd.service;

import com.xkd.mapper.CompanyMapper;
import com.xkd.mapper.PagerFileMapper;
import com.xkd.mapper.ProjectMapper;
import com.xkd.mapper.UserMapper;
import com.xkd.model.Company;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PagerFileService {

	@Autowired
	PagerFileMapper mapper;
	
	@Autowired
	ProjectMapper projectMapper;

	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	UserMapper userMapper;


	public void savePagerFile(Map<String, Object> map) {

	}

	public void changePagerFile(Map<String, String> pagerFile) {
		
		if(pagerFile.get("flag").equals("add")){
			String userId = pagerFile.get("userId");
			if(StringUtils.isBlank(pagerFile.get("ttype"))){
				Map<String,String> pagerFileUser = new HashMap<>();
				pagerFileUser.put("userOrDepartmentId",userId);
				pagerFileUser.put("ttype","1");
				pagerFileUser.put("level","0");
				pagerFileUser.put("pagerFileId",pagerFile.get("id"));
				pagerFileUser.put("id",UUID.randomUUID().toString());
				mapper.savePagerFileUser(pagerFileUser);
			}else{
				Map<String, Object> user = userMapper.selectUserById(userId);
				pagerFile.put("pcCompanyId",user.get("pcCompanyId").toString());
				pagerFile.put("departmentId",user.get("departmentId").toString());

			}
			mapper.savePagerFile(pagerFile);
		}else if(pagerFile.get("flag").equals("edit")){
			mapper.updatePagerFile(pagerFile);
		}
	}
	public void updatePagerFile(String id,String userId) {
		Map<String, String> pagerFile = new HashMap<>();
		pagerFile.put("id", id);
		pagerFile.put("userId", userId);
		mapper.updatePagerFile(pagerFile);
	}
	

	public Map<String, String> getPagerFileByPid(String parentId) {
		
		return mapper.getPagerFileByPid(parentId);
	}

	public Map<String, String> getPagerFileById(String id) {
		return mapper.getPagerFileById(id);
	}

	public void deletePagerFileById(String id,String pagerPath,String fileName) {
		if(StringUtils.isNotBlank(pagerPath)){
			//删除文件夹里面的文件
			mapper.deleteDocumentByPagerFileId(id,pagerPath,fileName);
		}
		
		//删除文件夹
		mapper.deletePagerFileById(id);
		
	}

	public Map<String, String> getDocumentByPidAndName(String pagerFileId, String fileName) {
		return mapper.getDocumentByPidAndName(pagerFileId,fileName);
	}

	public List<Map<String, String>> getPagerFileAndDocumentLikePath(String pagerFileId) {
		return mapper.getPagerFileAndDocumentLikePath(pagerFileId);
	}

	//修改文件夹及其所有子文件的  部门id
	//参数：企业id,部门id,操作人id
	public void editFolderDepartment(String id,String departmentId,String updatedBy) {
		List<Map<String, String>> list = getPagerFileAndDocumentLikePath(id);
		if(list.size()>0){
			List<String> ids = new ArrayList<>();
			for (Map<String, String> obj:list) {
				ids.add(obj.get("id"));
			}
			mapper.editDepartmentFolder(ids,departmentId,updatedBy);
		}
	}

	public List<Map<String, String>> checkFileName(String pagerFileId, List<String> folderlist) {
		return mapper.checkFileName(pagerFileId,folderlist);
	}

	public List<Map<String, String>> getPagerFileByPidAndName(String pagerFileId, String pagerFileName) {
		return mapper.getPagerFileByPidAndName(pagerFileId,pagerFileName);
		
	}

	public List<Map<String, String>> getPagerFileInId(String ids,String departmentId,String userId) {
		return mapper.getPagerFileInId(ids,departmentId,userId);
	}

	//初始化项目方案的内部文件夹
	public void LodingPagerFile(Object projectName, String ttype, String id, String loginUserId, String projectTypeId,String departmentId) {
		Map<String, String> pagerFile = new HashMap<>();
		pagerFile.put("id", id);
		pagerFile.put("flag", "add");
		pagerFile.put("parentId", ttype);
		pagerFile.put("ttype", id);
		pagerFile.put("pagerPath", ttype+"/"+id);
		pagerFile.put("pagerName", projectName+"");
		pagerFile.put("userId", loginUserId);
		pagerFile.put("sysFolder", "1");
		Map<String, Object> user = userMapper.selectUserById(loginUserId);
		pagerFile.put("pcCompanyId",user.get("pcCompanyId").toString());
		pagerFile.put("departmentId",StringUtils.isBlank(departmentId) ? user.get("departmentId").toString():departmentId);
		mapper.savePagerFile(pagerFile);
		FileUtil.newFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/"+pagerFile.get("pagerPath"));

		if(ttype.equals("1") || ttype.equals("2")){
			String pagerList[] = {"准备阶段","调研阶段","辅导阶段","总结阶段","固化回访阶段","其他"};
			pagerFile.put("parentId", id);
			if(projectTypeId.equals("培训")){
				pagerList = new String[]{"学员手册","培训照片","培训反馈结果","评估报告","学员名单","其他"};
			}
			int times = 6;
			for (String value : pagerList) {
				String pagerId = UUID.randomUUID().toString();
				pagerFile.put("pagerName", value);
				pagerFile.put("id", pagerId);
				pagerFile.put("times", (times--)+"");
				pagerFile.put("pagerPath", ttype+"/"+id+"/"+pagerId);
				FileUtil.newFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/"+pagerFile.get("pagerPath"));
				mapper.savePagerFile(pagerFile);
			}
		}
	}

	public void updatePagerFileName(String id, String loginUserId, String name) {
		Map<String, String> pagerFile = mapper.getPagerFileById(id);
		if(pagerFile != null){
			if(StringUtils.isBlank(pagerFile.get("pagerName")) ||!pagerFile.get("pagerName").equals(name)){
				pagerFile.put("pagerName", name);
				pagerFile.put("userId", loginUserId);
				mapper.updatePagerFile(pagerFile);
			}
		}
	}

	//初始化企业内部文件夹
	//ttype __0商机__1基础文档
    public void initializationCompanyFolder(String companyId,String loginUserId) {
		//查看是否有公司文件夹
		if(companyId.contains("__")){
			Map<String, Object> user = userMapper.selectUserById(loginUserId);
			String pcCompanyId = user.get("pcCompanyId").toString();
			companyId = companyId.substring(0,companyId.length()-3);
			String departmentId = user.get("departmentId").toString();

			Map<String, String> companyPagerFile = mapper.getPagerFileById(companyId);
			if (companyPagerFile==null){
				Company companys = companyMapper.selectCompanyInfoById(companyId);

				Map<String, String> company = new HashMap<>();
				company.put("flag", "add");
				company.put("parentId", "0");
				company.put("pagerPath", "0/" + companyId);
				company.put("id", companyId);
				company.put("pagerName", companys.getCompanyName());
				company.put("userId", loginUserId);
				company.put("ttype", companyId);
				company.put("sysFolder","1");
				company.put("pcCompanyId",pcCompanyId);
				company.put("times", "-5");
				company.put("departmentId",departmentId);
				mapper.savePagerFile(company);
				FileUtil.newFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/0/" + companyId);

			}
			String types [] = {"0","1"};
			String typeName [] = {"商机文档","基本文档"};
			Map<String, String> opportunityType = new HashMap<>();
			opportunityType.put("flag", "add");
			opportunityType.put("parentId", companyId);
			opportunityType.put("userId", loginUserId);
			opportunityType.put("ttype", companyId);
			opportunityType.put("sysFolder","1");
			opportunityType.put("pcCompanyId",pcCompanyId);
			opportunityType.put("departmentId",departmentId);
			for (int i = 0; i <types.length ; i++) {
				//构建公司“商机”文件夹Id
				String opportunityTypeId=companyId+"__"+types[i];
				//查看是否有公司下商机类文件夹
				Map<String, String> opportunityPagerFile = mapper.getPagerFileById(opportunityTypeId);
				opportunityType.put("times", (i-2)+"");
				if (opportunityPagerFile==null){
					opportunityType.put("pagerPath", "0/" +companyId+"/" + opportunityTypeId);
					opportunityType.put("id", opportunityTypeId);
					opportunityType.put("pagerName",typeName[i]);

					mapper.savePagerFile(opportunityType);
					FileUtil.newFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/0/"+companyId+"/" + opportunityTypeId);
				}
			}
		}
    }

    public void addBusinessOppotunityFolder(String companyId, String opportunityName, String id, String loginUserId) {
		Map<String, String> company = new HashMap<>();
		company.put("flag", "add");
		company.put("parentId", companyId+"__0");
		company.put("pagerPath", "0/" + companyId+"/"+companyId+"__0/"+id);
		company.put("id", id);
		company.put("pagerName", opportunityName);
		company.put("userId", loginUserId);
		company.put("ttype", id);
		company.put("sysFolder","1");
		Map<String, Object> user = userMapper.selectUserById(loginUserId);
		String pcCompanyId = user.get("pcCompanyId").toString();
		company.put("pcCompanyId",pcCompanyId);
		company.put("departmentId",user.get("departmentId").toString());
		mapper.savePagerFile(company);
		FileUtil.newFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/0/"+companyId+"/" + companyId+"__0/"+id);
    }

	public int getDocumentListTotal(Map<String, Object> documentMap) {
		return  mapper.getDocumentListTotal(documentMap);
	}

	public int getPagerFileListTotal(Map<String, Object> pagerFileMap) {
		return  mapper.getPagerFileListTotal(pagerFileMap);
	}

    public Map<String, List<HashMap<String, Object>>> getDocumentAndPagerFileList(Map<String, Object> documentMap) {
		Map<String, List<HashMap<String, Object>>> mapList = new HashMap<>();
		List<HashMap<String,Object>> all = mapper.getDocumentAndPagerFileList(documentMap);
		List<HashMap<String,Object>> pagerFileIdsList = new ArrayList<>();
		List<HashMap<String,Object>> documentList = new ArrayList<>();

		for (HashMap<String, Object> a:all) {
			if("1".equals(a.get("flag"))){
				pagerFileIdsList.add(a);
			}else{
				documentList.add(a);
			}
		}
		mapList.put("pagerFileIdsList",pagerFileIdsList);
		mapList.put("documentList",documentList);
		return  mapList;

    }

	public List<HashMap<String,Object>> getPagerFileListInIds(List<HashMap<String, Object>> ids, String ttype,Object userId,Object departmentId) {
		if(null == ids || ids.size() ==0){
			return null;
		}
		return  mapper.getPagerFileListInIds(ids,ttype,userId,departmentId);
	}

	public List<HashMap<String,Object>> getMyPagerFileList(Map<String,Object> pareMap) {
		return mapper.getMyPagerFileList(pareMap);
	}

	public void savePagerFileUser(Map<String, String> pagerFileUser) {
		mapper.savePagerFileUser(pagerFileUser);
	}

	public void deleteSetShare(String pagerFileId,String level) {
		mapper.deleteSetShare(pagerFileId,level);
	}

	public Map<String, Object> getUserAndDepartment(String pagerFileId, String level) {

		Map<String, Object> userAndDepartment = new HashMap<>();


		if(level.equals("0")){
			List<Map<String,Object>> departmentList = mapper.getUserAndDepartment(pagerFileId,level);
			userAndDepartment.put("departmentList",departmentList);
		}else{
			List<Map<String,Object>> userList = new ArrayList<>();
			List<Map<String,Object>> disableUserList = new ArrayList<>();
			List<Map<String,Object>> userAndDepartmentList = mapper.getUserAndDepartment(pagerFileId,null);
			for (Map<String,Object> ud:userAndDepartmentList) {
				if (ud.get("level").equals(level)){
					userList.add(ud);
				}else{
					disableUserList.add(ud);
				}
			}
			userAndDepartment.put("userList",userList);
			userAndDepartment.put("disableUserList",disableUserList);
		}


		return  userAndDepartment;
	}



	public int getMyPagerFileListTotal(Map<String,Object> pareMap) {
		return mapper.getMyPagerFileListTotal(pareMap);
	}

}
