import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';

class AppLocalizations {

  static const String TAG = "AppLocalizations";
  /// 路径
  static var assets = "assets/string/";
  /// i18n 库支持的语言
  /// 扩展位置，写在 main 函数中 runApp 之前
  static var supportedLocales = [
    const Locale('zh', 'CN'),
  ];

  static Map<String, dynamic> _localizedValues;

  final Locale locale;
  static String package;

  AppLocalizations(this.locale) {
    LogUtils.d('i18n',tag: TAG);
  }

  static AppLocalizations of(BuildContext context) {
    return Localizations.of<AppLocalizations>(context, AppLocalizations);
  }

  static Future<AppLocalizations> _load(Locale locale) async {
    AppLocalizations translations = new AppLocalizations(locale);
    String jsonContent;
    LogUtils.d('load package = $package',tag: TAG);
    String path ;
    //比如：zh_CN.json
    //languageCode就是：zh
    //countryCode就是：CN
    if (package == null) {
      String languageCode = locale.languageCode;
      String countryCode = locale.countryCode;
      LogUtils.d('load languageCode = $languageCode , '
          'countryCode = $countryCode',tag: TAG);
      path = "$assets${languageCode}_$countryCode.json";
    } else {
      path = "packages/$package/$assets${locale.languageCode}_${locale.countryCode}.json";
    }
    LogUtils.d('load path = $path',tag: TAG);
    jsonContent = await rootBundle.loadString(path);
    _localizedValues = json.decode(jsonContent);
    return translations;
  }

  static bool _isSupported(Locale locale) {
    for (final supportedLocale in supportedLocales) {
      if (supportedLocale.countryCode == locale.countryCode &&
          supportedLocale.languageCode == locale.languageCode) {
        return true;
      }
    }
    return false;
  }

  ///获取字符串信息
  String getString(String key) {
    LogUtils.d('getString = $key',tag: TAG);
    return _localizedValues[key];
  }
}

class AppLocalizationsDelegate extends LocalizationsDelegate<AppLocalizations> {

  const AppLocalizationsDelegate();

  @override
  bool isSupported(Locale locale) => AppLocalizations._isSupported((locale));

  @override
  Future<AppLocalizations> load(Locale locale) async {
    return AppLocalizations._load(locale);
  }

  @override
  bool shouldReload(AppLocalizationsDelegate old) => false;
}
