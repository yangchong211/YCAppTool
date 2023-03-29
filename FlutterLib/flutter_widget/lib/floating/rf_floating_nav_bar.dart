import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_widget/floating/rf_floating_text_attr.dart';

class RFFloatingNavBar extends StatelessWidget {
  final double ICON_SIZE = 48;
  final double ICON_MARGIN = 48;
  final double BUTTON_WIDTH = 180;
  final double BUTTON_MARGIN = 48;

  final RFFloatingTextAttr title;
  final RFFloatingTextAttr leftIcon;
  final RFFloatingTextAttr rightBtn;
  final Color bgColor;

  RFFloatingNavBar({
    @required this.title,
    this.leftIcon,
    this.rightBtn,
    this.bgColor,
  })  : assert(title.text != null),
        assert(leftIcon == null || leftIcon.iconPoint != null),
        assert(rightBtn == null || rightBtn.text != null);

  @override
  Widget build(BuildContext context) {
    num maxSize;

    // 设置最大宽度
    if (rightBtn != null) {
      maxSize = BUTTON_WIDTH + BUTTON_MARGIN;
    } else {
      maxSize = ICON_SIZE + ICON_MARGIN;
    }

    return Container(
      color: bgColor ?? Colors.white,
      height: 112,
      padding: EdgeInsets.symmetric(
        horizontal: 10,
      ),
      child: Row(
        children: [
          _buildLeftIcon(maxSize),
          _buildTitle(),
          _buildRightButton(maxSize),
        ],
      ),
    );
  }

  // 左边 icon
  Widget _buildLeftIcon(num maxWidth) {
    Widget iconWidget;
    if (leftIcon != null) {
      iconWidget = GestureDetector(
        child: Icon(Icons.add,
          size: ICON_SIZE,
        ),
        onTap: leftIcon.onTap,
      );
    }

    return Container(
      width: maxWidth,
      alignment: Alignment.centerLeft,
      child: iconWidget,
    );
  }

  // 顶部 title
  Widget _buildTitle() {
    return Expanded(
      child: Text(
        title.text,
        maxLines: 2,
        softWrap: true,
        textAlign: TextAlign.center,
        overflow: TextOverflow.ellipsis,
        style: TextStyle(
          color: title.color ?? Colors.black,
          fontSize: 18,
        ),
      ),
    );
  }

  // 右侧按钮
  Widget _buildRightButton(num maxWidth) {
    Widget btnWidget;

    if (rightBtn != null) {
      btnWidget = Container(
        width: BUTTON_WIDTH,
        alignment: Alignment.centerRight,
        child: GestureDetector(
          behavior: HitTestBehavior.opaque,
          child: Text(
            rightBtn.text,
            overflow: TextOverflow.ellipsis,
            style: TextStyle(
              color: rightBtn.color ?? Colors.black,
              fontSize: 28,
            ),
          ),
          onTap: rightBtn.onTap,
        ),
      );
    }

    return Container(
      width: maxWidth,
      child: btnWidget,
    );
  }
}
