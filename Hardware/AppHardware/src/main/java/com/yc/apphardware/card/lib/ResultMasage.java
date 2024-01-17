package com.yc.apphardware.card.lib;

public class ResultMasage {

    /*=======================================================================
    Function name   :  CommandProcesscSTACode函数
    message         :  响应状态码
    mcSTACode        :  //响应状态码  // Result: 00 读取成功  // Result: 非00 读取失败
    ========================================================================*/
    public static String CommandProcesscSTACode(String mcSTACode)
    {
        String message = "数据读取异常:";

        switch (mcSTACode)
        {
            //========================================================
            // CPU卡操作成功处理
            //========================================================
            case "91":
                message +="未找到IC卡/卡号输入错误";
                break;
            case "96":
                message +="未知异常";
                break;
            case "61":
                message +="命令长度异常";
                break;
        }

        return message;
    }

     /*=======================================================================
    Function name   :  CommandProcessCPU函数
    message         :  CPU卡操作成功处理
    ReceiveBuffer   :  CPU卡操作返回值
    ========================================================================*/
    public static String CommandProcesscCMDSuc(String cCMD)
    {
        String message = "";
        switch (cCMD)
        {
            //========================================================
            // CPU卡操作成功处理
            //========================================================
            case "01":
                message +="Open M1 card/scan success";
                break;

            case "02":
                message +="Close M1 card/scan success";
                break;

            case "03":
                message +="Search card/scan success";
                break;

            case "04":
                message +="CPU card Reset success";
                break;

            case "05":
                message +="CPU card send apdu commands success";
                break;

            case "06":
                message +="Remove card success";
                break;

            case "11":
                message +="M1 card authority success";
                break;

            case "12":
                message +="Read M1 card block data success";
                break;

            case "13":
                message +="Write M1 card block data success";
                break;

            case "14":
                message +="Wallet recharge success";
                break;

            case "15":
                message +="Wallet purchase success";
                break;

            case "16":
                message +="Wallet backup success";
                break;

            case "20":// CPU卡复位
                message += "CPU card reset";
                break;

            case "21":// CPU卡发送COS指令
                message += "CPU card send apdu commands success";
                break;
        }

        return message;
    }

    /*=======================================================================
    Function name   :  CommandProcessCPU函数
    message         :  CPU卡操作错误处理
    ReceiveBuffer   :  CPU卡操作返回值
    ========================================================================*/
    public static String CommandProcesscCMDFail(String cCMD)
    {
        String message = "";

        switch (cCMD)
        {
            //========================================================
            // CPU卡操作成功处理
            //========================================================
            case "01":
                message +="Open M1 card/scan failed";
                break;

            case "02":
                message +="Close M1 card/scan failed";
                break;

            case "03":
                message +="Search card/scan failed";
                break;

            case "04":
                message +="CPU card Reset failed";
                break;

            case "05":
                message +="CPU card send apdu commands failed";
                break;

            case "06":
                message +="Remove card failed";
                break;

            case "11":
                message +="M1 card authority failed";
                break;

            case "12":
                message +="Read M1 card block data failed";
                break;

            case "13":
                message +="Write M1 card block data failed";
                break;

            case "14":
                message +="Wallet recharge failed";
                break;

            case "15":
                message +="Wallet purchase failed";
                break;

            case "16":
                message +="Wallet backup failed";
                break;

            case "20":// CPU卡复位
                message += "CPU card Reset failed";
                break;

            case "21":// CPU卡发送COS指令
                message += "CPU card send apdu commands failed";
                break;

            //错误返回处理
            case "F0":
                message += "CPU card Reset failed";
                break;

            case "F1":
                message += "CPU card send apdu commands failed";
                break;
        }
        //current_result=0;
        return message;
    }

    /*=======================================================================
Function name   :  CommandProcessPsam函数
message         :  CPU卡操作成功处理
ReceiveBuffer   :  CPU卡操作返回值
========================================================================*/
    public static String CommandProcessIccCMDSuc(String cCMD)
    {
        String message = "";
        switch (cCMD)
        {
            //========================================================
            // icc卡操作成功处理
            //========================================================
            case "01":
                message +="打开icc卡成功";
                break;

            case "02":
                message +="关闭icc卡成功";
                break;

            case "03":
                message +="icc卡复位成功";
                break;

            case "04":
                message +="icc卡ADPU指令发送成功";
                break;
        }

        return message;
    }

    /*=======================================================================
    Function name   :  CommandProcessCPU函数
    message         :  CPU卡操作错误处理
    ReceiveBuffer   :  CPU卡操作返回值
    ========================================================================*/
    public static String CommandProcessIccCMDFail(String cCMD)
    {
        String message = "";

        switch (cCMD)
        {
            //========================================================
            // icc卡操作失败处理
            //========================================================
            case "01":
                message +="打开icc卡失败";
                break;

            case "02":
                message +="关闭icc卡失败";
                break;

            case "03":
                message +="icc卡复位失败";
                break;

            case "04":
                message +="icc卡ADPU指令发送失败";
                break;
        }

        return message;
    }

    /*=======================================================================
    Function name   :  CommandProcess函数
    Description     :  处理接收到的命令和返回的错误
    ReceiveBuffer   :  无
    message         :  无
    ========================================================================*/
    public static String CommandProcess(String ReceiveBuffer)
    {
        String message = "";

        switch (ReceiveBuffer)
        {
            //========================================================
            //模块设置返回成功处理
            //========================================================
            case "01":
                message += "设置模块为低功耗状态\t成功\t";
                break;

            case "02":
                message += "设置天线和寻卡控制\t成功\t";
                break;

            case "0c":
                message += "设置主动输出卡片ID模式\t成功\t";
                break;

            case "0d":
                message += "设置LED\t成功\t";
                break;

            case "0e":
                message += "设置蜂鸣器\t成功\t";
                break;

            case "0f":
                message += "设置串口波特率\t成功\t";
                break;

            //错误返回处理
            case "E0":
                message += "设置模块进入睡眠模式\t失败\t";
                break;

            case "E1":
                message += "设置天线和寻卡模式\t失败\t";
                break;

            case "EB":
                message += "设置主动输出卡片ID模式\t失败\t";
                break;

            case "EC":
                message += "设置LED状态\t失败\t";
                break;

            case "ED":
                message += "设置蜂鸣器状态\t失败\t";
                break;

            case "EE":
                message += "设置串口波特率\t失败\t";
                break;
            //========================================================
            // CPU卡操作成功处理
            //========================================================
            case "20":// CPU卡复位
                message += "ISO14443 TYPE A CPU卡复位\t成功\t";
                break;

            case "21":// CPU卡发送COS指令
                message += "ISO14443 TYPE A CPU卡发送COS指令\t成功\t";
                break;

            //错误返回处理
            case "F0":
                message += "ISO14443 TYPE A CPU卡复位\t失败\t";
                break;

            case "F1":
                message += "ISO14443 TYPE A CPU卡发送COS指令\t失败\t";
                break;
        }
        return message;
    }

    /*=======================================================================
    Function name   :  CommandSMCMDFail
    Description     :  处理接收到的命令和返回的错误
    ReceiveBuffer   :  无
    message         :  无
    ========================================================================*/
    public static String CommandSMCMDResult(String cCMD) {
        String message = "";

        switch (cCMD) {
            case "00":
                message += "成功";
                break;

            case "04":
                message += "未设置密钥";
                break;

            case "62":
                message += "加/解密数据长度不是16的整数倍";
                break;

            case "98":
                message += "SM2公私密钥对生成失败";
                break;

            case "99":
                message += "SM2签名失败";
                break;

            case "9A":
                message += "SM2验签失败";
                break;
        }
        return message;
    }
}
