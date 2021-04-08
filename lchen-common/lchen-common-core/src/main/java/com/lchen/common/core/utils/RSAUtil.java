package com.lchen.common.core.utils;

import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.exception.RsaException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RSAUtil {

    /**
     * 密钥长度 于原文长度对应 以及越长速度越慢
     */
    private final static int KEY_SIZE = 1000;

    public static final String RSA_ALGORITHM = "RSA";

    /**
     * 随机生成密钥对
     * @throws Exception
     */
    public static Map<Integer, String> genKeyPair(){
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            // 初始化密钥对生成器
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到公钥
            Key publicKey = keyPair.getPublic();
            String publicKeyString = Base64.encodeBase64URLSafeString(publicKey.getEncoded());;
            // 得到私钥
            Key privateKey = keyPair.getPrivate();
            String privateKeyString = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
            // 将公钥和私钥保存到Map
            Map<Integer, String> keyMap = new HashMap<Integer, String>();
            //0表示公钥
            keyMap.put(0, publicKeyString);
            //1表示私钥
            keyMap.put(1, privateKeyString);
            return keyMap;
        }catch (NoSuchAlgorithmException e){
            throw new RsaException("RSAUtil genKeyPair error");
        }
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKeyStr 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKeyStr){
        //X509编码的公钥
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
            //RSA加密
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String outStr = Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, str.getBytes(Constants.UTF8), publicKey.getModulus().bitLength()));
            return outStr;
        }
        catch (NoSuchAlgorithmException e){
            throw new RsaException("RSA publicKey encrypt error NoSuchAlgorithm");
        }
        catch (InvalidKeySpecException e){
            throw new RsaException("RSA publicKey encrypt error InvalidKeySpec");
        }
        catch (NoSuchPaddingException e){
            throw new RsaException("RSA publicKey encrypt error NoSuchPadding");
        }
        catch (InvalidKeyException e){
            throw new RsaException("RSA publicKey encrypt error InvalidKey");
        }
        catch (UnsupportedEncodingException e){
            throw new RsaException("RSA privateKey encrypt error UnsupportedEncodingException");
        }
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKeyStr 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKeyStr){
        try {
            //通过PKCS#8编码的Key指令获得私钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr));
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            //RSA解密
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            String outStr = new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(str), privateKey.getModulus().bitLength()), Constants.UTF8);;
            return outStr;
        }
        catch (NoSuchAlgorithmException e){
            throw new RsaException("RSA privateKey decrypt error NoSuchAlgorithmException");
        }
        catch (InvalidKeySpecException e){
            throw new RsaException("RSA privateKey decrypt error InvalidKeySpecException");
        }
        catch (NoSuchPaddingException e){
            throw new RsaException("RSA privateKey decrypt error NoSuchPaddingException");
        }
        catch (InvalidKeyException e){
            throw new RsaException("RSA privateKey decrypt error InvalidKeyException");
        }
        catch (UnsupportedEncodingException e){
            throw new RsaException("RSA privateKey decrypt error UnsupportedEncodingException");
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int mode, byte[] bytes, int keySize){
        int maxBlock = 0;
        if(mode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(bytes.length > offSet){
                if(bytes.length-offSet > maxBlock){
                    buff = cipher.doFinal(bytes, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(bytes, offSet, bytes.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }
        byte[] resultData = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultData;
    }


    public static void main(String[] args) throws Exception {
        long temp = System.currentTimeMillis();
        //生成公钥和私钥
        Map<Integer, String> map = genKeyPair();
        //加密字符串
        System.out.println("公钥:" + map.get(0));
        System.out.println("私钥:" + map.get(1));
        System.out.println("生成密钥消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");


        String message = "123456";
        System.out.println("原文:" + message);

        temp = System.currentTimeMillis();
        String messageEn = encrypt(message, map.get(0));
        System.out.println("密文:" + messageEn);
        System.out.println("加密消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");

        temp = System.currentTimeMillis();

        String messageDe = decrypt(messageEn, map.get(1));
        System.out.println("解密:" + messageDe);
        System.out.println("解密消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");
    }
}
