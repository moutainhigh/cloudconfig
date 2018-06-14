package com.xkd.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.AddressService;
import com.xkd.utils.SplitAddress;

@Controller
@RequestMapping("/address")
@Transactional
public class AddressController  extends BaseController{
	
	@Autowired
	private AddressService addressService;
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月3日
	 * @功能描述:拆分地址表地址，将地址拆分成省市区三级
	 * @param req
	 * @param rsp
	 * @return
	 */
	/*
	@ResponseBody
	@RequestMapping("/splitAddress")
	public ResponseDbCenter splitAddress(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String qiccCompanyInfo = null;
		
		try {
			
			List<Address> addressList = addressService.selectAllAddress();
			
			for(Address address : addressList){
				
				if(StringUtils.isNotBlank(address.getAddress())){
					
					Map<String, String> map = SplitAddress.getCountryMap(address.getAddress());
					// {area=c区, county=d县, provinces=a省, country=a省b市c区d县, city=b市}
					if(map != null){
						
						String provice = map.get("provinces");
						String city = map.get("city");
						String area = map.get("area");
						String county = map.get("county");
						String country = map.get("country");
						
						if(StringUtils.isNotBlank(provice)){
							address.setProvince(provice);
						}
						if(StringUtils.isNotBlank(city)){
							address.setCity(city);
						}
						if(StringUtils.isNotBlank(country)){
							address.setCounty(country);
						}
						
						addressService.updateAddressInfoById(address);
						
					}
				}
			}
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(qiccCompanyInfo);
		
		return responseDbCenter;
	}*/
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月23日 
	 * @功能描述:根据企查查企业地址拆分成省市区
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/splitCompanyAddress")
	public ResponseDbCenter splitCompanyAddress(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String companyAddress =  req.getParameter("companyAddress");
		
		Map<String, String>  map = null;
		
		try {
			
			map = SplitAddress.getCountryMap(companyAddress);
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		Map<String, String>  newMap = new HashMap<>();
		String  province = map.get("provinces");
		String  city = map.get("city");
		String  county = map.get("area");
		
		newMap.put("province", province);
		newMap.put("city", city);
		newMap.put("county", county);
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(newMap);
		
		return responseDbCenter;
	}

}
