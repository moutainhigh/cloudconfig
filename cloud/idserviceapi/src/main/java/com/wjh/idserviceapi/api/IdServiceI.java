package com.wjh.idserviceapi.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/id")
public interface IdServiceI {
    @RequestMapping(value = "/generateId", method = RequestMethod.GET)
    public long generateId();
}
