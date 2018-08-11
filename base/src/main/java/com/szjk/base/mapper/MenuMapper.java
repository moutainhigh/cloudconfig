package com.szjk.base.mapper;

import com.szjk.base.model.menu.MenuVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuMapper {
    public List<Map<String,Object>> selectNavi();
}
