package demo.ykq.com.apidemo.utils;

import android.util.Base64;

import com.google.gson.Gson;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * <br/>
 * Created on 2016/10/21
 *
 * @author yekangqi
 */

public class AES {
    private static final String VERSION="v1.1.0";
    private static final String ANDROID_KEY="1";
//    public static void main(String[] args) {
//        try {
//            String key="1234567890asdfgh";
//            String before="thisBefore";
//            System.out.println("加密前:"+before);
//            String after=Encrypt(before,key);
//            System.out.println("加密后:"+after);
//            System.out.println("解密:"+Encrypt(after,key));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public static String getEncryptParam(long timeSmap,Object param)
    {
        try {
            String md5Data=VERSION+ANDROID_KEY+timeSmap;
            String key=MD5(md5Data).toUpperCase().substring(8,24);

            return Encrypt(new Gson().toJson(param),key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
//        return bytesToHexString(encrypted);
        return Base64.encodeToString(encrypted,Base64.DEFAULT);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//            byte[] encrypted1 =hexStringToByte(sSrc);
            byte[] encrypted1 = Base64.decode(sSrc,Base64.DEFAULT);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

//    /**
//     * 数组转换成十六进制字符串
//     * @param bArray
//     * @return HexString
//     */
//    public static final String bytesToHexString(byte[] bArray) {
//        StringBuffer sb = new StringBuffer(bArray.length);
//        String sTemp;
//        for (int i = 0; i < bArray.length; i++) {
//            sTemp = Integer.toHexString(0xFF & bArray[i]);
//            if (sTemp.length() < 2)
//                sb.append(0);
//            sb.append(sTemp.toUpperCase());
//        }
//        return sb.toString();
//    }
//    /**
//     * 把16进制字符串转换成字节数组
//     * @param hex
//     * @return byte[]
//     */
//    public static byte[] hexStringToByte(String hex) {
//        int len = (hex.length() / 2);
//        byte[] result = new byte[len];
//        char[] achar = hex.toCharArray();
//        for (int i = 0; i < len; i++) {
//            int pos = i * 2;
//            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
//        }
//        return result;
//    }
//    private static int toByte(char c) {
//        byte b = (byte) "0123456789ABCDEF".indexOf(c);
//        return b;
//    }

    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
