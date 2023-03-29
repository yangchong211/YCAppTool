import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';
import 'package:yc_flutter_utils/num/num_utils.dart';
import 'package:yc_flutter_utils/parsing/elements.dart';
import 'package:yc_flutter_utils/parsing/markup_utils.dart';
import 'package:yc_flutter_utils/sp/sp_utils.dart';
import 'package:yc_flutter_utils/toast/snack_utils.dart';
import 'package:yc_flutter_utils_example/model/city.dart';
import 'package:yc_flutter_utils/extens/extension_map.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ParserPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new _DatePageState();
  }
}

class _DatePageState extends State<ParserPage> {
  String markup1;
  bool isWidget = false;
  String markup2;
  String markup3;
  String editedMarkup4;


  @override
  void initState() {
    super.initState();
    String markup = """
      <!DOCTYPE html>
      <html>
        <head>
          <meta charset="UTF-8">
          <title>title</title>
        </head>
        <body>
        
        </body>
      </html>
    """;
    markup1 = MarkupUtils.markup2json(markup).toJson();


    String jsonString = r"""
      {
        "type": "root",
        "children": [{
            "type": "element",
            "tag": "html",
            "children": [{
                "type": "element",
                "tag": "head",
                "children": [{
                    "type": "element",
                    "tag": "meta",
                    "attributes": [{
                        "name": "charset",
                        "value": "UTF-8",
                        "valueOriginal": "\"UTF-8\""
                    }]
                }, {
                    "type": "element",
                    "tag": "title",
                    "children": [{
                        "type": "text",
                        "text": "title"
                    }]
                }]
            }, {
                "type": "element",
                "tag": "body"
            }]
        }]
    }
    """;
    markup2 = MarkupUtils.jsonToMarkup(jsonString);
    LogUtils.d(markup2,tag: "jsonToMarkup 2 :");


    Node node3 = RootNode([
      ElementNode(tag: "message", children: [
        ElementNode(tag: "text", children: [TextNode("Hello!!")]),
        ElementNode(tag: "from", children: [TextNode("Manoj sadhu")]),
        CommentNode("A simple comment")
      ])
    ]);

    markup3 = node3.toMarKup();
    LogUtils.d(markup3,tag: "toMarKup 3 :");

    node3.children.add(ElementNode(tag: "time", children: [TextNode("18-06-2019 09:18:00")]));
    editedMarkup4 = node3.toMarKup();
    LogUtils.d(editedMarkup4,tag: "toMarKup 4 :");


  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("Parser 解析工具类"),
        centerTitle: true,
      ),
      body: new ListView(
        children: <Widget>[
          new Text("将HTML/XML字符串转换为json 2 ：" + markup1),
          new Text("toMarKup 2 ：" + markup2),
          new Text("toMarKup 3 ：" + markup3),
          new Text("toMarKup 4 ：" + editedMarkup4),
        ],
      ),
    );
  }

}
