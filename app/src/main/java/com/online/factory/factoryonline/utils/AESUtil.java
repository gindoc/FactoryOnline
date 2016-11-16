package com.online.factory.factoryonline.utils;

import org.apache.commons.codec.binary.Base64;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by cwenhui on 2016/11/5.
 */

public class AESUtil {
    /**
     * AES加密
     *
     * @param data      明文
     * @param secretKey 秘钥
     * @param ivspec    初始向量IvParameterSpec
     * @return
     */
    public static String encrypt(String data, String secretKey, String ivspec) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(secretKey.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivspec.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, iv);
            byte[] encrypted = cipher.doFinal(plaintext);
            return new String(Base64.encodeBase64(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES加密
     *
     * @param data      明文
     * @param secretKey 密钥
     * @return
     */
    public static String encrypt(String data, String secretKey) {
        return encrypt(data, secretKey, secretKey + secretKey);
    }

    /**
     * AES解密
     *
     * @param data      密文
     * @param secretKey 秘钥
     * @param ivspec    初始向量IvParameterSpec
     * @return
     */
    public static String desEncrypt(String data, String secretKey, String ivspec) {
        try {
            byte[] encrypted1 = Base64.decodeBase64(data.getBytes());

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(secretKey.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivspec.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, iv);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param data      密文
     * @param secretKey 秘钥
     * @return
     */
    public static String desEncrypt(String data, String secretKey) {
        return desEncrypt(data, secretKey, secretKey + secretKey);
    }
}
