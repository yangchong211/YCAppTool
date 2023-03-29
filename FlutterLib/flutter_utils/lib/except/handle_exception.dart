import 'dart:async';
import 'package:flutter/cupertino.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';

const bool _inProduction = const bool.fromEnvironment("dart.vm.product");
const String TAG = "handle_exception : ";

void reportError(dynamic e, StackTrace stack) {
  LogUtils.e('$TAG e---->' + e.toString());
  LogUtils.e('$TAG stack---->' + stack.toString());
}

String _stringify(Object exception) {
  try {
    return exception.toString();
  } catch (e) {
    // intentionally left empty.
  }
  return 'Error';
}

Widget _errorWidgetBuilder(FlutterErrorDetails details) {
  LogUtils.e('$TAG _errorWidgetBuilder '+ _stringify(details.exception)
      + _stringify(details.stack));
  String message = '';
  if (true) {
    message = _stringify(details.exception) +
        '\nSee also: https://flutter.dev/docs/testing/errors';
  }
  final Object exception = details.exception;
  return ErrorWidget.withDetails(
      message: message, error: exception is FlutterError ? exception : null);
}

void hookCrash(Function main) {
  ErrorWidget.builder =
      (FlutterErrorDetails errorDetails) => _errorWidgetBuilder(errorDetails);
  if (_inProduction) {
    FlutterError.onError = (FlutterErrorDetails details) async {
      Zone.current.handleUncaughtError(details.exception, details.stack);
    };
  }

  runZoned<Future<void>>(() async {
      LogUtils.d('$TAG runZoned');
      main();
      LogUtils.d('$TAG runZoned main');
    },
    onError: (dynamic error, StackTrace stack) {
      //捕获异常打印日志
      reportError(error, stack);
    },
  );
}
