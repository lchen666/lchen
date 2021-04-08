package com.lchen.common.core.utils;

import com.lchen.common.core.exception.PasswordException;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordUtil {

    private static final byte[] SALT = {65, 82, 103, 54, 48, 83, 99, 71};

    public static String cryptPassword(String userId, String password) {
        PBEKeySpec publicKey = null;
        SecretKey secretKey = null;
        PBEParameterSpec parameter = null;

        String encoding = "GBK";
        try {
            publicKey = new PBEKeySpec(userId.toCharArray());

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

            secretKey = factory.generateSecret(publicKey);

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

            parameter = new PBEParameterSpec(SALT, 1000);

            cipher.init(1, secretKey, parameter);

            byte[] passwordByte = password.getBytes(encoding);

            byte[] encrypedPasswordByte = cipher.doFinal(passwordByte);

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] digistPasswordByte = md.digest(encrypedPasswordByte);

            BASE64Encoder encoder = new BASE64Encoder();

            String encyrpedPassword = encoder.encode(digistPasswordByte);

            return encyrpedPassword.replace('\r', ' ').replace('\n', ' ').replaceAll(" ", "");
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordException("加密算法：PBEWithMD5AndDES不存在！", e);
        } catch (InvalidKeySpecException e) {
            throw new PasswordException("公钥" + publicKey.toString() + "不正确！", e);
        } catch (NoSuchPaddingException e) {
            throw new PasswordException("不支持PBEWithMD5AndDES加密算法的Padding！", e);
        } catch (InvalidKeyException e) {
            throw new PasswordException("加密算法：PBEWithMD5AndDES的私钥" + secretKey +
                    "不正确！", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new PasswordException("加密算法：PBEWithMD5AndDES的参数" + parameter +
                    "不正确！", e);
        } catch (UnsupportedEncodingException e) {
            throw new PasswordException("不支持" + encoding + "编码！", e);
        } catch (IllegalBlockSizeException e) {
            throw new PasswordException("加密块大小不正确！", e);
        } catch (BadPaddingException e) {
            throw new PasswordException("加密填充异常！", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(cryptPassword("100", "123456"));
    }
}
