package com.yc.mocklocationlib.gpsmock.point;


import java.text.DecimalFormat;
import java.util.Objects;

public class GeoPointer {
    static DecimalFormat sDecimalFormat = new DecimalFormat("0.000000");
    double mLongitude;
    double mLatitude;

    public GeoPointer(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public static boolean outOfChina(double lat, double lng) {
        if (lng >= 72.004D && lng <= 137.8347D) {
            return lat < 0.8293D || lat > 55.8271D;
        } else {
            return true;
        }
    }

    protected static double[] delta(double lat, double lng) {
        double[] delta = new double[2];
        double a = 6378137.0D;
        double ee = 0.006693421622965943D;
        double dLat = transformLat(lng - 105.0D, lat - 35.0D);
        double dLng = transformLon(lng - 105.0D, lat - 35.0D);
        double radLat = lat / 180.0D * 3.141592653589793D;
        double magic = Math.sin(radLat);
        magic = 1.0D - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        delta[0] = dLat * 180.0D / (a * (1.0D - ee) / (magic * sqrtMagic) * 3.141592653589793D);
        delta[1] = dLng * 180.0D / (a / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
        return delta;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x * y + 0.2D * Math.sqrt(Math.abs(x));
        ret += (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math.sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(y * 3.141592653589793D) + 40.0D * Math.sin(y / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (160.0D * Math.sin(y / 12.0D * 3.141592653589793D) + 320.0D * Math.sin(y * 3.141592653589793D / 30.0D)) * 2.0D / 3.0D;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D * Math.sqrt(Math.abs(x));
        ret += (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math.sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(x * 3.141592653589793D) + 40.0D * Math.sin(x / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (150.0D * Math.sin(x / 12.0D * 3.141592653589793D) + 300.0D * Math.sin(x / 30.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        return ret;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof GeoPointer)) {
            return false;
        } else {
            GeoPointer otherPointer = (GeoPointer)other;
            return sDecimalFormat.format(this.mLatitude).equals(sDecimalFormat.format(otherPointer.mLatitude)) && sDecimalFormat.format(this.mLongitude).equals(sDecimalFormat.format(otherPointer.mLongitude));
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mLongitude, this.mLatitude});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("latitude, longitude: ");
        sb.append(this.mLatitude).append(" , ").append(this.mLongitude);
        return sb.toString();
    }

    public double distance(GeoPointer target) {
        double earthR = 6371000.0D;
        double x = Math.cos(this.mLatitude * 3.141592653589793D / 180.0D) * Math.cos(target.mLatitude * 3.141592653589793D / 180.0D) * Math.cos((this.mLongitude - target.mLongitude) * 3.141592653589793D / 180.0D);
        double y = Math.sin(this.mLatitude * 3.141592653589793D / 180.0D) * Math.sin(target.mLatitude * 3.141592653589793D / 180.0D);
        double s = x + y;
        if (s > 1.0D) {
            s = 1.0D;
        }

        if (s < -1.0D) {
            s = -1.0D;
        }

        double alpha = Math.acos(s);
        double distance = alpha * earthR;
        return distance;
    }
}

