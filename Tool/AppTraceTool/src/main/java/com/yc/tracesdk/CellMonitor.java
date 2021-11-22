package com.yc.tracesdk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Pair;

import com.yc.tracesdk.LocInfoProtoBuf.CellInfo;
import com.yc.tracesdk.LocInfoProtoBuf.CellInfo.CellType;

public class CellMonitor {
    private static volatile CellMonitor mInstance;
    private Context mContext;
    /** {@code TelephonyManager}对象 */
    private TelephonyManager mTelephonyManager;
    /** 基站位置的监听 */
    private PhoneStateListener mCellListener;
    /* 信号强度 */
    private int mRssi = 0;
    /* 基站类型 */
    private CellType mCellType = CellType.UNKNOWN;

    private CellMonitor(Context context) {
        this.mContext = context.getApplicationContext();
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取单例对象
     * 
     * @param context
     *            程序上下文对象
     * @return {@code CellMonitor}单例对象
     */
    /* package */static CellMonitor getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CellMonitor.class) {
                if (mInstance == null) {
                    mInstance = new CellMonitor(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 开始监听Cell状态
     */
    /* package */void start() {
        LogHelper.log("CellMonitor#start()");

        /*
         * {@code PhoneStateListener}中要用到{@code Handler}， 必须保证{@code
         * PhoneStateListener}对象能拿到当前线程的{@code Looper}对象， 上次创建{@code
         * PhoneStateListener}对象时所在的线程可能已经退出， 所以必须在当前线程中重新创建{@code
         * PhoneStateListener}对象
         */
        mCellListener = new MyPhoneStateListener();

        try {
            mTelephonyManager.listen(mCellListener, PhoneStateListener.LISTEN_CELL_LOCATION
                    | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        } catch (SecurityException e) {
            //e.printStackTrace();
        }

    }

    /**
     * 停止监听Cell状态
     */
    /* package */void stop() {
        LogHelper.log("CellMonitor#stop()");
        if (mCellListener == null) {
            return;
        }
        mTelephonyManager.listen(mCellListener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * 获取基站信息
     */
    private CCellInfo getCellInfo() {
        CCellInfo cCellInfo = new CCellInfo();
        try {
            String netWorkOperator = mTelephonyManager.getNetworkOperator();
            if (netWorkOperator == null) {
                return null;
            }
            if (netWorkOperator.length() == 5 || netWorkOperator.length() == 6) {
                cCellInfo.mMcc = Integer.valueOf(netWorkOperator.substring(0, 3));
                cCellInfo.mMnc = Integer.valueOf(netWorkOperator.substring(3, netWorkOperator.length()));
            } else {
                return null;
            }

            cCellInfo.mIsRoaming = mTelephonyManager.isNetworkRoaming();
            cCellInfo.mCellType = mCellType;
            cCellInfo.mRssi = mRssi;

            CellLocation cellLocation = mTelephonyManager.getCellLocation();
            if (cellLocation == null) {
                return null;
            }
            if (cellLocation instanceof GsmCellLocation) {
                /* GSM */
                // cCellInfo.mCellType = CellType.GSM;
                cCellInfo.mLac = ((GsmCellLocation) cellLocation).getLac();
                cCellInfo.mCellId = ((GsmCellLocation) cellLocation).getCid();
                cCellInfo.mPsc = ((GsmCellLocation) cellLocation).getPsc();
            } else if (cellLocation instanceof CdmaCellLocation) {
                /* CDMA */
                // cCellInfo.mCellType = CellType.CDMA;
                cCellInfo.mMnc = ((CdmaCellLocation) cellLocation).getSystemId(); //#liuc# sid will occupy mnc's field
                cCellInfo.mLac = ((CdmaCellLocation) cellLocation).getNetworkId();
                cCellInfo.mCellId = ((CdmaCellLocation) cellLocation).getBaseStationId();
            }

            ArrayList<NCellInfo> neighboringCellInfo = new ArrayList<NCellInfo>();
            List<NeighboringCellInfo> neighboringCellInfos = mTelephonyManager.getNeighboringCellInfo();
            if (neighboringCellInfos != null) {
                for (NeighboringCellInfo cellInfo : neighboringCellInfos) {
                    if (cellInfo == null) {
                        continue;
                    }
                    NCellInfo nCellInfo = new NCellInfo();
                    nCellInfo.mRssi = -113 + cellInfo.getRssi() * 2;
                    nCellInfo.mLac = cellInfo.getLac();
                    nCellInfo.mCellId = cellInfo.getCid();
                    nCellInfo.mPsc = cellInfo.getPsc();
                    neighboringCellInfo.add(nCellInfo);
                    LogHelper.log(nCellInfo.toString());
                }
            }
            cCellInfo.mNeighboringCellInfo = neighboringCellInfo;
        } catch (Exception e) {
            //e.printStackTrace();
        }

        // fill cell data with new API, if available
        Pair<Pair<CellLocation, Integer>, Boolean> cellDbmLte = ReflectCellAPI.getAllCellInfoReflect(mTelephonyManager);
        if (cellDbmLte != null && cellDbmLte.first.first != null) {
            CellLocation cellLocation = cellDbmLte.first.first;
            if (cellLocation instanceof CdmaCellLocation) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation)cellLocation;
                cCellInfo.mMnc = cdmaCellLocation.getSystemId();
                cCellInfo.mLac = cdmaCellLocation.getNetworkId();
                cCellInfo.mCellId = cdmaCellLocation.getBaseStationId();
            } else if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation)cellLocation;
                cCellInfo.mLac = gsmCellLocation.getLac();
                cCellInfo.mCellId = gsmCellLocation.getCid();
            }
            if (cellDbmLte.first.second != 0) {
                cCellInfo.mRssi = cellDbmLte.first.second;
            }
        }


        return cCellInfo;
    }

    /**
     * 将基站信息转换为字节数组
     * 
     * @param cCellInfo
     *            基站信息
     * @return 基站信息字节数组
     */
    private byte[] convertCellInfo2ByteArray(CCellInfo cCellInfo) {
        CellInfo.Builder builder = new CellInfo.Builder();
        builder.time(cCellInfo.mTime);
        builder.mcc(cCellInfo.mMcc);
        builder.mnc(cCellInfo.mMnc);
        builder.lac(cCellInfo.mLac);
        builder.cid(cCellInfo.mCellId);
        builder.cell_type(cCellInfo.mCellType);
        builder.psc(cCellInfo.mPsc);
        builder.rssi(cCellInfo.mRssi);

        if (cCellInfo.mNeighboringCellInfo != null) {
            builder.neighboringCellInfo = new ArrayList<CellInfo.NeighboringCellInfo>();
            for (NCellInfo nCellInfo : cCellInfo.mNeighboringCellInfo) {
                CellInfo.NeighboringCellInfo.Builder nBuilder = new CellInfo.NeighboringCellInfo
                        .Builder();
                nBuilder.cid(nCellInfo.mCellId);
                nBuilder.lac(nCellInfo.mLac);
                nBuilder.psc(nCellInfo.mPsc);
                nBuilder.rssi(nCellInfo.mRssi);
                builder.neighboringCellInfo.add(nBuilder.build());
            }
        }

        return builder.build().toByteArray();
    }

    /**
     * 将基站信息写入文件
     */
    private void saveCellInfo() {
        CCellInfo cellInfo = getCellInfo();
        if (cellInfo == null) {
            return;
        }

        LogHelper.log("cellInfo:" + cellInfo.toString());

        try {
            byte[] cellInfoByteArray = convertCellInfo2ByteArray(cellInfo);
            DBHandler.getInstance(mContext).insertCellData(cellInfoByteArray);
        } catch (Exception e) {}
        
        /* 将json格式的数据写入文件 */
        LogHelper.writeToFile(cellInfo.toJsonString());
    }

    /**
     * 当前基站信息
     */
    private final class CCellInfo {
        public long mTime;
        public CellType mCellType = CellType.UNKNOWN;
        public int mMcc;
        public int mMnc;
        public int mLac;
        public int mCellId;
        public int mPsc; // gsm
        public int mRssi;
        public boolean mIsRoaming;
        public ArrayList<NCellInfo> mNeighboringCellInfo;

        public CCellInfo() {
            this.mTime = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("CCellInfo:");
            sb.append("[mTime=").append(mTime).append("]");
            sb.append("[mCellType=").append(mCellType).append("]");
            sb.append("[mMcc=").append(mMcc).append("]");
            sb.append("[mMnc=").append(mMnc).append("]");
            sb.append("[mLac=").append(mLac).append("]");
            sb.append("[mCellId=").append(mCellId).append("]");
            sb.append("[mPsc=").append(mPsc).append("]");
            sb.append("[mIsRoaming=").append(mIsRoaming).append("]");
            sb.append("[mRssi=").append(mRssi).append("]");
            return sb.toString();
        }
        
        /**
         * 生成json格式的基站信息
         * 
         * @return json格式的基站信息
         */
        public String toJsonString() {
            JSONObject json = new JSONObject();
            try {
                json.put("time", mTime);
                json.put("cellType", mCellType);
                json.put("mcc", mMcc);
                json.put("mnc", mMnc);
                json.put("lac", mLac);
                json.put("cellId", mCellId);
                json.put("psc", mPsc);
                json.put("rssi", mRssi);
                json.put("isRoaming", mIsRoaming);
                JSONArray neighboringCellInfoJsonArray = new JSONArray();
                if (mNeighboringCellInfo != null) {
                    for (NCellInfo nCellInfo : mNeighboringCellInfo) {
                        JSONObject neighboringCellInfoJson = new JSONObject();
                        neighboringCellInfoJson.put("nLac", nCellInfo.mLac);
                        neighboringCellInfoJson.put("nCellInfo", nCellInfo.mCellId);
                        neighboringCellInfoJson.put("nRssi", nCellInfo.mRssi);
                        neighboringCellInfoJson.put("nPsc", nCellInfo.mPsc);
                        neighboringCellInfoJsonArray.put(neighboringCellInfoJson);
                    }  
                }
                
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            
            return json.toString();
        }
    }

    /**
     * 附近基站信息
     */
    private final class NCellInfo {
        public int mLac;
        public int mCellId;
        public int mRssi;
        public int mPsc;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("NCellInfo:");
            sb.append("[mLac=").append(mLac).append("]");
            sb.append("[mCellId=").append(mCellId).append("]");
            sb.append("[mRssi=").append(mRssi).append("]");
            sb.append("[mPsc=").append(mPsc).append("]");
            return sb.toString();
        }
    }

    /**
     * 基站位置改变的监听
     */
    private final class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            LogHelper.log("#onCellLocationChanged");

            try {saveCellInfo();} catch (Exception e) {}
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            LogHelper.log("#onSignalStrengthsChanged");
            if (signalStrength.isGsm()) {
                int gsmSignalStrength = signalStrength.getGsmSignalStrength();
                if (gsmSignalStrength >= 0 && gsmSignalStrength <= 31) {
                    mCellType = CellType.GSM;
                    mRssi = 2 * gsmSignalStrength - 113;
                } else {
                    Method methodGetLteSignalStrength;
                    try {
                        methodGetLteSignalStrength = SignalStrength.class.getDeclaredMethod("getLteRsrp");
                        int lteRssi = (Integer) methodGetLteSignalStrength.invoke(signalStrength);
                        if (lteRssi < 0) {
                            mCellType = CellType.LTE;
                            mRssi = lteRssi;
                        }
                    } catch (IllegalAccessException e) {
                        //e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        //e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        //e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        //e.printStackTrace();
                    }

                }
            } else {
                mCellType = CellType.CDMA;
                mRssi = signalStrength.getCdmaDbm();
            }
        }

    }
}
