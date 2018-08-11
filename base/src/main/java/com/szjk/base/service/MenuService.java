package com.szjk.base.service;

import com.szjk.base.mapper.MenuMapper;
import com.szjk.base.utils.HashToTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

    public List<Map<String, Object>> selectNavi() {

        List<Map<String, Object>> list = menuMapper.selectNavi();
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
}
