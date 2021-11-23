package com.yc.location.easymock;

import android.location.Location;

public final class LocationModel {

    private Location location;
    private long time;

    public LocationModel(){

    }

    public LocationModel(Location location, long time) {
        this.location = location;
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
