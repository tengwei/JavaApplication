package org.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rsa {

    public static void main(String[] args) {
        rsa1();
    }

    // jdk实现：私钥加密、公钥解密： 并发时间测试
    public static void rsa1() {
        try {
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
            byte[] result;
            long startTimes = System.currentTimeMillis();

            // 1.初始化发送方密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.println("Public Key:" + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
            System.out.println("Private Key:" + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));

            // 2.公钥加密、私钥解密 ---- 加密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            //Cipher cipher = Cipher.getInstance("RSA");
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            result = cipher.doFinal(src.getBytes());
            System.out.println("公钥加密、私钥解密 ---- 加密:" + Base64.encodeBase64String(result));
            long endEncryptTime = System.currentTimeMillis();
            System.out.println("公钥加密、私钥解密 ---- 加密1个时间(单位毫秒):" + (endEncryptTime - startTimes));

            int decryptTimes = 1;// 并发解密的个数
            //创建一个可重用固定线程数的线程池
            ExecutorService pool = Executors.newCachedThreadPool(); // Executors.newFixedThreadPool(1000);
            for (int i = 0; i < decryptTimes; i++) {
                pool.execute(new Thread(() -> {
                    try {
                        // 3.私钥解密、公钥加密 ---- 解密
                        PKCS8EncodedKeySpec pkcs8EncodedKeySpec2 = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
                        KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");


                        PrivateKey privateKey2 = keyFactory2.generatePrivate(pkcs8EncodedKeySpec2);
                        //Cipher cipher2 = Cipher.getInstance("RSA");
                        Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPPadding");
                        cipher2.init(Cipher.DECRYPT_MODE, privateKey2);
                        byte[] result2 = cipher2.doFinal(result);
                        System.out.println("公钥加密、私钥解密 ---- 解密:" + new String(result2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
            }

            pool.shutdown();

            while (true) {
                if (pool.isTerminated()) {
                    System.out.println("结束了！");
                    long endDencryptTime = System.currentTimeMillis();
                    long totalTimes = (endDencryptTime - endEncryptTime) / 1000;
                    System.out.println("公钥加密、私钥解密 ---- 并发：" + decryptTimes + "个解密时间(单位秒):" + totalTimes);
                    break;
                }
                Thread.sleep(200);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // jdk实现：
    public static void rsa2() {
        try {
            String src = "asdk234324?''sdfsd";
            // 1.初始化发送方密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.println("Public Key:" + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
            System.out.println("Private Key:" + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));

            // 2.私钥加密、公钥解密 ---- 加密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("私钥加密、公钥解密 ---- 加密:" + Base64.encodeBase64String(result));

            // 3.私钥加密、公钥解密 ---- 解密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            result = cipher.doFinal(result);
            System.out.println("私钥加密、公钥解密 ---- 解密:" + new String(result));

            // 4.公钥加密、私钥解密 ---- 加密
            X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
            PublicKey publicKey2 = keyFactory2.generatePublic(x509EncodedKeySpec2);
            Cipher cipher2 = Cipher.getInstance("RSA");
            cipher2.init(Cipher.ENCRYPT_MODE, publicKey2);
            byte[] result2 = cipher2.doFinal(src.getBytes());
            System.out.println("公钥加密、私钥解密 ---- 加密:" + Base64.encodeBase64String(result2));

            // 5.私钥解密、公钥加密 ---- 解密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory5 = KeyFactory.getInstance("RSA");
            PrivateKey privateKey5 = keyFactory5.generatePrivate(pkcs8EncodedKeySpec5);
            Cipher cipher5 = Cipher.getInstance("RSA");
            cipher5.init(Cipher.DECRYPT_MODE, privateKey5);
            byte[] result5 = cipher5.doFinal(result2);
            System.out.println("公钥加密、私钥解密 ---- 解密:" + new String(result5));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
