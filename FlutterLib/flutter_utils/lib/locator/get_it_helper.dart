
typedef FactoryFunc<T> = T Function();

/// ServiceLocator简单实现代码
class GetItHelper {

  final _factories = new Map<Type, _ServiceFactory<dynamic>>();

  /// 默认情况下，不允许第二次注册类型。
  /// 如果你真的需要你可以通过设置[allowReassignment]= true禁用断言
  bool allowReassignment = false;

  /// 检索或创建一个注册类型[T]的实例，这取决于用于该类型的注册函数。
  T get<T>() {
    _ServiceFactory<T> object = _factories[T];
    if (object == null) {
      throw new Exception(
          "Object of type ${T.toString()} is not registered inside GetIt");
    }
    return object.getObject();
  }

   T call<T>() {
    return get<T>();
  }

  /// 注册一个类型，以便每次对该类型的[get]调用都会创建一个新实例
  void registerFactory<T>(FactoryFunc<T> func) {
    assert(allowReassignment || !_factories.containsKey(T),
        "Type ${T.toString()} is already registered");
    _factories[T] = new _ServiceFactory<T>(_ServiceFactoryType.alwaysNew,
        creationFunction: func);
  }

  /// 通过传递一个工厂函数注册一个类型为Singleton，该工厂函数将在第一次调用该类型的[get]时被调用
  void registerLazySingleton<T>(FactoryFunc<T> func) {
    assert(allowReassignment || !_factories.containsKey(T),
        "Type ${T.toString()} is already registered");
    _factories[T] = new _ServiceFactory<T>(_ServiceFactoryType.lazy,
        creationFunction: func);
  }

  /// 通过传递一个实例来注册一个类型为Singleton，该实例将在每次调用该类型的[get]时返回
  void registerSingleton<T>(T instance) {
    assert(allowReassignment || !_factories.containsKey(T),
        "Type ${T.toString()} is already registered");
    _factories[T] = new _ServiceFactory<T>(_ServiceFactoryType.constant,
        instance: instance);
  }

  /// 清除所有已注册的类型。
  void reset() {
    _factories.clear();
  }
}

enum _ServiceFactoryType { alwaysNew, constant, lazy }

class _ServiceFactory<T> {
  _ServiceFactoryType type;
  FactoryFunc creationFunction;
  Object instance;

  _ServiceFactory(this.type, {this.creationFunction, this.instance});

  T getObject() {
    try {
      switch (type) {
        case _ServiceFactoryType.alwaysNew:
          return creationFunction() as T;
          break;
        case _ServiceFactoryType.constant:
          return instance as T;
          break;
        case _ServiceFactoryType.lazy:
          if (instance == null) {
            instance = creationFunction();
          }
          return instance as T;
          break;
      }
    } catch (e, s) {
      print("Error while creating ${T.toString()}");
      print('Stack trace:\n $s');
      rethrow;
    }
    return null;
  }
}
