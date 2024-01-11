package com.yc.apphardware.card.lib;

import android.util.Log;



public class CardReadManager {

	private static final String TAG = CardReadManager.class.getSimpleName();
	MySerialPort mySerialPort;

	//防冲撞，用来保存上一次刷卡卡号
	private static String mCardNo = null;

    //防冲撞开关
	private boolean AntiSwipe_status = true;

    //debug开关
    private boolean debug = false;

    public static final byte KEY_MODE_A = 0x60;
	public static final byte KEY_MODE_B = 0x61;

    private static volatile CardReadManager INSTANCE;


	//枚举通过卡类型得到卡号
    public enum CardType {
        INVALID_CARD("", (byte)0x00), M1_CARD("", (byte)0x01), CPU_CARD("", (byte)0x02), OTHER_CARD("", (byte)0x03);

        private String cardNo;
        private byte cardType;

        private CardType(String cardNo, byte cardType) {
            this.cardNo = cardNo;
            this.cardType = cardType;
        }

		private CardType setCardType(String cardNo) {
            this.cardNo = cardNo;
            return this;
		}

		private CardType setCardTypeByte(byte cardType) {
            this.cardType = cardType;
            return this;
		}

		public String getCardNo() {
            return this.cardNo;
		}

        public byte getCardType() {
            return this.cardType;
        }
		
        @Override
        public String toString() {
            return this.cardNo;
        }
    }


    private CardReadManager() {
        mySerialPort  = new MySerialPort();
    }

    public static CardReadManager getInstance() {
        if (INSTANCE == null) {
            synchronized (CardReadManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CardReadManager();
                }
            }
        }
        return INSTANCE;
    }

    //打开读卡模块
    public boolean CardOpen() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x52, (byte)0x01);
            if(rcvData == null) {   //timeout
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //关闭读卡模块
    public boolean CardClose() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x52, (byte)0x02);
            if(rcvData == null) {   //timeout
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }

    //寻卡
    public String CardSearch() {
        try {
            String rcvData = mySerialPort.setCosOrder(new byte[]{(byte)0xFF, (byte)0xFF}, (byte)0x52, (byte)0x03);
            if(rcvData == null) {   //timeout
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6] == (byte)0x00) {  // 状态码为(0x00)“成功”
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] cardNoBuff = new byte[rcvDataByte.length - 9 - 2];
                System.arraycopy(rcvDataByte, 9, cardNoBuff, 0, cardNoBuff.length);
                //第一字节是卡类型，最后字节是确认值，所以去掉头尾各一字节，才是卡UID
                byte[] cardNoByte = new byte[cardNoBuff.length - 2];//获取到的卡号
                System.arraycopy(cardNoBuff, 1, cardNoByte, 0, cardNoByte.length);
                String cardNo = MyConverterTool.ByteArrToHex(cardNoByte);
                cardNo = cardNo.replace(" ", "");

                if(getDebugStatus())
				    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD) + ",card number:" + cardNo);

                //防撞卡
                if((mCardNo == null) || (mCardNo != null && !cardNo.equals(mCardNo))) {
                    if(getAntiSwipeCardStatus())
                        mCardNo = cardNo;
                    return cardNo;
                } else {
                    if(getDebugStatus())
                        Log.e(TAG, "Please don't swipe the card repeatedly!");
                }
            } else {  // 状态码为“非成功”
                if(getDebugStatus())
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                mCardNo = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //寻卡类型：可返回卡类型值+卡号
    public CardType SearchCardType() {
        try {
            String rcvData = mySerialPort.setCosOrder(new byte[]{(byte)0xFF, (byte)0xFF}, (byte)0x52, (byte)0x03);
            if(rcvData == null) {   //超时
                return CardType.INVALID_CARD.setCardType(null);
            }

            byte cardType = 0x00;
            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6] == (byte)0x00) {  // 状态码为(0x00)“成功”
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] cardNoBuff = new byte[rcvDataByte.length - 9 - 2];
                System.arraycopy(rcvDataByte, 9, cardNoBuff, 0, cardNoBuff.length);

                //卡类型
                cardType = cardNoBuff[0];
                //第一字节是卡类型，最后字节是确认值，所以去掉尾字节，是卡UID
                byte[] cardNoByte = new byte[cardNoBuff.length - 2];//获取到的卡号
                System.arraycopy(cardNoBuff, 1, cardNoByte, 0, cardNoByte.length);
                String cardNo = MyConverterTool.ByteArrToHex(cardNoByte);
                cardNo = cardNo.replace(" ", "");

                if(getDebugStatus())
				    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD) + ",card number:" + cardNo);

                //防撞卡
                if((mCardNo == null) || (mCardNo != null && !cardNo.equals(mCardNo))) {
                    if(getAntiSwipeCardStatus())
                        mCardNo = cardNo;
                    if(cardType == 0x45)
                        return CardType.M1_CARD.setCardTypeByte(cardType).setCardType(cardNo);
                    else if(cardType == 0x41)
                        return CardType.CPU_CARD.setCardTypeByte(cardType).setCardType(cardNo);
                    else
                        return CardType.OTHER_CARD.setCardTypeByte(cardType).setCardType(cardNo);
                } else {
                    if(getDebugStatus())
                        Log.e(TAG, "Please don't swipe the card repeatedly!");
                }
            } else {  // 状态码为“非成功”
                if(getDebugStatus())
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                mCardNo = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CardType.INVALID_CARD.setCardType(null);
    }

    //cpu卡复位
    public byte[] CardCpuReset() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x52, (byte)0x04);
            if(rcvData == null) {   //timeout
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] resetData = new byte[rcvDataByte.length - 9 - 2];
                System.arraycopy(rcvDataByte, 9, resetData, 0, resetData.length);
                return resetData;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }

    //CPU卡apdu操作
    public byte[] CardCpuSendCosCmd(byte[] CosCmd) {
        try {
            String rcvData = mySerialPort.setCosOrder(CosCmd, (byte)0x52, (byte)0x05);
            if(rcvData == null) {   //超时
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);

            if(rcvDataByte[6] == 0x00){// 状态码为(0x00)“成功”
                byte[] SW1SW2data = new byte[2];
                System.arraycopy(rcvDataByte, rcvDataByte.length - 4, SW1SW2data, 0, SW1SW2data.length);
                String SW1SW2 = MyConverterTool.bytesToHex(SW1SW2data);
                if(getDebugStatus())
                    Log.i(TAG, "SW1SW2:" + SW1SW2);
                if(SW1SW2.equals("9000")) {
                    //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                    //尾部4字节：状态码2字节 + 校验码 + 包尾
                    byte[] apduData = new byte[rcvDataByte.length - 9 - 2];
                    System.arraycopy(rcvDataByte, 9, apduData, 0, apduData.length);
                    if(getDebugStatus())
                        Log.i(TAG, "APDU data:" + MyConverterTool.ByteArrToHex(apduData));
                    return apduData;
                }
                else {
                    if(getDebugStatus())
                        Log.e(TAG,"error code ：" + ResultMasage.CommandProcessSW1SW2(SW1SW2));
                    return SW1SW2data;
                }
            }else{// 状态码为“非成功”
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //认证
    public boolean CardM1Authority(byte keyMode, byte[] key, byte[] cardNo, byte cardBlock) {
        if(key.length != 6) {
            Log.e(TAG, "key is invalid!");
            return false;
        }

		try {
            byte[] byte_order = new byte[1 + key.length + cardNo.length + 1];
            byte_order[0] = (byte)keyMode;
            System.arraycopy(key, 0, byte_order, 1, key.length);
            System.arraycopy(cardNo, 0, byte_order, (1 + key.length), cardNo.length);
            byte_order[byte_order.length - 1] = (byte)cardBlock;

            String rcvData = mySerialPort.setCosOrder(byte_order, (byte)0x52, (byte)0x11);
            if(rcvData == null) {   //timeout
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, "block " + cardBlock + " key is: " + MyConverterTool.ByteArrToHex(key) + ", Authority success");
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, "block " + cardBlock + " key is: " + MyConverterTool.ByteArrToHex(key) + ", Authority failed");
                }
            }
		} catch (Exception e) {
            e.printStackTrace();
        }

		if(getAntiSwipeCardStatus()) {
		    mCardNo = null;
        }

		return false;
    }

    //读
	public byte[] CardM1BlockRead(byte cardBlock) {
        try {
            String rcvData = mySerialPort.setCosOrder(new byte[]{cardBlock}, (byte)0x52, (byte)0x12);
            if(rcvData == null) {   //timeout
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);

			if(rcvDataByte[6] == 0x00){// 状态码为(0x00)“成功”
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] blockData = new byte[rcvDataByte.length - 9 - 2];
				System.arraycopy(rcvDataByte, 9, blockData, 0, blockData.length);
                if(getDebugStatus()) {
					Log.i(TAG, "block " + cardBlock + " read data: " + MyConverterTool.ByteArrToHex(blockData));
                }
				return blockData;
			}else{// 状态码为“非成功”
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
			}

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //写
	public boolean CardM1BlockWrite(byte cardBlock, byte[] blockData) {
        try {
			byte[] byte_order = new byte[1 + 16];			
            byte_order[0] = (byte)cardBlock;
            System.arraycopy(blockData, 0, byte_order, 1, blockData.length);
            if(blockData.length < 16) {
				if(getDebugStatus()) {
				    Log.i(TAG, "The data length is less than 16 bytes, it will be automatically filled.");
				}
				for(int i = blockData.length; i < 16; i ++)
				    byte_order[i + 1] = (byte)0xFF;
            }
            String	rcvData = mySerialPort.setCosOrder(byte_order, (byte)0x52, (byte)0x13);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);

            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    byte[] write_data = new byte[16];
                    System.arraycopy(byte_order, 1, write_data, 0, write_data.length);
                    Log.i(TAG, "block " + cardBlock + " write data: " + MyConverterTool.ByteArrToHex(write_data));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //钱包初始化
    public boolean CardM1PurseInit(byte keyMode, byte[] key, byte[] cardNo, byte cardBlock, byte[] initPurse) {
        //密码块不能用作钱包
        if (((int) cardBlock % 4) == 3) {
            Log.e(TAG, "CardM1PurseInit, the cardBlock can't use to purse!");
            return false;
        }

        if (initPurse.length  != 4) {
            Log.e(TAG, "CardM1PurseInit, init purse length must be 4!");
            return false;
        }

        boolean authority = CardM1Authority(keyMode, key, cardNo, cardBlock);
        if(authority == false) {
            Log.e(TAG, "CardM1PurseInit, key authority failed!");
            return false;
        }

        byte[] initData = new byte[16];
        int initPurseLength =  initPurse.length;
        System.arraycopy(initPurse, 0, initData, 0, initPurseLength);   //0-4

        for(int i = 0; i < initPurseLength; i ++)
            initData[initPurseLength + i] = (byte) ~(initPurse[i]); //5-8取反

        System.arraycopy(initPurse, 0, initData, initPurseLength * 2, initPurseLength); //9-12

        initData[initPurseLength * 3] = cardBlock;
        initData[initPurseLength * 3 + 1] = (byte) ~cardBlock;
        initData[initPurseLength * 3 + 2] = cardBlock;
        initData[initPurseLength * 3 + 3] = (byte) ~cardBlock;

        boolean write = CardM1BlockWrite(cardBlock, initData);
        if(write == false) {
            Log.e(TAG, "init purse failed!");
            return false;
        }

        return true;
    }

    //钱包读取
    public byte[] CardM1PurseRead(byte keyMode, byte[] key, byte[] cardNo, byte cardBlock) {
        //密码块不能用作钱包
        if (((int) cardBlock % 4) == 3) {
            Log.e(TAG, "CardM1PurseRead, the cardBlock can't use to purse!");
            return null;
        }

        boolean authority = CardM1Authority(keyMode, key, cardNo, cardBlock);
        if(authority == false) {
            Log.e(TAG, "CardM1PurseRead, key authority failed!");
            return null;
        }

        byte[] readByte = CardM1BlockRead(cardBlock);

        for(int i = 0; i < 4; i ++) {
            if(readByte[i] != ~readByte[i + 4] || readByte[i] != readByte[i + 8]) {
                Log.e(TAG, "CardM1PurseRead, purse data format error!!");
                return null;
            }
        }

        if((int)readByte[12] != (int)cardBlock || (int)readByte[13] != ~cardBlock
                || (int)readByte[14] != (int)cardBlock || (int)readByte[15] != ~cardBlock) {
            Log.e(TAG, "CardM1PurseRead, purse block num format error!!");
            return null;
        }

        byte[] purseData = new byte[4];
        System.arraycopy(readByte, 0, purseData, 0, purseData.length);
        return purseData;
    }

    //钱包充值
    public boolean CardM1PurseRecharge(byte keyMode, byte[] key, byte[] cardNo, byte cardBlock, byte[] rechargeNum) {
        //密码块不能用作钱包
        if (((int) cardBlock % 4) == 3) {
            Log.e(TAG, "the cardBlock can't use to purse!");
            return false;
        }

        boolean authority = CardM1Authority(keyMode, key, cardNo, cardBlock);
        if(authority == false) {
            Log.e(TAG, "CardM1PurseRecharge, key authority failed!");
            return false;
        }

        byte[] byte_order = new byte[1 + 2 + 4];
        byte_order[0] = cardBlock;
        byte_order[1] = 0x00;
        byte_order[2] = 0x00;
        System.arraycopy(rechargeNum, 0, byte_order, 3, rechargeNum.length);

        try {
            String rcvData = mySerialPort.setCosOrder(byte_order, (byte)0x52, (byte)0x14);
            if(rcvData == null) {   //timeout
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {

        }

        return false;
    }

    //钱包扣费
    public boolean CardM1PurseDeduct(byte keyMode, byte[] key, byte[] cardNo, byte cardBlock, byte[] deductNum) {
        //密码块不能用作钱包
        if (((int) cardBlock % 4) == 3) {
            Log.e(TAG, "the cardBlock can't use to purse!");
            return false;
        }

        boolean authority = CardM1Authority(keyMode, key, cardNo, cardBlock);
        if(authority == false) {
            Log.e(TAG, "CardM1PurseDeduct, key authority failed!");
            return false;
        }

        byte[] byte_order = new byte[1 + 2 + 4];
        byte_order[0] = cardBlock;
        byte_order[1] = 0x00;
        byte_order[2] = 0x00;
        System.arraycopy(deductNum, 0, byte_order, 3, deductNum.length);

        try {
            String rcvData = mySerialPort.setCosOrder(byte_order, (byte)0x52, (byte)0x15);
            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {

        }

        return false;
    }

    //钱包备份
    public boolean CardM1PurseBackup(byte keyMode, byte[] key, byte[] cardNo, byte cardBlock, byte cardBackupBlock) {
        //密码块不能用作钱包
        if (((int) cardBlock % 4) == 3) {
            Log.e(TAG, "the cardBlock can't use to purse!");
            return false;
        }

        boolean authority = (cardBlock / 4 == cardBackupBlock / 4) ? CardM1Authority(keyMode, key, cardNo, cardBlock) :
                CardM1Authority(keyMode, key, cardNo, cardBlock) && CardM1Authority(keyMode, key, cardNo, cardBackupBlock);
        if(authority == false) {
            Log.e(TAG, "CardM1PurseBackup, key authority failed!");
            return false;
        }

        try {
            String rcvData = mySerialPort.setCosOrder(new byte[]{cardBlock, cardBackupBlock}, (byte)0x52, (byte)0x16);
            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {

        }

        return false;
    }

    //打开扫码功能
    public boolean ScannerOpen() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x53, (byte)0x01);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }

    //关闭扫码
    public boolean ScannerClose() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x53, (byte)0x02);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }

    //读取扫码数据
    public byte[] ScannerGetData() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x53, (byte)0x03);
            if(rcvData == null) {   //超时
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[4] == 0x53 && rcvDataByte[6] == (byte)0x00) {
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] ScannerData = new byte[rcvDataByte.length - 9 - 2];
				System.arraycopy(rcvDataByte, 9, ScannerData, 0, ScannerData.length);
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD) + ",ScannerData:" + MyConverterTool.ByteArrToHex(ScannerData));
                    Log.i(TAG, "Scanner text : " + new String(ScannerData));
                }
                return ScannerData;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }

    //打开补光灯
    public boolean LedOpen() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x54, (byte)0x01);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //关闭补光灯
    public boolean LedClose() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x54, (byte)0x02);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcesscCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //设置sm4密钥
    public boolean setSM4Key(byte[] key) {
        try {
            String rcvData = mySerialPort.setCosOrder(key, (byte)0x55, (byte)0x01);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            if(rcvDataByte[4] == 0x55 && rcvDataByte[6] == (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, "设置密钥成功：" + MyConverterTool.ByteArrToHex(key));
                }
                return true;
            } else {
                if(getDebugStatus()) {
                    String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[6]);
                    Log.i(TAG, ResultMasage.CommandSMCMDResult(rcvCMD));
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //sm4加密数据
    public byte[] encryptSM4(byte[] data) {
        try {
            String rcvData = mySerialPort.setCosOrder(data, (byte)0x55, (byte)0x02);
            if(rcvData == null) {   //超时
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            if(rcvDataByte[4] == 0x55 && rcvDataByte[5] == 0x02 && rcvDataByte[6] == (byte)0x00) {
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] encryptData = new byte[rcvDataByte.length - 9 - 2];
                System.arraycopy(rcvDataByte, 9, encryptData, 0, encryptData.length);
                if(getDebugStatus()) {
                    Log.i(TAG, "加密数据：" + MyConverterTool.ByteArrToHex(encryptData));
                }
                return encryptData;
            } else {
                if(getDebugStatus()) {
                    String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[6]);
                    Log.i(TAG, "" + ResultMasage.CommandSMCMDResult(rcvCMD));
                }
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //sm4解密数据
    public byte[] decryptSM4(byte[] data) {
        try {
            String rcvData = mySerialPort.setCosOrder(data, (byte)0x55, (byte)0x03);
            if(rcvData == null) {   //超时
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            if(rcvDataByte[4] == 0x55 && rcvDataByte[5] == 0x03 && rcvDataByte[6] == (byte)0x00) {
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] decryptData = new byte[rcvDataByte.length - 9 - 2];
                System.arraycopy(rcvDataByte, 9, decryptData, 0, decryptData.length);
                if(getDebugStatus()) {
                    Log.i(TAG, "解密数据：" + MyConverterTool.ByteArrToHex(decryptData));
                }
                return decryptData;
            } else {
                if(getDebugStatus()) {
                    String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[6]);
                    Log.i(TAG, ResultMasage.CommandSMCMDResult(rcvCMD));
                }
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //sm2自动生成公私钥对
    public boolean genKeyPairSM2() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x56, (byte)0x01);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            if(rcvDataByte[4] == 0x56 && rcvDataByte[6] == (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, "公私密钥对生成成功");
                }
                return true;
            } else {
                if(getDebugStatus()) {
                    String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[6]);
                    Log.i(TAG, ResultMasage.CommandSMCMDResult(rcvCMD));
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //sm2签名
    public byte[] signSM2(byte[] data) {
        try {
            String rcvData = mySerialPort.setCosOrder(data, (byte)0x56, (byte)0x02);
            if(rcvData == null) {   //超时
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            if(rcvDataByte[4] == 0x56 && rcvDataByte[5] == 0x02 && rcvDataByte[6] == (byte)0x00) {
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] signData = new byte[rcvDataByte.length - 9 - 2];
                System.arraycopy(rcvDataByte, 9, signData, 0, signData.length);
                if(getDebugStatus()) {
                    Log.i(TAG, "签名数据：" + MyConverterTool.ByteArrToHex(signData));
                }
                return signData;
            } else {
                if(getDebugStatus()) {
                    String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[6]);
                    Log.i(TAG, ResultMasage.CommandSMCMDResult(rcvCMD));
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //sm2验签
    public boolean verifySM2(byte[] data) {
        try {
            String rcvData = mySerialPort.setCosOrder(data, (byte)0x56, (byte)0x03);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            if(rcvDataByte[4] == 0x56 && rcvDataByte[5] == 0x03 && rcvDataByte[6] == (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, "验签成功！");
                }
                return true;
            } else {
                if(getDebugStatus()) {
                    String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[6]);
                    Log.i(TAG, ResultMasage.CommandSMCMDResult(rcvCMD));
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //ic卡打开
    public boolean CardIccOpen() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x55, (byte)0x01);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcessIccCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcessIccCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //icc卡关闭
    public boolean CardIccClose() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x55, (byte)0x02);
            if(rcvData == null) {   //超时
                return false;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcessIccCMDSuc(rcvCMD));
                }
                return true;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcessIccCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //icc卡复位
    public byte[] CardIccReset() {
        try {
            String rcvData = mySerialPort.setCosOrder((byte)0x55, (byte)0x03);
            if(rcvData == null) {   //超时
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);
            if(rcvDataByte[6]== (byte)0x00) {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcessIccCMDSuc(rcvCMD));
                }
                //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                //尾部2字节：校验码 + 包尾
                byte[] resetData = new byte[rcvDataByte.length - 9 - 2];
                System.arraycopy(rcvDataByte, 9, resetData, 0, resetData.length);
                return resetData;
            }
            else {
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcessIccCMDFail(rcvCMD));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //icc卡apdu操作
    public byte[] CardIccSendCosCmd(byte[] CosCmd) {
        try {
            String rcvData = mySerialPort.setCosOrder(CosCmd, (byte)0x55, (byte)0x04);
            if(rcvData == null) {   //超时
                return null;
            }

            byte[] rcvDataByte = MyConverterTool.toBytes(rcvData);
            String rcvCMD = MyConverterTool.Byte2Hex(rcvDataByte[5]);

            if(rcvDataByte[6] == 0x00) {// 状态码为(0x00)“成功”
                byte[] SW1SW2data = new byte[2];
                System.arraycopy(rcvDataByte, rcvDataByte.length - 4, SW1SW2data, 0, SW1SW2data.length);
                String SW1SW2 = MyConverterTool.bytesToHex(SW1SW2data);
                if(getDebugStatus())
                    Log.i(TAG, "SW1SW2:" + SW1SW2);
                if(SW1SW2.equals("9000")) {
                    //去掉前面9个字节：包头+序列号+2字节的长度+指令类型+命令+状态码+2字节buff长度
                    //尾部4字节：状态码2字节 + 校验码 + 包尾
                    byte[] apduData = new byte[rcvDataByte.length - 9 - 2];
                    System.arraycopy(rcvDataByte, 9, apduData, 0, apduData.length);
                    if(getDebugStatus())
                        Log.i(TAG, "APDU数据:" + MyConverterTool.ByteArrToHex(apduData));
                    return apduData;
                }
                else {
                    if(getDebugStatus())
                        Log.e(TAG,"错误码 ：" + ResultMasage.CommandProcessSW1SW2(SW1SW2));
                    return SW1SW2data;
                }
            } else {// 状态码为“非成功”
                if(getDebugStatus()) {
                    Log.i(TAG, ResultMasage.CommandProcessIccCMDFail(rcvCMD));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

	public boolean getDebugStatus() {
        return this.debug;
    }

	public void setDebugStatus(boolean status) {
        this.debug = status;
	}

	public boolean getAntiSwipeCardStatus() {
        return this.AntiSwipe_status;
    }

	public void setAntiSwipeCardStatus(boolean status) {
        this.AntiSwipe_status = status;
	}

    public void destory() {
        mySerialPort.close();
        INSTANCE = null;
    }
}
