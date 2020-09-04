package com.example.covidnews.NetParser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils{
    private final static String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    //使用MD5算法加密
    public static String encode(String source){
        String result = null;
        try{
            result = source;
            //获得摘要对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //使用指定字节数组更新信息
            messageDigest.update(result.getBytes());
            result = byteArrayToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String byteArrayToHexString(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        for (byte tem : bytes) {
            stringBuilder.append(byteToHexString(tem));
        }
        return stringBuilder.toString();
    }

    private static String byteToHexString(byte b){
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}