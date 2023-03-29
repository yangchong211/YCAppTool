import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_widget/flutter_widget.dart';
import 'package:flutter_widget/notification/notification_change_notifier.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:yc_flutter_tool/page/dialog/dialog_page.dart';
import 'package:yc_flutter_tool/page/event/provider/business_pattern.dart';
import 'package:yc_flutter_tool/page/event/provider/service_locator.dart';
import 'package:yc_flutter_utils/flutter_utils.dart';
import 'package:yc_flutter_utils/i18/localizations.dart';
import 'package:yc_flutter_utils/i18/template_time.dart';
import 'package:yc_flutter_tool/page/animation/animation_page.dart';
import 'package:yc_flutter_tool/page/basic/basic_widget_page.dart';
import 'package:yc_flutter_tool/page/callback/call_back_page.dart';
import 'package:yc_flutter_tool/page/custom/custom_widget_page.dart';
import 'package:yc_flutter_tool/page/dialog/dialog_toast_page.dart';
import 'package:yc_flutter_tool/page/event/event_page.dart';
import 'package:yc_flutter_tool/page/function/function_page.dart';
import 'package:yc_flutter_tool/page/layout/layout_widget_page.dart';
import 'package:yc_flutter_tool/page/navigator/navigator_page.dart';
import 'package:yc_flutter_tool/page/channel/platform_page.dart';
import 'package:yc_flutter_tool/page/scroll/scroll_page.dart';
import 'package:yc_flutter_tool/page/storage/storage_page.dart';
import 'package:yc_flutter_tool/page/use/common_use_page.dart';
import 'package:yc_flutter_tool/page/vessel/vessel_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';
import 'package:yc_flutter_utils/except/handle_exception.dart';
import 'package:yc_flutter_utils/router/animation_page_route.dart';


//main函数使用了(=>)符号, 这是Dart中单行函数或方法的简写。
//void main() => runApp(MainDart());
void main() {
  FlutterWidget.init();
  //await Global.init();
  Future(() async {
    await Global.init();
  });
  //应用入口
  hookCrash(() {
    runApp(MainApp());
  });
}

class MainApp extends StatelessWidget{


  //在构建页面时，会调用组件的build方法
  //widget的主要工作是提供一个build()方法来描述如何构建UI界面
  //通常是通过组合、拼装其它基础widget
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      //一定需要添加这个
      builder: (BuildContext context, Widget child) {
        return MultiProvider(providers: [
          ChangeNotifierProvider(create: (context) => BusinessPattern()),
          ChangeNotifierProvider(create: (context) => TopNotification()),
          // ChangeNotifierProvider.value(),
        ], child: ServiceLocator(child));
      },
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      // 本地化的代理类
      localizationsDelegates: [
        const AppLocalizationsDelegate(),
        // 本地化的代理类
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      //支持的语言
      supportedLocales: AppLocalizations.supportedLocales,
      //localeListResolutionCallback: _localeListResolutionCallback,
      onGenerateRoute: (RouteSettings settings) {
        return AnimationPageRoute(builder: (context) {
          return parseRouter(settings.name, settings.arguments);
        });
      },
      //home: new HomePage(title: 'Flutter进阶之旅'),
    );
  }

  ///通过`localeResolutionCallback`或`localeListResolutionCallback`回调来监听locale改变的事件
  Locale _localeListResolutionCallback(List<Locale> locales,
      Iterable<Locale> supportedLocales) {
    // 判断当前locale是否为英语系国家，如果是直接返回Locale('en', 'US')
    if(locales!=null){
      for(int i=0 ; i<locales.length ; i++){
        var myLocale = locales[i];
        LogUtils.d("myLocale-$i--${myLocale.countryCode}----${myLocale.languageCode}");
      }
    }
    return Locale('en', 'US');
  }

  Widget parseRouter(String path, dynamic params) {
    print('path---->' + path + " ");
    if (path == "home") {
      return new HomePage(title: 'Flutter进阶之旅');
    } else if (path == "anim") {
      return AnimationPage();
    } else {
      return new HomePage(title: 'Flutter进阶之旅');
    }
  }

}


//StatelessWidget表示组件，一切都是widget，可以理解为组件
//有状态的组件（Stateful widget）
//无状态的组件（Stateless widget）
//Stateful widget可以拥有状态，这些状态在widget生命周期中是可以变的，而Stateless widget是不可变的
class HomePage extends StatefulWidget{

  HomePage({Key key, this.title}) : super(key: key);
  final String title;

  //createState()来创建状态(State)对象
  @override
  State<StatefulWidget> createState() {
    return new HomePageState();
  }

}

class HomePageState extends State<HomePage> with WidgetsBindingObserver {

  final String TAG = "HomePage ";

  @override
  void initState() {
    super.initState();
    LogUtils.d(TAG+'initState');
    WidgetsBinding.instance.addObserver(this);
    //todo 获取报错
    //这个过程是隐式完成的，我们并没有主动去监听系统语言切换
    //Locale myLocale = Localizations.localeOf(context);
    //LogUtils.d("myLocale--${myLocale.countryCode}----${myLocale.languageCode}");
  }

  @override
  void didChangeDependencies() {
    LogUtils.d(TAG+'didChangeDependencies');
    super.didChangeDependencies();
  }

  @override
  void deactivate() {
    LogUtils.d(TAG+'deactivate');
    super.deactivate();
  }

  @override
  void dispose() {
    LogUtils.d(TAG+'dispose');
    super.dispose();
    WidgetsBinding.instance.removeObserver(this);
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    LogUtils.d(TAG+'didChangeAppLifecycleState $state');
    super.didChangeAppLifecycleState(state);
  }

  @override
  void reassemble() {
    LogUtils.d(TAG+'reassemble');
    super.reassemble();
  }

  //在构建页面时，会调用组件的build方法
  //widget的主要工作是提供一个build()方法来描述如何构建UI界面
  //通常是通过组合、拼装其它基础widget
  @override
  Widget build(BuildContext context) {
    LogUtils.d(TAG+'build');
    return MaterialApp(
      home: Scaffold(
        appBar: new AppBar(
          title: new Text(this.widget.title),
        ),
        body: new Center(
          child: new ListView(
            children: <Widget>[
              CustomRaisedButton(new BasicWidgetPage("充哥"), "基础类widget"),
              CustomRaisedButton(new LayoutWidgetPage(), "布局类widget"),
              CustomRaisedButton(new VesselWidgetPage(), "容器类widget"),
              CustomRaisedButton(new CommonUsePage(), "Scaffold脚手架"),
              CustomRaisedButton(new ScrollPage(), "可滚动widget"),
              CustomRaisedButton(new FunctionPage(), "功能型组件"),
              CustomRaisedButton(new CustomWidgetPage(), "自定义widget"),
              CustomRaisedButton(new DialogPage(), "弹窗和吐司"),
              CustomRaisedButton(new EventPage(), "事件处理和通知"),
              CustomRaisedButton(new AnimationPage(), "动画处理"),
              CustomRaisedButton(new PlatformPage(), "平台调用"),
              CustomRaisedButton(new CallBackPage(), "页面跳转后回调"),
              CustomRaisedButton(new StoragePage(), "数据库操作"),
              CustomRaisedButton(new NavigatorPage(), "路由页面跳转"),
            ],
          ),
        ),
      ),
    );
  }
}




class Global {

  // 网络缓存对象
  // 可选的主题列表
  // 是否为release版
  static bool get isRelease => bool.fromEnvironment("dart.vm.product");

  //初始化全局信息，会在APP启动时执行
  static Future init() async {

    // 如果没有缓存策略，设置默认缓存策略

    //初始化网络请求相关配置


    AppLocalizations.supportedLocales = [
      const Locale('en', 'US'),
      const Locale('pt', 'BR'),
      const Locale('ja', 'JP'),
      const Locale('zh', 'CN'),
    ];


    //区分格式化时间
    TemplateTime.dateFormat2 = {
      "en_US": "MM-dd-yyyy",
      "pt_BR": "dd-MM-yyyy",
      "ja_JP": "yyyy/MM/dd",
      "zh_CN": "yyyy年MM月dd日",
    };
    //比如：zh_CN，表示中国
    //languageCode就是：zh
    //countryCode就是：CN
    //Locale myLocale = Localizations.localeOf(context);
    LocalizationTime.locale = new Locale("zh","CN");

    //初始化
    WidgetsFlutterBinding.ensureInitialized();

  }

}
