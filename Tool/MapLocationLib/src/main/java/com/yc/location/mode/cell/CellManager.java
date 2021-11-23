package com.yc.location.mode.cell;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.yc.location.constant.Constants;
import com.yc.location.listener.LocationUpdateInternalListener;
import com.yc.location.log.LogHelper;
import com.yc.location.utils.ReflectUtils;
import com.yc.location.utils.LocationUtils;
import com.yc.location.bean.DefaultLocation;

import java.util.ArrayList;
import java.util.List;

public class CellManager {

    private Context mContext;

    private int iCgiT = Cgi.iDefCgiT;

    /**
     * lstCgi[0]主基站，其它为相邻基站
     */
    private volatile List<Cgi> lstCgi = new ArrayList<>();

    /**
     * 基站信号强度，gsm、cdma均适用
     */
    private int iCgiSig = Constants.iDefCgiSig;

    private TelephonyManager mTelephonyManager;

    private Object mTelephonyManagerSub;

    private long lLastCgiUpdate = 0l;

    private PhoneStateListener mListener;

    private CellLocation lastCellLoc;
    private LocationUpdateInternalListener mLocationInternalListener;

    public CellManager(Context ctx, LocationUpdateInternalListener listener) {
        mLocationInternalListener = listener;
        mContext = ctx;
    }

    /**
     * 获取副卡类型
     *
     * @param
     * @return int
     */
    public static int getSim2T() {
        int iT = Constants.iSim2Def;
        try {
            Class.forName("android.telephony.MSimTelephonyManager");
            iT = Constants.iSim2QualComm;
        } catch (Exception e) {
            //
        }
        if (iT == Constants.iSim2Def) {
            try {
                Class.forName("android.telephony.TelephonyManager2");
                iT = Constants.iSim2Mtk;
            } catch (Exception e) {
                //
            }
        }

        if (iT == Constants.iSim2Def) {
            LogHelper.logFile("sim2 default");
        }

        return iT;
    }

    public void init() {
        if (mContext == null) {
            LogHelper.logFile("CellManager::init context is null");

            return;
        }

        mTelephonyManager = (TelephonyManager) LocationUtils.getServ(mContext, Context.TELEPHONY_SERVICE);
        try {
            iCgiT = LocationUtils.getCellLocT(mTelephonyManager.getCellLocation(), mContext);
            initPhoneStateListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateCgi();
    }

    public List<Cgi> getDetectedCgiList() {
        return lstCgi;
    }

    public int getMainCgiType() {
        return iCgiT;
    }

    public String getDeviceId() {
        if (null != mTelephonyManager) {
            try {
                return mTelephonyManager.getDeviceId();
            } catch (Exception e) {
                return "";
            }
        }

        return "";
    }

    public CellLocation getCellLocation() {
        if (null != mTelephonyManager) {

            CellLocation cellLoc = null;
            try {
                cellLoc = mTelephonyManager.getCellLocation();

                if (cgiUseful(cellLoc)) {
                    lastCellLoc = cellLoc;
                }
            } catch (Exception e) {
                //
            }

            return cellLoc;
        }
        return null;
    }

    public CellLocation getLastCellLocation() {
        return lastCellLoc;
    }

    public TelephonyManager getTelephonyManagerInstance() {
        return mTelephonyManager;
    }

    /**
     * hdlCgiChange()
     */
    public void refresh() {
        hdlCgiChange();
    }

    /**
     * resetCgiData()
     */
    public void reset() {
        resetCgiData();
    }

    /**
     * updateCgi()
     */
    public void requestCgiLocationUpdate() {
        updateCgi();
    }

    public void destroy() {
        if (mTelephonyManager != null && mListener != null) {
            try {
                LogHelper.logFile("cell unregister listener");
                mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
            } catch (Exception e) {
                LogHelper.logFile(e.toString());
            }
        }
        if (null != lstCgi) {
            lstCgi.clear();
        }
        iCgiSig = Constants.iDefCgiSig;

        mTelephonyManager = null;
        mTelephonyManagerSub = null;
        mLocationInternalListener = null;
    }

    /**
     * 校正CELL类型，如果周边基站为空，将iCgiT置为默认
     *
     * @param
     * @return void
     */
    public void refineCellT() {
        switch (iCgiT) {
            case Cgi.iGsmT:
                if (null != lstCgi && lstCgi.isEmpty()) {
                    LogHelper.logFile("refine cgi gsm2def");
                    iCgiT = Cgi.iDefCgiT;
                }
                break;
            case Cgi.iCdmaT:
                if (null != lstCgi && lstCgi.isEmpty()) {
                    LogHelper.logFile("refine cell cdma2def");

                    iCgiT = Cgi.iDefCgiT;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置电话号码
     *
     * @param
     * @return void
     */
    public void setPhnum() {
    /*
     * 默认不收集电话号码
     */
    }

    /**
     * 判断CGI是否需要强制刷新：飞行模式开启、上次Cgi更新时间为0或者本次距离上次更新时间小于规定值
     *
     * @param
     * @return boolean
     */
    public boolean cgiNeedUpdate(boolean airPlaneModeOn) {
        boolean b = true;

        if (airPlaneModeOn || lLastCgiUpdate == 0l) {
            b = false;
        } else if (LocationUtils.getTimeBoot() - lLastCgiUpdate < Constants.lMinCgiUpdate) {
            b = false;
        }
        return b;
    }

    public boolean isCellValid() {
        if (lastCellLoc == null || !cgiUseful(lastCellLoc)
                || LocationUtils.getTimeBoot() - lLastCgiUpdate > Constants.lMinCgiUpdate) {
            return false;
        }

        return true;
    }

    /**
     * 判断CGI是否有用，（策略）
     *
     * @param
     * @return boolean
     */
    public boolean cgiUseful(CellLocation cellLoc) {
        if (cellLoc == null) {
            return false;
        }
        boolean bFine = true;
        int iCellLocT = LocationUtils.getCellLocT(cellLoc, mContext);
        switch (iCellLocT) {
            case Cgi.iGsmT:
                GsmCellLocation gsmCellLoc = (GsmCellLocation) cellLoc;
                if (gsmCellLoc.getLac() == -1) {
                    bFine = false;
                } else if (gsmCellLoc.getLac() == 0) {
                    bFine = false;
                } else if (gsmCellLoc.getLac() > 65535) {
                    bFine = false;
                } else if (gsmCellLoc.getCid() == -1) {
                    bFine = false;
                } else if (gsmCellLoc.getCid() == 0) {
                    bFine = false;
                } else if (gsmCellLoc.getCid() == 65535) {
                    bFine = false;
                } else if (gsmCellLoc.getCid() >= 268435455) {
                    bFine = false;
                }

                switch (gsmCellLoc.getCid()) {
                    case 8:
                    case 10:
                    case 33:
                        LogHelper.logFile("cgi|fake");
                        break;
                    default:
                        break;
                }

                break;
            case Cgi.iCdmaT:
                Object oCdma = (Object) cellLoc;
                try {
                    if (ReflectUtils.invokeIntMethod(oCdma, "getSystemId") <= 0) {
                        bFine = false;
                    } else if (ReflectUtils.invokeIntMethod(oCdma, "getNetworkId") < 0) {
                        bFine = false;
                    } else if (ReflectUtils.invokeIntMethod(oCdma, "getBaseStationId") < 0) {
                        bFine = false;
                    }
                } catch (Exception e) {
                    LogHelper.logFile(e.toString());
                }
                break;
            default:
                break;
        }
        if (!bFine) {
            iCgiT = Cgi.iDefCgiT;
//            LogHelper.logBamai("cell illegal");
        }

        return bFine;
    }

    /**
     * 判断CGI是否有用
     *
     * @param
     * @return boolean
     */
    public boolean cgiUseful(NeighboringCellInfo nbCellInfo) {
        if (nbCellInfo == null) {
            return false;
        }
        boolean bFine = true;
        if (nbCellInfo.getLac() == -1) {
            bFine = false;
        } else if (nbCellInfo.getLac() == 0) {
            bFine = false;
        } else if (nbCellInfo.getLac() > 65535) {
            bFine = false;
        } else if (nbCellInfo.getCid() == NeighboringCellInfo.UNKNOWN_CID) {
            bFine = false;
        } else if (nbCellInfo.getCid() == 0) {
            bFine = false;
        } else if (nbCellInfo.getCid() == 65535) {
      /*
       * 0xFFFF
       */
            bFine = false;
        } else if (nbCellInfo.getCid() >= 268435455) {
      /*
       * 0xFFFFFFF
       */
            bFine = false;
        }
        return bFine;
    }

    /**
     * 处理手机CGI切换的回调，获取CellLocation
     *
     * @param
     * @return void
     */
    private synchronized void hdlCgiChange() {

        boolean bAirPlaneMode = LocationUtils.airPlaneModeOn(mContext);

        if (!bAirPlaneMode && mTelephonyManager != null) {
      /*
       * 在飞行模式关闭情况下，优先主动获取基站
       */

            //LogHelper.logBamai("try to found cell");
            lstCgi = getSim1Cgis();
            if (lstCgi == null || lstCgi.size() == 0) {
                LogHelper.logFile("sim1 miss");
        /*
         * 获取副卡基站
         */
                lstCgi = getSim2Cgis();
            }
            if (lstCgi == null || lstCgi.size() == 0) {//lcc:此分支只是为了日志
                LogHelper.logFile("sim2 miss");
        /*
         * 使用onCellLocationChange中回调的值
         */
            }
            if (!cgiUseful(lastCellLoc)) {
                LogHelper.logFile("non sim found");
                return;
            }
            if (lstCgi == null) {
                lstCgi = new ArrayList<>();
            }
            int iCellLocT = LocationUtils.getCellLocT(lastCellLoc, mContext);
            //lcc：若lsgCgi不为空，则lsgCgi第一个元素和lastCellLoc是同步的，即相同
            switch (iCellLocT) {
                case Cgi.iGsmT:
                    //LogHelper.logBamai("cell: maybe gsm");
                    if (lstCgi.size() == 0) {
                        hdlGsmLocChange(lastCellLoc);
                    }
                    break;
                case Cgi.iCdmaT:
                    //LogHelper.logBamai("cell: maybe cdma");
                    //lcc:由于副卡cdma可能包含gsm，所以当为CDMA时，都要处理。见函数处理逻辑。
                    hdlCdmaLocChange(lastCellLoc);
                    break;
                default:
                    LogHelper.logFile("cell: type unknown " + iCellLocT);

                    break;
            }
            if (lstCgi.size() > 0) {
                iCgiT = lstCgi.get(0).type;
            }
        }
    }

    /**
     * 处理GSM CGI切换
     *
     * @param
     * @return void
     */
    private void hdlGsmLocChange(CellLocation cellLoc) {
        if (cellLoc == null || mTelephonyManager == null) {
            return;
        }
//        lstCgi.clear();
        if (!cgiUseful(cellLoc)) {
            return;
        }
//        iCgiT = Cgi.iGsmT;
//        List<NeighboringCellInfo> lstNbCellInfo = null;
        if (null != lstCgi) {
            lstCgi.add(0, getGsm(cellLoc, Cgi.iGsmOldT));
        }
//        lstNbCellInfo = mTelephonyManager.getNeighboringCellInfo();
//        if (lstNbCellInfo == null || lstNbCellInfo.isEmpty()) {
//            //LogHelper.logBamai("cell neighboring cell unknown");
//
//            return;
//        }
//
//        for (NeighboringCellInfo nbCellInfo : lstNbCellInfo) {
//            if (!cgiUseful(nbCellInfo)) {
//                continue;
//            }
//            Cgi cgi = getGsm(nbCellInfo);
//            if (cgi != null && !lstCgi.contains(cgi)) {
//                lstCgi.add(cgi);
//            }
//        }
    }

    /**
     * 处理CDMA CGI切换
     *
     * @param
     * @return void
     */
    private void hdlCdmaLocChange(CellLocation cellLoc) {
        if (cellLoc == null) {
            return;
        }

//        lstCgi.clear();
        if (LocationUtils.getSdk() < 5) {
            LogHelper.logFile("do not support cdma");
            return;
        }
        try {
            Object oCdmaCellLoc = (Object) cellLoc;
            if (mTelephonyManagerSub != null) {
        /*
         * 只有副卡的CDMA才可能包含GSM
         */
                boolean bSim2Fine = false;
                java.lang.reflect.Field fieldCdma = null;
                try {
                    Class<?> classCdma = oCdmaCellLoc.getClass();
                    fieldCdma = classCdma.getDeclaredField("mGsmCellLoc");
                    if (!fieldCdma.isAccessible()) {
                        fieldCdma.setAccessible(true);
                    }
                    GsmCellLocation gsmCellLoc = null;
                    gsmCellLoc = (GsmCellLocation) fieldCdma.get(oCdmaCellLoc);
                    if (gsmCellLoc != null && cgiUseful(gsmCellLoc)) {
                        LogHelper.logFile("get gsm cell loc");
                        lastCellLoc = gsmCellLoc;
                        //lstCgi中存在的第一个元素就是原lastCellLoc对应的Cgi
                        if (lstCgi != null && lstCgi.size() > 0) {
                            lstCgi.remove(0);
                        }
                        hdlGsmLocChange(gsmCellLoc);
                        bSim2Fine = true;
                    }
                } catch (Exception e) {
                    LogHelper.logFile("can not found gsm cell loc");
                }
                if (bSim2Fine) {
          /*
           * 检测到副卡CMDA中包含GSM
           */
                    return;
                }
            }

            if (!cgiUseful(cellLoc)) {
                return;
            }
//            iCgiT = Cgi.iCdmaT;

            Cgi cgi = getCdmaCgi(oCdmaCellLoc, Cgi.iCdmaT);
            if (null != cgi && lstCgi.size() == 0) {
                lstCgi.add(cgi);
            }
        } catch (Exception e) {
            LogHelper.logFile(e.toString());
        }
    }

    private Cgi getCdmaCgi(Object oCdmaCellLoc, int type) {
        String[] saMccMnc = LocationUtils.getMccMnc(mTelephonyManager);
        Cgi cgi = new Cgi();
        try {
            cgi.mcc = saMccMnc[0];
            cgi.mnc_sid = String.valueOf(ReflectUtils.invokeIntMethod(oCdmaCellLoc, "getSystemId"));
            cgi.lac_nid = ReflectUtils.invokeIntMethod(oCdmaCellLoc, "getNetworkId");
            String strMethod = "getBaseStationId";
            cgi.cid_bid = ReflectUtils.invokeIntMethod(oCdmaCellLoc, strMethod);
            cgi.sig = iCgiSig;
            strMethod = "getBaseStationLatitude";
            cgi.lat = ReflectUtils.invokeIntMethod(oCdmaCellLoc, strMethod);
            strMethod = "getBaseStationLongitude";
            cgi.lon = ReflectUtils.invokeIntMethod(oCdmaCellLoc, strMethod);
            cgi.type = type;
            boolean bCoordFine = true;
            if (cgi.lat < 0 || cgi.lon < 0) {
                cgi.lat = 0;
                cgi.lon = 0;
                bCoordFine = false;
            } else if (cgi.lat == Integer.MAX_VALUE) {
                cgi.lat = 0;
                cgi.lon = 0;
                bCoordFine = false;
            } else if (cgi.lon == Integer.MAX_VALUE) {
                cgi.lat = 0;
                cgi.lon = 0;
                bCoordFine = false;
            } else if ((cgi.lat == cgi.lon) && cgi.lat > 0) {
                cgi.lat = 0;
                cgi.lon = 0;
                bCoordFine = false;
            }
            if (!bCoordFine) {
                LogHelper.logFile("cdma coordinate is invalid");
            }
        } catch (Exception e) {
            cgi = null;
        }
        return cgi;
    }

    /**
     * 获取主卡基站
     *
     * @param
     * @return CellLocation
     */
    private List<Cgi> getSim1Cgis() {
        TelephonyManager tm = mTelephonyManager;

        if (tm == null) {
            return null;
        }

        CellLocation cellLoc = null;
        List<?> lstCellInfo = null;
        List<Cgi> list = null;
        try {
            lstCellInfo = (List<?>) ReflectUtils.invokeMethod(tm, "getAllCellInfo");
            list = retrieveCgis(lstCellInfo);
        } catch (Exception e) {
            LogHelper.logFile(e.toString());
        }

        if (list != null && list.size() > 0) {
            LogHelper.logFile("sim1 cellLocation got 1");
            return list;
        }
        list = new ArrayList<>();
        try {
            cellLoc = tm.getCellLocation();
        } catch (Exception e) {
            //
        }

        if (cgiUseful(cellLoc)) {
            lastCellLoc = cellLoc;
            addCellLoc(cellLoc, list);
            LogHelper.logFile("sim1 cellLocation got 2");
            return list;
        }
    /*
     * DUAL SIM START(HTC 802D...)
     */
        Object o = null;
        try {
            o = ReflectUtils.invokeMethod(tm, "getCellLocationExt", 1);
            if (o != null) {
                cellLoc = (CellLocation) o;
            }
        } catch (NoSuchMethodException e) {
            LogHelper.logFile("sim1 getAllCellInfo failed 2");
        } catch (Exception e) {
            LogHelper.logFile(e.toString());
        }
        o = null;
        if (cgiUseful(cellLoc)) {
            LogHelper.logFile("sim1 getAllCellInfo got 3");
            lastCellLoc = cellLoc;
            addCellLoc(cellLoc, list);
            return list;
        }
    /*
     * DUAL SIM END(HTC 802D...)
     */
    /*
     * DUAL SIM START(HUAWEI Y511...)
     */
        try {
            o = ReflectUtils.invokeMethod(tm, "getCellLocationGemini", 1);
            if (o != null) {
                cellLoc = (CellLocation) o;
            }
        } catch (NoSuchMethodException e) {
            LogHelper.logFile("sim1 getCellLocationGemini failed");
        } catch (Exception e) {
            LogHelper.logFile(e.toString());
        }
        o = null;
        if (cgiUseful(cellLoc)) {
            LogHelper.logFile("sim1 getAllCellInfo got 4");
            lastCellLoc = cellLoc;
            addCellLoc(cellLoc, list);
            return list;
        }
    /*
     * DUAL SIM END(HUAWEI Y511...)
     */
        return list;
    }

    /**
     * 将Cell Location转化成Cgi，然后加入到list中。
     * cell location都是由非反射方式（非新接口）获取的，才会这么经过这么处理。
     *
     * @param cellLoc 由非反射方式（非新接口）获取的cell location。
     * @param list
     */
    private void addCellLoc(CellLocation cellLoc, List<Cgi> list) {
        int type = LocationUtils.getCellLocT(cellLoc, mContext);
        if (type == Cgi.iGsmT) {
            type = Cgi.iGsmOldT;
            list.add(getGsm(cellLoc, type));
        } else if (type == Cgi.iCdmaT) {
            Cgi cgi = getCdmaCgi(cellLoc, type);
            if (null != cgi) {
                list.add(cgi);
            }
        }
    }

    /**
     * 获取副卡基站
     *
     * @param
     * @return void
     */
    private List<Cgi> getSim2Cgis() {

        Object tm2 = mTelephonyManagerSub;

        if (tm2 == null) {
            return null;
        }

//        LogHelper.logBamai("try to found sim2 CellLocation");
        CellLocation cellLoc = null;
        Object o = null;
        List<Cgi> cgiList = null;
        try {
            Class<?> cTm2 = getSim2TmClass();
            if (cTm2.isInstance(tm2)) {
                Object oTm2 = cTm2.cast(tm2);

                List<?> lstCI = null;
                String s = "getAllCellInfo";
                try {
                    lstCI = (List<?>) ReflectUtils.invokeMethod(oTm2, s);
                } catch (Exception e) {
                    LogHelper.logFile("sim2 exception 3: " + e.toString());
                }
                cgiList = retrieveCgis(lstCI);

                if (null == cgiList) {
                    cgiList = new ArrayList<Cgi>();
                }

                if (cgiList.size() == 0) {
                    s = "getCellLocation";
                    try {
                        o = ReflectUtils.invokeMethod(oTm2, s);
                    } catch (Exception e) {
                        LogHelper.logFile("sim2 exception 0: " + e.toString());
                    }
                    if (o == null) {
                        try {
                            o = ReflectUtils.invokeMethod(oTm2, s, 1);
                        } catch (NoSuchMethodException e) {
                            LogHelper.logFile("sim2 NoSuchMethodException: getCellLocation 2");
                        } catch (Exception e) {
                            LogHelper.logFile("sim2 exception 1: " + e.toString());
                        }
                    }

                    if (o == null) {
                        s = "getCellLocationGemini";
                        try {
                            o = ReflectUtils.invokeMethod(oTm2, s, 1);
                        } catch (NoSuchMethodException e) {
                            LogHelper.logFile("sim2 NoSuchMethodException: getCellLocationGemini");
                        } catch (Exception e) {
                            LogHelper.logFile("sim2 exception 2: " + e.toString());
                        }
                    } else {
//                    LogHelper.logBamai("sim2 celllocation found 2");
                    }
                    if (o != null) {
                        cellLoc = (CellLocation) o;
                        lastCellLoc = cellLoc;
                        addCellLoc(cellLoc, cgiList);
                    }
                } else {
//                    LogHelper.logBamai("sim2 celllocation found 1");
                }
            }
        } catch (Exception e) {
            LogHelper.logFile(e.toString());
        }

        return cgiList;
    }

    /**
     * 获取副卡的TELEPHONEMANAGER的CLASS
     *
     * @param
     * @return Class<?>
     */
    private Class<?> getSim2TmClass() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        String s = null;
        switch (getSim2T()) {
            case Constants.iSim2QualComm:
                s = "android.telephony.MSimTelephonyManager";
                break;
            case Constants.iSim2Mtk:
                s = "android.telephony.TelephonyManager2";
                break;
            case Constants.iSim2Def:
                s = "android.telephony.TelephonyManager";
                break;
            default:
                break;
        }
        Class<?> cTm2 = null;
        try {
            cTm2 = cl.loadClass(s);
      /*
       * if (Const.bDebug) { if (iLocNum == 1) { java.lang.reflect.Method[] ma =
       * cTm2.getDeclaredMethods(); for (java.lang.reflect.Method m : ma) { Utils.writeCat("sim2|",
       * m.getName()); Class<?>[] ca = m.getParameterTypes(); for (Class<?> c : ca) {
       * Utils.writeCat("sim2|->", c.getName()); } Utils.writeCat("sim2|<-",
       * m.getReturnType().getName()); } } }
       */
        } catch (Exception e) {
            LogHelper.logFile(e.toString());
        }
        return cTm2;
    }

    /**
     * 从List<CELLINFO>中获取CGI
     *
     * @param
     * @return 所提取出的第一个CGI
     */
    private List<Cgi> retrieveCgis(List<?> lstCellInfo) {
        if (lstCellInfo == null || lstCellInfo.isEmpty()) {
            return null;
        }
        List<Cgi> list = new ArrayList<>();
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        GsmCellLocation gsmCellLoc = null;
        CdmaCellLocation cdmaCL = null;
        int iT = 0;
        for (int i = 0; i < lstCellInfo.size(); i++) {
            CellInfo oCellInfo = (CellInfo) lstCellInfo.get(i);
            if (oCellInfo == null) {
                continue;
            }
            try {
                Class<?> cCIGsm = null;
                Class<?> cCIWcdma = null;
                Class<?> cCILte = null;
                Class<?> cCICdma = null;
                String s = "android.telephony.CellInfoGsm";
                cCIGsm = cl.loadClass(s);
                s = "android.telephony.CellInfoWcdma";
                cCIWcdma = cl.loadClass(s);
                s = "android.telephony.CellInfoLte";
                cCILte = cl.loadClass(s);
                s = "android.telephony.CellInfoCdma";
                cCICdma = cl.loadClass(s);
                if (cCIGsm.isInstance(oCellInfo)) {
                    iT = Cgi.iGsmT;
                } else if (cCIWcdma.isInstance(oCellInfo)) {
                    iT = Cgi.iWcdmaT;
                } else if (cCILte.isInstance(oCellInfo)) {
                    iT = Cgi.iLteT;
                } else if (cCICdma.isInstance(oCellInfo)) {
                    iT = Cgi.iCdmaT;
                } else {
                    iT = Cgi.iDefCgiT;
                }
                if (iT != Cgi.iDefCgiT) {
                    Object oCI = null;
                    Object oCIIdentity = null;
                    Object oCISignal = null;
                    if (iT == Cgi.iGsmT) {
                        oCI = cCIGsm.cast(oCellInfo);
                    } else if (iT == Cgi.iWcdmaT) {
                        oCI = cCIWcdma.cast(oCellInfo);
                    } else if (iT == Cgi.iLteT) {
                        oCI = cCILte.cast(oCellInfo);
                    } else if (iT == Cgi.iCdmaT) {
                        oCI = cCICdma.cast(oCellInfo);
                    }
                    oCIIdentity = ReflectUtils.invokeMethod(oCI, "getCellIdentity");
                    oCISignal = ReflectUtils.invokeMethod(oCI, "getCellSignalStrength");
                    if (oCIIdentity == null) {
                        continue;
                    }
          /*
           * GSM LTE WCDMA
           */
                    int iLac = 0;
                    int iCid = 0;
          /*
           * CDMA
           */
                    int iSid = 0;
                    int iNid = 0;
                    int iBid = 0;
                    int iLon = 0;
                    int iLat = 0;
                    if (iT == Cgi.iCdmaT) {
                        cdmaCL = new CdmaCellLocation();
                        iSid = ReflectUtils.invokeIntMethod(oCIIdentity, "getSystemId");
                        iNid = ReflectUtils.invokeIntMethod(oCIIdentity, "getNetworkId");
                        iBid = ReflectUtils.invokeIntMethod(oCIIdentity, "getBasestationId");
                        iLon = ReflectUtils.invokeIntMethod(oCIIdentity, "getLongitude");
                        iLat = ReflectUtils.invokeIntMethod(oCIIdentity, "getLatitude");
                        cdmaCL.setCellLocationData(iBid, iLat, iLon, iSid, iNid);
                    } else if (iT == Cgi.iLteT) {
                        iLac = ReflectUtils.invokeIntMethod(oCIIdentity, "getTac");
                        iCid = ReflectUtils.invokeIntMethod(oCIIdentity, "getCi");
                        gsmCellLoc = new GsmCellLocation();
                        gsmCellLoc.setLacAndCid(iLac, iCid);
                    } else {
                        iLac = ReflectUtils.invokeIntMethod(oCIIdentity, "getLac");
                        iCid = ReflectUtils.invokeIntMethod(oCIIdentity, "getCid");
                        gsmCellLoc = new GsmCellLocation();
                        gsmCellLoc.setLacAndCid(iLac, iCid);
                    }

//                    if (iT == 1) {
//                        LogHelper.logBamai("gsm:" + iLac + "&" + iCid);
//                    } else if (iT == 2) {
//                        LogHelper.logBamai("wcdma:" + iLac + "&" + iCid);
//                    } else if (iT == 3) {
//                        LogHelper.logBamai("lte:" + iLac + "&" + iCid);
//                    } else if (iT == 4) {
//                        LogHelper.logBamai("cdma:" + iSid + "&" + iNid + "&" + iBid);
//                    }
                    if (iT == Cgi.iCdmaT) {
                        if (cgiUseful(cdmaCL)) {
                            if (list.size() == 0) {
                                lastCellLoc = cdmaCL;
                            }
                            //lcc:内部不用新接口getDbm赋值sig，因为有的手机返回-2的15次幂。如三星Galaxy Note5（SM-N9200）。
                            Cgi cgi = getCdmaCgi(cdmaCL, iT);

                            if (null != cgi) {
                                list.add(cgi);
                            }
                        }
                    } else {
                        if (cgiUseful(gsmCellLoc)) {
                            if (list.size() == 0) {
                                lastCellLoc = gsmCellLoc;
                            }
                            Cgi cgi = getGsm(gsmCellLoc, iT);
                            try {
                                //fixme:lcc:主基站用telephony Manager获得sig。getDbm（）测试获得的信号强度有时为-113.
                                if (i != 0) {
                                    cgi.sig = ReflectUtils.invokeIntMethod(oCISignal, "getDbm");
                                }
                                /*fix bug:使用新接口，但mcc和mnc没有正确分别赋值。老接口有拿不到值的情况：mi note,也考虑到双卡双GSM卡情况：
                                用getNetworkOperator获得到的mnc可能是另一张卡的。
                                 */
                                cgi.mcc = String.valueOf(ReflectUtils.invokeIntMethod(oCIIdentity, "getMcc"));
                                cgi.mnc_sid = String.valueOf(ReflectUtils.invokeIntMethod(oCIIdentity, "getMnc"));
                            } catch (Exception e) {

                            }
                            list.add(cgi);
                        }
                    }
                }
            } catch (Exception e) {
                LogHelper.logFile(e.toString());
                continue;
            }
        }
        cl = null;
        return list;
    }

    /**
     * 从CGI中获取GSM
     *
     * @param
     * @return Cgi
     */
    private Cgi getGsm(CellLocation cellLoc, int type) {
        GsmCellLocation gsmLoc = (GsmCellLocation) cellLoc;
        Cgi cgi = new Cgi();
        String[] saMccMnc = LocationUtils.getMccMnc(mTelephonyManager);
        cgi.mcc = saMccMnc[0];
        cgi.mnc_sid = saMccMnc[1];
        cgi.lac_nid = gsmLoc.getLac();
        cgi.cid_bid = gsmLoc.getCid();
        cgi.sig = iCgiSig;
        cgi.type = type;
        return cgi;
    }

    /**
     * 从NB中获取GSM
     *
     * @param
     * @return Cgi
     */
    private Cgi getGsm(NeighboringCellInfo nbCellInfo) {
        if (LocationUtils.getSdk() < 5) {
            LogHelper.logFile("api" + LocationUtils.getSdk() + " do not support NeighboringCellInfo");
            return null;
        }

        try {
            Cgi cgi = new Cgi();
            String[] saMccMnc = LocationUtils.getMccMnc(mTelephonyManager);
            cgi.mcc = saMccMnc[0];
            cgi.mnc_sid = saMccMnc[1];
            cgi.lac_nid = ReflectUtils.invokeIntMethod(nbCellInfo, "getLac");
            cgi.cid_bid = nbCellInfo.getCid();
            int iRssi = nbCellInfo.getRssi();
            cgi.sig = LocationUtils.asu2Dbm(iRssi);

        /*
         * 扰码
         */
            if (nbCellInfo.getPsc() != NeighboringCellInfo.UNKNOWN_CID) {
                LogHelper.logFile("nb Primary Scrambling Code #" + nbCellInfo.getPsc());
            }

            return cgi;
        } catch (Exception e) {
            //
        }

        return null;
    }

    /**
     * 强制刷新CGI
     *
     * @param
     * @return void
     */
    private void updateCgi() {
        try {
            CellLocation.requestLocationUpdate();
        } catch (Exception e) {
            LogHelper.logFile(e.toString());
        }
        lLastCgiUpdate = LocationUtils.getTimeBoot();
    }

    private void initPhoneStateListener() {
        mListener = new PhoneStateListener() {
            @Override
            public void onCellLocationChanged(CellLocation cellLoc) {
                if (!cgiUseful(cellLoc)) {
                    LogHelper.logFile("cell state change: cellloc invalid");
                    return;
                }

//                LogHelper.logBamai("cell state change");

                lastCellLoc = cellLoc;
            }

            @Override
            public void onSignalStrengthChanged(int iAsu) {
                int iDbm = Constants.iDefCgiSig;
                switch (iCgiT) {
                    case Cgi.iGsmT:
                    case Cgi.iLteT:
                    case Cgi.iWcdmaT:
                        iDbm = LocationUtils.asu2Dbm(iAsu);
//                        LogHelper.logBamai(String.format("gsm1: %sTS %sDbm", iAsu, iDbm));
                        break;
                    case Cgi.iCdmaT:
                        iDbm = LocationUtils.asu2Dbm(iAsu);
//                        LogHelper.logBamai(String.format("cdma1: %sTS %sDbm", iAsu, iDbm));
                        break;
                    default:
                        break;
                }
                hdlCgiSigStrenChange(iDbm);
            }

            @Override
            public void onSignalStrengthsChanged(SignalStrength sigStren) {
                if (sigStren == null) {
                    return;
                }
                int iDbm = Constants.iDefCgiSig;
                switch (iCgiT) {
                    case Cgi.iGsmT:
                    case Cgi.iLteT:
                    case Cgi.iWcdmaT:
                        iDbm = LocationUtils.asu2Dbm(sigStren.getGsmSignalStrength());
                        //LogHelper.logBamai(String.format("gsm2: %sTS %sDbm", sigStren.getGsmSignalStrength(), iDbm));
                        break;
                    case Cgi.iCdmaT:
                        iDbm = sigStren.getCdmaDbm();
                        //LogHelper.logBamai(String.format("cdma2: %sTS %sDbm", sigStren.getCdmaDbm(), iDbm));
                        break;
                    default:
                        break;
                }
                hdlCgiSigStrenChange(iDbm);
            }

            @Override
            public void onServiceStateChanged(ServiceState servState) {
                int iState = servState.getState();
                switch (iState) {
                    case ServiceState.STATE_OUT_OF_SERVICE:
                        LogHelper.logFile("phone: STATE_OUT_OF_SERVICE");
                        resetCgiData();
                        if (mLocationInternalListener != null)
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_CELL, DefaultLocation.STATUS_CELL_UNAVAILABLE);
                        break;
                    case ServiceState.STATE_IN_SERVICE:
                        LogHelper.logFile("phone: STATE_IN_SERVICE");
                        updateCgi();
                        if (mLocationInternalListener != null)
                            mLocationInternalListener.onStatusUpdate(DefaultLocation.STATUS_CELL, DefaultLocation.STATUS_CELL_AVAILABLE);
                        break;
                    default:
                        break;
                }
            }
        };

        String strPsl = "android.telephony.PhoneStateListener";
        String strProp = "";
        int iListenSignal = PhoneStateListener.LISTEN_NONE;
        if (LocationUtils.getSdk() < 7) {
            strProp = "LISTEN_SIGNAL_STRENGTH";
            try {
                iListenSignal = ReflectUtils.getStaticIntProp(strPsl, strProp);
            } catch (Exception e) {
                LogHelper.logFile(e.toString());
            }
        } else {
            strProp = "LISTEN_SIGNAL_STRENGTHS";
            try {
                iListenSignal = ReflectUtils.getStaticIntProp(strPsl, strProp);
            } catch (Exception e) {
                LogHelper.logFile(e.toString());
            }
        }
        if (iListenSignal == PhoneStateListener.LISTEN_NONE) {
            mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CELL_LOCATION);
        } else {
            try {
                int iLcl = PhoneStateListener.LISTEN_CELL_LOCATION;
                mTelephonyManager.listen(mListener, iLcl | iListenSignal);
            } catch (Exception e) {
                LogHelper.logFile(e.toString());
            }
        }
        try {
            switch (getSim2T()) {
                case Constants.iSim2QualComm:
                    mTelephonyManagerSub = LocationUtils.getServ(mContext, Constants.strSim2QualComm);
                    break;
                case Constants.iSim2Mtk:
                    mTelephonyManagerSub = LocationUtils.getServ(mContext, Constants.strSim2Mtk);
                    break;
                case Constants.iSim2Def:
                    mTelephonyManagerSub = LocationUtils.getServ(mContext, Constants.strSim2Def);
                default:
                    break;
            }
        } catch (Throwable e) {
            LogHelper.logFile(e.toString());
        }

        if (mTelephonyManagerSub != null) {
            LogHelper.logFile("set sim2 state listener success " + mTelephonyManagerSub.getClass().getName());
        } else {
            LogHelper.logFile("set sim2 state listener failed");
        }
    }

    /**
     * 处理CGI的信号强度
     *
     * @param
     * @return void
     */
    private void hdlCgiSigStrenChange(int iDbm) {
        if (iDbm == Constants.iDefCgiSig) {
            iCgiSig = Constants.iDefCgiSig;
            return;
        }
        iCgiSig = iDbm;
        switch (iCgiT) {
            case Cgi.iGsmT:
            case Cgi.iCdmaT:
            case Cgi.iLteT:
            case Cgi.iWcdmaT:
                if (null != lstCgi && !lstCgi.isEmpty()) {
                    try {
                        lstCgi.get(0).sig = iCgiSig;//cdma只支持单个基站
                    } catch (Exception e) {
                        LogHelper.logFile(e.toString());
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 重置CGI数据
     *
     * @param
     * @return void
     */
    private void resetCgiData() {
        lastCellLoc = null;
        iCgiT = Cgi.iDefCgiT;
        //fix crash:http://omega.xiaojukeji.com/app/quality/crash/detail?app_id=1&msgid=hyUZPQTGS-e-kdTKdJjGuA
        if (null != lstCgi) {
            lstCgi.clear();
        }
    }

}
