package org.encrypt;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 */
public class Rsa1 {

    //java默认"RSA"="RSA/ECB/PKCS1Padding"
    private final static String RSA_ALGORITHM = "RSA/ECB/OAEPPadding";
    //private final static String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";


    private static final String KEY_ALGORITHM = "RSA";

    /**
     * RSA最大加密明文大小
     */
    //private static final int MAX_ENCRYPT_BLOCK = 117;  //RSA/ECB/PKCS1Padding  1024/8 -11
    private static final int MAX_ENCRYPT_BLOCK = 86;  //RSA/ECB/OAEPPadding  1024/8 -42

    /**
     * RSA最大解密密文大小  keyPairGenerator.initialize(1024);
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final String DEFAULT_ENCODING = "UTF-8";


    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        System.out.println("Public Key:" + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        System.out.println("Private Key:" + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));

        String src1 = "最近在做一个项目中需要，在android对一个密码字段首先进行\n" +
                "\n" +
                "一次md5加密后再进行一次rsa加密，然后把加密的结果通过\n" +
                "\n" +
                "json协议传输给nginx服务器进行解密。在android中，可以直接\n" +
                "\n" +
                "使用java提供Cipher类来进行加密，nginx服务器使用openssl来进行解密。\n" +
                "\n" +
                "android客户端使用RSA加密的字段，要使nginx服务器能正常解密，\n" +
                "\n" +
                "这里需要客户端和服务器统一RSA加密所用的填充方式。\n" +
                "\n" +
                "RSA加密常用的填充方式有下面3种：\n" +
                "\n" +
                "1.RSA_PKCS1_PADDING 填充模式，最常用的模式\n" +
                "\n" +
                "要求:\n" +
                "输入：必须 比 RSA 钥模长(modulus) 短至少11个字节, 也就是　RSA_size(rsa) – 11\n" +
                "如果输入的明文过长，必须切割，　然后填充\n" +
                "\n" +
                "输出：和modulus一样长\n" +
                "\n" +
                "根据这个要求，对于512bit的密钥，　block length = 512/8 – 11 = 53 字节\n" +
                "\n" +
                "2.RSA_PKCS1_OAEP_PADDING\n" +
                "输入：RSA_size(rsa) – 41\n" +
                "\n" +
                "输出：和modulus一样长\n" +
                "\n" +
                "3.for RSA_NO_PADDING　　不填充\n" +
                "\n" +
                "输入：可以和RSA钥模长一样长，如果输入的明文过长，必须切割，　然后填充\n" +
                "\n" +
                "输出：和modulus一样长\n" +
                "\n" +
                "跟DES，AES一样，　RSA也是一个块加密算法（ block cipher algorithm），总是在一个固定长度的块上进行操作。\n" +
                "\n" +
                "但跟AES等不同的是，　block length是跟key length有关的。\n" +
                "\n" +
                "每次RSA加密的明文的长度是受RSA填充模式限制的，但是RSA每次加密的块长度就是key length。\n" +
                "\n" +
                "需要注意：\n" +
                "\n" +
                "假如你选择的秘钥长度为1024bit共128个byte：\n" +
                "\n" +
                "1.当你在客户端选择RSA_NO_PADDING填充模式时，如果你的明文不够128字节\n" +
                "\n" +
                "加密的时候会在你的明文前面，前向的填充零。解密后的明文也会包括前面填充的零，这是服务器需要注意把解密后的字段前向填充的\n" +
                "\n" +
                "零去掉，才是真正之前加密的明文。\n" +
                "\n" +
                "2.当你选择RSA_PKCS1_PADDING填充模式时，如果你的明文不够128字节\n" +
                "\n" +
                "加密的时候会在你的明文中随机填充一些数据，所以会导致对同样的明文每次加密后的结果都不一样。\n" +
                "\n" +
                "对加密后的密文，服务器使用相同的填充方式都能解密。解密后的明文也就是之前加密的明文。\n" +
                "\n" +
                "3.RSA_PKCS1_OAEP_PADDING填充模式没有使用过， 他是PKCS#1推出的新的填充方式，安全性是最高的，\n" +
                "\n" +
                "和前面RSA_PKCS1_PADDING的区别就是加密前的编码方式不一样。";

        /*String src1 = "sdfsdfsdfsdfsdf";*/

        String target = encrypt(src1, Base64.encodeBase64String(rsaPublicKey.getEncoded()));

        String src2 = decrypt(target, Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
        System.out.println("1111111111111111111");
        System.out.println(target);
        if (src1.equals(src2)) {
            System.out.println("==");
        }
    }

    /**
     * 公钥加密
     *
     * @param content
     * @param publicKeyBase64
     * @return
     */
    public static String encrypt(String content, String publicKeyBase64) throws Exception {
        return encrypt(content.getBytes(Charset.forName(DEFAULT_ENCODING)), getPublicKey(publicKeyBase64));
    }

    /**
     * 公钥加密
     *
     * @param content
     * @param publicKey
     * @return
     */
    private static String encrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);//java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = content.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(content, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(content, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param content
     * @param privateKey
     * @return
     */
    private static String decrypt(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            int inputLen = content.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(content, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(content, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, Charset.forName(DEFAULT_ENCODING));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥解密
     *
     * @param content
     * @param privateKeyBase64
     * @return
     */
    public static String decrypt(String content, String privateKeyBase64) throws Exception {
        return decrypt(Base64.decodeBase64(content), getPrivateKey(privateKeyBase64));
    }

    /**
     * String转公钥PublicKey
     *
     * @param publicKeyBase64
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKeyBase64) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * String转私钥PrivateKey
     *
     * @param privateKeyBase64
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
}

