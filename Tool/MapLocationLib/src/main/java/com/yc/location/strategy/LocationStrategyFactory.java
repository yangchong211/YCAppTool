package com.yc.location.strategy;

import android.content.Context;

import com.yc.location.CompositeLocationStrategy;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.log.LogHelper;

public class LocationStrategyFactory {

    private static LocationStrategyFactory sInstance;
    private final Context mContext;

    private LocationStrategyFactory(Context context) {
        mContext = context;
    }

    public static LocationStrategyFactory getInstance(Context context) {
        if (null == sInstance) {
            synchronized (LocationStrategyFactory.class) {
                if (null == sInstance) {
                    sInstance = new LocationStrategyFactory(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public ILocationStrategy createLocationStrategy(boolean useTecent, int requestCoordinateType) {
        ILocationStrategy locateStrategy;
        if (useTecent) {
            // request tencent location
            locateStrategy = new TencentLocationStrategy(mContext);
            LogHelper.logBamai("loc type tencent");
        } else {
            if (requestCoordinateType == DefaultLocation.COORDINATE_TYPE_WGS84) {
                //国际版应用使用
                locateStrategy = new PlayServiceLocationStrategy(mContext);
                LogHelper.logBamai("loc type wgs84");
            } else {
                //国内版应用使用
                locateStrategy = new CompositeLocationStrategy(mContext);
                LogHelper.logBamai("loc type gcj02");
            }
        }
        return locateStrategy;
    }
}
