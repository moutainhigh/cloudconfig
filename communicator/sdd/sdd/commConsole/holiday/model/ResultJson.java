package com.kuangchi.sdd.commConsole.holiday.model;

import java.util.List;

public class ResultJson {
    private String block;
    private List<HolidayJson> groups;
    public String getBlock() {
        return block;
    }
    public void setBlock(String block) {
        this.block = block;
    }
    public List<HolidayJson> getGroups() {
        return groups;
    }
    public void setGroups(List<HolidayJson> groups) {
        this.groups = groups;
    }
    
}
