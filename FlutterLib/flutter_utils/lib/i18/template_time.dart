import 'package:flutter/cupertino.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';

///国际化格式时间
class TemplateTime {

  static Map<String, String> dateFormat1 = {
    "zh_CN": "HH:mm",
  };

  static Map<String, String> dateFormat2 = {
    "zh_CN": "yyyy年MM月dd日",
  };

  static String getFormatTemplate(Locale locale, Map format) {
    String  language = "${locale.languageCode}_${locale.countryCode}";
    return format[language];
  }
}

class LocalizationTime {

  static Locale locale;

  static String getFormat1(int timestamp) {
    return _getFormat(locale, timestamp, TemplateTime.dateFormat1);
  }

  static String getFormat2(int timestamp) {
    return _getFormat(locale, timestamp, TemplateTime.dateFormat2);
  }

  static String _getFormat(Locale locale, int timestamp,
      Map<String, String> mapTemplate) {
    var formatTemplate = TemplateTime.getFormatTemplate(locale, mapTemplate);
    String formatDateMs = DateUtils.formatDateMilliseconds(timestamp,format: formatTemplate);
    return formatDateMs;
  }
}
