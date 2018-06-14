package com.kuangchi.sdd.util.commonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtil {

    public static boolean isConnected() {
        String addr = "http://localhost:8080";
        URL url = null;
        try {
            url = new URL(addr);
            try {
                InputStream in = url.openStream();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}