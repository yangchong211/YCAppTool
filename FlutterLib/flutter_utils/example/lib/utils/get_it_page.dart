import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/locator/get_it.dart';
import 'package:yc_flutter_utils/locator/get_it_helper.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';



class GetItPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new GetItState();
  }

}

class GetItState extends State<GetItPage>{

  String title = "初始化值";
  GetItLocator getItLocator;
  String titleLocation = "位置";
  String titleLocationData = "位置数据";
  LocationListener _locationListener = locationService();

  @override
  void initState() {
    super.initState();
    if(getItLocator==null){
      getItLocator = new GetItLocator();
      getItLocator.register();
    }
    _locationListener.registerLocationChangedFunction(_onLocationChange);
    _locationListener.registerDataChangedFunction(_onDataChange);
  }

  @override
  void dispose() {
    super.dispose();
    if(getItLocator!=null){
      getItLocator.reset();
    }
    _locationListener.unregisterLocationChangedFunction(_onLocationChange);
    _locationListener.unregisterDataChangedFunction(_onDataChange);
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(title: new Text("测试getIt的功能")),
        body: ListView(
          children: <Widget>[
            new Text("测试GetItHelper的功能，ServiceLocator，将类与依赖解耦，让类在编译的时候并知道依赖相的具体实现。从而提升其隔离性"),
            new Text(title),
            RaisedButton(
              onPressed: () {
                var businessService = getItLocator.get();
                var noneBusinessPattern = businessService.noneBusinessPattern();
                setState(() {
                  title = noneBusinessPattern+ "---使用GetItHelper";
                });
              },
              color: const Color(0xffff0000),
              child: new Text('使用GetItHelper，获取接口的子类数据'),
            ),
            RaisedButton(
              onPressed: () {
                var businessService = getItLocator.get();
                var noneBusinessPattern = businessService.noneBusinessPattern();
                setState(() {
                  title = noneBusinessPattern + "---使用GetIt";
                });
              },
              color: const Color(0xffff0000),
              child: new Text('使用GetIt，获取接口的子类数据'),
            ),
            RaisedButton(
              onPressed: () {
                // 注册地图资源服务
                GetIt.I.registerLazySingleton<ResourceService>(() => ResourceManagerImpl());

                // 注册后才能使用
                ResourceService _resourceService = resourceService();
                _resourceService.init();
                var style = _resourceService.getStyle();
                _resourceService.release();
                setState(() {
                  title = "---使用GetIt调用ResourceService--"+style;
                });
              },
              color: const Color(0xffff0000),
              child: new Text('使用GetIt调用ResourceService'),
            ),

            RaisedButton(
              onPressed: () {
                _locationListener.locationChangedCallback(1.8, 1.6);
              },
              color: const Color(0xffff0000),
              child: new Text('修改位置：$titleLocation'),
            ),
            RaisedButton(
              onPressed: () {
                _locationListener.locationDataChangedCallback(520.0);
              },
              color: const Color(0xffff0000),
              child: new Text('修改位置数据：$titleLocationData'),
            ),
          ],
        ));
  }


  void _onLocationChange(double lat, double lng) {
    setState(() {
      titleLocation = "lat $lat , lng $lng";
    });
  }

  void _onDataChange(double p1) {
    setState(() {
      titleLocationData = "double $p1";
    });
  }
}

class GetItLocator{

  //简单案例，方便分析代码原理
  GetItHelper getIt = new GetItHelper();

  void register(){
    //注册模式状态管理service
    getIt.registerSingleton<BusinessService>(new BusinessServiceImpl());
  }

  void reset(){
    getIt.reset();
  }

  BusinessService get(){
    var businessService = getIt<BusinessService>();
    return businessService;
  }
}

abstract class BusinessService {
  //无业务模式
  String noneBusinessPattern();
}


class BusinessServiceImpl extends BusinessService {
  BusinessServiceImpl();
  @override
  String noneBusinessPattern() {
    LogUtils.d("-----noneBusinessPattern");
    return "获取子类的数据";
  }
}





ResourceService Function() resourceService = () => GetIt.I.get<ResourceService>();
abstract class ResourceService {

  ///初始化数据
  Future<void> init();
  ///释放
  void release();
  ///获取样式
  String getStyle();

}

class ResourceManagerImpl extends ResourceService{
  @override
  String getStyle() {
    LogUtils.d("ResourceManagerImpl------getStyle");
    return "getStyle";
  }

  @override
  Future<void> init() {
    LogUtils.d("ResourceManagerImpl------init");
  }

  @override
  void release() {
    LogUtils.e("ResourceManagerImpl------release");
  }

}








LocationListener Function() locationService = () => GetIt.I.get<LocationListener>();

typedef LocationDataChangedFunction = void Function(double);
typedef LocationChangedFunction = void Function(double lat, double lng);

abstract class LocationListener {

  /// 注册数据变化的回调
  void registerDataChangedFunction(LocationDataChangedFunction function);
  /// 移除数据变化的回调
  void unregisterDataChangedFunction(LocationDataChangedFunction function);

  /// 移除位置变化的回调
  void registerLocationChangedFunction(LocationChangedFunction function);
  /// 移除位置变化的回调
  void unregisterLocationChangedFunction(LocationChangedFunction function);

  /// 更新位置变化
  void locationChangedCallback(double lat, double lng);
  /// 更新数据的变化
  void locationDataChangedCallback(double angle);

}

class LocationServiceCenterImpl extends LocationListener {

  List<LocationDataChangedFunction> _locationDataChangedFunction = List();
  List<LocationChangedFunction> _locationChangedFunctionList = List();

  @override
  void locationChangedCallback(double lat, double lng) {
    _locationChangedFunctionList.forEach((function) {
      function.call(lat, lng);
    });
  }

  @override
  void locationDataChangedCallback(double angle) {
    _locationDataChangedFunction.forEach((function) {
      function.call(angle);
    });
  }

  @override
  void registerDataChangedFunction(LocationDataChangedFunction function) {
    _locationDataChangedFunction.add(function);
  }

  @override
  void registerLocationChangedFunction(LocationChangedFunction function) {
    _locationChangedFunctionList.add(function);
  }

  @override
  void unregisterDataChangedFunction(LocationDataChangedFunction function) {
    _locationDataChangedFunction.remove(function);
  }

  @override
  void unregisterLocationChangedFunction(LocationChangedFunction function) {
    _locationChangedFunctionList.remove(function);
  }

}



