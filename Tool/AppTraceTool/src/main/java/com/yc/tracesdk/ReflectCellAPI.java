package com.yc.tracesdk;

import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectCellAPI {
    static Pair<Pair<CellLocation, Integer>, Boolean> getAllCellInfoReflect(TelephonyManager telephonyManager) {

        Object result = invokeMethod(telephonyManager, "getAllCellInfo");

        if (result != null && result instanceof ArrayList) {
            ArrayList<?> listResult = (ArrayList<?>)result;

            return getCgi(listResult);
        }

        return null;
    }

    static Pair<Pair<CellLocation, Integer>, Boolean> getCgi(List<?> lstCellInfo) {
        if (lstCellInfo == null || lstCellInfo.isEmpty()) {
            return null;
        }
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        GsmCellLocation gsmCellLoc = null;
        CdmaCellLocation cdmaCL = null;
        int dbm = 0;
        boolean isLte = false;
        int iT = 0;
        for (int i = 0; i < lstCellInfo.size(); i++) {
            Object oCellInfo = lstCellInfo.get(i);
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
                    iT = 1;
                } else if (cCIWcdma.isInstance(oCellInfo)) {
                    iT = 2;
                } else if (cCILte.isInstance(oCellInfo)) {
                    iT = 3;
                } else if (cCICdma.isInstance(oCellInfo)) {
                    iT = 4;
                } else {
                    iT = 0;
                }
                if (iT > 0) {
                    Object oCI = null;
                    Object identOCI = null;
                    Object sigOCI = null;
                    if (iT == 1) {
                        oCI = cCIGsm.cast(oCellInfo);
                    } else if (iT == 2) {
                        oCI = cCIWcdma.cast(oCellInfo);
                    } else if (iT == 3) {
                        oCI = cCILte.cast(oCellInfo);
                    } else if (iT == 4) {
                        oCI = cCICdma.cast(oCellInfo);
                    }
                    sigOCI = invokeMethod(oCI, "getCellSignalStrength");
                    if (sigOCI != null) {
                        dbm = invokeIntMethod(sigOCI, "getDbm");
                    }

                    identOCI = invokeMethod(oCI, "getCellIdentity");
                    if (identOCI == null) {
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
                    if (iT == 4) {
                        cdmaCL = new CdmaCellLocation();
                        iSid = invokeIntMethod(identOCI, "getSystemId");
                        iNid = invokeIntMethod(identOCI, "getNetworkId");
                        iBid = invokeIntMethod(identOCI, "getBasestationId");
                        iLon = invokeIntMethod(identOCI, "getLongitude");
                        iLat = invokeIntMethod(identOCI, "getLatitude");
                        cdmaCL.setCellLocationData(iBid, iLat, iLon, iSid, iNid);
                    } else if (iT == 3) {
                        iLac = invokeIntMethod(identOCI, "getTac");
                        iCid = invokeIntMethod(identOCI, "getCi");
                        gsmCellLoc = new GsmCellLocation();
                        gsmCellLoc.setLacAndCid(iLac, iCid);
                        isLte = true;
                    } else {
                        iLac = invokeIntMethod(identOCI, "getLac");
                        iCid = invokeIntMethod(identOCI, "getCid");
                        gsmCellLoc = new GsmCellLocation();
                        gsmCellLoc.setLacAndCid(iLac, iCid);
                    }

//                    if (iT == 1) {
//                        FileLog.write("gsm@"+iLac+"&"+iCid+"&"+dbm);
//                    } else if (iT == 2) {
//                        FileLog.write("wcdma@"+iLac+"&"+iCid+"&"+dbm);
//                    } else if (iT == 3) {
//                        FileLog.write("lte@"+iLac+"&"+iCid+"&"+dbm);
//                    } else {
//                        FileLog.write("cdma@"+iSid+"&"+iNid+"&"+iBid+"&"+dbm);
//                    }

                    break;
                }
            } catch (Exception e) {
                //FileLog.writeException(e);
            }
        }

        CellLocation ret = iT == 4 ? cdmaCL : gsmCellLoc;

        return Pair.create(Pair.create(ret, dbm), isLte);
    }

    static Object invokeMethod(Object obj, String methodName) {
        Class<?> clz = obj.getClass();
        try {
            Method method = clz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(obj);
        } catch (NoSuchMethodException e1) {

        } catch (IllegalAccessException e2) {

        } catch (InvocationTargetException e3) {

        }

        return null;
    }

    public static int invokeIntMethod(Object obj, String methodName) {
        Class<?> clz = obj.getClass();
        try {
            Method method = clz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return (int)(Integer)method.invoke(obj);
        } catch (NoSuchMethodException e1) {

        } catch (IllegalAccessException e2) {

        } catch (InvocationTargetException e3) {

        }

        return 0;
    }
}
