

///判断区域工具类
class CountryUtils {
  ///墨西哥
  static final String MEXICO = "MX";
  ///巴西
  static final String BRAZIL = "BR";
  ///日本
  static final String JAPAN = "JP";
  ///哥斯达黎加
  static final String COSTA_RICA = "CR";
  ///哥伦比亚
  static final String COLOMBIA = "CO";
  ///多米尼加
  static final String DOMINICA = "DO";
  ///智利
  static final String CHILE = "CL";

  /**
   * 是否是墨西哥
   * @param country 国家简写
   * @return
   */
  static bool isMexico(String country) {
    if (_isEmpty(country)) {
      return false;
    }
    return MEXICO == country;
  }

  /**
   * 是否是日本
   * @param country 国家简写
   * @return
   */
  static bool isJapan(String country) {
    if (_isEmpty(country)) {
      return false;
    }
    return JAPAN == country;
  }

  /**
   * 是否是巴西
   * @param country 国家简写
   * @return
   */
  static bool isBrazil(String country) {
    if (_isEmpty(country)) {
      return false;
    }
    return BRAZIL == country;
  }

  /**
   * 是否是哥斯达黎加
   * @param country 国家简写
   * @return
   */
  static bool isCostaRica(String country) {
    if (_isEmpty(country)) {
      return false;
    }
    return COSTA_RICA == country;
  }

  /**
   * 是否是哥伦比亚
   * @param country 国家简写
   * @return
   */
  static bool isColombia(String country) {
    if (_isEmpty(country)) {
      return false;
    }
    return COLOMBIA == country;
  }

  /**
   * 是否是多米尼加
   * @param country 国家简写
   * @return
   */
  static bool isDominica(String country) {
    if (_isEmpty(country)) {
      return false;
    }
    return DOMINICA == country;
  }

  /**
   * 是否是智利
   * @param country 国家简写
   * @return
   */
  static bool isChile(String country) {
    if (_isEmpty(country)) {
      return false;
    }
    return CHILE == country;
  }

  /**
   * 是否为空
   * @param country 国家简写
   * @return
   */
  static bool _isEmpty(String country) {
    if (country == null || country.length == 0) {
      return true;
    }
    return false;
  }
}
