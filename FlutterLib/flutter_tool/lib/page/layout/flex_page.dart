


import 'package:flutter/material.dart';

class FlexLayoutPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("伸缩布局"),
      ),
      body: Column(
        children: <Widget>[
          new Text("弹性布局允许子组件按照一定比例来分配父容器空间。"
              "Flutter中的弹性布局主要通过Flex和Expanded来配合实现。"),
          //Flex的两个子widget按1：2来占据水平空间
          Flex(
            //弹性布局的方向
            direction: Axis.horizontal,
            children: <Widget>[
              Text("文本1"),
              Expanded(
                flex: 1,
                child: Container(
                  height: 30.0,
                  color: Colors.red,
                ),
              ),
              Text("文本逗比"),
              Expanded(
                flex: 1,
                child: Container(
                  height: 30.0,
                  color: Colors.green,
                ),
              ),
              Text("文本哈"),
              Expanded(
                flex: 1,
                child: Container(
                  height: 30.0,
                  color: Colors.amber,
                ),
              ),
              Text("文本444"),
            ],
          ),

          Padding(
            padding: const EdgeInsets.only(top: 20.0),
            child: SizedBox(
              height: 100.0,
              //Flex的三个子widget，在垂直方向按2：1：1来占用100像素的空间
              child: Flex(
                direction: Axis.vertical,
                children: <Widget>[
                  Expanded(
                    flex: 2,
                    child: Container(
                      height: 30.0,
                      color: Colors.red,
                    ),
                  ),
                  new Text("Spacer的功能是占用指定比例的空间，"
                      "实际上它只是Expanded的一个包装类"),
                  Spacer(
                    flex: 1,
                  ),
                  Expanded(
                    flex: 1,
                    child: Container(
                      height: 30.0,
                      color: Colors.green,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}

