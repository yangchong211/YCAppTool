
/// (xx)Configurable output.
/// (xx)为可配置输出.
enum DayFormat {
  /// (less than 10s->just now)、x minutes、x hours、(Yesterday)、x days.
  /// (小于10s->刚刚)、x分钟、x小时、(昨天)、x天.
  Simple,

  /// (less than 10s->just now)、x minutes、x hours、[This year:(Yesterday/a day ago)、(two days age)、MM-dd ]、[past years: yyyy-MM-dd]
  /// (小于10s->刚刚)、x分钟、x小时、[今年: (昨天/1天前)、(2天前)、MM-dd],[往年: yyyy-MM-dd].
  Common,

  /// 日期 + HH:mm
  /// (less than 10s->just now)、x minutes、x hours、[This year:(Yesterday HH:mm/a day ago)、(two days age)、MM-dd HH:mm]、[past years: yyyy-MM-dd HH:mm]
  /// 小于10s->刚刚)、x分钟、x小时、[今年: (昨天 HH:mm/1天前)、(2天前)、MM-dd HH:mm],[往年: yyyy-MM-dd HH:mm].
  Full,
}