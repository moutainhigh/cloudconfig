package com.xkd.controller;

import com.xkd.model.*;
import com.xkd.model.vo.DemoBean;
import com.xkd.service.CompanyRelativeUserService;
import com.xkd.service.CompanyService;
import com.xkd.service.SolrService;
import com.xkd.utils.ExcelUtilSpecial;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建人:巫建辉
 * 创建时间：2017-8-18
 * 功能描述：Sorl相关的接口
 */

@Api(description = "搜索企业，下载企业，查询下载状态")
@RestController
public class SolrController extends BaseController {
    @Autowired
    SolrService solrService;
    @Autowired
    CompanyService companyService;

    ConcurrentHashMap<String, Object> downloadStatusMap = new ConcurrentHashMap<>();


    @Autowired
    CompanyRelativeUserService companyRelativeUserService;

    /**
     * @param req
     * @param queryContent        检索值
     * @param industryId          行业数组
     * @param userType            客户类型数组
     * @param userLevel           客户等级数组
     * @param companyName         公司名称
     * @param uname               联系人
     * @param manageScope         经营范围
     * @param province            省
     * @param city                市
     * @param county              县
     * @param mobile              联系人电话
     * @param channel             渠道来源
     * @param companyAdviserName  顾问名称
     * @param companyDirectorName 总监名称
     * @param orderFlag           排序规则    1 修改时间降序   2 修改时间升序  3 客户等级降序  4 客户等级升序   如果不传 结合queryContent 默认按相关度排序
     * @param showOnlyLogIn       是否是显示登录人员的数据   1 是
     * @param attendStatus        参会状态  已参会  未参会
     * @param communicatStatus    沟通状态  已沟通 未沟通
     * @param moneySituation      完款状态    定金  全款
     * @param responsibleUserId   负责人ID
     * @param currentPage         当前页
     * @param pageSize            每页显示多少条
     * @return
     */
    @ApiOperation(value = "查询企业信息")
    @ResponseBody
    @RequestMapping(value = "/solr/company/search", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter search(HttpServletRequest req,
                                   @ApiParam(value = "检索值",required = false) @RequestParam(required = false)  String queryContent,
                                   @ApiParam(value = "行业数组",required = false)@RequestParam (required = false)  String[] industryId,
                                   @ApiParam(value = "客户类型数组",required = false)@RequestParam(required = false)   String[] userType,
                                   @ApiParam(value = "客户类型数组",required = false)@RequestParam(required = false)   String[] userLevel,
                                   @ApiParam(value = "公司名称",required = false)@RequestParam(required = false)   String companyName,
                                   @ApiParam(value = "联系人",required = false) @RequestParam(required = false)  String uname,
                                   @ApiParam(value = "经营范围 弃用",required = false) @RequestParam(required = false)  String manageScope,
                                   @ApiParam(value = "省",required = false)@RequestParam(required = false)   String province,
                                   @ApiParam(value = "市 ",required = false)@RequestParam(required = false)   String city,
                                   @ApiParam(value = "区",required = false)@RequestParam(required = false)   String county,
                                   @ApiParam(value = "手机",required = false) @RequestParam(required = false)  String mobile,
                                   @ApiParam(value = "渠道来源",required = false)@RequestParam(required = false)   String channel,
                                   @ApiParam(value = "顾问",required = false)@RequestParam(required = false)   String companyAdviserName,
                                   @ApiParam(value = "总监",required = false)@RequestParam(required = false)   String companyDirectorName,
                                   @ApiParam(value = "排序规则    1 修改时间降序   2 修改时间升序  3 客户等级降序  4 客户等级升序   如果不传 结合queryContent 默认按相关度排序",required = false) @RequestParam(required = false)  String orderFlag,
                                   @ApiParam(value = "是否是显示登录人员的数据   1 是",required = false) @RequestParam(required = false)  String showOnlyLogIn,
                                   @ApiParam(value = "参会状态  已参会  未参会",required = false)@RequestParam(required = false)   String attendStatus,
                                   @ApiParam(value = "沟通状态 未沟通 已沟通",required = false)@RequestParam(required = false)   String communicatStatus,
                                   @ApiParam(value = "完款状态 已完款 未完款",required = false) @RequestParam(required = false)  String moneySituation,
                                   @ApiParam(value = "负责人Id",required = false) @RequestParam(required = false)  String responsibleUserId,
                                   @ApiParam(value = "是否引入数据部门权限  0  否  1  是",required = false) @RequestParam(required = false)  Integer withDataPermission,
                                   @ApiParam(value = "部门Id 如果引入数据权限，此部门Id需要传值",required = false) @RequestParam(required = false)  String departmentId,
                                   @ApiParam(value = "是否是公海客户 1 公司客户 0 我的客户 不传时表示所有客户",required = false) @RequestParam(required = false)  Integer publicFlag,
                                   @ApiParam(value = "指定范围 1 我负责的客户 2 与我相关的客户 3 指定人员客户 不传时表示全部",required = false) @RequestParam(required = false)  Integer scopeFlag,
                                   @ApiParam(value = "特殊参数，用于首页A类客户沟通状态跳转  1  已沟通  2 未沟通",required = false) @RequestParam(required = false)  Integer extraFlag,
                                   @ApiParam(value = "优先级",required = false) @RequestParam(required = false)  String priority,
                                   @ApiParam(value = "当前页码",required = true) @RequestParam(required = true) Integer currentPage,
                                   @ApiParam(value = "每页多少条数据",required = true) @RequestParam(required = true) Integer pageSize) {
        //登录员工Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        String token = req.getParameter("token");
        List<String> industryList = new ArrayList<>();
        List<String> customerTypeList = new ArrayList();
        List<String> customerDegreeList = new ArrayList();


        if (!StringUtil.isBlank(companyName)) {
            companyName = companyName.replaceAll("\\*", "");
        }

        if (industryId != null && industryId.length > 0) {
            for (int i = 0; i < industryId.length; i++) {
                industryList.add(industryId[i]);
            }
        }

        if (userType != null && userType.length > 0) {
            for (int i = 0; i < userType.length; i++) {
                customerTypeList.add(userType[i]);
            }
        }
        if (userLevel != null && userLevel.length > 0) {
            for (int i = 0; i < userLevel.length; i++) {
                customerDegreeList.add(userLevel[i]);
            }
        }


        try {
            Map<String, Object> map = solrService.queryCompany(queryContent, industryList, customerTypeList,
                    customerDegreeList, companyName, uname, manageScope, province, city, county, mobile, channel, companyAdviserName, companyDirectorName, orderFlag, showOnlyLogIn, attendStatus, communicatStatus, moneySituation, null, responsibleUserId, loginUserId, token,withDataPermission,departmentId,publicFlag,scopeFlag,extraFlag,priority, currentPage, pageSize);

            ResponseDbCenter responseCenter = ResponseConstants.SUCCESS;
            responseCenter.setTotalRows(map.get("totalRows").toString());
            responseCenter.setResModel(map.get("data"));
            responseCenter.setResExtra(map.get("facetList"));

            return responseCenter;
        } catch (Exception e) {


            e.printStackTrace();
            ResponseDbCenter responseCenter = ResponseConstants.FUNC_MODULE_SERVERERROR;
            responseCenter.setResModel(new ArrayList());
            return responseCenter;
        }

    }


    /**
     * @param req
     * @param queryContent        检索值
     * @param industryId          行业数组
     * @param userType            客户类型数组
     * @param userLevel           客户等级数组
     * @param companyName         公司名称
     * @param uname               联系人
     * @param manageScope         经营范围
     * @param province            省
     * @param city                市
     * @param county              县
     * @param mobile              联系人电话
     * @param channel             渠道来源
     * @param companyAdviserName  顾问名称
     * @param companyDirectorName 总监名称
     * @param orderFlag           排序规则    1 修改时间降序   2 修改时间升序  3 客户等级降序  4 客户等级升序   如果不传 结合queryContent 默认按相关度排序
     * @param showOnlyLogIn       是否是显示登录人员的数据   1 是
     * @param attendStatus        参会状态  已参会  未参会
     * @param communicatStatus    沟通状态  已沟通 未沟通
     * @param moneySituation      完款状态    定金  全款
     * @param responsibleUserId   负责人ID
     * @param downloadId          下载Id ，该值由前端传入
     */
    @ApiOperation(value = "下载企业信息")
    @ResponseBody
    @RequestMapping(value = "/solr/company/downLoad", method = {RequestMethod.GET, RequestMethod.POST})
    public void downLoad(HttpServletRequest req, HttpServletResponse resp,
                         @ApiParam(value = "检索值",required = false)@RequestParam(required = false)   String queryContent,
                         @ApiParam(value = "行业数组",required = false) @RequestParam(required = false)  String[] industryId,
                         @ApiParam(value = "客户类型数组",required = false)@RequestParam(required = false)   String[] userType,
                         @ApiParam(value = "客户等级",required = false)@RequestParam(required = false)   String[] userLevel,
                         @ApiParam(value = "公司名称",required = false)@RequestParam(required = false)   String companyName,
                         @ApiParam(value = "联系人",required = false)@RequestParam (required = false)  String uname,
                         @ApiParam(value = "经营范围",required = false) @RequestParam(required = false)  String manageScope,
                         @ApiParam(value = "省",required = false) @RequestParam(required = false)  String province,
                         @ApiParam(value = "市",required = false) @RequestParam(required = false)  String city,
                         @ApiParam(value = "县",required = false) @RequestParam(required = false)  String county,
                         @ApiParam(value = "手机",required = false) @RequestParam(required = false)  String mobile,
                         @ApiParam(value = "渠道来源",required = false) @RequestParam(required = false)  String channel,
                         @ApiParam(value = "顾问",required = false)@RequestParam(required = false)   String companyAdviserName,
                         @ApiParam(value = "总监",required = false)@RequestParam(required = false)   String companyDirectorName,
                         @ApiParam(value = "排序规则    1 修改时间降序   2 修改时间升序  3 客户等级降序  4 客户等级升序   如果不传 结合queryContent 默认按相关度排序",required = false)@RequestParam(required = false)   String orderFlag,
                         @ApiParam(value = "是否是显示登录人员的数据   1 是",required = false) @RequestParam(required = false)  String showOnlyLogIn,
                         @ApiParam(value = "参会状态  已参会  未参会",required = false)@RequestParam(required = false)   String attendStatus,
                         @ApiParam(value = "沟通状态 未沟通 已沟通",required = false) @RequestParam(required = false)  String communicatStatus,
                         @ApiParam(value = "完款状态 已完款 未完款",required = false) @RequestParam(required = false)  String moneySituation,
                         @ApiParam(value = "负责人Id",required = false) @RequestParam (required = false) String responsibleUserId,
                         @ApiParam(value = "是否引入数据部门权限  0  否  1  是",required = false) @RequestParam(required = false)  Integer withDataPermission,
                         @ApiParam(value = "部门Id 如果引入数据权限，此部门Id需要传值",required = false) @RequestParam(required = false)  String departmentId,
                         @ApiParam(value = "是否是公海客户 1 公司客户 0 我的客户 不传时表示所有客户",required = false) @RequestParam(required = false)  Integer publicFlag,
                         @ApiParam(value = "指定范围 1 我负责的客户 2 与我相关的客户 3 指定人员客户 不传时表示全部",required = false) @RequestParam(required = false)  Integer scopeFlag,
                         @ApiParam(value = "特殊参数，用于首页A类客户沟通状态跳转  1  已沟通  2 未沟通",required = false) @RequestParam(required = false)  Integer extraFlag,
                         @ApiParam(value = "优先级",required = false) @RequestParam(required = false)  String priority,
                         @ApiParam(value = "下载Id ，该值由前端传入",required = true) @RequestParam(required = true) String downloadId) throws Exception {
        if (downloadId != null) {
            downloadStatusMap.put(downloadId, 1);  //0表示正在下载
        }
        String token = req.getParameter("token");

        //try {

            //登录员工Id
            String loginUserId = (String) req.getAttribute("loginUserId");


            List<String> industryList = new ArrayList();
            List<String> customerTypeList = new ArrayList();
            List<String> customerDegreeList = new ArrayList();


            if (!StringUtil.isBlank(companyName)) {
                companyName = companyName.replaceAll("\\*", "");
            }

            if (industryId != null && industryId.length > 0) {
                for (int i = 0; i < industryId.length; i++) {
                    industryList.add(industryId[i]);
                }
            }

            if (userType != null && userType.length > 0) {
                for (int i = 0; i < userType.length; i++) {
                    customerTypeList.add(userType[i]);
                }
            }
            if (userLevel != null && userLevel.length > 0) {
                for (int i = 0; i < userLevel.length; i++) {
                    customerDegreeList.add(userLevel[i]);
                }
            }


            Map<String, Object> map = solrService.queryCompany(queryContent, industryList, customerTypeList,
                    customerDegreeList, companyName, uname, manageScope, province, city, county, mobile, channel, companyAdviserName, companyDirectorName, orderFlag, showOnlyLogIn, attendStatus, communicatStatus, moneySituation, null, responsibleUserId, loginUserId, token,withDataPermission,departmentId,publicFlag,scopeFlag,extraFlag,priority, 1, 1);
            Integer pageSize = 1000;
            Integer totalCount = (Integer) map.get("totalRows");
            Integer batchSize = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;

            Integer columnSize = 26;
            String[] columnTitles = new String[columnSize];
            String[] columnKeys = new String[columnSize];


            columnTitles[0] = "所属顾问";
            columnKeys[0] = "companyAdviserName";

            columnTitles[1] = "所属总监";
            columnKeys[1] = "companyDirectorName";

            columnTitles[2] = "公司名称";
            columnKeys[2] = "companyName";

            columnTitles[3] = "行业";
            columnKeys[3] = "parentIndustry";

            columnTitles[4] = "省";
            columnKeys[4] = "province";

            columnTitles[5] = "市";
            columnKeys[5] = "city";

            columnTitles[6] = "区";
            columnKeys[6] = "county";

            columnTitles[7] = "法人代表";
            columnKeys[7] = "representative";

            columnTitles[8] = "成立时间";
            columnKeys[8] = "establishTime";

            columnTitles[9] = "注册资金";
            columnKeys[9] = "registeredMoney";

            columnTitles[10] = "公司性质";
            columnKeys[10] = "econKind";

            columnTitles[11] = "经营状态";
            columnKeys[11] = "manageType";

            columnTitles[12] = "联系人";
            columnKeys[12] = "contact";

            columnTitles[13] = "联系电话";
            columnKeys[13] = "contactPhone";

            columnTitles[14] = "电话";
            columnKeys[14] = "phone";

            columnTitles[15] = "客户等级";
            columnKeys[15] = "userLevel";

            columnTitles[16] = "客户类型";
            columnKeys[16] = "userType";

            columnTitles[17] = "报名时间";
            columnKeys[17] = "enrollDate";

            columnTitles[18] = "渠道";
            columnKeys[18] = "channel";

            columnTitles[19] = "付款金额";
            columnKeys[19] = "paymentMoney";

            columnTitles[20] = "完款状态";
            columnKeys[20] = "moneySituation";

            columnTitles[21] = "拥有资源";
            columnKeys[21] = "hasResource";

            columnTitles[15] = "需要资源";
            columnKeys[15] = "needResource";

            columnTitles[22] = "年销售额";
            columnKeys[22] = "annualSalesVolume";

            columnTitles[23] = "年利润";
            columnKeys[23] = "annualProfit";

            columnTitles[24] = "标签";
            columnKeys[24] = "label";

            columnTitles[25] = "经营范围";
            columnKeys[25] = "manageScope";


            List<Map<String, Object>> allData = new ArrayList<>();

            for (int i = 1; i <= batchSize; i++) {
                Map<String, Object> map2 = solrService.queryCompany(queryContent, industryList, customerTypeList,
                        customerDegreeList, companyName, uname, manageScope, province, city, county, mobile, channel, companyAdviserName, companyDirectorName, orderFlag, showOnlyLogIn, attendStatus, communicatStatus, moneySituation, null, responsibleUserId, loginUserId, token,withDataPermission,departmentId,publicFlag,scopeFlag,extraFlag,priority, i, pageSize);
                List<Map<String, Object>> list = (List<Map<String, Object>>) map2.get("data");
                List<String> companyIds = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    companyIds.add((String) list.get(j).get("companyId"));
                }
                List<Company> companyList = companyService.selectCompanyInfoByIdList(companyIds);
                for (int j = 0; j < companyList.size(); j++) {
                    Company company = companyList.get(j);
                    Map<String, Object> m = new HashMap<>();
                    m.put("companyAdviserName", company.getCompanyAdviserName());
                    m.put("companyDirectorName", company.getCompanyDirectorName());
                    m.put("companyName", company.getCompanyName());
                    m.put("parentIndustry", company.getParentIndustry());
                    m.put("province", company.getProvince());
                    m.put("city", company.getCity());
                    m.put("county", company.getCounty());
                    m.put("representative", company.getRepresentative());
                    m.put("establishTime", company.getEstablishTime());
                    m.put("registeredMoney", company.getRegisteredMoney());
                    m.put("econKind", company.getEconKind());
                    m.put("manageType", company.getManageType());
                    m.put("contact", company.getContactName());
                    m.put("contactPhone", company.getContactPhone());
                    m.put("phone", company.getPhone());
                    m.put("userLevel", company.getUserLevel());
                    m.put("userType", company.getUserType());
                    m.put("enrollDate", company.getEnrollDate());
                    m.put("channel", company.getChannel());
                    m.put("paymentMoney", company.getPaymentMoney());
                    m.put("moneySituation", company.getMoneySituation());
                    m.put("hasResource", company.getHasResource());
                    m.put("needResource", company.getNeedResource());
                    m.put("annualSalesVolume", company.getAnnualSalesVolume());
                    m.put("annualProfit", company.getAnnualProfit());
                    m.put("label", company.getLabel());
                    m.put("manageScope", company.getManageScope());
                    allData.add(m);
                }
            }


            Workbook workbook = ExcelUtilSpecial.createWorkbook("企业信息", columnTitles, columnKeys, allData);
            resp.setHeader("Content-Disposition", "attachment;filename= " + java.net.URLEncoder.encode("企业信息", "UTF-8") + ".xls");
            //设置导出文件的格式
            resp.setContentType("application/ms-excel");
            workbook.write(resp.getOutputStream());
            workbook.close();


            if (downloadId != null) {
                downloadStatusMap.remove(downloadId);
            }

       /* } catch (Exception e) {
            e.printStackTrace();
            if (downloadId != null) {
                downloadStatusMap.remove(downloadId);
            }

        }*/

    }


    /**
     * 查询企业信息下载状态 ，下载成功之后返回成功，否则阻塞
     *
     * @param req
     * @param resp
     * @param downloadId 下载Id。由前端传入
     * @return
     */
    @ApiOperation(value = "查询下载状态")
    @ResponseBody
    @RequestMapping(value = "/solr/company/downLoadStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter downLoad(HttpServletRequest req, HttpServletResponse resp,
                                     @ApiParam(value = "下载Id ，该值由前端传入",required = true) @RequestParam(required = true) String downloadId) {
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        Integer status = (Integer) downloadStatusMap.get(downloadId);
        while (status != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            status = (Integer) downloadStatusMap.get(downloadId);
        }
        responseDbCenter.setResModel(status == null ? 0 : 1);
        return responseDbCenter;
    }


    /**
     * 查询企业信息下载状态 ，下载成功之后返回成功，否则阻塞
     *
     * @param req
     * @param resp
     * @return
     */
    @ApiOperation(value = "demo例子",response = ResponseDbCenter.class)
    @ResponseBody
    @RequestMapping(value = "/solr/company/demo", method = {RequestMethod.POST})
    public ResponseDbCenter demo(HttpServletRequest req, HttpServletResponse resp,
                                 @ApiParam(value = "学生" ,required = true) @RequestBody DemoBean demoBean) {
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }






}
