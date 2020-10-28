/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yc.nfcserver;

import android.net.Uri;
import android.nfc.NdefRecord;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * A parsed record containing a Uri.
 */
public class UriRecord implements InterNdefRecord {

    private final Uri mUri;

    private UriRecord(Uri uri) {
        this.mUri = Preconditions.checkNotNull(uri);
    }

    public String getViewText(){
        return mUri.toString();
    }

    public Uri getUri() {
        return mUri;
    }

    /**
     * Convert {@link NdefRecord} into a {@link Uri}.
     * This will handle both TNF_WELL_KNOWN / RTD_URI and TNF_ABSOLUTE_URI.
     *
     * @throws IllegalArgumentException if the NdefRecord is not a record
     *         containing a URI.
     */
    public static UriRecord parse(NdefRecord record) {
        short tnf = record.getTnf();
        if (tnf == NdefRecord.TNF_WELL_KNOWN) {
            return parseWellKnown(record);
        } else if (tnf == NdefRecord.TNF_ABSOLUTE_URI) {
            return parseAbsolute(record);
        }
        throw new IllegalArgumentException("Unknown TNF " + tnf);
    }

    /** Parse and absolute URI record */
    private static UriRecord parseAbsolute(NdefRecord record) {
        byte[] payload = record.getPayload();
        Uri uri = Uri.parse(new String(payload, StandardCharsets.UTF_8));
        return new UriRecord(uri);
    }

    /** Parse an well known URI record */
    private static UriRecord parseWellKnown(NdefRecord record) {
        Preconditions.checkArgument(Arrays.equals(record.getType(), NdefRecord.RTD_URI));
        byte[] payload = record.getPayload();
        /*
         * payload[0] contains the URI Identifier Code, per the
         * NFC Forum "URI Record Type Definition" section 3.2.2.
         *
         * payload[1]...payload[payload.length - 1] contains the rest of
         * the URI.
         */
        String prefix = UriPrefixUtils.URI_PREFIX_MAP.get(payload[0]);
        byte[] fullUri = new byte[0];
        if (prefix != null) {
            fullUri = Bytes.concat(prefix.getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(payload, 1, payload.length));
        }
        Uri uri = Uri.parse(new String(fullUri, StandardCharsets.UTF_8));
        return new UriRecord(uri);
    }

    public static boolean isUri(NdefRecord record) {
        try {
            parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static final byte[] EMPTY = new byte[0];
}
