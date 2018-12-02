package org.encrypt;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

/**
 * RSA加解密工具类
 */
public class RSAUtils {

    public static void main(String[] args) throws Exception {
        String src1 = "asdk234324?''sdfsd";
        String cipherText = "NAD1opytw/0wd1oadnGw1KcSoz+Xe0nXKN/wZFnU9mCC500UZ5uRi8nZnoFQg5I8eKvGDvupA09WEcO8ZWC0fA==";
        String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAjGjOGc/JvogZR1JRXnZ9y6HsoZVN7FogWks0ZT9inJagDCUD1yMCFA6OP2B0ma+stCRMdPFx6SkmdZUVxucslQIDAQABAkBIrIJop70TKhxzZu8IxqnLuU7iP4YqnGd8dMp00T74LtX66nzAvqDWLCf2uCy+mOsR4MZ99zj5CR3745FNHdqBAiEAvcVZNhO3l0Rcs2kua5YHw7H3GQ49L5at4QrYqb12WyECIQC9aV16OXVh4oB1gyfEkp7J11VuRuF9Byee4s3FYUk29QIhAJAq6QdIG2VzrekusvFQ3T0yakJCqwEIO7iHgOoS4WOBAiBKiXItrPrSYE3o8InvwzsVdtW5pc8KYoxm7B3JtLUZGQIgcf+47A5n8WyX/JaBmHH8lNFJfszFsp1PoFe3pYg7Afc=";
        String src2 = decrypt(cipherText, getPrivateKey(privateKey));
        System.out.println(src2);

        if (src1.equals(src2)) {
            System.out.println("==");

        }

    }

    //公钥加密
    public static String encrypt(String content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");//java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(content.getBytes());
            //BASE64Encoder encoder = new BASE64Encoder();
            return Base64.encodeBase64String(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //公钥加密
    public static byte[] encrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");//java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //私钥解密
    public static byte[] decrypt(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //私钥解密
    public static String decrypt(String content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] b = cipher.doFinal(Base64.decodeBase64(content));
            //BASE64Encoder encoder = new BASE64Encoder();
            return new String(b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String转公钥PublicKey
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        //keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * String转私钥PrivateKey
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        //keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
}

