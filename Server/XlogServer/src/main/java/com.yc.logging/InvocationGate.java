package com.yc.logging;


public interface InvocationGate {

    final long TIME_UNAVAILABLE = -1;

    /**
     * The caller of this method can decide to skip further work if the returned value is true.
     * <p>
     * Implementations should be able to give a reasonable answer even if  current time date is unavailable.
     *
     * @return if true, caller should skip further work
     */
    public abstract boolean isTooSoon(long currentTime);
}
