package com.yc.apploglib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class Printer {
    @NonNull
    abstract String name();

    abstract void println(int level, String tag, String message, Throwable tr);

    @Override
    public final boolean equals(@Nullable Object obj) {
        if (obj instanceof Printer) {
            if (obj == this) {
                return true;
            }
            Printer p = (Printer) obj;
            return name().equals(p.name());
        }
        return false;
    }
}
