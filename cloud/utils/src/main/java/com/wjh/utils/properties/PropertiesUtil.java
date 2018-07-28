package com.wjh.utils.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {


    /**
     *
     * @param propertiesFilePath   属性文件路径,classpath下
     * @param key
     * @return
     */
    public static String getProperty(String propertiesFilePath, String key) {
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFilePath);
            try {
                Properties properties = new Properties();
                properties.load(is);
                String value = properties.getProperty(key);
                if (value == null) {
                    return "";
                } else {
                    return properties.getProperty(key);
                }
            } finally {
                is.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }


    public static Map<String, String> loadProperties(String propertiesFilePath) {
        Map<String, String> map = new HashMap();
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFilePath);
            try {
                Properties properties = new Properties();
                properties.load(new InputStreamReader(is, "UTF-8"));
                Enumeration enumeration = properties.propertyNames();
                while (enumeration.hasMoreElements()) {
                    String key = enumeration.nextElement().toString();
                    map.put(key, (String) properties.get(key));
                }
            } finally {
                is.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return map;
    }


    public static void main(String[] args) {
        Map<String, String> map = loadProperties("pv.properties");
        System.out.println(map);
        System.out.println(getProperty("pv.properties","a"));
    }

}
