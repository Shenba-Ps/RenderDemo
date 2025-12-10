package com.renderdeployment.renderdemo.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {


    public static String getEncryptedPassword(String password) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found!");
        }
        byte [] digest = algorithm.digest(password.getBytes());
        return new String(Hex.encodeHex(digest));
    }


}
