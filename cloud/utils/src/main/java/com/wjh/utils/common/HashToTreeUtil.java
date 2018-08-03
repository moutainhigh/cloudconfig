package com.wjh.utils.common;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashToTreeUtil {


    public static Map shuffleMapListToTree(List<Map<String, Object>> list) {


        Map<String, Object> rootMap = null;

        //id---map映射
        Map<String, Object> id_map_map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> m = list.get(i);
            id_map_map.put(m.get("id").toString(), m);
        }

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> m = list.get(i);
            String parentId = m.get("parentId") == null ? null : m.get("parentId").toString();

            if (parentId == null) {
                rootMap = m;
            }

            //parentId为null时表示根结点
            if (parentId != null) {
                /**
                 * 如果不是根结点，则组织结构
                 */
                Map<String, Object> parentMap = (Map<String, Object>) id_map_map.get(parentId);
                List<Map<String, Object>> childrenNodes = (List<Map<String, Object>>) parentMap.get("childrenNodes");
                if (childrenNodes == null) {
                    childrenNodes = new ArrayList<>();
                    parentMap.put("childrenNodes", childrenNodes);
                }
                childrenNodes.add(m);
            }


        }

        return rootMap;
    }

    public static void main(String[] args) {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", 1);
        map1.put("parentId", 2);
        map1.put("name", "jim");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", 2);
        map2.put("parentId", 3);
        map2.put("name", "tom");


        Map<String, Object> map3 = new HashMap<>();
        map3.put("id", 3);
        map3.put("parentId", null);
        map3.put("name", "tom");


        Map<String, Object> map4 = new HashMap<>();
        map4.put("id", 4);
        map4.put("parentId", 3);
        map4.put("name", "tom");


        Map<String, Object> map5 = new HashMap<>();
        map5.put("id", 5);
        map5.put("parentId", 4);
        map5.put("name", "tom");

        Map<String, Object> map6 = new HashMap<>();
        map6.put("id", 6);
        map6.put("parentId", 4);
        map6.put("name", "tom");

        Map<String, Object> map7 = new HashMap<>();
        map7.put("id", 7);
        map7.put("parentId", 8);
        map7.put("name", "tom");

        Map<String, Object> map8 = new HashMap<>();
        map8.put("id", 8);
        map8.put("parentId", 5);
        map8.put("name", "tom");


        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        list.add(map6);
        list.add(map7);
        list.add(map8);


        Map<String, Object> m = shuffleMapListToTree(list);

        System.out.println(new Gson().toJson(m));


    }
}
