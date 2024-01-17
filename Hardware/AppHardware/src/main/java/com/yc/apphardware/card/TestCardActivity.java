package com.yc.apphardware.card;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.cardmanager.CardHelper;
import com.yc.apphardware.R;
import com.yc.toolutils.AppLogUtils;

public class TestCardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardHelper.getInstance().setM1Card(new M1CardImpl());
        CardHelper.getInstance().setCpuCard(new CpuCardImpl());
        startScanCard();
        AppLogUtils.d("Card M1CardActivity");
    }

    /**
     *  开始读卡
     */
    private void startScanCard() {
        ScanCardManager mScanCardManager = new ScanCardManager((msg, cardNum) -> {
            AppLogUtils.d("");
        });
        mScanCardManager.startScanCard();
        mScanCardManager.setScanCard(true);
    }


}
