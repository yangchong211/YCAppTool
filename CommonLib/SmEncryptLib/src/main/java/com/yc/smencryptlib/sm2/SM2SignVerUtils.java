package com.yc.smencryptlib.sm2;

import com.yc.smencryptlib.Util;

import org.bouncycastle.asn1.*;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.Enumeration;

/**
 * 国密算法的签名、验签
 */
public class SM2SignVerUtils {
	/**
	 * 默认USERID
	 */
	public static String USER_ID = "1234567812345678";
	/**
	 * 私钥签名
	 * 使用SM3进行对明文数据计算一个摘要值
	 * @param privatekey 私钥
	 * @param sourceData 明文数据
	 * @return 签名后的值
	 * @throws Exception
	 */
	public static SM2SignVO Sign2SM2(byte[] privatekey,byte[] sourceData) throws Exception{
		SM2SignVO sm2SignVO = new SM2SignVO();
		sm2SignVO.setSm2_type("sign");
		SM2Factory factory = SM2Factory.getInstance();
		BigInteger userD = new  BigInteger(1,privatekey);
		//System.out.println("userD:"+userD.toString(16));
		sm2SignVO.setSm2_userd(userD.toString(16));

		ECPoint userKey = factory.ecc_point_g.multiply(userD);
		//System.out.println("椭圆曲线点X: "+ userKey.getXCoord().toBigInteger().toString(16));
		//System.out.println("椭圆曲线点Y: "+ userKey.getYCoord().toBigInteger().toString(16));

		SM3Digest sm3Digest = new SM3Digest();
		byte [] z = factory.sm2GetZ(USER_ID.getBytes(), userKey);
		//System.out.println("SM3摘要Z: " + Util.getHexString(z));
		//System.out.println("被加密数据的16进制: " + Util.getHexString(sourceData));
		sm2SignVO.setSm3_z(Util.getHexString(z));
		sm2SignVO.setSign_express(Util.getHexString(sourceData));

		sm3Digest.update(z, 0, z.length);
		sm3Digest.update(sourceData,0,sourceData.length);
		byte [] md = new byte[32];
		sm3Digest.doFinal(md, 0);
		//System.out.println("SM3摘要值: " + Util.getHexString(md));
		sm2SignVO.setSm3_digest(Util.getHexString(md));

		SM2Result sm2Result = new SM2Result();
		factory.sm2Sign(md, userD, userKey, sm2Result);
		//System.out.println("r: " + sm2Result.r.toString(16));
		//System.out.println("s: " + sm2Result.s.toString(16));
		sm2SignVO.setSign_r(sm2Result.r.toString(16));
		sm2SignVO.setSign_s(sm2Result.s.toString(16));

		ASN1Integer d_r = new ASN1Integer(sm2Result.r);
		ASN1Integer d_s = new ASN1Integer(sm2Result.s);
		ASN1EncodableVector v2 = new ASN1EncodableVector();
		v2.add(d_r);
		v2.add(d_s);
		DERSequence sign = new DERSequence(v2);
		String result = Util.byteToHex(sign.getEncoded());
		sm2SignVO.setSm2_sign(result);
		return sm2SignVO;
	}
	/**
	 * 验证签名
	 * @param publicKey 公钥信息
	 * @param sourceData 密文信息
	 * @param signData 签名信息
	 * @return 验签的对象 包含了相关参数和验签结果
	 */
	@SuppressWarnings("unchecked")
	public static SM2SignVO VerifySignSM2(byte[] publicKey,byte[] sourceData,byte[] signData){
		try {
			byte[] formatedPubKey;
			SM2SignVO verifyVo = new SM2SignVO();
			verifyVo.setSm2_type("verify");
			if (publicKey.length == 64) {
				// 添加一字节标识，用于ECPoint解析
				formatedPubKey = new byte[65];
				formatedPubKey[0] = 0x04;
				System.arraycopy(publicKey, 0, formatedPubKey, 1, publicKey.length);
			} else{
				formatedPubKey = publicKey;
			}
			SM2Factory factory = SM2Factory.getInstance();
			ECPoint userKey = factory.ecc_curve.decodePoint(formatedPubKey);

			SM3Digest sm3Digest = new SM3Digest();
			byte [] z = factory.sm2GetZ(USER_ID.getBytes(), userKey);
			//System.out.println("SM3摘要Z: " + Util.getHexString(z));
			verifyVo.setSm3_z(Util.getHexString(z));
			sm3Digest.update(z,0,z.length);
			sm3Digest.update(sourceData,0,sourceData.length);
			byte [] md = new byte[32];
			sm3Digest.doFinal(md, 0);
			//System.out.println("SM3摘要值: " + Util.getHexString(md));
			verifyVo.setSm3_digest(Util.getHexString(md));
			ByteArrayInputStream bis = new ByteArrayInputStream(signData);
			ASN1InputStream dis = new ASN1InputStream(bis);
			SM2Result sm2Result = null;
			ASN1Primitive derObj = dis.readObject();
			Enumeration<ASN1Integer> e = ((ASN1Sequence)derObj).getObjects();
			BigInteger r = ((ASN1Integer) e.nextElement()).getValue();
			BigInteger s = ((ASN1Integer) e.nextElement()).getValue();
			sm2Result = new SM2Result();
			sm2Result.r = r;
			sm2Result.s = s;
			//System.out.println("vr: " + sm2Result.r.toString(16));
			//System.out.println("vs: " + sm2Result.s.toString(16));
			verifyVo.setVerify_r(sm2Result.r.toString(16));
			verifyVo.setVerify_s(sm2Result.s.toString(16));
			factory.sm2Verify(md, userKey, sm2Result.r, sm2Result.s, sm2Result);
			boolean verifyFlag = sm2Result.r.equals(sm2Result.R);
			verifyVo.setVerify(verifyFlag);
			return  verifyVo;
		} catch (IllegalArgumentException e) {
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) throws Exception {
		String text = "这是一段明文";
		byte [] sourceData = text.getBytes();
		//String publicKey ="FA05C51AD1162133DFDF862ECA5E4A481B52FB37FF83E53D45FD18BBD6F32668A92C4692EEB305684E3B9D4ACE767F91D5D108234A9F07936020A92210BA9447";
		//String privatekey = "5EB4DF17021CC719B678D970C620690A11B29C8357D71FA4FF9BF7FB6D89767A";
		String publicKey ="04BB34D657EE7E8490E66EF577E6B3CEA28B739511E787FB4F71B7F38F241D87F18A5A93DF74E90FF94F4EB907F271A36B295B851F971DA5418F4915E2C1A23D6E";
		String privatekey = "0B1CE43098BC21B8E82B5C065EDB534CB86532B1900A49D49F3C53762D2997FA";
		SM2SignVO sign = SM2SignVerUtils.Sign2SM2(Util.hexStringToBytes(privatekey), sourceData);
		SM2SignVO verify = SM2SignVerUtils.VerifySignSM2(Util.hexStringToBytes(publicKey), sourceData, Util.hexStringToBytes(sign.getSm2_signForSoft()));
		System.out.println("签名得到的r值:"+sign.getSign_r()+"\n签名值 "+sign.getSm2_signForSoft());
		System.out.println("验签得到的R值:"+verify.getVerify_r());
		System.err.println("\n验签结果" +verify.isVerify());
	}
}
