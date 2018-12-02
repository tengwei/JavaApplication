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
            String src = "ssSFqvSsapv8TZkAttT79Tfp6eQMu";
            byte[] result;
            long startTimes = System.currentTimeMillis();

            // 1.初始化发送方密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
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
