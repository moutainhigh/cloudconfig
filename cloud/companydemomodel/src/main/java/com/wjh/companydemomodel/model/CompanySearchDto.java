package com.wjh.companydemomodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CompanySearchDto {
    String searchValue;
    String companyName;
    String country;
    String province;
    String city;
    String county;
    String contactorName;
    String contactorMobile;
    String representative;

    @ApiModelProperty(value = "搜索值", required = true, example = "ABC")
    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    @ApiModelProperty(value = "公司名称", required = true, example = "ABC有限公司")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    @ApiModelProperty(value = "国家", required = true, example = "中国")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    @ApiModelProperty(value = "省", required = true, example = "湖北省")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    @ApiModelProperty(value = "市", required = true, example = "随州市")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    @ApiModelProperty(value = "县", required = true, example = "随县")
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
    @ApiModelProperty(value = "联系人", required = true, example = "小明")
    public String getContactorName() {
        return contactorName;
    }

    public void setContactorName(String contactorName) {
        this.contactorName = contactorName;
    }
    @ApiModelProperty(value = "联系人电话", required = true, example = "1300000000")
    public String getContactorMobile() {
        return contactorMobile;
    }

    public void setContactorMobile(String contactorMobile) {
        this.contactorMobile = contactorMobile;
    }
    @ApiModelProperty(value = "法人代表", required = true, example = "李明")
    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }
}
