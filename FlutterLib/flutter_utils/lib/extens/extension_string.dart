


import 'package:yc_flutter_utils/extens/transform_utils.dart';
import 'package:yc_flutter_utils/extens/validator_utils.dart';
import 'package:yc_flutter_utils/num/num_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

extension ExtensionString on String {

  bool isNull() {
    return this == null || this.isEmpty || ObjectUtils.isNull(this);
  }

  bool isNotNullOrEmpty() {
    return this != null && this.isNotEmpty;
  }

  /// Checks if data is null or Blank (empty or only contains whitespace).
  bool isNullOrBlank(){
    return isNotNullOrEmpty() && ObjectUtils.isNullOrBlank(this);
  }

  /// Checks if string is num (int or double).
  bool isNum(){
    return isNotNullOrEmpty() && NumUtils.isNum(this);
  }

  /// Checks if string is numeric only.
  /// Numeric only doesnt accepting "." which double data type have
  bool isNumericOnly() {
    return ValidatorUtils.isNumericOnly(this);
  }

  /// Checks if string consist only Alphabet. (No Whitespace)
  bool isAlphabetOnly(String s) {
    return ValidatorUtils.isAlphabetOnly(s);
  }

  /// Checks if string is boolean.
  bool isBool() {
    return ValidatorUtils.isBool(this);
  }

  /// Checks if string is vector image file path.
  bool isVector() {
    return ValidatorUtils.isVector(this);
  }

  /// Checks if string is image file path.
  bool isImage() {
    return ValidatorUtils.isImage(this);
  }

  /// Checks if string is audio file path.
  bool isAudio() {
    return ValidatorUtils.isAudio(this);
  }

  /// Checks if string is video file path.
  bool isVideo() {
    return ValidatorUtils.isVideo(this);
  }

  /// Checks if string is txt file path.
  bool isTxt() {
    return ValidatorUtils.isTxt(this);
  }

  /// Checks if string is Doc file path.
  bool isDocument() => ValidatorUtils.isDocument(this);

  /// Checks if string is Excel file path.
  bool isExcel() => ValidatorUtils.isExcel(this);

  /// Checks if string is powerpoint file path.
  bool isPPT() => ValidatorUtils.isPPT(this);

  /// Checks if string is APK file path.
  bool isApk() => ValidatorUtils.isAPK(this);

  /// Checks if string is PDF file path.
  bool isPDF() => ValidatorUtils.isPDF(this);

  /// Checks if string is html file path.
  bool isHTML() => ValidatorUtils.isHTML(this);

  /// Checks if string is URL.
  bool isURL() => ValidatorUtils.isURL(this);

  /// Checks if string is DateTime (UTC or Iso8601).
  bool isDateTime() => ValidatorUtils.isDateTime(this);

  /// Checks if string is email.
  bool isEmail({int minLength, int maxLength}) =>
      ValidatorUtils.isEmail(this) &&
          ((minLength != null)
              ? ValidatorUtils.isLengthGreaterOrEqual(this, minLength)
              : true) &&
          ((maxLength != null)
              ? ValidatorUtils.isLengthLowerOrEqual(this, maxLength)
              : true);

  /// Checks if string is MD5
  bool isMD5() => ValidatorUtils.isMD5(this);

  /// Checks if string is SHA251.
  bool isSHA1() => ValidatorUtils.isSHA1(this);

  /// Checks if string is SHA256.
  bool isSHA256() => ValidatorUtils.isSHA256(this);

  /// Checks if string is Palindrom.
  bool isPalindrom() => ValidatorUtils.isPalindrome(this);

  /// Checks if all character inside string are same.
  /// Example: 111111 -> true, wwwww -> true
  bool isOneAKind() => ValidatorUtils.isOneAKind(this);

  /// Checks if string is IPv4.
  bool isIPv4() => ValidatorUtils.isIPv4(this);

  /// Checks if string is IPv6.
  bool isIPv6() => ValidatorUtils.isIPv6(this);

  /// Checks if length of string is LOWER than maxLength.
  bool isLengthLowerThan(int maxLength) =>
      ValidatorUtils.isLengthLowerThan(this, maxLength);

  /// Checks if length of string is LOWER OR EQUAL to maxLength.
  bool isLengthLowerOrEqual(int maxLength) =>
      ValidatorUtils.isLengthLowerOrEqual(this, maxLength);

  /// Checks if length of string is GREATER than maxLength.
  bool isLengthGreaterThan(int maxLength) =>
      ValidatorUtils.isLengthGreaterThan(this, maxLength);

  /// Checks if length of string is GREATER OR EQUAL to maxLength.
  bool isLengthGreaterOrEqual(int maxLength) =>
      ValidatorUtils.isLengthGreaterOrEqual(this, maxLength);

  /// Checks if length of string is EQUAL than maxLength.
  bool isLengthEqualTo(int maxLength) =>
      ValidatorUtils.isLengthEqualTo(this, maxLength);

  /// Checks if length of string is BETWEEN minLength to maxLength.
  bool isLengthBetween(int minLength, int maxLength) =>
      ValidatorUtils.isLengthBetween(this, minLength, maxLength);

  /// Checks if a contains b (Treating or interpreting upper- and lowercase letters as being the same).
  bool isCaseInsensitiveContains(String compareTo) =>
      ValidatorUtils.isCaseInsensitiveContains(this, compareTo);

  /// Checks if a contains b or b contains a (Treating or interpreting upper- and lowercase letters as being the same).
  bool isCaseInsensitiveContainsAny(String compareTo) =>
      ValidatorUtils.isCaseInsensitiveContainsAny(this, compareTo);

  /// Checks if string value is camelcase.
  bool isCamelCase() => ValidatorUtils.isCamelCase(this);

  /// Checks if string value is capitalize.
  bool isCapitalize({bool firstOnly = false}) =>
      ValidatorUtils.isCapitalize(this, firstOnly: firstOnly);

  /// Transform string to int type
  int toInt({bool nullOnError = false}) {
    int i = int.tryParse(this);
    if (i != null) return i;
    if (nullOnError) return null;
    throw FormatException('Can only acception double value');
  }

  /// Transform string to double type
  double toDouble({bool nullOnError = false}) {
    double d = double.tryParse(this);
    if (d != null) return d;
    if (nullOnError) return null;
    throw FormatException('Can only acception double value');
  }

  /// Transform string to num type
  num toNum({bool nullOnError = false}) =>
      nullOnError ? num.tryParse(this) : num.parse(this);

  /// Transform string to boolean type
  bool toBool({bool falseOnError = false}) {
    if (ValidatorUtils.isBool(this)) return this == 'true';
    if (falseOnError) return false;
    throw FormatException('Can only acception boolean value');
  }

  /// Transform String millisecondsSinceEpoch (DateTime) to DateTime
  DateTime toDateTime() {
    int miliseconds = this.toInt(nullOnError: true);
    if (ObjectUtils.isNullOrBlank(miliseconds)) return null;
    return DateTime.fromMillisecondsSinceEpoch(miliseconds);
  }

  /// Transform string value to binary
  /// Example: 15 => 1111
  String toBinary({bool nullOnError = false}) {
    if (!NumUtils.isNum(this)) {
      if (nullOnError) return null;
      throw FormatException("Only accepting integer value");
    }
    return TransformUtils.toBinary(int.parse(this));
  }

  /// Capitalize each word inside string
  /// Example: your name => Your Name, your name => Your name
  ///
  /// If First Only is `true`, the only letter get uppercase is the first letter
  String toCapitalize({bool firstOnly = false}) =>
      TransformUtils.capitalize(this, firstOnly: firstOnly);

  /// Capitalize each word inside string
  /// Example: your name => yourName
  String toCamelCase() => TransformUtils.camelCase(this);

  /// Extract numeric value of string
  /// Example: OTP 12312 27/04/2020 => 1231227042020ß
  /// If firstword only is true, then the example return is "12312" (first found numeric word)
  String toNumericOnly() => TransformUtils.numericOnly(this);
}
