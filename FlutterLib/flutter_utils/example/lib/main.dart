import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:yc_flutter_utils/i18/localizations.dart';
import 'package:yc_flutter_utils/i18/template_time.dart';
import 'package:yc_flutter_utils/locator/get_it.dart';
import 'package:yc_flutter_utils/screen/screen_adaptation_utils.dart';
import 'package:yc_flutter_utils/screen/screen_adaption.dart';
import 'package:yc_flutter_utils/sp/sp_utils.dart';
import 'package:yc_flutter_utils/state/page_change_notifier.dart';
import 'package:yc_flutter_utils/utils/flutter_init_utils.dart';
import 'package:yc_flutter_utils/except/handle_exception.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';
import 'package:yc_flutter_utils_example/utils/bus_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/byte_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/color_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/data_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/encrypt_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/extension_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/file_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/get_it_page.dart';
import 'package:yc_flutter_utils_example/utils/i18_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/image_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/json_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/log_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/num_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/object_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/other_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/parser_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/polling_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/regex_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/screen_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/service_locator.dart';
import 'package:yc_flutter_utils_example/utils/sp_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/state_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/storage_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/system_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/text_utils_page.dart';
import 'package:yc_flutter_utils_example/utils/timer_utils_page.dart';
import 'package:yc_flutter_utils_example/widget/custom_raised_button.dart';


void main() {
  //初始化工具类操作
  Future(() async {
    await FlutterInitUtils.fetchInitUtils();
  });


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

  Future(() async {
    await SpUtils.init();
  });
  // 初始化屏幕适配
  ScreenAdaptationUtils.init(ScreenAdaptation.none());
  initSpi();

  //await FlutterInitUtils.fetchInitUtils();
  //FlutterInitUtils.fetchInitUtils();
  //runApp(MainApp());
  hookCrash(() {
    runApp(MainApp());
  });
}

Future initSpi() async {
  GetIt.instance.registerLazySingleton<PollingService>(() => PollingServiceImpl());
  GetIt.instance.registerLazySingleton<LocationListener>(() => LocationServiceCenterImpl());
  GetIt.instance.registerLazySingleton<BusinessService>(() => BusinessServiceImpl());
}



class MainApp extends StatelessWidget{
  //在构建页面时，会调用组件的build方法
  //widget的主要工作是提供一个build()方法来描述如何构建UI界面
  //通常是通过组合、拼装其它基础widget
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: new HomePage(title: 'Flutter常用工具类'),
      builder: (BuildContext context, Widget child) {
        return MultiProvider(providers: [
          ChangeNotifierProvider(create: (context) => PageChangeNotifier()),
        ], child: ServiceLocator(child));
      },
    );
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

class HomePageState extends State<HomePage>{


  @override
  void initState() {
    super.initState();
    LogUtils.init(tag : "yc",isDebug: true , maxLen: 128);
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
  }

  @override
  void deactivate() {
    super.deactivate();
  }

  @override
  void dispose() {
    super.dispose();
  }

  //在构建页面时，会调用组件的build方法
  //widget的主要工作是提供一个build()方法来描述如何构建UI界面
  //通常是通过组合、拼装其它基础widget
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
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
      home: Scaffold(
        appBar: new AppBar(
          title: new Text(this.widget.title),
        ),
        body: new Center(
          child: new ListView(
            children: <Widget>[
              CustomRaisedButton(new BusPage(), "EventBus 事件通知工具类"),
              CustomRaisedButton(new GetItPage(), "serviceLocator测试"),
              CustomRaisedButton(new LogUtilsPage(), "LogUtils 日志工具类"),
              CustomRaisedButton(new DatePage(), "DateUtils 日期工具类"),
              CustomRaisedButton(new JsonUtilsPage(), "JsonUtils Json工具类"),
              CustomRaisedButton(new FileStoragePage(), "FileUtils 文件工具类"),
              CustomRaisedButton(new EncryptPage(), "EncryptUtils 加解密工具类"),
              CustomRaisedButton(new ObjectPage(), "ObjectUtils Object工具类"),
              CustomRaisedButton(new TextPage(), "TextUtils 文本工具类"),
              CustomRaisedButton(new ScreenPage(), "ScreenUtils 屏幕工具类"),
              CustomRaisedButton(new I18Page(), "I18 国际化工具类"),
              CustomRaisedButton(new NumPage(), "NumUtils 格式处理工具类"),
              CustomRaisedButton(new ColorPage(), "ColorUtils 颜色工具类"),
              CustomRaisedButton(new ImagePage(), "ImageUtils 图片工具类"),
              CustomRaisedButton(new TimerPage(), "TimerUtils 计时器工具类"),
              CustomRaisedButton(new RegexPage(), "RegexUtils 正则校验工具类"),
              CustomRaisedButton(new StoragePage(), "StorageUtils 文件管理工具类"),
              CustomRaisedButton(new ExtensionPage(), "extension_xx 拓展工具类"),
              CustomRaisedButton(new SpPage(), "SpUtils sp存储工具类"),
              CustomRaisedButton(new ParserPage(), "MarkupUtils 解析xml/html工具类"),
              CustomRaisedButton(new SystemPage(), "SystemUtils 系统工具类"),
              CustomRaisedButton(new BytePage(), "Byte 字节工具类"),
              CustomRaisedButton(new PollingPage(), "TaskQueueUtils 队列工具类"),
              CustomRaisedButton(new StatePage(), "PageBaseState 状态切换管理"),
              CustomRaisedButton(new OtherPage(), "其他一些工具类"),
            ],
          ),
        ),
      ),
    );
  }

}


