package com.wjh.menuoperate.service;

import com.netflix.discovery.converters.Auto;
import com.wjh.menuoperate.mapper.MenuMapper;
import com.wjh.menuoperateservicemodel.model.MenuPo;
import com.wjh.menuoperateservicemodel.model.MenuVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {


    @Autowired
    IdService idService;
    @Autowired
    MenuMapper menuMapper;

    public MenuPo insert(MenuPo menuPo) {
        Long id=idService.generateId();
        menuPo.setId(id);
        menuMapper.insert(menuPo);
        return menuPo;
    }

    public MenuPo update(MenuPo menuPo) {
        menuMapper.update(menuPo);
        return menuPo;
    }


    public List<MenuVo> search(String menuName,int currentPage,int pageSize) {
        if (currentPage<1){
            currentPage=1;
        }
        if (pageSize<1){
            pageSize=10;
        }
        int start=(currentPage-1)*pageSize;
        return menuMapper.search(menuName,start,pageSize);
    }

    public Long delete(Long id) {
        menuMapper.delete(id);
        return id;
    }

}
