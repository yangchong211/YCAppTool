


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class StatesWidgetPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new StatesWidgetState();
  }
}

class StatesWidgetState extends State<StatesWidgetPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("状态(State)管理"),
      ),
      body: new Center(
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            new Text("管理状态的最常见的方法：\n "
                "Widget管理自己的状态。\n "
                "Widget管理子Widget状态。\n "
                "混合管理（父Widget和子Widget都管理状态）。"),
            new Padding(padding: EdgeInsets.all(10),),
            new Text("如何决定使用哪种管理方法？下面是官方给出的一些原则可以帮助你做决定：\n "
                "如果状态是用户数据，如复选框的选中状态、滑块的位置，则该状态最好由父Widget管理。\n "
                "如果状态是有关界面外观效果的，例如颜色、动画，那么状态最好由Widget本身来管理。\n "
                "如果某一个状态是不同Widget共享的则最好由它们共同的父Widget管理。"),
            new Padding(padding: EdgeInsets.all(10),),
            CustomRaisedButton(new TapboxA(), "Widget管理自身状态"),
            CustomRaisedButton(new ParentWidget(), "父Widget管理子Widget的状态"),
            CustomRaisedButton(new ParentWidgetC(), "混合状态管理"),
          ],
        ),
      ),
    );
  }

}

/// Widget管理自身状态
/// Widget管理自身状态
/// Widget管理自身状态
/// Widget管理自身状态
class TapboxA extends StatefulWidget {

  TapboxA({Key key}) : super(key: key);

  @override
  _TapboxAState createState() => new _TapboxAState();
}

class _TapboxAState extends State<TapboxA> {

  bool _active = false;

  void _handleTap() {
    setState(() {
      _active = !_active;
    });
  }

  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Widget管理自身状态"),
      ),
      body: new GestureDetector(
        child: new Container(
          child: new Center(
            child: new Text(
              _active ? 'Active' : 'Inactive',
              style: new TextStyle(fontSize: 32.0, color: Colors.white),
            ),
          ),
          width: 200.0,
          height: 200.0,
          decoration: new BoxDecoration(
            color: _active ? Colors.lightGreen[700] : Colors.grey[600],
          ),
        ),
        //点击
        onTap: _handleTap,
      ),
    );
  }
}

///父Widget管理子Widget的状态
///父Widget管理子Widget的状态
///父Widget管理子Widget的状态
///父Widget管理子Widget的状态
// ParentWidget 为 TapboxB 管理状态.

//------------------------ ParentWidget --------------------------------

class ParentWidget extends StatefulWidget {
  @override
  _ParentWidgetState createState() => new _ParentWidgetState();
}

class _ParentWidgetState extends State<ParentWidget> {
  bool _active = false;

  void _handleTapboxChanged(bool newValue) {
    setState(() {
      _active = newValue;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Widget管理子Widget状态"),
      ),
      body: new ListView(
        children: [
          new Text("Widget管理子Widget状态"),
          new TapboxB(
            active: _active,
            onChanged: _handleTapboxChanged,
          ),
        ],
      ),
    );
  }
}

class TapboxB extends StatefulWidget{

  final bool active;
  final ValueChanged<bool> onChanged;

  TapboxB({Key key , this.active : false ,@required this.onChanged });

  @override
  State<StatefulWidget> createState() {
    return new TabboxBState();
  }

}

class TabboxBState extends State<TapboxB>{

  void _handleTap() {
    widget.onChanged(!widget.active);
  }

  @override
  Widget build(BuildContext context) {
    return new GestureDetector(
      onTap: _handleTap,
      child: new Container(
        child: new Center(
          child: new Text(
            widget.active ? 'Active' : 'Inactive',
          ),
        ),
        width: 100,
        height: 100,
        decoration: new BoxDecoration(
          color: widget.active ? Colors.lightGreen[700] : Colors.grey[850],
        ),
      ),
    );
  }

}

//------------------------- TapboxB ----------------------------------

// class TapboxB extends StatelessWidget {
//
//   TapboxB({Key key, this.active: false, @required this.onChanged})
//       : super(key: key);
//
//   final bool active;
//   final ValueChanged<bool> onChanged;
//
//   void _handleTap() {
//     onChanged(!active);
//   }
//
//   Widget build(BuildContext context) {
//     return new GestureDetector(
//       //点击
//       onTap: _handleTap,
//       child: new Container(
//         child: new Center(
//           child: new Text(
//             active ? 'Active' : 'Inactive',
//             style: new TextStyle(fontSize: 32.0, color: Colors.white),
//           ),
//         ),
//         width: 200.0,
//         height: 200.0,
//         decoration: new BoxDecoration(
//           color: active ? Colors.lightGreen[700] : Colors.grey[600],
//         ),
//       ),
//     );
//   }
// }

/// 混合状态管理
/// 混合状态管理
/// 混合状态管理
/// 混合状态管理
//---------------------------- ParentWidget ----------------------------

class ParentWidgetC extends StatefulWidget {
  @override
  _ParentWidgetCState createState() => new _ParentWidgetCState();
}

class _ParentWidgetCState extends State<ParentWidgetC> {
  bool _active = false;

  void _handleTapboxChanged(bool newValue) {
    setState(() {
      _active = newValue;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("简单混合管理状态"),
      ),
      body: new Container(
        child: new ListView(
          children: [
            new Center(
              child: new Text("标题"),
            ),
            new Text("_ParentWidgetCState状态管理"),
            new Padding(padding: EdgeInsets.all(10)),
            new Text(
              _active ? 'Active' : 'Inactive',
            ),
            new Padding(padding: EdgeInsets.all(10)),
            new Text("_TapboxCState状态管理"),
            new TapboxC(
              active: _active,
              onChanged: _handleTapboxChanged,
            )
          ],
        ),
      ),
    );
  }
}

//----------------------------- TapboxC ------------------------------

class TapboxC extends StatefulWidget {

  TapboxC({Key key , this.active : false , @required this.onChanged})
      : super(key : key);

  final bool active;
  final ValueChanged<bool> onChanged;

  @override
  _TapboxCState createState() => new _TapboxCState();
}

class _TapboxCState extends State<TapboxC> {
  bool _highlight = false;

  void _handleTapDown(TapDownDetails details) {
    setState(() {
      _highlight = true;
    });
  }

  void _handleTapUp(TapUpDetails details) {
    setState(() {
      _highlight = false;
    });
  }

  void _handleTapCancel() {
    setState(() {
      _highlight = false;
    });
  }

  void _handleTap() {
    widget.onChanged(!widget.active);
  }

  @override
  Widget build(BuildContext context) {
    // 在按下时添加绿色边框，当抬起时，取消高亮
    return new GestureDetector(
      // 处理按下事件
      onTapDown: _handleTapDown,
      // 处理抬起事件
      onTapUp: _handleTapUp,
      // 处理点击事件
      onTap: _handleTap,
      // 处理结束事件
      onTapCancel: _handleTapCancel,
      child: new Container(
        child: new ListView(
          children: [
            new Text("这个是TapboxC"),
            new Center(
              child: new Text(widget.active ? 'Active' : 'Inactive',
                  style: new TextStyle(fontSize: 32.0, color: Colors.white)),
            ),
          ],
        ),
        width: 200.0,
        height: 200.0,
        decoration: new BoxDecoration(
          color: widget.active ? Colors.lightGreen[700] : Colors.grey[600],
          border: _highlight
              ? new Border.all(
            color: Colors.teal[700],
            width: 10.0,
          )
              : null,
        ),
      ),
    );
  }
}

