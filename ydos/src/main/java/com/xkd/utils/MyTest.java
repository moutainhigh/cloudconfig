package com.xkd.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyTest {

	public static void main(String[] args) {
		
		Map<String,Object> roleMenuMap = new HashMap<>();
		Map<String,Object> roleMenuMap1 = new HashMap<>();
		Map<String,Object> roleMenuMap2 = new HashMap<>();
		
		roleMenuMap.put("roleId", "1");
		roleMenuMap.put("menuId", "100100");
		
		roleMenuMap1.put("roleId", "1");
		roleMenuMap1.put("menuId", "100100");
		
		roleMenuMap2.put("roleId", "2");
		roleMenuMap2.put("menuId", "100100");
		
		Set<Map<String,Object>> setMaps = new HashSet<>();

		setMaps.add(roleMenuMap);
		setMaps.add(roleMenuMap1);
		setMaps.add(roleMenuMap2);
		
		System.err.println(setMaps.toString());
		
	}

}
