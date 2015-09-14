package com.claridy.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Encoder;

import com.sun.org.apache.xml.internal.security.utils.Base64;
//import org.apache.commons.codec.binary.Base64;

public class DesEncryption {
    /**
     * 測試方法
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // key為abcdefghijklmnopqrstuvwx的Base64編碼
        String key = "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4";

        String jiami = desEncrypt("password", key);
        System.out.println(jiami);
        String jiemi = desDecrypt("0z5xHS1li+KKgWSYS08VnQ==", key);
        System.out.println(jiemi);
    }

    /**
     * ECB模式的des加密，以base64的編碼輸出
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String message, String key)
            throws Exception {
        // DES/ECB CBC CFB OFB /PKCS5Padding NoPadding 加密/模式/填充
        Cipher cipher = Cipher.getInstance("DES");// 默認就是 DES/ECB/PKCS5Padding
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(1, secretKey);
        return new BASE64Encoder().encode(cipher.doFinal(message
                .getBytes("UTF-8")));
    }

    /**
     * ECB模式的des解密
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String desDecrypt(String message, String key)
            throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(2, secretKey);
        return new String(cipher.doFinal(Base64.decode(message)), "UTF-8");
    }
}
