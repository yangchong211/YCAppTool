package com.yc.apphardware.card.lib;

/**
 * @author benjaminwan
 *数据转换工具
 */
public class MyConverterTool {
	//-------------------------------------------------------
	// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
	static public int isOdd(int num)
	{
		return num & 0x1;
	}
	//-------------------------------------------------------
	static public int HexToInt(String inHex)//Hex字符串转int
	{
		return Integer.parseInt(inHex, 16);
	}

	//-------------------------------------------------------
	static public byte HexToByte(String inHex)//Hex字符串转byte
	{
		return (byte) Integer.parseInt(inHex,16);
	}

	//-------------------------------------------------------
	static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
	{
		return String.format("%02x", inByte).toUpperCase();
	}

	//-------------------------------------------------------
	static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
	{
		StringBuilder strBuilder=new StringBuilder();
		int j=inBytArr.length;
		for (int i = 0; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString();
	}

	//-------------------------------------------------------
	static public String ByteArrToHex(byte[] inBytArr, int offset, int byteCount)//字节数组转转hex字符串，可选长度
	{
		StringBuilder strBuilder=new StringBuilder();
		int j=byteCount;
		for (int i = offset; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
		}
		return strBuilder.toString();
	}

	//-------------------------------------------------------
	//转hex字符串转字节数组
	static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
	{
		int hexlen = inHex.length();
		byte[] result;
		if (isOdd(hexlen)==1)
		{//奇数
			hexlen++;
			result = new byte[(hexlen/2)];
			inHex = "0" + inHex;
		}else {//偶数
			result = new byte[(hexlen/2)];
		}
		int j = 0;
		for (int i = 0; i < hexlen; i+=2)
		{
			result[j] = HexToByte(inHex.substring(i, i + 2));
			j++;
		}
		return result;
	}

	static public byte CheckSum(byte[] data, int offset, int length)
	{
		byte temp = 0;
		for (int i = offset; i < length + offset; i++)
		{
			temp ^= data[i];
		}
		return temp;
	}


	/**
	 * 获取BCC校验码
	 * @param data
	 * @param start 开始位置0
	 * @param end  字节数组长度
	 * @return
	 */
	public static String getBCC(byte[] data, int start, int end) {
		String ret = "";
		byte BCC[] = new byte[1];
		for (int i = start; i < data.length; i++) {
			if (i == end) {
				break;
			}
			BCC[0] = (byte) (BCC[0] ^ data[i]);
		}
		String hex = Integer.toHexString(BCC[0] & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		ret += hex.toUpperCase();
		return ret;
	}

	/**
	 * 将一个整形化为十六进制，并以字符串的形式返回
	 */
	private final static String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

	public static String byteToHex(int n) {
		if (n < 0) {
			n = n + 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexArray[d1] + hexArray[d2];
	}
	private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 * 方法一：
	 * byte[] to hex string
	 *
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		// 一个byte为8位，可用两个十六进制位标识
		char[] buf = new char[bytes.length * 2];
		int a = 0;
		int index = 0;
		for (byte b : bytes) { // 使用除与取余进行转换
			if (b < 0) {
				a = 256 + b;
			} else {
				a = b;
			}

			buf[index++] = HEX_CHAR[a / 16];
			buf[index++] = HEX_CHAR[a % 16];
		}

		return new String(buf);
	}

	/**
	 * 将16进制字符串转换为byte[]
	 *
	 * @param str
	 * @return
	 */
	public static byte[] toBytes(String str) {
		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}

		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}

		return bytes;
	}

	/**
	 * 从一个byte[]数组中截取一部分
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
		return bs;
	}

	/**
	 * 去掉byte[]中填充的0 转为String
	 * @param buffer
	 * @return
	 */
	public static String byteToStr(byte[] buffer) {
		try {
			int length = 0;
			for (int i = 0; i < buffer.length; ++i) {
				if (buffer[i] == 0) {
					length = i;
					break;
				}
			}
			return new String(buffer, 0, length, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean isByteArrayEmpty(byte[] bytes) {

		if (bytes[0] != 0x00) {
			return false;

		}
		return true;
	}
}