package com.yc.smencryptlib.sm3;

import cn.xjfme.encrypt.utils.Util;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * Created by $(USER) on $(DATE)
 */
public class SM3 {
    public static final byte[] iv = { 0x73, (byte) 0x80, 0x16, 0x6f, 0x49,
            0x14, (byte) 0xb2, (byte) 0xb9, 0x17, 0x24, 0x42, (byte) 0xd7,
            (byte) 0xda, (byte) 0x8a, 0x06, 0x00, (byte) 0xa9, 0x6f, 0x30,
            (byte) 0xbc, (byte) 0x16, 0x31, 0x38, (byte) 0xaa, (byte) 0xe3,
            (byte) 0x8d, (byte) 0xee, 0x4d, (byte) 0xb0, (byte) 0xfb, 0x0e,
            0x4e };

    public static int[] Tj = new int[64];

    static
    {
        for (int i = 0; i < 16; i++)
        {
            Tj[i] = 0x79cc4519;
        }

        for (int i = 16; i < 64; i++)
        {
            Tj[i] = 0x7a879d8a;
        }
    }

    public static byte[] CF(byte[] V, byte[] B)
    {
        int[] v, b;
        v = convert(V);
        b = convert(B);
        return convert(CF(v, b));
    }

    private static int[] convert(byte[] arr)
    {
        int[] out = new int[arr.length / 4];
        byte[] tmp = new byte[4];
        for (int i = 0; i < arr.length; i += 4)
        {
            System.arraycopy(arr, i, tmp, 0, 4);
            out[i / 4] = bigEndianByteToInt(tmp);
        }
        return out;
    }

    private static byte[] convert(int[] arr)
    {
        byte[] out = new byte[arr.length * 4];
        byte[] tmp = null;
        for (int i = 0; i < arr.length; i++)
        {
            tmp = bigEndianIntToByte(arr[i]);
            System.arraycopy(tmp, 0, out, i * 4, 4);
        }
        return out;
    }

    public static int[] CF(int[] V, int[] B)
    {
        int a, b, c, d, e, f, g, h;
        int ss1, ss2, tt1, tt2;
        a = V[0];
        b = V[1];
        c = V[2];
        d = V[3];
        e = V[4];
        f = V[5];
        g = V[6];
        h = V[7];

        int[][] arr = expand(B);
        int[] w = arr[0];
        int[] w1 = arr[1];

        for (int j = 0; j < 64; j++)
        {
            ss1 = (bitCycleLeft(a, 12) + e + bitCycleLeft(Tj[j], j));
            ss1 = bitCycleLeft(ss1, 7);
            ss2 = ss1 ^ bitCycleLeft(a, 12);
            tt1 = FFj(a, b, c, j) + d + ss2 + w1[j];
            tt2 = GGj(e, f, g, j) + h + ss1 + w[j];
            d = c;
            c = bitCycleLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = bitCycleLeft(f, 19);
            f = e;
            e = P0(tt2);

		            /*System.out.print(j+" ");
		            System.out.print(Integer.toHexString(a)+" ");
		            System.out.print(Integer.toHexString(b)+" ");
		            System.out.print(Integer.toHexString(c)+" ");
		            System.out.print(Integer.toHexString(d)+" ");
		            System.out.print(Integer.toHexString(e)+" ");
		            System.out.print(Integer.toHexString(f)+" ");
		            System.out.print(Integer.toHexString(g)+" ");
		            System.out.print(Integer.toHexString(h)+" ");
		            System.out.println("");*/
        }
//		      System.out.println("");

        int[] out = new int[8];
        out[0] = a ^ V[0];
        out[1] = b ^ V[1];
        out[2] = c ^ V[2];
        out[3] = d ^ V[3];
        out[4] = e ^ V[4];
        out[5] = f ^ V[5];
        out[6] = g ^ V[6];
        out[7] = h ^ V[7];

        return out;
    }

    private static int[][] expand(int[] B)
    {
        int W[] = new int[68];
        int W1[] = new int[64];
        for (int i = 0; i < B.length; i++)
        {
            W[i] = B[i];
        }

        for (int i = 16; i < 68; i++)
        {
            W[i] = P1(W[i - 16] ^ W[i - 9] ^ bitCycleLeft(W[i - 3], 15))
                    ^ bitCycleLeft(W[i - 13], 7) ^ W[i - 6];
        }

        for (int i = 0; i < 64; i++)
        {
            W1[i] = W[i] ^ W[i + 4];
        }

        int arr[][] = new int[][] { W, W1 };
        return arr;
    }

    private static byte[] bigEndianIntToByte(int num)
    {
        return back(Util.intToBytes(num));
    }

    private static int bigEndianByteToInt(byte[] bytes)
    {
        return Util.byteToInt(back(bytes));
    }

    private static int FFj(int X, int Y, int Z, int j)
    {
        if (j >= 0 && j <= 15)
        {
            return FF1j(X, Y, Z);
        }
        else
        {
            return FF2j(X, Y, Z);
        }
    }

    private static int GGj(int X, int Y, int Z, int j)
    {
        if (j >= 0 && j <= 15)
        {
            return GG1j(X, Y, Z);
        }
        else
        {
            return GG2j(X, Y, Z);
        }
    }

    // 逻辑位运算函数
    private static int FF1j(int X, int Y, int Z)
    {
        int tmp = X ^ Y ^ Z;
        return tmp;
    }

    private static int FF2j(int X, int Y, int Z)
    {
        int tmp = ((X & Y) | (X & Z) | (Y & Z));
        return tmp;
    }

    private static int GG1j(int X, int Y, int Z)
    {
        int tmp = X ^ Y ^ Z;
        return tmp;
    }

    private static int GG2j(int X, int Y, int Z)
    {
        int tmp = (X & Y) | (~X & Z);
        return tmp;
    }

    private static int P0(int X)
    {
        int y = rotateLeft(X, 9);
        y = bitCycleLeft(X, 9);
        int z = rotateLeft(X, 17);
        z = bitCycleLeft(X, 17);
        int t = X ^ y ^ z;
        return t;
    }

    private static int P1(int X)
    {
        int t = X ^ bitCycleLeft(X, 15) ^ bitCycleLeft(X, 23);
        return t;
    }

    /**
     * 对最后一个分组字节数据padding
     *
     * @param in
     * @param bLen
     *            分组个数
     * @return
     */
    public static byte[] padding(byte[] in, int bLen)
    {
        int k = 448 - (8 * in.length + 1) % 512;
        if (k < 0)
        {
            k = 960 - (8 * in.length + 1) % 512;
        }
        k += 1;
        byte[] padd = new byte[k / 8];
        padd[0] = (byte) 0x80;
        long n = in.length * 8 + bLen * 512;
        byte[] out = new byte[in.length + k / 8 + 64 / 8];
        int pos = 0;
        System.arraycopy(in, 0, out, 0, in.length);
        pos += in.length;
        System.arraycopy(padd, 0, out, pos, padd.length);
        pos += padd.length;
        byte[] tmp = back(Util.longToBytes(n));
        System.arraycopy(tmp, 0, out, pos, tmp.length);
        return out;
    }

    /**
     * 字节数组逆序
     *
     * @param in
     * @return
     */
    private static byte[] back(byte[] in)
    {
        byte[] out = new byte[in.length];
        for (int i = 0; i < out.length; i++)
        {
            out[i] = in[out.length - i - 1];
        }

        return out;
    }

    public static int rotateLeft(int x, int n)
    {
        return (x << n) | (x >> (32 - n));
    }

    private static int bitCycleLeft(int n, int bitLen)
    {
        bitLen %= 32;
        byte[] tmp = bigEndianIntToByte(n);
        int byteLen = bitLen / 8;
        int len = bitLen % 8;
        if (byteLen > 0)
        {
            tmp = byteCycleLeft(tmp, byteLen);
        }

        if (len > 0)
        {
            tmp = bitSmall8CycleLeft(tmp, len);
        }

        return bigEndianByteToInt(tmp);
    }

    private static byte[] bitSmall8CycleLeft(byte[] in, int len)
    {
        byte[] tmp = new byte[in.length];
        int t1, t2, t3;
        for (int i = 0; i < tmp.length; i++)
        {
            t1 = (byte) ((in[i] & 0x000000ff) << len);
            t2 = (byte) ((in[(i + 1) % tmp.length] & 0x000000ff) >> (8 - len));
            t3 = (byte) (t1 | t2);
            tmp[i] = (byte) t3;
        }

        return tmp;
    }

    private static byte[] byteCycleLeft(byte[] in, int byteLen)
    {
        byte[] tmp = new byte[in.length];
        System.arraycopy(in, byteLen, tmp, 0, in.length - byteLen);
        System.arraycopy(in, 0, tmp, in.length - byteLen, byteLen);
        return tmp;
    }

    public static void main(String[] args) {
        byte[] md = new byte[32];
        byte[] msg1 = "ererfeiisgod".getBytes();
        System.out.println(Util.byteToHex(msg1));
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        String s = new String(Hex.encode(md));
        System.out.println(s.toUpperCase());
    }
}
