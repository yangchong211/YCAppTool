package com.yc.catonhelperlib.canary.ui;

import java.util.Locale;


public class BlockInfoCorruptException extends Exception {

    public BlockInfoCorruptException(BlockInfoEx blockInfo) {
        this(String.format(Locale.US,
                "BlockInfo (%s) is corrupt.", blockInfo.logFile.getName()));
    }

    public BlockInfoCorruptException(String detailMessage) {
        super(detailMessage);
    }
}
