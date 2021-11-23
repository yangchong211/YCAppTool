package com.yc.location.utils;

import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Pair;

import com.yc.location.log.LogHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {

    public static Object getStaticProp(String strClass, String strFieldName) throws Exception {
        Class<?> classObj = Class.forName(strClass);
        Field field = classObj.getField(strFieldName);
        field.setAccessible(true);
        Object propObj = field.get(classObj);
        return propObj;
    }

    public static int getStaticIntProp(String strClass, String strFieldName) throws Exception {
        Object obj = getStaticProp(strClass, strFieldName);
        return ((Integer) obj).intValue();
    }

    public static Object newInstance(String strClass, Object... oa) throws Exception {
        Class<?> classObj = Class.forName(strClass);
        Class<?>[] ca = new Class[oa.length];
        for (int i = 0; i < oa.length; i++) {
            ca[i] = oa[i].getClass();
        }
        Constructor<?> constructor = classObj.getConstructor(ca);
        return constructor.newInstance(oa);
    }

    public static final boolean testClassExistence(String strClass) {
        if (null == strClass)
            return false;

        try {
            Class.forName(strClass, false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static Object invokeMethod(Object objOwner, String strMethod,
                                      Object... oa) throws Exception {
        Class<?> classObj = objOwner.getClass();
        Class<?>[] ca = new Class[oa.length];
        for (int i = 0, j = oa.length; i < j; i++) {
            ca[i] = oa[i].getClass();
            if (ca[i] == Integer.class) {
                ca[i] = int.class;
            }
        }
        Method method = classObj.getDeclaredMethod(strMethod, ca);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(objOwner, oa);
    }

    public static int invokeIntMethod(Object objOwner,
									  String strMethod, Object... oa) throws Exception {
        Class<?> classObj = objOwner.getClass();
        Class<?>[] ca = new Class[oa.length];
        for (int i = 0, j = oa.length; i < j; i++) {
            ca[i] = oa[i].getClass();
            if (ca[i] == Integer.class) {
                ca[i] = int.class;
            }
        }
        Method method = classObj.getDeclaredMethod(strMethod, ca);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return ((Integer) method.invoke(objOwner, oa)).intValue();
    }


    public static Object invokeStaticMethod(String strClass, String strMethod,
                                            Object[] oa, Class<?>[] ca) throws Exception {
        Class<?> classObj = Class.forName(strClass);
        Method method = classObj.getDeclaredMethod(strMethod, ca);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(null, oa);
    }


    public static Pair<Pair<CellLocation, Integer>, Boolean> getAllCellInfoReflect(TelephonyManager telephonyManager) {
        Object result = invokeMethod(telephonyManager, "getAllCellInfo");
        if (result != null && result instanceof ArrayList) {
            ArrayList<?> listResult = (ArrayList<?>)result;
            return getCgi(listResult);
        }
        return null;
    }

    public static Pair<Pair<CellLocation, Integer>, Boolean> getCgi(List<?> lstCellInfo) {
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

                    if (iT == 1) {
                        LogHelper.write("gsm@"+iLac+"&"+iCid+"&"+dbm);
                    } else if (iT == 2) {
                        LogHelper.write("wcdma@"+iLac+"&"+iCid+"&"+dbm);
                    } else if (iT == 3) {
                        LogHelper.write("lte@"+iLac+"&"+iCid+"&"+dbm);
                    } else {
                        LogHelper.write("cdma@"+iSid+"&"+iNid+"&"+iBid+"&"+dbm);
                    }

                    break;
                }
            } catch (Exception e) {
                LogHelper.writeException(e);
            }
        }

        CellLocation ret = iT == 4 ? cdmaCL : gsmCellLoc;

        return Pair.create(Pair.create(ret, dbm), isLte);
    }

    public  static Object invokeMethod(Object obj, String methodName) {
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
            return (int)method.invoke(obj);
        } catch (NoSuchMethodException e1) {

        } catch (IllegalAccessException e2) {

        } catch (InvocationTargetException e3) {

        }
        return 0;
    }

}