package com.lnson.simple.projname.common;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5Utils {

    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 使用MD5算法对字符串加密
     *
     * @param sourceStr 原始字符串
     * @return 返回原始字符的MD5密文
     */
    public static String encryptionByMd5(String sourceStr) {
        String ciphertext = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            ciphertext = base64en.encode(md5.digest(sourceStr.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ciphertext;
    }

    /**
     * 获取一个字符串的MD5哈希值
     *
     * @return 返回字符串的哈希值
     */
    public static String getStringHashValue(String s) {
        MessageDigest mdInst;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        byte[] btInput = s.getBytes();
        mdInst.update(btInput);
        byte[] md = mdInst.digest();
        int length = md.length;
        char str[] = new char[length * 2];
        int k = 0;
        for (byte b : md) {
            str[k++] = hexDigits[b >>> 4 & 0xf];
            str[k++] = hexDigits[b & 0xf];
        }
        return new String(str);
    }

    /**
     * 获取一个文件的MD5哈希值
     *
     * @return 返回文件的哈希值
     */
    public static String getFileHashValue(File file) {
        // 在Windows系统中计算文件的哈希值
        // certutil -hashfile [文件路径] MD5
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            return MD5(ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 计算MD5校验
     */
    private static String MD5(ByteBuffer buffer) {
        String s = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }
}
