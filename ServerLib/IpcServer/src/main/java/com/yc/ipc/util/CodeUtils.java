

package com.yc.ipc.util;

import com.google.gson.Gson;

/**
 * Created by yangchong on 16/4/9.
 */
public class CodeUtils {

    private static final Gson GSON = new Gson();

    private CodeUtils() {

    }

    public static String encode(Object object) throws HermesException {
        if (object == null) {
            return null;
        } else {
            try {
                return GSON.toJson(object);
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new HermesException(ErrorCodes.GSON_ENCODE_EXCEPTION,
                    "Error occurs when Gson encodes Object "
                    + object + " to Json.");
        }
    }

    public static <T> T decode(String data, Class<T> clazz) throws HermesException {
        try {
            return GSON.fromJson(data, clazz);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new HermesException(ErrorCodes.GSON_DECODE_EXCEPTION,
                "Error occurs when Gson decodes data of the Class "
                + clazz.getName());

    }

}
