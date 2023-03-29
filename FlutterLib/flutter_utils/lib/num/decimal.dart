


import 'package:yc_flutter_utils/num/rational.dart';

class Decimal implements Comparable<Decimal> {
  factory Decimal.parse(String value) =>
      Decimal._fromRational(Rational.parse(value));

  factory Decimal.fromInt(int value) =>
      Decimal._fromRational(Rational(BigInt.from(value)));

  Decimal._fromRational(this._rational);

  static Decimal zero = Decimal.fromInt(0);
  static Decimal one = Decimal.fromInt(1);

  static Decimal tryParse(String value) {
    try {
      return Decimal.parse(value);
    } on FormatException {
      return null;
    }
  }

  Rational _rational;

  bool get isInteger => _rational.isInteger;

  Decimal get inverse => Decimal._fromRational(_rational.inverse);

  @override
  bool operator ==(Object other) =>
      other is Decimal && _rational == other._rational;

  @override
  int get hashCode => _rational.hashCode;

  @override
  String toString() => _rational.toDecimalString();

  // implementation of Comparable

  @override
  int compareTo(Decimal other) => _rational.compareTo(other._rational);

  // implementation of num

  Decimal operator +(Decimal other) =>
      Decimal._fromRational(_rational + other._rational);

  Decimal operator -(Decimal other) =>
      Decimal._fromRational(_rational - other._rational);

  Decimal operator *(Decimal other) =>
      Decimal._fromRational(_rational * other._rational);

  Decimal operator %(Decimal other) =>
      Decimal._fromRational(_rational % other._rational);

  Decimal operator /(Decimal other) =>
      Decimal._fromRational(_rational / other._rational);

  /// Truncating division operator.
  ///
  /// The result of the truncating division [:a ~/ b:] is equivalent to
  /// [:(a / b).truncate():].
  Decimal operator ~/(Decimal other) =>
      Decimal._fromRational(_rational ~/ other._rational);

  Decimal operator -() => Decimal._fromRational(-_rational);

  /// Return the remainder from dividing this [num] by [other].
  Decimal remainder(Decimal other) =>
      Decimal._fromRational(_rational.remainder(other._rational));

  /// Relational less than operator.
  bool operator <(Decimal other) => _rational < other._rational;

  /// Relational less than or equal operator.
  bool operator <=(Decimal other) => _rational <= other._rational;

  /// Relational greater than operator.
  bool operator >(Decimal other) => _rational > other._rational;

  /// Relational greater than or equal operator.
  bool operator >=(Decimal other) => _rational >= other._rational;

  bool get isNaN => _rational.isNaN;

  bool get isNegative => _rational.isNegative;

  bool get isInfinite => _rational.isInfinite;

  /// Returns the absolute value of this [num].
  Decimal abs() => Decimal._fromRational(_rational.abs());

  /// The signum function value of this [num].
  ///
  /// E.e. -1, 0 or 1 as the value of this [num] is negative, zero or positive.
  int get signum => _rational.signum;

  /// Returns the greatest integer value no greater than this [num].
  Decimal floor() => Decimal._fromRational(_rational.floor());

  /// Returns the least integer value that is no smaller than this [num].
  Decimal ceil() => Decimal._fromRational(_rational.ceil());

  /// Returns the integer value closest to this [num].
  ///
  /// Rounds away from zero when there is no closest integer:
  /// [:(3.5).round() == 4:] and [:(-3.5).round() == -4:].
  Decimal round() => Decimal._fromRational(_rational.round());

  /// Returns the integer value obtained by discarding any fractional digits
  /// from this [num].
  Decimal truncate() => Decimal._fromRational(_rational.truncate());

  /// Returns the integer value closest to `this`.
  ///
  /// Rounds away from zero when there is no closest integer:
  /// [:(3.5).round() == 4:] and [:(-3.5).round() == -4:].
  ///
  /// The result is a double.
  double roundToDouble() => _rational.roundToDouble();

  /// Returns the greatest integer value no greater than `this`.
  ///
  /// The result is a double.
  double floorToDouble() => _rational.floorToDouble();

  /// Returns the least integer value no smaller than `this`.
  ///
  /// The result is a double.
  double ceilToDouble() => _rational.ceilToDouble();

  /// Returns the integer obtained by discarding any fractional digits from
  /// `this`.
  ///
  /// The result is a double.
  double truncateToDouble() => _rational.truncateToDouble();

  /// Clamps `this` to be in the range [lowerLimit]-[upperLimit]. The comparison
  /// is done using [compareTo] and therefore takes [:-0.0:] into account.
  Decimal clamp(Decimal lowerLimit, Decimal upperLimit) =>
      Decimal._fromRational(
          _rational.clamp(lowerLimit._rational, upperLimit._rational));

  /// Truncates this [num] to an integer and returns the result as an [int].
  int toInt() => _rational.toInt();

  /// Return this [num] as a [double].
  ///
  /// If the number is not representable as a [double], an approximation is
  /// returned. For numerically large integers, the approximation may be
  /// infinite.
  double toDouble() => _rational.toDouble();

  /// Inspect if this [num] has a finite precision.
  bool get hasFinitePrecision => _rational.hasFinitePrecision;

  /// The precision of this [num].
  ///
  /// The sum of the number of digits before and after the decimal point.
  ///
  /// Throws [StateError] if the precision is infinite, i.e. when
  /// [hasFinitePrecision] is `false`.
  int get precision => _rational.precision;

  /// The scale of this [num].
  ///
  /// The number of digits after the decimal point.
  ///
  /// Throws [StateError] if the scale is infinite, i.e. when
  /// [hasFinitePrecision] is `false`.
  int get scale => _rational.scale;

  /// Converts a [num] to a string representation with [fractionDigits] digits
  /// after the decimal point.
  String toStringAsFixed(int fractionDigits) =>
      _rational.toStringAsFixed(fractionDigits);

  /// Converts a [num] to a string in decimal exponential notation with
  /// [fractionDigits] digits after the decimal point.
  String toStringAsExponential([int fractionDigits]) =>
      _rational.toStringAsExponential(fractionDigits);

  /// Converts a [num] to a string representation with [precision] significant
  /// digits.
  String toStringAsPrecision(int precision) =>
      _rational.toStringAsPrecision(precision);

  /// Returns `this` to the power of [exponent].
  ///
  /// Returns [one] if the [exponent] equals `0`.
  ///
  /// The [exponent] must otherwise be positive.
  Decimal pow(int exponent) => Decimal._fromRational(_rational.pow(exponent));
}
