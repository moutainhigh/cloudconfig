package com.wjh.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


public class ObjectSerializeUtil<T> {
    public byte[] objectToBytes(Object t) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public T bytesToObject(byte[] bytes) {
        ObjectInputStream ois = null;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            Object o = ois.readObject();
            return (T) o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "A");
        map.put("b", "B");
        ObjectSerializeUtil<Map<String, String>> osu = new ObjectSerializeUtil<Map<String, String>>();
        System.out.println(osu.objectToBytes(map).length);
        map = osu.bytesToObject(osu.objectToBytes(map));
        ;
        System.out.println(map.get("b"));
    }


}
