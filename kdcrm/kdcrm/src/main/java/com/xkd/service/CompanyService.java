package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xkd.mapper.CompanyMapper;
import com.xkd.mapper.CompanyNeedMapper;
import com.xkd.model.Address;
import com.xkd.model.AddressData;
import com.xkd.model.Company;
import com.xkd.model.Payment;
import com.xkd.utils.BaiduAddressUtil;
import com.xkd.utils.CompanyInfoApi;
import com.xkd.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyService {



	@Autowired
	private CompanyMapper companyMapper;
	@Autowired
	private AddressService addressService;
	@Autowired
	PaymentService paymentService;
	@Autowired
	CompanyNeedService companyNeedService;
	@Autowired
	SolrService solrService;

	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	private CompanyNeedMapper companyNeedMapper;
	@Autowired
	private CompanyRelativeUserService companyRelativeUserService;
	
	public List<Company> selectCompanyByName(String companyName,String pcCompanyId){
		
		
		List<Company> companys = companyMapper.selectCompanyByName(companyName,pcCompanyId);

		return companys;
	}
	
	
	public List<Company> selectCompanyByNameIncludingDeleted(String companyName,String pcCompanyId){
		return companyMapper.selectCompanyByNameIncludingDeleted(companyName,pcCompanyId);
	}

	
	


 

	public int updateCompanyInfoById(Map<String,Object> company) {
		
		int num = companyMapper.updateCompanyInfoById(company);
		if(null != company && null!= company.get("companyName")&&!"".equals(company.get("companyName"))){
			companyMapper.updatePagerFileName(company);
		}

		return num;
	}
	
	
	
	public int updateCompanyDetailInfoById(Map<String,Object> company) {
		
		int num = companyMapper.updateCompanyDetailInfoById(company);
		
		return num;
	}

 

	public int deleteCompanyById(String ids) {
		
		int num = companyMapper.deleteCompanyById(ids);
		
		return num;
	}

 

	public Company selectCompanyInfoById(String companyId) {
		
		Company company = companyMapper.selectCompanyInfoById(companyId);
		
		return company;
	}

 

	public Integer updateCompanyBySql(String sql) {
		
		Integer num = companyMapper.updateCompanyBySql(sql);
		
		return num;
	}


	public List<Map<String,Object>>  searchCompanyByName(String companyName,List<String> departmentIdList,Integer start,Integer pageSize){
		return companyMapper.searchCompanyByName(companyName,departmentIdList,start,pageSize);
	}



	public List<HashMap<String, Object>> selectCompanyByNameMH(String companyName,String pcCompanyId) {
		
		List<HashMap<String, Object>> companies = companyMapper.selectCompanyByNameMH(companyName,pcCompanyId);
		
		return companies;
	}

	public Integer insertCompanyInfo(Map<String,Object> company) {
		Integer num = companyMapper.insertCompanyInfo(company);
		
		return num;
	}
	
	
	public Integer insertCompanyDetailInfo(Map<String,Object> company) {
		
		Integer num = companyMapper.insertCompanyDetailInfo(company);
		
		return num;
	}










	public Integer updateCompanyLabelById(String companyId, String label) {
		
	Integer num = companyMapper.updateCompanyLabelById(companyId,label);
	
	return num;
}

	
	//查询天眼查的一些信息
	public Map<String,Object>   selectCrawlInfo(@Param("id") String id){
		   Map<String,Object> resultMap= new HashMap<>();

	   Map<String,String> map=	companyMapper.selectCrawlInfo(id);
	   if (null==map) {
		return resultMap;
	  }
	   String infoJson=map.get("infoJson");
	   if (StringUtil.isBlank(infoJson)) {
		   return resultMap;
	    }
	   
		// 将其解析为一个大Map
		Map<String, Object> jsonMap = JSON.parseObject(infoJson, new TypeReference<Map<String, Object>>() {
		});
		Map<String,Object> jsonMarketInfo=(Map<String, Object>) jsonMap.get("marketInfo");
		Map<String,Object> jsonEnterpriseBackground=(Map<String, Object>) jsonMap.get("enterpriseBackground");
		Map<String,Object> jsonJudicialRisk=(Map<String, Object>) jsonMap.get("judicialRisk");
		Map<String,Object> jsonBusinessRisk=(Map<String, Object>) jsonMap.get("businessRisk");
		
		Map<String,Object> jsonCopyRight=(Map<String, Object>) jsonMap.get("copyRight");
		
		
		//市场信息
		Map<String,Object> marketInfo=new HashMap();
		
		resultMap.put("marketInfo", marketInfo);
		
		marketInfo.put("basicInfo", jsonEnterpriseBackground.get("basicInfo")); 
		marketInfo.put("seniorPersonList", jsonMarketInfo.get("seniorPersonList"));
		marketInfo.put("topTenHolderList", jsonMarketInfo.get("topTenHolderList"));
		marketInfo.put("stockStructureList", jsonMarketInfo.get("stockStructureList"));
		marketInfo.put("stockChangeList", jsonMarketInfo.get("stockChangeList"));
		marketInfo.put("bonusInfoList", jsonMarketInfo.get("bonusInfoList"));
		
		
		//背景信息
		Map<String,Object> enterpriseBackground=new HashMap();
		resultMap.put("enterpriseBackground", enterpriseBackground);
		enterpriseBackground.put("holderList", jsonEnterpriseBackground.get("holderList"));
		enterpriseBackground.put("outerInvestInfoList", jsonEnterpriseBackground.get("outerInvestInfoList"));
		
		
		
		//风险信息
		Map<String,Object> risk=new HashMap();
		resultMap.put("risk", risk);
		risk.put("lawsuitList", jsonJudicialRisk.get("lawsuitList"));
		risk.put("punishList", jsonBusinessRisk.get("punishList"));
		
		
		//知识产权
		Map<String,Object> copyRight=new HashMap();
		resultMap.put("copyRight", copyRight);
		copyRight.put("tradeMarkList", jsonCopyRight.get("tradeMarkList"));
		copyRight.put("patentList", jsonCopyRight.get("patentList"));
		copyRight.put("copyRightList", jsonCopyRight.get("copyRightList"));

		
 		
		return resultMap;
	
	}


	public List<Company> selectAllStatusCompanyByName(String companyName,String pcCompanyId) {
		
		List<Company>  companies = companyMapper.selectAllStatusCompanyByName(companyName,pcCompanyId);
		
		return companies;
	}
	
	






	public 	void deleteByCompanyById(String id){
		companyMapper.deleteByCompanyById(id);
	}
	public List<String> selecAllCompanyId(){
		return companyMapper.selecAllCompanyId();
	}


	public boolean isRelativePermission(String companyId,String userId){

		boolean relativeFlag=false;
		List<String> companyIdList=new ArrayList<>();
		companyIdList.add(companyId);
		List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);
		for (int i = 0; i <relativeUserList.size() ; i++) { //判断相关人员权限
			if (userId!=null&&userId.equals(relativeUserList.get(i).get("userId"))){
				relativeFlag=true;
			}
		}
		return  relativeFlag;
	}



	public void updateInfoScore(String companyId){
		Company company=companyMapper.selectCompanyInfoById(companyId);
		List<Payment> payments= paymentService.selectPaymentByCompanyId(companyId);
		List<Map<String,Object>> needs=companyNeedService.selectCompanyNeedByCompanyId(companyId);
		List<Map<String,Object>> userInfos=   userInfoService.selectUserInfoByCompanyId(companyId);
		Integer v_establishTime=StringUtils.isBlank(company.getEstablishTime())?0:1;
		Integer v_registerMoney=StringUtils.isBlank(company.getRegisteredMoney())?0:1;
		Integer v_econKind=StringUtils.isBlank(company.getEconKind())?0:1;
		Integer v_socialCredit=StringUtils.isBlank(company.getSocialCredit())?0:1;
		Integer v_representative=StringUtils.isBlank(company.getRepresentative())?0:1;
		Integer v_address=StringUtils.isBlank(company.getAddress())?0:1;
		Integer v_manageScope=StringUtils.isBlank(company.getManageScope())?0:1;
		Integer v_label=StringUtils.isBlank(company.getLabel())?0:1;
		Integer v_annualSalesVolume=StringUtils.isBlank(company.getAnnualSalesVolume())?0:1;
		Integer v_annualProfit=StringUtils.isBlank(company.getAnnualProfit())?0:1;
		Integer v_thisYearSalesVolume=StringUtils.isBlank(company.getThisYearSalesVolume())?0:1;
		Integer v_nextYearSalesVolume=StringUtils.isBlank(company.getNextYearSalesVolume())?0:1;
		Integer v_businessScope=StringUtils.isBlank(company.getBusinessScope())?0:1;
		Integer v_companySize=StringUtils.isBlank(company.getCompanySize())?0:1;
		Integer v_companyPosition=StringUtils.isBlank(company.getCompanyPosition())?0:1;
		Integer v_hasResource=StringUtils.isBlank(company.getHasResource())?0:1;
		Integer v_needResource=StringUtils.isBlank(company.getNeedResource())?0:1;
		Integer v_needs=needs.size()>0?1:0;
		Integer v_payment=payments.size()>0?1:0;
		Integer v_userInfo=userInfos.size()>0?1:0;

		Integer score=v_establishTime*2+v_registerMoney*2
				+v_econKind*2+v_socialCredit*2
				+v_representative*2+v_address*2
				+v_manageScope*2
				+v_label*10+v_annualSalesVolume*5
				+v_annualProfit*5+v_thisYearSalesVolume*5
				+v_nextYearSalesVolume*5+v_businessScope*5
				+v_companySize*5+v_companyPosition*5
				+v_hasResource*5+v_needResource*5
				+v_needs*15+v_payment*10
				+v_userInfo*6;

		 Map<String,Object> companyMap=new HashMap<>();
		companyMap.put("id",companyId);
		companyMap.put("infoScore",score);
		companyMapper.updateCompanyInfoById(companyMap);

	}


	public List<Company> selectCompanyInfoByIdList(@Param("idList")List<String> idList){
		if (idList.size()==0){
			return new ArrayList<>()
					;
		}
		return companyMapper.selectCompanyInfoByIdList(idList);
	}



	public List<String> selectSolrCompanyIdsByDepartmentIdsAndOperate(List<String> departmentIdList,Integer operate, Integer start,Integer pageSize){
		return companyMapper.selectSolrCompanyIdsByDepartmentIdsAndOperate(departmentIdList,operate, start, pageSize);
	}

	public int deleteSolrCompanyIds(List<String> idList,Integer Operate){
		return companyMapper.deleteSolrCompanyIds(idList,Operate);
	}

	public int insertDcSolrCompany( List<String> departmentIdList,Integer operate){
		return companyMapper.insertDcSolrCompany(departmentIdList,operate);
	}

	public Map<String,Map> getQCC(JsonObject json, Map<String, Object> companyMap, Map<String, Object> companyDetailMap) {
		//返回企业信息正确
		String establish_time = "";
		String operating_period = "";
		String approve_date = "";
		String updated_date = "";
		String termStart = "";



			establish_time = json.get("Result").getAsJsonObject().get("StartDate")==null?"":json.get("Result").getAsJsonObject().get("StartDate").getAsString().substring(0, 10);//get("StartDate").getAsString().substring(0, 10)
			operating_period = json.get("Result").getAsJsonObject().get("TeamEnd")==null?"":json.get("Result").getAsJsonObject().get("TeamEnd").getAsString().substring(0, 10);
			approve_date = json.get("Result").getAsJsonObject().get("CheckDate")==null?"":json.get("Result").getAsJsonObject().get("CheckDate").getAsString().substring(0, 10);
			updated_date = json.get("Result").getAsJsonObject().get("UpdatedDate")==null?"":json.get("Result").getAsJsonObject().get("UpdatedDate").getAsString().substring(0, 10);
			termStart = json.get("Result").getAsJsonObject().get("TermStart")==null?"":json.get("Result").getAsJsonObject().get("TermStart").getAsString().substring(0, 10);

			String social_credit = json.get("Result").getAsJsonObject().get("CreditCode").isJsonNull()?null:json.get("Result").getAsJsonObject().get("CreditCode").getAsString();
			String registration_code = json.get("Result").getAsJsonObject().get("No").isJsonNull()?null:json.get("Result").getAsJsonObject().get("No").getAsString();
			String organization_code = json.get("Result").getAsJsonObject().get("No").isJsonNull()?null:json.get("Result").getAsJsonObject().get("No").getAsString();
			String manage_type = json.get("Result").getAsJsonObject().get("Status").isJsonNull()?null:json.get("Result").getAsJsonObject().get("Status").getAsString();
			String representative = json.get("Result").getAsJsonObject().get("OperName").isJsonNull()?null:json.get("Result").getAsJsonObject().get("OperName").getAsString();
			String registered_money = json.get("Result").getAsJsonObject().get("RegistCapi").isJsonNull()?null:json.get("Result").getAsJsonObject().get("RegistCapi").getAsString();
			String registration_authority = json.get("Result").getAsJsonObject().get("BelongOrg").isJsonNull()?null:json.get("Result").getAsJsonObject().get("BelongOrg").getAsString();
			String companyAddress = json.get("Result").getAsJsonObject().get("Address").isJsonNull()?null:json.get("Result").getAsJsonObject().get("Address").getAsString();
			String scope = json.get("Result").getAsJsonObject().get("Scope").isJsonNull()?"":json.get("Result").getAsJsonObject().get("Scope").getAsString();
//						            		String province = json.get("Result").getAsJsonObject().get("Province").isJsonNull()?"":json.get("Result").getAsJsonObject().get("Province").getAsString();
			String econKind = json.get("Result").getAsJsonObject().get("EconKind").isJsonNull()?"":json.get("Result").getAsJsonObject().get("EconKind").getAsString();
			String name = json.get("Result").getAsJsonObject().get("Name").isJsonNull()?"":json.get("Result").getAsJsonObject().get("Name").getAsString();






			//拆分地址
			String province = null;
			String city = null;
			String area = null;
			String county = null;
			String country = null;
			//Map<String, String> mapCompanyAddresss = SplitAddress.getCountryMap(companyAddress);
			AddressData addressData = BaiduAddressUtil.parseAddressToCountryProvinceCityCounty(companyAddress);
			// {area=c区, county=d县, provinces=a省, country=a省b市c区d县, city=b市}
			if(addressData != null){

				//	province = mapCompanyAddresss.get("provinces");
				province = addressData.getProvince();
				//	city = mapCompanyAddresss.get("city");
				city = addressData.getCity();
				//	area = mapCompanyAddresss.get("area");
				//	county = mapCompanyAddresss.get("county");
				county = addressData.getCounty();
				//	country = mapCompanyAddresss.get("country");
			}

			Address address = new Address();

			address.setId(UUID.randomUUID().toString());
			address.setAddress(companyAddress);
			address.setCity(city);
			address.setProvince(province);
			address.setCounty(county);
			address.setUserId(companyMap.get("id").toString());
			address.setCreatedBy(companyMap.get("createdBy").toString());
			address.setCreateDate(new Date());

			addressService.saveUserAddress(address);




			companyMap.put("companyName",name);
			companyMap.put("representative",representative);
			companyMap.put("status","0");

			companyDetailMap.put("companyName",name);
			companyDetailMap.put("socialCredit",social_credit);
			companyDetailMap.put("registrationCode",registration_code);
			companyDetailMap.put("organizationCode",organization_code);
			companyDetailMap.put("manageType",manage_type);
			companyDetailMap.put("manageScope",scope);
			companyDetailMap.put("registeredMoney",registered_money);
			companyDetailMap.put("registrationAuthority",registration_authority);
			companyDetailMap.put("content",scope);
			companyDetailMap.put("econKind",econKind);
			companyDetailMap.put("establishTime",establish_time);
			companyDetailMap.put("approveDate",approve_date);
			companyDetailMap.put("termStart",termStart);
			companyDetailMap.put("operatingPeriod",operating_period);
			companyDetailMap.put("createDate",new Date());
			companyDetailMap.put("updateDate",new Date());
			companyMap.put("qccUpdatedDate",DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));


		Map<String,Map> map = new HashMap<>();
		map.put("companyMap",companyMap);
		map.put("companyDetailMap",companyDetailMap);



		return map;
	}

	public List<HashMap<String,Object>> checkCompany(String companyName, String pcCompanyId) {
		return companyMapper.checkCompany(companyName,pcCompanyId);
	}


	public List<String> selectAllCompanyIds(){
		return companyMapper.selectAllCompanyIds();
	}

}
