import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';

class LifecycleAppPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new _LifecycleAppPageState('构造函数');
  }
}

class _LifecycleAppPageState extends State<LifecycleAppPage>
    with WidgetsBindingObserver {
  String str;

  int count = 0;

  _LifecycleAppPageState(this.str);

  @override
  void initState() {
    LogUtils.d('initState');
    super.initState();
    WidgetsBinding.instance.addObserver(this);
  }

  @override
  void didChangeDependencies() {
    LogUtils.d('didChangeDependencies');
    super.didChangeDependencies();
  }

  @override
  void didUpdateWidget(LifecycleAppPage oldWidget) {
    LogUtils.d('didUpdateWidget');
    super.didUpdateWidget(oldWidget);
  }

  @override
  void deactivate() {
    LogUtils.d('deactivate');
    super.deactivate();
  }

  @override
  void dispose() {
    LogUtils.d('dispose');
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    switch (state) {
      case AppLifecycleState.inactive:
        LogUtils.d('AppLifecycleState.inactive');
        break;
      case AppLifecycleState.paused:
        LogUtils.d('AppLifecycleState.paused');
        break;
      case AppLifecycleState.resumed:
        LogUtils.d('AppLifecycleState.resumed');
        break;
      case AppLifecycleState.detached:
        LogUtils.d('AppLifecycleState.detached');
        break;
    }

    super.didChangeAppLifecycleState(state);
  }

  @override
  Widget build(BuildContext context) {
    LogUtils.d('build');
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('lifecycle生命周期 学习'),
        centerTitle: true,
      ),
      body: new OrientationBuilder(
        builder: (context, orientation) {
          return new Center(
            child: new Text(
              '当前计数值：$count',
              style: new TextStyle(
                  color: orientation == Orientation.portrait
                      ? Colors.blue
                      : Colors.red),
            ),
          );
        },
      ),
      floatingActionButton: new FloatingActionButton(
          child: new Text('click'),
          onPressed: () {
            count++;
            setState(() {});
          }),
    );
  }
}

class LifecyclePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: new LifecycleAppPage(),
    );
  }
}