package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.mapper.SolrMapper;
import com.xkd.model.SolrPriorityBean;
import com.xkd.utils.DateUtils;
import com.xkd.utils.SolrUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SolrService {
    Logger log= LoggerFactory.getLogger(SolrService.class);

    @Autowired
    SolrMapper solrMapper;
    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserDataPermissionService userDataPermissionService;
    @Autowired
    DepartmentService departmentService;

    @Autowired
    CompanyRelativeUserService companyRelativeUserService;

    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SolrService.class);

    /**
     * 添加公司索引
     *
     * @param mapList 公司信息列表
     * @return
     */
    private boolean addCompanyIndex(List<Map<String, Object>> mapList) {
        try {

            HttpSolrClient client = SolrUtil.getHttpSolrClient();
            List<SolrInputDocument> list = new ArrayList<SolrInputDocument>();
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> companyMap = mapList.get(i);
                SolrInputDocument solrInputDocument = new SolrInputDocument();
                solrInputDocument.setField("id", (String) companyMap.get("id"));
                solrInputDocument.setField("companyName", (String) companyMap.get("companyName"));
                solrInputDocument.setField("logo", (String) companyMap.get("logo"));
                solrInputDocument.setField("representative", (String) companyMap.get("representative"));
                solrInputDocument.setField("companyId", (String) companyMap.get("id"));
                solrInputDocument.setField("address", (String) companyMap.get("address"));
                solrInputDocument.setField("parentIndustry", (String) companyMap.get("parentIndustry"));
                solrInputDocument.setField("industry", (String) companyMap.get("industry"));
                solrInputDocument.setField("userlevel", (String) companyMap.get("userlevel"));
                solrInputDocument.setField("usertype", (String) companyMap.get("usertype"));
                solrInputDocument.setField("mobile", (String) companyMap.get("mobile"));
                solrInputDocument.setField("uname", (String) companyMap.get("uname"));
                solrInputDocument.setField("province", (String) companyMap.get("province"));
                solrInputDocument.setField("city", (String) companyMap.get("city"));
                solrInputDocument.setField("county", (String) companyMap.get("county"));
                solrInputDocument.setField("channel", (String) companyMap.get("channel"));
                solrInputDocument.setField("dbChangeTime", companyMap.get("dbChangeTime") == null ? "" : companyMap.get("dbChangeTime").toString());
                solrInputDocument.setField("createDate", (String) companyMap.get("createDate"));
                solrInputDocument.setField("companyAdviserName", (String) companyMap.get("companyAdviserName"));
                solrInputDocument.setField("companyDirectorName", (String) companyMap.get("companyDirectorName"));
                solrInputDocument.setField("companyDirectorId", (String) companyMap.get("companyDirectorId"));
                solrInputDocument.setField("companyAdviserId", (String) companyMap.get("companyAdviserId"));
                solrInputDocument.setField("createdBy", (String) companyMap.get("createdBy"));
                solrInputDocument.setField("createdByName", (String) companyMap.get("createdByName"));
                solrInputDocument.setField("attendStatus", (String) companyMap.get("attendStatus"));
                solrInputDocument.setField("moneySituation", (String) companyMap.get("moneySituation"));
                solrInputDocument.setField("communicatStatus", (String) companyMap.get("communicatStatus"));
                solrInputDocument.setField("infoScore", (Integer) companyMap.get("infoScore"));
                solrInputDocument.setField("label", (String) companyMap.get("label"));
                solrInputDocument.setField("departmentId", (String) companyMap.get("departmentId"));
                solrInputDocument.setField("departmentName", (String) companyMap.get("departmentName"));
                solrInputDocument.setField("priority", (String) companyMap.get("priority"));
                solrInputDocument.setField("latestContactTime", (Long) companyMap.get("latestContactTime"));
                solrInputDocument.setField("relativeUserId", (String) companyMap.get("relativeUserId"));
                solrInputDocument.setField("relativeUserName", (String) companyMap.get("relativeUserName"));
                list.add(solrInputDocument);
            }

            client.add(list);
            client.commit();
        } catch (Exception e) {
            log.error("异常栈:",e);
            return false;
        }
        return true;
    }


    /**
     * 根据公司ID列表批量删除索引
     *
     * @param companyIdList 公司Id列表
     * @throws IOException
     * @throws SolrServerException
     */
    public boolean deleteDocumentByCompanyId(List<String> companyIdList) {
        try {

            if (!companyIdList.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("(companyId:").append(companyIdList.get(0)).append(")");

                for (int i = 1; i < companyIdList.size(); i++) {
                    stringBuffer.append(" OR (companyId:").append(companyIdList.get(i)).append(")");
                }
                HttpSolrClient client = SolrUtil.getHttpSolrClient();

                client.deleteByQuery(stringBuffer.toString());
                client.commit();
            }

        } catch (Exception e) {
            log.error("异常栈:",e);
            logger.error("solr删除失败");
            throw new RuntimeException("solr删除失败");
        }
        return true;
    }


    public boolean isRelativePermission(String userId, List<Map<String, Object>> relativeUserList) {
        boolean relativeFlag = false;
        for (int i = 0; i < relativeUserList.size(); i++) { //判断相关人员权限
            if (userId != null && userId.equals(relativeUserList.get(i).get("userId"))) {
                relativeFlag = true;
            }
        }
        return relativeFlag;
    }


    /**
     * @param searchValue         检索值
     * @param industryList        行业列表
     * @param customerTypeList    　客户类型列表
     * @param customerDegreeList  　客户等级列表
     * @param companyName         　公司名称
     * @param contact             　联系人
     * @param province            　省
     * @param city                　市
     * @param county              　县
     * @param mobile              　手机
     * @param channel             　渠道来源
     * @param companyAdviserName  　　公司顾问名称
     * @param companyDirectorName 　　公司总监名称
     * @param orderFlag           　排序规则　　　１　修改时间降序　　　２　修改时间升序　　　３　客户等级降级　　４　客户等级升序　　当不传时　根据searchValue按相关度排序
     * @param infoScore           信息完整度
     * @param loginUserId         登录员工Id
     * @param token               登录员工token
     * @param withDataPermission  是否引入数据权限  1 是
     * @param departmentId        部门Id
     * @param publicFlag          是否是公海客户  1  我负责的客户 2 我参与的客户  3 总监是我的客户  4 公海客户  5 全部客户
     * @param token               登录员工token
     * @param priority            优先级
     * @param pageNum             当前页
     * @param pageSize            每页显示多少条数据
     * @return
     */
    public Map<String, Object> queryCompany(String searchValue, List<String> industryList,
                                            List<String> customerTypeList, List<String> customerDegreeList, String companyName, String contact,
                                            String province, String city, String county, String mobile, String channel,
                                            String companyAdviserName, String companyDirectorName, String orderFlag, Integer infoScore,
                                             String loginUserId, String token, Integer withDataPermission,
                                            String departmentId, Integer publicFlag, String priority, int pageNum,
                                            int pageSize) {


        searchValue = StringUtils.isBlank(searchValue) ? null : searchValue;
        companyName = StringUtils.isBlank(companyName) ? null : companyName;
        contact = StringUtils.isBlank(contact) ? null : contact;
        province = StringUtils.isBlank(province) ? null : province;
        city = StringUtils.isBlank(city) ? null : city;
        county = StringUtils.isBlank(county) ? null : county;
        mobile = StringUtils.isBlank(mobile) ? null : mobile;
        channel = StringUtils.isBlank(channel) ? null : channel;
        companyAdviserName = StringUtils.isBlank(companyAdviserName) ? null : companyAdviserName;
        companyDirectorName = StringUtils.isBlank(companyDirectorName) ? null : companyDirectorName;
        orderFlag = StringUtils.isBlank(orderFlag) ? null : orderFlag;
        loginUserId = StringUtils.isBlank(loginUserId) ? null : loginUserId;
        token = StringUtils.isBlank(token) ? null : token;
        departmentId = StringUtils.isBlank(departmentId) ? null : departmentId;
        priority = StringUtils.isBlank(priority) ? null : priority;


        if (pageNum < 1) {
            pageNum = 1;
        }

        Map<String, Object> user = userService.selectUserById(loginUserId);

        // 结果集
        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap.put("totalPage", 0);
        resultMap.put("code", "0");
        resultMap.put("note", "SUCCESS");
        resultMap.put("pageNo", 1);
        resultMap.put("pageSize", 0);
        resultMap.put("totalCount", 0);
        resultMap.put("totalRows", 0);
        resultMap.put("data", new ArrayList<Map<String, Object>>());


        /**
         * 设置优先级问题   ，该优先级用于在搜索值匹配到多个字段时，以哪个字段为准，数值越大，说明越是我们想要的匹配结果
         */
        Integer companyNamePriority = 11;

        Integer companyAdviserNamePriority = 10;


        try {

            HttpSolrClient client = SolrUtil.getHttpSolrClient();
            SolrQuery solrQuery = new SolrQuery();

            // 开始条数
            Integer start = ((pageNum - 1) * pageSize);

            // 构建搜索条件
            StringBuffer stringBuffer = new StringBuffer();

            if (StringUtils.isBlank(searchValue) && industryList.size() == 0 && customerDegreeList.size() == 0
                    && customerTypeList.size() == 0 && StringUtils.isBlank(companyName) && StringUtils.isBlank(contact)
                    && StringUtils.isBlank(province) && StringUtils.isBlank(city)
                    && StringUtils.isBlank(county) && StringUtils.isBlank(mobile) && StringUtils.isBlank(channel)
                    && StringUtils.isBlank(companyAdviserName) && StringUtils.isBlank(companyDirectorName)
                    && infoScore == null  && (null == withDataPermission || withDataPermission == 0)
                    && StringUtils.isBlank(departmentId) && (null == publicFlag) && StringUtils.isBlank(priority)) {
                // 如果没有任何搜索条件，则查询全部值的前pageSize条
                stringBuffer.append("(")
                        .append("*:").append("*")
                        .append(")");
                solrQuery.set("q", stringBuffer.toString());

            } else {

                // 如果有搜索关键字
                if (!StringUtils.isBlank(searchValue)) {

                    searchValue = searchValue.replace("(", " ").replace(")", " ");

                    searchValue = searchValue.trim();

                    /*
                     * 注意，这里我们使用了^符号用来设置权重 ，
					 */
                    stringBuffer.append("(");

                    String[] searchValues = searchValue.split("\\s+");


                    stringBuffer
                            .append("(").append("searchValue:").append("*").append(searchValues[0]).append("*").append("^").append(100)
                            .append(")");

                    for (int i = 1; i < searchValues.length; i++) {
                        stringBuffer.append(" AND ")
                                .append("(").append("searchValue:").append("*").append(searchValues[i]).append("*").append("^").append(100)
                                .append(")");
                    }

                    stringBuffer.append(")");

                } else {
                    stringBuffer.append("(")
                            .append("*:").append("*")
                            .append(")");
                }
                if (withDataPermission != null && 1 == withDataPermission) { //如果开启数据权限
                    List<String> departmentIdList = new ArrayList<>(0);
                    stringBuffer.append(" AND (");
                    departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId, loginUserId);


                    if (departmentIdList.size() > 0) {
                        stringBuffer.append("departmentId:").append(departmentIdList.get(0));
                        for (int i = 1; i < departmentIdList.size(); i++) {
                            stringBuffer.append(" OR ").append("departmentId:").append(departmentIdList.get(i));
                        }
                    } else {
                        //如果没有有权限的部门则查不到
                        stringBuffer.append("departmentId:").append("-1");
                    }
                    stringBuffer.append(")");
                }
                solrQuery.set("q", stringBuffer.toString());


                if (infoScore != null) {
                    StringBuffer fq = new StringBuffer("");
                    fq.append("infoScore:[0 TO " + infoScore + "]");
                    solrQuery.addFilterQuery(fq.toString());
                }
                // 如果有行业条件
                if (industryList.size() > 0) {
                    StringBuffer fq = new StringBuffer("parentIndustryName:").append(industryList.get(0));
                    for (int i = 1; i < industryList.size(); i++) {
                        fq.append(" OR ").append("parentIndustryName:").append(industryList.get(i));
                    }
                    solrQuery.addFilterQuery(fq.toString());
                }
                // 如果有客户类型条件
                if (customerTypeList.size() > 0) {
                    StringBuffer fq = new StringBuffer("usertype:").append(customerTypeList.get(0));
                    for (int i = 1; i < customerTypeList.size(); i++) {
                        fq.append(" OR ").append("usertype:").append(customerTypeList.get(i));
                    }
                    solrQuery.addFilterQuery(fq.toString());
                }
                // 如果有客户等级条件
                if (customerDegreeList.size() > 0) {
                    StringBuffer fq = new StringBuffer("userlevel:").append(customerDegreeList.get(0));
                    for (int i = 1; i < customerDegreeList.size(); i++) {
                        fq.append(" OR ").append("userlevel:").append(customerDegreeList.get(i));
                    }
                    solrQuery.addFilterQuery(fq.toString());

                }


                // 如果省不为空
                if (!StringUtils.isBlank(province)) {
                    StringBuffer fq = new StringBuffer("province:").append(province);
                    solrQuery.addFilterQuery(fq.toString());
                }

                // 如果市不为空
                if (!StringUtils.isBlank(city)) {
                    StringBuffer fq = new StringBuffer("city:").append(city);
                    solrQuery.addFilterQuery(fq.toString());
                }

                // 如果县不为空
                if (!StringUtils.isBlank(county)) {
                    StringBuffer fq = new StringBuffer("county:").append(county);
                    solrQuery.addFilterQuery(fq.toString());
                }


                // 如果渠道不为空
                if (!StringUtils.isBlank(channel)) {
                    StringBuffer fq = new StringBuffer("channel:").append(channel);
                    solrQuery.addFilterQuery(fq.toString());
                }


                //如果公司顾问不为空
                if (!StringUtils.isBlank(companyAdviserName)) {
                    StringBuffer fq = new StringBuffer("companyAdviserName:").append(companyAdviserName);
                    solrQuery.addFilterQuery(fq.toString());
                }

                //如果公司总监不为空
                if (!StringUtils.isBlank(companyDirectorName)) {
                    StringBuffer fq = new StringBuffer("companyDirectorName:").append(companyDirectorName);
                    solrQuery.addFilterQuery(fq.toString());
                }

                if (publicFlag!=null) {
                    if (publicFlag == 1) {
                        //我负责的客户
                        StringBuffer fq = new StringBuffer("");
                        fq.append("companyAdviserId:").append("【" + loginUserId + "】");
                        solrQuery.addFilterQuery(fq.toString());
                    } else if (publicFlag == 2) {
                        //我参与的客户
                        StringBuffer fq = new StringBuffer("");
                        fq.append("relativeUserId:").append("*【" + loginUserId + "】*");
                        solrQuery.addFilterQuery(fq.toString());
                    } else if (publicFlag == 3) {
                        //总监是我的客户
                        StringBuffer fq = new StringBuffer("");
                        fq.append("companyDirectorId:").append("【" + loginUserId + "】");
                        solrQuery.addFilterQuery(fq.toString());
                    } else if (publicFlag == 4) {
                        //公海客户
                        StringBuffer fq = new StringBuffer("");
                        fq.append("(!companyAdviserId:*)");
                        solrQuery.addFilterQuery(fq.toString());
                    } else if (publicFlag == 5 || publicFlag == null) {
                        //全部客户,什么也不做

                    }
                }else {
                    //默认全部客户，什么也不做
                }
                //优先级
                if (!StringUtils.isBlank(priority)) {
                     StringBuffer fq = new StringBuffer("");
                    fq.append("priority: " + priority);
                    solrQuery.addFilterQuery(fq.toString());
                }


            }

            String prefix = "<span style='color: red'>";
            String affix = "</span>";
            // 从哪一行开始
            solrQuery.set("start", start);
            // 每天同显示多少
            solrQuery.set("rows", pageSize);


            if ("1".equals(orderFlag)) { //1按修改时间降序
                solrQuery.set("sort", "dbChangeTime desc");
            } else if ("2".equals(orderFlag)) {
                solrQuery.set("sort", "dbChangeTime asc");
            } else if ("3".equals(orderFlag)) {//3按客户等级降序
                solrQuery.set("sort", "userlevel desc");
            } else if ("4".equals(orderFlag)) {
                solrQuery.set("sort", "userlevel asc");
            }
            if (StringUtils.isBlank(searchValue) && "0".equals(orderFlag)) {//如果什么值都不输入，则默认按时间降序
                solrQuery.set("sort", "dbChangeTime desc");
            }

            QueryResponse response = client.query(solrQuery, SolrRequest.METHOD.POST);
//            Map<String, List<Map<String, String>>> facetList = getFacets(solrQuery);


            List<SolrDocument> resultList = response.getResults();


            List<Map<String, Object>> companyList = new ArrayList<>();


            for (int i = 0; i < resultList.size(); i++) {
                SolrDocument solrDocument = resultList.get(i);
                String id = (String) solrDocument.get("id");
                Map<String, Object> map = JSON.parseObject(JSON.toJSON(solrDocument).toString(),
                        new TypeReference<Map<String, Object>>() {
                        });
                companyList.add(map);
            }


            // 总条数
            Integer totalNum = Double.valueOf(response.getResults().getNumFound()).intValue();


            //查询相关人员
            List<String> companyIds = new ArrayList<>();
            for (int i = 0; i < companyList.size(); i++) {
                Map<String, Object> data = companyList.get(i);
                companyIds.add((String) data.get("companyId"));
            }

            List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIds);
            //整理数据，将相关人员整合到相应的企业记录中去
            for (int i = 0; i < companyList.size(); i++) {
                Map<String, Object> data = companyList.get(i);
                List<Map<String, Object>> relUserList = new ArrayList<>();
                data.put("relativeUserList", relUserList);
                for (int j = 0; j < relativeList.size(); j++) {
                    if (relativeList.get(j).get("companyId").equals(data.get("companyId"))) {
                        relUserList.add(relativeList.get(j));
                    }
                }
            }

            for (int i = 0; i < companyList.size(); i++) {
                Map<String, Object> companyMap = companyList.get(i);
                String id = (String) companyMap.get("id");
                List<SolrPriorityBean> hitColumnList = new ArrayList<>();


                {
                    companyMap.put("companyNameWithoutHightlist", companyMap.get("companyName"));
                    // 公司名称
                    String hs = (String) companyMap.get("companyName");
                    if (!StringUtils.isBlank(searchValue)) {
                        String[] searchValues = searchValue.split("\\s+");
                        if (!StringUtils.isBlank(hs)) {
                            boolean flag = true;
                            for (int j = 0; j < searchValues.length; j++) {
                                if (!hs.contains(searchValues[j])) {
                                    flag = false;
                                }
                                hs = hs.replaceAll(searchValues[j], prefix + searchValues[j] + affix);
                            }
                            if (flag) {
                                hitColumnList.add(new SolrPriorityBean(companyNamePriority, "公司名称", hs));

                            }
                        }
                        companyMap.put("companyName", hs);
                    }

                }


                {
                    // 负责人
                    String hs = (String) companyMap.get("companyAdviserName");
                    if (!StringUtils.isBlank(searchValue)) {
                        String[] searchValues = searchValue.split("\\s+");
                        if (!StringUtils.isBlank(hs)) {
                            boolean flag = true;
                            for (int j = 0; j < searchValues.length; j++) {
                                if (!hs.contains(searchValues[j])) {
                                    flag = false;
                                }
                                hs = hs.replaceAll(searchValues[j], prefix + searchValues[j] + affix);
                            }
                            if (flag) {
                                hitColumnList.add(new SolrPriorityBean(companyAdviserNamePriority, "负责人", hs));
                            }
                        }
                        companyMap.put("companyAdviserName", hs);

                    }

                }


                //取优先级高的检索作为碰撞值
                Collections.sort(hitColumnList);
                if (hitColumnList.size() > 0) {
                    companyMap.put("hitColumn", hitColumnList.get(hitColumnList.size() - 1).getHitColumn());
                    companyMap.put("hitValue", hitColumnList.get(hitColumnList.size() - 1).getHitValue());
                }


                boolean flag = true;
                /**
                 * 如果即没有总监，也没有顾问，则默认所有人都可以看
                 */
                if ((StringUtils.isBlank((String) companyMap.get("companyAdviserId"))
                        && StringUtils.isBlank((String) companyMap.get("companyDirectorId")))) {
                    flag = true;
                } else if (loginUserId == null ||
                        (!userService.hasPrivatePermission(token, "company/private")
                                && !"1".equals(user.get("roleId"))
                                && (null == user.get("isAdmin") || (null != user.get("isAdmin") && 1 != (Integer) user.get("isAdmin")))
                                && !("【" + loginUserId + "】").equals(companyMap.get("companyAdviserId"))
                                && !("【" + loginUserId + "】").equals(companyMap.get("companyDirectorId"))
                                && !loginUserId.equals(companyMap.get("createdBy"))
                                && !isRelativePermission(loginUserId, (List) companyMap.get("relativeUserList")))) {
                    flag = false;
                }


                //过滤敏感信息  超级管理员，顾问，总监，创建人,相关人员 可以查看敏感信息，其它人不能查看敏感信息
                if (!flag) {
                    companyMap.put("mobile", StringUtils.isBlank((String) companyMap.get("mobile")) ? "" : "***");
                    companyMap.put("phone", StringUtils.isBlank((String) companyMap.get("phone")) ? "" : "***");

                }

                if (companyMap.get("companyAdviserId") != null) {
                    companyMap.put("companyAdviserId", ((String) companyMap.get("companyAdviserId")).replaceAll("【", "").replaceAll("】", ""));
                }
                if (companyMap.get("companyDirectorId") != null) {
                    companyMap.put("companyDirectorId", ((String) companyMap.get("companyDirectorId")).replaceAll("【", "").replaceAll("】", ""));
                }


            }


            Integer totalPage = totalNum % pageSize == 0 ? (totalNum / pageSize) : (totalNum / pageSize + 1);

            resultMap.put("totalPage", totalPage);
            resultMap.put("code", "0");
            resultMap.put("note", "SUCCESS");
            resultMap.put("pageNo", pageNum);
            resultMap.put("pageSize", pageSize);
            resultMap.put("totalCount", totalNum);
            resultMap.put("totalRows", totalNum);
            resultMap.put("data", companyList);
//            resultMap.put("facetList", facetList);

        } catch (Exception e) {
            log.error("异常栈:",e);
            resultMap.put("totalPage", 0);
            resultMap.put("code", "1");
            resultMap.put("note", "ERROR");
            resultMap.put("pageNo", 0);
            resultMap.put("pageSize", pageSize);
            resultMap.put("totalCount", 0);
            resultMap.put("totalRows", 0);
            resultMap.put("facetList", new HashMap<>(1));
            resultMap.put("data", new ArrayList());
        }
        return resultMap;
    }


    /**
     * 获取搜索分类条目,该方法用于提取分类条目，这些条目可以用于页面检索的分类选项
     *
     * @return
     */
    private Map<String, List<Map<String, String>>> getFacets(SolrQuery solrQuery) {

        try {
            HttpSolrClient client = SolrUtil.getHttpSolrClient();
            solrQuery.setFacet(true);
            String keyParentIndustryName = "parentIndustryName";
            String keyUserLevel = "userlevel";
            String qiTa = "其它";
            // 添加分面字段，分页字段分别为行业，客户类型，客户等级
            solrQuery.addFacetField(keyParentIndustryName,  keyUserLevel);
            solrQuery.setFacetMinCount(1);
            QueryResponse response = client.query(solrQuery, SolrRequest.METHOD.POST);
            List<FacetField> facetList = response.getFacetFields();
            Map<String, List<Map<String, String>>> map = new HashMap();
			/*
			 * 提取分面字段结果
			 */
            for (int i = 0; i < facetList.size(); i++) {
                List<Map<String, String>> list = new ArrayList();
                for (int j = 0; j < facetList.get(i).getValues().size(); j++) {
                    String str = facetList.get(i).getValues().get(j).toString();
                    Integer index = str.indexOf("(");
                    String title = str.substring(0, index - 1);
                    String countStr = str.substring(index, str.length());
                    Map<String, String> m = new HashMap();
                    if (!StringUtils.isBlank(title)) {
                        title = title.trim();
                        m.put("value", title);
                        if (!StringUtils.isBlank(countStr)) {
                            m.put("count", countStr.trim());
                        }
                        list.add(m);
                    }

                }
                map.put(facetList.get(i).getName(), list);
            }


            /**
             * 将“其它” 项放到最后
             */
            List<Map<String, String>> orderedParentIndustryNameList = new ArrayList<>();
            List<Map<String, String>> orderedUserlevelList = new ArrayList<>();
            for (int i = 0; i < map.get(keyParentIndustryName).size(); i++) {
                if (!qiTa.equals(map.get(keyParentIndustryName).get(i).get("value"))) {
                    orderedParentIndustryNameList.add(map.get(keyParentIndustryName).get(i));
                }

            }
            for (int i = 0; i < map.get(keyParentIndustryName).size(); i++) {
                if (qiTa.equals(map.get(keyParentIndustryName).get(i).get("value"))) {
                    orderedParentIndustryNameList.add(map.get(keyParentIndustryName).get(i));
                }
            }


            map.put(keyParentIndustryName, orderedParentIndustryNameList);


            for (int i = 0; i < map.get(keyUserLevel).size(); i++) {
                if (!qiTa.equals(map.get(keyUserLevel).get(i).get("value"))) {
                    orderedUserlevelList.add(map.get(keyUserLevel).get(i));
                }
            }
            for (int i = 0; i < map.get(keyUserLevel).size(); i++) {
                if (qiTa.equals(map.get(keyUserLevel).get(i).get("value"))) {
                    orderedUserlevelList.add(map.get(keyUserLevel).get(i));
                }
            }
            map.put(keyUserLevel, orderedUserlevelList);

            return map;

        } catch (Exception e) {
            log.error("异常栈:",e);
        }
        return new HashMap<String, List<Map<String, String>>>();
    }


    /**
     * 通过Id，到数据库中查找索引数据，并在Solr中建立索引
     *
     * @param idList 主键Id 列表
     */
    public boolean updateIndex(List<String> idList) {

        boolean result = false;

        List<Map<String, Object>> list = solrMapper.selectCompanyIndexInfoByIds(idList);
        if (!list.isEmpty()) {
            result = addCompanyIndex(list);
        } else {
            result = true;
        }


        if (!result) {
            logger.error("更新solr失败");
            throw new RuntimeException("solr更新失败");
        }

        return result;

    }


    /**
     * 更新与该公司相关的所有索引
     *
     * @param companyId 公司Id
     */
    public void updateCompanyIndex(String companyId) {
        List<String> companyIds = new ArrayList<>();
        companyIds.add(companyId);
        //先删除与企业相关的所有索引
        deleteDocumentByCompanyId(companyIds);

        //添加企业索引
        if (companyIds.size() > 0) {
            updateIndex(companyIds);
        }

    }


    /**
     * 批量更新公司索引
     *
     * @param companyIdList 公司Id列表
     */
    public void updateCompanyIndex(List<String> companyIdList) {
        if (companyIdList.size() > 0) {
            deleteDocumentByCompanyId(companyIdList);
            updateIndex(companyIdList);
        }

    }

}

