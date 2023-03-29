



import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/time/abs_time_info.dart';
import 'package:yc_flutter_utils/time/day_format.dart';
import 'package:yc_flutter_utils/time/en_info_impl.dart';
import 'package:yc_flutter_utils/time/en_normal_info_impl.dart';
import 'package:yc_flutter_utils/time/zh_info_impl.dart';
import 'package:yc_flutter_utils/time/zh_normal_info_impl.dart';

Map<String, AbsTimelineInfo> _timelineInfoMap = {
  'zh': ZhInfo(),
  'en': EnInfo(),
  'zh_normal': ZhNormalInfo(),
  'en_normal': EnNormalInfo(),
};

/// add custom configuration.
void setLocaleInfo(String locale, AbsTimelineInfo timelineInfo) {
  ArgumentError.checkNotNull(locale, '[locale] must not be null');
  ArgumentError.checkNotNull(timelineInfo, '[timelineInfo] must not be null');
  _timelineInfoMap[locale] = timelineInfo;
}

/// 时间轴工具类
class TimeUtils {

  /// format time by DateTime.
  /// dateTime
  /// locDateTime: current time or schedule time.
  /// locale: output key.
  static String formatByDateTime(
    DateTime dateTime, {
    DateTime locDateTime,
    String locale,
    DayFormat dayFormat,
  }) {
    return format(
      dateTime.millisecondsSinceEpoch,
      locTimeMs: locDateTime?.millisecondsSinceEpoch,
      locale: locale,
      dayFormat: dayFormat,
    );
  }

  /// format time by millis.
  /// dateTime : millis.
  /// locDateTime: current time or schedule time. millis.
  /// locale: output key.
  static String format(
    int ms, {
    int locTimeMs,
    String locale,
    DayFormat dayFormat,
  }) {
    int _locTimeMs = locTimeMs ?? DateTime.now().millisecondsSinceEpoch;
    String _locale = locale ?? 'en';
    AbsTimelineInfo _info = _timelineInfoMap[_locale] ?? EnInfo();
    DayFormat _dayFormat = dayFormat ?? DayFormat.Common;

    int elapsed = _locTimeMs - ms;
    String suffix;
    if (elapsed < 0) {
      suffix = _info.suffixAfter();
      // suffix after is empty. user just now.
      if (suffix.isNotEmpty) {
        elapsed = elapsed.abs();
        _dayFormat = DayFormat.Simple;
      } else {
        return _info.lessThanOneMinute();
      }
    } else {
      suffix = _info.suffixAgo();
    }

    String timeline;
    if (_info.customYesterday().isNotEmpty &&
        DateUtils.isYesterdayByMilliseconds(ms, _locTimeMs)) {
      return _getYesterday(ms, _info, _dayFormat);
    }

    if (!DateUtils.yearIsEqualByMilliseconds(ms, _locTimeMs)) {
      timeline = _getYear(ms, _dayFormat);
      if (timeline.isNotEmpty) return timeline;
    }

    final num seconds = elapsed / 1000;
    final num minutes = seconds / 60;
    final num hours = minutes / 60;
    final num days = hours / 24;

    if (seconds < 90) {
      timeline = _info.oneMinute(1);
      if (suffix != _info.suffixAfter() &&
          _info.lessThanOneMinute().isNotEmpty &&
          seconds < _info.maxJustNowSecond()) {
        timeline = _info.lessThanOneMinute();
        suffix = '';
      }
    } else if (minutes < 60) {
      timeline = _info.minutes(minutes.round());
    } else if (minutes < 90) {
      timeline = _info.anHour(1);
    } else if (hours < 24) {
      timeline = _info.hours(hours.round());
    } else {
      if ((days.round() == 1 && _info.keepOneDay() == true) ||
          (days.round() == 2 && _info.keepTwoDays() == true)) {
        _dayFormat = DayFormat.Simple;
      }
      timeline = _formatDays(ms, days.round(), _info, _dayFormat);
      suffix = (_dayFormat == DayFormat.Simple ? suffix : '');
    }
    return timeline + suffix;
  }

  /// Timeline like QQ.
  ///
  /// today (HH:mm)
  /// yesterday (昨天;Yesterday)
  /// this week (星期一,周一;Monday,Mon)
  /// others (yyyy-MM-dd)
  static String formatA(
    int ms, {
    int locMs,
    String formatToday = 'HH:mm',
    String format = 'yyyy-MM-dd',
    String languageCode = 'en',
    bool short = false,
  }) {
    int _locTimeMs = locMs ?? DateTime.now().millisecondsSinceEpoch;
    int elapsed = _locTimeMs - ms;
    if (elapsed < 0) {
      return DateUtils.formatDateMilliseconds(ms, format: formatToday);
    }

    if (DateUtils.isToday(ms, locMs: _locTimeMs)) {
      return DateUtils.formatDateMilliseconds(ms, format: formatToday);
    }

    if (DateUtils.isYesterdayByMilliseconds(ms, _locTimeMs)) {
      return languageCode == 'zh' ? '昨天' : 'Yesterday';
    }

    if (DateUtils.isWeek(ms, locMs: _locTimeMs)) {
      return DateUtils.getWeekdayByMilliseconds(ms,
          languageCode: languageCode, short: short);
    }
    return DateUtils.formatDateMilliseconds(ms, format: format);
  }

  /// get Yesterday.
  /// 获取昨天.
  static String _getYesterday(
    int ms,
    AbsTimelineInfo info,
    DayFormat dayFormat,
  ) {
    return info.customYesterday() +
        (dayFormat == DayFormat.Full
            ? (' ' + DateUtils.formatDateMilliseconds(ms, format: 'HH:mm'))
            : '');
  }

  /// get is not year info.
  /// 获取非今年信息.
  static String _getYear(
    int ms,
    DayFormat dayFormat,
  ) {
    if (dayFormat != DayFormat.Simple) {
      return DateUtils.formatDateMilliseconds(ms,
          format: (dayFormat == DayFormat.Common
              ? 'yyyy-MM-dd'
              : 'yyyy-MM-dd HH:mm'));
    }
    return '';
  }

  /// format Days.
  static String _formatDays(
    int ms,
    num days,
    AbsTimelineInfo info,
    DayFormat dayFormat,
  ) {
    String timeline;
    switch (dayFormat) {
      case DayFormat.Simple:
        timeline = (days == 1 ? info.customYesterday().isEmpty
                ? info.oneDay(days.round()) : info.days(2)
            : info.days(days.round()));
        break;
      case DayFormat.Common:
        timeline = DateUtils.formatDateMilliseconds(ms, format: 'MM-dd');
        break;
      case DayFormat.Full:
        timeline = DateUtils.formatDateMilliseconds(ms, format: 'MM-dd HH:mm');
        break;
    }
    return timeline;
  }
}
