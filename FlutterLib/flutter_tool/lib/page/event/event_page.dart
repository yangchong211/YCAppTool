

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:yc_flutter_tool/page/event/event_bus_page.dart';
import 'package:yc_flutter_tool/page/event/gesture_clash_page.dart';
import 'package:yc_flutter_tool/page/event/gesture_many_page.dart';
import 'package:yc_flutter_tool/page/event/gesture_recognizer_page.dart';
import 'package:yc_flutter_tool/page/event/pointer_event_page.dart';
import 'package:yc_flutter_tool/page/event/gesture_detector_page.dart';
import 'package:yc_flutter_tool/page/event/provider/business_pattern.dart';
import 'package:yc_flutter_tool/page/event/provider/pattern_state_service.dart';
import 'package:yc_flutter_tool/page/event/provider/pattern_state_service_impl.dart';
import 'package:yc_flutter_tool/page/event/provider/provider_state_page.dart';
import 'package:yc_flutter_tool/page/event/provider/service_locator.dart';
import 'package:yc_flutter_tool/page/event/states_widget_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class EventPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new EventState();
  }

}

class EventState extends State<EventPage>{

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {

    return new Scaffold(
      appBar: new AppBar(
        title: new Text("事件处理和通知"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            CustomRaisedButton(new StatesWidgetPage(), "状态(State)管理"),
            CustomRaisedButton(new PointerEventPage(), "原始指针事件处理"),
            CustomRaisedButton(new GestureDetectorPage(), "手势识别-GestureDetector"),
            CustomRaisedButton(new GestureRecognizerPage(), "手势识别-GestureRecognizer"),
            CustomRaisedButton(new GestureClashPage(), "手势冲突-x和y轴"),
            CustomRaisedButton(new GestureManyPage(), "手势冲突-多个手势器"),
            CustomRaisedButton(new EventBusPage(), "事件总线"),
            CustomRaisedButton(new ProviderStatePage(), "Provider状态事件"),
          ],
        ),
      ),
    );
  }

}