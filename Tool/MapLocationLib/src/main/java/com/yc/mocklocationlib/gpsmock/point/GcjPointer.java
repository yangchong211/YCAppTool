package com.yc.mocklocationlib.gpsmock.point;

public class GcjPointer extends GeoPointer {

    public GcjPointer(double latitude, double longitude) {
        super(latitude, longitude);
    }

    public WgsPointer toWgsPointer() {
        if (GeoPointer.outOfChina(this.mLatitude, this.mLongitude)) {
            return new WgsPointer(this.mLatitude, this.mLongitude);
        } else {
            double[] delta = GeoPointer.delta(this.mLatitude, this.mLongitude);
            return new WgsPointer(this.mLatitude - delta[0],
                    this.mLongitude - delta[1]);
        }
    }
}
