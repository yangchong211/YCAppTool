/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.yc.nfcserver;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * description:Nfc消息解析
 */
public class NdefMessageParser {

    // Utility class
    private NdefMessageParser() {

    }

    /** Parse an NdefMessage */
    public static List<InterNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<InterNdefRecord> getRecords(NdefRecord[] records) {
        List<InterNdefRecord> elements = new ArrayList<InterNdefRecord>();
        for (final NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record));
            } else {
//            	String temp ="";
//            	temp = new String(record.getPayload());
//            	temp = bytesToHexString(record.getPayload(),true);
            	elements.add(new InterNdefRecord() {
					@Override
					public String getViewText() {
				        String temp = new String(record.getPayload()) + "\n";
				        return temp;
					}
            	});
             }
        }
        return elements;
    }
    
    private static String hexString="0123456789ABCDEF"; 
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		for (int i = 0; i < bytes.length(); i += 2){
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		}
		return new String(baos.toByteArray());
	}
    
    
	private static String bytesToHexString(byte[] src, boolean isPrefix) {
		StringBuilder stringBuilder = new StringBuilder();
		if (isPrefix == true) {
			stringBuilder.append("0x");
		}
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.toUpperCase(Character.forDigit(
					(src[i] >>> 4) & 0x0F, 16));
			buffer[1] = Character.toUpperCase(Character.forDigit(src[i] & 0x0F,
					16));
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}
}
