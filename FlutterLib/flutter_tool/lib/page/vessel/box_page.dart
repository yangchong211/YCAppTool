


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/vessel/padding_page.dart';
import 'package:yc_flutter_tool/res/color/yc_colors.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class BoxPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new BoxState();
  }

}

class BoxState extends State<BoxPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("尺寸限制类容器"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text("尺寸限制类容器用于限制容器大小，Flutter中提供了多种这样的容器，"
                "如ConstrainedBox、SizedBox、UnconstrainedBox、AspectRatio等"),
            new Text(
              "这个是一个 ConstrainedBox",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            ConstrainedBox(
              constraints: BoxConstraints(
                  minWidth: double.infinity, //宽度尽可能大
                  minHeight: 50.0 //最小高度为50像素
              ),
              child: Container(
                  height: 5.0,
                  child:  getBox(Colors.green)
              ),
            ),
            new Padding(
              padding: EdgeInsets.all(10),
            ),
            new Text(
              "这个是一个 SizedBox",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("SizedBox只是ConstrainedBox的一个定制"),
            SizedBox(
                width: 80.0,
                height: 80.0,
                child: getBox(Colors.red)
            ),
            SizedBox(
                width: 80.0,
                height: 80.0,
                child: Container(
                  color: YCColors.color_2EBFD9,
                  child: Text("文本"),
                ),
            ),

            // Container(
            //   child: SizedBox(
            //       width: 80.0,
            //       height: 80.0,
            //       child: getBox(Colors.red)
            //   ),
            // ),
            ConstrainedBox(
              constraints: BoxConstraints.tightFor(width: 80.0,height: 80.0),
              child: getBox(Colors.yellow),
            ),
            new Padding(
              padding: EdgeInsets.all(10),
            ),

            new Text(
              "这个是一个 box多重限制",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("多重限制，某一个组件有多个父级ConstrainedBox限制，那么最终会是哪个生效？"
                "最终显示效果是宽90，高60，也就是说是子ConstrainedBox的minWidth生效，"
                "而minHeight是父ConstrainedBox生效。"),
            ConstrainedBox(
                constraints: BoxConstraints(minWidth: 60.0, minHeight: 60.0), //父
                child: ConstrainedBox(
                  constraints: BoxConstraints(minWidth: 90.0, minHeight: 20.0),//子
                  child: getBox(Colors.green),
                )
            ),

            new Padding(
              padding: EdgeInsets.all(10),
            ),
            new Text("最终的显示效果仍然是90，高60，效果相同，但意义不同，"
                "因为此时minWidth生效的是父ConstrainedBox，"
                "而minHeight是子ConstrainedBox生效。"),
            ConstrainedBox(
                constraints: BoxConstraints(minWidth: 90.0, minHeight: 20.0),
                child: ConstrainedBox(
                  constraints: BoxConstraints(minWidth: 60.0, minHeight: 60.0),
                  child: getBox(Colors.red),
                )
            ),
            new Text("思考题：对于maxWidth和maxHeight，多重限制的策略是什么样的呢？"),

          ],
        ),
      ),
    );
  }

  Widget getBox(Color c){
    Widget box =DecoratedBox(
      decoration: BoxDecoration(color: c),
    );
    return box;
  }

}

