part of 'get_it.dart';

/// Two handy function that helps me to express my intention clearer and shorter to check for runtime
/// errors
// ignore: avoid_positional_boolean_parameters
void throwIf(bool condition, Object error) {
  if (condition) throw error;
}

// ignore: avoid_positional_boolean_parameters
void throwIfNot(bool condition, Object error) {
  if (!condition) throw error;
}

/// You will see a rather esoteric looking test `(const Object() is! T)` at several places
/// /// it tests if [T] is a real type and not Object or dynamic

/// For each registered factory/singleton a [_ServiceFactory<T>] is created
/// it holds either the instance of a Singleton or/and the creation functions
/// for creating an instance when [get] is called
///
/// There are three different types
enum _ServiceFactoryType {
  alwaysNew,

  /// factory which means on every call of [get] a new instance is created
  constant, // normal singleton
  lazy, // lazy
}

/// If I use `Singleton` without specifier in the comments I mean normal and lazy

class _ServiceFactory<T, P1, P2> {
  final _ServiceFactoryType factoryType;

  Type param1Type;
  Type param2Type;

  /// Because of the different creation methods we need alternative factory functions
  /// only one of them is always set.
  final FactoryFunc<T> creationFunction;
  final FactoryFuncAsync<T> asyncCreationFunction;
  final FactoryFuncParam<T, P1, P2> creationFunctionParam;
  final FactoryFuncParamAsync<T, P1, P2> asyncCreationFunctionParam;

  ///  Dispose function that is used when a scope is popped
  final DisposingFunc<T> disposeFunction;

  /// In case of a named registration the instance name is here stored for easy access
  final String instanceName;

  /// true if one of the async registration functions have been used
  final bool isAsync;

  /// If a an existing Object gets registered or an async/lazy Singleton has finished
  /// its creation it is stored here
  Object instance;

  /// the type that was used when registering. used for runtime checks
  Type registrationType;

  /// to enable Singletons to signal that they are ready (their initialization is finished)
  Completer _readyCompleter;

  /// the returned future of pending async factory calls or facttory call with dependencies
  Future<T> pendingResult;

  /// If other objects are waiting for this one
  /// they are stored here
  final List<Type> objectsWaiting = [];

  bool get isReady => _readyCompleter.isCompleted;

  bool get isNamedRegistration => instanceName != null;

  String get debugName => '$instanceName : $registrationType';

  bool get canBeWaitedFor =>
      shouldSignalReady || pendingResult != null || isAsync;

  final bool shouldSignalReady;

  _ServiceFactory(this.factoryType,
      {this.creationFunction,
      this.asyncCreationFunction,
      this.creationFunctionParam,
      this.asyncCreationFunctionParam,
      this.instance,
      this.isAsync = false,
      this.instanceName,
      @required this.shouldSignalReady,
      this.disposeFunction}) {
    registrationType = T;
    param1Type = P1;
    param2Type = P2;
    _readyCompleter = Completer();
  }

  FutureOr dispose() {
    return disposeFunction?.call(instance as T);
  }

  /// returns an instance depending on the type of the registration if [async==false]
  T getObject(dynamic param1, dynamic param2) {
    assert(
        !(factoryType != _ServiceFactoryType.alwaysNew &&
            (param1 != null || param2 != null)),
        'You can only pass parameters to factories!');

    try {
      switch (factoryType) {
        case _ServiceFactoryType.alwaysNew:
          if (creationFunctionParam != null) {
            // param1.runtimeType == param1Type should use 'is' but Dart does
            // not support this comparison. For the time being it is therefore
            // disabled
            // assert(
            //     param1 == null || param1.runtimeType == param1Type,
            //     'Incompatible Type passed as param1\n'
            //     'expected: $param1Type actual: ${param1.runtimeType}');
            // assert(
            //     param2 == null || param2.runtimeType == param2Type,
            //     'Incompatible Type passed as param2\n'
            //     'expected: $param2Type actual: ${param2.runtimeType}');
            return creationFunctionParam(param1 as P1, param2 as P2);
          } else {
            return creationFunction();
          }
          break;
        case _ServiceFactoryType.constant:
          return instance as T;
          break;
        case _ServiceFactoryType.lazy:
          if (instance == null) {
            instance = creationFunction();
            _readyCompleter.complete();
          }
          return instance as T;
          break;
        default:
          throw StateError('Impossible factoryType');
      }
    } catch (e, s) {
      // ignore: avoid_print
      print('Error while creating ${T.toString()}');
      // ignore: avoid_print
      print('Stack trace:\n $s');
      rethrow;
    }
  }

  /// returns an async instance depending on the type of the registration if [async==true] or if [dependsOn.isnoEmpty].
  Future<R> getObjectAsync<R>(dynamic param1, dynamic param2) async {
    assert(
        !(factoryType != _ServiceFactoryType.alwaysNew &&
            (param1 != null || param2 != null)),
        'You can only pass parameters to factories!');

    throwIfNot(
        isAsync || pendingResult != null,
        StateError(
            'You can only access registered factories/objects this way if they are created asynchronously'));
    try {
      switch (factoryType) {
        case _ServiceFactoryType.alwaysNew:
          if (asyncCreationFunctionParam != null) {
/*             assert(
                param1 == null || param1.runtimeType == param1Type,
                'Incompatible Type passed a param1\n'
                'expected: $param1Type actual: ${param1.runtimeType}');
            assert(
                param2 == null || param2.runtimeType == param2Type,
                'Incompatible Type passed a param2\n'
                'expected: $param2Type actual: ${param2.runtimeType}');
  */
            return asyncCreationFunctionParam(param1 as P1, param2 as P2)
                as Future<R>;
          } else {
            return asyncCreationFunction() as Future<R>;
          }
          break;
        case _ServiceFactoryType.constant:
          if (instance != null) {
            return Future<R>.value(instance as R);
          } else {
            assert(pendingResult != null);
            return pendingResult as Future<R>;
          }
          break;
        case _ServiceFactoryType.lazy:
          if (instance != null) {
            // We already have a finished instance
            return Future<R>.value(instance as R);
          } else {
            if (pendingResult !=
                null) // an async creation is already in progress
            {
              return pendingResult as Future<R>;
            }

            /// Seems this is really the first access to this async Signleton
            final asyncResult = asyncCreationFunction();

            pendingResult = asyncResult.then((newInstance) {
              if (!shouldSignalReady) {
                ///only complete automatically if the registration wasn't marked with [signalsReady==true]
                _readyCompleter.complete();
              }
              instance = newInstance;
              return newInstance;
            });
            return pendingResult as Future<R>;
          }
          break;
        default:
          throw StateError('Impossible factoryType');
      }
    } catch (e, s) {
      // ignore: avoid_print
      print('Error while creating ${T.toString()}');
      // ignore: avoid_print
      print('Stack trace:\n $s');
      rethrow;
    }
  }
}

class _Scope {
  final String name;
  final ScopeDisposeFunc disposeFunc;
  final factoriesByName =
      <String, Map<Type, _ServiceFactory<dynamic, dynamic, dynamic>>>{};

  _Scope({this.name, this.disposeFunc});

  Future<void> reset({@required bool dispose}) async {
    if (dispose) {
      for (final _factory in allFactories) {
        await _factory.dispose();
      }
    }
    factoriesByName.clear();
  }

  List<_ServiceFactory> get allFactories => factoriesByName.values
      .fold<List<_ServiceFactory>>([], (sum, x) => sum..addAll(x.values));

  Future<void> dispose() async {
    await disposeFunc?.call();
  }
}

class _GetItImplementation implements GetIt {
  static const _baseScopeName = 'baseScope';
  final _scopes = [_Scope(name: _baseScopeName)];

  _Scope get _currentScope => _scopes.last;

  /// We still support a global ready signal mechanism for that we use this
  /// Completer.
  final _globalReadyCompleter = Completer();

  /// By default it's not allowed to register a type a second time.
  /// If you really need to you can disable the asserts by setting[allowReassignment]= true
  @override
  bool allowReassignment = false;

  /// Is used by several other functions to retrieve the correct [_ServiceFactory]
  _ServiceFactory<T, dynamic, dynamic>
      _findFirstFactoryByNameAndTypeOrNull<T extends Object>(
    String instanceName, [
    Type type,
  ]) {
    /// We use an assert here instead of an `if..throw` because it gets called on every call
    /// of [get]
    /// `(const Object() is! T)` tests if [T] is a real type and not Object or dynamic
    assert(
      type != null || const Object() is! T,
      'GetIt: The compiler could not infer the type. You have to provide a type '
      'and optionally a name. Did you accidentally do `var sl=GetIt.instance();` '
      'instead of var sl=GetIt.instance;',
    );

    _ServiceFactory<T, dynamic, dynamic> instanceFactory;

    int scopeLevel = _scopes.length - 1;
    while (instanceFactory == null && scopeLevel >= 0) {
      final factoryByTypes = _scopes[scopeLevel].factoriesByName[instanceName];
      if (type == null) {
        instanceFactory = factoryByTypes != null
            ? factoryByTypes[T] as _ServiceFactory<T, dynamic, dynamic>
            : null;
      } else {
        /// in most cases we can rely on the generic type T because it is passed
        /// in by callers. In case of dependent types this does not work as these types
        /// are dynamic
        instanceFactory = factoryByTypes != null
            ? factoryByTypes[type] as _ServiceFactory<T, dynamic, dynamic>
            : null;
      }
      scopeLevel--;
    }

    return instanceFactory;
  }

  /// Is used by several other functions to retrieve the correct [_ServiceFactory]
  _ServiceFactory _findFactoryByNameAndType<T extends Object>(
    String instanceName, [
    Type type,
  ]) {
    final instanceFactory =
        _findFirstFactoryByNameAndTypeOrNull<T>(instanceName, type);

    assert(
      instanceFactory != null,
      // ignore: missing_whitespace_between_adjacent_strings
      'Object/factory with ${instanceName != null ? 'with name $instanceName and ' : ' '}'
      'type ${T.toString()} is not registered inside GetIt. '
      '\n(Did you accidentally do GetIt sl=GetIt.instance(); instead of GetIt sl=GetIt.instance;'
      '\nDid you forget to register it?)',
    );

    return instanceFactory;
  }

  /// retrieves or creates an instance of a registered type [T] depending on the registration
  /// function used for this type or based on a name.
  /// for factories you can pass up to 2 parameters [param1,param2] they have to match the types
  /// given at registration with [registerFactoryParam()]
  @override
  T get<T>({String instanceName, dynamic param1, dynamic param2}) {
    final instanceFactory = _findFactoryByNameAndType<T>(instanceName);

    Object instance;
    if (instanceFactory.isAsync || instanceFactory.pendingResult != null) {
      /// We use an assert here instead of an `if..throw` for performance reasons
      assert(
          instanceFactory.factoryType == _ServiceFactoryType.constant ||
              instanceFactory.factoryType == _ServiceFactoryType.lazy,
          "You can't use get with an async Factory of ${instanceName ?? T.toString()}.");
      assert(instanceFactory.isReady,
          'You tried to access an instance of ${instanceName ?? T.toString()} that was not ready yet');
      instance = instanceFactory.instance;
    } else {
      instance = instanceFactory.getObject(param1, param2);
    }

    assert(instance is T,
        'Object with name $instanceName has a different type (${instanceFactory.registrationType.toString()}) than the one that is inferred (${T.toString()}) where you call it');

    return instance as T;
  }

  /// Callable class so that you can write `GetIt.instance<MyType>` instead of
  /// `GetIt.instance.get<MyType>`
  @override
  T call<T>({String instanceName, dynamic param1, dynamic param2}) {
    return get<T>(instanceName: instanceName, param1: param1, param2: param2);
  }

  /// Returns an Future of an instance that is created by an async factory or a Singleton that is
  /// not ready with its initialization.
  /// for async factories you can pass up to 2 parameters [param1,param2] they have to match the types
  /// given at registration with [registerFactoryParamAsync()]
  @override
  Future<T> getAsync<T>({String instanceName, dynamic param1, dynamic param2}) {
    final factoryToGet = _findFactoryByNameAndType<T>(instanceName);
    return factoryToGet.getObjectAsync<T>(param1, param2);
  }

  /// registers a type so that a new instance will be created on each call of [get] on that type
  /// [T] type to register
  /// [func] factory function for this type
  /// [instanceName] if you provide a value here your factory gets registered with that
  /// name instead of a type. This should only be necessary if you need to register more
  /// than one instance of one type. Its highly not recommended
  @override
  void registerFactory<T>(FactoryFunc<T> func, {String instanceName}) {
    _register<T, void, void>(
        type: _ServiceFactoryType.alwaysNew,
        instanceName: instanceName,
        factoryFunc: func,
        isAsync: false,
        shouldSignalReady: false);
  }

  /// registers a type so that a new instance will be created on each call of [get] on that type based on
  /// up to two parameters provided to [get()]
  /// [T] type to register
  /// [P1] type of  param1
  /// [P2] type of  param2
  /// if you use only one parameter pass void here
  /// [func] factory function for this type that accepts two parameters
  /// [instanceName] if you provide a value here your factory gets registered with that
  /// name instead of a type. This should only be necessary if you need to register more
  /// than one instance of one type. Its highly not recommended
  ///
  /// example:
  ///    getIt.registerFactoryParam<TestClassParam,String,int>((s,i)
  ///        => TestClassParam(param1:s, param2: i));
  ///
  /// if you only use one parameter:
  ///
  ///    getIt.registerFactoryParam<TestClassParam,String,void>((s,_)
  ///        => TestClassParam(param1:s);
  @override
  void registerFactoryParam<T, P1, P2>(FactoryFuncParam<T, P1, P2> func,
      {String instanceName}) {
    _register<T, P1, P2>(
        type: _ServiceFactoryType.alwaysNew,
        instanceName: instanceName,
        factoryFuncParam: func,
        isAsync: false,
        shouldSignalReady: false);
  }

  /// We use a separate function for the async registration instead just a new parameter
  /// so make the intention explicit
  @override
  void registerFactoryAsync<T>(FactoryFuncAsync<T> asyncFunc,
      {String instanceName}) {
    _register<T, void, void>(
        type: _ServiceFactoryType.alwaysNew,
        instanceName: instanceName,
        factoryFuncAsync: asyncFunc,
        isAsync: true,
        shouldSignalReady: false);
  }

  /// registers a type so that a new instance will be created on each call of [getAsync]
  /// on that type based on up to two parameters provided to [getAsync()]
  /// the creation function is executed asynchronously and has to be accessed  with [getAsync]
  /// [T] type to register
  /// [P1] type of  param1
  /// [P2] type of  param2
  /// if you use only one parameter pass void here
  /// [func] factory function for this type that accepts two parameters
  /// [instanceName] if you provide a value here your factory gets registered with that
  /// name instead of a type. This should only be necessary if you need to register more
  /// than one instance of one type. Its highly not recommended
  ///
  /// example:
  ///    getIt.registerFactoryParam<TestClassParam,String,int>((s,i) async
  ///        => TestClassParam(param1:s, param2: i));
  ///
  /// if you only use one parameter:
  ///
  ///    getIt.registerFactoryParam<TestClassParam,String,void>((s,_) async
  ///        => TestClassParam(param1:s);
  @override
  void registerFactoryParamAsync<T, P1, P2>(
      FactoryFuncParamAsync<T, P1, P2> func,
      {String instanceName}) {
    _register<T, P1, P2>(
        type: _ServiceFactoryType.alwaysNew,
        instanceName: instanceName,
        factoryFuncParamAsync: func,
        isAsync: true,
        shouldSignalReady: false);
  }

  /// registers a type as Singleton by passing a factory function that will be called
  /// on the first call of [get] on that type
  /// [T] type to register
  /// [func] factory function for this type
  /// [instanceName] if you provide a value here your factory gets registered with that
  /// name instead of a type. This should only be necessary if you need to register more
  /// than one instance of one type. Its highly not recommended
  /// [registerLazySingleton] does not influence [allReady] however you can wait
  /// for and be dependent on a LazySingleton.
  @override
  void registerLazySingleton<T>(FactoryFunc<T> func,
      {String instanceName, DisposingFunc<T> dispose}) {
    _register<T, void, void>(
      type: _ServiceFactoryType.lazy,
      instanceName: instanceName,
      factoryFunc: func,
      isAsync: false,
      shouldSignalReady: false,
      disposeFunc: dispose,
    );
  }

  /// registers a type as Singleton by passing an [instance] of that type
  ///  that will be returned on each call of [get] on that type
  /// [T] type to register
  /// If [signalsReady] is set to `true` it means that the future you can get from `allReady()`  cannot complete until this
  /// registration was signalled ready by calling [signalsReady(instance)]
  /// [instanceName] if you provide a value here your instance gets registered with that
  /// name instead of a type. This should only be necessary if you need to register more
  /// than one instance of one type. Its highly not recommended
  @override
  void registerSingleton<T>(
    T instance, {
    String instanceName,
    bool signalsReady,
    DisposingFunc<T> dispose,
  }) {
    _register<T, void, void>(
      type: _ServiceFactoryType.constant,
      instanceName: instanceName,
      instance: instance,
      isAsync: false,
      shouldSignalReady: signalsReady ?? <T>[] is List<WillSignalReady>,
      disposeFunc: dispose,
    );
  }

  /// registers a type as Singleton by passing an factory function of that type
  /// that will be called on each call of [get] on that type
  /// [T] type to register
  /// [instanceName] if you provide a value here your instance gets registered with that
  /// name instead of a type. This should only be necessary if you need to register more
  /// than one instance of one type. Its highly not recommended
  /// [dependsOn] if this instance depends on other registered  Singletons before it can be initialized
  /// you can either orchestrate this manually using [isReady()] or pass a list of the types that the
  /// instance depends on here. [factoryFunc] won't get executed till this types are ready.
  /// [func] is called
  /// If [signalsReady] is set to `true` it means that the future you can get from `allReady()`
  /// cannot complete until this this instance was signalled ready by calling [signalsReady(instance)].
  @override
  void registerSingletonWithDependencies<T>(
    FactoryFunc<T> providerFunc, {
    String instanceName,
    Iterable<Type> dependsOn,
    bool signalsReady,
    DisposingFunc<T> dispose,
  }) {
    _register<T, void, void>(
      type: _ServiceFactoryType.constant,
      instanceName: instanceName,
      isAsync: false,
      factoryFunc: providerFunc,
      dependsOn: dependsOn,
      shouldSignalReady: signalsReady ?? <T>[] is List<WillSignalReady>,
      disposeFunc: dispose,
    );
  }

  /// registers a type as Singleton by passing an asynchronous factory function which has to return the instance
  /// that will be returned on each call of [get] on that type.
  /// Therefore you have to ensure that the instance is ready before you use [get] on it or use [getAsync()] to
  /// wait for the completion.
  /// You can wait/check if the instance is ready by using [isReady()] and [isReadySync()].
  /// [factoryfunc] is executed immediately if there are no dependencies to other Singletons (see below).
  /// As soon as it returns, this instance is marked as ready unless you don't set [signalsReady==true]
  /// [instanceName] if you provide a value here your instance gets registered with that
  /// name instead of a type. This should only be necessary if you need to register more
  /// than one instance of one type. Its highly not recommended
  /// [dependsOn] if this instance depends on other registered  Singletons before it can be initialized
  /// you can either orchestrate this manually using [isReady()] or pass a list of the types that the
  /// instance depends on here. [factoryFunc] won't get  executed till this types are ready.
  /// If [signalsReady] is set to `true` it means that the future you can get from `allReady()`  cannot complete until this
  /// this instance was signalled ready by calling [signalsReady(instance)]. In that case no automatic ready signal
  /// is made after completion of [factoryfunc]
  @override
  void registerSingletonAsync<T>(FactoryFuncAsync<T> providerFunc,
      {String instanceName,
      Iterable<Type> dependsOn,
      bool signalsReady,
      DisposingFunc<T> dispose}) {
    _register<T, void, void>(
      type: _ServiceFactoryType.constant,
      instanceName: instanceName,
      isAsync: true,
      factoryFuncAsync: providerFunc,
      dependsOn: dependsOn,
      shouldSignalReady: signalsReady ?? <T>[] is List<WillSignalReady>,
      disposeFunc: dispose,
    );
  }

  /// registers a type as Singleton by passing a async factory function that will be called
  /// on the first call of [getAsnc] on that type
  /// This is a rather esoteric requirement so you should seldom have the need to use it.
  /// This factory function [providerFunc] isn't called immediately but wait till the first call by
  /// [getAsync()] or [isReady()] is made
  /// To control if an async Singleton has completed its [providerFunc] gets a `Completer` passed
  /// as parameter that has to be completed to signal that this instance is ready.
  /// Therefore you have to ensure that the instance is ready before you use [get] on it or use [getAsync()] to
  /// wait for the completion.
  /// You can wait/check if the instance is ready by using [isReady()] and [isReadySync()].
  /// [instanceName] if you provide a value here your instance gets registered with that
  /// name instead of a type. This should only be necessary if you need to register more
  /// than one instance of one type. Its highly not recommended.
  /// [registerLazySingletonAsync] does not influence [allReady] however you can wait
  /// for and be dependent on a LazySingleton.
  @override
  void registerLazySingletonAsync<T>(FactoryFuncAsync<T> func,
      {String instanceName, DisposingFunc<T> dispose}) {
    _register<T, void, void>(
      isAsync: true,
      type: _ServiceFactoryType.lazy,
      instanceName: instanceName,
      factoryFuncAsync: func,
      shouldSignalReady: false,
      disposeFunc: dispose,
    );
  }

  /// Clears all registered types. Handy when writing unit tests
  @override
  Future<void> reset({bool dispose = true}) async {
    if (dispose) {
      for (int level = _scopes.length - 1; level >= 0; level--) {
        await _scopes[level].dispose();
        await _scopes[level].reset(dispose: dispose);
      }
    }
    _scopes.removeRange(1, _scopes.length);
    await resetScope(dispose: dispose);
  }

  /// Clears all registered types of the current scope.
  @override
  Future<void> resetScope({bool dispose = true}) async {
    if (dispose) {
      await _currentScope.dispose();
    }
    await _currentScope.reset(dispose: dispose);
  }

  /// Creates a new registration scope. If you register types after creating
  /// a new scope they will hide any previous registration of the same type.
  /// Scopes allow you to manage different live times of your Objects.
  /// [scopeName] if you name a scope you can pop all scopes above the named one
  /// by using the name.
  /// [dispose] function that will be called when you pop this scope. The scope
  /// is still valied while it is executed
  @override
  void pushNewScope({String scopeName, ScopeDisposeFunc dispose}) {
    assert(scopeName != _baseScopeName,
        'This name is reseved for the real base scope');
    assert(
        _scopes.firstWhere((x) => x.name == scopeName, orElse: () => null) ==
            null,
        'You already have used the scope name $scopeName');
    _scopes.add(_Scope(name: scopeName, disposeFunc: dispose));
  }

  /// Disposes all factories/Singletons that have ben registered in this scope
  /// and pops (destroys) the scope so that the previous scope gets active again.
  /// if you provided  dispose functions on registration, they will be called.
  /// if you passed a dispose function when you pushed this scope it will be
  /// calles before the scope is popped.
  /// As dispose funcions can be async, you should await this function.
  @override
  Future<void> popScope() async {
    assert(
        _scopes.length > 1,
        "You are already on the base scope. you can't pop"
        ' this one');
    await _currentScope.dispose();
    await _currentScope.reset(dispose: true);
    _scopes.removeLast();
  }

  /// if you have a lot of scopes with names you can pop (see [popScope]) all scopes above
  /// the scope with [scopeName] including that scope
  /// Scopes are poped in order from the top
  /// As dispose funcions can be async, you should await this function.
  @override
  Future<bool> popScopesTill(String scopeName) async {
    assert(scopeName != _baseScopeName, "You can't pop the base scope");
    if (_scopes.firstWhere((x) => x.name == scopeName, orElse: () => null) ==
        null) {
      return false;
    }
    String _scopeName;
    do {
      _scopeName = _currentScope.name;
      await popScope();
    } while (_scopeName != scopeName);
    return true;
  }

  void _register<T, P1, P2>(
      {@required _ServiceFactoryType type,
      FactoryFunc<T> factoryFunc,
      FactoryFuncParam<T, P1, P2> factoryFuncParam,
      FactoryFuncAsync<T> factoryFuncAsync,
      FactoryFuncParamAsync<T, P1, P2> factoryFuncParamAsync,
      T instance,
      @required String instanceName,
      @required bool isAsync,
      Iterable<Type> dependsOn,
      @required bool shouldSignalReady,
      DisposingFunc<T> disposeFunc}) {
    throwIfNot(
      const Object() is! T,
      'GetIt: You have to provide type. Did you accidentally do  `var sl=GetIt.instance();` instead of var sl=GetIt.instance;',
    );
    final factoriesByName = _currentScope.factoriesByName;
    throwIf(
        factoriesByName.containsKey(instanceName) &&
            factoriesByName[instanceName].containsKey(T) &&
            !allowReassignment,
        ArgumentError(
            'Object/factory with ${instanceName != null ? 'with name $instanceName and ' : ''}'
            ' type ${T.toString()} is already registered inside GetIt. '));

    final serviceFactory = _ServiceFactory<T, P1, P2>(type,
        creationFunction: factoryFunc,
        creationFunctionParam: factoryFuncParam,
        asyncCreationFunctionParam: factoryFuncParamAsync,
        asyncCreationFunction: factoryFuncAsync,
        instance: instance,
        isAsync: isAsync,
        instanceName: instanceName,
        shouldSignalReady: shouldSignalReady,
        disposeFunction: disposeFunc);

    factoriesByName.putIfAbsent(instanceName,
        () => <Type, _ServiceFactory<dynamic, dynamic, dynamic>>{});
    factoriesByName[instanceName][T] = serviceFactory;

    // simple Singletons get creates immediately
    if (type == _ServiceFactoryType.constant &&
        !shouldSignalReady &&
        !isAsync &&
        (dependsOn?.isEmpty ?? false)) {
      serviceFactory.instance = factoryFunc();
      serviceFactory._readyCompleter.complete();
      return;
    }

    // if its an async or an dependent Singleton we start its creation function here after we check if
    // it is dependent on other registered Singletons.
    if ((isAsync || (dependsOn?.isNotEmpty ?? false)) &&
        type == _ServiceFactoryType.constant) {
      /// Any client awaiting the completion of this Singleton
      /// Has to wait for the completion of the Singleton itself as well
      /// as for the completion of all the Singletons this one depends on
      /// For this we use [outerFutureGroup]
      /// A `FutureGroup` completes only if it's closed and all contained
      /// Futures have completed
      final outerFutureGroup = FutureGroup();
      Future dependentFuture;

      if (dependsOn?.isNotEmpty ?? false) {
        /// To wait for the completion of all Singletons this one is depending on
        /// before we start to create itself we use [dependentFutureGroup]
        final dependentFutureGroup = FutureGroup();

        for (final type in dependsOn) {
          final dependentFactory =
              _findFirstFactoryByNameAndTypeOrNull(null, type);
          throwIf(dependentFactory == null,
              ArgumentError('Dependent Type $type is not registered in GetIt'));
          throwIfNot(dependentFactory.canBeWaitedFor,
              ArgumentError('Dependent Type $type is not an async Singleton'));
          dependentFactory.objectsWaiting.add(serviceFactory.registrationType);
          dependentFutureGroup.add(dependentFactory._readyCompleter.future);
        }
        dependentFutureGroup.close();

        dependentFuture = dependentFutureGroup.future;
      } else {
        /// if we have no dependencies we still create a dummy Future so that
        /// we can use the same code path further down
        dependentFuture = Future.sync(() {}); // directly execute then
      }
      outerFutureGroup.add(dependentFuture);

      /// if someone uses getAsync on an async Singleton that has not be started to get created
      /// because its dependent on other objects this doesn't work because [pendingResult] is not set in
      /// that case. Therefore we have to set [outerFutureGroup] as [pendingResult]
      dependentFuture.then((_) {
        Future<T> isReadyFuture;
        if (!isAsync) {
          /// SingletonWithDepencencies
          serviceFactory.instance = factoryFunc();
          isReadyFuture = Future<T>.value(serviceFactory.instance as T);
          if (!serviceFactory.shouldSignalReady) {
            /// As this isn't an asnc function we declare it as ready here
            /// if is wasn't marked that it will signalReady
            serviceFactory._readyCompleter.complete();
          }
        } else {
          /// Async Singleton with dependencies
          final asyncResult = factoryFuncAsync();

          isReadyFuture = asyncResult.then((instance) {
            serviceFactory.instance = instance;

            if (!serviceFactory.shouldSignalReady && !serviceFactory.isReady) {
              serviceFactory._readyCompleter.complete();
            }
            return instance;
          });
        }
        outerFutureGroup.add(isReadyFuture);
        outerFutureGroup.close();
      });

      /// outerFutureGroup.future returns a Future<List> and not a Future<T>
      /// As we know that the actual factory function was added last to the FutureGroup
      /// we just use that one
      serviceFactory.pendingResult =
          outerFutureGroup.future.then((completedFutures) {
        return completedFutures.last as T;
      });
    }
  }

  /// Tests if an [instance] of an object or aType [T] or a name [instanceName]
  /// is registered inside GetIt
  @override
  bool isRegistered<T extends Object>({
    Object instance,
    String instanceName,
  }) {
    if (instance != null) {
      return _findFirstFactoryByInstanceOrNull(instance) != null;
    } else {
      return _findFirstFactoryByNameAndTypeOrNull<T>(instanceName) != null;
    }
  }

  /// Unregister an instance of an object or a factory/singleton by Type [T] or by name [instanceName]
  /// if you need to dispose any resources you can do it using [disposingFunction] function
  /// that provides a instance of your class to be disposed
  @override
  void unregister<T>(
      {Object instance,
      String instanceName,
      void Function(T) disposingFunction}) {
    _ServiceFactory factoryToRemove;
    if (instance != null) {
      factoryToRemove = _findFactoryByInstance(instance);
    } else {
      factoryToRemove = _findFactoryByNameAndType<T>(instanceName);
    }

    throwIf(
        factoryToRemove.objectsWaiting.isNotEmpty,
        StateError(
            'There are still other objects waiting for this instance so signal ready'));

    _currentScope.factoriesByName[factoryToRemove.instanceName]
        .remove(factoryToRemove.registrationType);

    if (factoryToRemove.instance != null) {
      if (disposingFunction != null) {
        disposingFunction?.call(factoryToRemove.instance as T);
      } else {
        factoryToRemove.dispose();
      }
    }
  }

  /// Clears the instance of a lazy singleton,
  /// being able to call the factory function on the next call
  /// of [get] on that type again.
  /// you select the lazy Singleton you want to reset by either providing
  /// an [instance], its registered type [T] or its registration name.
  /// if you need to dispose some resources before the reset, you can
  /// provide a [disposingFunction]
  @override
  void resetLazySingleton<T>(
      {Object instance,
      String instanceName,
      void Function(T) disposingFunction}) {
    _ServiceFactory factoryToReset;

    if (instance != null) {
      factoryToReset = _findFactoryByInstance(instance);
    } else {
      factoryToReset = _findFactoryByNameAndType<T>(instanceName);
    }
    throwIfNot(
        factoryToReset.factoryType == _ServiceFactoryType.lazy,
        StateError(
            'There is no type ${instance.runtimeType} registered as LazySingleton in GetIt'));

    if (factoryToReset.instance != null) {
      if (disposingFunction != null) {
        disposingFunction?.call(factoryToReset.instance as T);
      } else {
        factoryToReset.dispose();
      }
    }

    factoryToReset.instance = null;
    factoryToReset.pendingResult = null;
    factoryToReset._readyCompleter = Completer();
  }

  List<_ServiceFactory> get _allFactories => _scopes
      .fold<List<_ServiceFactory>>([], (sum, x) => sum..addAll(x.allFactories));

  _ServiceFactory _findFirstFactoryByInstanceOrNull(Object instance) {
    final registeredFactories =
        _allFactories.where((x) => identical(x.instance, instance));
    return registeredFactories.isEmpty ? null : registeredFactories.first;
  }

  _ServiceFactory _findFactoryByInstance(Object instance) {
    final registeredFactory = _findFirstFactoryByInstanceOrNull(instance);

    throwIf(
      registeredFactory == null,
      StateError(
          'This instance of the type ${instance.runtimeType} is not available in GetIt '
          'If you have registered it as LazySingleton, are you sure you have used '
          'it at least once?'),
    );
    return registeredFactory;
  }

  /// Used to manually signal the ready state of a Singleton.
  /// If you want to use this mechanism you have to pass [signalsReady==true] when registering
  /// the Singleton.
  /// If [instance] has a value GetIt will search for the responsible Singleton
  /// and completes all futures that might be waited for by [isReady]
  /// If all waiting singletons have signalled ready the future you can get
  /// from [allReady] is automatically completed
  ///
  /// Typically this is used in this way inside the registered objects init
  /// method `GetIt.instance.signalReady(this);`
  ///
  /// if [instance] is `null` and no factory/singleton is waiting to be signalled this
  /// will complete the future you got from [allReady], so it can be used to globally
  /// giving a ready Signal
  ///
  /// Both ways are mutual exclusive, meaning either only use the global `signalReady()` and
  /// don't register a singleton to signal ready or use any async registrations
  ///
  /// Or use async registrations methods or let individual instances signal their ready
  /// state on their own.
  @override
  void signalReady(Object instance) {
    _ServiceFactory registeredInstance;
    if (instance != null) {
      registeredInstance = _findFactoryByInstance(instance);
      throwIf(
          registeredInstance == null,
          ArgumentError.value(instance,
              'There is no object type ${instance.runtimeType} registered in GetIt'));

      throwIfNot(
          registeredInstance.shouldSignalReady,
          ArgumentError.value(instance,
              'This instance of type ${instance.runtimeType} is not supposed to be signalled.\nDid you forget to set signalsReady==true when registering it?'));

      throwIf(
          registeredInstance.isReady,
          StateError(
              'This instance of type ${instance.runtimeType} was already signalled'));

      registeredInstance._readyCompleter.complete();
      registeredInstance.objectsWaiting.clear();
    } else {
      /// Manual signalReady without an instance

      /// In case that there are still factories that are marked to wait for a signal
      /// but aren't signalled we throw an error with details which objects are concerned
      final notReady = _allFactories
          .where((x) =>
              (x.shouldSignalReady) && (!x.isReady) ||
              (x.pendingResult != null) && (!x.isReady))
          .map<String>((x) => '${x.registrationType}/${x.instanceName}')
          .toList();
      throwIf(
          notReady.isNotEmpty,
          StateError(
              "You can't signal ready manually if you have registered instances that should signal ready or are asnyc.\n"
              // this lint is stupif because it doesn't recognize newlines
              // ignore: missing_whitespace_between_adjacent_strings
              'Did you forget to pass an object instance?'
              'This registered types/names: $notReady should signal ready but are not ready'));

      _globalReadyCompleter.complete();
    }
  }

  /// returns a Future that completes if all asynchronously created Singletons and any Singleton that had
  ///  [signalsReady==true] are ready.
  /// This can be used inside a FutureBuilder to change the UI as soon as all initialization
  /// is done
  /// If you pass a [timeout], an [WaitingTimeOutException] will be thrown if not all Singletons
  /// were ready in the given time. The Exception contains details on which Singletons are not ready yet.
  @override
  Future<void> allReady(
      {Duration timeout, bool ignorePendingAsyncCreation = false}) {
    final futures = FutureGroup();
    _allFactories
        .where((x) =>
            (x.isAsync && !ignorePendingAsyncCreation ||
                (!x.isAsync &&
                    x.pendingResult != null) || // Singletons with dependencies
                x.shouldSignalReady) &&
            !x.isReady &&
            x.factoryType == _ServiceFactoryType.constant)
        .forEach((f) {
      futures.add(f._readyCompleter.future);
    });
    futures.close();
    if (timeout != null) {
      return futures.future.timeout(timeout, onTimeout: _throwTimeoutError);
    } else {
      return futures.future;
    }
  }

  /// Returns if all async Singletons are ready without waiting
  /// if [allReady] should not wait for the completion of async Signletons set
  /// [ignorePendingAsyncCreation==true]
  @override
  bool allReadySync([bool ignorePendingAsyncCreation = false]) {
    final notReadyTypes = _allFactories
        .where((x) =>
            (x.isAsync && !ignorePendingAsyncCreation ||
                    (!x.isAsync &&
                        x.pendingResult !=
                            null) || // Singletons with dependencies
                    x.shouldSignalReady) &&
                !x.isReady &&
                x.factoryType == _ServiceFactoryType.constant ||
            x.factoryType == _ServiceFactoryType.lazy)
        .map<String>((x) {
      if (x.isNamedRegistration) {
        return 'Object ${x.instanceName} has not completed';
      } else {
        return 'Registered object of Type ${x.registrationType.toString()} has not completed';
      }
    }).toList();

    /// IN debug mode we print the List of not ready types/named instances
    if (notReadyTypes.isNotEmpty) {
      // Hack to only output this in debug mode;
      assert(() {
        // ignore: avoid_print
        print('Not yet ready objects:');
        // ignore: avoid_print
        print(notReadyTypes);
        return true;
      }());
    }
    return notReadyTypes.isEmpty;
  }

  /// we use dynamic here because this functoin is used at two places, one that expects a Future
  /// the other a List. As this function just throws an exception and doesn't return anything
  /// this is save
  List _throwTimeoutError() {
    final allFactories = _allFactories;
    final waitedBy = Map.fromEntries(
      allFactories
          .where((x) =>
              (x.shouldSignalReady || x.pendingResult != null) &&
              !x.isReady &&
              x.objectsWaiting.isNotEmpty)
          .map<MapEntry<String, List<String>>>(
            (isWaitedFor) => MapEntry(
                isWaitedFor.debugName,
                isWaitedFor.objectsWaiting
                    .map((waitedByType) => waitedByType.toString())
                    .toList()),
          ),
    );
    final notReady = allFactories
        .where((x) =>
            (x.shouldSignalReady || x.pendingResult != null) && !x.isReady)
        .map((f) => f.debugName)
        .toList();
    final areReady = allFactories
        .where((x) =>
            (x.shouldSignalReady || x.pendingResult != null) && x.isReady)
        .map((f) => f.debugName)
        .toList();

    throw WaitingTimeOutException(waitedBy, notReady, areReady);
  }

  /// Returns a Future that completes if the instance of an Singleton, defined by Type [T] or
  /// by name [instanceName] or by passing the an existing [instance],  is ready
  /// If you pass a [timeout], an [WaitingTimeOutException] will be thrown if the instance
  /// is not ready in the given time. The Exception contains details on which Singletons are
  /// not ready at that time.
  /// [callee] optional parameter which makes debugging easier. Pass `this` in here.
  @override
  Future<void> isReady<T>({
    Object instance,
    String instanceName,
    Duration timeout,
    Object callee,
  }) {
    _ServiceFactory factoryToCheck;
    if (instance != null) {
      factoryToCheck = _findFactoryByInstance(instance);
    } else {
      factoryToCheck = _findFactoryByNameAndType<T>(instanceName);
    }
    throwIfNot(
      factoryToCheck.canBeWaitedFor &&
          factoryToCheck.factoryType != _ServiceFactoryType.alwaysNew,
      ArgumentError(
          'You only can use this function on Singletons that are async, that are marked as dependen '
          'or that are marked with "signalsReady==true"'),
    );
    factoryToCheck.objectsWaiting.add(callee.runtimeType);
    if (factoryToCheck.isAsync &&
        factoryToCheck.factoryType == _ServiceFactoryType.lazy &&
        factoryToCheck.instance == null) {
      if (timeout != null) {
        return factoryToCheck.getObjectAsync(null, null).timeout(timeout,
            onTimeout: () {
          _throwTimeoutError();
          return null;
        });
      } else {
        return factoryToCheck.getObjectAsync(null, null);
      }
    }
    if (timeout != null) {
      return factoryToCheck._readyCompleter.future
          .timeout(timeout, onTimeout: _throwTimeoutError);
    } else {
      return factoryToCheck._readyCompleter.future;
    }
  }

  /// Checks if an async Singleton defined by an [instance], a type [T] or an [instanceName]
  /// is ready without waiting.
  @override
  bool isReadySync<T>({Object instance, String instanceName}) {
    _ServiceFactory factoryToCheck;
    if (instance != null) {
      factoryToCheck = _findFactoryByInstance(instance);
    } else {
      factoryToCheck = _findFactoryByNameAndType<T>(instanceName);
    }
    throwIfNot(
        factoryToCheck.canBeWaitedFor &&
            factoryToCheck.factoryType != _ServiceFactoryType.alwaysNew,
        ArgumentError(
            'You only can use this function on async Singletons or Singletons '
            'that have ben marked with "signalsReady" or that they depend on others'));
    return factoryToCheck.isReady;
  }
}
