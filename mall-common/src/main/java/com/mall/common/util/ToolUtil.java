package com.mall.common.util;

import sun.misc.BASE64Encoder;


import java.security.MessageDigest;
import java.util.*;

public class ToolUtil {
    private static final String UTF8 = "utf-8";

    /**
     * 随机id生成
     */
    public static long getRandomId() {
        long millis = System.currentTimeMillis();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        //如果不足两位前面补0
        String str = millis + String.format("%02d", end2);
        long id = new Long(str);
        return id;
    }

    /**
     * 获取随机位数的字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * MD5数字签名
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String md5Digest(String src) throws Exception {
        // 定义数字签名方法, 可用：MD5, SHA-1
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(src.getBytes(UTF8));
        return byte2HexStr(b);
    }

    /**
     * BASE64编码
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String base64Encoder(String src) throws Exception {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(src.getBytes(UTF8));
    }

    /**
     * 字节数组转化为大写16进制字符串
     *
     * @param b
     * @return
     */
    private static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }

    public static int[] String2int(String str) {
        String[] ss = str.split(",");
        int[] num = new int[ss.length];
        for (int i = 0; i < ss.length; i++) {
            num[i] = Integer.parseInt(ss[i]);
        }
        return num;
    }
}
