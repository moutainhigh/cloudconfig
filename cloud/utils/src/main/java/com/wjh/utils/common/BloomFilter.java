package com.wjh.utils.common;

import java.util.BitSet;

/**
 * 布隆过滤器，用于大量数据的缓存查询
 */
public class BloomFilter {

    static BitSet bitSet1 = new BitSet();
    static BitSet bitSet2 = new BitSet();
    static BitSet bitSet3 = new BitSet();
    static BitSet bitSet4 = new BitSet();


    private static Integer hash1(String key) {
        int hash = 0;
        int i;
        for (i = 0; i < key.length(); i++) {
            hash = 33 * hash + key.charAt(i);
        }
        return hash;
    }

    private static Integer hash2(String key) {
        int b = 378551;
        int a = 63689;
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = hash * a + key.charAt(i);
            a = a * b;
        }
        return (hash & 0x7FFFFFF);
    }


    private static Integer hash3(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = key.charAt(i) * 13 + 31;
        }
        return Math.abs(hash);
    }

    private static Integer hash4(String key) {
        return key.hashCode();
    }

    public static void register(String key) {
        Integer hash1 = hash1(key);
        Integer hash2 = hash2(key);
        Integer hash3 = hash3(key);
        Integer hash4 = hash4(key);
        bitSet1.set(hash1);
        bitSet2.set(hash2);
        bitSet3.set(hash3);
        bitSet4.set(hash4);
    }


    public static boolean hasKey(String key) {
        Integer hash1 = hash1(key);
        Integer hash2 = hash2(key);
        Integer hash3 = hash3(key);
        Integer hash4 = hash4(key);

        boolean result1 = bitSet1.get(hash1);
        boolean result2 = bitSet2.get(hash2);
        boolean result3 = bitSet3.get(hash3);
        boolean result4 = bitSet4.get(hash4);

        return result1 & result2 & result3 & result4;
    }


    public static void main(String[] args) {
        BloomFilter.register("abc");

        System.out.println(BloomFilter.hasKey("abc"));


    }

}