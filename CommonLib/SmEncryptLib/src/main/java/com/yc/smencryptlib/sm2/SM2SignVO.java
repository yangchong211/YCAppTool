package com.yc.smencryptlib.sm2;
/**
 * SM2签名所计算的值 可以根据实际情况增加删除字段属性
 */
public class SM2SignVO {
	//16进制的私钥
	public String sm2_userd;
	//椭圆曲线点X
	public String x_coord;
	//椭圆曲线点Y
	public String y_coord;
	//SM3摘要Z
	public String sm3_z;
	//明文数据16进制
	public String sign_express;
	//SM3摘要值
	public String sm3_digest;
	//R
	public String sign_r;
	//S
	public String sign_s;
	//R
	public String verify_r;
	//S
	public String verify_s;
	//签名值
	public String sm2_sign;
	//sign 签名  verfiy验签
	public String sm2_type;
	//是否验签成功  true false
	public boolean isVerify;
	public String getX_coord() {
		return x_coord;
	}
	public void setX_coord(String x_coord) {
		this.x_coord = x_coord;
	}
	public String getY_coord() {
		return y_coord;
	}
	public void setY_coord(String y_coord) {
		this.y_coord = y_coord;
	}
	public String getSm3_z() {
		return sm3_z;
	}
	public void setSm3_z(String sm3_z) {
		this.sm3_z = sm3_z;
	}
	public String getSm3_digest() {
		return sm3_digest;
	}
	public void setSm3_digest(String sm3_digest) {
		this.sm3_digest = sm3_digest;
	}
	public String getSm2_signForSoft() {
		return sm2_sign;
	}
	public String getSm2_signForHard() {
		//System.out.println("R:"+getSign_r());
		//System.out.println("s:"+getSign_s());
		return getSign_r()+getSign_s();
	}
	public void setSm2_sign(String sm2_sign) {
		this.sm2_sign = sm2_sign;
	}
	public String getSign_express() {
		return sign_express;
	}
	public void setSign_express(String sign_express) {
		this.sign_express = sign_express;
	}
	public String getSm2_userd() {
		return sm2_userd;
	}
	public void setSm2_userd(String sm2_userd) {
		this.sm2_userd = sm2_userd;
	}
	public String getSm2_type() {
		return sm2_type;
	}
	public void setSm2_type(String sm2_type) {
		this.sm2_type = sm2_type;
	}
	public boolean isVerify() {
		return isVerify;
	}
	public void setVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}
	public String getSign_r() {
		return sign_r;
	}
	public void setSign_r(String sign_r) {
		this.sign_r = sign_r;
	}
	public String getSign_s() {
		return sign_s;
	}
	public void setSign_s(String sign_s) {
		this.sign_s = sign_s;
	}
	public String getVerify_r() {
		return verify_r;
	}
	public void setVerify_r(String verify_r) {
		this.verify_r = verify_r;
	}
	public String getVerify_s() {
		return verify_s;
	}
	public void setVerify_s(String verify_s) {
		this.verify_s = verify_s;
	}
}
