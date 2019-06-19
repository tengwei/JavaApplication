//package org.encrypt;
//
//import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
//
//import org.apache.commons.io.output.ByteArrayOutputStream;
//
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import java.io.IOException;
//import java.security.*;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//
//public class RSAHelper {
//
//    public static final String PUBLIC_KEY_VALUES = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0bb7yxuvRIWOUxlTbTXE\n" +
//            "cKX5f4Q6+BBOf1fYJKic9l6Wf1QPmyt6ML7PywaPH861D7eYoQl0bGNK2fKsgcAG\n" +
//            "ZzObG5CpmP8ESnSzqcjltdAgx+neCZQy7yUmXUIhpBEQMN80CNYoasOxeZTdPh2w\n" +
//            "zhlmwa27ubkvpINtKUfZbg8sQ5wiDGbLM32ej8z2Rl8DNY4vrusJaNXB7LWaRQm7\n" +
//            "4lPhLN2B/hMv/Ktif4iNxUCYDY97Xws2kVVu7ffWkn4rnhiCrTw2XMZRjIJq3a4o\n" +
//            "4zGWUhYm0usVOLz+yG22cLSCIDhM8tBXL2f3960l4OIQbSObfQkGnqlmi0Fe686p\n" +
//            "gwIDAQAB";
//    public static final String PRIVATE_KEY_VALUES = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDRtvvLG69EhY5T\n" +
//            "GVNtNcRwpfl/hDr4EE5/V9gkqJz2XpZ/VA+bK3owvs/LBo8fzrUPt5ihCXRsY0rZ\n" +
//            "8qyBwAZnM5sbkKmY/wRKdLOpyOW10CDH6d4JlDLvJSZdQiGkERAw3zQI1ihqw7F5\n" +
//            "lN0+HbDOGWbBrbu5uS+kg20pR9luDyxDnCIMZsszfZ6PzPZGXwM1ji+u6wlo1cHs\n" +
//            "tZpFCbviU+Es3YH+Ey/8q2J/iI3FQJgNj3tfCzaRVW7t99aSfiueGIKtPDZcxlGM\n" +
//            "gmrdrijjMZZSFibS6xU4vP7IbbZwtIIgOEzy0FcvZ/f3rSXg4hBtI5t9CQaeqWaL\n" +
//            "QV7rzqmDAgMBAAECggEBAIpVZ5Y8tso+RvmttQhO9TtRYFirArLrHryFV4Am8RLa\n" +
//            "pe9rpbe3YCyTUUIdG3/hvDGX68geEnwEqzVFDGGyJwcgVWpDbHwNi+kJPhH7APuD\n" +
//            "RHxaWip7ZXW2Ta4ql0JANyvlr888ZQC4AXOvrJjywNPSkaOkVDZYX4LnZrRaPqSH\n" +
//            "j9iVGuKK4nbqbXP1tuLyto6bsn3L2KuwrBthtiDPaZ1cRm3aSu/XND030iSGloW/\n" +
//            "/f6vHI3U//cPFPdi8p54nbpGTB0E66D2xbfK5+z9dZYoFDZUdMsloR1JgS4vQErr\n" +
//            "17165RS7cpWx2HejiKat6qmNFXkHbREpcLDT+lq91LECgYEA7wqm+mIFIYyXEARc\n" +
//            "lKKabCKaufKE+7kyBGpAEyon1lYZHw62dCYkGmuXULam4IAz7TesWsbG5SEgDx/A\n" +
//            "jeFSoC6NTyyTzSh1I08UHdOJ4xhAa67GnlJG7WLNZFK+itl7v3VOOlGsAYAxaG5q\n" +
//            "kQKQ1mKk6C6oGRpQ0aqKxVhoDc0CgYEA4Je2flPUYs33qy4m09UBFx6Xf91/5LvL\n" +
//            "iTtEo+HGcMzxmfeV3sawUwtjHhyippN5lTR80JLbYoEvoUMyKt+N5Jnic206alSH\n" +
//            "hujsQnO4LCRHz/XwkHnB8Ncd3J67Uaq9PUPY630NIA/ChBAQchsqntJSdVn65PZf\n" +
//            "mw7+hO2WxI8CgYB4eX/qlVhMrlS8R9Z5OvJlKZOdv/LyA0aIHxyoDAkD52TF2F5w\n" +
//            "b4CmqC8dCNFWOIbzOanuHlzDwkwsEy6y0ysXfB7QFoFvVsKixwo2dhT6lZByNSX5\n" +
//            "STJiFfe6ZlGOHUpXFkIU9nCgWQGNxoiDCS4CPrkqI8mozTCKW0+RYpseyQKBgQCR\n" +
//            "cK21UQQQl+Dy8YgjVaTHHABvxTi1HwfHbqIcnnCrS4yJcFOVWIWGwbEGJvUNeiMa\n" +
//            "BEtvpip7t7zoaWNrcCmrCBwlM27IvMSnEN8uiVGTBEuc2F9YsABvvl6QKBqV4EN8\n" +
//            "ERvAI9MEGDCW5PBBdGY9Q2YyqHpZG1L+Ts9ztYgU8QKBgB7bP9eNMrDzgZ6i9pFP\n" +
//            "djlcJ+NE0veOLwTA1eQhiIZFSJTI5eyiePTYNIS3GwIyGauzePw5LeBb6rLVBt4Y\n" +
//            "NAOgJDeHbPV2bnstONjE7FyUswJivIVD3n3UaVGgBTe6xc468Ws4rmeKZ8/Ph1Nq\n" +
//            "ylaViyPWz486JAibF3Kudl5B";
//    /**
//     * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
//     */
//    public static final int KEY_SIZE = 2048;
//
//    /**
//     * 生成公钥、私钥对(keysize=1024)
//     */
//    public static RSAHelper.KeyPairInfo getKeyPair() {
//        return getKeyPair(KEY_SIZE);
//    }
//
//    /**
//     * 生成公钥、私钥对
//     *
//     * @param keySize
//     * @return
//     */
//    public static RSAHelper.KeyPairInfo getKeyPair(int keySize) {
//        try {
//            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//            keyPairGen.initialize(keySize);
//            // 生成一个密钥对，保存在keyPair中
//            KeyPair keyPair = keyPairGen.generateKeyPair();
//            // 得到私钥
//            RSAPrivateKey oraprivateKey = (RSAPrivateKey) keyPair.getPrivate();
//            // 得到公钥
//            RSAPublicKey orapublicKey = (RSAPublicKey) keyPair.getPublic();
//
//            RSAHelper.KeyPairInfo pairInfo = new RSAHelper.KeyPairInfo(keySize);
//            //公钥
//            byte[] publicKeybyte = orapublicKey.getEncoded();
//            String publicKeyString = Base64.encode(publicKeybyte);
//            pairInfo.setPublicKey(publicKeyString);
//            //私钥
//            byte[] privateKeybyte = oraprivateKey.getEncoded();
//            String privateKeyString = Base64.encode(privateKeybyte);
//            pairInfo.setPrivateKey(privateKeyString);
//
//            return pairInfo;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 获取公钥对象
//     *
//     * @param publicKeyBase64
//     * @return
//     * @throws InvalidKeySpecException
//     * @throws NoSuchAlgorithmException
//     */
//    public static PublicKey getPublicKey(String publicKeyBase64)
//            throws InvalidKeySpecException, NoSuchAlgorithmException, Base64DecodingException {
//
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        X509EncodedKeySpec publicpkcs8KeySpec =
//                new X509EncodedKeySpec(Base64.decode(publicKeyBase64));
//        PublicKey publicKey = keyFactory.generatePublic(publicpkcs8KeySpec);
//        return publicKey;
//    }
//
//    /**
//     * 获取私钥对象
//     *
//     * @param privateKeyBase64
//     * @return
//     * @throws NoSuchAlgorithmException
//     * @throws InvalidKeySpecException
//     */
//    public static PrivateKey getPrivateKey(String privateKeyBase64)
//            throws NoSuchAlgorithmException, InvalidKeySpecException, Base64DecodingException {
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PKCS8EncodedKeySpec privatekcs8KeySpec =
//                new PKCS8EncodedKeySpec(Base64.decode(privateKeyBase64));
//        PrivateKey privateKey = keyFactory.generatePrivate(privatekcs8KeySpec);
//        return privateKey;
//    }
//
//    /**
//     * 使用工钥加密
//     *
//     * @param content         待加密内容
//     * @param publicKeyBase64 公钥 base64 编码
//     * @return 经过 base64 编码后的字符串
//     */
//    public static String encipher(String content, String publicKeyBase64) {
//        return encipher(content, publicKeyBase64, KEY_SIZE / 8 - 11);
//    }
//
//    /**
//     * 使用公司钥加密（分段加密）
//     *
//     * @param content         待加密内容
//     * @param publicKeyBase64 公钥 base64 编码
//     * @param segmentSize     分段大小,一般小于 keySize/8（段小于等于0时，将不使用分段加密）
//     * @return 经过 base64 编码后的字符串
//     */
//    public static String encipher(String content, String publicKeyBase64, int segmentSize) {
//        try {
//            PublicKey publicKey = getPublicKey(publicKeyBase64);
//            return encipher(content, publicKey, segmentSize);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 分段加密
//     *
//     * @param ciphertext  密文
//     * @param key         加密秘钥
//     * @param segmentSize 分段大小，<=0 不分段
//     * @return
//     */
//    public static String encipher(String ciphertext, java.security.Key key, int segmentSize) {
//        try {
//            // 用公钥加密
//            byte[] srcBytes = ciphertext.getBytes();
//
//            // Cipher负责完成加密或解密工作，基于RSA
//            Cipher cipher = Cipher.getInstance("RSA");
//            // 根据公钥，对Cipher对象进行初始化
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            byte[] resultBytes = null;
//
//            if (segmentSize > 0)
//                resultBytes = cipherDoFinal(cipher, srcBytes, segmentSize); //分段加密
//            else
//                resultBytes = cipher.doFinal(srcBytes);
//            String base64Str = org.apache.commons.codec.binary.Base64.encodeBase64String(resultBytes);
//            return base64Str;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 分段大小
//     *
//     * @param cipher
//     * @param srcBytes
//     * @param segmentSize
//     * @return
//     * @throws IllegalBlockSizeException
//     * @throws BadPaddingException
//     * @throws IOException
//     */
//    public static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
//            throws IllegalBlockSizeException, BadPaddingException, IOException {
//        if (segmentSize <= 0)
//            throw new RuntimeException("分段大小必须大于0");
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int inputLen = srcBytes.length;
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段解密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > segmentSize) {
//                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
//            } else {
//                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * segmentSize;
//        }
//        byte[] data = out.toByteArray();
//        out.close();
//        return data;
//    }
//
//    /**
//     * 使用私钥解密
//     *
//     * @param contentBase64    待加密内容,base64 编码
//     * @param privateKeyBase64 私钥 base64 编码
//     * @return
//     * @segmentSize 分段大小
//     */
//    public static String decipher(String contentBase64, String privateKeyBase64) {
//        return decipher(contentBase64, privateKeyBase64, KEY_SIZE / 8);
//    }
//
//    /**
//     * 使用私钥解密（分段解密）
//     *
//     * @param contentBase64    待加密内容,base64 编码
//     * @param privateKeyBase64 私钥 base64 编码
//     * @return
//     * @segmentSize 分段大小
//     */
//    public static String decipher(String contentBase64, String privateKeyBase64, int segmentSize) {
//        try {
//            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
//            return decipher(contentBase64, privateKey, segmentSize);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 分段解密
//     *
//     * @param contentBase64 密文
//     * @param key           解密秘钥
//     * @param segmentSize   分段大小（小于等于0不分段）
//     * @return
//     */
//    public static String decipher(String contentBase64, java.security.Key key, int segmentSize) {
//        try {
//            // 用私钥解密
//            byte[] srcBytes = org.apache.commons.codec.binary.Base64.decodeBase64(contentBase64);
//            // Cipher负责完成加密或解密工作，基于RSA
//            Cipher deCipher = Cipher.getInstance("RSA");
//            // 根据公钥，对Cipher对象进行初始化
//            deCipher.init(Cipher.DECRYPT_MODE, key);
//            byte[] decBytes = null;//deCipher.doFinal(srcBytes);
//            if (segmentSize > 0)
//                decBytes = cipherDoFinal(deCipher, srcBytes, segmentSize); //分段加密
//            else
//                decBytes = deCipher.doFinal(srcBytes);
//
//            String decrytStr = new String(decBytes);
//            return decrytStr;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 秘钥对
//     */
//    public static class KeyPairInfo {
//        String privateKey;
//        String publicKey;
//        int keySize = 0;
//
//        public KeyPairInfo(int keySize) {
//            setKeySize(keySize);
//        }
//
//        public KeyPairInfo(String publicKey, String privateKey) {
//            setPrivateKey(privateKey);
//            setPublicKey(publicKey);
//        }
//
//        public String getPrivateKey() {
//            return privateKey;
//        }
//
//        public void setPrivateKey(String privateKey) {
//            this.privateKey = privateKey;
//        }
//
//        public String getPublicKey() {
//            return publicKey;
//        }
//
//        public void setPublicKey(String publicKey) {
//            this.publicKey = publicKey;
//        }
//
//        public int getKeySize() {
//            return keySize;
//        }
//
//        public void setKeySize(int keySize) {
//            this.keySize = keySize;
//        }
//    }
//}
