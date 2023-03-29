import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/i18/localizations.dart';

/// extension 拓展类。extension methods，顾名思义，就是扩展方法
/// on 表示
/// 扩展类名：一般没用到。该案例拓展类名：LocatizationExtensionState
extension LocatizationExtensionState on State {

  ///扩展方法：自定义的扩展方法，扩展方法数量无限，不可以和特定类型里已有方法的方法名相同，否则会报错或无效；
  String getString(String id) {
    if (id == null || id.isEmpty) {
      return "";
    }
    return AppLocalizations.of(context).getString(id);
  }

}

/// 使用扩展方法地方必须import扩展类所在文件
/// 注：如果没有import扩展类所在的文件，会找不到扩展方法
/// 使用：var name = context.getString("name");
extension LocatizationExtensionContext on BuildContext {

  String getString(String id) {
    if (id == null || id.isEmpty) {
      return "";
    }
    return AppLocalizations.of(this).getString(id);
  }

}
