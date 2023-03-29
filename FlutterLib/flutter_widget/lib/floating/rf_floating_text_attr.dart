import 'package:flutter/widgets.dart';

class RFFloatingTextAttr {
  final int iconPoint;
  final String text;
  final Color color;
  final GestureTapCallback onTap;

  RFFloatingTextAttr.icon({
    @required this.iconPoint,
    this.onTap,
    this.color,
  }) : text = null;

  RFFloatingTextAttr.text({
    @required this.text,
    this.onTap,
    this.color,
  }) : iconPoint = null;
}
