package com.yc.http.synthesis.request;

import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;

import com.yc.http.synthesis.EasyConfig;
import com.yc.http.synthesis.EasyLog;
import com.yc.http.synthesis.EasyUtils;
import com.yc.http.synthesis.body.JsonBody;
import com.yc.http.synthesis.body.ProgressBody;
import com.yc.http.synthesis.body.TextBody;
import com.yc.http.synthesis.body.UpdateBody;
import com.yc.http.synthesis.listener.OnHttpListener;
import com.yc.http.synthesis.listener.OnUpdateListener;
import com.yc.http.synthesis.model.BodyType;
import com.yc.http.synthesis.model.FileContentResolver;
import com.yc.http.synthesis.model.HttpHeaders;
import com.yc.http.synthesis.model.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Okio;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/10/07
 *    desc   : 带 RequestBody 请求
 */
public abstract class BodyRequest<T extends BodyRequest<?>> extends HttpRequest<T> {

    private OnUpdateListener<?> mUpdateListener;

    private RequestBody mRequestBody;

    public BodyRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    /**
     * 自定义 json 字符串
     */
    public T json(Map<?, ?> map) {
        if (map == null) {
            return (T) this;
        }
        return body(new JsonBody(map));
    }

    public T json(List<?> list) {
        if (list == null) {
            return (T) this;
        }
        return body(new JsonBody(list));
    }

    public T json(String json) {
        if (json == null) {
            return (T) this;
        }
        return body(new JsonBody(json));
    }

    /**
     * 自定义文本字符串
     */
    public T text(String text) {
        if (text == null) {
            return (T) this;
        }
        return body(new TextBody(text));
    }

    /**
     * 自定义 RequestBody
     */
    public T body(RequestBody body) {
        mRequestBody = body;
        return (T) this;
    }

    /**
     * 执行异步请求（执行传入上传进度监听器）
     */
    @Override
    public void request(OnHttpListener<?> listener) {
        if (listener instanceof OnUpdateListener) {
            mUpdateListener = (OnUpdateListener<?>) listener;
        }
        if (mRequestBody != null) {
            mRequestBody = new ProgressBody(this, mRequestBody, getLifecycleOwner(), mUpdateListener);
        }
        super.request(listener);
    }

    @Override
    protected void addHttpParams(HttpParams params, String key, Object value, BodyType bodyType) {
        switch (bodyType) {
            case JSON:
                // Json 提交
                params.put(key, EasyUtils.convertObject(value));
                break;
            case FORM:
            default:
                // 表单提交
                params.put(key, value);
                break;
        }
    }

    @Override
    protected void addRequestParams(Request.Builder requestBuilder, HttpParams params, BodyType type) {
        RequestBody body = mRequestBody != null ? mRequestBody : createRequestBody(params, type);
        requestBuilder.method(getRequestMethod(), body);
    }

    @Override
    protected void printRequestLog(Request request, HttpParams params, HttpHeaders headers, BodyType type) {
        if (!EasyConfig.getInstance().isLogEnabled()) {
            return;
        }

        EasyLog.printKeyValue(this, "RequestUrl", String.valueOf(request.url()));
        EasyLog.printKeyValue(this, "RequestMethod", getRequestMethod());

        RequestBody body = request.body();

        // 打印请求头和参数的日志
        if (!headers.isEmpty() || !params.isEmpty()) {
            EasyLog.printLine(this);
        }

        for (String key : headers.getKeys()) {
            EasyLog.printKeyValue(this, key, headers.get(key));
        }

        if (!headers.isEmpty() && !params.isEmpty()) {
            EasyLog.printLine(this);
        }

        if (body instanceof FormBody ||
                body instanceof MultipartBody ||
                body instanceof ProgressBody) {
            // 打印表单
            for (String key : params.getKeys()) {
                Object value = params.get(key);
                if (value instanceof Map) {
                    // 如果这是一个 Map 集合
                    Map<?, ?> map = ((Map<?, ?>) value);
                    for (Object itemKey : map.keySet()) {
                        if (itemKey == null) {
                            continue;
                        }
                        printKeyValue(String.valueOf(itemKey), map.get(itemKey));
                    }
                } else if (value instanceof List) {
                    // 如果这是一个 List 集合
                    List<?> list = (List<?>) value;
                    for (int i = 0; i < list.size(); i++) {
                        Object itemValue = list.get(i);
                        printKeyValue(key + "[" + i + "]", itemValue);
                    }
                } else {
                    printKeyValue(key, value);
                }
            }
        } else if (body instanceof JsonBody) {
            // 打印 Json
            EasyLog.printJson(this, body.toString());
        } else if (body != null) {
            // 打印文本
            EasyLog.printLog(this, body.toString());
        }

        if (!headers.isEmpty() || !params.isEmpty()) {
            EasyLog.printLine(this);
        }
    }

    /**
     * 组装 RequestBody 对象
     */
    private RequestBody createRequestBody(HttpParams params, BodyType type) {
        RequestBody requestBody;

        if (params.isMultipart() && !params.isEmpty()) {
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
            bodyBuilder.setType(MultipartBody.FORM);
            for (String key : params.getKeys()) {
                Object value = params.get(key);

                if (value instanceof Map) {
                    // 如果这是一个 Map 集合
                    Map<?, ?> map = ((Map<?, ?>) value);
                    for (Object itemKey : map.keySet()) {
                        if (itemKey == null) {
                           continue;
                        }
                        Object itemValue = map.get(itemKey);
                        if (itemValue == null) {
                            continue;
                        }
                        addFormData(bodyBuilder, String.valueOf(itemKey), itemValue);
                    }
                } else if (value instanceof List) {
                    // 如果这是一个 List 集合
                    List<?> list = (List<?>) value;
                    for (Object itemValue : list) {
                        if (itemValue == null) {
                            continue;
                        }
                        addFormData(bodyBuilder, key, itemValue);
                    }
                } else {
                    addFormData(bodyBuilder, key, value);
                }
            }

            try {
                requestBody = bodyBuilder.build();
            } catch (IllegalStateException ignored) {
                // 如果参数为空则会抛出异常：Multipart body must have at least one part.
                requestBody = new FormBody.Builder().build();
            }

        } else if (type == BodyType.JSON) {
            requestBody = new JsonBody(params.getParams());
        } else {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (!params.isEmpty()) {
                for (String key : params.getKeys()) {
                    Object value = params.get(key);
                    if (value instanceof List) {
                        List<?> list = (List<?>) value;
                        for (Object itemValue : list) {
                            if (itemValue == null) {
                                continue;
                            }
                            bodyBuilder.add(key, String.valueOf(itemValue));
                        }
                        continue;
                    }

                    bodyBuilder.add(key, String.valueOf(value));
                }
            }
            requestBody = bodyBuilder.build();
        }

        return mUpdateListener == null ? requestBody : new ProgressBody(this, requestBody, getLifecycleOwner(), mUpdateListener);
    }

    /**
     * 添加参数
     */
    private void addFormData(MultipartBody.Builder bodyBuilder, String key, Object object) {
        if (object instanceof File) {
            // 如果这是一个 File 对象
            File file = (File) object;
            String fileName = null;
            if (file instanceof FileContentResolver) {
                fileName = ((FileContentResolver) file).getFileName();
            }
            if (TextUtils.isEmpty(fileName)) {
                fileName = file.getName();
            }
            // 文件名必须不能带中文，所以这里要编码
            String encodeFileName = EasyUtils.encodeString(fileName);

            try {
                MultipartBody.Part part;
                if (file instanceof FileContentResolver) {
                    FileContentResolver fileContentResolver = (FileContentResolver) file;
                    InputStream inputStream = fileContentResolver.openInputStream();
                    part = MultipartBody.Part.createFormData(key, encodeFileName, new UpdateBody(
                            Okio.source(inputStream), fileContentResolver.getContentType(),
                            fileName, inputStream.available()));
                } else {
                    part = MultipartBody.Part.createFormData(key, encodeFileName, new UpdateBody(file));
                }
                bodyBuilder.addPart(part);
            } catch (FileNotFoundException e) {
                // 文件不存在，将被忽略上传
                EasyLog.printLog(this, "File does not exist, will be ignored upload: " +
                        key + " = " + file.getPath());
            } catch (IOException e) {
                EasyLog.printThrowable(this, e);
                // 文件流读取失败，将被忽略上传
                EasyLog.printLog(this, "File stream reading failed and will be ignored upload: " +
                        key + " = " + file.getPath());
            }
        } else if (object instanceof InputStream) {
            // 如果这是一个 InputStream 对象
            InputStream inputStream = (InputStream) object;
            try {
                bodyBuilder.addPart(MultipartBody.Part.createFormData(key, null, new UpdateBody(inputStream, key)));
            } catch (IOException e) {
                EasyLog.printThrowable(this, e);
            }
        } else if (object instanceof RequestBody) {
            // 如果这是一个自定义的 RequestBody 对象
            RequestBody requestBody = (RequestBody) object;
            if (requestBody instanceof UpdateBody) {
                bodyBuilder.addPart(MultipartBody.Part.createFormData(key, EasyUtils.encodeString(
                        ((UpdateBody) requestBody).getKeyName()), requestBody));
            } else {
                bodyBuilder.addPart(MultipartBody.Part.createFormData(key, null, requestBody));
            }
        } else if (object instanceof MultipartBody.Part) {
            // 如果这是一个自定义的 MultipartBody.Part 对象
            bodyBuilder.addPart((MultipartBody.Part) object);
        } else {
            // 如果这是一个普通参数
            bodyBuilder.addFormDataPart(key, String.valueOf(object));
        }
    }
}