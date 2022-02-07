
package com.yc.catonhelperlib.canary.monitor;

import android.content.Context;

import com.yc.catonhelperlib.canary.internal.BlockInfo;

public interface BlockInterceptor {
    void onBlock(Context context, BlockInfo blockInfo);
}
