


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/color/yc_colors.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';
import 'package:yc_flutter_utils/screen/screen_utils.dart';
class PositionPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new PositionState();
  }

}

class PositionState extends State<PositionPage>{
  @override
  Widget build(BuildContext context) {
    ScreenUtils.instance.init(context);
    var screenWidth = ScreenUtils.screenWidth;
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Position"),
      ),
      body: new Column(
        children: [
          new Text('Position，可以随意摆放一个组件，有点像绝对布局'),
          //有点像绝对布局
          new Positioned(
            right: 10,
            bottom: 100,
            width: screenWidth,
            child: getWidget(),
          ),
        ],
      ),
    );
  }

  Widget getWidget(){
    return Container(
      color: Colors.brown,
      child: Row(
        children: [
          LeftComponent(),
          RightComponent(),
        ],
        crossAxisAlignment: CrossAxisAlignment.end,
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
      ),
    );
  }

}

class LeftComponent extends StatefulWidget {
  LeftComponent();
  @override
  _LeftComponentState createState() => _LeftComponentState();
}

class _LeftComponentState extends State<LeftComponent> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Align(
      //左边
      alignment: Alignment.bottomLeft,
      child: Container(
        //写一个真布局
        child: Stack(
          children: [
            _getSafetyButton(),
          ],
        ),
      ),
    );
  }

  Widget _getSafetyButton() {
    return new Container(
      child: GestureDetector(
        behavior: HitTestBehavior.opaque,
        onTap: () {
          LogUtils.d("--------_getSafetyButton------");
        },
        child: Container(
            height: 50,
            width: 50,
            margin: EdgeInsets.only(left: 16, bottom: 16, top: 4),
            padding: EdgeInsets.symmetric(vertical: 10, horizontal: 12),
            decoration: new BoxDecoration(
              color: YCColors.color_F0,
              borderRadius: BorderRadius.all(Radius.circular(10)),
              boxShadow: [
                BoxShadow(
                  offset: Offset(2.0,2.0),
                  blurRadius: 8,
                  color: Colors.black54,
                ),
              ]
            ),
            child: Icon(Icons.add, color: Color(0xFF007AFF), size: 30)),
      ),
    );
  }

}


class RightComponent extends StatefulWidget {

  RightComponent();
  @override
  _RightComponentState createState() => _RightComponentState();
}

class _RightComponentState extends State<RightComponent> {

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: Alignment.bottomRight,
      child: Container(
        margin: EdgeInsets.only(right: 16),
        child: Column(
          children: _getMerchantCustomerWidget(),
          crossAxisAlignment: CrossAxisAlignment.end,
        ),
      ),
    );
  }

  List<Widget> _getMerchantCustomerWidget() {
    List<Widget> list = [];
    list.add(_buildResetMapButton());
    list.add(_buildOverViewButton());
    list.add(_buildNavigationMapButton());
    return list;
  }

  Widget _buildResetMapButton() {
    return Container(
        child: GestureDetector(
          behavior: HitTestBehavior.opaque,
          onTap: () {
          },
          child: Container(
              height: 50,
              width: 50,
              margin: EdgeInsets.only(bottom: 16),
              padding: EdgeInsets.symmetric(vertical: 10, horizontal: 10),
              decoration: new BoxDecoration(
                  color: YCColors.color_F0,
                  borderRadius: BorderRadius.all(Radius.circular(10.0)),
                  boxShadow: [
                    BoxShadow(
                      offset: Offset(2.0, 2.0),
                      blurRadius: 8,
                      color: Colors.black54,
                    )
                  ]),
              child: Icon(Icons.access_alarms, color: Color(0xFF007AFF), size: 30)),
        ));
  }

  Widget _buildNavigationMapButton() {
    return Container(
        child: GestureDetector(
          behavior: HitTestBehavior.opaque,
          onTap: () {
          },
          child: Container(
              height: 50,
              width: 50,
              margin: EdgeInsets.only(bottom: 16),
              padding: EdgeInsets.symmetric(vertical: 10, horizontal: 10),
              decoration: new BoxDecoration(
                  color: YCColors.color_F0,
                  borderRadius: BorderRadius.all(Radius.circular(10.0)),
                  boxShadow: [
                    BoxShadow(
                      offset: Offset(2.0, 2.0),
                      blurRadius: 8,
                      color: Colors.black54,
                    )
                  ]),
              child: Icon(Icons.info, color: Color(0xFF007AFF), size: 30)),
        ));
  }

  Widget _buildOverViewButton() {
    return Container(
        child: GestureDetector(
          behavior: HitTestBehavior.opaque,
          onTap: () {

          },
          child: Container(
              height: 50,
              width: 50,
              margin: EdgeInsets.only(bottom: 16),
              padding: EdgeInsets.symmetric(vertical: 10, horizontal: 10),
              decoration: new BoxDecoration(
                  color: YCColors.color_F0,
                  borderRadius: BorderRadius.all(Radius.circular(10.0)),
                  boxShadow: [
                    BoxShadow(
                      offset: Offset(2.0, 2.0),
                      blurRadius: 8,
                      color: Colors.black54,
                    )
                  ]),
              child: Icon(Icons.cancel, color: Color(0xFF007AFF), size: 30)),
        ));
  }


}



