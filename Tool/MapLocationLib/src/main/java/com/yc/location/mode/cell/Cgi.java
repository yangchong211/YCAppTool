package com.yc.location.mode.cell;

import com.yc.location.constant.Constants;

public class Cgi {
  
    /**
     * 手机默认CGI类型
     */
    public static final int iDefCgiT = 0;
	/**
     * 手机GSM CGI类型（新接口反射获得到的）
     */
    public static final int iGsmT = 1;
	/**
	 * 手机WCDMA CGI类型
	 */
	public static final int iCdmaT = 2;
	/**
     * 手机WCDMA CGI类型
     */
    public static final int iWcdmaT = 3;
	/**
     * 手机WCDMA CGI类型
     */
    public static final int iLteT = 4;
	/**
	 * 手机GSM CGI类型（通过老接口获得到的）
	 */
	public static final int iGsmOldT = 5;

	public String mcc = "";
	public String mnc_sid = "";
	public int cid_bid = 0;
	public int lat = 0;
	public int lon = 0;
//	public int sid = 0;
	//位置区域码
	public int lac_nid = 0;
//	public int cid_bid = 0;
	public int sig = Constants.iDefCgiSig;
	public int type = iDefCgiT;

	public Cgi() {
		//
	}
}