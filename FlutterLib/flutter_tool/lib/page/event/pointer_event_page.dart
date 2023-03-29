
import 'package:flutter/material.dart';

class PointerEventPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new PointerEventState();
  }

}

class PointerEventState extends State<PointerEventPage>{

  //定义一个状态，保存当前指针位置
  PointerEvent _event;
  String listener1 = "event";
  String listener2 = "event";
  String listener3 = "event";

  @override
  void initState() {
    super.initState();
    setState(() {

    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("原始指针事件处理"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text("在移动端，各个平台或UI系统的原始指针事件模型基本都是一致，"
                "即：一次完整的事件分为三个阶段：手指按下、手指移动、和手指抬起，"
                "而更高级别的手势（如点击、双击、拖动等）都是基于这些原始事件的。"),
            new Padding(padding: EdgeInsets.all(10)),
            new Text(
              "这个是一个 Listener",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("Listener是一个功能性组件"),
            Container(
              height: 120,
              color: Colors.blue[50],
              child: Listener(
                child: Container(
                  alignment: Alignment.center,
                  color: Colors.blue,
                  width: 300.0,
                  height: 150.0,
                  child: Text(_event?.toString()??"",style: TextStyle(color: Colors.white)),
                ),
                onPointerDown: (PointerDownEvent event) => setState(()=>_event=event),
                onPointerMove: (PointerMoveEvent event) => setState(()=>_event=event),
                onPointerUp: (PointerUpEvent event) => setState(()=>_event=event),
              ),
            ),
            new Text("Listener是一个功能性组件"),
            Container(
              height: 120,
              color: Colors.blue[50],
              child: Listener(
                  child: ConstrainedBox(
                    constraints: BoxConstraints.tight(Size(300.0, 150.0)),
                    child: Center(child: new Text("Box A"+listener1)),
                  ),
                  //behavior: HitTestBehavior.opaque,
                  onPointerDown: (event) => {
                    print("down A"),
                    setState(() {
                      listener1 = "down A";
                    }),
                  }
              ),
            ),
            new Text("translucent：当点击组件透明区域时，可以对自身边界内及底部可视区域"
                "都进行命中测试，这意味着点击顶部组件透明区域时，"
                "顶部组件和底部组件都可以接收到事件，例如："+listener2),

            Stack(
              children: <Widget>[
                Listener(
                  child: ConstrainedBox(
                    constraints: BoxConstraints.tight(Size(200.0, 150.0)),
                    child: DecoratedBox(
                        decoration: BoxDecoration(color: Colors.blue)),
                  ),
                  onPointerDown: (event) =>{
                    print("down0"),
                    setState(() {
                      listener2 = "down0";
                    }),
                  }
                ),
                Listener(
                  child: ConstrainedBox(
                    constraints: BoxConstraints.tight(Size(100.0, 60.0)),
                    child: Center(
                        child: Text("左上角200*100范围内非文本区域点击"),
                    ),
                  ),
                  onPointerDown: (event) => {
                    setState(() {
                      listener2 = "down1";
                    }),
                    print("down1")
                  },
                  //behavior: HitTestBehavior.translucent, //放开此行注释后可以"点透"
                )
              ],
            ),

            new Text(
              "这个是一个 忽略PointerEvent",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("假如我们不想让某个子树响应PointerEvent的话，我们可以使用IgnorePointer"
                "和AbsorbPointer，这两个组件都能阻止子树接收指针事件，"
                "不同之处在于AbsorbPointer本身会参与命中测试，而IgnorePointer本身不会参与，"
                "这就意味着AbsorbPointer本身是可以接收指针事件的(但其子树不行)，"
                "而IgnorePointer不可以。"+listener3),
            Listener(
              child: AbsorbPointer(
                child: Listener(
                  child: Container(
                    color: Colors.red,
                    width: 200.0,
                    height: 100.0,
                  ),
                  onPointerDown: (event)=>{
                    setState(() {
                      listener3 = "in";
                    }),
                    print("in"),
                  },
                ),
              ),
              onPointerDown: (event)=>{
                setState(() {
                  listener3 = "up";
                }),
                print("up"),
              },
            ),

          ],
        ),
      ),
    );
  }

}