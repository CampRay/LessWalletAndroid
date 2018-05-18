package com.campray.lesswalletandroid.util;


import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 使用Java自带的MessageDigest类进行MD5或SHA加密
 * (三种方法结果相同)
 * Created by Phills on 4/25/2018.
 */

public class EncryptionUtil {
    /**
     * 使用位运算符，将加密后的数据转换成16进制
     *
     * 由于MD5 与SHA-1均是从MD4 发展而来，它们的结构和强度等特性有很多相似之处
     * SHA-1与MD5 的最大区别在于其摘要比MD5 摘要长 32 比特（1byte=8bit，相当于长4byte，转换16进制后比MD5多8个字符）。
     * 对于强行攻击，：MD5 是2128 数量级的操作，SHA-1 是2160数量级的操作。
     * 对于相同摘要的两个报文的难度：MD5是 264 是数量级的操作，SHA-1 是280 数量级的操作。
     * 因而，SHA-1 对强行攻击的强度更大。 但由于SHA-1 的循环步骤比MD5 多（80:64）且要处理的缓存大（160 比特:128 比特），SHA-1 的运行速度比MD5 慢。
     *
     * @param source 需要加密的字符串
     * @param hashType 加密类型 （MD5 和 SHA）
     * @return
     */
    public static String getHash(String source, String hashType) {
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            md.update(source.getBytes()); // 通过使用 update 方法处理数据,使指定的 byte数组更新摘要
            byte[] encryptStr = md.digest(); // 获得密文完成哈希计算,产生128 位的长整数
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对每一个字节,转换成 16 进制字符的转换
                byte byte0 = encryptStr[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(str); // 换后的结果转换为字符串
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用格式化方式，将加密后的数据转换成16进制（推荐）
     * @param source 需要加密的字符串
     * @param hashType  加密类型 （MD5 和 SHA）
     * @return
     */
    public static String getHash2(String source, String hashType) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(hashType);
            md5.update(source.getBytes());
            for (byte b : md5.digest()) {
                sb.append(String.format("%02X", b)); // 10进制转16进制，X 表示以十六进制形式输出，02 表示不足两位前面补0输出
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用算法，将加密后的数据转换成16进制
     * @param source 需要加密的字符串
     * @param hashType  加密类型 （MD5 和 SHA）
     * @return
     */
    public static String getHash3(String source, String hashType) {
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        StringBuilder sb = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(hashType);
            md5.update(source.getBytes());
            byte[] encryptStr = md5.digest();
            for (int i = 0; i < encryptStr.length; i++) {
                int iRet = encryptStr[i];
                if (iRet < 0) {
                    iRet += 256;
                }
                int iD1 = iRet / 16;
                int iD2 = iRet % 16;
                sb.append(hexDigits[iD1] + "" + hexDigits[iD2]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES加密(ECB分组加密模式)
     * @param plainText 要加密文本
     * @param key 加密私钥
     * @return String base64文本
     * @throws Exception
     */
    public static String desECBEncrypt(String plainText, String key) throws Exception {
        //第一段是加密算法的名称，如DESede实际上是3-DES。这一段还可以放其它的对称加密算法，如Blowfish等
        //第二段是分组加密的模式，除了CBC和ECB之外，还可以是NONE/CFB/QFB等。最常用的就是CBC和ECB了。
        //电子密码本模式（ECB）、加密分组链接模式（CBC）、加密反馈模式（CFB）和输出反馈模式（OFB）
        // DES采用分组加密的方式，将明文按8字节（64位）分组分别加密。如果每个组独立处理，则是ECB。
        // CBC的处理方式是先用初始向量IV对第一组加密，再用第一组的密文作为密钥对第二组加密，然后依次完成整个加密操作。
        // 如果明文中有两个分组的内容相同，ECB会得到完全一样的密文，但CBC则不会。
        //第三段是指最后一个分组的填充方式。大部分情况下，明文并非刚好64位的倍数。
        // 对于最后一个分组，如果长度小于64位，则需要用数据填充至64位。PKCS5Padding是常用的填充方式，如果没有指定，默认的方式就是它。
        String CIPHER_TRANSFORMAT = "DESede/ECB/PKCS5Padding";
        //使用DESede算法加密，DESede是3Des对称加密算法,是Des加密算法的增强版本
        SecretKey deskey = new SecretKeySpec(key.getBytes(), "DESede");
        Cipher c1 = Cipher.getInstance(CIPHER_TRANSFORMAT);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] result = c1.doFinal(plainText.getBytes("UTF-8"));
        //return Base64.encodeBase64String(result);
        return Base64.encodeToString(result,Base64.DEFAULT);
    }

    /**
     * 3DES解密(ECB分组加密模式)
     * @param base64 要解密的base64文本
     * @param key 解密私钥
     * @return String 原始文本
     * @throws Exception
     */
    public static String desECBDecrypt(String base64, String key) throws Exception {
        //第一段是加密算法的名称，如DESede实际上是3-DES。这一段还可以放其它的对称加密算法，如Blowfish等
        //第二段是分组加密的模式，除了CBC和ECB之外，还可以是NONE/CFB/QFB等。最常用的就是CBC和ECB了。
        //电子密码本模式（ECB）、加密分组链接模式（CBC）、加密反馈模式（CFB）和输出反馈模式（OFB）
        // DES采用分组加密的方式，将明文按8字节（64位）分组分别加密。如果每个组独立处理，则是ECB。
        // CBC的处理方式是先用初始向量IV对第一组加密，再用第一组的密文作为密钥对第二组加密，然后依次完成整个加密操作。
        // 如果明文中有两个分组的内容相同，ECB会得到完全一样的密文，但CBC则不会。
        //第三段是指最后一个分组的填充方式。大部分情况下，明文并非刚好64位的倍数。
        // 对于最后一个分组，如果长度小于64位，则需要用数据填充至64位。PKCS5Padding是常用的填充方式，如果没有指定，默认的方式就是它。
        String CIPHER_TRANSFORMAT = "DESede/ECB/PKCS5Padding";
        ////使用DESede算法加密，DESede是3Des对称加密算法,是Des加密算法的增强版本
        SecretKey deskey = new SecretKeySpec(key.getBytes(), "DESede");
        Cipher c1 = Cipher.getInstance(CIPHER_TRANSFORMAT);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        //byte[] result = c1.doFinal(Base64.decodeBase64(base64));
        byte[] result = c1.doFinal(Base64.decode(base64, Base64.DEFAULT));
        return new String(result, "UTF-8");
    }


    /**
     * 3DES加密(CBC分组加密模式)
     * @param plainText 要加密文本
     * @param key 加密私钥,长度必须至少24位及以上
     * @return String base64文本
     * @throws Exception
     */
    public static String desCBCEncrypt(String plainText, String key) throws Exception {
        String CIPHER_TRANSFORMAT = "DESede/CBC/PKCS5Padding";
        //使用DESede算法加密，DESede是3Des对称加密算法,是Des加密算法的增强版本
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        //只取密钥前16位
        DESedeKeySpec dks = new DESedeKeySpec(key.substring(0,24).getBytes());
        Key secretKey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(key.substring(8,8).getBytes());
        Cipher c1 = Cipher.getInstance(CIPHER_TRANSFORMAT);
        c1.init(Cipher.ENCRYPT_MODE, secretKey,iv);
        byte[] result = c1.doFinal(plainText.getBytes("UTF-8"));
        return Base64.encodeToString(result,Base64.DEFAULT);
    }

    /**
     * 3DES解密(CBC分组加密模式)
     * @param base64 要解密的base64文本
     * @param key 解密私钥,长度必须至少24位及以上
     * @return String 原始文本
     * @throws Exception
     */
    public static String desCBCDecrypt(String base64, String key) throws Exception {
        String CIPHER_TRANSFORMAT = "DESede/CBC/PKCS5Padding";
        ////使用DESede算法加密，DESede是3Des对称加密算法,是Des加密算法的增强版本
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        //只取密钥前16位
        DESedeKeySpec dks = new DESedeKeySpec(key.substring(0,24).getBytes());
        Key secretKey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(key.substring(8,16).getBytes());
        Cipher c1 = Cipher.getInstance(CIPHER_TRANSFORMAT);
        c1.init(Cipher.DECRYPT_MODE, secretKey,iv);
        //byte[] result = c1.doFinal(Base64.decodeBase64(base64));
        byte[] result = c1.doFinal(Base64.decode(base64,Base64.DEFAULT));
        return new String(result, "UTF-8");
    }
}
