import 'dart:async';
import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/locator/get_it.dart';
import 'package:yc_flutter_utils_example/utils/polling_utils_page.dart';


GetIt serviceLocator = GetIt.instance;

class ServiceLocator extends StatefulWidget {

  final Widget child;
  ServiceLocator(this.child);
  @override
  _ServiceLocator createState() => _ServiceLocator();
}

class _ServiceLocator extends State<ServiceLocator> with WidgetsBindingObserver {

  Future _fetchContextFuture;

  @override
  void initState() {
    super.initState();
    _fetchContextFuture = fetchContext();
    WidgetsBinding.instance.addObserver(this);
  }

  Future fetchContext() async {
    setupServiceLocator(context);
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
        future: _fetchContextFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.done) {
            return widget.child;
          } else {
            return Container();
          }
        });
  }

  @override
  void dispose() {
    super.dispose();
    serviceLocator.resetLazySingleton<PollingService>();
    WidgetsBinding.instance.removeObserver(this);
  }

  void setupServiceLocator(BuildContext context) {
    if (!serviceLocator.isRegistered<PollingService>()) {
      serviceLocator.registerLazySingleton<PollingService>(() => PollingServiceImpl());
    }
  }

}
