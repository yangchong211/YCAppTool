package com.yc.appencryptlib;


import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : md5工具类
 *     revise:
 * </pre>
 */
public final class Md5EncryptUtils {


    /**
     * 用来将字节转换成16进制表示的字符
     * 如何加强md5安全性
     * 1.对明文多次MD5加密，对明文加密之后的MD5串再次进行MD5加密
     * 2.MD5加盐（salt），基本过程是这样的：当需要对明文进行MD5加密的时候，程序会添加一个salt值跟明文一起进行MD5加密，这样可以极大增强MD5被破解的难度。
     */
    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_DIGITS_2 =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private Md5EncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getMd5(String str) {
        byte[] source = str.getBytes();
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            // MD5 的计算结果是一个 128 位的长整数
            byte[] tmp = md.digest();
            // 用字节表示就是 16 个字节
            // 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
            char[] str0 = new char[16 * 2];
            // 进制需要 32 个字符
            // 表示转换结果中对应的字符位置
            int k = 0;
            for (int i = 0; i < 16; i++) {
                // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                // 取第 i 个字节
                byte byte0 = tmp[i];
                // 取字节中高 4 位的数字转换,// >>>
                str0[k++] = HEX_DIGITS_2[byte0 >>> 4 & 0xf];
                // 为逻辑右移，将符号位一起右移
                // 取字节中低 4 位的数字转换
                str0[k++] = HEX_DIGITS_2[byte0 & 0xf];
            }
            // 换后的结果转换为字符串
            s = new String(str0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }


    /**
     * MD5 加密
     *
     * @param data 明文字符串
     * @return 16 进制密文
     */
    public static String encryptMD5ToString(final String data) {
        return encryptMD5ToString(data.getBytes());
    }

    /**
     * MD5 加密
     *
     * @param data 明文字符串
     * @param salt 盐
     * @return 16 进制加盐密文
     */
    public static String encryptMD5ToString(final String data, final String salt) {
        return bytes2HexString(encryptMD5((data + salt).getBytes()));
    }

    /**
     * MD5 加密
     *
     * @param data 明文字节数组
     * @return 16 进制密文
     */
    public static String encryptMD5ToString(final byte[] data) {
        return bytes2HexString(encryptMD5(data));
    }

    /**
     * MD5 加密
     *
     * @param data 明文字节数组
     * @param salt 盐字节数组
     * @return 16 进制加盐密文
     */
    public static String encryptMD5ToString(final byte[] data, final byte[] salt) {
        if (data == null || salt == null) {
            return null;
        }
        byte[] dataSalt = new byte[data.length + salt.length];
        System.arraycopy(data, 0, dataSalt, 0, data.length);
        System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
        return bytes2HexString(encryptMD5(dataSalt));
    }

    /**
     * MD5 加密文件
     *
     * @param filePath 文件路径
     * @return 文件的 MD5 校验码
     */
    public static String encryptMD5File1(final String filePath) {
        File file = isSpace(filePath) ? null : new File(filePath);
        return bytes2HexString(encryptMD5File(file));
    }

    public static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * MD5 加密文件
     *
     * @param file 文件
     * @return 文件的 16 进制密文
     */
    public static String encryptMD5File2(final File file) {
        return bytes2HexString(encryptMD5File(file));
    }

    /**
     * MD5 加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    private static byte[] encryptMD5(final byte[] data) {
        return hashTemplate(data, "MD5");
    }

    /**
     * MD5 加密文件
     *
     * @param file 文件
     * @return 文件的 MD5 校验码
     */
    private static byte[] encryptMD5File(final File file) {
        if (file == null) {
            return null;
        }
        FileInputStream fis = null;
        DigestInputStream digestInputStream;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(fis, md);
            byte[] buffer = new byte[256 * 1024];
            while (true) {
                int read = digestInputStream.read(buffer);
                if (!( read > 0)) {
                    break;
                }
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(fis);
        }
    }

    /**
     * 关闭 IO
     *
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * hash 加密模板
     *
     * @param data      数据
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        if (len <= 0) {
            return null;
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >>> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

}
