package org.example.push;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 数据加密工具类
 */
public class DataTool {

    /**
     * 数据加密
     * @param data
     * @return
     * @throws Exception
     */
    public String encrypt(String data) throws Exception{
        //密码
        SecretKeySpec secretKeySpec = new SecretKeySpec(CommonParam.encryption_code.getBytes(StandardCharsets.UTF_8), "AES");
        //偏移量
        IvParameterSpec ivParameterSpec = new IvParameterSpec(CommonParam.init_data.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(CommonParam.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encrypted);
//        return new String(encrypted, StandardCharsets.UTF_8);
    }

    /**
     * 数据解密
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(CommonParam.encryption_code.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(CommonParam.init_data.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(CommonParam.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

        return new String(original, StandardCharsets.UTF_8);
    }

    /**
     * md5签名
     * @param data
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String hmacMD5(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacMD5");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacMD5");
        mac.init(secretKeySpec);

        return bytesToHex(mac.doFinal(data.getBytes())) ;
    }

    /**
     * 签名转字符串
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
