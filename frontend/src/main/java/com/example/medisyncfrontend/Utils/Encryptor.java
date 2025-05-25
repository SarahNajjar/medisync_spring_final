package com.example.medisyncfrontend.Utils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    public static String encryptString(String input) throws NoSuchAlgorithmException {
        MessageDigest md=MessageDigest.getInstance("MD5");
        byte[] messageDigest=md.digest(input.getBytes());
        BigInteger bigInteger=new BigInteger(1,messageDigest);

        return bigInteger.toString(16);
    }



}
