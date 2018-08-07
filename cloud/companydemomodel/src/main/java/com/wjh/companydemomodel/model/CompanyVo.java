package com.wjh.companydemomodel.model;

import java.util.List;

public class CompanyVo {
    private Long id;
    private String companyName;
    private String country;
    private String province;
    private String city;
    private String county;
    private List<ContactorVo> contactors;

    private ContentVo content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

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
