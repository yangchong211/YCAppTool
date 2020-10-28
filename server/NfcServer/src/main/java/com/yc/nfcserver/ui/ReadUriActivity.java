package com.yc.nfcserver.ui;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.yc.nfcserver.BaseNfcActivity;
import com.yc.nfcserver.R;
import com.yc.nfcserver.UriPrefixUtils;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Description: 读取url
 * 这个是在界面上显示nfc卡的uri链接内容
 */
public class ReadUriActivity extends BaseNfcActivity {

    private TextView mNfcText;
    private String mTagText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_uri);
        mNfcText = (TextView) findViewById(R.id.tv_nfctext);
    }

    @Override
    public void onNewIntent(Intent intent) {
        //获取Tag对象
        super.onNewIntent(intent);
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //获取Ndef的实例
        Ndef ndef = Ndef.get(detectedTag);
        mTagText = ndef.getType() + "\n max size:" + ndef.getMaxSize() + " bytes\n\n";
        readNfcTag(intent);
        mNfcText.setText(mTagText);
    }

    /**
     * 读取NFC标签Uri
     */
    private void readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage ndefMessage = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                if (rawMsgs.length > 0) {
                    ndefMessage = (NdefMessage) rawMsgs[0];
                    contentSize = ndefMessage.toByteArray().length;
                } else {
                    return;
                }
            }
            try {
                NdefRecord ndefRecord = ndefMessage.getRecords()[0];
                Log.i("JAVA", ndefRecord.toString());
                Uri uri = parse(ndefRecord);
                Log.i("JAVA", "uri:" + uri.toString());
                mTagText += uri.toString() + "\n\nUri\n" + contentSize + " bytes";
            } catch (Exception e) {
            }
        }
    }

    /**
     * 解析NdefRecord中Uri数据
     *
     * @param record
     * @return
     */
    public static Uri parse(NdefRecord record) {
        short tnf = record.getTnf();
        if (tnf == NdefRecord.TNF_WELL_KNOWN) {
            return parseWellKnown(record);
        } else if (tnf == NdefRecord.TNF_ABSOLUTE_URI) {
            return parseAbsolute(record);
        }
        throw new IllegalArgumentException("Unknown TNF " + tnf);
    }

    /**
     * 处理绝对的Uri
     * 没有Uri识别码，也就是没有Uri前缀，存储的全部是字符串
     *
     * @param ndefRecord 描述NDEF信息的一个信息段，一个NdefMessage可能包含一个或者多个NdefRecord
     * @return
     */
    private static Uri parseAbsolute(NdefRecord ndefRecord) {
        //获取所有的字节数据
        byte[] payload = ndefRecord.getPayload();
        Uri uri = Uri.parse(new String(payload, Charset.forName("UTF-8")));
        return uri;
    }

    /**
     * 处理已知类型的Uri
     *
     * @param ndefRecord
     * @return
     */
    private static Uri parseWellKnown(NdefRecord ndefRecord) {
        //判断数据是否是Uri类型的
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_URI)){
            return null;
        }
        //获取所有的字节数据
        byte[] payload = ndefRecord.getPayload();
        String prefix = UriPrefixUtils.URI_PREFIX_MAP.get(payload[0]);
        byte[] prefixBytes = prefix.getBytes(Charset.forName("UTF-8"));
        byte[] fullUri = new byte[prefixBytes.length + payload.length - 1];
        System.arraycopy(prefixBytes, 0, fullUri, 0, prefixBytes.length);
        System.arraycopy(payload, 1, fullUri, prefixBytes.length, payload.length - 1);
        Uri uri = Uri.parse(new String(fullUri, Charset.forName("UTF-8")));
        return uri;
    }
}
