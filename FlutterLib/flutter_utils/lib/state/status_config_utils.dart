
class StateConfigUtils{

  static StateConfigUtils _instance;

  StateConfigUtils._();

  factory StateConfigUtils() {
    return _instance;
  }

  String loadingImage;
  String netErrorImage;
  String dataErrorImage;
  String emptyImage;

  static void init( String loadingImage,) {

  }


}