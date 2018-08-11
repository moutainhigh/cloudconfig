package com.szjk.base.mapper;

import com.szjk.base.model.menu.MenuPo;
import com.szjk.base.model.menu.MenuVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuMapper {
    public List<Map<String,Object>> selectNavi(String contextPath);


    public void insert(MenuPo menuPo);

    public void update(MenuPo menuPo);

    public List<MenuVo> search(@Param("menuName") String menuName,@Param("start") Integer start,@Param("pageSize") Integer pageSize);

    public Integer searchCount(@Param("menuName") String menuName);
}
