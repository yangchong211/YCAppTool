
import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_widget/dialog/snack/snack_bar_utils.dart';
import 'package:flutter_widget/floating/rf_floating.dart';
import 'package:flutter_widget/notification/notification_factory.dart';
import 'package:flutter_widget/res/color/flutter_colors.dart';
import 'package:yc_flutter_tool/res/color/yc_colors.dart';
import 'package:yc_flutter_utils/flutter_utils.dart';

class ToastAndDialogPage extends StatelessWidget {


  void _showAboutDialog(BuildContext context) {
    showDialog(
        context: context,
        child: new AboutDialog(
          applicationIcon: new Icon(Icons.android),
          applicationName: "Flutter",
          applicationVersion: "3.1.1",
          children: <Widget>[new Text("更新摘要\n新增飞天遁地功能\n优化用户体验")],
        ));
  }


  //showModalBottomSheet与BottomSheet的区别是 BottomSheet充满屏幕，ModalBottomSheet半屏
  void _showModalBottomSheetDialog(BuildContext context) {
    showModalBottomSheet(
        context: context,
        builder: (BuildContext context) {
          return new Container(
            child: new Padding(
              padding: const EdgeInsets.all(10.0),
              child: new Column(
                children: <Widget>[
                  new ListTile(
                    leading: new Icon(Icons.chat),
                    title: new Text("开始会话"),
                  ),
                  new ListTile(
                    leading: new Icon(Icons.help),
                    title: new Text("操作说明"),
                  ),
                  new ListTile(
                    leading: new Icon(Icons.settings),
                    title: new Text("系统设置"),
                  ),
                  new ListTile(
                    leading: new Icon(Icons.more),
                    title: new Text("更多设置"),
                  ),
                ],
              ),
            ),
          );
        });
  }

  void _showBottomSheetDialog(BuildContext context) {
    showBottomSheet(
        context: context,
        builder: (BuildContext context) {
          return new Container(
            child: new Padding(
              padding: const EdgeInsets.all(10.0),
              child: new Column(
                children: <Widget>[
                  new ListTile(
                    leading: new Icon(Icons.chat),
                    title: new Text("开始会话"),
                  ),
                  new ListTile(
                    leading: new Icon(Icons.help),
                    title: new Text("操作说明"),
                  ),
                  new ListTile(
                    leading: new Icon(Icons.settings),
                    title: new Text("系统设置"),
                  ),
                  new ListTile(
                    leading: new Icon(Icons.more),
                    title: new Text("更多设置"),
                  ),
                ],
              ),
            ),
          );
        });
  }

  void _showSimpleDialog(BuildContext context) {
    showDialog(
        context: context,
        child: new SimpleDialog(
          title: new Text("标题"),
          contentPadding: const EdgeInsets.all(10.0),
          children: <Widget>[
            //SimpleDialog内可指定多个children
            new Text("内容1"),
            new ListTile(
              leading: new Icon(Icons.android),
              title: new Text("android"),
            ),
            new ListTile(
              leading: new Icon(Icons.android),
              title: new Text("andrpid"),
            ),
            new ListTile(
              leading: new Icon(Icons.cake),
              title: new Text("cake"),
            ),
            new ListTile(
              leading: new Icon(Icons.local_cafe),
              title: new Text("cofe"),
            ),
          ],
        ));
  }

  void _shwoAlertDialog(BuildContext context) {
    showDialog(
        context: context,
        child: new AlertDialog(
          title: new Text("标题"),
          content: new Text("内容区域"),
          actions: <Widget>[
            new FlatButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                child: new Text("确定")),
            new FlatButton(
                onPressed: () {
                  Navigator.of(context).pop();
                  print("点击取消------");
                },
                child: new Text("取消")),
          ],
        ));
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Toast-Dialog Page"),
      ),
      body: ListView(
        children: <Widget>[
          Text(
            "轻量级提示",
            textAlign: TextAlign.center,
            style: new TextStyle(fontSize: 20.0),
          ),


          MaterialButton(
            onPressed: () {
              new Tooltip(
                  message: "Tooltip轻量级提示",
                  child: Container(
                    color: Colors.redAccent,
                    alignment: Alignment.center,
                    height: 40.0,
                    child: new Text(
                      "Tooltip提示",
                      textAlign: TextAlign.center,
                    ),
                  ),
                  verticalOffset: 80.0,
                  //具体内部child Widget竖直方向的距离
                  preferBelow: false,
                  //是否显示在下面
                  height: 100.0,
                  //Tooltip的高度
                  padding: EdgeInsets.symmetric(vertical: 50.0, horizontal: 50.0)
              );
            },
            child: new Text("Tooltip轻量级提示"),
            color: Colors.redAccent,
          ),



          // 值得注意的是这个context必须不能是Scaffold节点下的context,因为Scaffold.of（）
          // 方法需要从Widget树中去找到Scaffold的Context，所以如果直接在Scaffold中使用showSnackBar，
          // 需要在外城包括上Builder Widget，这个Builder不做任何的其他操作，只不过把Widget树往下移了一层而已。
          Builder(
            builder: (BuildContext context) {
              return MaterialButton(
                onPressed: () {
                  SnackBarUtils.showSnackBarDialog(context,"吐司");
                },
                child: new Text("SnackBar"),
                color: Colors.redAccent,
              );
            },
          ),


          Text(
            "对话框提示",
            textAlign: TextAlign.center,
            style: new TextStyle(fontSize: 20.0),
          ),


          MaterialButton(
            onPressed: () {
              _showAboutDialog(context);
            },
            child: new Text("AboutDialog"),
            color: Colors.redAccent,
          ),

          MaterialButton(
            onPressed: () {
              _shwoAlertDialog(context);
            },
            child: new Text("AlertDialog"),
            color: Colors.redAccent,
          ),

          MaterialButton(
            onPressed: () {
              _showSimpleDialog(context);
            },
            child: new Text("SimpleDialog"),
            color: Colors.redAccent,
          ),

          //跟SnackBar不显示原理一样
          Builder(builder: (BuildContext context) {
            return MaterialButton(
              onPressed: () {
                _showBottomSheetDialog(context);
              },
              child: new Text("BottomSheetDialog"),
              color: Colors.redAccent,
            );
          }),

          MaterialButton(
            onPressed: () {
              _showModalBottomSheetDialog(context);
            },
            child: new Text("ModalBottomSheetDialog"),
            color: Colors.redAccent,
          ),


          MaterialButton(
            onPressed: () {
              _showCustomDialog(context);
            },
            child: new Text("自定义弹窗"),
            color: Colors.redAccent,
          ),

          MaterialButton(
            onPressed: () {
              _showCustomDialog2(context);
            },
            child: new Text("地图选择弹窗"),
            color: Colors.redAccent,
          ),
        ],
      ),
    );
  }

  void _showCustomDialog(BuildContext context) {
    showRFFloating(
      context: context,
      spec: RFFloatingSpec.auto,
      //不支持支持点击背景区域关闭
      isDismissible: false,
      //不支持支持拖拽
      enableDrag: false,
      builder: (context){
        return new NavigationSwitchDialog();
      },
    );
  }

  void _showCustomDialog2(BuildContext context) {
    showRFFloating(
      context: context,
      spec: RFFloatingSpec.auto,
      //不支持支持点击背景区域关闭
      isDismissible: false,
      //不支持支持拖拽
      enableDrag: false,
      builder: (context){
        return new NavigationSelectDialog();
      },
    );
  }
}


class NavigationSwitchDialog extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return NavigationSwitchDialogState();
  }
}

class NavigationSwitchDialogState extends State<NavigationSwitchDialog>{
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        // RFFloatingNavBar(
        //   title: RFFloatingTextAttr.text(
        //       text: "选择是否跳转外部导航",
        //       color: FlutterColors.color_00),
        // ),
        SizedBox(
          height: 40,
        ),
        Padding(
          padding: EdgeInsets.fromLTRB(
              20,
              0,
              20,
              20
          ),
          child: Container(
            alignment: Alignment.topLeft,
            child: Text(
              "Switch to Google Map？",
              // "请选择默认的导航类型",
              textAlign: TextAlign.left,
              style: new TextStyle(
                fontSize: 22,
                color: YCColors.color_00,
                fontWeight: FontWeight.w700,
              ),
              maxLines: 2,
              //超过2行省略符“...”表示
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ),
        Padding(
          padding: EdgeInsets.fromLTRB(
              20,
              10,
              20,
              10
          ),
          child: Row(
            children: <Widget>[
              Expanded(
                child: new Container(
                  height: 48,
                  child: new OutlineButton(
                      textColor: FlutterColors.color_99,
                      highlightedBorderColor: FlutterColors.color_E5,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(5.0)),
                      borderSide: new BorderSide(color: FlutterColors.color_CC),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                      child: new Text("取消")
                  ),
                ),
              ),
              SizedBox(
                width: 20,
              ),
              Expanded(
                child: new Container(
                  height: 48,
                  child: FlatButton(
                    color: FlutterColors.color_FF6325,
                    highlightColor: FlutterColors.color_FF6325,
                    colorBrightness: Brightness.dark,
                    splashColor: Colors.grey,
                    child: Text("确定"),
                    //这个是设置圆角的
                    shape:RoundedRectangleBorder(borderRadius: BorderRadius.circular(5.0)),
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                  ),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}



class NavigationSelectDialog extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return NavigationSelectDialogState();
  }
}

class NavigationSelectDialogState extends State<NavigationSelectDialog>{

  List<SelectNav> listTitle = new List<SelectNav>();
  StreamSubscription _subscription;

  @override
  void initState() {
    super.initState();
    listTitle.add(new SelectNav("User the google navigation",1,true));
    listTitle.add(new SelectNav("User waze map app",2,false));
    listTitle.add(new SelectNav("Download google map app",3,false));

    //添加前后台监听
    _registerAppSwitchBackground();
  }

  @override
  void dispose() {
    _unregisterAppSwitchBackground();
    super.dispose();
  }


  /// 监听app退至后台
  void _registerAppSwitchBackground() {
    if (null != _subscription) {
      _unregisterAppSwitchBackground();
    }
    _subscription = EventBusService.instance.eventBus.on<EventMessage>().listen((event) {
      String name = event.eventName;
      //前后台切换发生了变化
      if (name == "event_background_change") {
        bool appBackground = event.arguments["appBackground"];
        if (!appBackground && this.mounted) {
          //切至前台
          setState(() {

          });
        }
      }
    });
  }

  /// 取消监听app退至后台
  void _unregisterAppSwitchBackground() {
    if (_subscription != null) {
      _subscription.cancel();
      _subscription = null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        // RFFloatingNavBar(
        //   title: RFFloatingTextAttr.text(
        //       text: "请选择默认的导航类型",
        //       color: FlutterColors.color_00),
        // ),
        SizedBox(
          height: 40,
        ),
        Padding(
          padding: EdgeInsets.fromLTRB(
              20,
              0,
              20,
              20
          ),
          child: Container(
            alignment: Alignment.topLeft,
            child: Text(
              "Please select the default navigation method",
              // "请选择默认的导航类型",
              textAlign: TextAlign.left,
              style: new TextStyle(
                fontSize: 22,
                color: YCColors.color_00,
                fontWeight: FontWeight.w700,
              ),
              maxLines: 2,
              //超过2行省略符“...”表示
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ),
        Padding(
          padding: EdgeInsets.only(left: 20, right: 20, top: 0, bottom: 10),
          child: Container(height: 1, color: YCColors.color_E5),
        ),
        SizedBox(
          height: 150,
          child: new ListView.builder(
            padding: EdgeInsets.only(left: 20, right: 20, top: 0, bottom: 0),
            itemCount: 3,
            itemExtent: 50.0,
            itemBuilder: (BuildContext context, int index) {
              return buildListItem(context,index);
            },
            physics: NeverScrollableScrollPhysics(),
          ),
        ),
        Padding(
          padding: EdgeInsets.fromLTRB(
              20, 20, 20, 50
          ),
          child: new Row(
            children: <Widget>[
              Expanded(
                child: new Container(
                  height: 48,
                  child: new OutlineButton(
                      textColor: FlutterColors.color_99,
                      highlightedBorderColor: FlutterColors.color_E5,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(5.0)),
                      borderSide: new BorderSide(color: FlutterColors.color_CC),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                      child: new Text("取消")
                  ),
                ),
              ),
              SizedBox(
                width: 20,
              ),
              Expanded(
                child: new Container(
                  height: 48,
                  child: FlatButton(
                    color: FlutterColors.color_FF6325,
                    highlightColor: FlutterColors.color_FF6325,
                    colorBrightness: Brightness.dark,
                    splashColor: Colors.grey,
                    child: Text("确定"),
                    //这个是设置圆角的
                    shape:RoundedRectangleBorder(borderRadius: BorderRadius.circular(5.0)),
                    onPressed: () {
                      Navigator.of(context).pop();
                      if(listTitle[_radioValue].isInstall){
                        //同步状态给NA
                      } else {
                        //未安装跳转应用市场详情页
                      }
                    },
                  ),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  int _radioValue = 0;
  Widget buildListItem(BuildContext context, int index) {
    Row row = Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Expanded(
          child: new Text(
            listTitle[index].name,
            style: new TextStyle(
              fontSize: 14,
              color: YCColors.color_99,
            ),
          ),
        ),
        new Radio(
          value: index,
          groupValue: _radioValue,
          onChanged: handleRadioValueChanged,
        ),
      ],
    );
    return row;
  }

  void handleRadioValueChanged(int value) {
    setState(() {
      _radioValue = value;
    });
  }

}

class SelectNav{
  String name;
  int type;
  bool isInstall;

  SelectNav(String navName , int navType , bool navInstall){
    name = navName;
    type = navType;
    isInstall = navInstall;
  }
}
