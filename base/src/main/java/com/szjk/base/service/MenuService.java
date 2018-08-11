package com.szjk.base.service;

import com.szjk.base.mapper.MenuMapper;
import com.szjk.base.model.menu.MenuPo;
import com.szjk.base.model.menu.MenuVo;
import com.szjk.base.utils.HashToTreeUtil;
import com.szjk.base.utils.IdGenerator;
import com.szjk.base.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    IdGenerator idGenerator;

    public List<Map<String, Object>> selectNavi(String contextPath) {

        List<Map<String, Object>> list = menuMapper.selectNavi( contextPath);
        Map<String, Object> shuffledMap = HashToTreeUtil.shuffleMapListToTree(list);

        List<Map<String, Object>> levelOneList = (List<Map<String, Object>>) shuffledMap.get("children");

        if (levelOneList != null && levelOneList.size() > 0) {
            for (int i = 0; i < levelOneList.size(); i++) {
                Map<String, Object> levelOne = levelOneList.get(i);
                levelOne.put("menu",levelOne.get("children"));
                levelOne.remove("children");
            }

        }
        return levelOneList;
    }




    public MenuPo insert(MenuPo menuPo, Long loginUserId) {
        Long id= idGenerator.generateId();
        menuPo.setId(id);
        Date date=new Date();
        menuPo.setCreateDate(date);
        menuPo.setUpdateDate(date);
        menuPo.setCreatedBy(loginUserId);
        menuPo.setUpdatedBy(loginUserId);
        menuMapper.insert(menuPo);
        return menuPo;
    }

    public MenuPo update(MenuPo menuPo,Long loginUserId) {
        Date date=new Date();
        menuPo.setUpdateDate(date);
        menuPo.setUpdatedBy(loginUserId);
        menuMapper.update(menuPo);
        return menuPo;
    }


    public List<MenuVo> search(String menuName, int currentPage, int pageSize) {
        if (currentPage<1){
            currentPage=1;
        }
        if (pageSize<1){
            pageSize=10;
        }
        int start=(currentPage-1)*pageSize;
        return menuMapper.search(menuName,start,pageSize);
    }
    public Integer searchCount(String menuName) {
      return menuMapper.searchCount(menuName);
    }

}
