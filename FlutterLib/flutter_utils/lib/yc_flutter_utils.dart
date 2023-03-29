
import 'dart:async';

import 'package:flutter/services.dart';

class YcFlutterUtils {
  static const MethodChannel _channel =
      const MethodChannel('yc_flutter_utils');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
