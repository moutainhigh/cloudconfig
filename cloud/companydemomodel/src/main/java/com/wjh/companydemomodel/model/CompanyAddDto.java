package com.wjh.companydemomodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel
public class CompanyAddDto {

    private String companyName;
    private String country;
    private String province;
    private String city;
    private String county;
    private List<ContactorVo> contactors;

    private ContentVo content;



    @ApiModelProperty(value = "公司名称", required = true, example = "ABC有限公司")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @ApiModelProperty(value = "国家", required = true, example = "中华人民共和国")
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

    public List<ContactorVo> getContactors() {
        return contactors;
    }

    public void setContactors(List<ContactorVo> contactors) {
        this.contactors = contactors;
    }

    public ContentVo getContent() {
        return content;
    }

    public void setContent(ContentVo content) {
        this.content = content;
    }
}
