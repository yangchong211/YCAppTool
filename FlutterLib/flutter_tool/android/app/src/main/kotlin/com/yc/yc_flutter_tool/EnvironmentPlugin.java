package com.yc.yc_flutter_tool;

import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.JSONMethodCodec;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class EnvironmentPlugin implements MethodChannel.MethodCallHandler, FlutterPlugin {

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        MethodChannel channel = new MethodChannel(binding.getBinaryMessenger(),
                "com.yc.flutter/environment_plugin", JSONMethodCodec.INSTANCE);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "environment":
                Map<String, Object> hashMap = new HashMap<>();
//                hashMap.put("isGlobal", BuildConfig.IS_GLOBAL ? 1 : 0);
//                hashMap.put("lang", LocaleService.getInstance().getCurrentLocaleTag());
//                hashMap.put("apiHost", AppConfig.sApiHost);
                result.success(hashMap);
                break;
        }
    }
}
