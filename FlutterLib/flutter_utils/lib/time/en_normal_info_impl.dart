


import 'package:yc_flutter_utils/time/abs_time_info.dart';

class EnNormalInfo implements AbsTimelineInfo {

  String suffixAgo() => ' ago';

  String suffixAfter() => ' after';

  int maxJustNowSecond() => 30;

  String lessThanOneMinute() => 'just now';

  String customYesterday() => 'Yesterday';

  bool keepOneDay() => true;

  bool keepTwoDays() => false;

  String oneMinute(int minutes) => 'a minute';

  String minutes(int minutes) => '$minutes minutes';

  String anHour(int hours) => 'an hour';

  String hours(int hours) => '$hours hours';

  String oneDay(int days) => 'a day';

  String weeks(int week) => ''; //x week(星期x).

  String days(int days) => '$days days';
}
