package com.lina.toolkit.simple;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Utils {

    public static String SHA1(String decript) throws NoSuchAlgorithmException {
        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
        digest.update(decript.getBytes());
        byte messageDigest[] = digest.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }

}
