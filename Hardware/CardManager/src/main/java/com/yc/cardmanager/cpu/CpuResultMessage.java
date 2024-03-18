package com.yc.cardmanager.cpu;

/**
 * CPU卡指令集
 */
public final class CpuResultMessage {

    /**
     * CPU卡常用的APDU指令错误码
     * @param receiveBuffer     APDU响应报文状态码
     * @return              对应的错误解释信息
     */
    public static String commandProcessSW1SW2(String receiveBuffer) {
        if (receiveBuffer.contains("63C")) {
            char lastChar = receiveBuffer.charAt(receiveBuffer.length() - 1);
            if (Character.isDigit(lastChar)) {
                int lastDigit = Character.getNumericValue(lastChar);
                return "校验失败（允许重试次数:" + lastDigit + "）";
            } else {
                return "";
            }
        }
        String message = "";
        switch (receiveBuffer) {
            //========================================================
            // 响应报文状态码处理
            //========================================================
            case "9000":// 执行成功
                message = "执行成功";
                break;
            case "6100":// 数据被截断
                message = "数据被截断";
                break;
            case "6200":// 警告，信息未提供
                message = "警告 信息未提供";
                break;
            case "6281"://  警告，回送数据可能出错
                message = "警告 回送数据可能出错";
                break;
            case "6282"://  警告，文件长度小于Le
                message = "警告 文件长度小于Le";
                break;
            case "6283"://选择文件无效，文件或密钥校验错误
                message = "警告 选中的文件无效";
                break;
            case "6284"://警告 FCI格式与P2指定的不符
                message = "警告 FCI格式与P2指定的不符";
                break;
            case "6300"://警告 鉴别失败
                message = "警告 鉴别失败";
                break;
            case "6400":// 状态标志未改变
                message = "出错 状态标志位没有变";
                break;
            case "6581":// 写EEPROM不成功
                message = "出错 内存失败";
                break;
            case "6700"://错误的长度
                message = "出错 长度错误";
                break;
            case "6882"://出错 不支持安全报文
                message = "出错 不支持安全报文";
                break;
            case "6900"://CLA与线路保护要求不匹配
                message = "出错 不能处理";
                break;
            case "6901"://无效的状态
                message = "出错 无效状态";
                break;
            case "6981"://命令与文件结构不相容，当前文件非所需文件
                message = "出错 命令与文件结构不相容";
                break;
            case "6982"://出错 操作条件（AC）不满足，没有校验PIN
                message = "出错 不满足安全状态";
                break;
            case "6983"://出错 认证方法锁定，PIN被锁定。密钥被锁死
                message = "出错 密钥被锁死";
                break;
            case "6984"://出错 随机数无效，引用的数据无效
                message = "出错 随机数无效，引用的数据无效";
                break;
            case "6985"://使用条件不满足
                message = "出错 使用条件不满足";
                break;
            case "6986"://出错 不满足命令执行条件（不允许的命令，INS有错）
                message = "出错 不满足命令执行条件";
                break;
            case "6987"://无安全报文
                message = "出错 无安全报文";
                break;
            case "6988"://安全报文数据项不正确
                message = "出错 安全报文数据项不正确";
                break;
            case "6A80"://数据域参数错误
                message = "出错 数据域参数错误";
                break;
            case "6A81"://功能不支持或卡中无MF或卡片已锁定
                message = "出错 功能不支持或卡中无MF或卡片已锁定";
                break;
            case "6A82"://文件未找到
                message = "出错 文件未找到";
                break;
            case "6A83"://记录未找到
                message = "出错 该记录未找到";
                break;
            case "6A84"://文件无足够空间
                message = "出错 文件无足够空间";
                break;
            case "6A86"://参数P1 P2 错误
                message = "出错 参数P1 P2 错误";
                break;
            case "6A88"://密钥未找到
                message = "出错 密钥未找到";
                break;
            case "6B00"://在达到Le/Lc字节之前文件结束，偏移量错误
                message = "出错 在达到Le/Lc字节之前文件结束，偏移量错误";
                break;
            case "6Cxx"://Le错误
                message = "Le错误";
                break;
            case "6E00"://无效的CLA
                message = "无效的CLA";
                break;
            case "6F00"://数据无效
                message = "数据无效";
                break;
            case "9301"://出错 资金不足
                message = "出错 资金不足";
                break;
            case "9302"://MAC错误
                message = "出错 MAC错误";
                break;
            case "9303"://应用已被锁定
                message = "出错 应用被永久锁定";
                break;
            case "9401"://金额不足
                message = "出错 交易金额不足";
                break;
            case "9402"://出错 交易计数器达到最大值
                message = "出错 交易计数器达到最大值";
                break;
            case "9403"://出错 密钥索引不支持
                message = "出错 密钥索引不支持";
                break;
            case "9406"://所需的MAC不可用
                message = "出错 所需MAC不可用";
                break;
        }
        return message;
    }

}
