package org.encrypt;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSACoder {
    private static final String KEY_ALGORITHM = "RSA";
    //private static final String CIPHER_TYPE = "RSA/ECB/OAEPPadding";
    private static final String CIPHER_TYPE = "RSA/ECB/PKCS1Padding";

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;  //RSA/ECB/PKCS1Padding
    //private static final int MAX_ENCRYPT_BLOCK = 86;  //RSA/ECB/OAEPPadding

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    public static void main(String[] args) throws Exception {
        String src = "最近在做一个项目中需要，在android对一个密码字段首先进行\n" +
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

        Map<String, Key> keyMap = initKey();
        byte[] bytes = encryptByPublicKey(src, getPublicKey(keyMap));
        String src1 = new String(decryptByPrivateKey(bytes, getPrivateKey(keyMap)));
        if (src.equals(src1)) {
            System.out.println("==");
        }

    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(String data, String key) throws Exception {
        return decryptByPrivateKey(Base64.decodeBase64(data), key);
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key) throws Exception {
        // 对公钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密(分段)
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] dataByteArray = data.getBytes();
        int inputLen = dataByteArray.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(dataByteArray, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataByteArray, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Key> initKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, Key> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
        return keyMap;
    }
}

