package com.yc.apphardware.card;

import com.yc.apphardware.card.lib.CardReadManager;
import com.yc.cpucard.AbstractCpuCard;

public class CpuCardImpl extends AbstractCpuCard {


    @Override
    public String search() {
        return null;
    }

    @Override
    public String[] sendAPDU(byte[] cosCmd) {
        return new String[0];
    }

    @Override
    public byte[] reset() {
        return CardReadManager.getInstance().CardCpuReset();
    }
}
