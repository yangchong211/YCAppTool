import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:yc_flutter_tool/page/event/provider/business_pattern.dart';
import 'package:yc_flutter_tool/page/event/provider/pattern_state_service.dart';
import 'package:yc_flutter_tool/page/event/provider/service_locator.dart';

class ProviderStatePage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new ProviderStateState();
  }

}

class ProviderStateState extends State<ProviderStatePage>{

  BusinessPatternService _patternService = serviceLocator<BusinessPatternService>();

  @override
  void initState() {
    super.initState();
    _patternService.nonePattern();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Provider"),
      ),
      body: new ListView(
        children: [
          new Text(
            "这个是一个 ChangeNotifier",
            style:new TextStyle(
              color: Colors.red,
              fontSize: 20,
            ),
          ),

          new Row(
            children: [
              new RaisedButton(
                  onPressed: () {
                    _patternService.nonePattern();
                  },
                  child: new Text("none")
              ),
              new RaisedButton(
                  onPressed: () {
                    _patternService.normalPattern();
                  },
                  child: new Text("normal")
              ),
              new RaisedButton(
                  onPressed: () {
                    _patternService.smallPattern();
                  },
                  child: new Text("小屏模式")
              ),
              new RaisedButton(
                  onPressed: () {
                    BusinessPattern pattern = Provider.of<BusinessPattern>(context);
                    pattern.updateBusinessPatternState(PatternState.overview);
                    //_patternService.overviewPattern();
                  },
                  child: new Text("全屏模式")
              ),
            ],
          ),
          new Text(
            "使用Consumer获取Provider",
          ),
          // getWidget(context),
          getWidget2(context),
          new Padding(padding: EdgeInsets.all(10)),
          new Text(
            "使用Consumer获取Provider",
          ),
          getWidget3(context),
          new Padding(padding: EdgeInsets.all(10)),
          new Text(
            "使用Selector获取Provider",
          ),
          getWidget4(context),
        ],
      ),
    );
  }

  Widget getWidget(BuildContext context) {
    return Consumer<BusinessPattern>(builder: (context, businessModel, child) {
      switch (businessModel.currentState) {
        case PatternState.none:
          return  WidgetPage("无模式");
          break;
        case PatternState.normal:
          return WidgetPage("正常模式");
          break;
        case PatternState.small:
          return WidgetPage("小屏模式");
          break;
        case PatternState.overview:
          return WidgetPage("全屏模式");
          break;
        default:
          return WidgetPage("其他模式");
          return SizedBox();
      }
    });
  }

  Widget getWidget2(BuildContext context) {
    return Consumer<BusinessPattern>(builder: (context, businessModel, child) {
      switch (businessModel.currentState) {
        case PatternState.none:
          return  Text("无模式");
          break;
        case PatternState.normal:
          return Text("正常模式");
          break;
        case PatternState.small:
          return Text("小屏模式");
          break;
        case PatternState.overview:
          return Text("全屏模式");
          break;
        default:
          return Text("其他模式");
          return SizedBox();
      }
    });
  }


  Widget getWidget3(BuildContext context) {
    return Consumer<BusinessPattern>(builder: (context, businessModel, child) {
      switch (businessModel.currentState) {
        case PatternState.none:
          return  Text("无模式");
          break;
        case PatternState.normal:
          return Text("正常模式");
          break;
        case PatternState.small:
          return Text("小屏模式");
          break;
        case PatternState.overview:
          return Text("全屏模式");
          break;
        default:
          return Text("其他模式");
          return SizedBox();
      }
    });
  }

  Widget getWidget4(BuildContext context) {
    return Selector<BusinessPattern, PatternState>(
        selector: (context, businessPattern) => businessPattern.currentState,
        builder: (context, state, child) {
          switch (state) {
            case PatternState.none:
              return  Text("无模式");
              break;
            case PatternState.normal:
              return Text("正常模式");
              break;
            case PatternState.small:
              return Text("小屏模式");
              break;
            case PatternState.overview:
              return Text("全屏模式");
              break;
            default:
              return Text("其他模式");
              return SizedBox();
          }
        }
    );
  }


  Widget getWidget5(BuildContext context) {
    return Selector<BusinessPattern, PatternState>(
        selector: (context, businessPattern){
          return businessPattern.currentState;
        },
        builder: (context, state, child) {
          switch (state) {
            case PatternState.none:
              return  Text("无模式");
              break;
            case PatternState.normal:
              return Text("正常模式");
              break;
            case PatternState.small:
              return Text("小屏模式");
              break;
            case PatternState.overview:
              return Text("全屏模式");
              break;
            default:
              return Text("其他模式");
              return SizedBox();
          }
        }
    );
  }

}


class WidgetPage extends StatefulWidget{

  String title = "这个是标题";

  WidgetPage(String title){
    this.title = title;
  }

  @override
  State<StatefulWidget> createState() {
    return new WidgetState(title);
  }

}

class WidgetState extends State<WidgetPage>{

  var _title;

  WidgetState(String title){
    this._title = title;
  }
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new Text(
      "这个是一个 "+_title,
      style:new TextStyle(
        color: Colors.black,
        fontSize: 20,
      ),
    );
  }

  void _buildWidget() {
    if (this.mounted) {
      //刷新一下
      setState(() {

      });
    }
  }

}