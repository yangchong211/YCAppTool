package com.yc.logging;


import androidx.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY)
class RecoveryCoordinator {

    public final static long BACKOFF_COEFFICIENT_MIN = 20;

    static long BACKOFF_COEFFICIENT_MAX = 327680;  // BACKOFF_COEFFICIENT_MIN * 4^7

    private long backOffCoefficient = BACKOFF_COEFFICIENT_MIN;

    private static long UNSET = -1;

    private long currentTime = UNSET;

    long next = System.currentTimeMillis() + getBackoffCoefficient();

    public boolean isTooSoon() {
        long now = getCurrentTime();
        if (now > next) {
            next = now + getBackoffCoefficient();
            return false;
        } else {
            return true;
        }
    }

    void setCurrentTime(long forcedTime) {
        currentTime = forcedTime;
    }

    private long getCurrentTime() {
        if (currentTime != UNSET) {
            return currentTime;
        }
        return System.currentTimeMillis();
    }

    private long getBackoffCoefficient() {
        long currentCoeff = backOffCoefficient;
        if (backOffCoefficient < BACKOFF_COEFFICIENT_MAX) {
            backOffCoefficient *= 4;
        }
        return currentCoeff;
    }
}
