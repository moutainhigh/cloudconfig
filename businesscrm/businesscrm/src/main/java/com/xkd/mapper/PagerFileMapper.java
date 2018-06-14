package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface PagerFileMapper {

	void savePagerFile(Map<String, String> pagerFile);

	void updatePagerFile(Map<String, String> pagerFile);

	Map<String, String> getPagerFileByPid(@Param("parentId") String parentId);

	Map<String, String> getPagerFileById(@Param("id") String id);

	void deletePagerFileById(@Param("id") String id);
	
	void deleteDocumentByPagerFileId(@Param("id") String id, @Param("pagerPath") String pagerPath, @Param("fileName") String fileName);

	Map<String, String> getDocumentByPidAndName(@Param("pagerFileId") String pagerFileId, @Param("fileName") String fileName);

	List<Map<String, String>> getPagerFileAndDocumentLikePath(@Param("pagerFileId") String pagerFileId);

	List<Map<String, String>> checkFileName(@Param("pagerFileId") String pagerFileId, @Param("folderlist") List<String> folderlist);

	List<Map<String, String>> getPagerFileByPidAndName(@Param("pagerFileId") String pagerFileId, @Param("pagerFileName") String pagerFileName);

	List<Map<String, String>> getPagerFileInId(@Param("ids") String ids,@Param("departmentId") String departmentId,@Param("userId")String userId);

    void editFolder(@Param("idList")List<String> idList);

	void editDepartmentFolder(@Param("idList")List<String> idList,@Param("departmentId") String departmentId,@Param("updatedBy") String updatedBy);

	int getDocumentListTotal(Map<String, Object> documentMap);

    int getPagerFileListTotal(Map<String, Object> pagerFileMap);

    List<HashMap<String,Object>> getDocumentAndPagerFileList(Map<String, Object> documentMap);

	List<HashMap<String,Object>> getPagerFileListInIds(@Param("ids") List<HashMap<String, Object>> ids,@Param("ttype")  String ttype,@Param("userId")  Object userId,@Param("departmentId")Object departmentId);

    List<HashMap<String,Object>> getMyPagerFileList(Map<String,Object> pareMap);

	void savePagerFileUser(Map<String, String> pagerFileUser);

	void deleteSetShare(@Param("pagerFileId")  String pagerFileId,@Param("level") String level);

	List<Map<String,Object>> getUserAndDepartment(@Param("pagerFileId") String pagerFileId,@Param("level") String level);

	int getMyPagerFileListTotal(Map<String,Object> pareMap);

}
