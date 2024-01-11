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
    Function name   :  CommandProcessSW1SW2函数
    message         :  APDU响应报文状态码处理
    ReceiveBuffer   :  APDU响应报文状态码
    ========================================================================*/
    public static String CommandProcessSW1SW2(String ReceiveBuffer)
    {
        if(ReceiveBuffer.contains("63C")) {
            char lastChar = ReceiveBuffer.charAt(ReceiveBuffer.length() - 1);
            if (Character.isDigit(lastChar)) {
                int lastDigit = Character.getNumericValue(lastChar);
                return "校验失败（允许重试次数:" + lastDigit + "）";
            } else {
                return "";
            }
        }

        String message = "";

        switch (ReceiveBuffer)
        {
            //========================================================
            // 响应报文状态码处理
            //========================================================
            case "9000":// 执行成功
                message += "执行成功";

                break;
            case "6100":// 数据被截断
                message += "数据被截断";

                break;
            case "6200":// 警告，信息未提供
                message += "警告 信息未提供";
                break;

            case "6281"://  警告，回送数据可能出错
                message += "警告 回送数据可能出错";
                break;

            case "6282"://  警告，文件长度小于Le
                message += "警告 文件长度小于Le";
                break;

            case "6283"://选择文件无效，文件或密钥校验错误
                message += "警告 选中的文件无效";
                break;

            case "6400":// 状态标志未改变
                message += "状态标志位没有变";
                break;

            case "6581":// 写EEPROM不成功
                message += "出错 内存失败";
                break;

            case "6700"://错误的长度
                message += "出错 长度错误";
                break;

            case "6900"://CLA与线路保护要求不匹配
                message += "出错 不能处理";
                break;

            case "6901"://无效的状态
                message += "无效状态";

                break;
            case "6981"://命令与文件结构不相容
                message += "命令与文件结构不相容";
                break;

            case "6982"://不满足安全状态
                message += "不满足安全状态";
                break;

            case "6983"://密钥被锁死
                message += "密钥被锁死";
                break;

            case "6985"://使用条件不满足
                message += "使用条件不满足";
                break;

            case "6987"://无安全报文
                message += "无安全报文";
                break;

            case "6988"://安全报文数据项不正确
                message += "安全报文数据项不正确";
                break;

            case "6A80"://数据域参数错误
                message += "数据域参数错误";
                break;

            case "6A81"://功能不支持或卡中无MF或卡片已锁定
                message += "功能不支持或卡中无MF或卡片已锁定";
                break;

            case "6A82"://文件未找到
                message += "文件未找到";
                break;

            case "6A83"://记录未找到
                message += "记录未找到";
                break;

            case "6A84"://文件无足够空间
                message += "文件无足够空间";
                break;

            case "6A86"://参数P1 P2 错误
                message += "参数P1 P2 错误";
                break;

            case "6A88"://密钥未找到
                message += "密钥未找到";
                break;

            case "6B00"://在达到Le/Lc字节之前文件结束，偏移量错误
                message += "在达到Le/Lc字节之前文件结束，偏移量错误";
                break;

            case "6Cxx"://Le错误
                message += "Le错误";
                break;

            case "6E00"://无效的CLA
                message += "无效的CLA";
                break;

            case "6F00"://数据无效
                message += "数据无效";
                break;

            case "9302"://MAC错误
                message += "MAC错误";
                break;

            case "9303"://应用已被锁定
                message += "应用已被锁定";
                break;

            case "9401"://金额不足
                message += "金额不足";
                break;

            case "9403"://密钥未找到
                message += "密钥未找到";
                break;

            case "9406"://所需的MAC不可用
                message += "所需的MAC不可用";
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
